package com.masonord.harmonyhound.config;

import com.masonord.harmonyhound.telegram.TelegramBot;
import com.masonord.harmonyhound.telegram.handlers.CallbackQueryHandler;
import com.masonord.harmonyhound.telegram.handlers.MessageHandler;
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
    public TelegramBot springWebhookBot(SetWebhook setWebhook,
                                        MessageHandler messageHandler,
                                        CallbackQueryHandler callbackQueryHandler) {
        TelegramBot bot = new TelegramBot(setWebhook, messageHandler, callbackQueryHandler);
        bot.setBotPath(botConfig.getBotToken());
        bot.setBotUsername(botConfig.getBotName());
        bot.setBotPath(botConfig.getWebHookPath());
        return bot;
    }
}
