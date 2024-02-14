package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BackToMainMenuHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public BackToMainMenuHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update){
        entityService.setUserSelectedChannel(entityService.getUser(update), 0L);
        entityService.setUsersUsingPage(update, "Main menu");
        return entityService.getUser(update).getStatus().equals("Admin")?
                messageService.buildMainAdminPage(update, entityService.getChatId(update)):
                messageService.buildMainUserPage(update, entityService.getChatId(update));
    }
}
