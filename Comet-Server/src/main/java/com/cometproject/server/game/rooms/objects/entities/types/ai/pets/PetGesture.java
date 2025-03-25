package com.cometproject.server.game.rooms.objects.entities.types.ai.pets;

public enum PetGesture {
    SMILE("sml"),
    ANGRY("agr"),
    SURPRISED("srp"),
    SAD("sad"),
    PUZZLED("puz"),
    MISERABLE("mis"),
    BLINK("eyb"),
    SNIFF("snf"),
    TONGUE("tng"),
    CRAZY("crz"),
    JOY("joy"),
    HUNGRY("hng"),
    LEVEL_UP("lvl"),
    EXPERIENCE("exp"),
    ENERGY("nrg"),
    LOVE("lov"),
    WARNING("und"),
    QUESTION("que"),
    THIRSTY("thr"),
    TIRED("trd"),
    PLAYFUL("plf");

    private final String gestureKey;

    PetGesture(String key) {
        this.gestureKey = key;
    }

    public String getGestureKey() {
        return this.gestureKey;
    }
}
