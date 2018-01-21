package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
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

public class ApartmentClassDaoImpl implements ApartmentClassDao {

    private static final String SQL_SELECT_ALL_APARTMENTS_CLASSES = "SELECT `id_apartment_class`, `type`, `rooms_amount`," +
            " `max_capacity`, `cost_per_night`, `cost_per_person`, `animal_cost`, `image_path` FROM `hotel_booking`.`apartment_class`";

    private static final String SQL_FIND_APARTMENT_CLASS_BY_ID = "SELECT `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `cost_per_night`, `cost_per_person`," +
            " `animal_cost`, `image_path` FROM `hotel_booking`.`apartment_class` " +
            " WHERE `id_apartment_class` = ?";


    public List<ApartmentClass> findAllApartmentClasses() throws DaoException {
        List<ApartmentClass> apartmentClassList = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SQL_SELECT_ALL_APARTMENTS_CLASSES);
            while (resultSet.next()) {
                apartmentClassList.add(new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                        resultSet.getBigDecimal("animal_cost"), resultSet.getString("image_path")));
            }
            return apartmentClassList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public ApartmentClass findApartmentClassById(long id) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SQL_FIND_APARTMENT_CLASS_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                return new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                        resultSet.getBigDecimal("animal_cost"), resultSet.getString("image_path"));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }
}