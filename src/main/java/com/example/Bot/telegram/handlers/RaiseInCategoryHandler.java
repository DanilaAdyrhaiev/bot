package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class RaiseInCategoryHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    public RaiseInCategoryHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }


    @Override
    public Object execute(Update update) {
        List<Object> objects = new ArrayList<>();
        entityService.setUsersUsingPage(update, "RaiseUpInCategory");
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 2));
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update) - 1));
        objects.add(messageService.deleteMessage(update,entityService.getChatId(update)));
        objects.add(messageService.buildQrCodePage(update));
        objects.add(messageService.buildRaiseUpPage(update));
        return objects;
    }
}
