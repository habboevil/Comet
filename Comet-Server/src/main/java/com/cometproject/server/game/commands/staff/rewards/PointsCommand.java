package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.boot.webhooks.CurrencyWebhook;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class PointsCommand extends ChatCommand {

    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        final String username = params[0];
        int points;

        boolean diamonds = true;
        boolean seasonal = false;

        try {
            points = Integer.parseInt(params[1]);
        } catch (Exception e) {
            return;
        }

        if (params.length > 2) {
            if (params[2].equals("seasonal")) {
                diamonds = false;
                seasonal = true;
            } else if (params[2].equals("duckets")) {
                diamonds = false;
            }
        }

        final Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null) {
            final PlayerData playerData = PlayerDao.getDataByUsername(username);

            if (playerData == null) return;

            if (diamonds) {
                playerData.increaseVipPoints(points);
            } else if (seasonal) {
                playerData.increaseSeasonalPoints(points);
            } else {
                playerData.increaseActivityPoints(points);
            }

            playerData.save();
            return;
        }

        String currency;
        String webhookCurrency;

        if (diamonds) {
            session.getPlayer().getData().increaseVipPoints(points);
            currency = "diamonds";
            webhookCurrency = "diamonds";
        } else if (seasonal) {
            session.getPlayer().getData().increaseSeasonalPoints(points);
            currency = "seasonal";
            webhookCurrency = "seasonal";
        } else {
            session.getPlayer().getData().increaseActivityPoints(points);
            currency = "duckets";
            webhookCurrency = "duckets";
        }

        session.getPlayer().getData().save();

        CurrencyWebhook.send(client.getPlayer().getData().getUsername(), session.getPlayer().getData().getUsername(), points, webhookCurrency);

        session.send(new AdvancedAlertMessageComposer(
                Locale.get("command.points.successtitle"),
                Locale.get("command.points.successmessage").replace("%amount%", String.valueOf(points))
                        .replace("%type%", Locale.getOrDefault("command.points.currency." + currency, currency))
        ));

        session.send(session.getPlayer().composeCurrenciesBalance());

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "%s sent %c %l to user '%u'"
                .replace("%l", Locale.get("command.points.currency." + currency))
                .replace("%c", params[1])
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%u", username);
    }

    @Override
    public String getPermission() {
        return "points_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username" + " " + "command.parameter.amount", "(user) (amount)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.points.description");
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
