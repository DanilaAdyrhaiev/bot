package com.example.Bot.telegram.handlers;

import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class RaiseInTopHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;
    private final ChannelService channelService;

    public RaiseInTopHandler(MessageService messageService, EntityService entityService, ChannelService channelService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.channelService = channelService;
    }


    @Override
    public Object execute(Update update) {
        List<Object> objects = new ArrayList<>();
        entityService.setUsersUsingPage(update, "RaiseUpInTop");
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
        objects.add(messageService.deleteMessage(update,entityService.getChatId(update)));
        objects.add(messageService.buildQrCodePage(update));
        objects.add(messageService.buildRaiseUpPage(update));
        return objects;
    }
}
