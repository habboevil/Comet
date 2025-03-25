package com.cometproject.server.game.snowwar;

/*
 * ****************
 * @author capos *
 * ****************
 */

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomQueue {
    public final Map<Integer, Session> players = new ConcurrentHashMap<>(SnowWar.MAXPLAYERS);
    public SnowWarRoom room;

    public RoomQueue(SnowWarRoom snowRoom) {
        room = snowRoom;
    }

    public void broadcast(final IMessageComposer Message) {
        for (final Session cn : players.values()) {
            cn.getChannel().writeAndFlush(Message);
        }
    }
}
