package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.components.types.ChatMessageColour;
import com.cometproject.server.network.sessions.Session;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ColourCommand extends ChatCommand {
    private static final Map<String, ChatMessageColour> colorMappings = new HashMap<>();

    static {
        colorMappings.put("red", ChatMessageColour.red);
        colorMappings.put("blue", ChatMessageColour.blue);
        colorMappings.put("green", ChatMessageColour.green);
        colorMappings.put("purple", ChatMessageColour.purple);
        colorMappings.put("cyan", ChatMessageColour.cyan);
    }
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            client.getPlayer().setChatMessageColour(null);
            sendNotif(Locale.getOrDefault("command.colour.reset", "Your chat is back to normal!"), client);
            return;
        }

        String colorName = params[0].toLowerCase();

        if (colorMappings.containsKey(colorName)) {
            ChatMessageColour color = colorMappings.get(colorName);
            client.getPlayer().setChatMessageColour(color);
            String notification = MessageFormat.format(Locale.getOrDefault("command.colour.done", "Your chat messages are now {0}"), colorName);
            sendNotif(notification, client);
        } else if (colorName.equals(Locale.getOrDefault("command.colour.reset.word", "normal"))) {
            client.getPlayer().setChatMessageColour(null);
            sendNotif(Locale.getOrDefault("command.colour.reset.description", "The color of your chat is back to normal!"), client);
        } else {
            String availableColors = String.join(", ", colorMappings.keySet());
            String errorMessage = MessageFormat.format(Locale.getOrDefault("command.colour.invalid", "Invalid color, colors available: {0}"), availableColors);
            sendNotif(errorMessage, client);
        }
    }

    @Override
    public String getPermission() {
        return "colour_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.colour", "(colour)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.colour.description", "Change the color of your messages");
    }
}
