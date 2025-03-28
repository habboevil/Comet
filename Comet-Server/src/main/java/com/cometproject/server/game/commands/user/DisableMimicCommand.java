package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class DisableMimicCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] message) {
        final PlayerSettings playerSettings = client.getPlayer().getSettings();

        final boolean allowMimic = !playerSettings.getAllowMimic();

        playerSettings.setAllowMimic(allowMimic);

        sendNotif(Locale.getOrDefault("command.disablemimic." + (allowMimic ? "deactivated" : "activated"), "Already " + (allowMimic ? "no" : "") + " can copy your look!"), client);
        PlayerDao.updateAllowMimic(playerSettings.getAllowMimic(), client.getPlayer().getId());
    }

    @Override
    public String getPermission() {
        return "disable_mimic_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.disablemimic.description", "Whether or not to allow them to copy your look.");
    }
}