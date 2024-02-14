package com.example.Bot.telegram.controllers;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.services.UserService;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChannelService channelService;
    private final NotebookService notebookService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, ChannelService channelService, NotebookService notebookService) {
        this.messageService = messageService;
        this.userService = userService;
        this.channelService = channelService;
        this.notebookService = notebookService;
    }

    public Object messageProcessing(Update update){
        if(update.getMessage().hasText()){
            switch (update.getMessage().getText()){
                case "/start":
                    return buildStartPage(update);
                default:
                    List<Object> objects = null;
                    Channel channel = null;
                    switch (getUsersUsingPage(update)){
                        case "Input channel name":
                            setChannelName(update);
                            setDirectory(update);
                            setUsingPage(update, "Input channel link");
                            new File(getSelectedChannel(update)
                                    .getDirectoryLink())
                                    .mkdir();
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSecondAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Input channel link":
                            channel = getSelectedChannel(update);
                            channel.setLink(update.getMessage().getText());
                            channelService.update(channel);
                            setUsingPage(update, "Input screenshot of statistic");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildThirdAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Input link of admin":
                            channel = getSelectedChannel(update);
                            channel.setLinkOnAdmin(update.getMessage().getText());
                            channelService.update(channel);
                            setUsingPage(update,"Set category");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSixthAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        default:
                            User user = getUser(update);
                            user.setSelectedChannel(0L);
                            userService.update(user);
                          buildStartPage(update);
                    }
            }

        }
        else if(update.getMessage().hasPhoto()){
            Channel channel;
            List<Object> objects = new ArrayList<>();
            switch (getUsersUsingPage(update)){
                case "Input screenshot of statistic":
                    channel = getSelectedChannel(update);
                    channel.setLinkOnScreenshot1(channel.getDirectoryLink()+"/statistic.png");
                    channelService.update(channel);
                    setUsingPage(update, "Input screenshot of users");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFourthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
                case "Input screenshot of users":
                    channel = getSelectedChannel(update);
                    channel.setLinkOnScreenshot2(channel.getDirectoryLink()+"/users.png");
                    channelService.update(channel);
                    setUsingPage(update, "Input link of admin");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFifthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
            }
        }
        return buildStartPage(update);
    }

    private Object buildStartPage(Update update){
        if(getUser(update) == null){
            createUser(update);
            User user = getUser(update);
            user.setMessageMenu(getMessageId(update)+1);
            userService.update(user);
            setUsingPage(update, "Main menu");
            return messageService.buildStartUserPage(getChatId(update));
        }
        else{
            User user = null;
            switch (getUser(update).getStatus()){
                case "User":
                    user = getUser(update);
                    user.setMessageMenu(getMessageId(update)+1);
                    userService.update(user);
                    setUsingPage(update, "Main menu");
                    return messageService.buildStartUserPage(getChatId(update));
                case "Admin":
                    user = getUser(update);
                    user.setMessageMenu(getMessageId(update)+1);
                    userService.update(user);
                    setUsingPage(update, "Main menu");
                    return messageService.buildStartAdminPage(getChatId(update));
                default:
                    return messageService.buildStartUserPage(getChatId(update));
            }
        }
    }

    private void setUsingPage(Update update, String usingPage){
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

    private User getUser(Update update){
        return userService.getUserByChatId(getChatId(update));
    }
    
    private int getUsersMessageMenu(Update update){
        return getUser(update).getMessageMenu();
    }

    private String getUsersUsingPage(Update update){
        return getUser(update).getUsingPage();
    }

    private Channel getSelectedChannel(Update update){
        return channelService.getChannelById(getUser(update).getSelectedChannel());
    }

    private Long getChatId(Update update){
        return update.getMessage().getChatId();
    }

    private int getMessageId(Update update){
        return update.getMessage().getMessageId();
    }

    private void createUser(Update update){
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

    private void setDirectory(Update update){
        Channel channel = channelService.getChannelById(getUser(update).getSelectedChannel());
        String dir =channel.getChannelName()+"-"+getUser(update).getChatId()+"-Directory";
        channel.setDirectoryLink("src/main/resources/channels/"+dir);
        channelService.update(channel);
    }

    private void setChannelName(Update update){
        User user = userService.getUserByChatId(getChatId(update));
        Channel channel = channelService.saveChannel(new Channel(user));
        user.setSelectedChannel(channel.getId());
        user = userService.update(user);
        channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setChannelName(update.getMessage().getText());
        channelService.update(channel);
    }

    private void setChannelCategory(User user, String category){
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        channel.setCategory(category);
        channelService.update(channel);
    }
}
