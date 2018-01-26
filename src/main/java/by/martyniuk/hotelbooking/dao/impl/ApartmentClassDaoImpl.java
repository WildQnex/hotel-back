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
import java.util.Optional;

public class ApartmentClassDaoImpl implements ApartmentClassDao {

    @Override
    public List<ApartmentClass> findAllApartmentClasses() throws DaoException {
        List<ApartmentClass> apartmentClassList = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SqlQuery.SQL_SELECT_ALL_APARTMENTS_CLASSES);
            while (resultSet.next()) {
                apartmentClassList.add(new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                        resultSet.getString("description"), resultSet.getString("image_path")));
            }
            return apartmentClassList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<ApartmentClass> findApartmentClassById(long id) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_FIND_APARTMENT_CLASS_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            Optional<ApartmentClass> apartmentClassOptional = Optional.empty();
            if (resultSet.next()) {
                apartmentClassOptional = Optional.of(new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                        resultSet.getString("description"), resultSet.getString("image_path")));
            }
            return apartmentClassOptional;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<ApartmentClass> findApartmentClassByType(String type) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_FIND_APARTMENT_CLASS_BY_TYPE);
            ps.setString(1, type);
            ResultSet resultSet = ps.executeQuery();
            Optional<ApartmentClass> apartmentClassOptional = Optional.empty();
            if (resultSet.next()) {
                apartmentClassOptional = Optional.of(new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                        resultSet.getString("description"), resultSet.getString("image_path")));
            }
            return apartmentClassOptional;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}