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
        List<Object> objects = new ArrayList<>();
        int categoryPage;
        switch (entityService.getUsersUsingPage(update)) {
            case "Channels menu":
                entityService.setUsersUsingPage(update, "Main menu");
                return entityService.getUser(update).getStatus().equals("Admin")?
                        messageService.buildMainAdminPage(update, entityService.getChatId(update)):
                        messageService.buildMainUserPage(update, entityService.getChatId(update));

            case "Input channel name":
                entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
                entityService.setUsersUsingPage(update, "Main menu");
                return messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update));


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
                entityService.setChannelsEmptyAdminLink(entityService.getUser(update), update);
                entityService.setUsersUsingPage(update, "Input link of admin");
                return messageService.buildFifthAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "InfoOfSelectedChannel":
                if(entityService.checkChannelsPhoto1IsExist(update)){
                    objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
                }
                if(entityService.checkChannelsPhoto2IsExist(update)){
                    objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
                }
                entityService.setUsersMessageMenu(update, 0);
                switch(entityService.getUser(update).getPreviousPage()){
                    case "List of crypto signals":
                        categoryPage = Integer.parseInt(entityService.getUser(update).getPreviousPage().replace("List top channel, page:", ""));
                        entityService.setUsersUsingPage(update, "List of crypto signals");
                        objects.add(messageService.buildCryptoChannelsPage(update, entityService.getUser(update).getMessageMenu(), "Crypto signals", categoryPage));
                        break;
                    case "List of Airdrop/Retrodrop":
                        categoryPage = Integer.parseInt(entityService.getUser(update).getPreviousPage().replace("List top channel, page:", ""));
                        entityService.setUsersUsingPage(update, "List of Airdrop/Retrodrop");
                        objects.add(messageService.buildAirdropChannelsPage(update, entityService.getUser(update).getMessageMenu(), "Airdrop/Retrodrop", categoryPage));
                        break;
                    case "List of news":
                        categoryPage = Integer.parseInt(entityService.getUser(update).getPreviousPage().replace("List top channel, page:", ""));
                        entityService.setUsersUsingPage(update, "List of news");
                        objects.add(messageService.buildNewsCategoryPage(update, entityService.getUser(update).getMessageMenu(), "News", categoryPage));
                        break;
                    default:
                        if(entityService.getUser(update).getPreviousPage().startsWith("List top channel, page:")) {
                            int topPage = Integer.parseInt(entityService.getUser(update).getPreviousPage().replace("List top channel, page:", ""));
                            entityService.setUsersUsingPage(update, "List top channel, page:" + topPage);
                            objects.add(messageService.buildTopChannelsPage(update, entityService.getUser(update).getMessageMenu(), topPage));
                        }
                }
                return objects;

            case "InfoOfSelectedUsersChannel":
                entityService.setUsersUsingPage(update, entityService.getUser(update).getPreviousPage());
                entityService.setUsersMessageMenu(update, 0);
                List<Object> userChannelObjects = new ArrayList<>();
                userChannelObjects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
                userChannelObjects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
                userChannelObjects.add(messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update)));
                return userChannelObjects;

            case "Edit channel name":
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
                objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
                objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
                if(entityService.getUser(update).getPreviousPage().equals("Edit channel link")){
                    entityService.setUsersMessageMenu(update, 4);
                }
                else {
                    entityService.setUsersMessageMenu(update, 3);
                }
                entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
                return objects;

            case "Edit channel link":
                entityService.setUsersUsingPage(update, "Edit channel name");
                return messageService.buildFirstAddingChannelPage(update, entityService.getChatId(update));

            case "Edit screenshot of statistic":
                entityService.setUsersUsingPage(update, "Edit channel link");
                return messageService.buildSecondAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Edit screenshot of users":
                entityService.setUsersUsingPage(update, "Edit screenshot of statistic");
                return messageService.buildThirdAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Edit link of admin":
                entityService.setUsersUsingPage(update, "Edit screenshot of users");
                return messageService.buildFourthAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));

            case "Edit channel category":
                entityService.setUsersUsingPage(update, "Edit link of admin");
                return messageService.buildFifthAddingChannelPage(entityService.getChatId(update), entityService.getMessageId(update));
            case "RaiseUpInTop":
                entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update)-1));
                objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
                objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
                objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
                entityService.setUsersMessageMenu(update, 0);
                return objects;
            case "RaiseUpInCategory":
                entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update)-1));
                objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
                objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
                objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
                entityService.setUsersMessageMenu(update, 0);
                return objects;
            case "RaiseUpInTopAfterPay":
                entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
                objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
                objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
                entityService.setUsersMessageMenu(update, 0);
                return objects;
            case "RaiseUpInCategoryAfterPay":
                entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));                objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
                objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
                entityService.setUsersMessageMenu(update, 0);
                return objects;
            case "SelectedRequest":
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.buildRequestsPage(update));
                return objects;
            case "Requests":
                entityService.setUsersUsingPage(update, "Main menu");
                objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
                objects.add(messageService.buildStartAdminPage(entityService.getChatId(update)));
                return objects;
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
