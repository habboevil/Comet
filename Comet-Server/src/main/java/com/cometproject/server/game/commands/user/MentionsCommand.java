package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerMention;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class MentionsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        StringBuilder builder = new StringBuilder();
        for (final PlayerMention mention : client.getPlayer().getMentions()) {
            mention.getUsername().replace("%username%", mention.getUsername());
            mention.getMessage().replace("%message%", mention.getMessage());
            builder.append("These are your latest mentions: " + "\n\n")
                    .append(mention.getUsername()).append(": ").append(mention.getMessage()).append("\n");
        }

        client.getPlayer().getSession().send(new MotdNotificationMessageComposer(builder.toString()));
    }

    @Override
    public String getPermission() {
        return "mentions_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.mentions.description", "Check your list of mentions received");
    }
}
