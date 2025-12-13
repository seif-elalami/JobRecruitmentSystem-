package shared.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class NotificationSubject {

    private transient List<NotificationObserver> observerCollection;

    public void registerObserver(NotificationObserver observer) {
        if (observerCollection == null) {
            observerCollection = new ArrayList<>();
        }
        if (!observerCollection.contains(observer)) {
            observerCollection.add(observer);
            System.out.println("DEBUG: Observer registered: " + observer.getClass().getName());
        }
    }

    public void unregisterObserver(NotificationObserver observer) {
        if (observerCollection != null) {
            observerCollection.remove(observer);
        }
    }

    protected void notifyObservers(String notificationData) {
        if (observerCollection != null) {
            System.out.println("DEBUG: Notifying " + observerCollection.size() + " observers");
            for (NotificationObserver observer : observerCollection) {
                observer.update(notificationData);
            }
        } else {
            System.out.println("DEBUG: No observers to notify (collection is null)");
        }
    }
}