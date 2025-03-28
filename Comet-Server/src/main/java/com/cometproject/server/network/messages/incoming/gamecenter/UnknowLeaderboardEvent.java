package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.gamecenter.GameCenterAchievementsConfigurationComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class UnknowLeaderboardEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int gameId = msg.readInt();

        if (gameId == 2) {
            client.send(new GameCenterAchievementsConfigurationComposer(gameId, client.getPlayer().getAchievements()));
        }
    }
}
