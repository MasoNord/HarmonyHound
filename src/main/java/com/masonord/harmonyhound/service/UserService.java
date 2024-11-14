package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

public interface UserService {
    User findByChatId(Long chatId);
    User addUser(Message message);
    void updateUserLang(String lang, Long chatId);
    void updateUserApiCalls(Long chatId);
    List<User> findAll();
}
