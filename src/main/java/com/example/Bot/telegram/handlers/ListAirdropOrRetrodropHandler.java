package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ListAirdropOrRetrodropHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;

    @Autowired
    public ListAirdropOrRetrodropHandler(MessageService messageService, EntityService entityService) {
        this.messageService = messageService;
        this.entityService = entityService;
    }

    @Override
    public Object execute(Update update){
        int page = Integer.parseInt(update.getCallbackQuery().getData().replace("/listAirdrop:", ""));
        entityService.setUsersUsingPage(update, "List of Airdrop/Retrodrop, page:"+page);
        return messageService.buildAirdropChannelsPage(update, entityService.getMessageId(update),
                "Airdrop/Retrodrop", page);
    }
}
