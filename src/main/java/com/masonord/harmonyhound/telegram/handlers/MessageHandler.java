package com.masonord.harmonyhound.telegram.handlers;

import com.masonord.harmonyhound.telegram.commands.CommandFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    public BotApiMethod<?> answerMessage(Message message) throws Exception {
        String chatId = message.getChatId().toString();
        return CommandFactory.INSTANCE.createCommand(message, chatId).execute();
    }
}
