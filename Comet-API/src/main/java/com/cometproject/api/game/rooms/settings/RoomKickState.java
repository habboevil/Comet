package com.cometproject.api.game.rooms.settings;

public enum RoomKickState {
    EVERYONE(2),
    RIGHTS(1),
    NONE(0);

    private final int state;

    RoomKickState(int state) {
        this.state = state;
    }

    public static RoomKickState valueOf(int state) {
        if (state == 0) return NONE;
        else if (state == 1) return RIGHTS;
        else return EVERYONE;
    }

    public int getState() {
        return this.state;
    }
}
