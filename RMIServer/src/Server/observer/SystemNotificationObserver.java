package Server.observer;

import shared.observer.NotificationObserver;

public class SystemNotificationObserver implements NotificationObserver {

    @Override
    public void update(String notificationData) {
        System.out.println("🔔 System Notification: " + notificationData);
        // In a real system, push notification logic would go here
    }
}
