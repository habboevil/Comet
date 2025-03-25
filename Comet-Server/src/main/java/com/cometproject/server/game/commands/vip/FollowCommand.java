package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.navigator.types.search.NavigatorSearchService;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class FollowCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendWhisper(Locale.getOrDefault("command.follow.none", "Who do you want to follow?"), client);
            return;
        }

        final Session leader = NetworkManager.getInstance().getSessions().getByPlayerUsername(params[0]);

        if (leader == client) {
            sendNotif(Locale.getOrDefault("command.follow.playerhimself", "You cannot follow yourself!"), client);
            return;
        }


        if (leader != null && leader.getPlayer() != null && leader.getPlayer().getEntity() != null) {
            if (!leader.getPlayer().getSettings().getAllowFollow() && !client.getPlayer().getPermissions().getRank().modTool()) {
                sendNotif(Locale.getOrDefault("command.follow.disabled", "This user has turned off being followed."), client);
                return;
            }

            if (!NavigatorSearchService.checkRoomVisibility(client.getPlayer(), leader.getPlayer().getEntity().getRoom())) {
                sendNotif(Locale.getOrDefault("command.follow.invisible_room", "This user is not in any room."), client);
                return;
            }

            isExecuted(client);
            client.send(new RoomForwardMessageComposer(leader.getPlayer().getEntity().getRoom().getId()));
        } else {
            if (leader == null || leader.getPlayer() == null)
                sendNotif(Locale.get("command.follow.online"), client);
            else
                sendNotif(Locale.get("command.follow.room"), client);
        }
    }

    @Override
    public String getPermission() {
        return "follow_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "(user)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.follow.description", "Follows a user to the room they are in");
    }
}
