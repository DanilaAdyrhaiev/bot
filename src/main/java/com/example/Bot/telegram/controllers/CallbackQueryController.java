package com.example.Bot.telegram.controllers;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.EntityService;
import com.example.Bot.services.NotebookService;
import com.example.Bot.services.UserService;
import com.example.Bot.telegram.handlers.*;
import com.example.Bot.telegram.interfaces.ICommand;
import com.example.Bot.telegram.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CallbackQueryController {
    private final MessageService messageService;
    private final EntityService entityService;
    private final NotebookService notebookService;
    private final Map<String, ICommand> commands = new HashMap<>();
    ICommand command;

    @Autowired
    public CallbackQueryController(MessageService messageService, ChannelService channelService, EntityService entityService, NotebookService notebookService) {
        this.messageService = messageService;
        this.entityService = entityService;
        this.notebookService = notebookService;

        commands.put("/channelInfo:", new AddChannelHandler(messageService, entityService));
        commands.put("/usersChannelInfo:", new UserChannelInfoHandler(messageService, entityService));
        commands.put("/listTop:", new ListTopHandler(messageService, entityService));
        commands.put("/deleteChannel:", new DeleteChannelHandler(messageService, entityService, channelService));
        commands.put("/addChannel", new AddChannelHandler(messageService, entityService));
        commands.put("/chooseCS", new ChooseCryptoSignalsHandler(messageService, entityService));
        commands.put("/chooseAR", new ChooseAirdropOrRetrodropHandler(messageService, entityService));
        commands.put("/chooseN", new ChooseNewsHandler(messageService, entityService));
        commands.put("/listCryptoSignals", new ListCryptoSignalsHandler(messageService, entityService));
        commands.put("/listAirdropRetrodrop", new ListAirdropOrRetrodropHandler(messageService, entityService));
        commands.put("/listNews", new ListNewsHandler(messageService, entityService));
        commands.put("/Account", new AccountHandler(messageService, entityService));
        commands.put("/BackToMainMenu", new BackToMainMenuHandler(messageService, entityService));
        commands.put("/Back", new BackHandler(messageService, entityService, channelService));
    }

    public Object commandProcessing(Update update){
        switch (checkData(update)){
            case "commandWithId":
                String cmd = extractChannelId(update);
                command = commands.get(cmd);
                return command.execute(update);
            case "commandWithoutId":
                command = commands.get(update.getCallbackQuery().getData());
                return command.execute(update);
            default:
                return null;
        }

    }

    private String extractChannelId(Update update) {
        String data = update.getCallbackQuery().getData();
        if (data.startsWith("/channelInfo:") || data.startsWith("/usersChannelInfo:") || data.startsWith("/listTop:") || data.startsWith("/deleteChannel:")) {
            int endIndex = data.indexOf(":");
            if (endIndex != -1) {
                return data.substring(0, endIndex+1);
            }
        }
        return null;
    }


    private String checkData(Update update){
        return update.getCallbackQuery().getData().contains(":")?
                "commandWithId":
                "commandWithoutId";
    }


}
