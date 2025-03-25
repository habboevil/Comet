package com.cometproject.api.game.pets;

import com.cometproject.api.game.utilities.Position;
import com.google.gson.JsonObject;

import java.util.List;

public interface IPetData extends IPetStats {

    JsonObject toJsonObject();

    void saveStats();

    void saveHorseData();

    void increaseExperience(int amount);

    void increaseHappiness(int amount);

    void incrementLevel();

    void incrementScratches();

    int getId();

    String getName();

    int getLevel();

    void setLevel(int level);

    int getHappiness();

    int getExperience();

    int getExperienceGoal();

    int getEnergy();

    int getHunger();

    void decreaseEnergy(int amount);

    void increaseEnergy(int amount);

    int getOwnerId();

    String getColour();

    int getRaceId();

    void setRaceId(int raceId);

    String getLook();

    int getHairDye();

    void setHairDye(int hairDye);

    int getHair();

    void setHair(int hair);

    int getTypeId();

    Position getRoomPosition();

    void setRoomPosition(Position position);

    boolean isSaddled();

    void setSaddled(boolean saddled);

    boolean isAnyRider();

    void setAnyRider(boolean anyRider);

    int getScratches();

    int getBirthday();

    void setBirthday(int birthday);

    String getOwnerName();

    void setOwnerName(String ownerName);

    String getExtradata();

    void setExtradata(String extraData);

    int getRoomId();

    void setRoomId(int roomId);

    void savePlantsData();

    void increaseHunger(int amount);
}
