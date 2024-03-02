package com.example.Bot.telegram.handlers;

import com.example.Bot.entities.Notebook;
import com.example.Bot.services.EntityService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SelecedRequestHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;
    private final NotebookService notebookService;

    public SelecedRequestHandler(MessageService messageService, EntityService entityService, NotebookService notebookService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.notebookService = notebookService;
    }

    @Override
    public Object execute(Update update) {
        entityService.setUsersUsingPage(update, "SelectedRequest");
        long id = Long.parseLong(update.getCallbackQuery().getData().replace("/request:", ""));
        Notebook notebook = entityService.getNotebook(id);
        return messageService.selectedRequestPage(update, notebook);
    }
}
