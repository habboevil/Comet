package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class PositionCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        double x = client.getPlayer().getEntity().getPosition().getX();
        double y = client.getPlayer().getEntity().getPosition().getY();
        double z = client.getPlayer().getEntity().getPosition().getZ();
        int direction = client.getPlayer().getEntity().getBodyRotation();

        String message = String.format("X: %.2f\r\nY: %.2f\r\nZ: %.2f\r\nDirection you are looking: %d\r\n", x, y, z, direction);
        sendNotif(message, client);
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
        return Locale.getOrDefault("command.position.description", " - Check the coordinates of your position in the room.");
    }
}
