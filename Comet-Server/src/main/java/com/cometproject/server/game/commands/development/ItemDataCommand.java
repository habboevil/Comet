package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ItemDataCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (playerEntity == null) {
            return; // Ensure playerEntity is not null
        }

        RoomItemFloor floorItem = playerEntity.getTile().getTopItemInstance();

        if (floorItem != null) {
            String message = String.format("Item Data (%s)\n\nRotation: %s\nType: %s\nPosition: %s",
                    floorItem.getId(), floorItem.getRotation(), floorItem.getClass().getSimpleName(), floorItem.getPosition());
            client.send(new MotdNotificationMessageComposer(message));
        }
    }

    @Override
    public String getPermission() {
        return "debug";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.position.description", " - Gets the data for the item you're currently standing on.");
    }
}
