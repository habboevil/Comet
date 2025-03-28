package com.cometproject.server.storage.queries.rooms;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomDiagonalType;
import com.cometproject.api.game.rooms.RoomProcessingType;
import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.models.CustomFloorMapData;
import com.cometproject.api.game.rooms.settings.*;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.storage.api.data.rooms.RoomData;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.map.ListOrderedMap;
import com.cometproject.api.game.rooms.settings.RoomTradeState;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

public class RoomDao {
    private static final Type STRING_LIST_TYPE = new TypeToken<List<String>>() {
    }.getType();

    public static Map<String, StaticRoomModel> getModels() {
        Map<String, StaticRoomModel> data = new HashMap<>();

        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM room_models", sqlConnection);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                data.put(resultSet.getString("id"), new StaticRoomModel(resultSet));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static IRoomData getRoomDataById(int id) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE id = ? LIMIT 1", sqlConnection)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return roomDataFromResultSet(resultSet);
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return null;
    }

    public static Map<Integer, IRoomData> getRoomsByPlayerId(int playerId) {
        final Map<Integer, IRoomData> rooms = new ListOrderedMap<>();

        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE owner_id = ?", sqlConnection)) {

            preparedStatement.setInt(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.put(resultSet.getInt("id"), roomDataFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
        return rooms;
    }

    public static Map<Integer, IRoomData> getRoomsWithRightsByPlayerId(int playerId) {
        final Map<Integer, IRoomData> rooms = new ListOrderedMap<>();

        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare(
                     "SELECT * FROM rooms " +
                             "WHERE id IN (SELECT room_id FROM room_rights WHERE player_id = ?) AND group_id = 0 " +
                             "ORDER BY id DESC, name ASC LIMIT 50", sqlConnection)) {

            preparedStatement.setInt(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.put(resultSet.getInt("id"), roomDataFromResultSet(resultSet));
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
        return rooms;
    }

    public static List<IRoomData> getRoomsByQuery(String query) {
        List<IRoomData> rooms = new ArrayList<>();

        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = createPreparedStatement(query, sqlConnection);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                rooms.add(roomDataFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return rooms;
    }

    private static PreparedStatement createPreparedStatement(String query, Connection connection) throws SQLException {
        if (query.equals("owner:")) {
            return connection.prepareStatement("SELECT * FROM rooms WHERE FALSE");
        }

        if (query.startsWith("owner:")) {
            String ownerName = query.split("owner:")[1];
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rooms WHERE owner = ? ORDER BY name ASC");
            preparedStatement.setString(1, ownerName);
            return preparedStatement;
        }

        if (query.startsWith("tag:")) {
            String tagName = SqlHelper.escapeWildcards(query.split("tag:")[1]) + "%";
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rooms WHERE tags LIKE ? ORDER BY id DESC, name ASC");
            preparedStatement.setString(1, tagName);
            return preparedStatement;
        }

        if (query.startsWith("group:")) {
            String groupName = SqlHelper.escapeWildcards(query.split("group:")[1]) + "%";
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rooms WHERE group_id IN (SELECT id FROM groups WHERE name LIKE ?) ORDER BY id DESC, name ASC LIMIT 50");
            preparedStatement.setString(1, groupName);
            return preparedStatement;
        }

        query = "%" + SqlHelper.escapeWildcards(query) + "%";
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rooms WHERE name LIKE ? ORDER BY id DESC, name ASC LIMIT 50");
        preparedStatement.setString(1, query);
        return preparedStatement;
    }

    public static boolean transferRoom(int roomId, int ownerId, String username) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET owner_id = ?, owner = ? WHERE id = ?", sqlConnection)) {

            preparedStatement.setInt(1, ownerId);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, roomId);

            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
        return false;
    }

    public static int createRoom(String name, CustomFloorMapData model, String description, int category, int maxVisitors, RoomTradeState tradeState, int creationTime, int userId, String username, int wallThickness, int floorThickness, String decorations, boolean hideWalls) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = createInsertStatement(sqlConnection, name, model, description, category, maxVisitors, tradeState, creationTime, userId, username, wallThickness, floorThickness, decorations, hideWalls)) {

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return 0;
    }

    private static PreparedStatement createInsertStatement(Connection connection, String name, CustomFloorMapData model, String description, int category, int maxVisitors, RoomTradeState tradeState, int creationTime, int userId, String username, int wallThickness, int floorThickness, String decorations, boolean hideWalls) throws SQLException {
        String insertSql = "INSERT into rooms (owner_id, owner, name, heightmap, description, category, max_users, trade_state, creation_time, thickness_wall, thickness_floor, decorations, hide_walls) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, JsonUtil.getInstance().toJson(model));
        preparedStatement.setString(5, description);
        preparedStatement.setInt(6, category);
        preparedStatement.setInt(7, maxVisitors);
        preparedStatement.setString(8, tradeState.toString());
        preparedStatement.setInt(9, creationTime);
        preparedStatement.setInt(10, wallThickness);
        preparedStatement.setInt(11, floorThickness);
        preparedStatement.setString(12, decorations == null ? "" : decorations);
        preparedStatement.setString(13, hideWalls ? "1" : "0");

        return preparedStatement;
    }

    public static int createRoom(String name, String model, String description, int category, int maxVisitors, RoomTradeState tradeState, int creationTime, int userId, String username) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = createInsertStatement(sqlConnection, name, model, description, category, maxVisitors, tradeState, creationTime, userId, username)) {

            preparedStatement.execute();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                while (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return 0;
    }

    private static PreparedStatement createInsertStatement(Connection connection, String name, String model, String description, int category, int maxVisitors, RoomTradeState tradeState, int creationTime, int userId, String username) throws SQLException {
        String insertSql = "INSERT into rooms (owner_id, owner, name, model, description, category, max_users, trade_state, creation_time, thickness_floor, thickness_wall) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, userId);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, name);
        preparedStatement.setString(4, model);
        preparedStatement.setString(5, description);
        preparedStatement.setInt(6, category);
        preparedStatement.setInt(7, maxVisitors);
        preparedStatement.setString(8, tradeState.toString());
        preparedStatement.setInt(9, creationTime);
        preparedStatement.setInt(10, 0);
        preparedStatement.setInt(11, 0);

        return preparedStatement;
    }

    public static void deleteRoom(int roomId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("DELETE FROM rooms WHERE id = ?", sqlConnection)) {

            preparedStatement.setInt(1, roomId);
            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static List<IRoomData> getHighestScoredRooms() {
        List<IRoomData> roomData = new ArrayList<>();

        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM rooms ORDER BY score DESC LIMIT 50", sqlConnection);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                roomData.add(roomDataFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return roomData;
    }

    public static void getActivePromotions(Map<Integer, RoomPromotion> roomPromotions) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM rooms_promoted WHERE time_expire > ?", sqlConnection)) {

            preparedStatement.setLong(1, Comet.getTime());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    roomPromotions.put(resultSet.getInt("room_id"), new RoomPromotion(resultSet.getInt("room_id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getLong("time_start"), resultSet.getLong("time_expire")));
                }
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void updatePromotedRoom(RoomPromotion roomPromotion) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms_promoted SET name = ?, description = ?, time_expire = ? WHERE room_id = ?", sqlConnection)) {

            preparedStatement.setString(1, roomPromotion.getPromotionName());
            preparedStatement.setString(2, roomPromotion.getPromotionDescription());
            preparedStatement.setLong(3, roomPromotion.getTimestampFinish());
            preparedStatement.setInt(4, roomPromotion.getRoomId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void createPromotedRoom(RoomPromotion roomPromotion) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("INSERT into rooms_promoted (room_id, name, description, time_start, time_expire) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name), description = VALUES(description), time_start = VALUES(time_start), time_expire = VALUES(time_expire);", sqlConnection)) {

            preparedStatement.setInt(1, roomPromotion.getRoomId());
            preparedStatement.setString(2, roomPromotion.getPromotionName());
            preparedStatement.setString(3, roomPromotion.getPromotionDescription());
            preparedStatement.setLong(4, roomPromotion.getTimestampStart());
            preparedStatement.setLong(5, roomPromotion.getTimestampFinish());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void deleteExpiredRoomPromotions() {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("DELETE FROM rooms_promoted WHERE time_expire < ?", sqlConnection)) {

            preparedStatement.setLong(1, Comet.getTime());
            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void saveUserCounts(Map<Integer, Integer> roomStatuses) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET users_now = ? WHERE `id` = ?;", sqlConnection)) {

            for (Map.Entry<Integer, Integer> room : roomStatuses.entrySet()) {
                preparedStatement.setInt(1, room.getValue());
                preparedStatement.setInt(2, room.getKey());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void updateRollerSpeedRoom(int rollerLevel, int roomId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET roller_speed_level = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, rollerLevel);
            preparedStatement.setInt(2, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void updateRollerSpeed(int roomId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET roller_speed = '1' WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, roomId);
            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void transferItems(int roomId, int ownerId, int toOwnerId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE items SET `user_id` = ? WHERE `user_id` = ? AND `room_id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, toOwnerId);
            preparedStatement.setInt(2, ownerId);
            preparedStatement.setInt(3, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void removeItems(int roomId, int ownerId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("DELETE FROM items WHERE room_id = ? AND user_id = ? AND deleteable = 1", sqlConnection)) {

            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, ownerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void removeNonOwnerItems(int roomId, int ownerId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE items SET room_id = 0 WHERE `user_id` <> ? AND `room_id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, ownerId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void changeRoomPrice(int roomId, int price, int userBuyerId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET `room_price` = ?, `room_buyer` = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, userBuyerId);
            preparedStatement.setInt(3, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void changeRoomOwner(int roomId, int newOwnerUserId, String username) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET `owner_id` = ?, `owner` = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, newOwnerUserId);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void setRoomDiagonal(int roomId, boolean enable) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET room_diagonal = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setString(1, enable ? "1" : "0");
            preparedStatement.setInt(2, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void roomUpdateSongId(int roomId, int songId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET song_id = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setInt(1, songId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void updateRoomName(String name, int roomId) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("UPDATE rooms SET name = ? WHERE `id` = ?", sqlConnection)) {

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, roomId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    private static RoomData roomDataFromResultSet(final ResultSet room) throws SQLException {
        final int id = room.getInt("id");
        final RoomType type = RoomType.valueOf(room.getString("type"));
        final String name = room.getString("name");
        final String description = room.getString("description");
        final int ownerId = room.getInt("owner_id");
        final String owner = room.getString("owner");
        final int category = room.getInt("category");
        final int maxUsers = room.getInt("max_users");
        final String thumbnail = room.getString("thumbnail");
        final String accessTypeString = room.getString("access_type");
        final String password = room.getString("password");
        final RoomAccessType access = RoomAccessType.valueOf(accessTypeString.toUpperCase());
        final int score = room.getInt("score");
        final String[] tags = room.getString("tags").isEmpty() ? new String[0] :
                room.getString("tags").split(",");
        final Map<String, String> decorations = new HashMap<>();
        String[] decorationsArray = room.getString("decorations").split(",");
        fillDecorations(decorations, decorationsArray);

        final String model = room.getString("model");
        final boolean hideWalls = room.getString("hide_walls").equals("1");
        final int thicknessWall = room.getInt("thickness_wall");
        final int thicknessFloor = room.getInt("thickness_floor");
        final boolean allowWalkthrough = room.getString("allow_walkthrough").equals("1");
        final boolean allowPets = room.getString("allow_pets").equals("1");
        final String heightmap = room.getString("heightmap");
        final RoomTradeState tradeState = RoomTradeState.valueOf(room.getString("trade_state"));
        final int creationTime = room.getInt("creation_time");

        final RoomKickState kickState = RoomKickState.valueOf(room.getString("kick_state"));
        final RoomBanState banState = RoomBanState.valueOf(room.getString("ban_state"));
        final RoomMuteState muteState = RoomMuteState.valueOf(room.getString("mute_state"));

        final int bubbleMode = room.getInt("bubble_mode");
        final int bubbleScroll = room.getInt("bubble_scroll");
        final int bubbleType = room.getInt("bubble_type");
        final int antiFloodSettings = room.getInt("flood_level");
        final int chatDistance = room.getInt("chat_distance");

        final List<String> disabledCommands = JsonUtil.getInstance().fromJson(room.getString("disabled_commands"), STRING_LIST_TYPE);
        final int groupId = room.getInt("group_id");
        final String requiredBadge = room.getString("required_badge");
        final boolean wiredHidden = room.getString("hide_wired").equals("1");
        final int rollerSpeedLevel = room.getInt("roller_speed_level");
        final RoomDiagonalType roomDiagonal = RoomDiagonalType.parse(room.getString("room_diagonal"));
        final RoomProcessingType processingType = RoomProcessingType.parse(room.getString("processing_type"));
        final int songId = room.getInt("song_id");
        final int roomPrice = room.getInt("room_price");
        final int roomBuyer = room.getInt("room_buyer");

        return new RoomData(id, type, name, description, ownerId, owner, category, maxUsers, access, password,
                password, tradeState, creationTime, score, tags, decorations, model, hideWalls, thicknessWall, thicknessFloor,
                allowWalkthrough, allowPets, heightmap, muteState, kickState, banState, bubbleMode, bubbleType,
                bubbleScroll, chatDistance, antiFloodSettings, disabledCommands, groupId,
                requiredBadge, thumbnail, wiredHidden, rollerSpeedLevel, roomDiagonal, songId, roomPrice, roomBuyer, processingType);
    }

    private static void fillDecorations(Map<String, String> decorations, String[] decorationsArray) {
        for (final String s : decorationsArray) {
            final String[] decoration = s.split("=");

            if (decoration.length == 2)
                decorations.put(decoration[0], decoration[1]);
        }
    }
}