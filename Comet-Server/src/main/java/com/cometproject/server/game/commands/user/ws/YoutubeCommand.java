package com.cometproject.server.game.commands.user.ws;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.flash_external_interface_protocol.outgoing.OutgoingExternalInterfaceMessage;
import com.cometproject.server.network.flash_external_interface_protocol.outgoing.common.YoutubeTVComposer;
import com.cometproject.server.network.flash_external_interface_protocol.utils.RegexUtility;
import com.cometproject.server.network.messages.outgoing.misc.JavascriptCallbackMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class YoutubeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            return;
        }
        Room room = client.getPlayer().getEntity().getRoom();

        if (room != null && (room.getRights().hasRights(client.getPlayer().getId()) || client.getPlayer().getPermissions().getRank().roomFullControl() || client.getPlayer().getData().getRank() > 1)) {
            final String videoId = RegexUtility.getYouTubeId(params[0]);

            OutgoingExternalInterfaceMessage msg = new YoutubeTVComposer(videoId, 0);
            room.getEntities().broadcastMessage(new JavascriptCallbackMessageComposer(msg));
            room.getEntities().broadcastMessage(new NotificationMessageComposer("youtube", Locale.getOrDefault("command.youtube.notification", "%username% has started a YouTube Video!").replace("%username%", client.getPlayer().getEntity().getUsername())));
        } else {
            sendWhisper(Locale.getOrDefault("command.need.rights", "You have no rights to use this command in this room."), client);
        }
    }

    @Override
    public String getPermission() {
        return "youtube_command";
    }

    @Override
    public String getParameter() {
        return "(Youtube ID)";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.youtube.description");
    }
}