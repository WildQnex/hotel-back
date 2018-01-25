package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.UserDao;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    @Override
    public List<User> findAllUsers() throws DaoException {
        List<User> users = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SqlQuery.SQL_SELECT_ALL_USERS);
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                        resultSet.getString("middle_name"), resultSet.getString("last_name"),
                        new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                        resultSet.getString("phone_number"), resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()),
                        resultSet.getInt("active") != 0));
            }
        } catch (SQLException e) {
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
        return users;
    }


    @Override
    public Optional<User> findUserByMail(String mail) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_FIND_USER_BY_MAIL)) {
            ps.setString(1, mail);
            ResultSet resultSet = ps.executeQuery();
            Optional<User> user = Optional.empty();
            if (resultSet.next()) {
                user = Optional.of(new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                        resultSet.getString("middle_name"), resultSet.getString("last_name"),
                        new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                        resultSet.getString("phone_number"), resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()),
                        resultSet.getInt("active") != 0));
            }
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean addUser(User user) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_INSERT_USER)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ps.setBigDecimal(4, user.getBalance());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getPassword());
            ps.setString(8, user.getRole().toString());
            ps.setInt(9, (user.isActive()) ? 1 : 0);
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean updateUser(User user) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_UPDATE_USER)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getMiddleName());
            ps.setString(3, user.getLastName());
            ps.setBigDecimal(4, user.getBalance());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getPassword());
            ps.setInt(8, (user.isActive()) ? 1 : 0);
            ps.setString(9, user.getRole().toString());
            ps.setLong(10, user.getId());
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
