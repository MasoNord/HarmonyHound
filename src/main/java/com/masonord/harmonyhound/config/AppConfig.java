package com.masonord.harmonyhound.config;

import com.masonord.harmonyhound.TelegramBot;
import com.masonord.harmonyhound.TelegramFacade;
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
    public TelegramBot springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade){
        TelegramBot bot = new TelegramBot(telegramFacade, setWebhook);
        bot.setBotPath(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getBotName());
        bot.setBotPath(botConfig.getWebHookPath());

        return bot;
    }
}
