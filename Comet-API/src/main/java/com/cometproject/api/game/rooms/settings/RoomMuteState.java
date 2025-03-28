package com.cometproject.api.game.rooms.settings;

public enum RoomMuteState {
    NONE(0),
    RIGHTS(1);

    private final int state;

    RoomMuteState(int state) {
        this.state = state;
    }

    public static RoomMuteState valueOf(int state) {
        if (state == 0) return NONE;
        else return RIGHTS;
    }

    public int getState() {
        return this.state;
    }
}
