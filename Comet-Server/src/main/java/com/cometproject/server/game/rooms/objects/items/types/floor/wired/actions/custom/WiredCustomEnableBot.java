package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang3.StringUtils;

public class WiredCustomEnableBot extends WiredActionItem {

    public WiredCustomEnableBot(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 27;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (!this.getWiredData().getText().contains("\t")) {
            return;
        }

        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        final String[] talkData = this.getWiredData().getText().split("\t");

        if (talkData.length != 2) {
            return;
        }

        final String botName = talkData[0];
        String message = talkData[1];

        if (botName.isEmpty() || message.isEmpty()) {
            return;
        }

        message = message.replace("<", "").replace(">", "");

        final BotEntity botEntity = this.getRoom().getBots().getBotByName(botName);

        if (botEntity != null) {
            if (StringUtils.isNumeric(message)) {
                botEntity.applyEffect(new PlayerEffect(Integer.parseInt(message), 0));
            }
        }
    }
}