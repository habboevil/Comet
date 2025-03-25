package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.utilities.Position;

public interface IRoomItemData {

    long getId();

    int getItemId();

    int getOwnerId();

    void setOwnerId(int userId);

    String getOwnerName();

    Position getPosition();

    void setPosition(Position position);

    String getWallPosition();

    void setWallPosition(String wallPosition);

    int getRotation();

    void setRotation(int rotation);

    String getData();

    void setData(String data);

    void setData(int data);

    LimitedEditionItem getLimitedEdition();

    void decItemData(int i);

    int getIntData();
}
