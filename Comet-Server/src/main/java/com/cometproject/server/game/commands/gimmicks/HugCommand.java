package com.cometproject.server.game.commands.gimmicks;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class HugCommand extends ChatCommand {

    private static final int HUG_EFFECT_ID = 9;
    private static final int ANTI_SPAM_INTERVAL_SECONDS = 30;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.user.invalid", "Invalid user!"), client);
            return;
        }

        final int timeSinceLastUpdate = ((int) Comet.getTime() - client.getPlayer().getLastCommandRoleplay());
        final String huggedPlayer = params[0];
        final RoomEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(huggedPlayer, RoomEntityType.PLAYER);

        if (entity == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not found in any room."), client);
            return;
        }

        if (entity.getUsername().equals(client.getPlayer().getData().getUsername())) {
            sendNotif(Locale.getOrDefault("command.hug.himself", "You cannot embrace yourself."), client);
            return;
        }

        final int entityX = entity.getPosition().getX();
        final int entityY = entity.getPosition().getY();
        final int playerX = client.getPlayer().getEntity().getPosition().getX();
        final int playerY = client.getPlayer().getEntity().getPosition().getY();

        if (!isClose(entityX, entityY, playerX, playerY)) {
            client.getPlayer().getSession().send(new WhisperMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.notaround", "Oops! %playername% is not close. Walk to this player.").replace("%playername%", entity.getUsername()), 16));
            return;
        }

        if (timeSinceLastUpdate < ANTI_SPAM_INTERVAL_SECONDS) {
            client.getPlayer().getSession().send(new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.get("command.hug.antispam"), ChatEmotion.NONE, 1));
            return;
        }

        // Here is where we set the hug logic
        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new TalkMessageComposer(client.getPlayer().getEntity().getId(), Locale.getOrDefault("command.hug.message", "You hugged %username%.").replace("%username%", huggedPlayer), ChatEmotion.SMILE, HUG_EFFECT_ID));
        entity.applyEffect(new PlayerEffect(HUG_EFFECT_ID));
        client.getPlayer().getEntity().applyEffect(new PlayerEffect(HUG_EFFECT_ID));
        client.getPlayer().setLastCommandRoleplay(timeSinceLastUpdate);
    }

    private boolean isClose(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) < 2 && Math.abs(y1 - y2) < 2;
    }

    @Override
    public String getPermission() {
        return "hug_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "(user)");
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.hug.description", "Hug a user");
    }
}
