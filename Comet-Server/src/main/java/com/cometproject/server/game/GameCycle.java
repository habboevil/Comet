package com.cometproject.server.game;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UserObjectMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.system.StatisticsDao;
import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameCycle implements CometTask, Initialisable {
    private static final int interval = 1;
    private static final Logger log = LogManager.getLogger(GameCycle.class.getName());
    private static GameCycle gameThreadInstance;
    private ScheduledFuture gameFuture;

    private boolean active = false;

    private int currentOnlineRecord = 0;
    private int onlineRecord = 0;

    public GameCycle() {

    }

    public static GameCycle getInstance() {
        if (gameThreadInstance == null)
            gameThreadInstance = new GameCycle();

        return gameThreadInstance;
    }

    @Override
    public void initialize() {
        this.gameFuture = CometThreadManager.getInstance().executePeriodic(this, interval, interval, TimeUnit.MINUTES);
        this.active = true;

        this.onlineRecord = StatisticsDao.getPlayerRecord();
    }

    @Override
    public void run() {
        try {
            if (!this.active) return;

            BanManager.getInstance().processBans();

            final int usersOnline = NetworkManager.getInstance().getSessions().getUsersOnlineCount();
            boolean updateOnlineRecord = false;

            if (usersOnline > this.currentOnlineRecord) {
                this.currentOnlineRecord = usersOnline;
            }

            if (usersOnline > this.onlineRecord) {
                this.onlineRecord = usersOnline;
                updateOnlineRecord = true;
            }

            this.processSession();

            if (!updateOnlineRecord)
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild());
            else
                StatisticsDao.saveStatistics(usersOnline, RoomManager.getInstance().getRoomInstances().size(), Comet.getBuild(), this.onlineRecord);


        } catch (Exception e) {
            log.error("Error during game thread", e);
        }
    }

    public int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    private void processSession() {
        if (!CometSettings.onlineRewardEnabled && !this.needsUpdateDailyData()) return;

        final Collection<ISession> sessionValues = NetworkManager.getInstance().getSessions().getSessions().values();

        if (sessionValues.isEmpty()) return;

        for (final ISession client : sessionValues) {
            try {
                if (!(client instanceof Session) || client.getPlayer() == null || client.getPlayer().getData() == null) {
                    continue;
                }

                if (this.needsUpdateDailyData()) {
                    client.getPlayer().getData().setKisses(client.getPlayer().getData().getKisses());
                    client.getPlayer().getStats().setDailyRespects(CometSettings.dailyRespects);
                    client.getPlayer().getStats().setScratches(CometSettings.dailyScratchs);

                    client.send(new UserObjectMessageComposer(client.getPlayer()));
                }

                client.getPlayer().managePeriodicAchievements();
                client.getPlayer().getAchievements().progressAchievement(AchievementType.ONLINE_TIME, interval);

                if (client.getPlayer().needsReceivePeriodicRewards()) {
                    if (CometSettings.onlineRewardDiamonds > 0) {
                        client.getPlayer().getData().increaseVipPoints(CometSettings.onlineRewardDiamonds);
                        client.sendQueue(new NotificationMessageComposer("diamonds", String.format("%d You have won diamonds!", CometSettings.onlineRewardDiamonds)));
                    }

                    final int credits = this.getPeriodicRewardAmount(
                            client.getPlayer().isVip() ? CometSettings.onlineRewardCreditsVip : CometSettings.onlineRewardCredits
                    );

                    final int duckets = this.getPeriodicRewardAmount(
                            client.getPlayer().isVip() ? CometSettings.onlineRewardDucketsVip : CometSettings.onlineRewardDuckets
                    );

                    client.getPlayer().getData().increaseCredits(credits);
                    client.getPlayer().getData().increaseActivityPoints(duckets);

                    client.sendQueue(new NotificationMessageComposer("periodic.credits.received", String.format("You have just received %d credits!", credits)))
                            .sendQueue(new NotificationMessageComposer("periodic.duckets.received", String.format("You have just received %d duckets!", duckets)));

                    client.getPlayer().getData().save(); // TODO: Move this to a batch update
                    client.getPlayer().sendBalance();
                    client.getPlayer().setLastReward(Comet.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error while cycling rewards", e);
            }
        }

        if (this.needsUpdateDailyData()) {
            PlayerDao.dailyPlayerUpdate(CometSettings.dailyRespects, CometSettings.dailyScratchs);
        }
    }

    public boolean needsUpdateDailyData() {
        return this.getHour() == 0 && this.getMinute() == 0;
    }

    public int getPeriodicRewardAmount(int baseReward) {
        if (this.todayIsDoubleReward()) {
            return baseReward * 2;
        }

        return baseReward;
    }

    public boolean todayIsDoubleReward() {
        return CometSettings.onlineRewardDoubleDays.contains(LocalDate.now().getDayOfWeek());
    }

    public void stop() {
        this.active = false;
        this.gameFuture.cancel(false);
    }

    public int getCurrentOnlineRecord() {
        return this.currentOnlineRecord;
    }

    public int getOnlineRecord() {
        return this.onlineRecord;
    }
}
