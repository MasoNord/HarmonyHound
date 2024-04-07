package com.masonord.harmonyhound.util;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Getter
@Setter
public class LanguageUtil {
    private final Map<String, String> languages = new HashMap<>();
    private static Locale language = Locale.forLanguageTag("en-US");

    {
        languages.put("\uD83C\uDDFA\uD83C\uDDF8" + " " + "English", "en-US");
        languages.put("\uD83C\uDDF7\uD83C\uDDFA" + " " + "Русский", "ru-RU");
    }

    public String getProperty(String key)  {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", language);
        return bundle.getString(key);
    }

    public String getProperty(String key, String tag) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag(tag));
        return bundle.getString(key);
    }

    public void setLanguage(String tag) {
        language = Locale.forLanguageTag(tag);
    }

    public Locale getLanguage() {
        return language;
    }
}
