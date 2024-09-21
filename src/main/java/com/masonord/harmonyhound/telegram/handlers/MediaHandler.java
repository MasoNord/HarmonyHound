package com.masonord.harmonyhound.telegram.handlers;

import com.google.api.services.drive.model.File;
import com.masonord.harmonyhound.response.FilePathResponse;
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
import java.net.URISyntaxException;
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
        String result = recognizeMediaService.recognizeAudio(fileLink.getWebViewLink());
        googleDriveService.deleteFile(fileLink.getId());


        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(result);

        return sendMessage;
    }
}
