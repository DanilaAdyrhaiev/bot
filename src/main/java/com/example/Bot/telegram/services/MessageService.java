package com.example.Bot.telegram.services;

import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import com.example.Bot.services.ChannelService;
import com.example.Bot.services.UserService;
import com.example.Bot.telegram.factories.KeyboardFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.xml.bind.helpers.AbstractUnmarshallerImpl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final UserService userService;
    private final ChannelService channelService;

    public MessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    Map<String, String> channelPage = new LinkedHashMap<>();
    KeyboardFactory keyboardFactory = new KeyboardFactory();
    Map<String, String> startUserPage = new LinkedHashMap<>();
    Map<String, String> startAdminPage = new LinkedHashMap<>();
    Map<String, String> mainUserPage = new LinkedHashMap<>();
    Map<String, String> mainAdminPage = new LinkedHashMap<>();
    Map<String, String> AccountPage = new LinkedHashMap<>();
    Map<String, String> categoryPage = new LinkedHashMap<>();
    Map<String, String> chooseCategoryPage = new LinkedHashMap<>();
    Map<String, String> FirstAddingChannelPage = new LinkedHashMap<>();
    Map<String, String> SecondAddingChannelPage = new LinkedHashMap<>();
    Map<String, String> ThirdAddingChannelPage = new LinkedHashMap<>();
    Map<String, String> FourthAddingChannelPage = new LinkedHashMap<>();
    Map<String, String> FifthAddingChannelPage = new LinkedHashMap<>();
    Map<String, String> SelectedChannelPage = new LinkedHashMap<>();
    Map<String, String> SelectedUsersChannelPage = new LinkedHashMap<>();
    Map<String, String> TopChannelsPage = new LinkedHashMap<>();

    public SendMessage buildStartUserPage(Long chatId){
        startUserPage.put("Channels", "/channels");
//        startUserPage.put("Top", "/listTop:1");
//        startUserPage.put("Crypto signals", "/listCryptoSignals");
//        startUserPage.put("Airdrop/Retrodrop", "/listAirdropRetrodrop");
//        startUserPage.put("News", "/listNews");
//        startUserPage.put("Add channel", "/addChannel");
        startUserPage.put("Account", "/Account");
        SendMessage val = SendMessage.builder().
                text("Welcome to {Bot Name}")
                .chatId(chatId)
                .replyMarkup(keyboardFactory.getOneThreeTwoMarkup(startUserPage))
                .build();
        startUserPage.clear();
        return val;
    }

    public SendMessage buildStartAdminPage(Long chatId){
        startUserPage.put("Top", "/listTop:1");
        startAdminPage.put("Crypto signals", "/listCryptoSignals");
        startAdminPage.put("Airdrop/Retrodrop", "/listAirdropRetrodrop");
        startAdminPage.put("News", "/listNews");
        startAdminPage.put("Add channel", "/addChannel");
        startAdminPage.put("Requests", "/requests");
        SendMessage val = SendMessage.builder().
                text("text")
                .chatId(chatId)
                .replyMarkup(keyboardFactory.getOneThreeTwoMarkup(startAdminPage))
                .build();
        startAdminPage.clear();
        return val;
    }

    public EditMessageText buildMainUserPage(Update update, Long chatId){
        mainUserPage.put("Channels", "/channels");
//        mainUserPage.put("Top", "/listTop:1");
//        mainUserPage.put("Crypto signals", "/listCryptoSignals");
//        mainUserPage.put("Airdrop/Retrodrop", "/listAirdropRetrodrop");
//        mainUserPage.put("News", "/listNews");
//        mainUserPage.put("Add channel", "/addChannel");
        mainUserPage.put("Account", "/Account");
        EditMessageText val = EditMessageText.builder()
                .text("Welcome to {Bot Name}")
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .replyMarkup(keyboardFactory.getOneThreeTwoMarkup(mainUserPage))
                .build();
        mainUserPage.clear();
        return val;
    }

    public EditMessageText buildMainChannelPage(Update update){
        channelPage.put("Top", "/listTop:1");
        channelPage.put("Crypto signals", "/listCryptoSignals");
        channelPage.put("Airdrop/Retrodrop", "/listAirdropRetrodrop");
        channelPage.put("News", "/listNews");
        channelPage.put("Back", "/Back");
        EditMessageText val = EditMessageText.builder().text("Select a category to view")
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .replyMarkup(keyboardFactory.getMarkap(channelPage))
                .build();
        channelPage.clear();
        return val;
    }

    public EditMessageText buildMainAdminPage(Update update, Long chatId){
        mainAdminPage.put("Crypto signals", "/listCryptoSignals");
        mainAdminPage.put("Airdrop/Retrodrop", "/listAirdropRetrodrop");
        mainAdminPage.put("News", "/listNews");
        mainAdminPage.put("Add channel", "/addChannel");
        mainAdminPage.put("Requests", "/requests");
        EditMessageText val = EditMessageText.builder()
                .text("text")
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .replyMarkup(keyboardFactory.getOneThreeTwoMarkup(mainAdminPage))
                .build();
        mainAdminPage.clear();
        return val;
    }

    public EditMessageText buildFirstAddingChannelPage(Update update, Long chatId){
        FirstAddingChannelPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .chatId(chatId)
                .text("Input channel name")
                .replyMarkup(keyboardFactory.getMarkap(FirstAddingChannelPage))
                .build();
    }

    public EditMessageText buildSecondAddingChannelPage(Long chatId, int messageId){
        SecondAddingChannelPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text("Input channel link")
                .replyMarkup(keyboardFactory.getMarkap(SecondAddingChannelPage))
                .build();
    }

    public EditMessageText buildThirdAddingChannelPage(Long chatId, int messageId){
        ThirdAddingChannelPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text("Input Screenshot of statistic")
                .replyMarkup(keyboardFactory.getMarkap(ThirdAddingChannelPage))
                .build();
    }

    public EditMessageText buildFourthAddingChannelPage(Long chatId, int messageId){
        FourthAddingChannelPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text("Input Screenshot of users")
                .replyMarkup(keyboardFactory.getMarkap(FourthAddingChannelPage))
                .build();
    }


    public EditMessageText buildFifthAddingChannelPage(Long chatId, int messageId){
        FifthAddingChannelPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text("Input admin link")
                .replyMarkup(keyboardFactory.getMarkap(FourthAddingChannelPage))
                .build();
    }

    public EditMessageText buildSixthAddingChannelPage(Long chatId, int messageId){
        chooseCategoryPage.put("Crypto signals", "/chooseCS");
        chooseCategoryPage.put("Airdrop/Retrodrop","/chooseAR");
        chooseCategoryPage.put("News","/chooseN");
        chooseCategoryPage.put("Back", "/Back");
        return EditMessageText.builder()
                .messageId(messageId)
                .chatId(chatId)
                .text("Choose category")
                .replyMarkup(keyboardFactory.getMarkap(chooseCategoryPage))
                .build();
    }

    public DeleteMessage deleteMessage(Update update, Long Chatid){
        return update.hasCallbackQuery()?
                DeleteMessage.builder()
                .messageId(update.getCallbackQuery().getMessage().getMessageId()).chatId(Chatid).build():
                DeleteMessage.builder()
                        .messageId(update.getMessage().getMessageId()).chatId(Chatid).build();
    }

    public DeleteMessage deleteMessageById(Long Chatid, int id){
        return DeleteMessage.builder()
                .messageId(id)
                .chatId(Chatid)
                .build();
    }

    public EditMessageText buildAccountPage(Long chatId, int messageId){
        List<Channel> channels = channelService.getChannelsByUser(userService.getUserByChatId(chatId));
        boolean isEmpty = true;
        if(!channels.isEmpty()){
            isEmpty = false;
        }
        if(!isEmpty){
            for(Channel channel:  channels){
                AccountPage.put("Your channel: " +channel.getChannelName(), "/usersChannelInfo:"+channel.getId());
            }
        }
        AccountPage.put("Add channel", "/addChannel");
        AccountPage.put("Back", "/BackToMainMenu");
        EditMessageText val = EditMessageText.builder()
                .text("User: " + userService.getUserByChatId(chatId).getName()
                        +"\nNumber of your channels: "+channels.size()
                        +"\nChannels:")
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(keyboardFactory.getMarkap(AccountPage))
                .build();
        AccountPage.clear();
        return val;
    }

    public EditMessageText buildCategoryPage(Update update, int messageId, String category){
        List<Channel> list= channelService.getAllChannelsSortedByRate();
        List<Channel> channels = list.stream().filter(x->x.getCategory().equals(category)).collect(Collectors.toList());
        boolean isEmpty = true;
        if(!channels.isEmpty()){
            isEmpty = false;
        }
        if(!isEmpty){
            for(Channel channel:  channels){
                categoryPage.put(channel.getChannelName(), "/channelInfo:"+channel.getId());
            }
        }
        categoryPage.put("Back", "/channels");

        EditMessageText val = EditMessageText.builder()
                .text("List:")
                .messageId(messageId)
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .replyMarkup(keyboardFactory.getMarkap(categoryPage))
                .build();

        categoryPage.clear();
        return val;
    }

    public EditMessageText buildTopChannelsPage(Update update, int messageId, int page){
        List<Channel> channels = channelService.getAllChannelsSortedByRate();
        String text;
        if(!channels.isEmpty()){
            text = "List of channels:";
            int startIndex = (page - 1) * 1;
            int endIndex = Math.min(startIndex + 1, channels.size());
            for(int i = startIndex; i < endIndex; i++){
                TopChannelsPage.put(channels.get(i).getChannelName(), "/channelInfo:"+channels.get(i).getId());
            }
            if(!(page > 1) && !(endIndex < channels.size())){
                TopChannelsPage.put("Back", "/channels");
            }
            else{
                if (page > 1) {
                    TopChannelsPage.put("<-", "/listTop:"+(page-1));
                }else{
                    TopChannelsPage.put(" ", "/empty1");
                }
                if (endIndex < channels.size()) {
                    TopChannelsPage.put("->", "/listTop:"+(page+1));
                }else{
                    TopChannelsPage.put("  ", "/empty2");
                }
                TopChannelsPage.put("Back", "/channels");
            }
        } else{
            text = "Sorry, the channels have not been added";
            TopChannelsPage.put("Back", "/channels");
        }


        EditMessageText val = EditMessageText.builder()
                .text(text)
                .messageId(messageId)
                .chatId(getChatId(update))
                .replyMarkup(keyboardFactory.getManyTwoOneMarkup(TopChannelsPage))
                .build();
        TopChannelsPage.clear();
        return val;
    }

    public SendPhoto buildFirstPhotoOfSelectedChannel(Update update) {
        try {
            Long id = update.getCallbackQuery().getMessage().getChatId();
            File photoFile1 = new File(channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLinkOnScreenshot1());
            FileInputStream photo1 = null;
            try {
                photo1 = new FileInputStream(photoFile1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            SendPhoto val = SendPhoto.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .photo(new InputFile(photo1, "statistic.png"))
                                    .build();
            return val;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public SendPhoto buildSecondPhotoOfSelectedChannel(Update update) {
        try {
            Long id = update.getCallbackQuery().getMessage().getChatId();
            File photoFile1 = new File(channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLinkOnScreenshot2());
            FileInputStream photo1 = null;
            try {
                photo1 = new FileInputStream(photoFile1);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            SendPhoto val = SendPhoto.builder()
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .photo(new InputFile(photo1, "statistic.png"))
                    .build();
            return val;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public SendMessage buildInfoOfSelectedChannel(Update update){
        SelectedChannelPage.put("Back", "/Back");
        Long id = update.getCallbackQuery().getMessage().getChatId();
        String text = "Channel name: "
                +channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getChannelName()
                +"\nChannel link: "
                +channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLink()
                +"\nChannel admin: "
                + channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLinkOnAdmin();
        SendMessage val = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(text)
                .replyMarkup(keyboardFactory.getMarkap(SelectedChannelPage))
                .build();
        SelectedChannelPage.clear();
        return val;
    }

    public SendMessage buildInfoOfSelectedUsersChannel(Update update){
        Long id = update.getCallbackQuery().getMessage().getChatId();
        String text= "Channel name: "
                +channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getChannelName();
        if(channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLink() != null){
            text = text +"\nChannel link: "
                    +channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLink();
            if(channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLinkOnAdmin() != null){
                text = text+"\nChannel admin: "
                        + channelService.getChannelById(userService.getUserByChatId(id).getSelectedChannel()).getLinkOnAdmin();
            }
        }
        SelectedUsersChannelPage.put("Edit channel", "/editChannel:"+getUser(update).getSelectedChannel());
        SelectedUsersChannelPage.put("Delete channel", "/deleteChannel:"+getUser(update).getSelectedChannel());
        SelectedUsersChannelPage.put("Back", "/Back");
        SendMessage val = SendMessage.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .text(text)
                .replyMarkup(keyboardFactory.getMarkap(SelectedUsersChannelPage))
                .build();
        SelectedUsersChannelPage.clear();
        return val;
    }

    private Long getChatId(Update update){
        return update.getCallbackQuery().getMessage().getChatId();
    }

    private User getUser(Update update){
        return userService.getUserByChatId(getChatId(update));
    }

    public void setUsingPage(Update update, String usingPage){
        if(!getUser(update).getUsingPage().isEmpty()){
            User user = getUser(update);
            user.setPreviousPage(getUser(update).getUsingPage());
            user.setUsingPage(usingPage);
            userService.update(user);
        }
        else{
            User user = getUser(update);
            user.setPreviousPage("");
            user.setUsingPage(usingPage);
            userService.update(user);
        }
    }

}
