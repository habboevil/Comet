package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class NotificationLookCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        NetworkManager.getInstance().getSessions().broadcast(new NotificationMessageComposer("usr/look/" + client.getPlayer().getData().getFigure(), this.merge(params)));
    }

    @Override
    public String getPermission() {
        return "notificationlook_command";
    }

    @Override
    public String getParameter() {
        return "(message)";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.notificationlook.description", "Sends a singing message with your look.");
    }
}
