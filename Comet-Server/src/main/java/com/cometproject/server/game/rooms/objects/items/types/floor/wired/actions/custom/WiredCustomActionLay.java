package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;

public class WiredCustomActionLay extends WiredActionItem {
    public WiredCustomActionLay(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public void onEventComplete(WiredItemEvent e) {
        final PlayerEntity playerEntity = (PlayerEntity) e.entity;

        if (playerEntity.hasStatus(RoomEntityStatus.LAY)) {
            playerEntity.removeStatus(RoomEntityStatus.LAY);
        } else if (playerEntity.hasStatus(RoomEntityStatus.SIT)) {
            playerEntity.removeStatus(RoomEntityStatus.SIT);
            playerEntity.addStatus(RoomEntityStatus.LAY, "0.5");
        } else {
            playerEntity.addStatus(RoomEntityStatus.LAY, "0.5");
        }

        playerEntity.markNeedsUpdate();
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
