package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class EditChannelHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public EditChannelHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update) {
        List<Object> objects = new ArrayList<>();
        if(entityService.checkChannelsPhoto1IsExist(update)){
            objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
        }
        if(entityService.checkChannelsPhoto2IsExist(update)){
            objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
        }
        entityService.setUsersUsingPage(update, "Edit channel name");
        objects.add(messageService.buildFirstAddingChannelPage(update, entityService.getChatId(update)));
        return objects;
    }
}
