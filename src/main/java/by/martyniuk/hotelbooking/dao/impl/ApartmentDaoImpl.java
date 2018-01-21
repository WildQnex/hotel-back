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

    @Override
    public List<Apartment> findAllApartments() throws DaoException {
        List<Apartment> apartments = new ArrayList<>();
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             Statement st = cn.createStatement()) {
            ResultSet resultSet = st.executeQuery(SqlQuery.SQL_SELECT_ALL_APARTMENTS);
            while (resultSet.next()) {
                apartments.add(new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                        resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                        resultSet.getInt("smoking_allowed") != 0,
                        new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                                resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                                resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                                resultSet.getBigDecimal("animal_cost"), resultSet.getString("image_path"))));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return apartments;
    }


    @Override
    public Apartment findApartmentById(long id) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_FIND_APARTMENT_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                        resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                        resultSet.getInt("smoking_allowed") != 0,
                        new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                                resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                                resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                                resultSet.getBigDecimal("animal_cost"), resultSet.getString("image_path")));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return null;
    }

    @Override
    public List<Apartment> findApartmentListByClassId(long id) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            List<Apartment> apartmentList = new ArrayList<>();
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_FIND_APARTMENT_BY_CLASS_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                apartmentList.add(new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                        resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                        resultSet.getInt("smoking_allowed") != 0,
                        new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                                resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                                resultSet.getBigDecimal("cost_per_night"), resultSet.getBigDecimal("cost_per_person"),
                                resultSet.getBigDecimal("animal_cost"), resultSet.getString("image_path"))));
            }
            return apartmentList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean addApartment(Apartment apartment) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_INSERT_APARTMENT)) {
            ps.setString(1, apartment.getApartmentClass().toString());
            ps.setString(2, apartment.getNumber());
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
