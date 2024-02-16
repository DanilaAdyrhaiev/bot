package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChooseNewsHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public ChooseNewsHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update){
        entityService.setChannelCategory(entityService.getUser(update), "News");
        if(entityService.getUser(update).getUsingPage().equals("Edit category")){
            entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
            return messageService.buildInfoOfSelectedUsersChannel(update);

        }
        else {
            entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
            entityService.setUsersUsingPage(update, "Main menu");
            entityService.setUsersMessageMenu(update, 0);
            return messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update));
        }
    }
}
