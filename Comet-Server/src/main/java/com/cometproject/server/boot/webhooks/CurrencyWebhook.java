package com.cometproject.server.boot.webhooks;

import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class CurrencyWebhook {
    public static void send(String staffUsername, String username, int quantity, String coinName) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + "** send **" + quantity + " " + coinName + "** for **" + username + "**.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.currency.title", "A new amount has been sent"),
                "currency", fields);
    }
}
