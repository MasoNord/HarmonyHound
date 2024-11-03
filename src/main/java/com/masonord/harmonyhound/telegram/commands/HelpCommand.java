package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class HelpCommand implements Command{
    private final LanguageUtil languageUtil;
    private final User user;

    public HelpCommand(User user, LanguageUtil languageUtil) {
        this.languageUtil = languageUtil;
        this.user = user;
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("help"));
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
