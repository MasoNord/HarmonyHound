package com.masonord.harmonyhound.config;

import com.masonord.harmonyhound.telegram.TelegramBot;
import com.masonord.harmonyhound.telegram.TelegramFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class AppConfig {

    private final TelegramConfig botConfig;

    public AppConfig(TelegramConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botConfig.getWebHookPath()).build();
    }

    @Bean
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
        TelegramBot bot = new TelegramBot(setWebhook, telegramFacade);
        bot.setBotPath(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getBotName());
        bot.setBotPath(botConfig.getWebHookPath());
        return bot;
    }

    public TelegramConfig getTelegramConfig() {
        return botConfig;
    }
}
