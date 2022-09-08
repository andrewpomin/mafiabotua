package services;

import game.Rules;
import game.GameEnd;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import com.vdurmont.emoji.EmojiParser;

import static java.lang.Thread.sleep;

public class Service {
    static public String myRole = "Random";
    static public String roleAlina = "Random";

    private final long currentGroupId;
    public HashMap<Long, String> playersRoles = new HashMap<>();
    public HashMap<Long, String> playersMap = new HashMap<>();
    public HashMap<Long, String> playersFullNames = new HashMap<>();
    public HashMap<Long, Integer> messageToDelete = new HashMap<>();
    public LinkedList<Long> last20Messages = new LinkedList<>();
    boolean mafiaStartVote;
    public boolean mafiaWin;
    private boolean draw;
    public int registrationMessageId;
    public int confirmationToHungMessageId;
    public int messageWhoVoted;
    public int messageWhoConfirmed;
    public int activeRoles = 0;
    public int activeRolesUsed = 0;
    public int playersVoted = 0;
    public int playersConfirmed = 0;
    boolean startConfirmation;
    boolean forceEnd;
    int nightCount = 0;
    int dayCount = 0;
    public boolean gameStart;
    public boolean gameWithBots;

    private boolean chosenNightTime;
    private boolean chosenDayTime;
    private boolean chosenVoteTime;
    private boolean chosenConfirmTime;
    int nightTime = 30;
    int dayTime = 30;
    int voteTime = 30;
    int confirmTime = 20;

    public Service(long groupId) {
        currentGroupId = groupId;
    }

    //Додати в список 20 останніх повідомлень
    public synchronized void addInList(long time) {
        if (last20Messages.size() < 20) {
            last20Messages.addLast(time);
        } else {
            last20Messages.removeFirst();
            last20Messages.addLast(time);
        }
    }
    //Взяти мінімальний час повідомлення
    public synchronized long getMin() {
        long min = last20Messages.get(0);
        for (long l : last20Messages) {
            if (l < min) {
                min = l;
            }
        }
        return min;
    }

    //Який час змінємо
    public boolean isNightTime() {
        return chosenNightTime;
    }
    public boolean isDayTime() {
        return chosenDayTime;
    }
    public boolean isVoteTime() {
        return chosenVoteTime;
    }
    public boolean isConfirmTime() {
        return chosenConfirmTime;
    }

    //Який час встановлено
    public int getNightTime() {
        return nightTime;
    }
    public int getDayTime() {
        return dayTime;
    }
    public int getVoteTime() {
        return voteTime;
    }
    public int getConfirmTime() {
        return confirmTime;
    }

    //Встановити час
    public void setNightTime(int time) {
        nightTime = time;
    }
    public void setDayTime(int time) {
        dayTime = time;
    }
    public void setVoteTime(int time) {
        voteTime = time;
    }
    public void setConfirmTime(int time) {
        confirmTime = time;
    }

    //Налаштування часу
    public void setTime(int time) {
        if (isNightTime()) {
            setNightTime(time);
        } else if (isDayTime()) {
            setDayTime(time);
        } else if (isVoteTime()) {
            setVoteTime(time);
        } else if (isConfirmTime()) {
            setConfirmTime(time);
        }
    }
    public int getTime() {
        if (isNightTime()) {
            return getNightTime();
        } else if (isDayTime()) {
            return getDayTime();
        } else if (isVoteTime()) {
            return getVoteTime();
        } else if (isConfirmTime()) {
            return getConfirmTime();
        }
        return 0;
    }
    public void switcherTimeChoice(String str) {
        switch (str) {
            case "night":
                nullTimeChoice();
                chosenNightTime = true;
                break;
            case "day":
                nullTimeChoice();
                chosenDayTime = true;
                break;
            case "vote":
                nullTimeChoice();
                chosenVoteTime = true;
                break;
            case "confirm":
                nullTimeChoice();
                chosenConfirmTime = true;
                break;
        }
    }
    public void nullTimeChoice() {
        chosenNightTime = false;
        chosenDayTime = false;
        chosenVoteTime = false;
        chosenConfirmTime = false;
    }

    //Встановити id повідомлень
    public void setMessageWhoVoted(int id) {
        messageWhoVoted = id;
    }
    public void setMessageWhoConfirmed(int id) {
        messageWhoConfirmed = id;
    }

    //Чи гра почалась
    public boolean isGameStart() {
        return gameStart;
    }
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    //Яка команда виграла
    public boolean isMafiaWin() {
        return mafiaWin;
    }
    public void setMafiaWin(boolean mafiaWin) {
        this.mafiaWin = mafiaWin;
    }
    public boolean isDraw() {return draw;}
    public void setDraw(boolean b) {draw = b;}

    //Отримати id групи
    public synchronized long getCurrentGroupId() {
        return currentGroupId;
    }

    //Отримати лічильник ночі та дня
    public int getNightCount() {return ++this.nightCount;}
    public int getDayCount() {return ++this.dayCount;}
    public void nullNightAndDayCount() {this.nightCount = 0; this.dayCount = 0;}

    //Взяти карту
    public HashMap<Long, String> getPlayersRoles() {return playersRoles;}
    public HashMap<Long, String> getPlayersMap() {return playersMap;}
    public HashMap<Long, String> getPlayersFullNames() {return playersFullNames;}

    //Взяти гравця з карти
    public String getPlayerRoles(long id) {return playersRoles.get(id);}
    public String getPlayerMap(long id) {return playersMap.get(id);}
    public String getPlayerFullNames(long id) {
        if (playersFullNames.containsKey(id)) {
            return playersFullNames.get(id);
        } else {
            return "";
        }
    }
    //Чи гравець є в карті
    public boolean isPlayerInMap(long id) {return playersFullNames.containsKey(id);}

    //Додати гравця в карту
    public void putPlayerInRoles(long id, String role) {playersRoles.put(id, role);}
    public void putPlayerInMap(long id, String name) {playersMap.put(id, name);}
    public void putPlayerInFullNames(long id, String fullName) {playersFullNames.put(id, fullName);}

    //Отримати кількість активних ролей
    public int getActiveRoles() {return activeRoles;}
    //Отримати кількість використаних активних ролей
    public int getActiveRolesUsed() {return activeRolesUsed;}
    //Отримати кількість гравців, хто проголосував
    public int getPlayersVoted() {return playersVoted;}
    //Отримати кількість гравців, хто підтвердив
    public int getPlayersConfirmed() {return playersConfirmed;}

    //Додати кількість активних ролей
    public void addActiveRoles() {++activeRoles;}
    //Додати кількість використаних активних ролей
    public void addActiveRolesUsed() {++activeRolesUsed;}
    //Додати кількість гравців, хто проголосував
    public void addPlayersVoted() {++playersVoted;}
    //Додати кількість гравців, хто підтвердив
    public void addPlayersConfirmed() {++playersConfirmed;}

    //Очистити кількість активних ролей
    public void nullActiveRoles() {activeRoles = 0;}
    //Очистити кількість використаних активних ролей
    public void nullActiveRolesUsed() {activeRolesUsed = 0;}
    //Очистити кількість гравців, хто проголосував
    public void nullPlayersVoted() {playersVoted = 0;}
    //Очистити кількість гравців, хто підтвердив
    public void nullPlayersConfirmed() {playersConfirmed = 0;}

    //Чи почала мафія голосувати
    public boolean getMafiaNotStartVote() {return !mafiaStartVote;}
    public void setMafiaStartVote(boolean b) {mafiaStartVote = b;}

    //Чи почалося підтвердження
    public boolean isStartConfirmation() {return startConfirmation;}
    public void setStartConfirmation(boolean b) {startConfirmation = b;}

    //Id повідомлення про реєстрацію
    public int getRegistrationMessageId() {return registrationMessageId;}
    public void setRegistrationMessageId(int id) {registrationMessageId = id;}
    //Id повідомлення про підтвердження
    public int getConfirmationToHungMessageId() {return confirmationToHungMessageId;}
    public void setConfirmationToHungMessageId(int id) {confirmationToHungMessageId = id;}

    //Чи всі зробили вибір
    public synchronized boolean allVoted() {
        Rules r = Storage.getRules(currentGroupId);
        if (r.isNight()) {
            return getActiveRoles() == getActiveRolesUsed();
        } else if (r.isDay()) {
            if (isStartConfirmation()) {
                return getPlayersConfirmed() == (r.getCounter() - 1);
            } else {
                return getPlayersVoted() == r.getCounter();
            }
        }
        return false;
    }

    //Сформувати список переможців і учасників
    public synchronized void printResultList() throws InterruptedException {
        Rules r = Storage.getRules(currentGroupId);
        MessageController mc = new MessageController();
        if (isForceEnd()) {
            StringBuffer results = new StringBuffer("*В ролях:*");
            for (Map.Entry<Long, String> entry : getPlayersRoles().entrySet()) {
                long id = entry.getKey();
                results.append(getPlayerRoles(id));
            }
            mc.sendMessageById(getCurrentGroupId(), String.valueOf(results));
            return;
        }

        //Якщо нічия
        if (isDraw()) {
            StringBuffer results = new StringBuffer("*В ролях:*");
            for (Map.Entry<Long, String> entry : getPlayersRoles().entrySet()) {
                long id = entry.getKey();
                results.append(getPlayerRoles(id));
            }
            mc.sendMessageById(getCurrentGroupId(), String.valueOf(results));
            return;
        }

        StringBuffer results = new StringBuffer("*В ролях:*");
        results.append("\n\n").append(EmojiParser.parseToUnicode(":trophy:")).append(" *Переможці:*");
        for (Map.Entry<Long, String> entry : getPlayersRoles().entrySet()) {
            long id = entry.getKey();
            if (r.isWinner(id)) {
                results.append(getPlayerRoles(id));
            }
        }

        results.append("\n\n").append(EmojiParser.parseToUnicode(":second_place_medal:")).append(" *Намагалися:*");
        for (Map.Entry<Long, String> entry : getPlayersRoles().entrySet()) {
            long id = entry.getKey();
            if (!r.isWinner(id)) {
                results.append(getPlayerRoles(id));
            }
        }
        mc.sendMessageById(getCurrentGroupId(), String.valueOf(results));
    }

    //Виведення живих гравців
    public synchronized void printStillAliveList() throws InterruptedException {
        Rules r = Storage.getRules(currentGroupId);
        MessageController mc = new MessageController();
        StringBuffer users;
        if (r.isDay()) {
            users = new StringBuffer("*Сьогодні прокинулися:*");
        } else {
            users = new StringBuffer("*Поки що живі:*");
        }
        for (Map.Entry<Long, String> entry : getPlayersMap().entrySet()) {
            if (entry.getKey() < 0) {
                users.append("\n").append(entry.getValue());
            } else {
                users.append("\n").append("[").append(getPlayerFullNames(entry.getKey())).append("](tg://user?id=").
                        append(entry.getKey()).append(")");
            }
        }

        if (r.isDay()) {
            users.append("\n\nСаме час для обговорення подій ночі та пошуку мафії. У вас ").append(getDayTime())
                    .append(" секунд.");
        }
        mc.sendMessageById(getCurrentGroupId(), users.toString());
    }

    //Прибрати гравця зі списку
    public void removePlayer(long id) throws InterruptedException {
        if (id != 0) {
            Rules r = Storage.getRules(currentGroupId);
            if (!(r.getRole(id).equals("Civilian") || r.getRole(id).equals("Sergeant")
                    || r.getRole(id).equals("Suicide"))) {
                --activeRoles;
            }
            playersMap.remove(id);
            r.removePlayerInRules();
        }
    }

    //Очистити всі параметри
    public void clearAll() throws InterruptedException {
        Rules r = Storage.getRules(currentGroupId);
        r.clearAllRules();
        nullNightAndDayCount();
        playersMap.clear();
        playersFullNames.clear();
        playersRoles.clear();
        deleteTemporaryMessages();
        setMafiaWin(false);
        setGameStart(false);
        setRegistrationMessageId(0);
        setConfirmationToHungMessageId(0);
        setMessageWhoVoted(0);
        setMessageWhoConfirmed(0);
        nullActiveRoles();
        nullActiveRolesUsed();
        nullPlayersVoted();
        nullPlayersConfirmed();
        setStartConfirmation(false);
        setForceEnd(false);
        setMafiaStartVote(false);
        gameWithBots = false;
        setDraw(false);
    }

    //Перевіряємо чи закінчилася гра
    public boolean isGameEnd() throws InterruptedException {
        Rules r = Storage.getRules(getCurrentGroupId());
        if (isForceEnd()) {
            GameEnd gameEnd = new GameEnd(getCurrentGroupId());
            gameEnd.start();
            return true;
        } else {
            if (r.getCounter() == 0) {
                GameEnd gameEnd = new GameEnd(getCurrentGroupId());
                gameEnd.start();
                return true;
            } else if ((r.getCounter() == 2 && r.getManiac() > 0) || (r.getCounter() == 1 && r.getManiac() > 0)) {
                for (Map.Entry<Long, String> entry : playersMap.entrySet()) {
                    if (r.getRole(entry.getKey()).equals("Maniac")) {
                        r.setWinner(entry.getKey(), true);
                    }
                }
                GameEnd gameEnd = new GameEnd(getCurrentGroupId());
                gameEnd.start();
                return true;
            } else if (r.getMafia() + r.getDon() == 0) {
                for (Map.Entry<Long, String> entry : playersMap.entrySet()) {
                    if (!(r.getRole(entry.getKey()).equals("Maniac") || r.getRole(entry.getKey()).equals("Don")
                    || r.getRole(entry.getKey()).equals("Mafia") || r.getRole(entry.getKey()).equals("Suicide"))) {
                        r.setWinner(entry.getKey(), true);
                    }
                }
                GameEnd gameEnd = new GameEnd(getCurrentGroupId());
                gameEnd.start();
                return true;
            } else if ((r.getMafia() + r.getDon()) >= (r.getDoctor() + r.getCommissioner() +
                    r.getCivilian() + r.getSergeant() + r.getWhore() + r.getManiac() + r.getLawyer()
                    + r.getPersecutor() + r.getSamurai() + r.getSuicide()) && r.getCommissioner() == 0) {
                for (Map.Entry<Long, String> entry : playersMap.entrySet()) {
                    if (r.getRole(entry.getKey()).equals("Don") || r.getRole(entry.getKey()).equals("Mafia") ||
                            r.getRole(entry.getKey()).equals("Whore") || r.getRole(entry.getKey()).equals("Lawyer")) {
                        r.setWinner(entry.getKey(), true);
                    }
                }
                GameEnd gameEnd = new GameEnd(getCurrentGroupId());
                gameEnd.start();
                return true;
            }
        }
        return false;
    }
    //Чи примусове завершення гри
    public boolean isForceEnd() {return forceEnd;}
    public void setForceEnd(boolean b) {forceEnd = b;}

    //Відправити правила гри
    public void sendRules(long id) throws InterruptedException {
        MessageController mc = new MessageController();

        String description = "Мафія - це рольова гра з нотками детективу, суть якої зводиться до протистояння між " +
                "двома групами гравців - мафією та мирними жителями. До мафії відносяться ролі Дон і Мафія, всі " +
                "інші - мирні жителі. Мафія перемагає, коли в грі залишається більше гравців мафії, ніж мирних " +
                "жителів або їх рівна кількість. У кожної ролі свої можливості та завдання.";
        String time = "Гра ділиться на два періоди: ніч та день. Вночі гравці виконують специфічні їхнім ролям " +
                "дії. Вдень всі жителі міста збираються, щоб вирішити, хто з гравців мафія і повісити його.";
        String mafia = "*Мафія* - клан бандитів, що прагне абсолютної влади. Уночі кожен гравець Мафії голосує за " +
                "наступну жертву, яку, він вважає, потрібно вбити. Вдень він має не видати себе.";
        String don = "*Дон* - голова мафії. Його голос рахується за два, а коли виникає спірна ситуація - він " +
                "має вирішальний голос. Якщо Дон помирає - хтось із клану мафії займає його місце.";
        String civilian = "*Мирний житель* - законослухняний громадянин. Вночі його дії нічого не вирішують, проте " +
                "вдень він може голосувати на міських зборах і, таким чином, повісити мафію.";
        String doctor = "*Лікар* - вночі ви можете вибрати гравця, якого будете лікувати. Якщо вибраного гравця " +
                "намагатимуться вбити - ви його врятуєте.";
        String commissioner = "*Комісар Катані* - представник закону. Вночі може або перевірити гравця і дізнатися " +
                "його роль або когось вбити, якщо він впевнений, що це Мафія. (від 6 гравців)";
        String whore = "*Коханка* - обирає гравця, з яким проведе ніч. Вибраний гравець втрачає активну роль уночі " +
                "та не приймає участі в голосуванні вдень. Важке життя Коханки залишає їй лише одну ціль - вижити, " +
                "тому вона перемагає, якщо залишається живою. (від 8 гравців)";
        String persecutor = "*Переслідувач* - мабуть у нього також не всі дома, тому що він обожнює слідкувати за " +
                "людьми. Коли спостерігає за кимось - бачить у свій бінокль всіх, хто відвідує його ціль. " +
                "(від 10 гравців)";
        String lawyer = "*Адвокат* - висококваліфікований юрист. Він може захистити будь-кого від розправи " +
                "розлюченого натовпу навіть якщо це Дон. Адвокат навчився не лізти не у свої справи та дотримується " +
                "принципу \"тихіше їдеш, далі будеш\", тому залишитися в живих для нього вже перемога. (від 11 гравців)";
        String sergeant = "*Сержант* - помічник Комісара Катані. Він володіє всією інформацією про розслідування " +
                "Комісара Катані. Якщо Комісар загине - Сержант буде його наступником. (від 13 гравців)";
        String samurai = "*Самурай* - справжній поціновувач честі. Для нього найголовніше - служити. Тому самурай " +
                "може перемогти тільки в один спосіб - закрити своїм тілом нещасну жертву. (від 14 гравців)";
        String maniac = "*Маніяк* - хворий психопат, який хоче всіх убити. Перемагає лише коли залишається з кимось " +
                "сам на сам або є останнім, хто вижив. (від 15 гравців)";
        String suicide = "*Приречений* - типовий самогубець. Він не може наважитися власноруч зробити справу, тому " +
                "хоче щоб йому допомогли і... повісили. Загинути від кулі чи ножа - це не те. Він хоче, щоб, хоча б " +
                "в останні його хвилини, на нього звернуло увагу все місто. Тому перемога для Приреченого - тільки " +
                "повішення. (від 16 гравців)";
        String restriction = "Якщо тебе вбили, писати в чат заборонено. Вдавай мертвого до кінця гри. Спілкуватися " +
                "з іншими гравцями в приватних повідомленнях також не бажано. Для спілкування між членами " +
                "організації (Мафія або Комісар із Сержантом), можна використовувати чат з ботом - повідомлення, " +
                "відправлені боту будуть перенаправлені Вашій команді (якщо Ви ще не вмерли).";
        String goodLuck = "Щасти!";
        mc.sendMessageById(id, description);
        sleep(10000);
        mc.sendMessageById(id, time);
        sleep(7000);
        mc.sendMessageById(id, mafia);
        sleep(5000);
        mc.sendMessageById(id, don);
        sleep(5000);
        mc.sendMessageById(id, civilian);
        sleep(5000);
        mc.sendMessageById(id, doctor);
        sleep(5000);
        mc.sendMessageById(id, commissioner);
        sleep(5000);
        mc.sendMessageById(id, whore);
        sleep(5000);
        mc.sendMessageById(id, persecutor);
        sleep(5000);
        mc.sendMessageById(id, lawyer);
        sleep(5000);
        mc.sendMessageById(id, sergeant);
        sleep(5000);
        mc.sendMessageById(id, samurai);
        sleep(5000);
        mc.sendMessageById(id, maniac);
        sleep(5000);
        mc.sendMessageById(id, suicide);
        sleep(5000);
        mc.sendMessageById(id, restriction);
        sleep(5000);
        mc.sendMessageById(id, goodLuck);
    }
    //Відправити інструкцію
    public void sendHelp(long id) throws InterruptedException {
        MessageController mc = new MessageController();

        String t1 = "1. Спочатку потрібно додати бота до групового чату, де Ви хочете почати гру та зробити його " +
                "адміністратом чату (боту потрібен доступ до повідомлень).";
        String t2 = "\n2. Відправте в груповий чат команду \"/register\", щоб почати реєстрацію бажаючих гравців.";
        String t3 = "\n3. Натисніть кнопку \"Зареєструватися\", для переходу до чату з ботом і потім натисніть \"Start\"" +
                ", щоб зареєструватися.";
        String t4 = "\n4. Коли всі бажаючі зареєструвались, відправте у груповий чат команду \"/go\", щоб розпочати гру.";
        String t5 = "\n5. У груповому чаті бот, як ведучий, буде інформувати гравців про події які сталися, також там " +
                "можна обговорювати події ночі та розбиратися, хто є мафією. ";
        String t6 = "\n6. В особистий чат бот буде відправляти повідомлення, де можна вибирати дії, згідно Вашої ролі " +
                "(активна роль уночі та голосування вдень).";
        String t7 = "\n7. Якщо раптом необхідно завчасно припинити гру або реєстрацію, відправте команду \"/end\".";
        String t8 = "\n8. Щоб ознайомитися з правилами гри - відправте боту команду \"/rules\".";
        String t9 = "\n9. Ви також можете додавати ботів, відправивши команду \"/add_bots\". В отриманому повідомленні " +
                "виберіть необхідну кількість ботів і зачекайте, доки вони приєднаються.";
        String t10 = "\n10. Хорошої гри!";

        mc.sendMessageById(id, t1);
        sleep(1000);
        mc.sendMessageById(id, t2);
        sleep(1000);
        mc.sendMessageById(id, t3);
        sleep(1000);
        mc.sendMessageById(id, t4);
        sleep(1000);
        mc.sendMessageById(id, t5);
        sleep(1000);
        mc.sendMessageById(id, t6);
        sleep(1000);
        mc.sendMessageById(id, t7);
        sleep(1000);
        mc.sendMessageById(id, t8);
        sleep(1000);
        mc.sendMessageById(id, t9);
        sleep(1000);
        mc.sendMessageById(id, t10);
    }

    //Операції з тимчасовими повідомленнями
    public synchronized void addTemporaryMessages(long chatId, int msgId) {
        messageToDelete.put(chatId, msgId);
    }
    public synchronized void removeTemporaryMessages(long chatId, int msgId) {
        messageToDelete.remove(chatId, msgId);
    }
    public synchronized void deleteTemporaryMessages() throws InterruptedException {
        MessageController mc = new MessageController();
        if (!messageToDelete.isEmpty()) {
            for (Map.Entry<Long, Integer> entry : messageToDelete.entrySet()) {
                mc.deleteMessageById(entry.getValue(), entry.getKey());
            }
        }
        messageToDelete.clear();
    }
}
