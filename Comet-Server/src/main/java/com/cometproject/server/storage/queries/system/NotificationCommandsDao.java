package com.cometproject.server.storage.queries.system;

import com.cometproject.server.game.commands.notifications.types.Notification;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NotificationCommandsDao {
    public static Map<String, Notification> getAll() {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM command_notifications", sqlConnection);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            Map<String, Notification> data = new HashMap<>();

            while (resultSet.next()) {
                data.put(resultSet.getString("name"), new Notification(resultSet));
            }

            return data;
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
            return new HashMap<>();
        }
    }
}