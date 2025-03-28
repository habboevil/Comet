package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.pets.PetDao;

public class EmptyPetsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        String confirmMessage = Locale.get("command.emptypets.confirm") + Locale.get("command.emptypets.name") + " " + Locale.get("command.empty.yes") + "</b>";

        if (params.length != 1 || !params[0].equals(Locale.getOrDefault("command.empty.yes", "yes"))) {
            sendAlert(confirmMessage, client);
        } else {
            PetDao.deletePets(client.getPlayer().getId());

            client.getPlayer().getPets().clearPets();
            client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));
            sendNotif(Locale.getOrDefault("command.emptypets.emptied", "Your pets inventory was cleared."), client);
        }

        client.send(new UpdateInventoryMessageComposer());
    }

    @Override
    public String getPermission() {
        return "emptypets_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.empty.yes", "yes");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.emptypets.description", "Remove all pets in your inventory");
    }
}
