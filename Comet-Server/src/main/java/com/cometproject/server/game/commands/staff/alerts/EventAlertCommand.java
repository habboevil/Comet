package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.api.config.CometExternalSettings;
import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.settings.RoomAccessType;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class EventAlertCommand extends ChatCommand {
    private String logDesc = "";

    private static final String ALERT_TITLE_KEY = "command.eventalert.alerttitle";
    private static final String MESSAGE_KEY = "command.eventalert.message";
    private static final String BUTTON_TITLE_KEY = "command.eventalert.buttontitle";
    private static final String IMAGE_EVENT_KEY = "image.event";

    @Override
    public void execute(Session client, String[] params) {
        if (params.length == 0) {
            sendWhisper(Locale.getOrDefault("command.eventalert.missing_param", "Enter the message!"), client);
            return;
        }

        final int roomId = client.getPlayer().getEntity().getRoom().getId();
        final String imageEvent = Locale.getOrDefault(IMAGE_EVENT_KEY, "event");
        final Room room = client.getPlayer().getEntity().getRoom();
        final String message = this.merge(params);
        final String roomName = room.getData().getName();
        final IMessageComposer messageComposer = new RoomForwardMessageComposer(roomId);
        final String hostName = client.getPlayer().getData().getUsername();

        room.getData().setAccess(RoomAccessType.OPEN);
        GameContext.getCurrent().getRoomService().saveRoomData(room.getData());

        for (final ISession session : NetworkManager.getInstance().getSessions().getSessions().values()) {
            if (session.getPlayer() == null)
                continue;

            if (session.getPlayer().getNitro()) {
                if (session.getPlayer().getEntity() != null && session.getPlayer().getEntity().getRoom().getData().getId() == roomId)
                    continue;

                session.send(messageComposer);
                continue;
            }

            if (session.getPlayer().getSettings().ignoreEvents())
                continue;

            session.send(new AdvancedAlertMessageComposer(
                    Locale.get(ALERT_TITLE_KEY),
                    Locale.get(MESSAGE_KEY)
                            .replace("%message%", message)
                            .replace("%username%", session.getPlayer().getData().getUsername())
                            .replace("%hostname%", hostName)
                            .replace("%roomname%", roomName),
                    Locale.get(BUTTON_TITLE_KEY), "event:navigator/goto/" + roomId, imageEvent)
            );
        }

        if (!CometExternalSettings.enableStaffMessengerLogs) return;
        this.logDesc = String.format("Event alert created by %s in room '%s'",
                client.getPlayer().getData().getUsername(),
                roomName);
    }

    @Override
    public String getPermission() {
        return "eventalert_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.eventalert.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
