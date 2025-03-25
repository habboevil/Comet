package com.cometproject.server.game.commands.notifications.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Notification {
    private final String notificationName;
    private final String notificationText;
    private final NotificationType notificationType;
    private final int minRank;
    private final int cooldown;
    private final String image;

    public Notification(ResultSet data) throws SQLException {
        this.notificationName = data.getString("name");
        this.notificationText = data.getString("text");
        this.notificationType = NotificationType.valueOf(data.getString("type").toUpperCase());
        this.minRank = data.getInt("min_rank");
        this.cooldown = data.getInt("cooldown");
        this.image = data.getString("image");
    }

    public void execute(Player player) {
        long currentTime = Comet.getTime();
        long lastNotificationTime = player.getNotifCooldown();

        if ((lastNotificationTime + cooldown) >= currentTime) {
            return;
        }

        sendNotification(player, image, notificationText);
        player.setNotifCooldown((int) currentTime);
    }

    private void sendNotification(Player player, String image, String text) {
        switch (notificationType) {
            case GLOBAL ->
                    NetworkManager.getInstance().getSessions().broadcast(new NotificationMessageComposer(image, text));
            case LOCAL -> player.sendNotif(image, text);
            case ROOM ->
                    player.getEntity().getRoom().getEntities().broadcastMessage(new NotificationMessageComposer(image, text));
        }
    }

    public String getNotificationName() {
        return notificationName;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public int getMinRank() {
        return minRank;
    }

    public String getImage() {
        return image;
    }
}
