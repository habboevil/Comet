package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometThreadManager;

import java.util.concurrent.TimeUnit;


public class WiredCustomFreezeUnfreeze extends WiredActionItem {

    public WiredCustomFreezeUnfreeze(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 1;
    }

    @Override
    public boolean usesDelay() {
        return false;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        if (playerEntity.getPlayer() == null || playerEntity.getPlayer().getSession() == null) {
            return;
        }
        if (!playerEntity.getFreeze()) playerEntity.cancelWalk();
        freeze(playerEntity);

        CometThreadManager.getInstance().executeSchedule(() -> freeze(playerEntity), this.getWiredData().getDelay() * 500, TimeUnit.MILLISECONDS);
    }

    private void freeze(PlayerEntity playerEntity) {

        playerEntity.setCanWalk(!playerEntity.canWalk());
        playerEntity.setIsFreeze(!playerEntity.getFreeze());
    }
}
