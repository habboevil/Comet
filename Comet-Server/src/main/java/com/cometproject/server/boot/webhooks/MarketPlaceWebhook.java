package com.cometproject.server.boot.webhooks;

import com.cometproject.api.game.players.data.IPlayerData;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.boot.DiscordIntegration;
import com.cometproject.server.config.Locale;

import java.util.HashMap;
import java.util.Map;

public class MarketPlaceWebhook {
    public static void send(PlayerItem item, IPlayerData buyerPlayer, IPlayerData sellerPlayer, int price) {
        final Map<String, String> fields = new HashMap<>();

        fields.put(
                "\u200B",
                "**Item:**\\n"
                        + item.getDefinition().getPublicName() +
                        "\\n**Value:**\\n"
                        + price + " diamonds" +
                        "\\n**Buyer:**\\n"
                        + buyerPlayer.getUsername() +
                        "\\n**Seller:**\\n"
                        + sellerPlayer.getUsername()
        );

        DiscordIntegration.getInstance().sendWebhookFromNameWithThumbnail(
                Locale.getOrDefault("webhook.marketplace.title", "New item purchased"),
                "marketplace",
                fields,
                "https://localhost/apifiles/furnitures/" + item.getDefinition().getItemName().replace("*", "_"));
    }
}
