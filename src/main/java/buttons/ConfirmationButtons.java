package buttons;

import game.Rules;
import services.Storage;
import services.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.ArrayList;

public class ConfirmationButtons {
    //Створюємо клавіатуру для підтвердження вибору
    public synchronized InlineKeyboardMarkup createConfirmMarkup(long groupId) {
        Rules r = Storage.getRules(groupId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        if (r.getConfirm() == 0) {inlineKeyboardButton1.setText("О, так");}  //Текст кнопки
        else {inlineKeyboardButton1.setText("О, так  " + r.getConfirm());} //Текст кнопки
        inlineKeyboardButton1.setCallbackData("Так"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(); //Створюємо кнопку
        if (r.getNotConfirm() == 0) {inlineKeyboardButton2.setText("Та мабуть ні");} //Текст кнопки
        else {inlineKeyboardButton2.setText("Та мабуть ні  " + r.getNotConfirm());} //Текст кнопки
        inlineKeyboardButton2.setCallbackData("Ні"); //Відгук на натискання

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в ряд
        keyboardButtonsRow1.add(inlineKeyboardButton2); //Додаємо кнопку в ряд
        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        return inlineKeyboardMarkup;
    }

    //Створюємо підтвердження результатів голосування
    public synchronized SendMessage createConfirmButton(long groupId) {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        long hanged = r.whoIsHanged();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(s.getCurrentGroupId());
        if (hanged < 0) {
            sendMessage.setText("Ви хочете повісити " + s.getPlayerFullNames(hanged) + "?" +
                    "\nНа підтвердження є " + s.getConfirmTime() + " секунд.");
        } else {
            sendMessage.setText("Ви хочете повісити " + "[" + s.getPlayerMap(hanged) + "](tg://user?id=" + hanged + ")?" +
                    "\nНа підтвердження є " + s.getConfirmTime() + " секунд.");
        }
        sendMessage.setReplyMarkup(createConfirmMarkup(groupId));

        return sendMessage;
    }
}
