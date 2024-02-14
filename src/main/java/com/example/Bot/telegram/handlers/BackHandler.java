package com.example.Bot.telegram.handlers;

import com.example.Bot.entities.Channel;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class BackHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;
    private final ChannelService channelService;

    @Autowired
    public BackHandler(MessageService messageService, EntityService entityService, ChannelService channelService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.channelService = channelService;
    }

    @Override
    public Object execute(Update update){
        switch (entityService.getUsersUsingPage(update)) {
            case "Input channel name":
                entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
                entityService.setUsersUsingPage(update, "Main menu");
                return entityService.getUser(update).getStatus().equals("Admin") ?
                        messageService.buildMainAdminPage(update, entityService.getChatId(update)) :
                        messageService.buildMainUserPage(update, entityService.getChatId(update));

            case "Input channel link":
                deleteDir(update);
                channelService.deleteChannel(entityService.getUser(update).getSelectedChannel());
                entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
                entityService.setUsersUsingPage(update, "Input channel name");
                return messageService.buildFirstAddingChannelPage(update, entityService.getChatId(update));

            case "Input screenshot of statistic":
                entityService.setChannelsEmptyLink(entityService.getUser(update), update);
                entityService.setUsersUsingPage(update, "Input channel link");
                return messageService.buildSecondAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Input screenshot of users":
                deleteFirstPage(update);
                entityService.setUsersUsingPage(update, "Input screenshot of statistic");
                return messageService.buildThirdAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Input link of admin":
                deleteSecondPage(update);
                entityService.setUsersUsingPage(update, "Input screenshot of users");
                return messageService.buildFourthAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Set category":
                entityService.setChannelsAdminLink(entityService.getUser(update), update);
                entityService.setUsersUsingPage(update, "Input link of admin");
                return messageService.buildFifthAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "InfoOfSelectedChannel":
                List<Object> objects = new ArrayList<>();
                objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
                objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
                switch(entityService.getUser(update).getPreviousPage()){
                    case "List of crypto signals":
                        entityService.setUsersUsingPage(update, "List of crypto signals");
                    case "List of Airdrop/Retrodrop":
                        entityService.setUsersUsingPage(update, "List of Airdrop/Retrodrop");
                    case "List of news":
                        entityService.setUsersUsingPage(update, "List of news");
                    default:
                        if(entityService.getUser(update).getPreviousPage().startsWith("List top channel, page:")) {
                            int page = Integer.parseInt(entityService.getUser(update).getPreviousPage().replace("List top channel, page:", ""));
                            entityService.setUsersUsingPage(update, "List top channel, page:" + page);
                            objects.add(messageService.buildTopChannelsPage(update, entityService.getMessageId(update), page));
                        }
                }
                entityService.setUsersUsingPage(update, entityService.getUser(update).getPreviousPage());
                entityService.setUsersMessageMenu(update);
                return objects;

            case "InfoOfSelectedUsersChannel":
                entityService.setUsersUsingPage(update, entityService.getUser(update).getPreviousPage());
                entityService.setUsersMessageMenu(update);
                List<Object> userChannelObjects = new ArrayList<>();
                userChannelObjects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
                userChannelObjects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
                userChannelObjects.add(messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update)));
                return userChannelObjects;

            default:
                return entityService.getUser(update).getStatus().equals("Admin") ?
                        messageService.buildMainAdminPage(update, entityService.getChatId(update)) :
                        messageService.buildMainUserPage(update, entityService.getChatId(update));
        }
    }

    private void deleteDir(Update update){
        File file = new File(entityService.getChannelByUser(update).getDirectoryLink());
        file.delete();
        Channel channel = entityService.getChannelByUser(update);
        channel.setDirectoryLink(" ");
        channelService.update(channel);
    }

    private void deleteFirstPage(Update update){
        File file = new File(entityService.getChannelByUser(update).getDirectoryLink());
        file.delete();
        Channel channel = entityService.getChannelByUser(update);
        channel.setLinkOnScreenshot1(" ");
        channelService.update(channel);
    }

    private void deleteSecondPage(Update update){
        File file = new File(entityService.getChannelByUser(update).getDirectoryLink());
        file.delete();
        Channel channel = entityService.getChannelByUser(update);
        channel.setLinkOnScreenshot2(" ");
        channelService.update(channel);
    }
}
