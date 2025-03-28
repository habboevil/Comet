package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.utilities.Position;

public class RoomItemData implements IRoomItemData {
    private final long id;
    private final int itemId;
    private final String ownerName;
    private final LimitedEditionItem limitedEdition;
    private int ownerId;
    private String data;
    private int rotation;
    private Position floorPosition;
    private String wallPosition;

    public RoomItemData(long id, int itemId, int ownerId, String ownerName, Position position, int rotation, String data, String wallPosition, LimitedEditionItem limitedEditionItem) {
        this.id = id;
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.data = data;

        this.rotation = rotation;
        this.floorPosition = position;

        this.wallPosition = wallPosition;

        this.limitedEdition = limitedEditionItem;
    }

    public long getId() {
        return id;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public void setOwnerId(int userId) {
        this.ownerId = userId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public Position getPosition() {
        return this.floorPosition;
    }

    @Override
    public void setPosition(Position position) {
        this.floorPosition = position;
    }

    @Override
    public String getWallPosition() {
        return this.wallPosition;
    }

    @Override
    public void setWallPosition(String wallPosition) {
        this.wallPosition = wallPosition;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setData(int data) {
        this.data = "" + data;
    }

    @Override
    public int getIntData() {
        try {
            return Integer.parseInt(this.data);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void decItemData(int i) {
        int data = Integer.parseInt(this.data);

        data -= i;

        this.data = "" + data;
    }

    @Override
    public LimitedEditionItem getLimitedEdition() {
        return limitedEdition;
    }
}
