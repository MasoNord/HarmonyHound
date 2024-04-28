package com.masonord.harmonyhound.telegram.handlers;

import com.masonord.harmonyhound.response.FilePathResponse;
import com.masonord.harmonyhound.response.GetAudioRecognitionResult;
import com.masonord.harmonyhound.service.RecognizeMediaService;
import com.masonord.harmonyhound.util.DownloadUtil;
import com.masonord.harmonyhound.util.MediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MediaHandler {
    @Autowired
    private DownloadUtil downloadUtil;

    @Autowired
    private RecognizeMediaService recognizeMediaService;

    @Autowired
    private MediaUtil mediaUtil;

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();

        FilePathResponse response;
        if (message.hasVideo()) {
            response =  downloadUtil.download(message.getVideo().getFileId(), chatId);
        }else if (message.hasAudio()) {
            response = downloadUtil.download(message.getAudio().getFileId(), chatId);
        }else {
            response = downloadUtil.download(message.getVoice().getFileId(), chatId);
        }

        GetAudioRecognitionResult result = recognizeMediaService.recognizeAudio(chatId, response);

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(result.getData().get(0).getTracks().get(0).get(0));
        return sendMessage;
    }
}
