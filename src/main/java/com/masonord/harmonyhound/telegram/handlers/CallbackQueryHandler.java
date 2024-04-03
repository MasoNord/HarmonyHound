package com.masonord.harmonyhound.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.IOException;

@Component
public class CallbackQueryHandler {
    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) throws IOException {
        return new SendMessage();
    }
}
