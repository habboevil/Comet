package com.cometproject.server.game.commands.development;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class InstanceStatsCommand extends ChatCommand {

    private static final String SERVER_STATS_TITLE = "<b>Comet Server - Instance Statistics </b><br><br>";
    private static final String BUILD_STATS = "Build: ";
    private static final String PLAYERS_ONLINE = "<b>Game Statistics</b><br>Players online: ";
    private static final String ACTIVE_ROOMS = "Active rooms: ";

    @Override
    public void execute(Session client, String[] params) {
        StringBuilder message = new StringBuilder(SERVER_STATS_TITLE);
        message.append(BUILD_STATS).append(Comet.getBuild()).append("<br><br>");
        message.append(PLAYERS_ONLINE).append(PlayerManager.getInstance().size()).append("<br>");
        message.append(ACTIVE_ROOMS).append(RoomManager.getInstance().getRoomInstances().size()).append("<br><br>");
        client.send(new AlertMessageComposer(message.toString()));
    }

    @Override
    public String getPermission() {
        return "debug";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.instancestats.description", " - Build and game statistics");
    }
}
