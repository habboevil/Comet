package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.messenger.InstantChatMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HotelAlertCommand extends ChatCommand {
    public void execute(Session client, String[] message) {
        if (message.length == 0) {
            return;
        }

        final String realMessage = merge(message);
        final String alertMessage = String.format("%s<br><br>- %s", realMessage, client.getPlayer().getData().getUsername());

        NetworkManager.getInstance().getSessions().broadcastHotelAlert(new AlertMessageComposer(alertMessage));

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        String logDesc = String.format("%s sent an alert to the hotel. [%s]",
                client.getPlayer().getData().getUsername(),
                this.merge(message));

        for (final Session player : ModerationManager.getInstance().getLogChatUsers()) {
            player.send(new InstantChatMessageComposer(logDesc, Integer.MAX_VALUE - 1));
        }
    }

    public boolean isAsync() {
        return true;
    }

    public String getPermission() {
        return "hotelalert_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.hotel_alert.parameters", "");
    }

    public String getDescription() {
        return Locale.get("command.hotel_alert.description");
    }
}
