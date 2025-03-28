package com.cometproject.server.game.landing;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.landing.calendar.CalendarDay;
import com.cometproject.server.game.landing.types.PromoArticle;
import com.cometproject.server.storage.queries.landing.LandingDao;
import com.cometproject.server.tasks.CometThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LandingManager implements Initialisable {
    private static final Logger log = LogManager.getLogger(LandingManager.class.getName());
    private static LandingManager landingManagerInstance;
    private Map<Integer, PromoArticle> articles;
    private Map<Integer, CalendarDay> calendarDays;

    private Map<PlayerAvatar, Integer> hallOfFame;

    public LandingManager() {
    }

    public static LandingManager getInstance() {
        if (landingManagerInstance == null) {
            landingManagerInstance = new LandingManager();
        }

        return landingManagerInstance;
    }

    @Override
    public void initialize() {
        this.loadArticles();
        this.loadHallOfFame();
        this.loadCalendar();

        log.info("LandingManager initialized");
    }

    public void loadArticles() {
        if (this.articles != null) {
            this.articles.clear();
        }

        this.articles = LandingDao.getArticles();
    }

    public void loadCalendar() {
        if (this.calendarDays != null) {
            this.calendarDays.clear();
        }

        this.calendarDays = LandingDao.getCalendarDays();
    }

    private void loadHallOfFame() {
        if (this.hallOfFame != null) {
            this.hallOfFame.clear();
        }

        if (CometSettings.hallOfFameEnabled) {
            this.hallOfFame = LandingDao.getHallOfFame(CometSettings.hallOfFameCurrency, 10);

            // Queue it to be refreshed again in X minutes
            CometThreadManager.getInstance().executeSchedule(
                    this::loadHallOfFame,
                    CometSettings.hallOfFameRefreshMinutes,
                    TimeUnit.MINUTES);
        }
    }

    public Map<Integer, PromoArticle> getArticles() {
        return articles;
    }

    public Map<PlayerAvatar, Integer> getHallOfFame() {
        return this.hallOfFame;
    }

    public int getUnlockDays() {

        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getTotalDays() {
        return this.calendarDays.size();
    }

    public String getGiftByDay(int i) {
        if (this.calendarDays.containsKey(i))
            return this.calendarDays.get(i).getGift();

        return "";
    }

    public CalendarDay getCampaignDay(int i) {
        if (calendarDays.containsKey(i))
            return calendarDays.get(i);

        return null;
    }
}
