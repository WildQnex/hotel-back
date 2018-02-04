package by.martyniuk.hotelbooking.pool;

import by.martyniuk.hotelbooking.exception.ConnectionPoolException;
import com.mysql.cj.jdbc.Driver;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ConnectionPool {

    public static boolean isTest = false;

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static final Properties PROPERTIES = new Properties();
    private static final Driver DRIVER;
    static {
        try {
            DRIVER = new Driver();
            PROPERTIES.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        } catch (SQLException | IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private BlockingDeque<ProxyConnection> emptyConnectionQueue;
    private List<ProxyConnection> busyConnectionList;


    private ConnectionPool() throws ConnectionPoolException {
        try {
            initConnectionPool();
        } catch (SQLException e) {
            throw new ConnectionPoolException("Failed to get connection.", e);
        }
    }

    public void returnConnection(Connection connection) {
        if (busyConnectionList.remove(connection)) {
            emptyConnectionQueue.addLast((ProxyConnection) connection);
        }
    }

    private static class ConnectionPoolHolder {
        private static ConnectionPool HOLDER_INSTANCE;

        static {
            try {
                DriverManager.registerDriver(DRIVER);
                HOLDER_INSTANCE = new ConnectionPool();
            } catch (SQLException | ConnectionPoolException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static ConnectionPool getInstance() {
        return ConnectionPoolHolder.HOLDER_INSTANCE;
    }

    public Connection getConnection() {
        ProxyConnection connection = emptyConnectionQueue.poll();
        busyConnectionList.add(connection);
        return connection;
    }

    public void destroy() {
        closeConnections();
        try {
            DriverManager.deregisterDriver(DRIVER);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Can't close connection", e);
        }
    }

    public int getAmountFreeConnections() {
        return emptyConnectionQueue.size();
    }

    public int getAmountBusyConnections() {
        return busyConnectionList.size();
    }

    private void closeConnections() {
        int count = 0;
        for (ProxyConnection connection : emptyConnectionQueue) {
            connection.reallyClose();
            count++;
        }
        for (ProxyConnection connection : busyConnectionList) {
            connection.reallyClose();
            count++;
        }
        LOGGER.log(Level.INFO, "Connections in the amount of " + count + " where successfully closed.");
    }

    private void initConnectionPool() throws SQLException {
        int poolSize = Integer.parseInt(PROPERTIES.getProperty("pool.size"));
        emptyConnectionQueue = new LinkedBlockingDeque<>(poolSize);
        busyConnectionList = new ArrayList<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            Connection connection;
            if(isTest) {
                connection = DriverManager.getConnection(PROPERTIES.getProperty("jdbc.database.test.url"), PROPERTIES.getProperty("jdbc.username"),
                        PROPERTIES.getProperty("jdbc.password"));
            } else {
                connection = DriverManager.getConnection(PROPERTIES.getProperty("jdbc.database.url"), PROPERTIES.getProperty("jdbc.username"),
                        PROPERTIES.getProperty("jdbc.password"));
            }
            emptyConnectionQueue.offer(new ProxyConnection(connection));
        }
    }
}
