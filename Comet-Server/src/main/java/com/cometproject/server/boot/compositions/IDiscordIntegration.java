package com.cometproject.server.boot.compositions;

import com.cometproject.api.game.players.data.IPlayerData;

import java.util.Map;

public interface IDiscordIntegration {
    void initialize();

    void sendWebhookFromName(String title, String webhookName, Map<String, String> fields);

    String getAvatar();

    void setAvatar(String avatar);

    void setNameLink(String nameLink);

    String getDiscordNameLink();

    String getName();

    void setName(String name);

    void registerWebhook(String name, String endpoint);

    void sendCommandWebhook(String username, String command, String message);

    void sendWebhookFromNameWithThumbnail(String title, String webhookName, Map<String, String> fields, String thumbnailUrl);
}
