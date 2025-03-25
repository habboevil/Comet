package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class RandomizeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 2) {
            sendNotif(Locale.getOrDefault("command.randomize.none", "Between which 2 numbers do you want to randomize?"), client);
            return;
        }

        try {
            final int min = Integer.parseInt(params[0]);
            final int max = Integer.parseInt(params[1]);

            if (min > 1000 || max > 1000) {
                sendNotif(Locale.getOrDefault("command.randomize.toohigh", "The minimum or maximum number is not valid. Choose between 0 and 1000!"), client);
                return;
            } else if (min < 0 || max < 0) {
                sendNotif(Locale.getOrDefault("command.randomize.negative", "You can only use positive numbers!"), client);
                return;
            }

            final Room room = client.getPlayer().getEntity().getRoom();
            final PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(client.getPlayer().getId());

            room.getEntities().broadcastMessage(new TalkMessageComposer(playerEntity.getId(), Locale.getOrDefault("command.randomize.done", "The number chosen between %min% and %max% was : %output%").replace("%output%", "" + (int) Math.floor(Math.random() * (max - min) + min)).replace("%min%", min + "").replace("%max%", max + ""), ChatEmotion.NONE, 8));
            isExecuted(client);
        } catch (Exception e) {
            sendNotif(Locale.get("command.randomize.invalidnumbers"), client);
        }
    }

    @Override
    public String getPermission() {
        return "randomize_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.randomize", "(mín) (max)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.randomize.description", "Get random numbers");
    }
}
