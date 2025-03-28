package com.cometproject.server.game.rooms.objects;

import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.game.rooms.objects.IRoomObject;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.game.utilities.Positionable;
import com.cometproject.server.game.rooms.objects.entities.types.BotEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.utilities.comparators.PositionComparator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public abstract class RoomObject implements IRoomObject, Positionable {
    private final Room room;
    private Position position;

    public RoomObject(Position position, Room room) {
        this.position = position;
        this.room = room;
    }

    public RoomObject(
            int id,
            int userId,
            PlayerItem item,
            String extraData,
            int limitedStack,
            int limitedSells) {
        room = null;
    }

    public RoomTile getTile() {
        if (this.getPosition() == null) return null;

        return this.getRoom().getMapping().getTile(this.getPosition().getX(), this.getPosition().getY());
    }

    public boolean isAtDoor() {
        return this.position.getX() == this.getRoom().getModel().getDoorX() && this.position.getY() == this.getRoom().getModel().getDoorY();
    }

    public @Nullable PlayerEntity nearestPlayerEntity() {
        final PositionComparator positionComparator = new PositionComparator(this);
        final List<PlayerEntity> nearestEntities = this.getRoom().getEntities().getPlayerEntities();

        nearestEntities.sort(positionComparator);

        if (nearestEntities.isEmpty()) return null;

        return nearestEntities.get(0);
    }

    public PlayerEntity nearestPlayerEntity(PlayerEntity w) {
        final PositionComparator positionComparator = new PositionComparator(this);

        final List<PlayerEntity> nearestEntities = this.getRoom().getEntities().getPlayerEntities();

        nearestEntities.sort(positionComparator);

        for (final PlayerEntity playerEntity : nearestEntities) {

            //condition test possibly bypasses bug
            if ((!playerEntity.isVisible() && !playerEntity.getPlayer().isInvisible()) || !playerEntity.getPlayer().getEntity().isFinalized())
                continue;

            if (playerEntity == w)
                continue;

            return playerEntity;
        }

        return null;
    }

    public BotEntity nearestBotEntity(BotType type) {
        final PositionComparator positionComparator = new PositionComparator(this);

        final List<BotEntity> bots = new ArrayList<>();
        final List<BotEntity> nearestEntities = this.getRoom().getEntities().getBotEntities();

        if (type == null) {
            bots.addAll(nearestEntities);
        } else {
            for (final BotEntity botEntity : nearestEntities) {
                if (botEntity.getData().getBotType() == type) {
                    bots.add(botEntity);
                }
            }
        }

        bots.sort(positionComparator);

        for (final BotEntity botEntity : bots) {
            if (this.getPosition().distanceTo(botEntity.getPosition()) < 4) {
                return botEntity;
            }
        }

        if (bots.size() >= 1) {
            return bots.get(0); // no bots found, find the closest one.
        }

        return null;
    }

    public BotEntity nearestBotEntity() {
        return nearestBotEntity(null);
    }

    public Room getRoom() {
        return this.room;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position newPosition) {
        if (newPosition == null) return;

        this.position = newPosition.copy();
    }
}
