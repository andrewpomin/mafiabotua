package buttons;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.ArrayList;

public class RegistrationButtons {
    //Створити клавіатуру реєстрації
    public InlineKeyboardMarkup createRegisterKeyboard(long groupId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(); //Створюємо кнопку

        inlineKeyboardButton.setText("Зареєструватися"); //Текст кнопки
        inlineKeyboardButton.setUrl("https://t.me/ua_mafia_bot?start=join" + groupId);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо рядок

        keyboardButtonsRow1.add(inlineKeyboardButton); //Додаємо кнопку в рядок

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів
        rowList.add(keyboardButtonsRow1); //Додаємо рядок в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        return inlineKeyboardMarkup;
    }

    //Створення повідомлення про реєстрацію
    public SendMessage createRegistration(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("*Реєстрацію розпочато*");
        sendMessage.setChatId(chatId); //Встановлюємо id чату
        sendMessage.setReplyMarkup(createRegisterKeyboard(Long.parseLong(chatId)));
        return sendMessage;
    }

    //Створення клавіатури вибору ботів
    public SendMessage createBotCountChose(long groupId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton1.setText("1"); //Текст кнопки
        inlineKeyboardButton1.setCallbackData("/addBot1"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton2.setText("2"); //Текст кнопки
        inlineKeyboardButton2.setCallbackData("/addBot2"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton3.setText("3"); //Текст кнопки
        inlineKeyboardButton3.setCallbackData("/addBot3"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton4.setText("4"); //Текст кнопки
        inlineKeyboardButton4.setCallbackData("/addBot4"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton5.setText("5"); //Текст кнопки
        inlineKeyboardButton5.setCallbackData("/addBot5"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton6.setText("6"); //Текст кнопки
        inlineKeyboardButton6.setCallbackData("/addBot6"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton7.setText("7"); //Текст кнопки
        inlineKeyboardButton7.setCallbackData("/addBot7"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton8.setText("8"); //Текст кнопки
        inlineKeyboardButton8.setCallbackData("/addBot8"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton9 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton9.setText("9"); //Текст кнопки
        inlineKeyboardButton9.setCallbackData("/addBot9"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton10 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton10.setText("10"); //Текст кнопки
        inlineKeyboardButton10.setCallbackData("/addBot10"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton11 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton11.setText("11"); //Текст кнопки
        inlineKeyboardButton11.setCallbackData("/addBot11"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton12 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton12.setText("12"); //Текст кнопки
        inlineKeyboardButton12.setCallbackData("/addBot12"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton13 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton13.setText("13"); //Текст кнопки
        inlineKeyboardButton13.setCallbackData("/addBot13"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton14 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton14.setText("14"); //Текст кнопки
        inlineKeyboardButton14.setCallbackData("/addBot14"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton15 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton15.setText("15"); //Текст кнопки
        inlineKeyboardButton15.setCallbackData("/addBot15"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton16 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton16.setText("16"); //Текст кнопки
        inlineKeyboardButton16.setCallbackData("/addBot16"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton17 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton17.setText("17"); //Текст кнопки
        inlineKeyboardButton17.setCallbackData("/addBot17"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton18 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton18.setText("18"); //Текст кнопки
        inlineKeyboardButton18.setCallbackData("/addBot18"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton19 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton19.setText("19"); //Текст кнопки
        inlineKeyboardButton19.setCallbackData("/addBot19"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton20 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton20.setText("20"); //Текст кнопки
        inlineKeyboardButton20.setCallbackData("/addBot20"); //Відгук на натискання

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>(); //Створюємо ряд

        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в ряд
        keyboardButtonsRow1.add(inlineKeyboardButton2); //Додаємо кнопку в ряд
        keyboardButtonsRow1.add(inlineKeyboardButton3); //Додаємо кнопку в ряд
        keyboardButtonsRow1.add(inlineKeyboardButton4); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton5); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton6); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton7); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton8); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton9); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton10); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton11); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton12); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton13); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton14); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton15); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton16); //Додаємо кнопку в ряд
        keyboardButtonsRow5.add(inlineKeyboardButton17); //Додаємо кнопку в ряд
        keyboardButtonsRow5.add(inlineKeyboardButton18); //Додаємо кнопку в ряд
        keyboardButtonsRow5.add(inlineKeyboardButton19); //Додаємо кнопку в ряд
        keyboardButtonsRow5.add(inlineKeyboardButton20); //Додаємо кнопку в ряд


        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow2); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow3); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow4); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow5); //Додаємо ряд в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("*Скільки ботів додати?*");
        sendMessage.setChatId(groupId); //Встановлюємо id чату
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public SendMessage createWantRole(long id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(); //Створюємо розмітку клавіатури
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(); //Створюємо список рядів

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton1.setText("Дон"); //Текст кнопки
        inlineKeyboardButton1.setCallbackData("/wantRoleDon"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton2.setText("Мафія"); //Текст кнопки
        inlineKeyboardButton2.setCallbackData("/wantRoleMafia"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton3.setText("Лікар"); //Текст кнопки
        inlineKeyboardButton3.setCallbackData("/wantRoleDoctor"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton4.setText("Мирний житель"); //Текст кнопки
        inlineKeyboardButton4.setCallbackData("/wantRoleCivilian"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton5.setText("Комісар Катані"); //Текст кнопки
        inlineKeyboardButton5.setCallbackData("/wantRoleCommissioner"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton6.setText("Коханка"); //Текст кнопки
        inlineKeyboardButton6.setCallbackData("/wantRoleWhore"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton7.setText("Переслідувач"); //Текст кнопки
        inlineKeyboardButton7.setCallbackData("/wantRolePersecutor"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton8.setText("Адвокат"); //Текст кнопки
        inlineKeyboardButton8.setCallbackData("/wantRoleLawyer"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton9 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton9.setText("Сержант"); //Текст кнопки
        inlineKeyboardButton9.setCallbackData("/wantRoleSergeant"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton10 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton10.setText("Самурай"); //Текст кнопки
        inlineKeyboardButton10.setCallbackData("/wantRoleSamurai"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton11 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton11.setText("Маніяк"); //Текст кнопки
        inlineKeyboardButton11.setCallbackData("/wantRoleManiac"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton12 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton12.setText("Приречений"); //Текст кнопки
        inlineKeyboardButton12.setCallbackData("/wantRoleSuicide"); //Відгук на натискання

        InlineKeyboardButton inlineKeyboardButton13 = new InlineKeyboardButton(); //Створюємо кнопку
        inlineKeyboardButton13.setText("Випадкова"); //Текст кнопки
        inlineKeyboardButton13.setCallbackData("/wantRoleRandom"); //Відгук на натискання

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow6 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow7 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow8 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow9 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow10 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow11 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow12 = new ArrayList<>(); //Створюємо ряд
        List<InlineKeyboardButton> keyboardButtonsRow13 = new ArrayList<>(); //Створюємо ряд

        keyboardButtonsRow1.add(inlineKeyboardButton1); //Додаємо кнопку в ряд
        keyboardButtonsRow2.add(inlineKeyboardButton2); //Додаємо кнопку в ряд
        keyboardButtonsRow3.add(inlineKeyboardButton3); //Додаємо кнопку в ряд
        keyboardButtonsRow4.add(inlineKeyboardButton4); //Додаємо кнопку в ряд
        keyboardButtonsRow5.add(inlineKeyboardButton5); //Додаємо кнопку в ряд
        keyboardButtonsRow6.add(inlineKeyboardButton6); //Додаємо кнопку в ряд
        keyboardButtonsRow7.add(inlineKeyboardButton7); //Додаємо кнопку в ряд
        keyboardButtonsRow8.add(inlineKeyboardButton8); //Додаємо кнопку в ряд
        keyboardButtonsRow9.add(inlineKeyboardButton9); //Додаємо кнопку в ряд
        keyboardButtonsRow10.add(inlineKeyboardButton10); //Додаємо кнопку в ряд
        keyboardButtonsRow11.add(inlineKeyboardButton11); //Додаємо кнопку в ряд
        keyboardButtonsRow12.add(inlineKeyboardButton12); //Додаємо кнопку в ряд
        keyboardButtonsRow13.add(inlineKeyboardButton13); //Додаємо кнопку в ряд

        rowList.add(keyboardButtonsRow1); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow2); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow3); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow4); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow5); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow6); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow7); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow8); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow9); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow10); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow11); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow12); //Додаємо ряд в список
        rowList.add(keyboardButtonsRow13); //Додаємо ряд в список

        inlineKeyboardMarkup.setKeyboard(rowList); //Встановлюємо клавіатуру

        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage.enableMarkdown(true);
        sendMessage.setText("Яку роль хочеш?");
        sendMessage.setChatId(id); //Встановлюємо id чату
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }
}
