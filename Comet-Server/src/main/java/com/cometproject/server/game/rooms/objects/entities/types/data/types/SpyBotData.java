package com.cometproject.server.game.rooms.objects.entities.types.data.types;

import com.cometproject.server.game.rooms.objects.entities.types.data.BotDataObject;

import java.util.List;

public class SpyBotData implements BotDataObject {
    private final List<String> visitors;

    public SpyBotData(List<String> visitors) {
        this.visitors = visitors;
    }

    public List<String> getVisitors() {
        return visitors;
    }

    public void addIfAbsent(String username) {
        if (this.visitors.contains(username)) return;

        this.visitors.add(username);
    }
}
