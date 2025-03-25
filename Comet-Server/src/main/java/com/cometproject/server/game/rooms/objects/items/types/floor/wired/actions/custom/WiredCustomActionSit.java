package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;

import static com.cometproject.api.game.rooms.entities.RoomEntityStatus.LAY;
import static com.cometproject.api.game.rooms.entities.RoomEntityStatus.SIT;

public class WiredCustomActionSit extends WiredActionItem {
    public WiredCustomActionSit(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public void onEventComplete(WiredItemEvent e) {
        final PlayerEntity playerEntity = (PlayerEntity) e.entity;
        if (playerEntity.hasStatus(SIT) && playerEntity.hasStatus(LAY)) {
            playerEntity.removeStatus(LAY);
            playerEntity.removeStatus(SIT);
        }

        if (playerEntity.hasStatus(SIT)) {
            playerEntity.removeStatus(SIT);
            playerEntity.markNeedsUpdate();
        } else {
            playerEntity.sit(0.5, playerEntity.getBodyRotation());
        }
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
