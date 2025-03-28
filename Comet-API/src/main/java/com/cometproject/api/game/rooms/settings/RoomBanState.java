package com.cometproject.api.game.rooms.settings;

public enum RoomBanState {
    NONE(0),
    RIGHTS(1);

    private final int state;

    RoomBanState(int state) {
        this.state = state;
    }

    public static RoomBanState valueOf(int state) {
        if (state == 0) return NONE;
        else return RIGHTS;
    }

    public int getState() {
        return this.state;
    }
}
