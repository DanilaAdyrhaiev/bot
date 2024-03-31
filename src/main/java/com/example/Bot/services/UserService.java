package com.example.Bot.services;


import com.example.Bot.entities.User;
import com.example.Bot.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private Gson gson;
    private HashMap<Long, User> users = new HashMap<>();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public User saveUser(User user) {
        User saveUser = userRepository.save(user);
        users.put(saveUser.getId(), saveUser);
        return saveUser;
    }
    public List<User> getUserByStatus(String status) {
        return userRepository.findByStatus(status);
    }


    public User getUserByChatId(Long chatId){return userRepository.findByChatId(chatId);}

    public User update(User user){
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("Note with id " +user.getId() + " not found");
        }
        User updateUser = userRepository.save(user);
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }

}
