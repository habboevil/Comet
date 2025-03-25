package com.cometproject.server.game.commands.development;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.chat.PrefixDao;

import java.util.List;

public class PrefixCommand extends ChatCommand {
    private static List<String> prefixes;

    @Override
    public void execute(Session client, String[] params) {
        String prefix = params[0];

        prefixes = PrefixDao.getAll();

        if (prefix == null) {
            return;
        }

        boolean prefixesFilter = prefixes.stream().anyMatch(prefix::equalsIgnoreCase);

        if (prefixesFilter) {
            client.send(new NotificationMessageComposer("generic", Locale.get("command.prefix.filtered").replace("%prefix%", prefix)));
            return;
        } else if (prefix.length() < 1 || prefix.length() > 5) {
            client.send(new NotificationMessageComposer("generic", Locale.get("command.prefix.invalid")));
            return;
        }

        client.getPlayer().getData().setTag(prefix);
        client.getPlayer().getData().save();
        isExecuted(client);
    }

    @Override
    public String getPermission() {
        return "prefix_command";
    }

    @Override
    public String getParameter() {
        return "(ön ek)";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.prefix.description", "Add a prefix to your name");
    }
}
