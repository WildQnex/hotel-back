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

public class UserDaoImpl implements UserDao {

    private static final String SQL_SELECT_ALL_USERS = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `hotel_booking`.`user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk`";

    private static final String SQL_FIND_USER_BY_MAIL = "SELECT `id_user`, `first_name`, `middle_name`, `last_name`, `balance`, `email`, " +
            "`phone_number`, `password`, `role`, `active` FROM `hotel_booking`.`user` LEFT JOIN `role` " +
            "ON `role`.`id_role` = `user`.`role_id_fk` WHERE `user`.`email`	= ?";

    private static final String SQL_INSERT_USER = "INSERT INTO `hotel_booking`.`user` (`first_name`, `middle_name`, `last_name`, " +
            "`balance`, `email`, `phone_number`, `password`, `role_id_fk`, `active`)  VALUES (?,?,?,?,?,?,?,(SELECT `id_role` FROM `role` WHERE UPPER(`role`) LIKE UPPER(?)),?)";

    @Override
    public List<User> findAllUsers() throws DaoException {
        List<User> users = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SQL_SELECT_ALL_USERS);
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
    public User findUserByMail(String mail) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_BY_MAIL)) {
            ps.setString(1, mail);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                        resultSet.getString("middle_name"), resultSet.getString("last_name"),
                        new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                        resultSet.getString("phone_number"), resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()),
                        resultSet.getInt("active") != 0);
            }
        } catch (SQLException e) {
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
        return null;
    }

    @Override
    public boolean addUser(User user) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_USER)) {
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
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
    }
}
