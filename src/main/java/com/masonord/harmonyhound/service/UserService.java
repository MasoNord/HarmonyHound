package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

public interface UserService {
    User findByChatId(Long chatId);
    User addUser(Message message);
    int updateUserLang(String lang, Long chatId);
    List<User> findAll();
}
