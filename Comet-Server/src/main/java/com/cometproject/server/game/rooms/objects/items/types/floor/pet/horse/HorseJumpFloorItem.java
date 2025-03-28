package com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse;

import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.pets.PetGesture;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.AdvancedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.SeatFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse.event.HorseJumpFloorItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.RandomUtil;

import java.util.List;

public class HorseJumpFloorItem extends AdvancedFloorItem<HorseJumpFloorItemEvent> {
    private final HorseJumpFloorItemEvent event;
    private boolean restore = false;
    private PetEntity petEntity;

    public HorseJumpFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);

        this.getItemData().setData("0");
        this.queueEvent(this.event = new HorseJumpFloorItemEvent(this.getTickCount()));
    }

    @Override
    public void onLoad() {
        this.resetTicks();
    }

    @Override
    public void onPlaced() {
        this.resetTicks();
    }

    @Override
    public void onItemAddedToStack(RoomItemFloor floorItem) {
        if (floorItem instanceof SeatFloorItem) {
            this.resetTicks();
        }
    }

    @Override
    public void onEventComplete(HorseJumpFloorItemEvent event) {
        this.movementEntities();
        this.resetTicks();
    }

    @Override
    public void onTickComplete() {
        if (this.restore) {
            this.restore = false;

            this.getItemData().setData("0");
            this.sendUpdate();
            return;
        }

        this.getItemData().setData("0");

        if (this.petEntity.getData().getHappiness() == 0) {
            this.petEntity.getPetAI().applyGesture(PetGesture.SAD.getGestureKey());
            this.getItemData().setData("3");
        } else if (this.petEntity.getData().getHunger() == 0) {
            this.petEntity.getPetAI().applyGesture(PetGesture.HUNGRY.getGestureKey());
            this.getItemData().setData("3");
        } else if (this.petEntity.getData().getEnergy() == 0) {
            this.petEntity.getPetAI().applyGesture(PetGesture.ENERGY.getGestureKey());
            this.getItemData().setData("3");
        }

        if (this.getItemData().getData().equals("0")) {
            final int random = RandomUtil.getRandomInt(1, 100);

            if (random >= 66) {
                this.getItemData().setData("4");
            } else if (random <= 50 && random > 10) {
                this.getItemData().setData("2");
            } else if (random <= 10) {
                this.getItemData().setData("1");
            } else {
                this.getItemData().setData("3");
            }
        }

        this.sendUpdate();

        if (this.getItemData().getData().equals("4")) {
            // success!
            this.manageAchievements();

            this.petEntity.getPetAI().increaseExperience(5);
            this.petEntity.getPetAI().applyGesture(PetGesture.SMILE.getGestureKey());
        } else {
            // failure!
            if (this.petEntity.getMountedEntity() instanceof PlayerEntity) {
                ((PlayerEntity) this.petEntity.getMountedEntity()).getPlayer().setOnHorseJumpingSequence(false);
            }

            this.petEntity.getPetAI().applyGesture(PetGesture.SAD.getGestureKey());
        }

        this.restore = true;
        this.setTicks(RoomItemFactory.getProcessTime(1.5));
    }

    public void manageAchievements() {
        if (!(this.petEntity.getMountedEntity() instanceof final PlayerEntity playerEntity)) return;

        playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.HORSE_JUMPING, 1);

        if (playerEntity.getPlayer().onHorseJumpingSequence()) {
            playerEntity.getPlayer().getAchievements().progressAchievement(AchievementType.HORSE_CONSECUTIVE_JUMPS, 1);
        }

        playerEntity.getPlayer().setOnHorseJumpingSequence(true);

        final Session ownerRoomSession = NetworkManager.getInstance().getSessions().getByPlayerId(this.getRoom().getData().getOwnerId());

        if (ownerRoomSession != null) {
            ownerRoomSession.getPlayer().getAchievements().progressAchievement(AchievementType.HORSE_JUMPING_IN_ROOM, 1);
        }
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        final PetEntity petEntity = (PetEntity) entity;

        if (petEntity.getMountedEntity() == null) {
            return;
        }

        final Position[] positions = this.getBarPositions();

        int barToFace = 0;

        if (this.getRotation() != 2) {
            if (petEntity.getPosition().getX() != this.getPosition().getX()) barToFace = 1;
        } else {
            if (petEntity.getPosition().getY() != this.getPosition().getY()) barToFace = 1;
        }

        final Position bar = this.getBarPositions()[barToFace];

        if (!entity.hasStatus(RoomEntityStatus.JUMP) && this.petEntity != entity) {
            entity.lookTo(bar.getX(), bar.getY());

            entity.getMountedEntity().moveTo(entity.getPosition().squareInFront(entity.getBodyRotation(), 4));
            entity.addStatus(RoomEntityStatus.JUMP, "1.1");
            entity.markNeedsUpdate();

            this.petEntity = petEntity;
        }

        if (positions[0].equals(entity.getPosition()) || positions[1].equals(entity.getPosition())) {
            if (petEntity.getData().getTypeId() == PetType.HORSE) {
                entity.removeStatus(RoomEntityStatus.JUMP);
                entity.markNeedsUpdate();

                this.setTicks(RoomItemFactory.getProcessTime(0.5));
            }
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == PetType.HORSE) {
            entity.removeStatus(RoomEntityStatus.JUMP);

            this.petEntity = null;
        }
    }

    @Override
    public void onUnload() {
        this.petEntity = null;
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity, Position position) {
        final Position[] barPos = this.getBarPositions();
        final boolean barPosEq = (barPos[0].getX() == position.getX() && barPos[0].getY() == position.getY()) || (barPos[1].getX() == position.getX() && barPos[1].getY() == position.getY());

        return entity.getMountedEntity() == null && barPosEq && !this.hasSeatItems();
    }

    public boolean hasSeatItems() {
        return this.getItemsOnStack().stream().anyMatch(item -> item instanceof SeatFloorItem);
    }

    private Position[] getBarPositions() {
        final Position a = this.getPosition().copy();
        final Position b = this.getPosition().copy();

        if (this.getRotation() == 2) {
            a.setX(a.getX() + 1);
        } else {
            a.setY(a.getY() + 1);
        }

        b.setX(b.getX() + 1);
        b.setY(b.getY() + 1);

        return new Position[]{a, b};
    }

    public void movementEntities() {
        final List<RoomEntity> entities = this.getEntitiesOnItem();

        if (entities.size() < 1)
            return;

        for (final RoomEntity entity : entities) {
            if (!(entity instanceof PlayerEntity) || entity.getMountedEntity() != null || !(entity.getTile().getTopItemInstance() instanceof SeatFloorItem))
                continue;

            final RoomItemFloor topItemInstance = entity.getTile().getTopItemInstance();

            entity.moveTo(topItemInstance.getPosition().squareInFront(topItemInstance.getRotation()));
        }
    }

    private int getTickCount() {
        return RoomItemFactory.getProcessTime(1);
    }

    private void resetTicks() {
        event.setTotalTicks(this.getTickCount());
        this.queueEvent(event);
    }
}
