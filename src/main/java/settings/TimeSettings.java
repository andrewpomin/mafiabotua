package settings;

import services.Storage;
import services.Service;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TimeSettings {
    //Створити кнопки вибору часу
    public InlineKeyboardMarkup createTimeChoice(long groupId) {
        Service s = Storage.getService(groupId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури

        //Який зараз час встановлено для періоду
        int time = 0;
        if (s.isNightTime()) {
            time = s.getNightTime();
        } else if (s.isDayTime()) {
            time = s.getDayTime();
        } else if (s.isVoteTime()) {
            time = s.getVoteTime();
        } else if (s.isConfirmTime()) {
            time = s.getConfirmTime();
        }

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        if (time == 20) {inlineKeyboardButton1.setText("20" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton1.setText("20");} //Текст кнопки
        inlineKeyboardButton1.setCallbackData("/setting0020" + groupId); //Відгук кнопки

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        if (time == 30) {inlineKeyboardButton2.setText("30" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton2.setText("30");}
        inlineKeyboardButton2.setCallbackData("/setting0030" + groupId);

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        if (time == 45) {inlineKeyboardButton3.setText("45" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton3.setText("45");}
        inlineKeyboardButton3.setCallbackData("/setting0045" + groupId);

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        if (time == 60) {inlineKeyboardButton4.setText("60" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton4.setText("60");}
        inlineKeyboardButton4.setCallbackData("/setting0060" + groupId);

        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        if (time == 90) {inlineKeyboardButton5.setText("90" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton5.setText("90");}
        inlineKeyboardButton5.setCallbackData("/setting0090" + groupId);

        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        if (time == 120) {inlineKeyboardButton6.setText("120" + EmojiParser.parseToUnicode(":white_small_square:"));}
        else {inlineKeyboardButton6.setText("120");}
        inlineKeyboardButton6.setCallbackData("/setting0120" + groupId);

        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
        inlineKeyboardButton7.setText("Назад");
        inlineKeyboardButton7.setCallbackData("/settingBack" + groupId);

        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton();
        inlineKeyboardButton8.setText("Ок");
        inlineKeyboardButton8.setCallbackData("/settingDone" + groupId);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо рядок 1
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>(); //Створюємо рядок 2
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>(); //Створюємо рядок 3
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>(); //Створюємо рядок 4
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>(); //Створюємо рядок 5

        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в рядок 1
        keyboardButtonsRow1.add(inlineKeyboardButton2); //Додаємо кнопку в рядок 1
        keyboardButtonsRow2.add(inlineKeyboardButton3); //Додаємо кнопку в рядок 2
        keyboardButtonsRow2.add(inlineKeyboardButton4); //Додаємо кнопку в рядок 2
        keyboardButtonsRow3.add(inlineKeyboardButton5); //Додаємо кнопку в рядок 3
        keyboardButtonsRow3.add(inlineKeyboardButton6); //Додаємо кнопку в рядок 3
        keyboardButtonsRow4.add(inlineKeyboardButton7); //Додаємо кнопку в рядок 4
        keyboardButtonsRow5.add(inlineKeyboardButton8); //Додаємо кнопку в рядок 5

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів
        rowList.add(keyboardButtonsRow1); //Додаємо рядок 1 в список
        rowList.add(keyboardButtonsRow2); //Додаємо рядок 2 в список
        rowList.add(keyboardButtonsRow3); //Додаємо рядок 3 в список
        rowList.add(keyboardButtonsRow4); //Додаємо рядок 4 в список
        rowList.add(keyboardButtonsRow5); //Додаємо рядок 5 в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        return inlineKeyboardMarkup;
    }

    //Створити кнопки вибору періоду
    public synchronized InlineKeyboardMarkup createSettingsButtons(long groupId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton1.setText("Встановити час ночі"); //Текст кнопки
        inlineKeyboardButton1.setCallbackData("/settingНіч " + groupId); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton2.setText("Встановити час дня"); //Текст кнопки
        inlineKeyboardButton2.setCallbackData("/settingДень" + groupId); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton3.setText("Встановити час голосування"); //Текст кнопки
        inlineKeyboardButton3.setCallbackData("/settingVote" + groupId); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton4.setText("Встановити час підтвердження"); //Текст кнопки
        inlineKeyboardButton4.setCallbackData("/settingConf" + groupId); //Відгук на натискання

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>(); //Створюємо ряд

        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton2); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton3); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton4); //Додаємо кнопку в ряд

        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow2); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow3); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow4); //Додаємо ряд в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        return inlineKeyboardMarkup;
    }

    //Відправити повідомлення налаштувань
    public synchronized SendMessage createSettingsMenu(Update update) {
        long chatId = update.getMessage().getFrom().getId();
        long groupId = update.getMessage().getChatId();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("*Оберіть налаштування:*");
        sendMessage.setReplyMarkup(createSettingsButtons(groupId));

        return sendMessage;
    }
}
