package com.masonord.harmonyhound.telegram.keyboards;

import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.List;

public class KeyboardMaker {

    private final LanguageUtil languageUtil;

    public KeyboardMaker(LanguageUtil languageUtil) {
        this.languageUtil = languageUtil;
    }

    private ReplyKeyboardMarkup getMainMenuKeyBoard() {
        return null;
    }

    public ReplyKeyboardMarkup getLanguageMenuKeyBoard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("\uD83C\uDDFA\uD83C\uDDF8" + " " + languageUtil.getProperty("language", "en-US")));
        row1.add(new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA" + " " + languageUtil.getProperty("language", "ru-RU")));

        List<KeyboardRow> keyboard = List.of(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}
