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
import java.util.Properties;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ConnectionPool {

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);
    private static final Driver DRIVER;

    static {
        try {
            DRIVER = new Driver();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private BlockingDeque<ProxyConnection> emptyConnectionQueue;
    private BlockingDeque<ProxyConnection> busyConnectionQueue;

    public static int poolSize = 10;
    private static final String JDBC_URL = "jdbc:mysql://localhost/hotel_booking?useUnicode=true&useJDBCCompliantTimezoneShift=true&useSSL=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";


    private ConnectionPool(final int poolSize) throws ConnectionPoolException {
        try {
            makeConnection(poolSize);
        } catch (SQLException e) {
            throw new ConnectionPoolException("Failed to get connection.", e);
        }
    }

    public void returnConnection(ProxyConnection connection) {
        busyConnectionQueue.remove(connection);
        emptyConnectionQueue.addLast(connection);
    }

    private static class ConnectionPoolHolder {
        private static ConnectionPool HOLDER_INSTANCE;

        static {
            try {
                DriverManager.registerDriver(DRIVER);
                HOLDER_INSTANCE = new ConnectionPool(poolSize);
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
        busyConnectionQueue.addLast(connection);
        return connection;
    }

    public void destroy() {
        int count = 0;
        for (ProxyConnection connection : emptyConnectionQueue) {
            connection.reallyClose();
            count++;
        }
        try {
            DriverManager.deregisterDriver(DRIVER);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO,"Can't close connection", e);
        }
        LOGGER.log(Level.INFO,"Connections in the amount of " + count + " pieces successfully closed.");
    }

    private void makeConnection(final int POOL_SIZE) throws SQLException {
        emptyConnectionQueue = new LinkedBlockingDeque<>(POOL_SIZE);
        busyConnectionQueue = new LinkedBlockingDeque<>(POOL_SIZE);
        Properties properties = new Properties();
        try{
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        } catch (IOException e){
            LOGGER.log(Level.INFO, "Can't open property file");
            throw new RuntimeException(e);
        }
        for (int i = 0; i < POOL_SIZE; i++) {
            Connection connection = DriverManager.getConnection(JDBC_URL, properties.getProperty("jdbc.username"),
                    properties.getProperty("jdbc.password"));
            emptyConnectionQueue.offer(new ProxyConnection(connection));
        }
    }
}
