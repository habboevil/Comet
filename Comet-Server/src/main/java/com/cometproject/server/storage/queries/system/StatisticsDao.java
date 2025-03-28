package com.cometproject.server.storage.queries.system;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDao {
    public static void saveStatistics(int players, int rooms, String version, int onlineRecord) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare(
                     "UPDATE server_status SET active_players = ?, active_rooms = ?, server_version = ?, player_record = ?, player_record_timestamp = ?",
                     sqlConnection)) {

            preparedStatement.setInt(1, players);
            preparedStatement.setInt(2, rooms);
            preparedStatement.setString(3, version);
            preparedStatement.setInt(4, onlineRecord);
            preparedStatement.setInt(5, (int) Comet.getTime());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static void saveStatistics(int players, int rooms, String version) {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare(
                     "UPDATE server_status SET active_players = ?, active_rooms = ?, server_version = ?",
                     sqlConnection)) {

            preparedStatement.setInt(1, players);
            preparedStatement.setInt(2, rooms);
            preparedStatement.setString(3, version);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }
    }

    public static int getPlayerRecord() {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare(
                     "SELECT player_record FROM server_status LIMIT 1",
                     sqlConnection);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("player_record");
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        }

        return 0;
    }
}