package com.masonord.harmonyhound.telegram.handlers;

import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import com.masonord.harmonyhound.response.rapidapi.Sections;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import com.masonord.harmonyhound.response.rapidapi.RecognizedSongResponse;
import com.masonord.harmonyhound.response.videoresponse.YoutubeResponse;
import com.masonord.harmonyhound.service.GoogleDriveService;
import com.masonord.harmonyhound.service.RecognizeMediaService;
import com.masonord.harmonyhound.util.DownloadUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;

@Component
public class MediaHandler {
    @Autowired
    private DownloadUtil downloadUtil;

    @Autowired
    private RecognizeMediaService recognizeMediaService;

    @Autowired
    private MediaUtil mediaUtil;

    @Autowired
    private GoogleDriveService googleDriveService;

    private final Gson gson;

    public MediaHandler() {
        this.gson = new Gson();
    }

    public BotApiMethod<?> answerMessage(Message message) throws IOException, InterruptedException, URISyntaxException, GeneralSecurityException {
        String chatId = message.getChatId().toString();

        FilePathResponse response;

        if (message.hasVideo()) {
            response =  downloadUtil.download(message.getVideo().getFileId(), chatId);
        }else if (message.hasAudio()) {
            response = downloadUtil.download(message.getAudio().getFileId(), chatId);
        }else {
            response = downloadUtil.download(message.getVoice().getFileId(), chatId);
        }

        File fileLink = googleDriveService.uploadFile(response, chatId);
        RecognizedSongResponse recognizedAudio = recognizeMediaService.recognizeAudio(fileLink.getWebViewLink());
        googleDriveService.deleteFile(fileLink.getId());

        YoutubeResponse youtubeResponse = null;
        for (Sections s : recognizedAudio.getTrack().getSections()) {
            if (s.getYoutubeurl() != null) {
                youtubeResponse = getYoutubeResponse(s.getYoutubeurl());
            }
        }

        return getSendMessage(chatId, recognizedAudio, youtubeResponse);
    }

    private SendMessage getSendMessage(String chatId, RecognizedSongResponse recognizedAudio, YoutubeResponse youtubeResponse) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                "Accuracy match - " + (recognizedAudio.getLocation().getAccuracy() * 10000) + "%\n\n" +
                "*" + recognizedAudio.getTrack().getTitle() + "*"+ "\n\n" +
                "*Album: *" + recognizedAudio.getTrack().getSections().get(0).getMetadata().get(0).getText() + "\n" +
                "*Label: *" + recognizedAudio.getTrack().getSections().get(0).getMetadata().get(1).getText() + "\n" +
                "*Artist: *" + recognizedAudio.getTrack().getSections().get(0).getMetapages().get(0).getCaption() + "\n" +
                "*Genre: *" + recognizedAudio.getTrack().getGenres().getPrimary() + "\n\n" +
                "*Released: *" + recognizedAudio.getTrack().getSections().get(0).getMetadata().get(2).getText() + "\n\n" +
                "*Shazam: *" + recognizedAudio.getTrack().getUrl() + "\n" +
                (youtubeResponse == null ? "" : "*Youtube: *" + youtubeResponse.getActions().get(0).getUri()) + "\n"
        );
        return sendMessage;
    }

    private YoutubeResponse getYoutubeResponse(String httpLink) throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(httpLink);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), YoutubeResponse.class);
    }
}
