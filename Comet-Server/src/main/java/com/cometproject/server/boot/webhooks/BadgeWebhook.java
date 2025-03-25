package com.cometproject.server.boot.webhooks;

import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class BadgeWebhook {
    public static void send(String staffUsername, String username, String code) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + "** sent the badge **" + code + "** for **" + username + "**.");

        DiscordIntegration.getInstance().sendWebhookFromNameWithThumbnail(
                Locale.getOrDefault("webhook.badge.send", "A new badge has been sent"),
                "badge", fields,
                "https://localhost/apifiles/badges/" + code);
    }

    public static void sendAll(String staffUsername, String code) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + "** sent the badge **" + code + "** for **all** online users.");

        DiscordIntegration.getInstance().sendWebhookFromNameWithThumbnail(
                Locale.getOrDefault("webhook.badge.mass", "A new badge has been sent to everyone"),
                "badge", fields,
                "https://localhost/apifiles/badges/" + code);
    }

    public static void sendRemoval(String staffUsername, String username, String code) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + "** remove the badge **" + code + "** for **" + username + "**.");

        DiscordIntegration.getInstance().sendWebhookFromNameWithThumbnail(
                Locale.getOrDefault("webhook.badge.remove", "An badge has been removed"),
                "badge", fields,
                "https://localhost/apifiles/badges/" + code);
    }
}
