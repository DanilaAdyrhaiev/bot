package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ChooseCryptoSignalsHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public ChooseCryptoSignalsHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update){
        entityService.setChannelCategory(entityService.getUser(update), "Crypto signals");
        entityService.setUsersUsingPage(update, "Main menu");
        entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
        if(entityService.getUser(update).getStatus().equals("User")){
            return messageService.buildMainUserPage(update, entityService.getChatId(update));
        }
        else if(entityService.getUser(update).getStatus().equals("Admin")){
            return messageService.buildMainAdminPage(update, entityService.getChatId(update));
        }
        else {
            return messageService.buildMainUserPage(update, entityService.getChatId(update));
        }
    }
}
