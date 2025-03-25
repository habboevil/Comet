package com.cometproject.server.game.commands.user.building;

public enum BuildingType {
    NONE(""),
    FILL("fill"),
    COPY("copy"),
    MOVE("move");

    private final String key;

    BuildingType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
