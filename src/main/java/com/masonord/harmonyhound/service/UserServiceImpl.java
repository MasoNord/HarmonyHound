package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    public static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User findByChatId(Long chatId) {
        User user = userRepository.findByUserId(chatId);
        LOGGER.atDebug().setMessage("Get the user by chatId: {}").addArgument(chatId).log();
        LOGGER.atInfo().log("User has been successfully sent");
        return user;
    }

    @Transactional
    public User addUser(Message message) {
        User user = new User();

        user.setUserId(message.getChatId());
        user.setUsername(message.getChat().getUserName());
        user.setFirstName(message.getChat().getFirstName());
        user.setLastName(message.getChat().getLastName());
        user.setLang("en-US");
        user.setApiCalls(0L);
        userRepository.save(user);

        LOGGER.atDebug().setMessage("A new user's properties: userId={}, username={}, firstname={}, lastname={}")
                .addArgument(user.getUserId())
                .addArgument(user.getUsername())
                .addArgument(user.getFirstName())
                .addArgument(user.getLastName())
                .log();

        LOGGER.atInfo().log("A new user has been successfully created");

        return user;
    }

    @Transactional
    public void updateUserLang(String lang, Long chatId) {
        userRepository.updateUserLang(chatId, lang);

        LOGGER.atDebug().setMessage("The language set to {} for user with id {}")
                .addArgument(lang)
                .addArgument(chatId)
                .log();

        LOGGER.atInfo().log("The language has been changed successfully");
    }

    @Transactional
    public void updateUserApiCalls(Long chatId) {
        User user = userRepository.findByUserId(chatId);

        LOGGER.atDebug().setMessage("User's api calls before update: {}").addArgument(user.getApiCalls()).log();

        long apiCalls = user.getApiCalls() + 1;
        userRepository.updateUserApiCalls(chatId, apiCalls);

        LOGGER.atDebug().setMessage("User's api calls after update: {}").addArgument(apiCalls).log();
        LOGGER.atInfo().log("The user's api calls has been successfully updated");
    }

    @Transactional
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        LOGGER.atInfo().log("The list of users has been successfully sent");
        return users;
    }
}
