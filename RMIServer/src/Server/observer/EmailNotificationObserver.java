package Server.observer;

import shared.observer.NotificationObserver;

public class EmailNotificationObserver implements NotificationObserver {

    @Override
    public void update(String notificationData) {
        System.out.println("📧 Email Notification: " + notificationData);
        // In a real system, email sending logic would go here
    }
}
