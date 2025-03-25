package com.cometproject.server.game.commands.user;

import com.cometproject.api.config.CometSettings;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.OpenLinkMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class SuperWiredCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

        client.getPlayer().getSession().send(new MotdNotificationMessageComposer(
                "COMMANDS OF THE SUPERWIRED IN " + CometSettings.hotelName + " \n" +
                        "- handitem: " + "Applies a special handitem to the affected user"
        ));
    }

    @Override
    public String getPermission() {
        return "superwired_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.superwired.description", "Review the list of commands available on the superwired");
    }
}
