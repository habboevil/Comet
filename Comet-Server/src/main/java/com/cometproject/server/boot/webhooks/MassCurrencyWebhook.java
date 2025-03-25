package com.cometproject.server.boot.webhooks;

import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class MassCurrencyWebhook {
    public static void send(String staffUsername, int quantity, String coinName) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + "** send **" + quantity + " " + coinName + "** for **all** online users.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.masscurrency.title", "A new amount has been sent to everyone."),
                "currency", fields);
    }
}
