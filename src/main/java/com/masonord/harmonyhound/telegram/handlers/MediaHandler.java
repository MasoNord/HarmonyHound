package com.masonord.harmonyhound.telegram.handlers;

import com.masonord.harmonyhound.util.MediaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MediaHandler {

    @Autowired
    MediaUtil mediaUtil;

    private final ConcurrentHashMap<String, Queue<String>> songsToRecognize;

    public MediaHandler() {
        songsToRecognize = new ConcurrentHashMap<>();
    }

    public BotApiMethod<?> answerMessage(Message message) {
        String chatId = message.getChatId().toString();
        addNewChatId(chatId);

        if (message.hasVideo()) {
            mediaUtil.download(message.getVideo().getFileId());
        }else if (message.hasAudio()) {
            mediaUtil.download(message.getAudio().getFileId());
        }else {
            mediaUtil.download(message.getVoice().getFileId());
        }





        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    private void addNewChatId(String chatId) {
        if (!songsToRecognize.contains(chatId)) {
            Queue<String> songs = new ArrayDeque<>();
            songsToRecognize.put(chatId, songs);
        }
    }
}
