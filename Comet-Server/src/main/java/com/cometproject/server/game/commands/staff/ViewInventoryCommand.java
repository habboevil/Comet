package com.cometproject.server.game.commands.staff;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ViewInventoryCommand extends ChatCommand {
    private String logDesc = "";

    @Override
    public void execute(Session client, String[] params) {
        boolean error = false;

        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.viewinventory.reset", "It has been returned back your inventory!"), client);
            client.getPlayer().getInventory().loadItems(0);
            client.send(new UpdateInventoryMessageComposer());
            return;
        }

        final String username = params[0];

        if (!PlayerManager.getInstance().isOnline(username)) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        try {
            final int PlayerId = PlayerDao.getIdByUsername(username);
            if (PlayerId != 0) {
                client.getPlayer().getInventory().loadItems(PlayerId);
                client.send(new UpdateInventoryMessageComposer());
                sendNotif(Locale.getOrDefault("command.viewinventory.success", "You've changed your inventory successfully, write :viewinventory to return back your inventory."), client);
            } else {
                error = true;
            }
        } catch (Exception e) {
            error = true;
        } finally {
            if (error) {
                sendNotif(Locale.getOrDefault("command.viewinventory.error", "Error!, maybe the user you searched for does not exist!"), client);
            }
        }

        if (!CometExternalSettings.enableStaffMessengerLogs) return;

        this.logDesc = "%s looked at '%b' inventory"
                .replace("%s", client.getPlayer().getData().getUsername())
                .replace("%b", username);
    }

    @Override
    public String getPermission() {
        return "viewinventory_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "(user)");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.viewinventory.description");
    }

    @Override
    public boolean isAsync() {
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
