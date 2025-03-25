package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.misc.OpenLinkMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.regex.Pattern;

public class NameColourCommand extends ChatCommand {
    private static final String WHISPER_INVALID_COLOR = Locale.get("command.namecolour.wisper");
    private static final String WHISPER_SUCCESS = Locale.get("command.namecolour.success");

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendWhisper(WHISPER_INVALID_COLOR, client);
            return;
        }

        final String colour = params[0];
        if (!isValidHexColor(colour.replace("#", ""))) {
            sendWhisper(WHISPER_INVALID_COLOR, client);
            return;
        }

        sendWhisper(WHISPER_SUCCESS, client);
        client.getPlayer().getData().setNameColour(colour);
        client.getPlayer().getData().save();
        isExecuted(client);
    }

    private boolean isValidHexColor(String color) {
        return Pattern.matches("([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", color);
    }

    @Override
    public String getPermission() {
        return "namecolour_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.namecolour.description", "Put color to your name");
    }
}
