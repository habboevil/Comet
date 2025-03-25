package com.cometproject.server.storage.queries.chat;

import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrefixDao {
    public static List<String> getAll() {
        try (Connection sqlConnection = SqlHelper.getConnection();
             PreparedStatement preparedStatement = sqlConnection.prepareStatement("SELECT value FROM filter_prefix");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<String> data = new ArrayList<>();

            while (resultSet.next()) {
                data.add(resultSet.getString("value"));
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
