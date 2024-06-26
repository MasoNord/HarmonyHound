package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements Command{

    private final String chatId;
    private final LanguageUtil languageUtil;

    public HelpCommand(String chatId) {
        this.chatId = chatId;
        this.languageUtil = new LanguageUtil();
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage(chatId, languageUtil.getProperty("help"));
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
