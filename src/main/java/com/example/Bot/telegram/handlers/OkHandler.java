package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class OkHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    public OkHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update) {
        return messageService.deleteMessage(update, entityService.getChatId(update));
    }
}
