package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.telegram.keyboards.KeyboardMaker;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StartCommand implements Command{
    private final String chatId;
    private final KeyboardMaker keyboardMaker;
    private final LanguageUtil languageUtil;

    public StartCommand(String chatId) {
        this.chatId = chatId;
        this.languageUtil = new LanguageUtil();
        this.keyboardMaker = new KeyboardMaker(languageUtil);
    }

    @Override
    public BotApiMethod<?> execute() {
        SendMessage sendMessage = new SendMessage(chatId, languageUtil.getProperty("start"));
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(keyboardMaker.getLanguageMenuKeyBoard());
        return sendMessage;
    }
}
