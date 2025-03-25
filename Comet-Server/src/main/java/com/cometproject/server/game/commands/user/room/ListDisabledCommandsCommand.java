package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.Collections;
import java.util.List;

public class ListDisabledCommandsCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        final Room room = client.getPlayer().getEntity().getRoom();

        final List<String> listCommands = Collections.singletonList(room.getData().getDisabledCommands().toString());

        client.send(new MotdNotificationMessageComposer("This is the list of disabled commands in your room: \n\n" + listCommands + "\n"));

    }

    @Override
    public String getPermission() {
        return "disabledcommands_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.disabledcommands.description", "View the list of disabled commands in your room.");
    }
}
