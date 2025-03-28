package com.cometproject.server.game.achievements;

import com.cometproject.api.game.achievements.types.AchievementCategory;
import com.cometproject.api.game.achievements.types.IAchievement;
import com.cometproject.api.game.achievements.types.IAchievementGroup;

import java.util.Map;

public class AchievementGroup implements IAchievementGroup {
    private final Map<Integer, IAchievement> achievements;

    private final int id;
    private final String groupName;
    private final AchievementCategory category;

    public AchievementGroup(int id, Map<Integer, IAchievement> achievements, String groupName, AchievementCategory category) {
        this.id = id;
        this.achievements = achievements;
        this.groupName = groupName;
        this.category = category;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getLevelCount() {
        return this.achievements.size();
    }

    @Override
    public IAchievement getAchievement(int level) {
        return this.achievements.get(level);
    }

    @Override
    public Map<Integer, IAchievement> getAchievements() {
        return achievements;
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public IAchievement getAchievementByProgress(int progress) {
        IAchievement progressAchievement = null;

        if (progress > 0) {
            for (IAchievement achievement : this.achievements.values()) {
                if (progress > achievement.getProgressNeeded()) {
                    if (progressAchievement != null) {
                        if (progressAchievement.getLevel() > achievement.getLevel()) {
                            continue;
                        }
                    }

                    progressAchievement = achievement;
                }
            }
        }

        return progressAchievement;
    }

    @Override
    public AchievementCategory getCategory() {
        return category;
    }
}
