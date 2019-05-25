package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Class ApartmentClassDaoImpl.
 */
@Repository
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
    public boolean updateApartmentClass(ApartmentClass apartmentClass) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_UPDATE_APARTMENT_CLASS);
            ps.setString(1, apartmentClass.getType());
            ps.setInt(2, apartmentClass.getRoomsAmount());
            ps.setInt(3, apartmentClass.getMaxCapacity());
            ps.setBigDecimal(4, apartmentClass.getCostPerNight());
            ps.setBigDecimal(5, apartmentClass.getCostPerPerson());
            ps.setString(6, apartmentClass.getDescription());
            ps.setString(7, apartmentClass.getImagePath());
            ps.setLong(8, apartmentClass.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean addApartmentClass(ApartmentClass apartmentClass) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_ADD_APARTMENT_CLASS);
            ps.setString(1, apartmentClass.getType());
            ps.setInt(2, apartmentClass.getRoomsAmount());
            ps.setInt(3, apartmentClass.getMaxCapacity());
            ps.setBigDecimal(4, apartmentClass.getCostPerNight());
            ps.setBigDecimal(5, apartmentClass.getCostPerPerson());
            ps.setString(6, apartmentClass.getDescription());
            ps.setString(7, apartmentClass.getImagePath());
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean deleteApartmentClass(long id) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_DELETE_APARTMENT_CLASS);
            ps.setLong(1, id);
            return ps.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}