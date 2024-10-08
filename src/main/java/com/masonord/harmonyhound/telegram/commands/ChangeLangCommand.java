package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.telegram.keyboards.KeyboardMaker;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ChangeLangCommand implements Command {

    private final String chatId;
    private final String lang;
    private final LanguageUtil languageUtil;
    private final KeyboardMaker keyboardMaker;

    public ChangeLangCommand(String chatId, String lang) {
        this.chatId = chatId;
        this.lang = lang;
        this.languageUtil = new LanguageUtil();
        this.keyboardMaker = new KeyboardMaker(languageUtil);
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage;

        if (languageUtil.getLanguages().containsKey(lang)) {
            languageUtil.setLanguage(languageUtil.getLanguages().get(lang));
            sendMessage = new SendMessage(chatId, languageUtil.getProperty("change.language"));
            sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyBoard());
        }else {
            sendMessage = new SendMessage(chatId, languageUtil.getProperty("start"));
            sendMessage.setReplyMarkup(keyboardMaker.getLanguageMenuKeyBoard());
        }
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
