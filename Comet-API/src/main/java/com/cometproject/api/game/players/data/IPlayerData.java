package com.cometproject.api.game.players.data;

import java.util.HashMap;

public interface IPlayerData extends PlayerAvatar {

    void save();

    void decreaseCredits(int amount);

    void increaseCredits(int amount);

    void decreaseVipPoints(int points);

    void increaseVipPoints(int points);

    void increaseActivityPoints(int points);

    void decreaseActivityPoints(int points);

    void increaseSeasonalPoints(int points);

    void decreaseSeasonalPoints(int points);

    int getId();

    int getRank();

    void setRank(int rank);

    String getUsername();

    void setUsername(String username);

    int getAchievementPoints();

    void increaseAchievementPoints(int amount);

    int getXpPoints();

    void increaseXpPoints(int amount);

    String getMotto();

    void setMotto(String motto);

    String getFigure();

    void setFigure(String figure);

    String getGender();

    void setGender(String gender);

    int getCredits();

    void setCredits(int credits);

    int getVipPoints();

    void setVipPoints(int vipPoints);

    int getSeasonalPoints();

    void setSeasonalPoints(int points);

    int getBonusPoints();

    int getLastVisit();

    void setLastVisit(long time);

    String getRegDate();

    boolean isVip();

    void setVip(boolean vip);

    int getRegTimestamp();

    void setRegTimestamp(int regTimestamp);

    String getEmail();

    void setEmail(String email);

    int getFavouriteGroup();

    void setFavouriteGroup(int favouriteGroup);

    String getIpAddress();

    void setIpAddress(String ipAddress);

    int getActivityPoints();

    void setActivityPoints(int activityPoints);

    String getTemporaryFigure();

    void setTemporaryFigure(String temporaryFigure);

    int getQuestId();

    void setQuestId(int questId);

    String getNameColour();

    void setNameColour(String nameColour);

    void setTag(String var1);

    int getKisses();

    void setKisses(int amount);

    void increaseKisses(int amount);

    void incrementKisses();

    void decreaseKisses(int amount);

    boolean getChangingName();

}