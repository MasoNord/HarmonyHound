package com.masonord.harmonyhound.telegram.commands;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.telegram.keyboards.KeyboardMaker;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

public class ChangeLangCommand implements Command {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChangeLangCommand.class);
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
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        if (languageUtil.getLanguages().containsKey(lang)) {
            languageUtil.setLanguage(languageUtil.getLanguages().get(lang));
            replyKeyboardRemove.setRemoveKeyboard(true);

            sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("change.language"));
            sendMessage.setReplyMarkup(replyKeyboardRemove);

            LOGGER.atTrace().setMessage("Set the language keyboard markup to change the language").log();
        }else {
            replyKeyboardMarkup.setOneTimeKeyboard(true);
            replyKeyboardMarkup.setKeyboard(keyboardMaker.getLanguageMenuKeyBoard().getKeyboard());
            replyKeyboardMarkup.setResizeKeyboard(true);

            sendMessage = new SendMessage(String.valueOf(user.getUserId()), languageUtil.getProperty("start"));
            sendMessage.setReplyMarkup(replyKeyboardMarkup);

            LOGGER.atTrace().setMessage("Set the language keyboard markup as a result of calling start command").log();
        }

        sendMessage.enableMarkdown(true);
        LOGGER.atInfo().setMessage("Change language command has been executed successfully").log();
        return sendMessage;
    }
}
