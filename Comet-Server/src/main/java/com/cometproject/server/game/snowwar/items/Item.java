package com.cometproject.server.game.snowwar.items;

/**
 * Created by SpreedBlood on 2017-12-22.
 */

public abstract class Item {
    public static final int UPDATE = 1;
    public static final int MOVE = 2;
    public static final int INSERT = 3;
    public static final int DELETE = 4;

    public int refId;
    public int itemId;
    public BaseItem baseItem;
    public ExtraDataBase extraData;
    private int mysqlAction;

    @Override
    public int hashCode() {
        return itemId;
    }
}
