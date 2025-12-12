package shared.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class NotificationSubject {

    // Transient because we don't want to serialize the observers when sending Interview over RMI
    private transient List<NotificationObserver> observerCollection;

    public void registerObserver(NotificationObserver observer) {
        if (observerCollection == null) {
            observerCollection = new ArrayList<>();
        }
        if (!observerCollection.contains(observer)) {
            observerCollection.add(observer);
        }
    }

    public void unregisterObserver(NotificationObserver observer) {
        if (observerCollection != null) {
            observerCollection.remove(observer);
        }
    }

    protected void notifyObservers(String notificationData) {
        if (observerCollection != null) {
            for (NotificationObserver observer : observerCollection) {
                observer.update(notificationData);
            }
        }
    }
}
