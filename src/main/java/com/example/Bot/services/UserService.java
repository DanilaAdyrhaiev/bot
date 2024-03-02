package com.example.Bot.services;


import com.example.Bot.entities.User;
import com.example.Bot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<User> getUserByStatus(String status) {
        return userRepository.findByStatus(status);
    }


    public User getUserByChatId(Long chatId){return userRepository.findByChatId(chatId);}

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    public User update(User user){
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Note with id " +user.getId() + " not found");
        }
        return userRepository.save(user);
    }

}
