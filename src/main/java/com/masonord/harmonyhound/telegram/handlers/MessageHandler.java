package com.masonord.harmonyhound.telegram.handlers;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import com.masonord.harmonyhound.telegram.commands.CommandFactory;
import com.masonord.harmonyhound.util.LanguageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {
    public BotApiMethod<?> answerMessage(UserService userService,
                                         User user,
                                         Message message,
                                         String botToken) throws Exception {
        LanguageUtil languageUtil = new LanguageUtil(user, userService);
        CommandFactory commandFactory = new CommandFactory(languageUtil, user, message, botToken, userService);
        return commandFactory.createCommand().execute();
    }
}
