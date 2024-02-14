package com.example.Bot.telegram.factories;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class KeyboardFactory {
    public InlineKeyboardMarkup getMarkap(Map<String, String> buttons){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(Map.Entry<String, String> item : buttons.entrySet()){
            List<InlineKeyboardButton> button = new ArrayList<>();
            button.add(InlineKeyboardButton.builder()
                    .text(item.getKey())
                    .callbackData(item.getValue())
                    .build());
            rowList.add(button);
        }
        markup.setKeyboard(rowList);
        return markup;
    }


    public InlineKeyboardMarkup getTripleLineMarkup(Map<String, String> buttons){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            currentRow.add(InlineKeyboardButton.builder()
                    .text(entry.getKey())
                    .callbackData(entry.getValue())
                    .build());

            if (currentRow.size() == 3) {
                rowList.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        if (!currentRow.isEmpty()) {
            rowList.add(currentRow);
        }

        markup.setKeyboard(rowList);
        return markup;
    }

    public InlineKeyboardMarkup getOneThreeTwoMarkup(Map<String, String> buttons){
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        int counter = 1;
        for(Map.Entry<String, String> entry : buttons.entrySet()){
            currentRow.add(InlineKeyboardButton.builder()
                    .text(entry.getKey())
                    .callbackData(entry.getValue())
                    .build());

            if(counter == 1 && currentRow.size() == 1){
                rowList.add(currentRow);
                currentRow = new ArrayList<>();
                counter++;
            }else if(counter == 2 && currentRow.size() == 3){
                rowList.add(currentRow);
                currentRow = new ArrayList<>();
                counter++;
            } else if(counter == 3 && currentRow.size() == 2) {
                rowList.add(currentRow);
                currentRow = new ArrayList<>();
                counter++;
            }
        }
        if (!currentRow.isEmpty()) {
            rowList.add(currentRow);
        }
        markup.setKeyboard(rowList);
        return markup;
    }

    public InlineKeyboardMarkup getManyTwoOneMarkup(Map<String, String> buttons){
        int totalButtons = buttons.size();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        int counter = 0;
        for(Map.Entry<String, String> entry : buttons.entrySet()){
            counter++;
            if (counter <= totalButtons - 3) {
                currentRow.add(InlineKeyboardButton.builder()
                        .text(entry.getKey())
                        .callbackData(entry.getValue())
                        .build());

                rowList.add(currentRow);
                currentRow = new ArrayList<>();
            } else {

                currentRow.add(InlineKeyboardButton.builder()
                        .text(entry.getKey())
                        .callbackData(entry.getValue())
                        .build());

                if (counter >= totalButtons - 1) {
                    rowList.add(currentRow);
                    currentRow = new ArrayList<>();
                }
            }
        }

        if (!currentRow.isEmpty()) {
            rowList.add(currentRow);
        }
        markup.setKeyboard(rowList);
        return markup;
    }
}
