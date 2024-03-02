package com.example.Bot.telegram.handlers;

import com.example.Bot.services.EntityService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ListRequestsHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService e;
    private final NotebookService notebookService;

    public ListRequestsHandler(MessageService messageService, EntityService entityService, NotebookService notebookService) {
        this.messageService = messageService;
        this.e = entityService;
        this.notebookService = notebookService;
    }

    @Override
    public Object execute(Update update) {
        List<Object> objects = new ArrayList<>();
        objects.add(messageService.deleteMessage(update, e.getChatId(update)));
        objects.add(messageService.buildRequestsPage(update));
        e.setUsersMessageMenu(update, 0);
        return objects;
    }
}
