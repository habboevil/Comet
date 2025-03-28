package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredTriggerBotReachedAvatar extends WiredTriggerItem {

    public WiredTriggerBotReachedAvatar(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static boolean executeTriggers(RoomEntity entity) {
        boolean wasExecuted = false;

        for (final WiredTriggerBotReachedAvatar floorItem : getTriggers(entity.getRoom(), WiredTriggerBotReachedAvatar.class)) {
            if (floorItem == null) continue;

            wasExecuted = floorItem.evaluate(entity, null);
        }

        return wasExecuted;
    }

    @Override
    public int getInterface() {
        return 14;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }
}
