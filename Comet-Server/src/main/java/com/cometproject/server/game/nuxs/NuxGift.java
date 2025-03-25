package com.cometproject.server.game.nuxs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NuxGift {
    private final int id;
    private final int pageType;
    private final RewardType type;
    private final String name;
    private final String icon;
    private final String productData;
    private final List<String> data = new ArrayList<>();

    public NuxGift(int id, String type, int pageType, String icon, String name, String productData, String data) {
        this.id = id;

        switch (type.toLowerCase()) {
            case "item" -> this.type = RewardType.ITEM;
            case "diamonds" -> this.type = RewardType.DIAMONDS;
            case "seasonal" -> this.type = RewardType.SEASONAL;
            case "reward1" -> this.type = RewardType.REWARD1;
            case "reward2" -> this.type = RewardType.REWARD2;
            case "reward3" -> this.type = RewardType.REWARD3;
            case "rock" -> this.type = RewardType.ROCK;
            case "paper" -> this.type = RewardType.PAPER;
            case "scissors" -> this.type = RewardType.SCISSORS;
            default -> this.type = RewardType.BADGE;
        }

        this.pageType = pageType;
        this.icon = icon;
        this.name = name;
        this.productData = productData;

        Collections.addAll(this.data, data.split(","));
    }

    public int getId() {
        return this.id;
    }

    public int getPageType() {
        return this.pageType;
    }

    public RewardType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public String getProductData() {
        return this.productData;
    }

    public List<String> getData() {
        return this.data;
    }

    public enum RewardType {
        ITEM,
        DIAMONDS,
        SEASONAL,
        BADGE,
        REWARD1,
        REWARD2,
        REWARD3,
        ROCK,
        PAPER,
        SCISSORS
    }
}
