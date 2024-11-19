package com.masonord.harmonyhound.telegram.keyboards;

import com.masonord.harmonyhound.util.LanguageUtil;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardMaker {

    private final LanguageUtil languageUtil;

    public KeyboardMaker(LanguageUtil languageUtil) {
        this.languageUtil = languageUtil;
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
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getKeyboard(String[][] strings) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rowList = new ArrayList<>();

        for (String[] ss : strings) {
            KeyboardRow row = new KeyboardRow();
            row.addAll(Arrays.asList(ss));
            rowList.add(row);
        }

        keyboardMarkup.setKeyboard(rowList);
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        return keyboardMarkup;
    }

}
