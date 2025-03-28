package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.webhooks.PunishmentWebhook;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class UnBanCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        final String username = params[0];
        final int playerId = PlayerDao.getIdByUsername(username);

        if (BanManager.getInstance().unBan(playerId + "")) {
            sendNotif(Locale.getOrDefault("command.unban.success", "You have successfully unbanned %s!")
                    .replace("%s", username), client);

            PunishmentWebhook.sendUnBan(client.getPlayer().getData().getUsername(), username);
        } else {
            sendNotif(Locale.getOrDefault("command.unban.notbanned", "Oops! Perhaps this user has not been banned or has the machine banned."), client);
        }

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "-c, -d Removed the user's ban."
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", username);
    }

    @Override
    public String getPermission() {
        return "unban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.unban", "(user)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.unban.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
