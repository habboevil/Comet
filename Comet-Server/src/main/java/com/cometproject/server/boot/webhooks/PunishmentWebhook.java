package com.cometproject.server.boot.webhooks;

import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class PunishmentWebhook {
    public static void sendBan(String staffUsername, String userBannedName, String reason, int hours) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + "banned a user for:" + hours + " hours for user:" + userBannedName + " reason:" + reason + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.ban", "New registered ban"),
                "punishment", fields);
    }

    public static void sendIpBan(String staffUsername, String userBannedName, String reason, int hours) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " performed IP ban for:" + hours + " user:" + userBannedName + " reason:" + reason + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.ipban", "New registered ipban"),
                "punishment", fields);
    }

    public static void sendMachineBan(String staffUsername, String userBannedName, String reason, int hours) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " performed a machineban for:" + hours + " hours for user:" + userBannedName + " reason:" + reason + "**.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.machineban", "New registered machineban"),
                "punishment", fields);
    }

    public static void sendSuperBan(String staffUsername, String userBannedName) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " placed a Permanent Ban for user:" + userBannedName + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.superban", "New registered super ban"),
                "punishment", fields);
    }

    public static void sendTradeBan(String staffUsername, String userBannedName, String reason, int hours) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " performed a Trade Ban for user:" + userBannedName + " for " + hours + " hours the reason:" + reason + "**.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.tradeban", "New registered trade ban"),
                "punishment", fields);
    }

    public static void sendUnBan(String staffUsername, String userUnBannedName) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " performed an Unban for the user:" + userUnBannedName + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.unban", "New registered unban"),
                "punishment", fields);
    }

    public static void sendDisconnect(String staffUsername, String userUnBannedName) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " just Disconnected the user:" + userUnBannedName + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.disconnect", "New registered disconnect"),
                "punishment", fields);
    }

    public static void sendMute(String staffUsername, String username, int minutes) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "** " + staffUsername + " just muted for " + minutes + " minutes the user:" + username + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.mute", "New registered muted"),
                "punishment", fields);
    }

    public static void sendUnMute(String staffUsername, String username) {
        final Map<String, String> fields = new HashMap<>();

        fields.put("\u200B", "**" + staffUsername + " un-muted the user:" + username + " **.");

        DiscordIntegration.getInstance().sendWebhookFromName(
                Locale.getOrDefault("webhook.punishment.unmute", "New registered un-muted"),
                "punishment", fields);
    }
}
