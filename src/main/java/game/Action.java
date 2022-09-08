package game;

import services.Storage;
import services.Service;
import services.Randomizer;
import buttons.ActionButtons;
import settings.TimeSettings;
import services.MessageController;
import buttons.RegistrationButtons;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static java.lang.Thread.sleep;
import static services.MessageController.lastTime;

public class Action {
    long groupId;
    //Вибір користувачами гравців для взаємодії вночі
    public synchronized void actionNight(Update update) throws InterruptedException {
        MessageController mc = new MessageController();
        long id = update.getCallbackQuery().getFrom().getId(); //Від кого
        groupId = Storage.findGroupIdInService(update.getCallbackQuery().getMessage().getChatId()); //З якої групи
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        String role = r.getRole(update.getCallbackQuery().getFrom().getId()); //Роль
        String tempText;

        long chosen = 0;
        if (!(role.equals("Civilian") || role.equals("Sergeant") || role.equals("Suicide"))) {
            chosen = Long.parseLong(update.getCallbackQuery().getData().substring(1));
        }

        switch (role) {
            case "Don":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                if (s.getMafiaNotStartVote()) {
                    s.setMafiaStartVote(true);
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":busts_in_silhouette:") +
                            " *Мафія* вже зібралась на голосування.");
                }
                r.mafiaVotes(chosen, "Don");
                tempText = "Вважайте, що " + s.getPlayerMap(chosen) + " вже на тому світі, Доне " +
                        s.getPlayerMap(update.getCallbackQuery().getFrom().getId());
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                    if (r.getRole(entry.getKey()).equals("Mafia") &&
                            entry.getKey() != id && entry.getKey() > 0) {
                        mc.sendMessageById(entry.getKey(), "Дон " + s.getPlayerFullNames(id) + " хоче вбити " +
                                s.getPlayerFullNames(chosen) + ".");
                    }
                }
                s.addActiveRolesUsed();
                break;
            case "Mafia":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                if (s.getMafiaNotStartVote()) {
                    s.setMafiaStartVote(true);
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":busts_in_silhouette:") +
                            " *Мафія* вже зібралась на голосування.");
                }
                r.mafiaVotes(chosen, "Mafia");
                tempText = "Ваш голос враховано. Ви за " + s.getPlayerMap(chosen) + ".";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                    if ((r.getRole(entry.getKey()).equals("Don") ||
                            r.getRole(entry.getKey()).equals("Mafia")) &&
                            entry.getKey() != id && entry.getKey() > 0) {
                        mc.sendMessageById(entry.getKey(), s.getPlayerFullNames(id) + " пропонує вальнути " +
                                s.getPlayerFullNames(chosen) + ".");
                    }
                }
                s.addActiveRolesUsed();
                break;
            case "Doctor":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.doctorVotes(chosen);
                tempText = "Заводимо двигун. " + s.getPlayerMap(chosen) + " житиме!";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":man_health_worker:") +
                        " *Лікар* вже виїхав.");
                s.addActiveRolesUsed();
                break;
            case "Commissioner":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.commissionerVotes(chosen);
                if (r.choiceToKill) {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":detective:") +
                            " *Комісар Катані* вже дістав свій ствол.");
                    tempText = s.getPlayerMap(chosen) + " має померти. Назад шляху немає...";
                    mc.sendMessageById(id, tempText);
                    s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                } else {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":detective:") +
                            " *Комісар Катані* вже поряд, стерво.");
                    s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                    r.setCommissionerVisitedThisGame(chosen, true);
                }
                s.addActiveRolesUsed();
                break;
            case "Whore":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.whoreVotes(chosen);
                tempText = "Ходімо, " + s.getPlayerMap(chosen) + " буде щасливий тебе бачити.";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":underage:") +
                        " *Коханка* вже обрала щасливчика.");
                s.addActiveRolesUsed();
                break;
            case "Maniac":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.maniacVotes(chosen);
                tempText = s.getPlayerMap(chosen) + " навіть не дізнається, чим його вдарило.";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":knife:") +
                        " *Маніяк* уже сховався в кущах.");
                s.addActiveRolesUsed();
                break;
            case "Lawyer":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.lawyerVotes(chosen);
                tempText = "Липового свідка вже знайдено. " + s.getPlayerMap(chosen) + " може не боятися суду.";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":man_judge:") +
                        " *Адвокат* уже зібрав фальшиві докази.");
                s.addActiveRolesUsed();
                break;
            case "Persecutor":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.persecutorVotes(chosen);
                tempText = "Ти вже бачиш квартиру де живе " + s.getPlayerMap(chosen);
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":eyes:") +
                        " *Переслідувач* за кимось спостерігає.");
                s.addActiveRolesUsed();
                break;
            case "Samurai":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                r.setChosen(id, chosen);
                r.samuraiVotes(chosen);
                tempText = "У самурая немає цілі, є тільки шлях. " + s.getPlayerMap(chosen) + " буде жити, завдяки " +
                        "твоїй жертві.";
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":kimono:") +
                        " *Самурай* вже когось закриває своїм тілом.");
                s.addActiveRolesUsed();
                break;
            case "Civilian":
            case "Sergeant":
            case "Suicide":
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                Randomizer randomizer = new Randomizer();
                tempText = randomizer.getRandomNews();
                mc.sendMessageById(id, tempText);
                s.removeTemporaryMessages(id, update.getCallbackQuery().getMessage().getMessageId());
                break;
        }
    }

    //Вибір користувачами гравців для взаємодії вдень
    public synchronized void actionDay(Update update) throws TelegramApiException, InterruptedException {
        MessageController mc = new MessageController();
        groupId = Storage.findGroupIdInService(update.getCallbackQuery().getMessage().getChatId());
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        long id = Long.parseLong(update.getCallbackQuery().getData().substring(1));
        r.setVote(id);
        r.setVotedRules(update.getCallbackQuery().getFrom().getId());

        if (s.messageWhoVoted == 0) {
            s.setMessageWhoVoted(mc.execute(mc.sendWhoVoted(groupId)).getMessageId());
            lastTime = System.currentTimeMillis();
            s.addInList(lastTime);
        } else {
            mc.updateWhoVoted(groupId);
        }

        mc.editMessage(update, "Окей, найбільше за всіх Вам не подобається " + s.getPlayerMap(id), groupId);
        s.addPlayersVoted();
    }

    //Опрацювання повідомлень
    public synchronized void action(Update update) throws InterruptedException, TelegramApiException {
        String text = update.getMessage().getText();
        long id = update.getMessage().getFrom().getId(); //Від кого прийшло
        long fromChatId = update.getMessage().getChatId(); //З якого чату прийшло
        MessageController mc = new MessageController();
        Service s = new Service(fromChatId);
        Rules r = new Rules(fromChatId);

        //При реєстрації
        if (text.startsWith("/start join")) {
            groupId = Long.parseLong(text.substring(11));
            s = Storage.getService(groupId);
            r = Storage.getRules(groupId);

            if (s.getPlayersMap().containsKey(id)) {
                mc.sendMessageById(id, "Ви вже зареєстровані.");
                return;
            }

            String name = update.getMessage().getFrom().getFirstName();
            String lastName = update.getMessage().getFrom().getLastName();
            String fullName;
            if (lastName == null) {fullName = name;} else {fullName = name + " " + lastName;}
            r.addCounter();
            s.putPlayerInMap(id, name);
            s.putPlayerInFullNames(id, fullName);
            mc.editMessage(update, " ", groupId);
            mc.sendMessageById(update.getMessage().getFrom().getId(),
                    name + ", ви успішно приєднались");
            return;
        }

        //Перевіряємо чи груповий чат
        if (update.getMessage().getChat().isGroupChat()) {
            s = Storage.addService(s);
            r = Storage.addRules(r);
        } else {
            s = Storage.getService(Storage.findGroupIdInService(id));
            r = Storage.getRules(Storage.findGroupIdInService(id));
        }

        //Якщо чат груповий
        if (update.getMessage().getChat().isGroupChat()) {
            switch (text) {
                case "/register@ua_mafia_bot":
                case "/register":
                    s.clearAll();
                    RegistrationButtons register = new RegistrationButtons();
                    s.setRegistrationMessageId(mc.execute(register.createRegistration(update)).getMessageId());
                    return;
                case "/go@ua_mafia_bot":
                case "/go":
                    groupId = Storage.getService(fromChatId).getCurrentGroupId();
                    if (s.registrationMessageId == 0) {
                        mc.sendMessageById(update.getMessage().getChatId(), "Спочатку відкрийте реєстрацію.");
                    } else {
                        Game g = new Game();
                        g.game(groupId);
                        mc.deleteMessageById(s.getRegistrationMessageId(), groupId);
                    }
                    return;
                case "/end@ua_mafia_bot":
                case "/end":
                    if (s.isGameStart()) {
                        s.setForceEnd(true);
                        mc.sendMessageById(s.getCurrentGroupId(), "*Завершуємо гру...*");
                    } else {
                        if (s.getRegistrationMessageId() == 0) {
                            mc.sendMessageById(fromChatId, "Немає активної гри.");
                        } else {
                            mc.deleteMessageById(s.getRegistrationMessageId(), s.getCurrentGroupId());
                            mc.sendMessageById(fromChatId, "Немає активної гри.");
                        }
                    }
                    return;
                case "/settings@ua_mafia_bot":
                case "/settings":
                    TimeSettings ts = new TimeSettings();
                    mc.execute(ts.createSettingsMenu(update));
                    mc.deleteMessage(update.getMessage());
                    return;
                case "/rules@ua_mafia_bot":
                case "/rules":
                    s.sendRules(update.getMessage().getChatId());
                    return;
                case "/help@ua_mafia_bot":
                case "/help":
                    s.sendHelp(update.getMessage().getChatId());
                    return;
                case "/add_bots@ua_mafia_bot":
                case "/add_bots":
                    groupId = Storage.getService(fromChatId).getCurrentGroupId();
                    if (s.registrationMessageId == 0) {
                        mc.sendMessageById(update.getMessage().getChatId(), "Спочатку відкрийте реєстрацію.");
                    } else {
                        RegistrationButtons rb = new RegistrationButtons();
                        mc.execute(rb.createBotCountChose(update.getMessage().getChatId()));
                    }
                    return;
                case "/list@ua_mafia_bot":
                case "/list":
                    StringBuffer list = new StringBuffer("*Зареєструвалися:*");
                    for (Map.Entry<Long, String> entry : s.getPlayersFullNames().entrySet()) {
                        if (entry.getKey() < 0) {
                            list.append("\n").append(entry.getValue());
                        } else {
                            list.append("\n").append("[").append(entry.getValue()).append("](tg://user?id=").
                                    append(entry.getKey()).append(")");
                        }
                    }
                    mc.sendMessageById(s.getCurrentGroupId(), list.toString());
                    return;
            }

        //Якщо чат з користувачем
        } else {
            switch (text) {
                case "/register":
                case "/register@ua_mafia_bot":
                case "/go":
                case "/go@ua_mafia_bot":
                case "/end":
                case "/end@ua_mafia_bot":
                case "/settings":
                case "/add_bots@ua_mafia_bot":
                case "/add_bots":
                    mc.sendMessageById(update.getMessage().getFrom().getId(), "Відправ цю команду в груповий чат.");
                    return;
                case "/rules":
                    s.sendRules(update.getMessage().getFrom().getId());
                    return;
                case "/help":
                    s.sendHelp(update.getMessage().getFrom().getId());
                    return;
                case "/deleteAllData":
                    if (id == 401786507) {
                        Storage.clearRules();
                        Storage.clearService();
                        mc.sendMessageById(id, "Видалено всі дані.");
                    } else {
                        mc.sendMessageById(id, "Немає доступу.");
                    }
                    return;
            }

            if (update.getMessage().getText().startsWith("/wantRole")) {
                if (id == 401786507 || id == 333155517) {
                    RegistrationButtons rb = new RegistrationButtons();
                    mc.execute(rb.createWantRole(id));
                    return;
                }
            }
        }

        //Для фільтрування тексту під час гри
        if (s.isGameStart()) {
            actionGameStarted(text, update, s, r);
        }
    }

    //Опрацювання повідомлень під час гри
    public synchronized void actionGameStarted(String text, Update update, Service s, Rules r) throws InterruptedException {
        MessageController mc = new MessageController();
        long id = update.getMessage().getFrom().getId(); //Від кого

        if (s.gameStart && s.getPlayerFullNames(update.getMessage().getFrom().getId()).equals("")) {
            mc.deleteMessage(update.getMessage());
            mc.sendMessageById(id, "Ти не в грі, тому не пиши сюди. Поки що.");
            return;
        }

        //Відправити останнє слово
        if (!update.getMessage().getChat().isGroupChat() && r.isLastWord(id)) {
            mc.sendMessageById(r.getCurrentGroupId(), EmojiParser.parseToUnicode(":ghost:") +
                    " _" + s.getPlayerFullNames(id) + " залишає послання кров'ю на стіні: " + text + "_");
            mc.sendMessageById(id, "Про твоє послання дізналось усе місто.");
            r.clearLastWord(id);
            return;
        }
        //Заборонити мертвим писати в группу
        if ((update.getMessage().getChatId() == s.getCurrentGroupId()) && (!r.isAlive(id)) && (s.isGameStart())) {
            mc.sendMessageById(id, "Мертві не розказують казки.");
            mc.deleteMessage(update.getMessage());
            return;
        }
        //Вночі заборонити писати
        if ((r.isNight() && update.getMessage().getChatId() == s.getCurrentGroupId()) &&
                !update.getMessage().getText().equals("/end")) {
            mc.deleteMessage(update.getMessage());
            return;
        }
        //Відправляти повідомлення від мафії до команди
        if (r.getRole(id).equals("Don") || r.getRole(id).equals("Mafia")) {
            String mafiaText = update.getMessage().getText();
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if (entry.getKey() != id && (r.getRole(entry.getKey()).equals("Don") ||
                        r.getRole(entry.getKey()).equals("Mafia")) && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), s.getPlayerFullNames(id) + " каже: " + mafiaText);
                }
            }
            return;
        }

        //Відправляти повідомлення від поліції до команди
        if (r.getRole(id).equals("Commissioner") || r.getRole(id).equals("Sergeant")) {
            String policeText = update.getMessage().getText();
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if (entry.getKey() != id && (r.getRole(entry.getKey()).equals("Commissioner") ||
                        r.getRole(entry.getKey()).equals("Sergeant")) && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), s.getPlayerFullNames(id) + " каже: " + policeText);
                }
            }
        }
    }

    //Опрацювання данних CallBack
    public synchronized void actionCallBack(Update update) throws TelegramApiException, InterruptedException {
        MessageController mc = new MessageController();
        groupId = Storage.findGroupIdInService(update.getCallbackQuery().getMessage().getChatId());
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        String callbackQuery = update.getCallbackQuery().getData();
        long id = update.getCallbackQuery().getFrom().getId();

        //Для CallBack із групового чату
        if (update.getCallbackQuery().getMessage().getChat().isGroupChat()) {
            groupId = update.getCallbackQuery().getMessage().getChatId();
            s = Storage.getService(groupId);
            r = Storage.getRules(groupId);
        }

        if (s.gameStart && s.getPlayerFullNames(update.getCallbackQuery().getFrom().getId()).equals("")) {
            mc.sendMessageById(id, "Ти не в грі.");
            return;
        }

        //Додати ботів
        if (callbackQuery.startsWith("/addBot")) {
            addBots(update);
            return;
        }

        //Для переходу в налаштування
        if (callbackQuery.startsWith("/setting")) {
            actionSettings(update);
            return;
        }

        if (callbackQuery.startsWith("/wantRole")) {
            String role = callbackQuery.substring(9);
            if (id == 401786507) {
                switch (role) {
                    case "Don":
                        Service.myRole = "Don";
                        break;
                    case "Mafia":
                        Service.myRole = "Mafia";
                        break;
                    case "Doctor":
                        Service.myRole = "Doctor";
                        break;
                    case "Civilian":
                        Service.myRole = "Civilian";
                        break;
                    case "Commissioner":
                        Service.myRole = "Commissioner";
                        break;
                    case "Sergeant":
                        Service.myRole = "Sergeant";
                        break;
                    case "Whore":
                        Service.myRole = "Whore";
                        break;
                    case "Maniac":
                        Service.myRole = "Maniac";
                        break;
                    case "Lawyer":
                        Service.myRole = "Lawyer";
                        break;
                    case "Persecutor":
                        Service.myRole = "Persecutor";
                        break;
                    case "Samurai":
                        Service.myRole = "Samurai";
                        break;
                    case "Suicide":
                        Service.myRole = "Suicide";
                        break;
                }
            } else if (id == 333155517) {
                switch (role) {
                    case "Don":
                        Service.roleAlina = "Don";
                        break;
                    case "Mafia":
                        Service.roleAlina = "Mafia";
                        break;
                    case "Doctor":
                        Service.roleAlina = "Doctor";
                        break;
                    case "Civilian":
                        Service.roleAlina = "Civilian";
                        break;
                    case "Commissioner":
                        Service.roleAlina = "Commissioner";
                        break;
                    case "Sergeant":
                        Service.roleAlina = "Sergeant";
                        break;
                    case "Whore":
                        Service.roleAlina = "Whore";
                        break;
                    case "Maniac":
                        Service.roleAlina = "Maniac";
                        break;
                    case "Lawyer":
                        Service.roleAlina = "Lawyer";
                        break;
                    case "Persecutor":
                        Service.roleAlina = "Persecutor";
                        break;
                    case "Samurai":
                        Service.roleAlina = "Samurai";
                        break;
                    case "Suicide":
                        Service.roleAlina = "Suicide";
                        break;
                }
            }
            mc.deleteMessage(update.getCallbackQuery().getMessage());
            mc.sendMessageById(id, "Ви - " + role);
            return;
        }

        //Опрацювання CallBack
        ActionButtons ab = new ActionButtons();
        int msgId;
        switch (callbackQuery) {
            case "Перевірити":
                if (!s.isGameStart()) {
                    mc.editMessage(update, "Гру завершено.", groupId);
                    break;
                }
                r.choiceToKill = false;
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                msgId = mc.execute(ab.createActionButtons(update.getCallbackQuery().getFrom().getId(),
                        "Кого перевіримо?", groupId)).getMessageId();
                s.addTemporaryMessages(id, msgId);
                lastTime = System.currentTimeMillis();
                s.addInList(lastTime);
                break;
            case "Вбити":
                if (!s.isGameStart()) {
                    mc.editMessage(update, "Гру завершено.", groupId);
                    break;
                }
                r.choiceToKill = true;
                mc.deleteMessage(update.getCallbackQuery().getMessage());
                msgId = mc.execute(ab.createActionButtons(update.getCallbackQuery().getFrom().getId(),
                        "Тепер оберіть негідника, який має померти (на Вашу поважну думку):",
                        groupId)).getMessageId();
                s.addTemporaryMessages(id, msgId);
                lastTime = System.currentTimeMillis();
                s.addInList(lastTime);
                break;
            case "Так":
                //Чи гравець живий
                if (r.isAlive(id)) {

                    //Якщо гравець вже підтверджував
                    if (r.areYouConfirmed(id)) {

                        //Якщо гравця вішають
                        if (r.whoIsHanged() == id || r.whoreVisited(id)) {
                            mc.sendMessageById(id, "Ти не можеш голосувати.");

                        //Якщо не гравця вішають
                        } else {

                            //Гравець голосував "За"
                            if (r.yesNo(id)) {
                                mc.sendMessageById(id, "Твій голос вже й так \"За\".");

                            //Гравець голосував "Проти"
                            } else {
                                r.subtractNotConfirm(); //Забрати голос "Проти"
                                r.addConfirm(); //Додати голос "За"
                                r.setYouConfirm(id, true); //Відмітити, що гравець вже голосував
                            }
                        }

                    //Якщо гравець ще не підтверджував
                    } else {

                        //Якщо гравця вішають
                        if (r.whoIsHanged() == update.getCallbackQuery().getFrom().getId()) {
                            mc.sendMessageById(s.getCurrentGroupId(), "[" +
                                    update.getCallbackQuery().getFrom().getFirstName() + "](tg://user?id=" +
                                    id + ") тебе ніхто не питав. " + EmojiParser.parseToUnicode(":no_mouth:"));
                            r.setYouConfirm(id, true); //Відмітити, що гравець вже голосував

                        //Якщо у гравця була повія
                        } else if (r.whoreVisited(id)) {
                            mc.sendMessageById(id, "Коханка задурманила тобі голову, тому ти не голосуєш.");
                            r.setYouConfirm(id, true); //Відмітити, що гравець вже голосував

                        //Якщо не гравця вішають
                        } else {
                            r.addConfirm(); //Додати голос "За"
                            r.setYouConfirm(id, true); //Відмітити, що гравець вже голосував
                            if (s.messageWhoConfirmed == 0) {
                                s.setMessageWhoConfirmed(mc.execute(mc.sendWhoConfirmed(groupId)).getMessageId());
                                lastTime = System.currentTimeMillis();
                                s.addInList(lastTime);
                            } else {
                                mc.updateWhoConfirmed(groupId);
                            }
                            s.addPlayersConfirmed(); //Додати до списку тих, хто проголосував
                        }
                    }

                //Якщо гравець мертвий
                } else {
                    mc.sendMessageById(id, "Ти трохи мертвий, тому мовчи.");
                }
                break;
            case "Ні":
                //Чи гравець живий
                if (r.isAlive(id)) {

                    //Якщо гравець вже підтверджував
                    if (r.areYouConfirmed(id)) {

                        //Якщо гравця вішають
                        if (r.whoIsHanged() == id || r.whoreVisited(id)) {
                            mc.sendMessageById(id, "Ти не можеш голосувати.");

                        //Якщо не гравця вішають
                        } else {

                            //Гравець голосував "За"
                            if (r.yesNo(id)) {
                                r.subtractConfirm(); //Забрати голос "За"
                                r.addNotConfirm(); //Додати голос "Проти"
                                r.setYouConfirm(id, false); //Відмітити, що гравець вже голосував

                            //Гравець голосував "Проти"
                            } else {
                                mc.sendMessageById(id, "Твій голос вже було віддано за \"Ні\".");
                            }
                        }

                    //Якщо гравець ще не підтверджував
                    } else {

                        //Якщо гравця вішають
                        if (r.whoIsHanged() == id) {
                            mc.sendMessageById(s.getCurrentGroupId(), "[" +
                                    update.getCallbackQuery().getFrom().getFirstName() + "](tg://user?id=" +
                                    id + ") тебе ніхто не питав. " + EmojiParser.parseToUnicode(":no_mouth:"));
                            r.setYouConfirm(id, false); //Відмітити, що гравець вже голосував

                        //Якщо у гравця була повія
                        } else if (r.whoreVisited(id)) {
                            mc.sendMessageById(id, "Коханка задурманила тобі голову, тому ти не голосуєш.");
                            r.setYouConfirm(id, false); //Відмітити, що гравець вже голосував

                        //Якщо не гравця вішають
                        } else {
                            r.addNotConfirm(); //Додати голос "Проти"
                            r.setYouConfirm(id, false); //Відмітити, що гравець вже голосував
                            if (s.messageWhoConfirmed == 0) {
                                s.setMessageWhoConfirmed(mc.execute(mc.sendWhoConfirmed(groupId)).getMessageId());
                                lastTime = System.currentTimeMillis();
                                s.addInList(lastTime);
                            } else {
                                mc.updateWhoConfirmed(groupId);
                            }
                            s.addPlayersConfirmed(); //Додати до списку тих, хто проголосував
                        }
                    }

                //Якщо гравець мертвий
                } else {
                    mc.sendMessageById(id, "Ти трохи мертвий, тому мовчи.");
                }
                break;
            default:
                if (!s.isGameStart()) {
                    mc.editMessage(update, "Гру завершено.", groupId);
                    break;
                }

                //Вночі
                if (callbackQuery.charAt(0) == ('Н')) {
                    if (r.isNight()) {
                        actionNight(update);
                        break;
                    } else if (r.isDay()) {
                        mc.editMessage(update, "Ніч вже закінчилася.", groupId);
                        break;
                    }

                //Вдень
                } else if (callbackQuery.charAt(0) == ('Д')) {
                    if (r.isNight()) {
                        mc.editMessage(update, "Ти не встиг на голосування.", groupId);
                        break;
                    } else if (r.isDay()) {
                        actionDay(update);
                        break;
                    }
                }
        }
    }

    //Опрацювання налаштувань
    public synchronized void actionSettings(Update update) throws InterruptedException {
        MessageController mc = new MessageController();
        String callback = update.getCallbackQuery().getData().substring(8, 12);
        groupId = Long.parseLong(update.getCallbackQuery().getData().substring(12));
        Service s = Storage.getService(groupId);

        switch(callback) {
            case "Ніч ":
                s.switcherTimeChoice("night");
                mc.editMessage(update, "*Тривалість ночі:*", groupId);
                break;
            case "День":
                s.switcherTimeChoice("day");
                mc.editMessage(update, "*Тривалість дня:*", groupId);
                break;
            case "Vote":
                s.switcherTimeChoice("vote");
                mc.editMessage(update, "*Тривалість голосування:*", groupId);
                break;
            case "Conf":
                s.switcherTimeChoice("confirm");
                mc.editMessage(update, "*Тривалість підтвердження:*", groupId);
                break;
            case "0020":
                if (s.getTime() != 20) {
                    s.setTime(20);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "0030":
                if (s.getTime() != 30) {
                    s.setTime(30);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "0045":
                if (s.getTime() != 45) {
                    s.setTime(45);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "0060":
                if (s.getTime() != 60) {
                    s.setTime(60);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "0090":
                if (s.getTime() != 90) {
                    s.setTime(90);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "0120":
                if (s.getTime() != 120) {
                    s.setTime(120);
                    mc.editMessage(update, "Вибрано", groupId);
                }
                break;
            case "Back":
                s.nullTimeChoice();
                mc.editMessage(update, "Назад", groupId);
                break;
            case "Done":
                s.nullTimeChoice();
                mc.editMessage(update, "Ок", groupId);
                break;
        }
    }

    public synchronized void addBots(Update update) throws InterruptedException {
        groupId = update.getCallbackQuery().getMessage().getChatId();
        MessageController mc = new MessageController();
        mc.editMessage(update, "Додаю...", groupId);
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        int count = Integer.parseInt(update.getCallbackQuery().getData().substring(7));

        int alreadyHave = 0;
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (entry.getKey() < 0) {
                ++alreadyHave;
            }
        }

        for (int i = alreadyHave + 1; i <= alreadyHave + count; i++) {
            String name = "Bot " + i;
            r.addCounter();
            s.putPlayerInMap(-i, name);
            s.putPlayerInFullNames(-i, name);
            mc.editMessage(update, " ", groupId);
            sleep(1000);
        }
        mc.sendMessageById(update.getCallbackQuery().getMessage().getChatId(), "Додано ботів: " + count);
        s.gameWithBots = true;
        mc.deleteMessage(update.getCallbackQuery().getMessage());
    }
}
