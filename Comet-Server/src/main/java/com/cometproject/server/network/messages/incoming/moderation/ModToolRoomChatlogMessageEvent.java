package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolRoomChatlogMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ModToolRoomChatlogMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int context = msg.readInt();
        int roomId = msg.readInt();

        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            client.disconnect();
            return;
        }

        if (!LogManager.ENABLED) {
            client.send(new AdvancedAlertMessageComposer("Notice", "Logging is not currently enabled, please contact your system administrator to enable it."));
            return;
        }

        IRoomData roomData = GameContext.getCurrent().getRoomService().getRoomData(roomId);

        if (roomData != null) {
            client.send(new ModToolRoomChatlogMessageComposer(roomData.getId(), roomData.getName(), LogQueries.getChatlogsForRoom(roomData.getId())));

            // Logs in to client bubble
            for (ISession player : NetworkManager.getInstance().getSessions().getSessions().values()) {
                if (player.getPlayer().getLogsClientStaff() == true) {
                    final MotdNotificationMessageComposer msgg = new MotdNotificationMessageComposer(Locale.getOrDefault("message.staffalert", "Staff Alert: ") + " " + client.getPlayer().getData().getUsername() + " started reading the room's chatlog => " + roomData.getName() + " whose owner is " + roomData.getOwner() + "\n\n- " + client.getPlayer().getData().getUsername());
                    if (client.getPlayer().getEntity() != null) {
                        player.send(new TalkMessageComposer(player.getPlayer().getData().getId(), "<b>" + client.getPlayer().getData().getUsername() + "</b>: " + " started reading the room's chatlog => " + roomData.getName() + " whose owner is " + roomData.getOwner(), ChatEmotion.NONE, 34));
                    } else
                        player.send(msgg);
                }
            }

        } else {
            client.send(new AdvancedAlertMessageComposer("Notice", "There seems to be an issue with fetching the logs for this room (ID: " + roomId + ", Context: " + context + ")"));
        }
    }
}
