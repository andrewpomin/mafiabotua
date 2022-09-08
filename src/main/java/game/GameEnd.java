package game;

import services.Storage;
import services.Service;
import services.MessageController;

public class GameEnd extends Thread implements Runnable {
    long groupId;
    public GameEnd(long id) {
        groupId = id;
    }

    @Override
    public void run() {
        Service s = Storage.getService(groupId);
        Rules r = Storage.getRules(groupId);
        MessageController mc = new MessageController();
        try {
            if (!s.isForceEnd()) {
                sleep(3000);
                if (r.getCounter() == 0) {
                    sleep(3000);
                    s.setDraw(true);
                    mc.sendMessageById(s.getCurrentGroupId(),
                            "*Отакої! Місто вимерло, нікого не залишилось в живих.*");
                } else if ((r.getCounter() == 2 && r.getManiac() > 0) || (r.getCounter() == 1 && r.getManiac() > 0)) {
                    sleep(3000);
                    mc.sendMessageById(s.getCurrentGroupId(),
                            "*Маніяк переміг!*");
                } else if (r.getMafia() + r.getDon() == 0) {
                    sleep(3000);
                    mc.sendMessageById(s.getCurrentGroupId(),
                            "*Мирні жителі перебили всю мафію (можливо і не тільки)!*");
                } else if ((r.getMafia() + r.getDon()) >= (r.getDoctor() + r.getCommissioner() +
                        r.getCivilian() + r.getSergeant() + r.getWhore() + r.getManiac() + r.getLawyer()
                        + r.getPersecutor() + r.getSamurai() + r.getSuicide()) && r.getCommissioner() == 0) {
                    sleep(3000);
                    s.setMafiaWin(true);
                    mc.sendMessageById(s.getCurrentGroupId(), "*Мафія перемогла!*");
                }
            }

            s.deleteTemporaryMessages();

            sleep(3000);

            s.printResultList();

            sleep(3000);
            mc.sendMessageById(s.getCurrentGroupId(), "*Дякую за гру!*");
            s.clearAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
