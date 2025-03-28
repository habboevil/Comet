package com.cometproject.api.game.groups.types;

public enum GroupType {
    REGULAR(0), EXCLUSIVE(1), PRIVATE(2);

    private final int typeId;

    GroupType(int type) {
        this.typeId = type;
    }

    public static GroupType valueOf(int typeId) {
        if (typeId == 0)
            return REGULAR;
        else if (typeId == 1)
            return EXCLUSIVE;
        else
            return PRIVATE;
    }

    public int getTypeId() {
        return this.typeId;
    }
}
