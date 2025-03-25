package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class MeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        PlayerEntity playerEntity = client.getPlayer().getEntity();
        Room room = playerEntity.getRoom();

        if (params != null && params.length > 0) {
            String message = "*" + this.merge(params) + "*";
            room.getEntities().broadcastMessage(new TalkMessageComposer(playerEntity.getId(), message, ChatEmotion.SMILE, CHAT_MESSAGE_PARAMETER));
        } else {
            sendNotif(Locale.getOrDefault("command.me.error", "Oops!, We have received invalid input!"), client);
        }
    }

    @Override
    public String getPermission() {
        return "me_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.me.description");
    }
    private static final int CHAT_MESSAGE_PARAMETER = 34;
}
