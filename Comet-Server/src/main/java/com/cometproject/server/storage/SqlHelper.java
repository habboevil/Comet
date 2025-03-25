package com.cometproject.server.storage;

import com.cometproject.storage.mysql.MySQLConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SqlHelper {
    private static final Logger log = LogManager.getLogger(SqlHelper.class.getName());
    private static final Map<String, AtomicInteger> queryCounters = new ConcurrentHashMap<>();
    public static boolean queryLogEnabled = false;
    public static Map<Integer, QueryLog> queryLog = new ConcurrentHashMap<>();
    private static MySQLConnectionProvider connectionProvider;

    public static void init(MySQLConnectionProvider connectionProvider) {
        SqlHelper.connectionProvider = connectionProvider;
    }

    public static Connection getConnection() throws SQLException {
        try {
            return connectionProvider.getConnection();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieving connection", e);
            return null;
        }
    }

    public static void closeSilently(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                handleSqlException(e);
            }
        }
    }

    public static void closeSilently(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                handleSqlException(e);
            }
        }
    }

    public static void closeSilently(PreparedStatement statement) {
        if (statement != null) {
            try {
                if (queryLogEnabled && queryLog.containsKey(statement.hashCode())) {
                    final QueryLog logEntry = queryLog.get(statement.hashCode());
                    final long timeTaken = (System.currentTimeMillis() - logEntry.startTime);

                    log.debug("[QUERY] {} took {}ms", logEntry.query, timeTaken);

                    queryLog.remove(statement.hashCode());
                }

                statement.close();
            } catch (SQLException e) {
                handleSqlException(e);
            }
        }
    }

    public static void executeStatementSilently(PreparedStatement statement, boolean autoClose) {
        if (statement != null) {
            try {
                statement.execute();

                if (autoClose) {
                    statement.close();
                }
            } catch (SQLException e) {
                handleSqlException(e);
            }
        }
    }

    public static PreparedStatement prepare(String query, Connection con) throws SQLException {
        return prepare(query, con, false);
    }

    public static PreparedStatement prepare(String query, Connection con, boolean returnKeys) throws SQLException {
        if (Thread.currentThread().getName().startsWith("Room-Processor")) {
            log.trace("Executing query from room processor: {}", query);
        }

        queryCounters.computeIfAbsent(query, k -> new AtomicInteger()).incrementAndGet();

        final PreparedStatement statement = returnKeys ?
                con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS) :
                con.prepareStatement(query);

        if (queryLogEnabled) {
            final QueryLog logEntry = new QueryLog();
            logEntry.query = query;
            queryLog.put(statement.hashCode(), logEntry);
        }

        return statement;
    }

    public static void handleSqlException(SQLException e) {
        if (!("Pool has been shutdown".equals(e.getMessage()) || e.getMessage().contains("Data too long for column"))) {
            log.error("Error while executing query", e);
        }
    }

    public static String escapeWildcards(String s) {
        return s.replaceAll("_", "\\\\_").replaceAll("%", "\\\\%");
    }

    public static Map<String, AtomicInteger> getQueryCounters() {
        return queryCounters;
    }

    public static class QueryLog {
        public long startTime = System.currentTimeMillis();
        public String query;
    }
}
