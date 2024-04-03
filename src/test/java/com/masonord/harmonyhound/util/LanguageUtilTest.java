package com.masonord.harmonyhound.util;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.util.Locale;


public class LanguageUtilTest {
    LanguageUtil util;

    @BeforeEach
    void setUp() {
        util = new LanguageUtil();
    }

    @Test
    void should_successfully_get_message_from_bundle() {
        String message = util.getProperty("language");
        assertEquals("English", message);
    }

    @Test
    void should_successfully_change_language() {
        util.setLanguage(Locale.forLanguageTag("ru-RU"));
        assertEquals(Locale.forLanguageTag("ru-RU"), util.getLanguage());
    }

    @Test
    void should_successfully_get_message_from_bundle_with_lang() {
        String message = util.getProperty("language", "ru");
    }

    @Test
    void should_successfully_get_message_from_bundle_after_changing_lang() {
        util.setLanguage(Locale.forLanguageTag("ru-RU"));
        String message = util.getProperty("language");
        assertEquals("Русский", message);
    }
}
