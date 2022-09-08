package services;

import game.Rules;
import java.util.HashSet;

public class Storage {
    public static HashSet<Service> servicesSet = new HashSet<>();
    public static HashSet<Rules> rulesSet = new HashSet<>();

    //Додати помічника
    public static synchronized Service addService(Service s) {
        for (Service s2 : servicesSet) {
            if (s2.getCurrentGroupId() == s.getCurrentGroupId()) {
                return s2;
            }
        }
        servicesSet.add(s);
        return s;
    }
    //Очистити список помічників
    public static synchronized void clearService() {servicesSet.clear();}
    //Отримати помічника за id чгрупи
    public static synchronized Service getService(long id) {
        for (Service s : servicesSet) {
            if (s.getCurrentGroupId() == id) {
                return s;
            }
        }
        return new Service(0);
    }
    //Знайти id групи за id гравця
    public static synchronized long findGroupIdInService(long playerId) {
        try {
            for (Service s : servicesSet) {
                if (s.isPlayerInMap(playerId)) {
                    return s.getCurrentGroupId();
                }
            }
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    //Додати об'єкт правил
    public static synchronized Rules addRules(Rules r) {
        for (Rules r2 : rulesSet) {
            if (r2.getCurrentGroupId() == r.getCurrentGroupId()) {
                return r2;
            }
        }
        rulesSet.add(r);
        return r;
    }
    //Прибрати об'єкт правил зі списку
    public static synchronized void removeRules(Rules r) {rulesSet.remove(r);}
    //Очистити список об'єктів правил
    public static synchronized void clearRules() {rulesSet.clear();}
    //Отримати об'єкт правил за id групи
    public static synchronized Rules getRules(long id) {
        for (Rules r : rulesSet) {
            if (r.getCurrentGroupId() == id) {
                return r;
            }
        }
        return new Rules(0);
    }
}
