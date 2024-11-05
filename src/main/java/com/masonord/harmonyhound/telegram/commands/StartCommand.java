package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import com.masonord.harmonyhound.telegram.keyboards.KeyboardMaker;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand implements Command{
    private final KeyboardMaker keyboardMaker;
    private final LanguageUtil languageUtil;
    private final User user;

    public StartCommand(User user, LanguageUtil languageUtil) {
        this.keyboardMaker = new KeyboardMaker(languageUtil);
        this.languageUtil = languageUtil;
        this.user = user;
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("start"));
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getLanguageMenuKeyBoard());
        return sendMessage;
    }
}
