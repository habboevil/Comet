package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.webhooks.PunishmentWebhook;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class IpBanCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        final String username = params[0];
        final int length = StringUtils.isNumeric(params[1]) ? Integer.parseInt(params[1]) : 0;

        final Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable() || user.getPlayer().getPermissions().getRank().getId() >= client.getPlayer().getPermissions().getRank().getId()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You cannot ban this user!"), client);
            return;
        }

        final long expire = Comet.getTime() + (length * 3600L);

        final String ipAddress = user.getIpAddress();

        if (BanManager.getInstance().hasBan(ipAddress, BanType.IP)) {
            sendNotif("IP: " + ipAddress + " already banned from the hotel.", client);
            return;
        }

        BanManager.getInstance().banPlayer(BanType.IP, user.getIpAddress(), user.getPlayer().getEntity().getUsername(), length, expire, params.length > 2 ? this.merge(params, 2) : "", client.getPlayer().getId(), client.getPlayer().getEntity().getUsername(), (int) Comet.getTime(), user.getPlayer().getId());

        sendNotif("User banned along with IP address. (IP: " + ipAddress + ")", client);

        final List<Integer> playerIds = PlayerManager.getInstance().getPlayerIdsByIpAddress(ipAddress);

        for (final int playerId : playerIds) {
            Session player = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (player != null) {
                player.disconnect("banned");
            }
        }

        playerIds.clear();

        PunishmentWebhook.sendIpBan(client.getPlayer().getData().getUsername(), username, params.length > 2 ? this.merge(params, 2) : "Reason not mentioned", length);

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "-c banned by IP by -d"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", username);
    }

    @Override
    public String getPermission() {
        return "ipban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.ban", "(user) (time) (reason)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.ipban.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
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
