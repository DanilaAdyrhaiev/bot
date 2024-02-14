package com.example.Bot.telegram;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.UserService;
import com.example.Bot.telegram.controllers.CallbackQueryController;
import com.example.Bot.telegram.controllers.MessageController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String BotName;

    private final ChannelService channelService;
    private final UserService userService;
    private final MessageController messageController;

    private final CallbackQueryController commandController;

        public Bot(@Value("${bot.token}") String botToken, ChannelService channelService, UserService userService, MessageController messageController, CallbackQueryController commandController){
        super(botToken);

        this.channelService = channelService;
        this.userService = userService;
        this.messageController = messageController;
        this.commandController = commandController;
    }

    @Override
    public String getBotUsername() {
        return BotName;
    }

    @Override
    public void onUpdateReceived(Update update){
        if(update.hasMessage()){
            if(update.getMessage().hasText()){
                Object object = messageController.messageProcessing(update);
                executeObject(object);
            }
            else if(update.getMessage().hasPhoto()){
                downloadPhoto(update);
                Object object = messageController.messageProcessing(update);
                executeObject(object);
            }
        }
        else if(update.hasCallbackQuery()){
            Object object = commandController.commandProcessing(update);
            executeObject(object);
        }
    }

    private void executeObject(Object object){
        try {
            if(object != null){
                if(object instanceof SendMessage){
                    execute((SendMessage) object);
                }
                else if(object instanceof EditMessageText){
                    execute((EditMessageText) object);
                }
                else if(object instanceof DeleteMessage){
                    execute((DeleteMessage) object);
                }
                else if(object instanceof List<?>){
                    List<Object> list = (List<Object>) object;
                    for(Object obj : list){
                        executeObject(obj);
                    }
                }
                else if(object instanceof SendPhoto){
                    execute((SendPhoto) object);
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkOnUnnecessaryFiles(){

    }

    private void downloadPhoto(Update update){
        PhotoSize photo = update.getMessage().getPhoto().getLast();
        GetFile getFile = new GetFile(photo.getFileId());
        User user = userService.getUserByChatId(update.getMessage().getChatId());
        Channel channel = channelService.getChannelById(user.getSelectedChannel());
        String filename;
        if(userService.getUserByChatId(update.getMessage().getChatId())
                .getUsingPage()
                .equals("Input screenshot of statistic")){
            filename = "/statistic.png";
        }
        else if(userService.getUserByChatId(update.getMessage().getChatId())
                .getUsingPage()
                .equals("Input screenshot of users")){
            filename = "/users.png";
        }
        else{
            filename = "/photo.png";
        }
        try {
            File file = super.execute(getFile);
            super.downloadFile(file, new java.io.File(channel.getDirectoryLink()+filename));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
