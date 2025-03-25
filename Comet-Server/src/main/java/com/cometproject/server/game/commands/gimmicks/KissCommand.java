package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.types.PlayerAvatarActions;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class KissCommand extends ChatCommand {
    private static final int ANTI_SPAM_INTERVAL_SECONDS = 5;

    @Override
    public void execute(Session client, String[] params) {
        if (!isCommandValid(client, params)) {
            return;
        }

        if (isMuted(client)) {
            return;
        }

        String kissedPlayer = params[0];
        Session kissedSession = getKissedSession(kissedPlayer);

        if (kissedSession == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (!isInRoom(kissedSession)) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in any room."), client);
            return;
        }

        if (isSameUser(client, kissedSession)) {
            sendNotif(Locale.getOrDefault("command.kiss.himself", "You can't kiss yourself!"), client);
            return;
        }

        if (isTooFarAway(client, kissedSession)) {
            return;
        }

        int timeSinceLastUpdated = (int) Comet.getTime() - client.getPlayer().getLastCommandRoleplay();

        if (timeSinceLastUpdated < ANTI_SPAM_INTERVAL_SECONDS) {
            client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.get("command.hug.antispam"), ChatEmotion.NONE, 1));
            return;
        }

        // Let use perform the kiss
        performKiss(client, kissedSession);
        client.getPlayer().setLastCommandRoleplay(timeSinceLastUpdated);
    }

    private boolean isCommandValid(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.kiss.none", "Who do you want to kiss?"), client);
            return false;
        }
        return true;
    }

    private boolean isMuted(Session client) {
        if (client.getPlayer().getEntity().isRoomMuted() || client.getPlayer().getEntity().getRoom().getRights().hasMute(client.getPlayer().getId())) {
            sendNotif(Locale.getOrDefault("command.user.muted", "You are muted!"), client);
            return true;
        }
        return false;
    }

    private Session getKissedSession(String kissedPlayer) {
        return NetworkManager.getInstance().getSessions().getByPlayerUsername(kissedPlayer);
    }

    private boolean isInRoom(Session session) {
        return session.getPlayer().getEntity() != null;
    }

    private boolean isSameUser(Session client, Session kissedSession) {
        return kissedSession.getPlayer().getData().getUsername().equals(client.getPlayer().getData().getUsername());
    }

    private boolean isTooFarAway(Session client, Session kissedSession) {
        int kissedPlayerX = kissedSession.getPlayer().getEntity().getPosition().getX();
        int kissedPlayerY = kissedSession.getPlayer().getEntity().getPosition().getY();
        int kissingPlayerX = client.getPlayer().getEntity().getPosition().getX();
        int kissingPlayerY = client.getPlayer().getEntity().getPosition().getY();

        if (!((Math.abs(kissedPlayerX - kissingPlayerX) >= 2) || (Math.abs(kissedPlayerY - kissingPlayerY) >= 2))) {
            return false; // Look if the user is not too far away
        }

        client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.notaround", "Oops! %playername% is too far away, walk towards that player.")
                .replace("%playername%", kissedSession.getPlayer().getData().getUsername()), 16));

        return true;
    }

    private void performKiss(Session client, Session kissedSession) {
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(client.getPlayer().getEntity().getId(),
                "* " + client.getPlayer().getData().getUsername() + " " + Locale.getOrDefault("command.kiss.word", "kissed") + " " + kissedSession.getPlayer().getData().getUsername() + " *",
                ChatEmotion.NONE, 16));

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new ActionMessageComposer(client.getPlayer().getEntity().getId(),
                PlayerAvatarActions.EXPRESSION_BLOW_A_KISS.getValue()));

        client.getPlayer().getEntity().applyEffect(new PlayerEffect(9));
        kissedSession.getPlayer().getEntity().applyEffect(new PlayerEffect(9));
    }

    @Override
    public String getPermission() {
        return "kiss_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "(user)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.kiss.description", "Kiss a user");
    }
}
