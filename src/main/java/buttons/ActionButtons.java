package buttons;

import game.Rules;
import services.Storage;
import services.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionButtons {
    //Створюємо кнопки з гравцями
    public synchronized InlineKeyboardMarkup createActionMarkup(long chatId, long groupId) {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        //Додаємо всіх гравців
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            //Себе додаємо тільки лікарю
            if ((entry.getKey() == chatId) && (!r.getRole(entry.getKey()).equals("Doctor"))) {continue;}
            //Себе не додаємо, якщо лікувались минулої ночі
            if ((entry.getKey() == chatId) && r.getRole(entry.getKey()).equals("Doctor") &&
                    r.isDoctorVisitedLastNight(entry.getKey())) {
                continue;
            }

            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(); //Створюємо кнопку
            inlineKeyboardButton.setText(s.getPlayerFullNames(entry.getKey())); //Текст кнопки
            inlineKeyboardButton.setCallbackData("Н" + entry.getKey()); //Відгук на натискання
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
            keyboardButtonsRow1.add(inlineKeyboardButton); //Додаємо кнопку в ряд
            rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        }

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        return inlineKeyboardMarkup;
    }

    //Створюємо клавіатуру для вибору комісаром дії
    public synchronized SendMessage createChoiceForCommissioner(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton1.setText("Перевірити"); //Текст кнопки
        inlineKeyboardButton1.setCallbackData("Перевірити"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton2.setText("Вбити"); //Текст кнопки
        inlineKeyboardButton2.setCallbackData("Вбити"); //Відгук на натискання

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в ряд
        keyboardButtonsRow1.add(inlineKeyboardButton2); //Додаємо кнопку в ряд
        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Ви хочете перевірити гравця чи ліквідувати його?");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    //Створюємо клавіатуру для вибору користувачем гравців вночі
    public synchronized SendMessage createActionButtons(long chatId, String text, long groupId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(createActionMarkup(chatId, groupId));

        return sendMessage;
    }

    //Створюємо клавіатуру для вибору користувачем гравців вдень
    public synchronized SendMessage createVotingButtons(long chatId, String text, long groupId) {
        Service s = Storage.getService(groupId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        //Додаємо всіх гравців
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (entry.getKey() == chatId) {
                continue;
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(); //Створюємо кнопку
            inlineKeyboardButton.setText(s.getPlayerFullNames(entry.getKey())); //Текст кнопки
            inlineKeyboardButton.setCallbackData("Д" + entry.getKey()); //Відгук на натискання
            List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
            keyboardButtonsRow1.add(inlineKeyboardButton); //Додаємо кнопку в ряд
            rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        }

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    //Створюємо клавіатуру для мирних жителів уночі
    public synchronized SendMessage createButtonsForCivilian(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton.setText("Новини"); //Текст кнопки
        inlineKeyboardButton.setCallbackData("Новини"); //Відгук на натискання
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        keyboardButtonsRow1.add(inlineKeyboardButton); //Додаємо кнопку в ряд
        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("Можете перед сном почитати газету:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }
}
