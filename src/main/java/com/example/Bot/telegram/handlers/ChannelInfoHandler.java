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
public class ChannelInfoHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public ChannelInfoHandler(MessageService messageService, EntityService e) {
        this.messageService = messageService;
        this.entityService = e;
    }

    @Override
    public Object execute(Update update){
        entityService.setUserSelectedChannel(entityService.getUser(new Update()),
                Long.parseLong(update.getCallbackQuery().getData().replace("/channelInfo:", "")));
        entityService.setUsersUsingPage(update, "InfoOfSelectedChannel");
        List<Object> objects = new ArrayList<>();
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getUser(update).getMessageMenu()));
        objects.add(messageService.buildFirstPhotoOfSelectedChannel(update));
        objects.add(messageService.buildSecondPhotoOfSelectedChannel(update));
        objects.add(messageService.buildInfoOfSelectedChannel(update));
        return objects;
    }
}
