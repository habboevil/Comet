package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HomeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getSettings().getHomeRoom() > 0) {
            client.send(new RoomForwardMessageComposer(client.getPlayer().getSettings().getHomeRoom()));
            sendNotif(Locale.getOrDefault("command.home.message", "The power of the universe send you back to your home room!"), client);
        }
    }

    @Override
    public String getPermission() {
        return "home_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.home.description", "Return to your home room");
    }
}
