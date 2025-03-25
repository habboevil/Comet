package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;

public class EmptyCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        String confirmMessage = Locale.getOrDefault("command.empty.confirm", "<b>Warning!</b>\rAre you sure? You are going to delete your Furniture.\r\rIf you are sure type  <b>:" + Locale.get("command.empty.name") + " yes</b>");

        if (params.length != 1 || !params[0].equals(Locale.getOrDefault("command.empty.yes", "yes"))) {
            sendAlert(confirmMessage, client);
        } else {
            clearInventory(client);
            sendNotif(Locale.getOrDefault("command.empty.emptied", "Your inventory was cleared."), client);
        }

        client.send(new UpdateInventoryMessageComposer());
    }

    private void clearInventory(Session client) {
        int userId = client.getPlayer().getId();
        if (client.getPlayer().getInventory().isViewingInventory()) {
            userId = client.getPlayer().getInventory().viewingInventoryUserId();
        }

        client.getPlayer().getInventory().clearItems();
        client.getPlayer().getInventory().send();
        InventoryDao.clearInventory(userId);
    }

    @Override
    public String getPermission() {
        return "empty_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.empty.yes", "yes");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.empty.description", "Empty all your inventory");
    }
}