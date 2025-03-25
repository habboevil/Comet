package com.cometproject.storage.mysql;

public class MySQLStorageContext {
    private static MySQLStorageContext currentContext;

    private final MySQLConnectionProvider connectionProvider;

    public MySQLStorageContext(MySQLConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public static MySQLStorageContext getCurrentContext() {
        return currentContext;
    }

    public static void setCurrentContext(MySQLStorageContext storageContext) {
        currentContext = storageContext;
    }

    public MySQLConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

}
