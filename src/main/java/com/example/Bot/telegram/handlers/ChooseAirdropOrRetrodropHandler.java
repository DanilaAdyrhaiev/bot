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
public class ChooseAirdropOrRetrodropHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public ChooseAirdropOrRetrodropHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update){
        entityService.setChannelCategory(entityService.getUser(update), "Airdrop/Retrodrop");
        if(entityService.getUser(update).getUsingPage().equals("Edit category")){
            List<Object> objects = new ArrayList<>();
            objects.add(messageService.deleteMessage(update, entityService.getChatId(update)));
            objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
            objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
            objects.add(messageService.buildInfoOfSelectedUsersChannel(update));
            entityService.setUsersUsingPage(update, "InfoOfSelectedUsersChannel");
            return objects;
        }
        else {
            entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
            entityService.setUsersUsingPage(update, "Main menu");
            entityService.setUsersMessageMenu(update, 0);
            return messageService.buildAccountPage(entityService.getChatId(update), entityService.getMessageId(update));
        }

    }
}
