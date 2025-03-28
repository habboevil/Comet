package com.cometproject.server.network.messages.incoming.gamecenter;

import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.gamecenter.*;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetWeeklyLeaderboardEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int gameId = msg.readInt();
        int arg_2 = msg.readInt();

        if (gameId == 2) { // Snowstorm
            client.send(new LastWeekLeaderboardComposer(gameId));
            client.send(new WeeklyLeaderboardComposer(gameId, arg_2, 0, 0, 0, 0));
            client.send(new LuckyLosersComposer(gameId));
        }
    }
}
