package com.masonord.harmonyhound.service;

import com.masonord.harmonyhound.model.User;
import com.masonord.harmonyhound.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User findByChatId(Long chatId) {
        return userRepository.findByUserId(chatId);
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
        return user;
    }

    @Transactional
    public void updateUserLang(String lang, Long chatId) {
        userRepository.updateUserLang(chatId, lang);
    }

    @Transactional
    public void updateUserApiCalls(Long chatId) {
        User user = userRepository.findByUserId(chatId);
        long apiCalls = user.getApiCalls() + 1;
        userRepository.updateUserApiCalls(chatId, apiCalls);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
