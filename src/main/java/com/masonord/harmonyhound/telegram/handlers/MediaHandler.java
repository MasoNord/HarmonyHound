package com.masonord.harmonyhound.telegram.handlers;

import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import com.masonord.harmonyhound.exception.ExceedFileDurationException;
import com.masonord.harmonyhound.exception.ExceedFileSizeLimitException;
import com.masonord.harmonyhound.exception.FileTooShortException;
import com.masonord.harmonyhound.exception.SongNotFoundException;
import com.masonord.harmonyhound.response.rapidapi.Metadata;
import com.masonord.harmonyhound.response.rapidapi.Metapages;
import com.masonord.harmonyhound.response.rapidapi.Sections;
import com.masonord.harmonyhound.response.telegram.FilePathResponse;
import com.masonord.harmonyhound.response.rapidapi.RecognizedSongResponse;
import com.masonord.harmonyhound.service.GoogleDriveService;
import com.masonord.harmonyhound.service.RecognizeMediaService;
import com.masonord.harmonyhound.util.DownloadUtil;
import com.masonord.harmonyhound.util.FileSystemUtil;
import com.masonord.harmonyhound.util.LanguageUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class MediaHandler {
    private final double MIN_LENGTH = 3.00;
    private final double MAX_LENGTH = 10.00;

    @Autowired
    private DownloadUtil downloadUtil;

    @Autowired
    private RecognizeMediaService recognizeMediaService;

    @Autowired
    private MediaUtil mediaUtil;

    @Autowired
    private GoogleDriveService googleDriveService;

    @Autowired
    private FileSystemUtil fileSystemUtil;

    private final Gson gson;

    private final LanguageUtil languageUtil;

    public MediaHandler() {
        this.gson = new Gson();
        this.languageUtil = new LanguageUtil();
    }

    public BotApiMethod<?> answerMessage(Message message) throws IOException, InterruptedException, URISyntaxException, GeneralSecurityException, SongNotFoundException, ExceedFileSizeLimitException {
        String chatId = message.getChatId().toString();
        String in, out;
        FilePathResponse response;

        if (message.hasVideo()) {
            response = downloadUtil.download(message.getVideo().getFileId(), chatId);
        }else if (message.hasAudio()) {
            response = downloadUtil.download(message.getAudio().getFileId(), chatId);
        }else if (message.hasVoice()) {
            response = downloadUtil.download(message.getVoice().getFileId(), chatId);
        }else {
            response = downloadUtil.download(message.getVideoNote().getFileId(), chatId);
        }
        
        in = "downloaded-media/chat_" + chatId + "/" + response.getResult().getFile_path().split("/")[1];
        out = "downloaded-media/chat_" + chatId + "/" + response.getResult().getFile_path().split("/")[1].split("\\.")[0] + ".wav";

        mediaUtil.convertFileToWav(in, out);

        double fileLength = mediaUtil.getAudioDuration(out);

        try {
            if (fileLength < MIN_LENGTH) {
                throw new FileTooShortException(languageUtil.getProperty("file.too.short"));
            }else if (fileLength > MAX_LENGTH) {
                throw new ExceedFileDurationException(languageUtil.getProperty("file.duration.exceeded"));
            }
        }catch (FileTooShortException | ExceedFileDurationException e) {
            fileSystemUtil.deleteFile(in);
            fileSystemUtil.deleteFile(out);
        }


        File fileLink = googleDriveService.uploadFile(out, chatId);
        RecognizedSongResponse recognizedAudio = recognizeMediaService.recognizeAudio(fileLink.getWebViewLink());
        googleDriveService.deleteFile(fileLink.getId());
        fileSystemUtil.deleteFile(in);
        fileSystemUtil.deleteFile(out);

        if (recognizedAudio.getTrack() == null || recognizedAudio.getMatches().isEmpty()) {
            throw new SongNotFoundException(languageUtil.getProperty("song.not.found"));
        }

        return getSendMessage(chatId, recognizedAudio);
    }

    private SendMessage getSendMessage(String chatId, RecognizedSongResponse recognizedAudio) {
        SendMessage sendMessage = new SendMessage();
        List<Sections> sections = recognizedAudio.getTrack().getSections();
        List<Metapages> metapages = (sections.get(0).getMetapages().isEmpty() ? null : sections.get(0).getMetapages());
        List<Metadata> metadata = (sections.get(0).getMetadata().isEmpty() ? null : sections.get(0).getMetadata());
        String genre = recognizedAudio.getTrack().getGenres().getPrimary();

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(
                "Accuracy match - " + (recognizedAudio.getLocation().getAccuracy() * 10000) + "%\n\n" +
                "*" + recognizedAudio.getTrack().getTitle() + "*"+ "\n\n" +
                (metadata == null ? "" : "Album: " + metadata.get(0).getText() + '\n') +
                (metadata == null ? "" : "Label: " + metadata.get(1).getText() + '\n') +
                (metapages == null ? "" : "Artist: " + metapages.get(0).getCaption() + '\n') +
                (genre == null ? "" : "Genre: " + genre + '\n') +
                (metadata == null ? "" : "Released: " + metadata.get(2).getText()  + '\n') +
                "*Shazam: *" + recognizedAudio.getTrack().getUrl() + "\n"
        );
        return sendMessage;
    }
}
