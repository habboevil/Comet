package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class AlertCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.alert.none", "Who do you want to send an alert to?"), client);
            return;
        }

        String targetUsername = params[0];
        String message = merge(params, 1);

        Session targetSession = NetworkManager.getInstance().getSessions().getByPlayerUsername(targetUsername);

        if (targetSession == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        targetSession.send(new AlertMessageComposer(message));

        if (CometExternalSettings.enableStaffMessengerLogs) {
            String logDesc = String.format("%s sent an alert to %s. [%s]",
                    client.getPlayer().getData().getUsername(),
                    targetUsername,
                    message);
            String logDescField = logDesc;
        }
    }

    @Override
    public String getPermission() {
        return "alert_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.name" + " " + "command.parameter.message", "(user) (message)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.alert.description");
    }
}
