package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class OverrideCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final boolean isOverridden = !client.getPlayer().getEntity().isOverriden();

        client.getPlayer().getEntity().setOverrideA(isOverridden);
        client.getPlayer().getEntity().setOverriden(isOverridden);

        if (isOverridden)
            sendNotif(Locale.getOrDefault("command.override.enable", "The Override is activated !"), client);
        else
            sendNotif(Locale.getOrDefault("command.override.disable", "The Override is deactivated !"), client);
    }

    @Override
    public String getPermission() {
        return "override_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.override.description", "Walk on furniture that are forbidden to walk on");
    }

    @Override
    public boolean canDisable() {
        return true;
    }
}
