package com.cometproject.server.network.messages.incoming.user.interactions;

import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.ChatEmotion;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SendAmbassadorAlertMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.getPlayer().getEntity().setStatusType(0);
        long currentTimeMs = System.currentTimeMillis();
        long timeSinceLastUpdate = currentTimeMs - client.getPlayer().getLastPhotoTaken();
        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null || timeSinceLastUpdate < (long) (client.getPlayer().getData().getRank() > 6 ? 0 : 5000)) {
            client.send(new NotificationMessageComposer("time_error", Locale.getOrDefault("action.time.error.message", "You must wait 5 seconds between each action.")));
            return;
        }
        int playerId = msg.readInt();
        Room room = client.getPlayer().getEntity().getRoom();
        if (room == null) {
            return;
        }
        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);
        if (playerEntity == null) {
            return;
        }
        Position actorPosition = client.getPlayer().getEntity().getPosition();
        if (actorPosition == null) {
            return;
        }
        if (actorPosition.distanceTo(playerEntity.getPosition()) > 2.0) {
            client.send(new NotificationMessageComposer("action_distance_error", Locale.getOrDefault("action.time.error.message", "You must be in front of the person to perform the action.")));
            return;
        }
        this.handleInteraction(room, "kiss", client, playerEntity, actorPosition);
        client.getPlayer().setLastPhotoTaken(System.currentTimeMillis());
    }

    public void handleInteraction(Room room, String type, Session actor, PlayerEntity victim, Position actorPosition) {
        int effectOne = 0;
        int effectOneDuration = 5;
        int effectTwo = 0;
        int effectTwoDuration = 5;
        String text = "";
        ChatEmotion emotion = ChatEmotion.NONE;
        int bubbleId = 1;
        switch (type) {
            case "burn": {
                effectOne = 115;
                effectTwo = 25;
                text = "<b>%target%</b> has been burned by %user%. <font color='#C62B2B'>\u00aa</font>";
                emotion = ChatEmotion.LAUGH;
                break;
            }
            case "kiss": {
                room.getEntities().broadcastMessage(new ActionMessageComposer(actor.getPlayer().getEntity().getId(), 2));
                room.getEntities().broadcastMessage(new ActionMessageComposer(victim.getId(), 2));
                //actor.getPlayer().getAchievements().progressAchievement(AchievementType.ACH_102, 1);
                victim.lookTo(actorPosition.getX(), actorPosition.getY());
                text = "<b>%user%</b> He has kissed %target%. <font color='#c62b80'>\u0192</font>";
                bubbleId = 16;
                emotion = ChatEmotion.SMILE;
                //actor.getPlayer().getQuests().progressQuestById(86, 0);
            }
        }
        if (effectOne > 0) {
            actor.getPlayer().getEntity().applyEffect(new PlayerEffect(effectOne, effectOneDuration));
        }
        if (effectTwo > 0) {
            victim.applyEffect(new PlayerEffect(effectTwo, effectTwoDuration));
        }
        room.getEntities().broadcastMessage(new TalkMessageComposer(-1, Locale.getOrDefault("action." + type, text).replace("%user%", actor.getPlayer().getData().getUsername()).replace("%target%", victim.getUsername()), emotion, bubbleId));
    }
}

