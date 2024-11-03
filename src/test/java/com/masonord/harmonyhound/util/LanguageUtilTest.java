package com.masonord.harmonyhound.util;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Locale;


public class LanguageUtilTest {
    LanguageUtil util;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setLang("en-US");
        util = new LanguageUtil(user, userService);
    }

    @Test
    void should_successfully_get_message_from_bundle() {
        String message = util.getProperty("language");
        assertEquals(util.getProperty("language"), message);
    }

    @Test
    void should_successfully_change_language() {
        util.setLanguage("ru-RU");
        assertEquals(Locale.forLanguageTag("ru-RU"), util.getLanguage());
    }

    @Test
    void should_successfully_get_message_from_bundle_with_lang() {
        String message = util.getProperty("language", "ru");
    }

    @Test
    void should_successfully_get_message_from_bundle_after_changing_lang() {
        util.setLanguage("ru-RU");
        String message = util.getProperty("language");
        assertEquals("Русский", message);
    }
}
