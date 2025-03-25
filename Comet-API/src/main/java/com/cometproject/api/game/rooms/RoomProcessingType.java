package com.cometproject.api.game.rooms;

public enum RoomProcessingType {
    DEFAULT(0, "default", "Default pathfinder (in order of entry)"),
    RANDOM(1, "random", "Random pathfinder (for event uses)"),
    CLICK(2, "soccer", "Soccer pathfinder (old 2013 football)"),
    ;

    private final String description;
    private final byte key;
    private final String name;

    RoomProcessingType(int key, String name, String description) {
        this.key = (byte) key;
        this.name = name;
        this.description = description;
    }

    public static RoomProcessingType parse(String str) {
        return switch (str.toLowerCase()) {
            case "random", "1" -> RoomProcessingType.RANDOM;
            case "click", "soccer", "2" -> RoomProcessingType.CLICK;
            default -> RoomProcessingType.DEFAULT;
        };
    }

    public String getDescription() {
        return description;
    }

    public byte getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
