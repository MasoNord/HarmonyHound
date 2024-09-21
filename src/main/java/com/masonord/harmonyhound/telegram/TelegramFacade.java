package com.masonord.harmonyhound.telegram;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import com.masonord.harmonyhound.telegram.handlers.CallbackQueryHandler;
import com.masonord.harmonyhound.telegram.handlers.MediaHandler;
import com.masonord.harmonyhound.telegram.handlers.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramFacade {
    public final static ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final MessageHandler messageHandler;
    private final MediaHandler mediaHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    @Autowired
    private UserService userService;

    @SneakyThrows
    @PostConstruct
    void init() {
        userService.findAll().forEach(user -> TelegramFacade.users.put(user.getUserId(), user));
    }

    public TelegramFacade(MessageHandler messageHandler,
                       CallbackQueryHandler callbackQueryHandler,
                       MediaHandler mediaHandler) {
        this.messageHandler = messageHandler;
        this.mediaHandler = mediaHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) throws Exception {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        }else {
            Message message = update.getMessage();
            User user;

            if (!users.containsKey(message.getChatId())) {
                user = userService.addUser(message);
                users.put(message.getChatId(), user);
            }

            user = users.get(message.getChatId());

            if (message.hasText()) {
                return messageHandler.answerMessage(message, user);
            }else if (message.hasVoice() || message.hasAudio() || message.hasVideo()) {
                return mediaHandler.answerMessage(message);
            }
        }
        return null;
    }

}
