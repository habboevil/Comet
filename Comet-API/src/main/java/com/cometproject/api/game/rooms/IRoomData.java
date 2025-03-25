package com.cometproject.api.game.rooms;

import com.cometproject.api.game.rooms.settings.*;

import java.util.List;
import java.util.Map;

public interface IRoomData {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    int getOwnerId();

    void setOwnerId(int ownerId);

    String getOwner();

    void setOwner(String owner);

    int getMaxUsers();

    void setMaxUsers(int maxUsers);

    RoomAccessType getAccess();

    void setAccess(RoomAccessType access);

    String getPassword();

    void setPassword(String password);

    int getScore();

    void setScore(int score);

    int getSongId();

    int setSongId(int songId);

    String[] getTags();

    void setTags(String[] tags);

    Map<String, String> getDecorations();

    void setDecorations(Map<String, String> decorations);

    String getModel();

    void setModel(String model);

    boolean getHideWalls();

    void setHideWalls(boolean hideWalls);

    int getWallThickness();

    int getFloorThickness();

    int getCategoryId();

    void setCategoryId(int category);

    void setThicknessWall(int thicknessWall);

    void setThicknessFloor(int thicknessFloor);

    boolean getAllowWalkthrough();

    boolean isAllowWalkthrough();

    void setAllowWalkthrough(boolean allowWalkthrough);

    String getHeightmap();

    void setHeightmap(String heightmap);

    boolean isAllowPets();

    void setAllowPets(boolean allowPets);

    String getOriginalPassword();

    void setOriginalPassword(String originalPassword);

    RoomTradeState getTradeState();

    void setTradeState(RoomTradeState tradeState);

    int getCreationTime();

    int getBubbleMode();

    void setBubbleMode(int bubbleMode);

    int getBubbleType();

    void setBubbleType(int bubbleType);

    int getBubbleScroll();

    void setBubbleScroll(int bubbleScroll);

    int getChatDistance();

    void setChatDistance(int chatDistance);

    int getAntiFloodSettings();

    void setAntiFloodSettings(int antiFloodSettings);

    RoomMuteState getMuteState();

    void setMuteState(RoomMuteState muteState);

    RoomKickState getKickState();

    void setKickState(RoomKickState kickState);

    RoomBanState getBanState();

    void setBanState(RoomBanState banState);

    List<String> getDisabledCommands();

    int getGroupId();

    void setGroupId(int groupId);

    String getRequiredBadge();

    RoomType getType();

    void setType(RoomType type);

    String getThumbnail();

    void setThumbnail(String thumbnail);

    String getDecorationString();

    boolean isWiredHidden();

    void setIsWiredHidden(boolean hiddenWired);


    int getRollerSpeedLevel();


    void setRollerSpeedLevel(int rollerSpeedLevel);


    boolean isRoomDiagonal();

    RoomDiagonalType getRoomDiagonalType();

    void setRoomDiagonalType(RoomDiagonalType roomDiagonal);

    int getRoomPrice();

    void setRoomPrice(int value);

    int getRoomBuyer();

    void setRoomBuyer(int id);

    RoomProcessingType getRoomProcessType();

    RoomProcessingType setRoomProcessType(RoomProcessingType type);
}
