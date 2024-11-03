package com.masonord.harmonyhound.util;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import com.masonord.harmonyhound.telegram.TelegramFacade;
import lombok.Getter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class LanguageUtil {
    private final ConcurrentHashMap<String, String> languages = new ConcurrentHashMap<>();
    private final UserService userService;
    private final User user;
    private Locale currLang;

    {
        languages.put("\uD83C\uDDFA\uD83C\uDDF8" + " " + "English", "en-US");
        languages.put("\uD83C\uDDF7\uD83C\uDDFA" + " " + "Русский", "ru-RU");
    }

    public LanguageUtil(User user, UserService userService) {
        this.user = user;
        this.userService = userService;
        setLanguage(user.getLang());
    }

    public String getProperty(String key)  {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", currLang);
        return bundle.getString(key);
    }

    public String getProperty(String key, String tag) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag(tag));
        return bundle.getString(key);
    }

    public void setLanguage(String tag) {
        userService.updateUserLang(tag, user.getUserId());
        user.setLang(tag);
        TelegramFacade.users.put(user.getUserId(), user);
        currLang = Locale.forLanguageTag(tag);
    }

}
