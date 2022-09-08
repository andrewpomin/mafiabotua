package game;

import main.Bot;
import services.Storage;
import services.Service;
import services.Randomizer;
import buttons.ActionButtons;
import services.MessageController;
import buttons.ConfirmationButtons;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

import static java.lang.Thread.sleep;
import static services.MessageController.lastTime;

public class Game extends Bot {
    //Гра
    public synchronized void game(long groupId) throws InterruptedException {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        MessageController mc = new MessageController();

        //Підрахунок і реєстрація учасників
        String answer = r.registerPlayers();
        if (answer.equals("Замало гравців(") || answer.equals("Забагато гравців")) {
            mc.sendMessageById(s.getCurrentGroupId(), answer);
            Storage.removeRules(r);
            return;
        }
        mc.sendMessageById(s.getCurrentGroupId(), answer);
        s.setGameStart(true);

        writeRoles(s, r);

        GameThread gameThread = new GameThread(groupId);

        //Розсилаємо ролі
        registerRoles(groupId);

        //Починаємо гру
        gameThread.start();
    }

    //Ніч
    public synchronized void night(long groupId) throws TelegramApiException, InterruptedException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        Randomizer rand = new Randomizer();
        mc.sendMessageById(s.getCurrentGroupId(), "*Почалася ніч " + s.getNightCount() + ".* " +
                EmojiParser.parseToUnicode(":night_with_stars:") + "\n" + rand.randomDescriptionNight() +
                "\nСпимо " + s.getNightTime() + " секунд, якщо ніхто не розбудить.");

        s.nullActiveRolesUsed();
        r.clearVotes();
        sleep(1500);
        s.printStillAliveList();

        ActionButtons ab = new ActionButtons();

        //Розіслати дії і кнопки вибору
        int msgId;
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (entry.getKey() > 0) {
                sleep(100);
                String role = r.getRole(entry.getKey());
                switch (role) {
                    case "Don":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Оберіть наступну ціль мафії:", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Mafia":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Виберіть, кого ви хочете вбити:", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Doctor":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Кого будемо лікувати цієї ночі?", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Commissioner":
                        msgId = execute(ab.createChoiceForCommissioner(entry.getKey())).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Whore":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Кого ощасливимо цієї ночі?", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Maniac":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Давай виберемо жертву:", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Lawyer":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Виберіть кого будемо захищати вдень:", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Persecutor":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "За ким хочете стежити цієї ночі?", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Samurai":
                        msgId = execute(ab.createActionButtons(entry.getKey(),
                                "Кого будемо захищати ціною власного життя?", groupId)).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                    case "Civilian":
                    case "Sergeant":
                    case "Suicide":
                        msgId = execute(ab.createButtonsForCivilian(entry.getKey())).getMessageId();
                        s.addTemporaryMessages(entry.getKey(), msgId);
                        lastTime = System.currentTimeMillis();
                        s.addInList(lastTime);
                        break;
                }
            }
        }

        //Якщо граємо з ботами
        if (s.gameWithBots) {
            long[] temporary = new long[s.getPlayersMap().size()];
            for (int i = 0; i < s.getPlayersMap().size(); i++) {
                temporary[i] = (long) s.getPlayersMap().keySet().toArray()[i];
            }

            long[] temporary2 = new long[s.getPlayersMap().size()];;

            for (int i = 0; i < temporary.length; i++) {
                int random = (int) (Math.random() * (r.getCounter() - 1));
                while (temporary[random] == 0) {
                    if (random < temporary.length - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                }

                temporary2[i] = temporary[random];
                temporary[random] = 0;
            }

            for (long l : temporary2) {
                if (l < 0) {
                    botNight(groupId, l);
                    sleep(2000);
                }
            }
        }
    }

    //Дії ботів уночі
    public synchronized void botNight(long groupId, long botId) throws InterruptedException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        String role = r.getRole(botId);
        int random = (int) (Math.random() * (r.getCounter() - 1));
        long chosen = (long) s.getPlayersMap().keySet().toArray()[random];
        switch (role) {
            case "Don":
                if (s.getMafiaNotStartVote()) {
                    s.setMafiaStartVote(true);
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":busts_in_silhouette:") +
                            " *Мафія* вже зібралась на голосування.");
                }
                while (chosen == botId || r.getRole(chosen).equals("Don") || r.getRole(chosen).equals("Mafia")) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.mafiaVotes(chosen, "Don");
                for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                    if (r.getRole(entry.getKey()).equals("Mafia") && entry.getKey() > 0) {
                        mc.sendMessageById(entry.getKey(), "Дон " + s.getPlayerMap(botId) + " хоче вбити " +
                                s.getPlayerFullNames(chosen) + ".");
                    }
                }
                System.out.println(s.getPlayerFullNames(botId) + " - Don, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Mafia":
                if (s.getMafiaNotStartVote()) {
                    s.setMafiaStartVote(true);
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":busts_in_silhouette:") +
                            " *Мафія* вже зібралась на голосування.");
                }
                while (chosen == botId || r.getRole(chosen).equals("Don") || r.getRole(chosen).equals("Mafia")) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.mafiaVotes(chosen, "Mafia");
                for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                    if ((r.getRole(entry.getKey()).equals("Don") ||
                            r.getRole(entry.getKey()).equals("Mafia")) && entry.getKey() > 0) {
                        mc.sendMessageById(entry.getKey(), s.getPlayerMap(botId) + " пропонує вальнути " +
                                s.getPlayerFullNames(chosen) + ".");
                    }
                }
                System.out.println(s.getPlayerFullNames(botId) + " - Mafia, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Commissioner":
                if (r.isPoliceVisitedMafiaPreviousNight()) {
                    r.choiceToKill = true;
                    chosen = r.getMafiaId();
                } else {
                    int random2 = (int) (Math.random() * 10);
                    r.choiceToKill = random2 == 3 || random2 == 7;
                    while ((chosen == botId) || r.isCommissionerVisitedThisGame(chosen) ||
                            r.getRole(chosen).equals("Sergeant")) {
                        if (r.allWasVisited()) {
                            while ((chosen == botId) || r.getRole(chosen).equals("Sergeant")) {
                                if (random < r.getCounter() - 1) {
                                    ++random;
                                } else {
                                    random = 0;
                                }
                                chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                            }
                        }
                        if (random < r.getCounter() - 1) {
                            ++random;
                        } else {
                            random = 0;
                        }
                        chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                    }
                }
                r.setChosen(botId, chosen);
                r.commissionerVotes(chosen);
                if (r.choiceToKill) {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":detective:") +
                            " *Комісар Катані* вже дістав свій ствол.");
                } else {
                    if (r.getRole(chosen).equals("Don") || r.getRole(chosen).equals("Mafia")) {
                        r.setPoliceVisitedMafia(chosen);
                    }
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":detective:") +
                            " *Комісар Катані* вже поряд, стерво.");
                    r.setCommissionerVisitedThisGame(chosen, true);
                }
                System.out.println(s.getPlayerFullNames(botId) + " - Commissioner, choseToKill " + r.choiceToKill +
                        " for " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Doctor":
                while (chosen == botId && r.isDoctorVisitedLastNight(botId)) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.doctorVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":man_health_worker:") +
                        " *Лікар* вже виїхав.");
                System.out.println(s.getPlayerFullNames(botId) + " - Doctor, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Whore":
                while (chosen == botId) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.whoreVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":underage:") +
                        " *Коханка* вже обрала щасливчика.");
                System.out.println(s.getPlayerFullNames(botId) + " - Whore, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Maniac":
                while (chosen == botId) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.maniacVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":knife:") +
                        " *Маніяк* уже сховався в кущах.");
                System.out.println(s.getPlayerFullNames(botId) + " - Maniac, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Lawyer":
                while (chosen == botId) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.lawyerVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":man_judge:") +
                        " *Адвокат* уже зібрав фальшиві докази.");
                System.out.println(s.getPlayerFullNames(botId) + " - Lawyer, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Persecutor":
                while (chosen == botId) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.persecutorVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":eyes:") +
                        " *Переслідувач* за кимось спостерігає.");
                System.out.println(s.getPlayerFullNames(botId) + " - Persecutor, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
            case "Samurai":
                while (chosen == botId) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
                r.setChosen(botId, chosen);
                r.samuraiVotes(chosen);
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":kimono:") +
                        " *Самурай* вже когось закриває своїм тілом.");
                System.out.println(s.getPlayerFullNames(botId) + " - Samurai, chose " + s.getPlayerFullNames(chosen));
                s.addActiveRolesUsed();
                break;
        }
    }

    //Результати ночі
    public synchronized void nightResults(long groupId) throws InterruptedException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        r.whoreBlocked(groupId);
        sleep(1000);
        Randomizer rand = new Randomizer();
        s.deleteTemporaryMessages();
        r.setDay();
        mc.sendMessageById(s.getCurrentGroupId(), "*Світало. Почався день " + s.getDayCount() + ".* " +
                EmojiParser.parseToUnicode(":sunrise:") + "\n" + rand.randomDescriptionDay());

        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.whoreVisited(entry.getKey()) && entry.getKey() > 0) {
                mc.sendMessageById(entry.getKey(), "Всю ніч у тебе була коханка, ти залишишся з нею ще й на весь " +
                        "день. Всі справи потім.");
            }
        }

        long dead = r.mafiaKill();
        long killed = r.commissionerChoose();
        long murdered = r.maniacKill();

        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getRole(entry.getKey()).equals("Sergeant") && entry.getKey() > 0
                    && !r.whoreVisited(entry.getKey())) {
                if (r.choiceToKill) {
                    mc.sendMessageById(entry.getKey(), "Комісар Катані зарядив зброю. Його ціль - " +
                            s.getPlayerMap(killed) + ".");
                } else {
                    mc.sendMessageById(entry.getKey(), "Бос з'ясував, що " +
                            s.getPlayerMap(killed) + " - " + r.getRole(killed) + ".");
                }
            }
        }

        long samuraiId = 0;
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getRole(entry.getKey()).equals("Samurai")) {
                samuraiId = entry.getKey();
                break;
            }
        }
        long saved = r.samuraiVisited();
        long samuraiSavior = 0;
        if (saved == dead && saved != 0) {
            dead = samuraiId;
            samuraiSavior = samuraiId;
            r.samuraiSave();
            if (r.samuraiVisited() > 0) {
                mc.sendMessageById(saved, r.wasSaved(saved));
            }
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if ((r.getRole(entry.getKey()).equals("Don") || r.getRole(entry.getKey()).equals("Mafia"))
                && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), "Якийсь придурок кинувся під кулі. Доб'ємо жертву " +
                            "іншим разом. " + s.getPlayerFullNames(samuraiId) + " буде для інших посланням.");
                }
            }
        }
        if (saved == killed && r.choiceToKill && saved != 0) {
            killed = samuraiId;
            samuraiSavior = samuraiId;
            r.samuraiSave();
            if (r.samuraiVisited() > 0) {
                mc.sendMessageById(saved, r.wasSaved(saved));
            }
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if (r.getRole(entry.getKey()).equals("Commissioner") && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), s.getPlayerFullNames(samuraiId) + " раптово " +
                            " закриває собою підозрюваного і ти вбиваєш не того. Доведеться писати рапорт.");
                }
                if (r.getRole(entry.getKey()).equals("Sergeant") && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), "Комісар сказав, що стався нещасний випадок, тому цієї " +
                            "ночі нас покидає " + s.getPlayerFullNames(samuraiId));
                }
            }
        }
        if (saved == murdered && saved != 0) {
            murdered = samuraiId;
            samuraiSavior = samuraiId;
            r.samuraiSave();
            if (r.samuraiVisited() > 0) {
                mc.sendMessageById(saved, r.wasSaved(saved));
            }
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if (r.getRole(entry.getKey()).equals("Maniac") && entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), "Ти трохи промахуєшся і від твого ножа вмирає " +
                            s.getPlayerFullNames(samuraiId) + ". Що ж, теж непогано.");
                }
            }
        }

        //Рандомний показ убивств
        int random = (int) (Math.random() * 11);
        sleep(1000);

        switch (random) {
            case 0:
            case 6:
                mafiaAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                commissionerAct(s, r, mc, dead, killed, murdered, "mafia", samuraiSavior, saved);
                maniacAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
            case 1:
            case 7:
                mafiaAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                maniacAct(s, r, mc, dead, killed, murdered, "mafia", samuraiSavior, saved);
                commissionerAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
            case 2:
            case 8:
                commissionerAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                mafiaAct(s, r, mc, dead, killed, murdered, "commissioner", samuraiSavior, saved);
                maniacAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
            case 3:
            case 9:
                commissionerAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                maniacAct(s, r, mc, dead, killed, murdered, "commissioner", samuraiSavior, saved);
                mafiaAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
            case 4:
            case 10:
                maniacAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                mafiaAct(s, r, mc, dead, killed, murdered, "maniac", samuraiSavior, saved);
                commissionerAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
            case 5:
            case 11:
                maniacAct(s, r, mc, dead, killed, murdered, "none", samuraiSavior, saved);
                commissionerAct(s, r, mc, dead, killed, murdered, "maniac", samuraiSavior, saved);
                mafiaAct(s, r, mc, dead, killed, murdered, "both", samuraiSavior, saved);
                break;
        }
        sleep(1000);

        //Відправити повідомлення від лікаря
        String healed;
        if (samuraiSavior != 0 && (r.wasHealed() == samuraiSavior || r.wasHealed() == r.samuraiVisited())) {
            healed = r.doctorHeal("Samurai", s);
            if (!healed.equals("")) {
                if (samuraiSavior > 0) {
                    mc.sendMessageById(samuraiSavior, healed);
                }
            }
            if (r.wasHealed() != samuraiSavior) {
                healed = r.doctorHeal("Target", s);
                if (!healed.equals("")) {
                    if (r.wasHealed() > 0) {
                        mc.sendMessageById(r.wasHealed(), healed);
                    }
                }
            }
        } else if (samuraiSavior == 0) {
            healed = r.doctorHeal("Target", s);
            if (!healed.equals("")) {
                if (r.wasHealed() > 0) {
                    mc.sendMessageById(r.wasHealed(), healed);
                }
            }
        }

        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getRole(entry.getKey()).equals("Persecutor")) {
                if (entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), r.persecutorWatch(s, entry.getKey()));
                }
                break;
            }
        }

        if (!r.isAlive(dead) && dead != 0) {
            s.removePlayer(dead);
        }
        if (!r.isAlive(killed) && killed != 0) {
            s.removePlayer(killed);
        }
        if (!r.isAlive(murdered) && murdered != 0) {
            s.removePlayer(murdered);
        }

        s.setMafiaStartVote(false);
    }

    //День
    public synchronized void day(long groupId) throws InterruptedException, TelegramApiException {
        ActionButtons a = new ActionButtons();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        MessageController mc = new MessageController();
        sleep(1000);
        mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":raising_hand:") +
                " Приготуйте пальчики, починаємо голосування. На це відведено " +
                s.getVoteTime() + " секунд, якщо не впораємось раніше.");

        sleep(1000);

        //Розіслати список гравців для голосування
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.whoreVisited(entry.getKey())) {
                s.addPlayersVoted();
                continue;
            }
            if (entry.getKey() > 0 && !r.whoreVisited(entry.getKey())) {
                sleep(100);
                String text = "Хто на Вашу думку мафія?";
                s.addTemporaryMessages(entry.getKey(),
                        execute(a.createVotingButtons(entry.getKey(), text, groupId)).getMessageId());
            }
        }

        //Якщо граємо з ботами
        if (s.gameWithBots) {
            for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                if (entry.getKey() < 0) {
                    botDay(groupId, entry.getKey());
                    sleep(1500);
                }
            }
        }
    }

    //Дії ботів удень
    public synchronized void botDay(long groupId, long botId) throws TelegramApiException, InterruptedException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        int random = (int) (Math.random() * (r.getCounter() - 1));
        long chosen = (long) s.getPlayersMap().keySet().toArray()[random];

        if (r.whoreVisited(botId)) {
            s.addPlayersVoted();
            return;
        }

        //Комісар або сержант голосують за мафію
        if (r.getRole(botId).equals("Commissioner") || r.getRole(botId).equals("Sergeant")) {
            if (r.isPoliceVisitedMafiaPreviousNight()) {
                chosen = r.getMafiaId();
            } else {
                while (r.getRole(chosen).equals("Commissioner") || r.getRole(chosen).equals("Sergeant")
                        || (r.isCommissionerVisitedThisGame(chosen)
                        && !(r.getRole(chosen).equals("Don") || r.getRole(chosen).equals("Mafia")))) {
                    if (random < r.getCounter() - 1) {
                        ++random;
                    } else {
                        random = 0;
                    }
                    chosen = (long) s.getPlayersMap().keySet().toArray()[random];
                }
            }
        } else if (r.getRole(botId).equals("Don") || r.getRole(botId).equals("Mafia")) {
            while (r.getRole(chosen).equals("Don") || r.getRole(chosen).equals("Mafia")) {
                if (random < r.getCounter() - 1) {
                    ++random;
                } else {
                    random = 0;
                }
                chosen = (long) s.getPlayersMap().keySet().toArray()[random];
            }
        } else {
            while (chosen == botId) {
                if (random < r.getCounter() - 1) {
                    ++random;
                } else {
                    random = 0;
                }
                chosen = (long) s.getPlayersMap().keySet().toArray()[random];
            }
        }

        r.setVote(chosen);
        r.setVotedRules(botId);
        if (s.messageWhoVoted == 0) {
            s.setMessageWhoVoted(mc.execute(mc.sendWhoVoted(groupId)).getMessageId());
            lastTime = System.currentTimeMillis();
            s.addInList(lastTime);
        } else {
            mc.updateWhoVoted(groupId);
        }
        System.out.println(s.getPlayerFullNames(botId) + " chose " + s.getPlayerFullNames(chosen));
        s.addPlayersVoted();
    }

    //Результати голосування
    public synchronized void dayResults(long groupId) throws InterruptedException, TelegramApiException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        s.deleteTemporaryMessages();
        mc.sendMessageById(s.getCurrentGroupId(), "Голосування завершено.");
        s.nullPlayersVoted();
        s.setMessageWhoVoted(0);
        sleep(2000);
        long hanged = r.whoIsHanged();

        //Якщо нікого не вішаємо
        if (hanged == 0) {
            mc.sendMessageById(s.getCurrentGroupId(), "Сьогодні не вдалось вирішити хто винен, розходимось.");
            r.setNight();

        //Запускаємо підтвердження
        } else {
            s.setStartConfirmation(true);
            ConfirmationButtons confirmation = new ConfirmationButtons();
            s.setConfirmationToHungMessageId(execute(confirmation.createConfirmButton(groupId)).getMessageId());
            sleep(2000);
            if (s.gameWithBots) {
                botConfirm(groupId);
            }
        }
    }

    //Підтвердження від ботів
    public synchronized void botConfirm(long groupId) throws InterruptedException, TelegramApiException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (entry.getKey() < 0 && r.whoIsHanged() != entry.getKey()) {
                if (r.whoreVisited(entry.getKey())) {
                    s.addPlayersConfirmed();
                    continue;
                }
                float randomTime = (float) (Math.random() * 2);
                sleep((long) (randomTime * 1500));

                //Комісар або сержант підтверджують вибір за мафію
                if ((r.getRole(entry.getKey()).equals("Commissioner") || r.getRole(entry.getKey()).equals("Sergeant"))) {
                    if (r.getRole(r.whoIsHanged()).equals("Commissioner") ||
                            r.getRole(r.whoIsHanged()).equals("Sergeant")) {
                        r.addNotConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                        r.setYouConfirm(entry.getKey(), false);
                    } else if (r.findInMafiaId(r.whoIsHanged())) {
                        r.addConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - Yes");
                        r.setYouConfirm(entry.getKey(), true);
                    } else if (r.isCommissionerVisitedThisGame(r.whoIsHanged())
                            && !(r.getRole(r.whoIsHanged()).equals("Don")
                            || r.getRole(r.whoIsHanged()).equals("Mafia"))) {
                        r.addNotConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                        r.setYouConfirm(entry.getKey(), false);
                    } else {
                        int random = (int) (Math.random() * 10);

                        if (random == 1 || random == 3 || random == 5 || random == 7 || random == 9) {
                            r.addConfirm();
                            System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - Yes");
                            r.setYouConfirm(entry.getKey(), true);
                        } else {
                            r.addNotConfirm();
                            System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                            r.setYouConfirm(entry.getKey(), false);
                        }
                    }
                } else if ((r.getRole(entry.getKey()).equals("Don") || r.getRole(entry.getKey()).equals("Mafia"))) {
                    if (r.getRole(r.whoIsHanged()).equals("Don") || r.getRole(r.whoIsHanged()).equals("Mafia")) {
                        r.addNotConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                        r.setYouConfirm(entry.getKey(), false);
                    } else {
                        int random = (int) (Math.random() * 10);

                        if (random == 1 || random == 3 || random == 5 || random == 7 || random == 9) {
                            r.addConfirm();
                            System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - Yes");
                            r.setYouConfirm(entry.getKey(), true);
                        } else {
                            r.addNotConfirm();
                            System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                            r.setYouConfirm(entry.getKey(), false);
                        }
                    }
                } else {
                    int random = (int) (Math.random() * 10);

                    if (random == 1 || random == 3 || random == 5 || random == 7 || random == 9) {
                        r.addConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - Yes");
                        r.setYouConfirm(entry.getKey(), true);
                    } else {
                        r.addNotConfirm();
                        System.out.println(s.getPlayerFullNames(entry.getKey()) + " vote - No");
                        r.setYouConfirm(entry.getKey(), false);
                    }
                }

                if (s.messageWhoConfirmed == 0) {
                    s.setMessageWhoConfirmed(mc.execute(mc.sendWhoConfirmed(groupId)).getMessageId());
                    lastTime = System.currentTimeMillis();
                    s.addInList(lastTime);
                } else {
                    mc.updateWhoConfirmed(groupId);
                }

                s.addPlayersConfirmed();
                sleep(1000);
            }
        }
    }

    //Підтверджуємо чи вішати користувача
    public synchronized void voteResults(long groupId) throws TelegramApiException, InterruptedException {
        MessageController mc = new MessageController();
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        Randomizer randomizer = new Randomizer();
        mc.sendMessageById(s.getCurrentGroupId(), "Всі проголосували.");
        s.setMessageWhoConfirmed(0);
        s.setStartConfirmation(false);
        long hanged = r.whoIsHanged();
        if (r.lawyerProtect(hanged) && r.getConfirm() > r.getNotConfirm()) {
            mc.editMessage("ResultsOfConfirmation", groupId);
            mc.sendMessageById(s.getCurrentGroupId(), "Адвокатом було зібрано документи, що підтверджують " +
                    "алібі підозрюваного, доведеться відпускати... Розберемося з цим пізніше.");
        } else if (r.getConfirm() > r.getNotConfirm()) {
            mc.editMessage("ResultsOfConfirmation", groupId);
            r.setHanged(hanged);
            if (r.getRole(hanged).equals("Suicide")) {
                r.setWinner(hanged, true);
            }
            String hangedText;
            if (hanged < 0) {
                hangedText = s.getPlayerMap(hanged);
            } else {
                hangedText = "[" + s.getPlayerMap(hanged) + "](tg://user?id=" + hanged + ")";
            }
            mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":coffin:") + " " +
                    randomizer.getRandomExecution(hangedText));
            s.removePlayer(hanged);
        } else {
            mc.editMessage("ResultsOfConfirmation", groupId);
            mc.sendMessageById(s.getCurrentGroupId(), "Повісимо тебе іншим разом.");
        }
        r.nullConfirms();
        s.nullPlayersConfirmed();
        r.setNight();
    }

    //Роздача ролей
    public synchronized void registerRoles(long groupId) throws InterruptedException {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        MessageController mc = new MessageController();

        //Розсилаємо ролі
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            sleep(100);
            String role = r.getRole(entry.getKey());
            String startText = "";
            switch (role) {
                case "Don":
                    startText = "Ви - *Дон*, голова мафії. Ваш голос рахується за два і за Вами останнє слово. " +
                            "Задача для Вас і ваших поплічників мафії - вбити якомога більше людей.";
                    s.addActiveRoles();
                    break;
                case "Mafia":
                    startText = "Ви - *Мафія*. Ніч - ваша подруга і все місто Вас боїться. " +
                            "Ваш бос - Дон, і він прагне отримати абсолютну владу у місті шляхом вбивств і насильства.";
                    s.addActiveRoles();
                    break;
                case "Doctor":
                    startText = "Ви - *Лікар*. Ви пам'ятаєте клятву Гіппократа і допомагаєте всім, хто цього потребує. " +
                            "Ну, або кому встигнете...";
                    s.addActiveRoles();
                    break;
                case "Commissioner":
                    startText = "Ви - *Комісар Катані*. Ви - промінь надії в цьому, потонулому у темряві, місті. " +
                            "Ваше завдання - вирахувати мафію і ліквідувати її.";
                    s.addActiveRoles();
                    break;
                case "Civilian":
                    startText = "Ви - *Мирний Житель*. Але не засмучуйтесь, Брюс Вейн також не мав суперсил) " +
                            "Спробуйте вирахувати мафію на міських зборах і повісити їх усіх.";
                    break;
                case "Sergeant":
                    startText = "Ви - *Сержант* Поліції та помічник Комісара. Якщо Комісара вб'ють - Вам потрібно " +
                            "буде завершити його справу та знайти мафію.";
                    break;
                case "Whore":
                    startText = "Ви - *Коханка*. Ваша професія - любити. Ви можете відволікти кого захочете на цілу " +
                            "ніч і цілий день.";
                    s.addActiveRoles();
                    break;
                case "Maniac":
                    startText = "Ви - *Маніяк*. Просто психопат. Ваша ціль вбити усіх. Чому - не знаєте, але робите " +
                            "все для цього.";
                    s.addActiveRoles();
                    break;
                case "Lawyer":
                    startText = "Ви - *Адвокат*. Ви можете довести невинуватість будь-якої особи в місті, навіть " +
                            "якщо вона все ж таки винна.";
                    s.addActiveRoles();
                    break;
                case "Persecutor":
                    startText = "Ви - *Переслідувач*. Вам подобається стежити за людьми, тому Ви все про них знаєте і " +
                            "бачите хто до кого приходить.";
                    s.addActiveRoles();
                    break;
                case "Samurai":
                    startText = "Ви - *Самурай*. Для Вас найголовніше Ваша честь і відданість. Ви понад усе хочете " +
                            "врятувати когось ціною власного життя.";
                    s.addActiveRoles();
                    break;
                case "Suicide":
                    startText = "Ви - *Приречений*. Вам не пощастило в коханні, ви розорилися і тепер ви хочете " +
                            "покінчити з собою. Спробуйте змусити жителів міста повісити Вас.";
                    break;
            }

            //Якщо гравець не бот
            if (entry.getKey() > 0) {
                mc.sendMessageById(entry.getKey(), startText);
            }

            //Розіслати команду мафії
            if ((role.equals("Don") || role.equals("Mafia")) && s.getPlayersRoles().size() > 4) {
                StringBuilder mafias = new StringBuilder("Твоя банда:");
                for (Map.Entry<Long, String> entry2 : s.getPlayersRoles().entrySet()) {
                    String role2 = r.getRole(entry2.getKey());
                    if (role2.equals("Don")) {
                        mafias.append("\n").append(s.getPlayerFullNames(entry2.getKey())).append(" - _Дон_");
                    } else if (role2.equals("Mafia")) {
                        mafias.append("\n").append(s.getPlayerFullNames(entry2.getKey())).append(" - _Мафія_");
                    }
                }
                sleep(100);
                if (entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), mafias.toString());
                }
            }

            //Розіслати команду поліції
            if ((role.equals("Commissioner") || role.equals("Sergeant")) && s.getPlayersRoles().size() > 11) {
                StringBuilder police = new StringBuilder("Твоя опергрупа:");
                for (Map.Entry<Long, String> entry2 : s.getPlayersRoles().entrySet()) {
                    String role2 = r.getRole(entry2.getKey());
                    if (role2.equals("Commissioner")) {
                        police.append("\n").append(s.getPlayerFullNames(entry2.getKey())).append(" - _Комісар Катані_");
                    } else if (role2.equals("Sergeant")) {
                        police.append("\n").append(s.getPlayerFullNames(entry2.getKey())).append(" - _Сержант_");
                    }
                }
                sleep(100);
                if (entry.getKey() > 0) {
                    mc.sendMessageById(entry.getKey(), police.toString());
                }
            }
        }
    }

    //Записати ролі
    public synchronized void writeRoles(Service s, Rules r) {
        //Записуємо ролі в таблицю
        for (Map.Entry<Long, String> entry : s.getPlayersFullNames().entrySet()) {
            long id = entry.getKey();
            String role = r.getRole(id);
            StringBuilder value = new StringBuilder();
            switch (role) {
                case "Don":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":bust_in_silhouette:")).
                                append(" *Дон*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":bust_in_silhouette:")).append(" *Дон*");
                    }
                    break;
                case "Mafia":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":busts_in_silhouette:")).
                                append(" *Мафія*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":busts_in_silhouette:")).append(" *Мафія*");
                    }
                    break;
                case "Doctor":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":man_health_worker:")).
                                append(" *Лікар*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":man_health_worker:")).append(" *Лікар*");
                    }
                    break;
                case "Commissioner":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":detective:")).
                                append(" *Комісар Катані*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":detective:")).append(" *Комісар Катані*");
                    }
                    break;
                case "Civilian":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":man:")).
                                append(" *Мирний житель*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":man:")).append(" *Мирний житель*");
                    }
                    break;
                case "Sergeant":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":cop:")).
                                append(" *Сержант*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":cop:")).append(" *Сержант*");
                    }
                    break;
                case "Whore":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":underage:")).
                                append(" *Коханка*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":underage:")).append(" *Коханка*");
                    }
                    break;
                case "Maniac":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":knife:")).
                                append(" *Маніяк*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":knife:")).append(" *Маніяк*");
                    }
                    break;
                case "Lawyer":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":man_judge:")).
                                append(" *Адвокат*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":man_judge:")).append(" *Адвокат*");
                    }
                    break;
                case "Persecutor":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":eyes:")).
                                append(" *Переслідувач*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":eyes:")).append(" *Переслідувач*");
                    }
                    break;
                case "Samurai":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":kimono:")).
                                append(" *Самурай*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":kimono:")).append(" *Самурай*");
                    }
                    break;
                case "Suicide":
                    if (id > 0) {
                        value.append("\n").append("[").append(s.getPlayerFullNames(id)).append("](tg://user?id=").
                                append(id).append(") - ").append(EmojiParser.parseToUnicode(":shrug:")).
                                append(" *Приречений*");
                    } else {
                        value.append("\n").append(s.getPlayerFullNames(id)).append(" - ").
                                append(EmojiParser.parseToUnicode(":shrug:")).append(" *Приречений*");
                    }
                    break;
            }
            s.putPlayerInRoles(id, String.valueOf(value));
        }
    }

    //Мафія вбиває
    public synchronized void mafiaAct(Service s, Rules r, MessageController mc, long dead, long killed,
                                      long murdered, String order, long samuraiSavior, long saved) throws InterruptedException {
        if (order.equals("commissioner") && dead == killed && dead != 0 && r.choiceToKill) {
            return;
        } else if (order.equals("maniac") && murdered == dead  && dead != 0) {
            return;
        } else if (order.equals("both") && ((dead == killed && r.choiceToKill) || murdered == dead) && dead != 0) {
            return;
        }
        sleep(1500);
        //Якщо лікар когось вилікував
        if (r.deadAlive(dead) || (samuraiSavior == dead && samuraiSavior != 0
                && (r.isDoctorVisited(samuraiSavior) || r.isDoctorVisited(saved)))) {
            mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":bandaged:") +
                    " Сьогодні намагалися когось розстріляти... Але він не здався. І лікар його вилікував.");

        //Якщо самурай рятує
        } else if (samuraiSavior == dead && samuraiSavior != 0) {
            if (samuraiSavior > 0) {
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                        " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                        "гине " + "[" + s.getPlayerMap(samuraiSavior) + "](tg://user?id=" + samuraiSavior + ")" + ".");
            } else {
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                        " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                        "гине " + s.getPlayerMap(samuraiSavior) + ".");
            }

        //Якщо лікар не вгадав
        } else {

            //Якщо мафія когось вбила
            if (dead != 0) {
                r.setLastWord(dead);
                if (dead > 0) {
                    mc.sendMessageById(dead, "Тебе вбила мафія. Нікому не жаль. Не пиши в чат більше.");
                    sleep(1000);
                    mc.sendMessageById(dead, "Твоє останнє слово:");
                }
                sleep(1000);
                if (dead < 0) {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") + " " +
                            s.getPlayerMap(dead) + " вже не серед нас.");
                } else {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") +
                            " [" + s.getPlayerMap(dead) + "](tg://user?id=" + dead + ")" + " вже не серед нас.");
                }

                //Якщо мафія не зробила вибір
            } else {
                for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
                    if (r.getRole(entry.getKey()).equals("Mafia") && r.getChosen(entry.getKey()) != 0) {
                        mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":moyai:") +
                                " *Мафія* не змогла вирішити кого вбивати.");
                        return;
                    }
                }

                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":sleeping:") +
                        " *Мафія* проспала.");
            }
        }
    }

    //Комісар вбиває
    public synchronized void commissionerAct(Service s, Rules r, MessageController mc, long dead, long killed,
                                             long murdered, String order, long samuraiSavior, long saved) throws InterruptedException {
        sleep(1500);
        long id = 0;
        for (Map.Entry<Long, String> entry : s.getPlayersMap().entrySet()) {
            if (r.getRole(entry.getKey()).equals("Commissioner")) {
                id = entry.getKey();
            }
        }

        if (r.choiceToKill) {
            if (order.equals("mafia") && dead == killed && killed != 0) {
                return;
            } else if (order.equals("maniac") && murdered == killed && killed != 0) {
                return;
            } else if (order.equals("both") && (dead == killed || murdered == killed) && killed != 0) {
                return;
            }
            //Якщо лікар когось вилікував
            if (r.deadAlive(killed) || (samuraiSavior == killed && samuraiSavior != 0
                    && (r.isDoctorVisited(samuraiSavior) || r.isDoctorVisited(saved)))) {
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":bandaged:") +
                        " Сьогодні намагалися когось розстріляти... Але він не здався. І лікар його вилікував.");

                //Якщо лікар не вгадав
            } else if (samuraiSavior == killed && samuraiSavior != 0) {
                if (samuraiSavior > 0) {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                            " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                            "гине " + "[" + s.getPlayerMap(samuraiSavior) + "](tg://user?id=" + samuraiSavior + ")" + ".");
                } else {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                            " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                            "гине " + s.getPlayerMap(samuraiSavior) + ".");
                }
            } else {

                //Якщо комісар когось вбив
                if (killed != 0) {

                    if (r.findInMafiaId(killed)) {
                        r.removeDead(killed);
                    }

                    //Якщо комісар вбив мафію
                    if (r.getRole(killed).equals("Mafia") || r.getRole(killed).equals("Don")) {
                        r.setLastWord(killed);
                        if (killed > 0) {
                            mc.sendMessageById(killed, "Ти догрався. Комісар Катані тебе вирахував і ти " +
                                    "отримав унікальний декор \"мізки на стіні\". ");
                            sleep(1000);
                            mc.sendMessageById(killed, "Твоє останнє слово:");
                        }

                        //Якщо комісар вбив мирного
                    } else {
                        r.setLastWord(killed);
                        if (killed > 0) {
                            mc.sendMessageById(killed, "Тебе застрелив Комісар Катані, бо ти був надто підозрілий, " +
                                    "а він бухий. Лох - це доля.");
                            sleep(1000);
                            mc.sendMessageById(killed, "Твоє останнє слово:");
                        }
                    }
                    sleep(1000);
                    if (killed < 0) {
                        mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") + " " +
                                s.getPlayerMap(killed) + " вже не серед нас.");
                    } else {
                        mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") +
                                " [" + s.getPlayerMap(killed) + "](tg://user?id=" + killed + ")" + " вже не серед нас.");
                    }
                }
            }
        } else {
            if (killed != 0) {
                if (r.whoreVisited(id)) {
                    return;
                }
                String answer = r.getRole(killed);
                String tempText = s.getPlayerMap(killed) + " - " + answer;
                if (id > 0) {
                    mc.sendMessageById(id, tempText);
                }

                if (answer.equals("Don") || answer.equals("Mafia")) {
                    r.setPoliceVisitedMafia(killed);
                }

                if (killed > 0) {
                    mc.sendMessageById(killed, "Комісар Катані приходив до тебе.");
                }
            }
        }
    }

    //Маніяк вбиває
    public synchronized void maniacAct(Service s, Rules r, MessageController mc, long dead, long killed,
                                       long murdered, String order, long samuraiSavior, long saved) throws InterruptedException {
        if (order.equals("mafia") && dead == murdered && murdered != 0) {
            return;
        } else if (order.equals("commissioner") && murdered == killed && murdered != 0 && r.choiceToKill) {
            return;
        } else if (order.equals("both") && (dead == murdered || (murdered == killed && r.choiceToKill)) && murdered != 0) {
            return;
        }
        sleep(1500);
        //Якщо лікар когось вилікував
        if (r.deadAlive(murdered) || (samuraiSavior == murdered && samuraiSavior != 0
                && (r.isDoctorVisited(samuraiSavior) || r.isDoctorVisited(saved)))) {
            mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":bandaged:") +
                    " Сьогодні намагалися когось розстріляти... Але він не здався. І лікар його вилікував.");

            //Якщо лікар не вгадав
        } else if (samuraiSavior == murdered && samuraiSavior != 0) {
            if (samuraiSavior > 0) {
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                        " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                        "гине " + "[" + s.getPlayerMap(samuraiSavior) + "](tg://user?id=" + samuraiSavior + ")" + ".");
            } else {
                mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":shield:") +
                        " Сьогодні, рятуючи невинну жертву з криком \"Банзай!\", " +
                        "гине " + s.getPlayerMap(samuraiSavior) + ".");
            }
        } else {

            //Якщо маніяк когось вбив
            if (murdered != 0) {
                r.setLastWord(murdered);
                if (murdered > 0) {
                    mc.sendMessageById(murdered, "Тебе зарізав маніяк. Отаке непередбачуване життя.");
                    sleep(1000);
                    mc.sendMessageById(murdered, "Твоє останнє слово:");
                }
                sleep(1000);
                if (murdered < 0) {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") + " " +
                            s.getPlayerMap(murdered) + " вже не серед нас.");
                } else {
                    mc.sendMessageById(s.getCurrentGroupId(), EmojiParser.parseToUnicode(":skull:") +
                            " [" + s.getPlayerMap(murdered) + "](tg://user?id=" + murdered + ")" + " вже не серед нас.");
                }
            }
        }
    }
}
