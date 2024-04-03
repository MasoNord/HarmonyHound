package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Locale;

public class ChangeLangCommand implements Command {

    private final String chatId;
    private final String lang;
    private final LanguageUtil languageUtil;

    public ChangeLangCommand(String chatId, String lang) {
        this.chatId = chatId;
        this.lang = lang;
        this.languageUtil = new LanguageUtil();
    }

    @Override
    public BotApiMethod<?> execute() {
        languageUtil.setLanguage(Locale.forLanguageTag(languageUtil.getLanguages().get(lang)));
        SendMessage sendMessage = new SendMessage(chatId, languageUtil.getProperty("change.language"));
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
