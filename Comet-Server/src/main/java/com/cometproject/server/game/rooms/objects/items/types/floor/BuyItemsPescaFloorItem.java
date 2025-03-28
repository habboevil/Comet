package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class BuyItemsPescaFloorItem extends RoomItemFloor {

    public BuyItemsPescaFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        PlayerEntity playerEntity = ((PlayerEntity) entity);
        RoomItemFloor floorItem = playerEntity.getTile().getTopItemInstance();

        String botName = Locale.getOrDefault("bot.fishing.name", "Fisherman");
        final BotEntity botEntity = this.getRoom().getBots().getBotByName(botName);
        if (botEntity != null) {
            if (floorItem instanceof BuyItemsPescaFloorItem) {
                botEntity.say("Hola " + playerEntity.getUsername() + " My name is " + botEntity.getUsername() + " and I am in charge of selling you objects and guiding you with fishing");
                //WebSocketSessionManager.getInstance().sendMessage(new BuyCaneWebPacket("sendCaneFishing", playerEntity.getPlayer().getData().getFigure(), playerEntity.getPlayer().getData().getUsername()));
            }
        }
    }
}

