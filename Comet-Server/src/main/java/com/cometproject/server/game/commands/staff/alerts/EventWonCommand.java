package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.network.sessions.Session;

public class EventWonCommand extends NotificationCommand {
    private String logDesc = "";

    private static final String IMAGE_KEY = "command.eventwon.image";
    private static final String MESSAGE_KEY = "command.eventwon.message";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        this.globalNotification(Locale.get(IMAGE_KEY),
                Locale.get(MESSAGE_KEY).replace("%user%", params[0]), client);

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = String.format("%s executed event win command for user '%s'",
                client.getPlayer().getData().getUsername(),
                params[0]);
    }

    @Override
    public String getPermission() {
        return "eventwon_command";
    }

    @Override
    public String getParameter() {
        return "(user)";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.eventwon.description");
    }
}