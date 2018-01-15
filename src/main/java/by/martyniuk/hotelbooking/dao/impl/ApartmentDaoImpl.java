package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ApartmentDaoImpl implements ApartmentDao {

    private static final String SQL_SELECT_ALL_APARTMENTS = "SELECT `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment` LEFT JOIN `apartment_class` " +
            " ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`";


    private static final String SQL_FIND_APARTMENT = "SELECT * FROM apartment WHERE `number`=?";

    private static final String SQL_INSERT_APARTMENT = "INSERT INTO apartment(class,`number`) VALUES(?,?)";

    @Override
    public List<Apartment> findAllApartments() throws DaoException {
        List<Apartment> apartments = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SQL_SELECT_ALL_APARTMENTS);
            while (resultSet.next()) {
                apartments.add(new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                        resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                        resultSet.getInt("smoking_allowed") != 0,
                        new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                                resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                                resultSet.getInt("cost_per_night"), resultSet.getInt("cost_per_person"),
                                resultSet.getInt("animal_cost"), resultSet.getString("image_path"))));
            }
        } catch (SQLException e) {
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
        return apartments;
    }


    @Override
    public Apartment findApartmentByNumber(String number) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_FIND_APARTMENT)) {
            ps.setString(1, number);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                //return new Apartment(resultSet.getLong("id"), ApartmentClass.valueOf(resultSet.getNString("class").toUpperCase()), resultSet.getNString("number"));
            }
        } catch (SQLException e) {
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
        return null;
    }

    @Override
    public boolean addApartment(Apartment apartment) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_INSERT_APARTMENT)) {
            ps.setString(1, apartment.getApartmentClass().toString());
            ps.setString(2, apartment.getNumber());
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException("SQL exception (request or table failed): " + e, e);
        }
    }
}
