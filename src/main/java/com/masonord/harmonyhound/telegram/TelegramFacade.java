package com.masonord.harmonyhound.telegram;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.service.UserService;
import com.masonord.harmonyhound.telegram.handlers.CallbackQueryHandler;
import com.masonord.harmonyhound.telegram.handlers.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramFacade {

    // local cache for storing users
    public final static ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    public final static Logger LOGGER = LoggerFactory.getLogger(TelegramFacade.class);
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;
    private final String botToken;

    @Autowired
    private UserService userService;

    @SneakyThrows
    @PostConstruct
    void init() {
        userService.findAll().forEach(user -> TelegramFacade.users.put(user.getUserId(), user));
        LOGGER.atTrace().log("The users map has been initialized successfully");
    }

    public TelegramFacade(@Value("${telegram.bot-token}") String botToken,
                          MessageHandler messageHandler,
                          CallbackQueryHandler callbackQueryHandler) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botToken = botToken;
    }

    /**
     * Implementation of Telegram API method
     * The following method receives updates with callback and normal messages
     *
     * @param update
     * @return
     */

    public BotApiMethod<?> handleUpdate(Update update) throws Exception {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        }else {
            Message message = update.getMessage();
            User user;

            // check if the current users exists in the cache
            if (!users.containsKey(message.getChatId())) {
                user = userService.addUser(message);
                users.put(message.getChatId(), user);
                LOGGER.atTrace().log("Add a new user to the map of users");
            }else {
                user = userService.findByChatId(message.getChatId());
            }

            return messageHandler.answerMessage(userService, user, message, botToken);
        }
    }
}
