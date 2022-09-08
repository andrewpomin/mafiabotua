package game;

import services.MessageController;
import services.Storage;
import services.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GameThread extends Thread implements Runnable {
    long groupId;
    GameThread(long id) {
        groupId = id;
    }
    @Override
    public void run() {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        Game g = new Game();
        MessageController mc = new MessageController();
        try {
            int c;
            while (!s.isGameEnd()) {
                if (s.isGameEnd()) {
                    return;
                }
                g.night(groupId);

                if (s.isGameEnd()) {
                    return;
                }
                c = 0;
                while (true) {
                    if (s.allVoted()) {
                        sleep(5000);
                        g.nightResults(groupId);
                        break;
                    } else {
                        if (c <= s.getNightTime()) {
                            sleep(10000);
                            c += 10;
                        } else {
                            g.nightResults(groupId);
                            break;
                        }
                    }
                }

                if (s.isGameEnd()) {
                    return;
                }
                sleep(3000);
                s.printStillAliveList();

                sleep(s.getDayTime() * 1000L);

                if (s.isGameEnd()) {
                    return;
                }
                g.day(groupId);

                if (s.isGameEnd()) {
                    return;
                }
                c = 0;
                while (true) {
                    if (s.allVoted()) {
                        sleep(5000);
                        g.dayResults(groupId);
                        break;
                    } else {
                        if (c < s.getVoteTime()) {
                            sleep(5000);
                            c += 5;
                            mc.updateWhoVoted(groupId);
                        } else {
                            g.dayResults(groupId);
                            break;
                        }
                    }
                }

                if (s.isGameEnd()) {
                    return;
                }
                if (!(r.whoIsHanged() == 0)) {
                    c = 0;
                    while (true) {
                        if (s.allVoted()) {
                            sleep(5000);
                            g.voteResults(groupId);
                            break;
                        } else {
                            if (c < s.getConfirmTime()) {
                                    sleep(5000);
                                    c += 5;
                                    mc.updateWhoConfirmed(groupId);
                            } else {
                                g.voteResults(groupId);
                                break;
                            }
                        }
                    }
                }
                sleep(5000);
                if (s.isGameEnd()) {
                    return;
                }
            }
        } catch (InterruptedException e) {
            GameEnd gameEnd = new GameEnd(s.getCurrentGroupId());
            gameEnd.start();
//            throw new RuntimeException(e);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
