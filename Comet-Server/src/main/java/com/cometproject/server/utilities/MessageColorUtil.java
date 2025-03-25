package com.cometproject.server.utilities;

import com.cometproject.server.network.messages.incoming.room.action.utilities.ChatColors;

public class MessageColorUtil {
    public static MessageColorUtil instance;

    public MessageColorUtil() {
        instance = null;
    }

    public static MessageColorUtil getInstance() {
        if (instance == null) {
            instance = new MessageColorUtil();
        }

        return instance;
    }

    public String getFilteredString(String targetString) {
        final String color = this.getFirstColorCodeOnString(targetString);

        if (color.isEmpty())
            return targetString;

        return "<font color=\"" + color + "\">" + this.clearColorsCode(targetString) + "</font>";
    }

    public String getFirstColorCodeOnString(String targetString) {
        if (targetString.contains("@red@")) {
            return ChatColors.RED.getColor();
        }

        if (targetString.contains("@blue@")) {
            return ChatColors.BLUE.getColor();
        }

        if (targetString.contains("@green@")) {
            return ChatColors.GREEN.getColor();
        }

        if (targetString.contains("@yellow@")) {
            return ChatColors.YELLOW.getColor();
        }

        if (targetString.contains("@orange@")) {
            return ChatColors.ORANGE.getColor();
        }

        if (targetString.contains("@pink@")) {
            return ChatColors.PINK.getColor();
        }

        if (targetString.contains("@purple@")) {
            return ChatColors.PURPLE.getColor();
        }

        if (targetString.contains("@grey@")) {
            return ChatColors.GRAY.getColor();
        }

        return "";
    }

    public String clearColorsCode(String targetString) {
        return targetString.replace("@red@", "")
                .replace("@blue@", "")
                .replace("@yellow@", "")
                .replace("@orange@", "")
                .replace("@green@", "")
                .replace("@grey@", "")
                .replace("@purple@", "")
                .replace("@rose@", "");
    }
}
