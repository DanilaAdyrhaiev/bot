package com.example.Bot.services;

import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
        return update.hasCallbackQuery()?
                update.getCallbackQuery().getMessage().getChatId():
                update.getMessage().getChatId();
    }
    public int getMessageId(Update update){
        return update.hasCallbackQuery()?
                update.getCallbackQuery().getMessage().getMessageId():
                update.getMessage().getMessageId();
    }


    //Methods for using user entity
    public User getUser(Update update){
        return userService.getUserByChatId(getChatId(update));
    }

    @Async
    public void setUserSelectedChannel(User user, Long channel){
        user.setSelectedChannel(channel);
        userService.update(user);
    }

    public String getUsersUsingPage(Update update){
        return getUser(update).getUsingPage();
    }

    @Async
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

    @Async
    public void setUsersMessageMenu(Update update, int count){
        User user = getUser(update);
        user.setMessageMenu(getMessageId(update)+count);
        userService.update(user);
    }

    @Async
    public void createUser(Update update){
        User user;
        if(update.getMessage().getFrom().getLastName() != null){
            String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
            user = new User(name, getChatId(update), "User");
        }
        else{
            String name = update.getMessage().getFrom().getFirstName();
            user = new User(name, getChatId(update), "User");
        }
        userService.saveUser(user);
        user = userService.getUserByChatId(getChatId(update));
        user.setMessageMenu(update.getMessage().getMessageId()+1);
        user.setUsingPage("MainMenu");
        userService.update(user);
    }

    //Methods for using channel entity

    public Channel getChannelByUser(Update update){
        return channelService.getChannelById(getUser(update).getSelectedChannel());
    }

    @Async
    public void setChannelsEmptyLink(User user, Update update){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setLink(" ");
        channelService.update(channel);
    }

    @Async
    public void setChannelsLink(Update update){
        Channel channel = channelService.getChannelById(getUser(update).getSelectedChannel());
        channel.setLink(update.getMessage().getText());
        channelService.update(channel);
    }



    @Async
    public void setChannelName(Update update){
        User user = userService.getUserByChatId(getChatId(update));
        Channel channel = channelService.saveChannel(new Channel(user));
        user.setSelectedChannel(channel.getId());
        user = userService.update(user);
        channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setChannelName(update.getMessage().getText());
        channelService.update(channel);
    }

    @Async
    public void editChannelName(Update update){
        Channel channel = channelService.getChannelById(getUser(update).getSelectedChannel());
        channel.setChannelName(update.getMessage().getText());
        channelService.update(channel);
    }

    @Async
    public void setChannelDirectory(Update update){
        Channel channel = channelService.getChannelById(getUser(update).getSelectedChannel());
        String dir =channel.getChannelName()+"-"+getUser(update).getChatId()+"-Directory";
        channel.setDirectoryLink("src/main/resources/channels/"+dir);
        channelService.update(channel);
    }

    @Async
    public void setChannelsEmptyAdminLink(User user, Update update){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setLinkOnAdmin(" ");
        channelService.update(channel);
    }

    @Async
    public void setChannelsAdminLink(Update update){
        Channel channel = channelService.getChannelById(getUser(update).getSelectedChannel());
        channel.setLinkOnAdmin(update.getMessage().getText());
        channelService.update(channel);
    }

    @Async
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

    @Async
    public void setChannelsLinkOnScreenshot1(Update update){
        Channel channel = getChannelByUser(update);
        channel.setLinkOnScreenshot1(channel.getDirectoryLink()+"/statistic.png");
        channelService.update(channel);
    }

    @Async
    public void setChannelsLinkOnScreenshot2(Update update){
        Channel channel = getChannelByUser(update);
        channel.setLinkOnScreenshot2(channel.getDirectoryLink()+"/users.png");
        channelService.update(channel);
    }


}
