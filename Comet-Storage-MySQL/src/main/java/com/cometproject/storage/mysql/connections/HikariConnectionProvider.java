package com.cometproject.storage.mysql.connections;

import com.cometproject.api.config.Configuration;
import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HikariConnectionProvider extends MySQLConnectionProvider {

    private static final Logger LOGGER = LogManager.getLogger(HikariConnectionProvider.class.getName());

    private HikariDataSource hikariDataSource;

    public HikariConnectionProvider() {
        try {
            String dbHost = Configuration.currentConfig().get("comet.db.host");
            String dbName = Configuration.currentConfig().get("comet.db.name");
            String dbUsername = Configuration.currentConfig().get("comet.db.username");
            String dbPassword = Configuration.currentConfig().get("comet.db.password");
            String dbPort = Configuration.currentConfig().get("comet.db.port");
            String dbParams = Configuration.currentConfig().get("comet.db.params");

            HikariConfig databaseConfiguration = new HikariConfig();
            databaseConfiguration.setMaximumPoolSize(350);
            databaseConfiguration.setMinimumIdle(10);

            StringBuilder jdbcUrlBuilder = new StringBuilder();
            jdbcUrlBuilder.append("jdbc:mysql://")
                    .append(dbHost)
                    .append(":")
                    .append(dbPort)
                    .append("/")
                    .append(dbName)
                    .append(dbParams);
            String jdbcUrl = jdbcUrlBuilder.toString();
            LOGGER.info(jdbcUrl);

            databaseConfiguration.setJdbcUrl(jdbcUrl);
            databaseConfiguration.setUsername(dbUsername);
            databaseConfiguration.setPassword(dbPassword);

            this.hikariDataSource = new HikariDataSource(databaseConfiguration);

        } catch (Exception e) {
            LOGGER.error("Failed to connect to MySQL server", e);
            throw new RuntimeException("Failed to initialize HikariDataSource", e);
        }
    }

    @Override
    public Connection getConnection() throws Exception {
        return this.hikariDataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close Hikari connection", e);
        }
    }

    @Override
    public void closeStatement(PreparedStatement statement) {
        try {
            statement.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close prepared statement", e);
        }
    }

    @Override
    public void closeResults(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (Exception e) {
            LOGGER.error("Failed to close the result set");
        }
    }

    public void shutdown() {
        this.hikariDataSource.close();
    }
}