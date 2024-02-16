package com.example.Bot.services;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.repositories.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel saveChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public List<Channel> getAllChannels() {
        return (List<Channel>) channelRepository.findAll();
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
        channelRepository.deleteById(id);
    }

    public Channel update(Channel channel){
        if (!channelRepository.existsById(channel.getId())) {
            throw new IllegalArgumentException("Note with id " + channel.getId() + " not found");
        }
        return channelRepository.save(channel);
    }
}
