package com.cometproject.server.network.battleball.incoming.queues;

import com.cometproject.api.networking.sessions.SessionManagerAccessor;
import com.cometproject.server.game.snowwar.SnowPlayerQueue;
import com.cometproject.server.network.battleball.gameserver.GameServer;
import com.cometproject.server.network.battleball.incoming.IncomingEvent;
import com.cometproject.server.network.sessions.Session;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;

public class JoinQueueSnowstormEvent extends IncomingEvent {

    @Override
    public void handle() throws SQLException, IllegalAccessException, InstantiationException, IOException, NoSuchMethodException, InvocationTargetException {

        HashMap<String, String> player = GameServer.userMap.get(this.session);
        Session client = (Session) SessionManagerAccessor.getInstance().getSessionManager().fromPlayer(Integer.parseInt(player.get("id")));

        System.out.println("JOIN QUEUE SNOWSTORM SOCKET");
        SnowPlayerQueue.addPlayerInQueue(client);
    }
}
