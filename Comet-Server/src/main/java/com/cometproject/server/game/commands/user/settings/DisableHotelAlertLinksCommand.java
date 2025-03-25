package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class DisableHotelAlertLinksCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final PlayerSettings playerSettings = client.getPlayer().getSettings();

        playerSettings.setDisableHotelAlertLinks(!playerSettings.disableHotelAlertLinks());

        final String msg = playerSettings.disableHotelAlertLinks() ? "disabled" : "activated";
        sendNotif(Locale.getOrDefault("command.disablehotelalertlinks." + msg, String.format("Hotel alerts with link are now %s.", msg)), client);
        PlayerDao.updateHotelAlertsLinks(playerSettings.disableHotelAlertLinks(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "disablehotelalertlinks_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.disablehotelalertlinks.description", "Enables or disables linked hotel alerts.");
    }
}
