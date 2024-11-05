package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.telegram.keyboards.KeyboardMaker;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class ChangeLangCommand implements Command {

    private final User user;
    private final String lang;
    private final LanguageUtil languageUtil;
    private final KeyboardMaker keyboardMaker;

    public ChangeLangCommand(User user, String lang, LanguageUtil languageUtil) {
        this.user = user;
        this.lang = lang;
        this.languageUtil = languageUtil;
        this.keyboardMaker = new KeyboardMaker(languageUtil);
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage;

        if (languageUtil.getLanguages().containsKey(lang)) {
            languageUtil.setLanguage(languageUtil.getLanguages().get(lang));
            sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("change.language"));
            sendMessage.setReplyMarkup(keyboardMaker.getMainMenuKeyBoard());
        }else {
            sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("start"));
            sendMessage.setReplyMarkup(keyboardMaker.getLanguageMenuKeyBoard());
        }
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
