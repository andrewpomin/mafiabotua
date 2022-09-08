package main;

import game.Action;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    //Метод для прийому повідомлень
    @Override
    public synchronized void onUpdateReceived(Update update) {
        Action a = new Action();
        if (update.hasMessage()) {
            try {
                a.action(update);
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            try {
                a.actionCallBack(update);
            } catch (TelegramApiException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Повертає ім'я бота
    @Override
    public String getBotUsername() {
        return "@ua_mafia_bot";
    }

    //Повертає токен бота
    @Override
    public String getBotToken() {
        BotToken token = new BotToken();
        return token.getBotToken();
    }
}