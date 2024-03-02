package com.example.Bot.telegram.controllers;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.Notebook;
import com.example.Bot.entities.User;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.services.UserService;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    private final EntityService entityService;
    @Autowired
    public MessageController(MessageService messageService, UserService userService, ChannelService channelService, NotebookService notebookService, EntityService entityService) {
        this.messageService = messageService;
        this.userService = userService;
        this.channelService = channelService;
        this.notebookService = notebookService;
        this.entityService = entityService;
    }

    public Object messageProcessing(Update update){
        if(update.getMessage().hasText()){
            List<Object> objects = new ArrayList<>();
            User user = null;
            Notebook notebook = null;
            switch (update.getMessage().getText()){
                case "/start":
                    return buildStartPage(update);
                case "ushlzbw":
                    objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                    objects.add(messageService.deleteMessageById(entityService.getChatId(update),entityService.getMessageId(update)-1));
                    objects.add(messageService.buildStartAdminPage(entityService.getChatId(update)));
                    user = entityService.getUser(update);
                    user.setStatus("Admin");
                    userService.update(user);
                    return objects;
                default:

                    switch (getUsersUsingPage(update)){
                        case "Input channel name":
                            entityService.setChannelName(update);
                            entityService.setChannelDirectory(update);
                            entityService.setUsersUsingPage(update, "Input channel link");
                            new File(getSelectedChannel(update)
                                    .getDirectoryLink())
                                    .mkdir();
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSecondAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Input channel link":
                            entityService.setChannelsLink(update);
                            entityService.setUsersUsingPage(update, "Input screenshot of statistic");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildThirdAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Input link of admin":
                            entityService.setChannelsAdminLink(update);
                            entityService.setUsersUsingPage(update,"Set category");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSixthAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Edit channel name":
                            entityService.editChannelName(update);
                            editFiles(update);
                            entityService.setUsersUsingPage(update, "Edit channel link");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSecondAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Edit channel link":
                            entityService.setChannelsLink(update);
                            entityService.setUsersUsingPage(update, "Edit screenshot of statistic");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildThirdAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "Edit link of admin":
                            entityService.setChannelsAdminLink(update);
                            entityService.setUsersUsingPage(update,"Edit category");
                            objects = new ArrayList<>();
                            objects.add(messageService.deleteMessage(update, getChatId(update)));
                            objects.add(messageService.buildSixthAddingChannelPage(getChatId(update),
                                    getUsersMessageMenu(update)));
                            return objects;
                        case "RaiseUpInTop":
                            //
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()));
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()-1));
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()
                                    -2));
                            objects.add(messageService.buildRaiseUpWaiterPage(update));
                            notebook = entityService.createNotebook(update);
                            if(notebook!=null){
                                notebook.setCategory("Top");
                                notebookService.update(notebook);
                                try {
                                    objects.add(messageService.NotifyAboutTopAdmin(update, notebook, update.getMessage().getText(), notebook.getId()));
                                }catch(IndexOutOfBoundsException e){
                                    System.out.println("there are no administrators in the database");
                                }

                            }
                            return objects;
                        case "RaiseUpInCategory":
                            //
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()));
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()-1));
                            objects.add(messageService.deleteMessageById(entityService.getChatId(update), update.getMessage().getMessageId()
                                    -2));
                            objects.add(messageService.buildRaiseUpWaiterPage(update));
                            notebook = entityService.createNotebook(update);
                            if(notebook!=null){
                                notebook.setCategory(entityService.getChannelByUser(update).getCategory());
                                notebook.setPaymentCode(update.getMessage().getText());
                                notebookService.update(notebook);
                                try {
                                    objects.add(messageService.NotifyAboutCategoryAdmin(update, notebook, update.getMessage().getText(), notebook.getId()));
                                }
                                catch (IndexOutOfBoundsException e){
                                    System.out.println("there are no administrators in the database");
                                }
                            }
                            return objects;
                        case "RaiseUpInCategoryC":
                            return objects;

                        default:
                            user = getUser(update);
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
                    entityService.setChannelsLinkOnScreenshot1(update);
                    entityService.setUsersUsingPage(update, "Input screenshot of users");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFourthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
                case "Input screenshot of users":
                    entityService.setChannelsLinkOnScreenshot2(update);
                    entityService.setUsersUsingPage(update, "Input link of admin");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFifthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
                case "Edit screenshot of statistic":
                    channel = getSelectedChannel(update);
                    channel.setLinkOnScreenshot1(channel.getDirectoryLink()+"/statistic.png");
                    channelService.update(channel);
                    entityService.setUsersUsingPage(update, "Edit screenshot of users");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFourthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
                case "Edit screenshot of users":
                    channel = getSelectedChannel(update);
                    channel.setLinkOnScreenshot2(channel.getDirectoryLink()+"/users.png");
                    channelService.update(channel);
                    entityService.setUsersUsingPage(update, "Edit link of admin");
                    objects.add(messageService.deleteMessage(update, getChatId(update)));
                    objects.add(messageService.buildFifthAddingChannelPage(getChatId(update),
                            getUsersMessageMenu(update)));
                    return objects;
            }
        }
        return buildStartPage(update);
    }

    @Async
    public void editFiles(Update update){
        File dir = new File(entityService.getChannelByUser(update).getDirectoryLink());
        entityService.setChannelDirectory(update);
        dir.renameTo(new File(entityService.getChannelByUser(update).getDirectoryLink()));
        entityService.setChannelsLinkOnScreenshot1(update);
        entityService.setChannelsLinkOnScreenshot2(update);
    }

    private Object buildStartPage(Update update){
        if(entityService.getUser(update) == null){
            entityService.createUser(update);
            entityService.setUsersMessageMenu(update, 1);
            entityService.setUsersUsingPage(update, "Main menu");
            return messageService.buildStartUserPage(getChatId(update));
        }
        else{
            switch (getUser(update).getStatus()){
                case "User":
                    entityService.setUsersMessageMenu(update, 1);
                    entityService.setUsersUsingPage(update, "Main menu");
                    return messageService.buildStartUserPage(getChatId(update));
                case "Admin":
                    entityService.setUsersMessageMenu(update, 1);
                    entityService.setUsersUsingPage(update, "Main menu");
                    return messageService.buildStartAdminPage(getChatId(update));
                default:
                    return messageService.buildStartUserPage(getChatId(update));
            }
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

}
