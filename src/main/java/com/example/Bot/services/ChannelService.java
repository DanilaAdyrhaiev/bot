package com.example.Bot.services;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.repositories.ChannelRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;
    private Gson gson;
    private HashMap<Long, Channel> channels = new HashMap<>();

    @Autowired
    public ChannelService(UserService userService, ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
        this.userService = userService;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Channel saveChannel(Channel channel) {
        Channel saveChannel = channelRepository.save(channel);
        channels.put(saveChannel.getId(), saveChannel);
        return saveChannel;
    }

    public Channel getChannelById(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    public List<Channel> getChannelsByUser(User user) {
        return channelRepository.findByUser(user);
    }

    public List<Channel> getAllChannelsSortedByRate(){
        return channelRepository.findAllByCategoryIsNotNullOrderByRateDesc();
    }

    public void deleteChannel(Long id) {
        channels.remove(id);
        channelRepository.deleteById(id);
    }

    public Channel update(Channel channel){
        if (!channelRepository.existsById(channel.getId())) {
            throw new IllegalArgumentException("Note with id " + channel.getId() + " not found");
        }
        Channel updateChannel = channelRepository.save(channel);
        channels.put(updateChannel.getId(), updateChannel);
        return updateChannel;
    }

    public Channel getChannelWithHighestRate() {
        return channelRepository.findFirstByOrderByRateDesc();
    }

    public Channel getChannelWithHighestRateInCategory(String category) {
        return channelRepository.findFirstByCategoryOrderByRateDesc(category);
    }
}
