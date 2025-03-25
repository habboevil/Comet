package com.cometproject.api.game.players.data;

public interface IPlayerStatistics {
    void save();

    void incrementAchievementPoints(int amount);

    void incrementLevel();

    void incrementCautions(int amount);

    void incrementRespectPoints(int amount);

    void decrementDailyRespects(int amount);

    void incrementBans(int amount);

    void incrementAbusiveHelpTickets(int amount);

    int getPlayerId();

    int getDailyRespects();

    void setDailyRespects(int points);

    int getRespectPoints();

    int getAchievementPoints();

    int getLevel();

    int getFriendCount();

    int getTotalKisses();

    int getKissesReceived();

    int getKissesSent();

    int incrementTotalKisses(int increments);

    int incrementTotalKissesSent(int increments);

    int decrementTotalKisses(int increments);

    int incrementKissesReceived(int increments);

    int getHelpTickets();

    void setHelpTickets(int helpTickets);

    int getAbusiveHelpTickets();

    void setAbusiveHelpTickets(int abusiveHelpTickets);

    int getCautions();

    void setCautions(int cautions);

    int getBans();

    void setBans(int bans);

    void addBan();

    int getScratches();

    void setScratches(int scratches);
}
