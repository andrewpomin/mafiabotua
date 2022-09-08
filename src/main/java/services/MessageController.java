package services;

import main.Bot;
import game.Rules;
import buttons.ActionButtons;
import buttons.ConfirmationButtons;
import buttons.RegistrationButtons;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import settings.TimeSettings;

import java.util.Map;
import static java.lang.Thread.sleep;

public class MessageController extends Bot {
    public static long lastTime = 0;

    //Відправити повідомлення по id
    public synchronized void sendMessageById(long chatId, String text) throws InterruptedException {
        sleep(100);
        long timeNow = System.currentTimeMillis();

        //Не більше 20 повідомлень в секунду
        while ((timeNow - lastTime) < 50) {
            sleep(50);
            timeNow = System.currentTimeMillis();
        }

        //Не більше 20 повідомлень в один груповий чат на хвилину
        if (chatId < 0) {
            Service s = Storage.getService(chatId);
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
            lastTime = System.currentTimeMillis();
            if (chatId < 0) {
                Service s = Storage.getService(chatId);
                s.addInList(lastTime);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Відредагувати повідомлення
    public synchronized void editMessage(Update update, String setText, long groupId) throws InterruptedException {
        sleep(100);
        Service s = Storage.getService(groupId);
        EditMessageText edit = new EditMessageText();
        StringBuffer newText = new StringBuffer();
        long timeNow = System.currentTimeMillis();

        //Не більше 20 повідомлень в один груповий чат на хвилину
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        //Якщо повідомлення
        if (update.hasMessage()) {
            edit.setChatId(s.getCurrentGroupId());
            edit.setMessageId(s.getRegistrationMessageId());

            //Текст для повідомлення про реєстрацію
            if (update.getMessage().getText().startsWith("/start join") ||
                    update.getMessage().getText().startsWith("/addBot")) {
                RegistrationButtons r = new RegistrationButtons();
                edit.setReplyMarkup(r.createRegisterKeyboard(groupId));
                newText = new StringBuffer("*Реєстрацію розпочато*\n_Зареєструвалися:_");
                for (Map.Entry<Long, String> entry : s.getPlayersFullNames().entrySet()) {
                    if (entry.getKey() < 0) {
                        newText.append("\n").append(entry.getValue());
                    } else {
                        newText.append("\n").append("[").append(entry.getValue()).append("](tg://user?id=").
                                append(entry.getKey()).append(")");
                    }
                }
            } else {
                newText = new StringBuffer(setText);
            }

        //Якщо CallBack
        } else if (update.hasCallbackQuery()) {
            //Прибираємо повідомлення зі списку на видалення
            long chId = update.getCallbackQuery().getMessage().getChatId();
            int msgId = update.getCallbackQuery().getMessage().getMessageId();
            s.removeTemporaryMessages(chId, msgId);

            edit.setChatId(chId);
            edit.setMessageId(msgId);
            newText = new StringBuffer(setText);

            if (update.getCallbackQuery().getData().startsWith("/addBot")) {
                if (!setText.equals("Додаю...")) {
                    edit.setMessageId(s.getRegistrationMessageId());
                    RegistrationButtons r = new RegistrationButtons();
                    edit.setReplyMarkup(r.createRegisterKeyboard(groupId));
                    newText = new StringBuffer("*Реєстрацію розпочато*\n_Зареєструвалися:_");
                    for (Map.Entry<Long, String> entry : s.getPlayersFullNames().entrySet()) {
                        if (entry.getKey() < 0) {
                            newText.append("\n").append(entry.getValue());
                        } else {
                            newText.append("\n").append("[").append(entry.getValue()).append("](tg://user?id=").
                                    append(entry.getKey()).append(")");
                        }
                    }
                }
            }

            //Кнопка після вибору для комісара
            if (update.getCallbackQuery().getData().equals("Перевірити") ||
                    update.getCallbackQuery().getData().equals("Вбити")) {
                ActionButtons ab = new ActionButtons();
                long id = update.getCallbackQuery().getFrom().getId();
                edit.setReplyMarkup(ab.createActionMarkup(id, groupId));
                s.addTemporaryMessages(chId, msgId);
            }

            //Для вибору налаштування таймінгу
            if (update.getCallbackQuery().getData().startsWith("Ніч ", 8) ||
                    update.getCallbackQuery().getData().startsWith("День", 8) ||
                    update.getCallbackQuery().getData().startsWith("Vote", 8) ||
                    update.getCallbackQuery().getData().startsWith("Conf", 8)) {
                TimeSettings ts = new TimeSettings();
                edit.setReplyMarkup(ts.createTimeChoice(groupId));
            }

            //Кнопка назад
            if (setText.equals("Назад")) {
                TimeSettings ts = new TimeSettings();
                newText = new StringBuffer("*Оберіть налаштування:*");
                edit.setReplyMarkup(ts.createSettingsButtons(groupId));
            }

            //Оновлюємо кнопки після встановлення часу
            if (setText.equals("Вибрано")) {
                TimeSettings ts = new TimeSettings();
                if (s.isNightTime()) {
                    newText = new StringBuffer("*Тривалість ночі:*");
                } else if (s.isDayTime()) {
                    newText = new StringBuffer("*Тривалість дня:*");
                } else if (s.isVoteTime()) {
                    newText = new StringBuffer("*Тривалість голосування:*");
                } else if (s.isConfirmTime()) {
                    newText = new StringBuffer("*Тривалість підтвердження:*");
                }
                edit.setReplyMarkup(ts.createTimeChoice(groupId));
            }

            //При виборі кнопки Ок
            if (setText.equals("Ок")) {
                newText = new StringBuffer("Розумний вибір");
            }
        }

        edit.setParseMode(ParseMode.MARKDOWNV2);
        edit.enableMarkdown(true);
        edit.setText(newText.toString());

        try {
            execute(edit);
            lastTime = System.currentTimeMillis();
            if (update.hasCallbackQuery()) {
                if (update.getCallbackQuery().getMessage().getChatId() < 0) {
                    s.addInList(lastTime);
                }
            } else {
                if (update.getMessage().getChatId() < 0) {
                    s.addInList(lastTime);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Відредагувати повідомлення через текст
    public synchronized void editMessage(String text, long groupId) throws InterruptedException {
        sleep(100);
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        EditMessageText edit = new EditMessageText();
        edit.enableMarkdown(true);
        long timeNow = System.currentTimeMillis();

        //Не більше 20 повідомлень в один груповий чат на хвилину
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        //Оновлюємо кнопки при підтверджені
        if (text.equals("UpdateConfirm")) {
            long hunged = r.whoIsHanged();
            edit.setChatId(s.getCurrentGroupId());
            edit.setMessageId(s.getConfirmationToHungMessageId());
            if (hunged < 0) {
                edit.setText("Ви хочете повісити " +s.getPlayerMap(hunged) + "?");
            } else {
                edit.setText("Ви хочете повісити " + "[" + s.getPlayerMap(hunged) + "](tg://user?id=" + hunged + ")?");
            }
            ConfirmationButtons cb = new ConfirmationButtons();
            edit.setReplyMarkup(cb.createConfirmMarkup(groupId));
        }

        //Оновлюємо повідомлення після підтвердження
        if (text.equals("ResultsOfConfirmation")) {
            long hunged = r.whoIsHanged();
            edit.setChatId(s.getCurrentGroupId());
            edit.setMessageId(s.getConfirmationToHungMessageId());
            if (hunged < 0) {
                edit.setText("Ви хочете повісити " + s.getPlayerFullNames(hunged) + "?" +
                        "\n\nЗа  " + r.getConfirm() + "  " + EmojiParser.parseToUnicode(":vs:") +
                        "  Проти  " + r.getNotConfirm());
            } else {
                edit.setText("Ви хочете повісити " + "[" + s.getPlayerMap(hunged) + "](tg://user?id=" + hunged + ")?" +
                        "\n\nЗа  " + r.getConfirm() + "  " + EmojiParser.parseToUnicode(":vs:") +
                        "  Проти  " + r.getNotConfirm());
            }
        }

        edit.setParseMode(ParseMode.MARKDOWNV2);
        edit.enableMarkdown(true);

        try {
            execute(edit);
            lastTime = System.currentTimeMillis();
            s.addInList(lastTime);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Видалити повідомлення
    public synchronized void deleteMessage(Message message) throws InterruptedException {
        sleep(100);
        long timeNow = System.currentTimeMillis();

        //Не більше 20 повідомлень в секунду
        while ((timeNow - lastTime) < 50) {
            sleep(50);
            timeNow = System.currentTimeMillis();
        }

        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(message.getChatId());
        delete.setMessageId(message.getMessageId());

        try {
            execute(delete);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Видалити повідомлення по id
    public synchronized void deleteMessageById(int id, long groupId) throws InterruptedException {
        sleep(100);
        long timeNow = System.currentTimeMillis();

        //Не більше 20 повідомлень в секунду
        while ((timeNow - lastTime) < 50) {
            sleep(50);
            timeNow = System.currentTimeMillis();
        }

        Service s = Storage.getService(groupId);
        s.removeTemporaryMessages(groupId, id);

        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(groupId);
        delete.setMessageId(id);

        try {
            execute(delete);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Створити повідомлення хто проголосував
    public synchronized SendMessage sendWhoVoted(long groupId) throws InterruptedException {
        SendMessage sendMessage = new SendMessage();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);

        //Не більше 20 повідомлень в один груповий чат на хвилину
        long timeNow = System.currentTimeMillis();
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        if (s.messageWhoVoted != 0) {
            updateWhoConfirmed(groupId);
            return null;
        }

        //Оновлюємо повідомлення хто проголосував
        StringBuffer text = new StringBuffer("*Проголосували*:");
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getVotedRules(entry.getKey())) {
                if (entry.getKey() < 0) {
                    text.append("\n").append(s.getPlayerFullNames(entry.getKey()));
                } else {
                    text.append("\n").append("[").append(s.getPlayerFullNames(entry.getKey())).
                            append("](tg://user?id=").append(entry.getKey()).append(")");
                }
            }
        }

        sendMessage.setChatId(groupId);
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setText(String.valueOf(text));

        return sendMessage;
    }

    //Оновити повідомлення хто проголосував
    public synchronized void updateWhoVoted(long groupId) throws InterruptedException {
        EditMessageText edit = new EditMessageText();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);

        //Не більше 20 повідомлень в один груповий чат на хвилину
        long timeNow = System.currentTimeMillis();
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        //Оновлюємо повідомлення хто проголосував
        StringBuffer text = new StringBuffer("*Проголосували*:");
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getVotedRules(entry.getKey())) {
                if (entry.getKey() < 0) {
                    text.append("\n").append(s.getPlayerFullNames(entry.getKey()));
                } else {
                    text.append("\n").append("[").append(s.getPlayerFullNames(entry.getKey())).
                            append("](tg://user?id=").append(entry.getKey()).append(")");
                }
            }
        }

        edit.setChatId(groupId);
        edit.setMessageId(s.messageWhoVoted);
        edit.setParseMode(ParseMode.MARKDOWNV2);
        edit.enableMarkdown(true);
        edit.setText(String.valueOf(text));

        try {
            execute(edit);
            lastTime = System.currentTimeMillis();
            s.addInList(lastTime);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Створити повідомлення хто підтвердив
    public synchronized SendMessage sendWhoConfirmed(long groupId) throws InterruptedException {
        SendMessage sendMessage = new SendMessage();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);

        //Не більше 20 повідомлень в один груповий чат на хвилину
        long timeNow = System.currentTimeMillis();
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        if (s.messageWhoConfirmed != 0) {
            updateWhoConfirmed(groupId);
            return null;
        }

        //Оновлюємо повідомлення хто підтвердив
        StringBuffer text = new StringBuffer("*Рішуче визначилися*:");
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.areYouConfirmed(entry.getKey())) {
                if (entry.getKey() < 0) {
                    text.append("\n").append(s.getPlayerFullNames(entry.getKey()));
                } else {
                    text.append("\n").append("[").append(s.getPlayerFullNames(entry.getKey())).
                            append("](tg://user?id=").append(entry.getKey()).append(")");
                }
            }
        }

        sendMessage.setChatId(groupId);
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setText(String.valueOf(text));

        return sendMessage;
    }

    //Оновити повідомлення хто підтвердив
    public synchronized void updateWhoConfirmed(long groupId) throws InterruptedException {
        EditMessageText edit = new EditMessageText();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);

        //Не більше 20 повідомлень в один груповий чат на хвилину
        long timeNow = System.currentTimeMillis();
        if (groupId < 0) {
            if (s.last20Messages.size() >= 20) {
                while ((timeNow - s.getMin()) <= 60000) {
                    sleep(1000);
                    timeNow = System.currentTimeMillis();
                }
            }
        }

        //Оновлюємо повідомлення хто підтвердив
        StringBuffer text = new StringBuffer("*Рішуче визначилися*:");
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.areYouConfirmed(entry.getKey())) {
                if (entry.getKey() < 0) {
                    text.append("\n").append(s.getPlayerFullNames(entry.getKey()));
                } else {
                    text.append("\n").append("[").append(s.getPlayerFullNames(entry.getKey())).
                            append("](tg://user?id=").append(entry.getKey()).append(")");
                }
            }
        }

        edit.setChatId(groupId);
        edit.setMessageId(s.messageWhoConfirmed);
        edit.setParseMode(ParseMode.MARKDOWNV2);
        edit.enableMarkdown(true);
        edit.setText(String.valueOf(text));

        try {
            execute(edit);
            lastTime = System.currentTimeMillis();
            s.addInList(lastTime);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
