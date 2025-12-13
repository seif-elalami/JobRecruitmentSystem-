package Server.observer;

import shared.observer.NotificationObserver;
import Server.database.NotificationDAO;

public class SystemNotificationObserver implements NotificationObserver {

    private String recipientId;

    public SystemNotificationObserver(String recipientId) {
        this.recipientId = recipientId;
    }

    public SystemNotificationObserver() {
        this.recipientId = null; 
    }

    @Override
    public void update(String notificationData) {
        System.out.println("🔔 System Notification: " + notificationData);
        
        if (recipientId != null) {
            NotificationDAO.getInstance().save(recipientId, notificationData);
        } else {
             System.out.println("⚠️ Warning: Notification not saved (No recipient ID)");
        }
    }
}
