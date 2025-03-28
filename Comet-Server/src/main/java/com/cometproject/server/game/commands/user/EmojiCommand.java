package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class EmojiCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        if (message == null || message.length == 0) {
            return;
        }

        client.getPlayer().getData().setEmojiEnabled(!client.getPlayer().getData().isEmojiEnabled());
        client.getPlayer().getSession().send(new NotificationMessageComposer("emojis", "The emojis are " + (client.getPlayer().getData().isEmojiEnabled() ? "activated" : "disabled")));
    }

    @Override
    public String getPermission() {
        return "emoji_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.emoji.description", "Turn emojis on or off");
    }
}
