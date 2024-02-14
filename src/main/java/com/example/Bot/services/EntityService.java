package com.example.Bot.services;

import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
public class EntityService {
    private final UserService userService;
    private final ChannelService channelService;
    private final NotebookService notebookService;

    @Autowired
    public EntityService(UserService userService, ChannelService channelService, NotebookService notebookService) {
        this.userService = userService;
        this.channelService = channelService;
        this.notebookService = notebookService;
    }

    //Methods for using telegram`s update object
    public Long getChatId(Update update){
        return update.getCallbackQuery().getMessage().getChatId();
    }
    public int getMessageId(Update update){
        return update.getCallbackQuery().getMessage().getMessageId();
    }


    //Methods for using user entity
    public User getUser(Update update){
        return userService.getUserByChatId(getChatId(update));
    }

    public void setUserSelectedChannel(User user, Long channel){
        user.setSelectedChannel(channel);
        userService.update(user);
    }

    public String getUsersUsingPage(Update update){
        return getUser(update).getUsingPage();
    }

    public void setUsersUsingPage(Update update, String usingPage){
        if(!getUser(update).getUsingPage().isEmpty()){
            User user = getUser(update);
            user.setPreviousPage(getUser(update).getUsingPage());
            user.setUsingPage(usingPage);
            userService.update(user);
        }
        else{
            User user = getUser(update);
            user.setPreviousPage("");
            user.setUsingPage(usingPage);
            userService.update(user);
        }
    }
    public void setUsersMessageMenu(Update update){
        User user = getUser(update);
        user.setMessageMenu(getMessageId(update));
        userService.update(user);
    }

    //Methods for using channel entity

    public Channel getChannelByUser(Update update){
        return channelService.getChannelById(getUser(update).getSelectedChannel());
    }
    public void setChannelsEmptyLink(User user, Update update){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setLink(" ");
        channelService.update(channel);
    }

    public void setChannelsAdminLink(User user, Update update){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setLinkOnAdmin(" ");
        channelService.update(channel);
    }

    public void setChannelCategory(User user, String category){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setCategory(category);
        channelService.update(channel);
    }

    public boolean checkChannelsPhoto1IsExist(Update update){
        if(getChannelByUser(update).getLinkOnScreenshot1() != null){
            if(!getChannelByUser(update).getLinkOnScreenshot1().equals(" ")){
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }

    public boolean checkChannelsPhoto2IsExist(Update update){
        if(getChannelByUser(update).getLinkOnScreenshot1() != null){
            if(!getChannelByUser(update).getLinkOnScreenshot1().equals(" ")){
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }


}
