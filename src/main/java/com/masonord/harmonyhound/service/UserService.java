package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

public interface UserService {

    /**
     * Find user by chat id
     *
     * @param chatId
     * @return
     */
    User findByChatId(Long chatId);

    /**
     * Add a new user to the database
     *
     * @param message
     * @return
     */
    User addUser(Message message);

    /**
     * Update user's current language
     *
     * @param lang
     * @param chatId
     */
    void updateUserLang(String lang, Long chatId);

    /**
     * Update user's api calls
     * Api call - how many times a user tried to recognize a song
     *
     * @param chatId
     */
    void updateUserApiCalls(Long chatId);

    /**
     * Get all users from the database
     *
     * @return
     */
    List<User> findAll();
}
