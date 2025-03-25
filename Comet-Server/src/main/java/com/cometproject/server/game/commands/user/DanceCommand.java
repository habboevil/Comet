package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class DanceCommand extends ChatCommand {
    private static final int MIN_DANCE_ID = 1;
    private static final int MAX_DANCE_ID = 4;
    private static final String INVALID_DANCE_RANGE_MESSAGE = "You must put a valid dance number between %d and %d.";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif("You must put the desired dance number.", client);
            return;
        }

        try {
            final int danceId = Integer.parseInt(params[0]);

            if (danceId < MIN_DANCE_ID || danceId > MAX_DANCE_ID) {
                sendNotif(String.format(INVALID_DANCE_RANGE_MESSAGE, MIN_DANCE_ID, MAX_DANCE_ID), client);
                return;
            }

            final PlayerEntity entity = client.getPlayer().getEntity();

            setDance(entity, danceId);
            broadcastDance(entity);

        } catch (NumberFormatException e) {
            sendNotif("You must put a valid integer as the dance number!", client);
        }
    }

    private void setDance(PlayerEntity entity, int danceId) {
        entity.setDanceId(danceId);
    }

    private void broadcastDance(PlayerEntity entity) {
        entity.getRoom().getEntities().broadcastMessage(new DanceMessageComposer(entity.getId(), entity.getDanceId()));
    }

    @Override
    public String getPermission() {
        return "dance_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.dance.param", "(Number from 1 to 4)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.dance.description", "");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
