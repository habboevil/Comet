package com.cometproject.server.game.rooms.objects;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.types.Room;

public abstract class BigRoomFloorObject extends RoomObject {
    private final long id;
    private final int virtualId;

    public BigRoomFloorObject(long id, Position position, Room room) {
        super(position, room);

        this.id = id;
        this.virtualId = ItemManager.getInstance().getItemVirtualId(id);
    }

    public BigRoomFloorObject(int id, int userId, PlayerItem item, String extraData, int limitedStack, int limitedSells) {
        super(id, userId, item, extraData, limitedStack, limitedSells);

        this.id = id;
        this.virtualId = ItemManager.getInstance().getItemVirtualId(id);
    }

    public long getId() {
        return id;
    }

    public int getVirtualId() {
        return virtualId;
    }
}
