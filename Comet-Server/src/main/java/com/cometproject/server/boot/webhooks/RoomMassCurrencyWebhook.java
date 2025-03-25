package com.cometproject.server.boot.webhooks;

import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class RoomMassCurrencyWebhook {
    public static void send(String staffUsername, int quantity, String coinName, int roomId) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + " send:" + quantity + " " + coinName + " for ALL room users [Room ID: " + roomId + "].");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.masscurrency.title", "A new amount was sent to everyone in the room"),
                "currency", fields);
    }
}
