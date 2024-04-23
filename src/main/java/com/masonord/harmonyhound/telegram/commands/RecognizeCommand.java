package com.masonord.harmonyhound.telegram.commands;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class RecognizeCommand implements Command{
    private final String chatId;
    private final Message message;

    public RecognizeCommand(String chatId, Message message) {
        this.chatId = chatId;
        this.message = message;
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Hello from recognize command");
        return sendMessage;
    }
}
