package game;

import com.vdurmont.emoji.EmojiParser;
import services.*;

import java.util.List;
import java.util.ArrayList;

public class Rules {
    private final long currentGroupId;
    public List<Player> players = new ArrayList<>();
    public List<Player> allPlayers = new ArrayList<>();
    private boolean night;
    private boolean day = true;
    public boolean choiceToKill;
    public int confirm = 0;
    public int notConfirm = 0;

    private int counter = 0;
    private int don = 0;
    private int mafia = 0;
    private int doctor = 0;
    private int commissioner = 0;
    private int civilian = 0;
    private int sergeant = 0;
    private int whore = 0;
    private int maniac = 0;
    private int lawyer = 0;
    private int persecutor = 0;
    private int samurai = 0;
    private int suicide = 0;

    private long mafiaId1;
    private long mafiaId2;
    private long mafiaId3;
    private long mafiaId4;
    private long mafiaId5;

    public Rules(long groupId) {currentGroupId = groupId;}

    //Отримати id групи
    public synchronized long getCurrentGroupId() {
        return currentGroupId;
    }

    //Реєстрація гравців
    public String registerPlayers() {
        Service s = Storage.getService(currentGroupId);
        if (counter < 4) {
            return "Замало гравців(";
        } else if (counter > 20) {
            return "Забагато гравців(";
        } else {
            long[] temporary = new long[s.getPlayersMap().size()];
            for (int i = 0; i < s.getPlayersMap().size(); i++) {
                temporary[i] = (long) s.getPlayersMap().keySet().toArray()[i];
            }

            long[] temporary2 = new long[s.getPlayersMap().size()];

            if (Service.myRole.equals("Random") && Service.roleAlina.equals("Random")) {
                for (int i = 0; i < temporary.length; i++) {
                    int random = (int) (Math.random() * (getCounter() - 1));
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
            } else if (!Service.myRole.equals("Random") && Service.roleAlina.equals("Random")) {
                for (int i = 0; i < temporary.length; i++) {
                    if (temporary[i] == 401786507) {
                        temporary2[0] = temporary[i];
                        temporary[i] = 0;
                        break;
                    }
                }

                for (int i = 1; i < temporary.length; i++) {
                    int random = (int) (Math.random() * (getCounter() - 1));
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
            } else if (Service.myRole.equals("Random")) {
                for (int i = 0; i < temporary.length; i++) {
                    if (temporary[i] == 333155517) {
                        temporary2[0] = temporary[i];
                        temporary[i] = 0;
                        break;
                    }
                }

                for (int i = 1; i < temporary.length; i++) {
                    int random = (int) (Math.random() * (getCounter() - 1));
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
            } else {
                for (int i = 0; i < temporary.length; i++) {
                    if (temporary[i] == 401786507) {
                        temporary2[0] = temporary[i];
                        temporary[i] = 0;
                        break;
                    }
                }

                for (int i = 0; i < temporary.length; i++) {
                    if (temporary[i] == 333155517) {
                        temporary2[1] = temporary[i];
                        temporary[i] = 0;
                        break;
                    }
                }

                for (int i = 2; i < temporary.length; i++) {
                    int random = (int) (Math.random() * (getCounter() - 1));
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
            }

            for (long l : temporary2) {
                Player player = new Player(l, getCurrentGroupId());
                players.add(player);
                allPlayers.add(player);
                System.out.println(s.getPlayerFullNames(l) + " " + player.getRole());
            }

        }
        setNight();
        return "*Гра почалася!*";
    }

    //Генеруємо ролі
    public String generateRole(long id) {
        if (id == 401786507) {
            if (!Service.myRole.equals("Random")) {
                switch (Service.myRole) {
                    case "Don":
                        addDon();
                        break;
                    case "Mafia":
                        addMafia();
                        break;
                    case "Doctor":
                        addDoctor();
                        break;
                    case "Civilian":
                        addCivilian();
                        break;
                    case "Commissioner":
                        addCommissioner();
                        break;
                    case "Sergeant":
                        addSergeant();
                        break;
                    case "Whore":
                        addWhore();
                        break;
                    case "Maniac":
                        addManiac();
                        break;
                    case "Lawyer":
                        addLawyer();
                        break;
                    case "Persecutor":
                        addPersecutor();
                        break;
                    case "Samurai":
                        addSamurai();
                        break;
                    case "Suicide":
                        addSuicide();
                        break;
                }
                return Service.myRole;
            }
        } else if (id == 333155517) {
            if (!Service.roleAlina.equals("Random")) {
                switch (Service.roleAlina) {
                    case "Don":
                        addDon();
                        break;
                    case "Mafia":
                        addMafia();
                        break;
                    case "Doctor":
                        addDoctor();
                        break;
                    case "Civilian":
                        addCivilian();
                        break;
                    case "Commissioner":
                        addCommissioner();
                        break;
                    case "Sergeant":
                        addSergeant();
                        break;
                    case "Whore":
                        addWhore();
                        break;
                    case "Maniac":
                        addManiac();
                        break;
                    case "Lawyer":
                        addLawyer();
                        break;
                    case "Persecutor":
                        addPersecutor();
                        break;
                    case "Samurai":
                        addSamurai();
                        break;
                    case "Suicide":
                        addSuicide();
                        break;
                }
                return Service.roleAlina;
            }
        }

        if (id < 0) {
            if (getCounter() < 6 && getCivilian() < (getCounter() - 2)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() < 8 && getCivilian() < (getCounter() - 4)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 8 && getCivilian() < (getCounter() - 5)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 9 && getCivilian() < (getCounter() - 6)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 10 && getCivilian() < (getCounter() - 7)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 11 && getCivilian() < (getCounter() - 8)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 12 && getCivilian() < (getCounter() - 9)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 13 && getCivilian() < (getCounter() - 10)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 14 && getCivilian() < (getCounter() - 11)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 15 && getCivilian() < (getCounter() - 12)) {
                addCivilian();
                return "Civilian";
            } else if (getCounter() == 16 && getCivilian() < (getCounter() - 13)) {
                addCivilian();
                return "Civilian";
            } else if (getCivilian() < getCounter() - 14) {
                addCivilian();
                return "Civilian";
            }
        }

        int x = (int) (Math.random() * (getCounter() - 1) + 1);

        while (true) {
            if (getCounter() < 6) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    default: if (getCivilian() > 1 && (getDon() + getDoctor()) < 2) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() < 8) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getMafia() > 0) {++x;} else {addMafia();return "Mafia";}
                    case 4: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    default: if (getCivilian() > 1 && (getDon() + getMafia() + getDoctor() + getCommissioner()) < 4) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 8) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getMafia() > 0) {++x;} else {addMafia();return "Mafia";}
                    case 4: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 5: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() < 5) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 9) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    case 4: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 5: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 6: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() < 6) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 10) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 7: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getPersecutor() < 7) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 11) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 0) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getPersecutor() + getLawyer() < 8) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 12) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 0) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 1) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getPersecutor() + getLawyer() < 9) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 13) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getSergeant() > 0) {++x;} else {addSergeant();return "Sergeant";}
                    case 10: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getSergeant() + getPersecutor() + getLawyer() < 10) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 14) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getSergeant() > 0) {++x;} else {addSergeant();return "Sergeant";}
                    case 10: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 11: if (getSamurai() > 0) {++x;} else {addSamurai();return "Samurai";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getSergeant() + getPersecutor() + getLawyer() + getSamurai() < 11) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 15) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getSergeant() > 0) {++x;} else {addSergeant();return "Sergeant";}
                    case 10: if (getManiac() > 0) {++x;} else {addManiac();return "Maniac";}
                    case 11: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 12: if (getSamurai() > 0) {++x;} else {addSamurai();return "Samurai";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getSergeant() + getManiac() + getLawyer() + getPersecutor()
                            + getSamurai() < 12) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else if (getCounter() == 16) {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getSergeant() > 0) {++x;} else {addSergeant();return "Sergeant";}
                    case 10: if (getManiac() > 0) {++x;} else {addManiac();return "Maniac";}
                    case 11: if (getMafia() > 2) {++x;} else {addMafia();return "Mafia";}
                    case 12: if (getSamurai() > 0) {++x;} else {addSamurai();return "Samurai";}
                    case 13: if (getSuicide() > 0) {++x;} else {addSuicide();return "Suicide";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getSergeant() + getManiac() + getLawyer() + getPersecutor()
                            + getSamurai() + getSuicide() < 13) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            } else {
                switch (x) {
                    case 1: if (getDon() > 0) {++x;} else {addDon();return "Don";}
                    case 2: if (getDoctor() > 0) {++x;} else {addDoctor();return "Doctor";}
                    case 3: if (getPersecutor() > 0) {++x;} else {addPersecutor();return "Persecutor";}
                    case 4: if (getMafia() > 3) {++x;} else {addMafia();return "Mafia";}
                    case 5: if (getCommissioner() > 0) {++x;} else {addCommissioner();return "Commissioner";}
                    case 6: if (getLawyer() > 0) {++x;} else {addLawyer();return "Lawyer";}
                    case 7: if (getMafia() > 3) {++x;} else {addMafia();return "Mafia";}
                    case 8: if (getWhore() > 0) {++x;} else {addWhore();return "Whore";}
                    case 9: if (getSergeant() > 0) {++x;} else {addSergeant();return "Sergeant";}
                    case 10: if (getManiac() > 0) {++x;} else {addManiac();return "Maniac";}
                    case 11: if (getMafia() > 3) {++x;} else {addMafia();return "Mafia";}
                    case 12: if (getSamurai() > 0) {++x;} else {addSamurai();return "Samurai";}
                    case 13: if (getSuicide() > 0) {++x;} else {addSuicide();return "Suicide";}
                    case 14: if (getMafia() > 3) {++x;} else {addMafia();return "Mafia";}
                    default: if (getCivilian() > 2 && (getDon() + getMafia() + getDoctor() + getCommissioner())
                            + getWhore() + getSergeant() + getManiac() + getLawyer() + getPersecutor() + getSamurai()
                            + getSuicide() < 14) {
                        x = 1;} else {addCivilian();return "Civilian";}
                }
            }
        }
    }

    public int getCounter() {return counter;}
    public void addCounter() {++counter;}
    public void subtractCounter() {--counter;}

    //Отримати кількість ролей
    public int getDon() {return don;}
    public int getMafia() {return mafia;}
    public int getDoctor() {return doctor;}
    public int getCommissioner() {return commissioner;}
    public int getCivilian() {return civilian;}
    public int getSergeant() {return sergeant;}
    public int getWhore() {return whore;}
    public int getManiac() {return maniac;}
    public int getLawyer() {return lawyer;}
    public int getPersecutor() {return persecutor;}
    public int getSamurai() {return samurai;}
    public int getSuicide() {return suicide;}

    //Додати до кількості ролі
    public void addDon() {++don;}
    public void addMafia() {++mafia;}
    public void addDoctor() {++doctor;}
    public void addCommissioner() {++commissioner;}
    public void addCivilian() {++civilian;}
    public void addSergeant() {++sergeant;}
    public void addWhore() {++whore;}
    public void addManiac() {++maniac;}
    public void addLawyer() {++lawyer;}
    public void addPersecutor() {++persecutor;}
    public void addSamurai() {++samurai;}
    public void addSuicide() {++suicide;}

    //Відняти від кількості ролей
    public void subtractDon() {--don;}
    public void subtractMafia() {--mafia;}
    public void subtractDoctor() {--doctor;}
    public void subtractCommissioner() {--commissioner;}
    public void subtractCivilian() {--civilian;}
    public void subtractSergeant() {--sergeant;}
    public void subtractWhore() {--whore;}
    public void subtractManiac() {--maniac;}
    public void subtractLawyer() {--lawyer;}
    public void subtractPersecutor() {--persecutor;}
    public void subtractSamurai() {--samurai;}
    public void subtractSuicide() {--suicide;}
    public void nullCounters() {
        counter = 0;
        don = 0;
        mafia = 0;
        doctor = 0;
        commissioner = 0;
        civilian = 0;
        sergeant = 0;
        whore = 0;
        maniac = 0;
        lawyer = 0;
        persecutor = 0;
        samurai = 0;
        suicide = 0;

        mafiaId1 = 0;
        mafiaId2 = 0;
        mafiaId3 = 0;
        mafiaId4 = 0;
        mafiaId5 = 0;
    }

    //Операції з підтвердженнями
    public int getConfirm() {return confirm;}
    public void addConfirm() {++confirm;}
    public void subtractConfirm() {--confirm;}
    public int getNotConfirm() {return notConfirm;}
    public void addNotConfirm() {++notConfirm;}
    public void subtractNotConfirm() {--notConfirm;}
    public void nullConfirms() {confirm = 0; notConfirm = 0;}

    //Перевіряємо чи гравець живий
    public boolean isAlive(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isAlivePlayer();
            }
        }
        return false;
    }

    //Перемикаємо день і ніч
    public boolean isNight() {return night;}
    public void setNight() {night = true;day = false;}
    public boolean isDay() {return day;}
    public void setDay() {day = true;night = false;}

    //Перевіряємо чи воскресив когось лікар
    public boolean deadAlive (long id) {
        for (Player p : players) {
            if ((p.isMafiasVisited() || (p.isCommissionersVisited() && choiceToKill) || p.isManiacVisited())
                    && p.isDoctorsVisited() && p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    //Очистити усі параметри
    public void clearAllRules() {
        for (Player p : players) {
            p.doctorsVisitedPreviousNight = false;
            p.setCommissionerVisitedThisGame(false);
        }
        night = false;
        day = true;
        nullCounters();
        counter = 0;
        clearVotes();
        players.clear();
        allPlayers.clear();
    }

    //Отримати роль гравця
    public String getRole(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.getRole();
            }
        }
        return "Хм, а ти взагалі хто?";
    }

    //Мафія голосує кого вбивати
    public void mafiaVotes(long id, String role) {
        if (role.equals("Don")) {
            for (Player p : players) {
                if (p.getId() == id) {
                    p.addMafiasVotes();
                    p.addMafiasVotes();
                    p.addDonVotes();
                    break;
                }
            }
        } else {
            for (Player p : players) {
                if (p.getId() == id) {
                    p.addMafiasVotes();
                    break;
                }
            }
        }
    }
    //Мафія вбиває
    public long mafiaKillPlayer(int max, int counter) {
        long id = 0;
        if (counter == 1) {
            for (Player p : players) {
                if (p.getMafiasVotes() == max) {
                    p.setMafiasVisited(true);
                    p.setAlive(false);
                    id = p.getId();
                }
            }
        } else {
            for (Player p : players) {
                if (p.getDonVotes() == 1) {
                    p.setMafiasVisited(true);
                    p.setAlive(false);
                    id = p.getId();
                }
            }
        }
        return id;
    }
    //Мафія обирає жертву
    public long mafiaKill() {
        int max = 0;
        int counter = 0;
        for (Player p : players) {
            if (p.getMafiasVotes() > max) {
                max = p.getMafiasVotes();
            }
        }

        for (Player p : players) {
            if (p.getMafiasVotes() == max) {
                ++counter;
            }
        }

        long deadId = mafiaKillPlayer(max, counter);

        for (Player p : players) {
            p.nullMafiasVotes();
            p.nullDonVotes();
        }
        return deadId;
    }

    public boolean allWasVisited() {
        for (Player p : players) {
            if (!p.commissionerVisitedThisGame) {
                return false;
            }
        }

        return true;
    }

    //Комісар обирає гравця
    public void commissionerVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.addCommissionerVotes();
                p.setCommissionersVisited(true);
            }
        }
    }
    //Комісар виконує свою роботу
    public long commissionerChoose() {
        long killedByCommissioner = 0;
        for (Player p : players) {
            if (p.getCommissionerVotes() == 1) {
                if (choiceToKill) {
                    p.setAlive(false);
                }
                killedByCommissioner = p.getId();
            }
        }
        return killedByCommissioner;
    }
    //Встановити, що комісар уже відвідував цього гравця
    public void setCommissionerVisitedThisGame(long id, boolean b) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setCommissionerVisitedThisGame(b);
            }
        }
    }
    //Чи комісар уже відвідував цього гравця
    public boolean isCommissionerVisitedThisGame(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isCommissionerVisitedThisGame();
            }
        }
        return false;
    }
    //Чи був комісар або сержант минулої ночі у мафіїї
    public boolean isPoliceVisitedMafiaPreviousNight() {
        return mafiaId1 != 0 || mafiaId2 !=0 || mafiaId3 != 0 || mafiaId4 != 0 || mafiaId5 != 0;
    }
    //Отримати id мафії
    public long getMafiaId() {
        if (mafiaId1 != 0) {
            return mafiaId1;
        } else if (mafiaId2 != 0) {
            return mafiaId2;
        } else if (mafiaId3 != 0) {
            return mafiaId3;
        } else if (mafiaId4 != 0) {
            return mafiaId4;
        } else {
            return mafiaId5;
        }
    }
    //Чи є id гравця у списку мафії
    public boolean findInMafiaId(long id) {
        return mafiaId1 == id || mafiaId2 == id || mafiaId3 == id || mafiaId4 == id || mafiaId5 == id;
    }
    //Прибрати вбитого зі списку мафії
    public void removeDead(long id) {
        if (mafiaId1 == id) {
            mafiaId1 = 0;
        } else if (mafiaId2 == id) {
            mafiaId2 = 0;
        } else if (mafiaId3 == id) {
            mafiaId3 = 0;
        } else if (mafiaId4 == id) {
            mafiaId4 = 0;
        } else if (mafiaId5 == id) {
            mafiaId5 = 0;
        }
    }
    //Встановити, що комісар або сержант був у мафії і записати id мафії
    public void setPoliceVisitedMafia(long id) {
        if (mafiaId1 == 0) {
            mafiaId1 = id;
        } else if (mafiaId2 == 0) {
            mafiaId2 = id;
        } else if (mafiaId3 == 0) {
            mafiaId3 = id;
        } else if (mafiaId4 == 0) {
            mafiaId4 = id;
        } else if (mafiaId5 == 0) {
            mafiaId5 = id;
        }
    }

    public boolean isDoctorVisited(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isDoctorsVisited();
            }
        }
        return false;
    }
    //Доктор обирає до кого прийти
    public void doctorVotes(long chatId) {
        for (Player p : players) {
            if (p.getId() == chatId) {
                p.setDoctorsVisited(true);
            }
        }
    }
    //Доктор лікує
    public String doctorHeal(String role, Service s) {
        //Відправити повідомлення самураю
        if (role.equals("Samurai")) {
            long samurai = 0;
            for (Player p : players) {
                if (p.role.equals("Samurai")) {
                    p.setAlive(true);
                    samurai = p.getId();
                    break;
                }
            }
            for (Player p : players) {
                if (p.isDoctorsVisited() || isDoctorVisited(samurai)) {
                    long savedId = p.getId();
                    if (p.isMafiasVisited() && p.isCommissionersVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return "Стріляли всі: маніяк, мафія, комісар... " + s.getPlayerMap(savedId) + " ще дихає " +
                                "завдяки тобі. А ти завдяки лікарю.";
                    } else if (p.isMafiasVisited() && p.isCommissionersVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return s.getPlayerMap(savedId) + " стає ціллю мафії та комісара, але ти стаєш рятівником. " +
                                "Щоправда потім довелось рятувати тебе і лікарю це вдалося.";
                    } else if (p.isMafiasVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return "Маніяк і мафія розраховували, що " + s.getPlayerMap(savedId) + " буде не з нами, але " +
                                "ти ламаєш всі їхні плани. А лікар тебе реанімував.";
                    } else if (p.isCommissionersVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return s.getPlayerMap(savedId) + " залишається в живих після замаху маніяка та комісара " +
                                "завдяки твоєму втручанню. А тебе рятує лікар.";
                    } else if (p.isMafiasVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return "Як тобі розповіли в лікарні, в ту ніч виявилося, що на прицілі мафії - "
                                + s.getPlayerMap(savedId) + ", і ти без зайвих роздумів вирішуєш стрибнути під кулі. " +
                                "На щастя лікар тебе також врятував.";
                    } else if (p.isCommissionersVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return "Спочатку комісар і " + s.getPlayerMap(savedId) + " голосно сперечалися, потім " +
                                "комісар дістав револьвер, а ти прикрив жертву собою. Благо швидка приїхала швидко " +
                                "і лікар тебе воскресив.";
                    } else if (p.isManiacVisited()) {
                        p.setAlive(true);
                        setWinner(p.getId(), false);
                        return "Маніяк вирішив, що його жертва - " + s.getPlayerMap(savedId) + ". Але він не " +
                                "врахував твою самопожертву. Але лікар вилікував і тебе.";
                    }
                }
            }

        //Відправити повідомлення жертві
        } else if (role.equals("Target")) {
            for (Player p : players) {
                if (p.isDoctorsVisited()) {
                    if (!p.isAlivePlayer() && p.isMafiasVisited() && p.isCommissionersVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "Спочатку на тебе напав маніяк у парку, а потім на тебе розпочали сафарі і мафія, " +
                                    "і комісар, але ти недарма вчився в інтернатурі, тому вижив.";
                        } else {
                            return "І маніяк, і мафія, і комісар. Всі хотіли твоєї смерті. На щастя ти товаришуєш " +
                                    "із лікарем і він тебе врятував. Здається пора переїжджати з цього міста.";
                        }
                    } else if (!p.isAlivePlayer() && p.isMafiasVisited() && p.isCommissionersVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "В тебе стріляли і мафія, і комісар, але ти сьогодні красунчик, сам себе реанімував. " +
                                    "Двічі.";
                        } else {
                            return "Сьогодні до тебе навідалась мафія і намагалась розстріляти, але лікар тебе " +
                                    "реанімував. Щоправда потім прийшов комісар, який також хотів твоєї смерті. " +
                                    "Добре, що лікар недалеко від'їхав, тому ти поки що живий.";
                        }
                    } else if (!p.isAlivePlayer() && p.isMafiasVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "Здається маніяк із мафією змовились, щоб тебе вбити. Але ти вилікував себе.";
                        } else {
                            return "У місті високий рівень злочинності й ти відчув це на собі. Від ножа маніяка та " +
                                    "пістолета мафії. На щастя лікар тебе врятував і від леза, і від куль.";
                        }
                    } else if (!p.isAlivePlayer() && p.isCommissionersVisited() && p.isManiacVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "Що за люди? Маніяк хотів тебе зарізати, комісар застрелити. Добре, що ти лікар " +
                                    "і зміг сам собі надати медичну допомогу.";
                        } else {
                            return "Тепер ти точно знаєш, що комісар не маніяк - бо це дві різні людини і вони обоє " +
                                    "сьогодні на тебе напали. Щоправда лікар тебе вилікував.";
                        }
                    } else if (!p.isAlivePlayer() && p.isMafiasVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "Мафія стріляла в тебе, але ти сам себе зцілив!";
                        } else {
                            return "Лікар тебе воскресив. Мафії нічого не вдалося.";
                        }
                    } else if (!p.isAlivePlayer() && p.isCommissionersVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Don") || p.getRole().equals("Mafia")) {
                            return "Комісар в тебе стріляв, але лікар вилікував твоє поранення. Поки що живемо, бандите.";
                        } else if (p.getRole().equals("Doctor")) {
                            return "Комісар хотів тебе вбити, але тобі вдалося перев'язати рани. Ти випадково не винен " +
                                    "йому гроші?";
                        } else {
                            return "Здається комісар в тебе стріляв, але лікар вилікував. Є кілька питань до комісара.";
                        }
                    } else if (!p.isAlivePlayer() && p.isManiacVisited()) {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "На тебе напав маніяк в парку, але тобі вдалось зашити рани.";
                        } else {
                            return "Здається з кущів на тебе вистрибнув маніяк. Але прохожі викликали швидку й лікар " +
                                    "тебе врятував.";
                        }
                    } else {
                        p.setAlive(true);
                        if (p.getRole().equals("Doctor")) {
                            return "Ну ти й егоїст.";
                        } else {
                            return "Лікар приходив до тебе.";
                        }
                    }
                }
            }
        }
        return "";
    }
    //До кого приходив лікар
    public long wasHealed() {
        for (Player p : players) {
            if (p.isDoctorsVisited()) {
                return p.getId();
            }
        }
        return 0;
    }
    //Чи був лікар у гравця минулої ночі
    public boolean isDoctorVisitedLastNight(long id) {
        for (Player p : players) {
            if ((p.getId() == id) && p.isDoctorsVisitedPreviousNight()) {
                return true;
            }
        }
        return false;
    }

    public void whoreVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setWhoreVisited(true);
            }
        }
    }
    public void whoreBlocked(long groupId) {
        Rules r = Storage.getRules(groupId);
        long visited = 0;
        long chosen = 0;
        String role = "";
        for (Player p : players) {
            if (p.isWhoreVisited()) {
                visited = p.getId();
                role = r.getRole(visited);
                chosen = p.getChosenByYou();
                break;
            }
        }

        if (visited != 0) {
            Service s = Storage.getService(groupId);
            s.addPlayersConfirmed(); //Додати до списку тих, хто проголосував
            for (Player p : players) {
                if (p.getId() == chosen) {
                    switch (role) {
                        case "Don":
                            p.subtractMafiasVotes();
                            p.subtractMafiasVotes();
                            p.subtractDonVotes();
                            break;
                        case "Mafia":
                            p.subtractMafiasVotes();
                            break;
                        case "Doctor":
                            p.setDoctorsVisited(false);
                            break;
                        case "Commissioner":
                            p.subtractCommissionerVotes();
                            p.setCommissionersVisited(false);
                            p.setCommissionerVisitedThisGame(false);
                            if (!choiceToKill) {
                                removeDead(chosen);
                            }
                            break;
                        case "Maniac":
                            p.setManiacVisited(false);
                            break;
                        case "Lawyer":
                            p.setLawyerVisited(false);
                            break;
                        case "Persecutor":
                            p.setPersecutorVisited(false);
                            break;
                        case "Samurai":
                            p.setSamuraiVisited(false);
                            break;
                        case "Civilian":
                        case "Sergeant":
                        case "Suicide":
                            break;
                    }
                }
            }
        }
    }
    public boolean whoreVisited(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isWhoreVisited();
            }
        }
        return false;
    }

    public void maniacVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setManiacVisited(true);
            }
        }
    }
    public long maniacKill() {
        for (Player p : players) {
            if (p.isManiacVisited()) {
                p.setAlive(false);
                return p.getId();
            }
        }
        return 0;
    }

    public void lawyerVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setLawyerVisited(true);
            }
        }
    }
    public boolean lawyerProtect(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isLawyerVisited();
            }
        }
        return false;
    }

    public void persecutorVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setPersecutorVisited(true);
            }
        }
    }
    public String persecutorWatch(Service s, long id) {
        long watched = 0;
        boolean isMafiaVisited = false;
        StringBuilder answer = new StringBuilder("Ти бачив, як ");
        for (Player p : players) {
            if (p.isPersecutorVisited()) {
                watched = p.getId();
                answer.append(s.getPlayerMap(watched)).append(" комусь відчиняє двері. ");
                isMafiaVisited = p.isMafiasVisited();
            }
        }
        int visitors = 0;
        if (watched != 0) {
            for (Player p : players) {
                if (p.getChosenByYou() == watched) {
                    if (p.getId() == id) {
                        continue;
                    }
                    if (isMafiaVisited && getRole(p.getId()).equals("Mafia")) {
                        continue;
                    }
                    if (visitors < 1) {
                        answer.append("Це ").append(s.getPlayerFullNames(p.getId())).append(".");
                    } else if (visitors < 2) {
                        answer.append(" Був ще хтось, здається ").append(s.getPlayerFullNames(p.getId())).append(".");
                    } else {
                        answer.append(" І ще ").append(s.getPlayerFullNames(p.getId())).append(".");
                    }
                    ++visitors;
                }
            }
            if (visitors > 0) {
                return String.valueOf(answer);
            } else {
                return "Нічого особливого не відбувалось.";
            }
        }
        return "";
    }

    public void samuraiVotes(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setSamuraiVisited(true);
            }
        }
    }
    public void samuraiSave() throws InterruptedException {
        for (Player p : players) {
            if (p.isSamuraiVisited()) {
                p.setAlive(true);
            }
            if (p.role.equals("Samurai")) {
                MessageController mc = new MessageController();
                p.setAlive(false);
                setWinner(p.getId(), true);
                if (p.getId() > 0) {
                    mc.sendMessageById(p.getId(), "Ти захистив свою честь і врятував невинне життя.");
                }
            }
        }
    }
    public long samuraiVisited() {
        for (Player p : players) {
            if (p.isSamuraiVisited()) {
                return p.getId();
            }
        }
        return 0;
    }
    public String wasSaved(long savedId) {
        for (Player p : players) {
            if (p.getId() == savedId) {
                if (p.isMafiasVisited() && p.isCommissionersVisited() && p.isManiacVisited()) {
                    return "Тебе хотіли вбити всі, кому не лінь: маніяк, мафія, " +
                            "комісар. Проте твоє життя врятував самурай.";
                } else if (p.isMafiasVisited() && p.isCommissionersVisited()) {
                    return "Самурай врятував тебе від куль мафії та комісара.";
                } else if (p.isMafiasVisited() && p.isManiacVisited()) {
                    return "Мафія з маніяком здається домовились тебе вбити, але тебе " +
                            "прикрив собою самурай.";
                } else if (p.isCommissionersVisited() && p.isManiacVisited()) {
                    return "Самурай закрив тебе собою і від ножа маніяка, і від куль " +
                            "комісара.";
                } else if (p.isMafiasVisited()) {
                    return "Самурай зберіг твоє життя, коли мафія в тебе стріляла.";
                } else if (p.isCommissionersVisited()) {
                    return "Самурай врятував тебе від смерті од руки комісара.";
                } else if (p.isManiacVisited()) {
                    return "Самурай врятував тебе від маніяка.";
                }
            }
        }
        return "";
    }

    public void setChosen(long id, long chosen) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setChosenByYou(chosen);
            }
        }
    }
    public long getChosen(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.getChosenByYou();
            }
        }
        return 0;
    }

    //Проголосувати за гравця
    public synchronized void setVote(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.addVote();
                break;
            }
        }
    }
    //Чи голосував гравець
    public synchronized boolean getVotedRules(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.getVoted();
            }
        }
        return false;
    }
    //Встановити, що гравець вже голосував
    public synchronized void setVotedRules(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setVoted(true);
            }
        }
    }
    //Очистити голоси
    public void nullVotes() {
        for (Player p : players) {
            p.nullVotesPlayer();
            p.setVoted(false);
        }
    }

    //Кого вішають
    public long whoIsHanged() {
        long hanged = 0;
        int maxVotes = 0;

        //Знаходимо максимальну кількість голосів
        for (Player p : players) {
            if (p.getVotes() > maxVotes) {
                maxVotes = p.getVotes();
            }
        }

        //Знаходимо скільки гравців мають максимальну кількість голосів
        int counter = 0;
        for (Player pMax : players) {
            if (pMax.getVotes() == maxVotes) {
                ++counter;
            }
        }

        //Якщо обрали когось одного - повертаємо його id
        if (!(counter > 1 || counter == 0)) {
            for (Player p : players) {
                if (p.getVotes() == maxVotes) {
                    hanged = p.getId();
                }
            }
        }

        //Повертаємо 0, якщо нікого не обрали
        return hanged;
    }
    //Повісити гравця
    public void setHanged(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setAlive(false);
            }
        }
    }

    //Встановити підтвердження
    public void setYouConfirm(long id, boolean b) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setYouConfirm(b);
            }
        }
    }
    //Чи гравець вже підтверджував
    public boolean areYouConfirmed(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.isYouConfirm();
            }
        }
        return false;
    }
    //Гравець голосував за чи проти
    public boolean yesNo(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p.getConfirmVote();
            }
        }
        return false;
    }

    //Чи є останнє слово
    public boolean isLastWord(long id) {
        for (Player p : allPlayers) {
            if (p.getId() == id) {
                return p.isLastWord();
            }
        }
        return false;
    }
    //Закрити останнє слово
    public void clearLastWord(long id) {
        for (Player p : allPlayers) {
            if (p.getId() == id) {
                p.setLastWord(false);
            }
        }
    }
    //Дати останнє слово
    public void setLastWord(long id) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.setLastWord(true);
            }
        }
    }
    //Закрити останнє слово всім
    public void clearLastWordForAll() {
        for (Player p : players) {
            p.setLastWord(false);
        }
    }

    //Очистити візити
    public void nullVisited() {
        for (Player p : players) {
            p.setMafiasVisited(false);
            p.setCommissionersVisited(false);
            p.doctorsVisitedPreviousNight = p.doctorsVisited;
            p.setDoctorsVisited(false);
            p.setWhoreVisited(false);
            p.setManiacVisited(false);
            p.setLawyerVisited(false);
            p.setPersecutorVisited(false);
            p.setSamuraiVisited(false);
            p.nullCommissionerVotes();
            p.nullMafiasVotes();
            p.nullDonVotes();
        }
    }
    //Очистити всі голоси
    public void clearVotes() {
        nullVotes();
        nullVisited();
        clearConfirms();
        clearLastWordForAll();
        nullConfirms();
    }
    //Очистити підтвердження
    public void clearConfirms() {
        for (Player p : players) {
            p.nullYourConfirm();
        }
    }

    public void setWinner(long id, boolean winner) {
        for (Player p : players) {
            if (p.getId() == id) {
                p.isWinner = winner;
                break;
            }
        }
    }
    public boolean isWinner(long id) {
        for (Player p : allPlayers) {
            if (p.getId() == id) {
                return p.isWinner;
            }
        }
        return false;
    }

    //Видалити мертвого гравця
    public void removePlayerInRules() throws InterruptedException {
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if (!p.isAlivePlayer()) {
                players.remove(p);
                subtractCounter();
                removeDead(p.getId());
                String role = p.role;
                switch (role) {
                    case "Mafia":
                        if (findInMafiaId(p.getId())) {
                            removeDead(p.getId());
                        }
                        subtractMafia();
                        --i;
                        break;
                    case "Doctor":
                        subtractDoctor();
                        --i;
                        break;
                    case "Commissioner":
                        subtractCommissioner();
                        --i;
                        if (getSergeant() == 0) {
                            break;
                        }
                        for (Player p1 : players) {
                            if (p1.role.equals("Sergeant")) {
                                Service s = Storage.getService(currentGroupId);
                                MessageController mc = new MessageController();
                                p1.role = "Commissioner";
                                addCommissioner();
                                subtractSergeant();
                                if (p1.getId() > 0) {
                                    mc.sendMessageById(p1.getId(), "Тепер Ви - *Комісар Катані*. Ви - промінь надії" +
                                            " в цьому, потонулому у темряві, місті. Ваше завдання - вирахувати мафію і" +
                                            " ліквідувати її.");
                                }
                                StringBuilder value = new StringBuilder();
                                if (p1.getId() > 0) {
                                    value.append("\n").append("[").append(s.getPlayerFullNames(p1.getId())).
                                            append("](tg://user?id=").append(p1.getId()).append(") - ").
                                            append(EmojiParser.parseToUnicode(":detective:")).
                                            append(" *Комісар Катані*");
                                } else {
                                    value.append("\n").append(s.getPlayerFullNames(p1.getId())).append(" - ").
                                            append(EmojiParser.parseToUnicode(":detective:")).
                                            append(" *Комісар Катані*");
                                }
                                s.putPlayerInRoles(p1.getId(), String.valueOf(value));
                                break;
                            }
                        }
                        break;
                    case "Civilian":
                        subtractCivilian();
                        --i;
                        break;
                    case "Sergeant":
                        subtractSergeant();
                        --i;
                        break;
                    case "Whore":
                        subtractWhore();
                        --i;
                        break;
                    case "Maniac":
                        subtractManiac();
                        --i;
                        break;
                    case "Lawyer":
                        subtractLawyer();
                        --i;
                        break;
                    case "Persecutor":
                        subtractPersecutor();
                        --i;
                        break;
                    case "Samurai":
                        subtractSamurai();
                        --i;
                        break;
                    case "Suicide":
                        subtractSuicide();
                        --i;
                        break;
                    case "Don":
                        if (findInMafiaId(p.getId())) {
                            removeDead(p.getId());
                        }
                        subtractDon();
                        --i;
                        if (getMafia() == 0) {
                            break;
                        }
                        for (Player p2 : players) {
                            if (p2.role.equals("Mafia")) {
                                Service s = Storage.getService(currentGroupId);
                                MessageController mc = new MessageController();
                                p2.role = "Don";
                                addDon();
                                subtractMafia();
                                if (p2.getId() > 0) {
                                    mc.sendMessageById(p2.getId(), "Тепер Ви - *Дон*, голова мафії. Ваш голос" +
                                            " рахується за два і за Вами останнє слово. Задача для Вас і ваших" +
                                            " поплічників мафії - вбити якомога більше людей.");
                                }
                                StringBuilder value = new StringBuilder();
                                if (p2.getId() > 0) {
                                    value.append("\n").append("[").append(s.getPlayerFullNames(p2.getId())).
                                            append("](tg://user?id=").append(p2.getId()).append(") - ").
                                            append(EmojiParser.parseToUnicode(":bust_in_silhouette:")).
                                            append(" *Дон*");
                                } else {
                                    value.append("\n").append(s.getPlayerFullNames(p2.getId())).append(" - ").
                                            append(EmojiParser.parseToUnicode(":bust_in_silhouette:")).
                                            append(" *Дон*");
                                }
                                s.putPlayerInRoles(p2.getId(), String.valueOf(value));
                                break;
                            }
                        }
                        break;
                }
            }
        }
    }
}