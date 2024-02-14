package com.example.Bot.telegram.handlers;

import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class DeleteChannelHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;
    private final ChannelService channelService;

    @Autowired
    public DeleteChannelHandler(MessageService messageService, EntityService entityService, ChannelService channelService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.channelService = channelService;
    }

    @Override
    public Object execute(Update update){
        List<Object> objects = new ArrayList<>();
        if(entityService.checkChannelsPhoto1IsExist(update)){
            objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
        }
        if(entityService.checkChannelsPhoto2IsExist(update)){
            objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
        }
        channelService.deleteChannel(entityService.getUser(update).getSelectedChannel());
        objects.add(messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update)));
        entityService.setUsersMessageMenu(update);
        return objects;
    }
}
