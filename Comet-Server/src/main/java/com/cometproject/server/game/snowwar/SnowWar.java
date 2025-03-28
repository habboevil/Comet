package com.cometproject.server.game.snowwar;

/*
 * ****************
 * @author capos *
 * ****************
 */

import com.cometproject.server.game.snowwar.data.SnowWarPlayerData;

import java.util.HashMap;

public class SnowWar {
    // GAME VALUES
    public static final int GAMESECONDS = 120;
    public static final int GAMETURNMILLIS = 150;
    public static final int GAMETURNS = (GAMESECONDS * 1000) / GAMETURNMILLIS;
    // PLAYERS VALUES
    public static final int MINPLAYERS = 1;
    public static final int MAXPLAYERS = 10;
    // PLAYER STATUS
    public static final int INLOBBY = 0;
    public static final int INARENA = 1;
    // ROOM STATUS
    public static final int CLOSE = 0;
    public static final int TIMER_TOLOBBY = 1;
    public static final int STAGE_LOADING = 2;
    public static final int STAGE_STARTING = 3;
    public static final int STAGE_RUNNING = 4;
    public static final int ARENA = 5;
    public static final int ARENA_END = 6;
    // TEAMS VALUES
    public static final int TEAM_BLUE = 1;
    public static final int TEAM_RED = 2;
    public static final int[] TEAMS = {
            SnowWar.TEAM_BLUE,
            SnowWar.TEAM_RED
    };
    public static SnowWarArenaBase[] ArenaTypes = {
            new SnowWarArena8(),
            new SnowWarArena9(),
            new SnowWarArena11()
    };
    public static HashMap<Integer, SnowWarPlayerData> PLAYERS;
}
