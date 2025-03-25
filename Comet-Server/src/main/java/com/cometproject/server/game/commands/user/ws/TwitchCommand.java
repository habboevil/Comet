package com.cometproject.server.game.commands.user.ws;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.flash_external_interface_protocol.outgoing.OutgoingExternalInterfaceMessage;
import com.cometproject.server.network.flash_external_interface_protocol.outgoing.common.TwitchVideoComposer;
import com.cometproject.server.network.messages.outgoing.misc.JavascriptCallbackMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class TwitchCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        Room room = client.getPlayer().getEntity().getRoom();

        if (room != null && (room.getRights().hasRights(client.getPlayer().getId()) || client.getPlayer().getPermissions().getRank().roomFullControl() || client.getPlayer().getData().getRank() > 1)) {
            final String channel = params[0];

            OutgoingExternalInterfaceMessage msg = new TwitchVideoComposer(channel);
            room.getEntities().broadcastMessage(new JavascriptCallbackMessageComposer(msg));
            room.getEntities().broadcastMessage(new NotificationMessageComposer("twitch", Locale.getOrDefault("command.twitch.notification", "%username% has started a Twitch Video!").replace("%username%", client.getPlayer().getEntity().getUsername())));
        } else {
            sendWhisper(Locale.getOrDefault("command.need.rights", "You have no rights to use this command in this room."), client);
        }
    }

    @Override
    public String getPermission() {
        return "twitch_command";
    }

    @Override
    public String getParameter() {
        return "(Twitch ID)";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.twitch.description");
    }
}
