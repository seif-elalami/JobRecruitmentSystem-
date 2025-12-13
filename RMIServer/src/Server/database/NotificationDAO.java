package Server.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import shared.models.Notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationDAO {

    private static NotificationDAO instance;
    private MongoCollection<Document> notificationCollection;

    private NotificationDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        notificationCollection = database.getCollection("notifications");
    }

    public static NotificationDAO getInstance() {
        if (instance == null) {
            instance = new NotificationDAO();
        }
        return instance;
    }

    public void save(String recipientId, String message) {
        try {
            Document doc = new Document();
            doc.append("recipientId", recipientId);
            doc.append("message", message);
            doc.append("date", new Date());
            doc.append("isRead", false);

            notificationCollection.insertOne(doc);
            System.out.println("💾 Notification saved for user: " + recipientId);

        } catch (Exception e) {
            System.err.println("❌ Error saving notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Notification> getNotificationsByRecipientId(String recipientId) {
        List<Notification> notifications = new ArrayList<>();
        try {
            Document query = new Document("recipientId", recipientId);

            for (Document doc : notificationCollection.find(query).sort(new Document("date", -1))) {
                Notification notification = new Notification();
                notification.setId(doc.getObjectId("_id").toString());
                notification.setRecipientId(doc.getString("recipientId"));
                notification.setMessage(doc.getString("message"));
                notification.setDate(doc.getDate("date"));
                notification.setRead(doc.getBoolean("isRead", false));

                notifications.add(notification);
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching notifications: " + e.getMessage());
            e.printStackTrace();
        }
        return notifications;
    }
}