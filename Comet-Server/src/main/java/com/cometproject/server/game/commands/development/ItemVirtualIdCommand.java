package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ItemVirtualIdCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            int virtualIdCount = ItemManager.getInstance().getItemIdToVirtualIds().size();
            client.send(new AlertMessageComposer("There are currently " + virtualIdCount + " item virtual IDs in memory."));
        } else {
            try {
                int virtualId = Integer.parseInt(params[0]);
                Long realItemId = ItemManager.getInstance().getItemIdByVirtualId(virtualId);
                client.send(new AlertMessageComposer("Virtual ID: " + virtualId + "\nReal ID: " + realItemId));
            } catch (NumberFormatException e) {
                client.send(new AlertMessageComposer("Invalid input. Please provide a valid virtual item ID."));
            }
        }
    }

    @Override
    public String getPermission() {
        return "debug";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.itemvirtualid.description", " - Shows all current loaded virtual item IDs in memory");
    }
}
