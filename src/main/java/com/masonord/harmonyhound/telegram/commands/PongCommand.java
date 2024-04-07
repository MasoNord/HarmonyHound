package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class PongCommand implements Command{
    private final String chatId;
    private final LanguageUtil languageUtil;

    public PongCommand(String chatId) {
        this.chatId = chatId;
        this.languageUtil = new LanguageUtil();
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage(chatId, languageUtil.getProperty("pong"));
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
