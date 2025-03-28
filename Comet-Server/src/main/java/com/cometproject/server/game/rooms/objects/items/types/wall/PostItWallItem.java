package com.cometproject.server.game.rooms.objects.items.types.wall;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;


public class PostItWallItem extends RoomItemWall {
    private String colour;
    private String message;

    public PostItWallItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        if (this.isValidData(roomItemData.getData()))
            this.setExtraData(roomItemData.getData());
        else
            this.setExtraData("FFFF33 ");
    }

    @Override
    public String getState() {
        return colour;
    }

    public void setExtraData(String extraData) {
        final String[] data = extraData.split(" ");
        final String colour = data[0];

        if (!this.isValidColour(colour)) {
            return;
        }

        this.getItemData().setData(extraData);

        this.colour = colour;
        this.message = StringUtils.join(Arrays.copyOfRange(data, 1, data.length), " ");
    }

    private boolean isValidColour(String colour) {
        switch (colour) {
            default:
                return false;

            case "FFFF33":
            case "FF9CFF":
            case "9CCEFF":
            case "9CFF9C":
            case "F5AD42":
            case "CFCFCF":
            case "DBC4A2":
            case "EB4949":
                return true;
        }
    }

    private boolean isValidData(String data) {
        return data.contains(" ");
    }

    public String getColour() {
        return colour;
    }

    public String getMessage() {
        return message;
    }
}
