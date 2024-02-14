package com.example.Bot.telegram.interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ICommand {
    Object execute(Update update);
}
