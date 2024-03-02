package com.example.Bot.telegram.handlers;

import com.example.Bot.entities.Channel;
import com.example.Bot.entities.Notebook;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class ConfirmTopHandler implements ICommand {
    private final MessageService messageService;
    private final EntityService entityService;
    private final NotebookService notebookService;

    private final ChannelService channelService;

    public ConfirmTopHandler(MessageService messageService, EntityService entityService, ChannelService channelService, NotebookService notebookService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.notebookService = notebookService;
        this.channelService = channelService;
    }

    @Override
    public Object execute(Update update) {
        long id = Long.parseLong(update.getCallbackQuery().getData().replace("/confirmTop:", ""));
        Notebook notebook = entityService.getNotebook(id);
        notebook.setStatus("Done");
        notebookService.update(notebook);
        Channel chnl = channelService.getChannelWithHighestRate();
        Channel channel = notebook.getChannel();
        channel.setRate(chnl.getRate()+10);
        channelService.update(channel);
        List<Object> objects = new ArrayList<>();
        objects.add(messageService.deleteMessageById(entityService.getChatId(update), entityService.getMessageId(update)));
        System.out.println(entityService.getMessageId(update));
        objects.add(entityService.getUsersUsingPage(update).equals("SelectedRequest")?
                messageService.buildRequestsPage(update):
                messageService.deleteMessageById(entityService.getChatId(update), update.getCallbackQuery().getMessage().getMessageId()));
        if(entityService.getUsersUsingPage(update).equals("SelectedRequest")){
            entityService.setUsersUsingPage(update,"Requests");
        }
        objects.add(messageService.NotifyUserAboutChannelsRaiseUp(update, notebook, "Your channel is raised up"));
        return objects;
    }
}
