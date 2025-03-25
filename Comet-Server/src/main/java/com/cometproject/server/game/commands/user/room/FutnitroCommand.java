package com.cometproject.server.game.commands.user.room;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.RoomProcessingType;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomReloadListener;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class FutnitroCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (client.getPlayer().getEntity() != null && client.getPlayer().getEntity().getRoom() != null) {
            if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
                sendNotif(Locale.getOrDefault("command.need.rights", "You need rights to use this command!"), client);
                return;
            }

            if (params.length > 0) {
                if (params[0].equals("?") || params[0].equals("list")) {
                    sendAlert("Types of nitro available: \n" +
                            "\n\n- 0/default: " + RoomProcessingType.DEFAULT.getDescription() + "" +
                            "\n\n- 1/random: " + RoomProcessingType.RANDOM.getDescription() +
                            "\n\n- 2/old: " + RoomProcessingType.CLICK.getDescription(), client);
                    return;
                }


            } else {
                sendWhisper("Use :futnitro ? to open the list of pathfinders.", client);
                return;
            }

            RoomProcessingType type = RoomProcessingType.parse(params[0]);


            sendNotif(Locale.getOrDefault("command.pathfinder.modified", "Pathfinder modified for " + type.getName()), client);

            client.getPlayer().getEntity().getRoom().getData().setRoomProcessType(type);
            GameContext.getCurrent().getRoomService().saveRoomData(client.getPlayer().getEntity().getRoom().getData());


            final RoomReloadListener reloadListener = new RoomReloadListener(client.getPlayer().getEntity().getRoom(), (players, newRoom) -> {
                for (final Player player : players) {
                    if (player.getEntity() != null) {
                        sendNotif(Locale.getOrDefault("command.pathfinder.changed", "Pathfinder changed to" + type.getName()), player.getSession());
                        player.getSession().send(new RoomForwardMessageComposer(newRoom.getId()));
                    }
                }
            });

            RoomManager.getInstance().addReloadListener(client.getPlayer().getEntity().getRoom().getId(), reloadListener);


            client.getPlayer().getEntity().getRoom().reload();


        }
    }

    @Override
    public String getPermission() {
        return "commands_command";
    }


    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.pathfinder.description", "Change the pathfinder for the room");
    }
}
