package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.webhooks.PunishmentWebhook;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class MachineBanCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        final String username = params[0];
        int length = Integer.parseInt(params[1]);

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

        final String uniqueId = user.getUniqueId();

        if (BanManager.getInstance().hasBan(uniqueId, BanType.MACHINE)) {
            sendNotif(uniqueId + " machine ID is already banned.", client);
            return;
        }

        BanManager.getInstance().banPlayer(BanType.MACHINE, user.getUniqueId(), user.getPlayer().getEntity().getUsername(), length, expire, params.length > 2 ? this.merge(params, 2) : "", client.getPlayer().getId(), client.getPlayer().getEntity().getUsername(), (int) Comet.getTime(), user.getPlayer().getId());
        sendNotif("User machine ID is prohibited.", client);

        user.disconnect("banned");

        PunishmentWebhook.sendMachineBan(client.getPlayer().getData().getUsername(), username, params.length > 2 ? this.merge(params, 2) : "Reason not mentioned", length);

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "%s, '%u' banned user and machine ID."
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%u", username);
    }

    @Override
    public String getPermission() {
        return "machineban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.ban", "(user) (time) (reason)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.machineban.description");
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
