package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerCustomIdle extends WiredTriggerItem {
    private static final int PARAM_TICK_LENGTH = 0;

    public WiredTriggerCustomIdle(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
        this.getWiredData().getParams().putIfAbsent(PARAM_TICK_LENGTH, 30); // 15s
    }

    public static boolean executeTriggers(PlayerEntity playerEntity) {
        boolean wasExecuted = false;

        for (final WiredTriggerCustomIdle floorItem : getTriggers(playerEntity.getRoom(), WiredTriggerCustomIdle.class)) {
            if (floorItem == null) continue;

            if (playerEntity.getIdleTimeWiredWalk() >= floorItem.getTime()) {
                //entity.resetIdleTimeWiredWalk();
                wasExecuted = floorItem.evaluate(playerEntity, floorItem);
            }

            if (wasExecuted) {
                playerEntity.resetIdleTimeWiredWalk();
            }
        }

        return wasExecuted;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 3;
    }

    public int getTime() {
        return this.getWiredData().getParams().get(PARAM_TICK_LENGTH);
    }
}
