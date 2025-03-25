package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.game.commands.user.building.BuildingType;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.types.components.BuildingComponent;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.google.common.collect.Maps;

import java.util.Map;

public class PlaceItemMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) {

        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        if (!client.getPlayer().getEntity().getRoom().getBuilderComponent().isBuilder(client.getPlayer().getEntity())
                && !client.getPlayer().getEntity().getBuildingType().equals(BuildingType.NONE)) {
            return;
        }

        final String data = msg.readString();

        if (client.getPlayer().getInventory() == null) return;

        if (data.isEmpty()) return;

        final String[] parts = data.split(" ");
        final int id = Integer.parseInt(parts[0].replace("-", ""));

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            final Map<String, String> notificationParams = Maps.newHashMap();

            notificationParams.put("message", "${room.error.cant_set_not_owner}");

            client.send(new NotificationMessageComposer("furni_placement_error", notificationParams));
            return;
        }

        final BuildingComponent buildingComponent = client.getPlayer().getEntity().getRoom().getBuilderComponent();

        try {
            if (parts.length > 1 && parts[1].startsWith(":")) {
                buildingComponent.placeWallItem(client, parts, id);
            } else {
                final int x = Integer.parseInt(parts[1]), y = Integer.parseInt(parts[2]), rot = Integer.parseInt(parts[3]);

                final Long itemId = ItemManager.getInstance().getItemIdByVirtualId(id);

                if (itemId == null) {
                    return;
                }

                final PlayerItem item = client.getPlayer().getInventory().getItem(itemId);

                if (item == null) {
                    return;
                }

                if (client.getPlayer().getEntity().getBuildingType().equals(BuildingType.FILL)) {
                    switch (client.getPlayer().getEntity().getSelectionType()) {
                        case STACK -> {
                            buildingComponent.fillStack(client, x, y, rot, item);
                            return;
                        }
                        case REGION -> {
                            buildingComponent.placeFloorItem(client, item, x, y, rot);
                            return;
                        }
                    }
                }

                buildingComponent.placeFloorItem(client, item, x, y, rot);
            }
        } catch (Exception e) {
            client.getLogger().error("Error while placing item", e);
        }
    }
}
