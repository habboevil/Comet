package com.cometproject.server.game.commands.user;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.sessions.Session;

public class LayCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (playerEntity.hasStatus(RoomEntityStatus.SIT)) {
            sendNotif(Locale.getOrDefault("command.lay.error_sit", "Oops!, you are not allowed to use the command here, as you are already sitting"), client);
        } else if (playerEntity.getCurrentEffect() != null) {
            sendNotif(Locale.getOrDefault("command.lay.error", "Oops!, you are not allowed to right now as there is a effect active"), client);
        } else {
            playerEntity.removeStatus(RoomEntityStatus.LAY);
            playerEntity.addStatus(RoomEntityStatus.LAY, "0.5");
            playerEntity.markNeedsUpdate();
        }
        isExecuted(client);
    }

    @Override
    public String getPermission() {
        return "lay_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.lay.description", "Lie in a room");
    }

    @Override
    public boolean canDisable() { return true; }
}
