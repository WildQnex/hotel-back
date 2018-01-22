package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {

    @Override
    public boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personsAmount) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_RESERVE_APARTMENT)) {
            ps.setDate(1, Date.valueOf(checkInDate), Calendar.getInstance());
            ps.setDate(2, Date.valueOf(checkOutDate), Calendar.getInstance());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, personsAmount);
            ps.setBigDecimal(5, apartment.getApartmentClass().getCostPerPerson());
            ps.setBigDecimal(6, apartment.getApartmentClass().getCostPerNight());
            ps.setBigDecimal(7, apartment.getApartmentClass().getAnimalCost());
            ps.setBigDecimal(8, totalCost);
            ps.setLong(9, user.getId());
            ps.setLong(10, apartment.getId());
            ps.setLong(11, 1);
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isApartmentAvailable(Apartment apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException {
        if (checkInDate.compareTo(checkOutDate) >= 0) {
            return false;
        }
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery.SQL_CHECK_AVAILABILITY);
            Date checkIn = Date.valueOf(checkInDate);
            Date checkOut = Date.valueOf(checkOutDate);
            ps.setDate(1, checkIn, Calendar.getInstance());
            ps.setDate(2, checkOut, Calendar.getInstance());
            ps.setDate(3, checkIn, Calendar.getInstance());
            ps.setDate(4, checkOut, Calendar.getInstance());
            ps.setLong(5, apartment.getId());
            ps.setString(6, Status.APPROVED.toString());
            ps.setString(7, Status.WAITING_FOR_APPROVE.toString());
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Reservation readReservationById(long id) throws DaoException{
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery.SQL_SEECT_RESERVATION_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            return getReservations(resultSet).get(0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservations() throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement ps = connection.createStatement();
            ResultSet resultSet = ps.executeQuery(SqlQuery.SQL_SELECT_ALL_RESERVATIONS);
            return getReservations(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservationsByStatus(Status status) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery.SQL_SELECT_ALL_RESERVATIONS_BY_STATUS);
            ps.setString(1, status.toString());
            ResultSet resultSet = ps.executeQuery();
            return getReservations(resultSet);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private List<Reservation> getReservations(ResultSet resultSet) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                    resultSet.getString("middle_name"), resultSet.getString("last_name"),
                    new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                    resultSet.getString("phone_number"), resultSet.getString("password"),
                    Role.valueOf(resultSet.getString("role").toUpperCase()),
                    resultSet.getInt("active") != 0);
            Apartment apartment = new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                    resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                    resultSet.getInt("smoking_allowed") != 0,
                    new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                            resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                            resultSet.getBigDecimal("apartment_cost_per_night"), resultSet.getBigDecimal("apartment_cost_per_person"),
                            resultSet.getBigDecimal("apartment_animal_cost"), resultSet.getString("image_path")));
            Status status = Status.valueOf(resultSet.getString("status").toUpperCase());
            Reservation reservation = new Reservation(resultSet.getLong("id_reservation"), resultSet.getDate("check_in_date").toLocalDate(),
                    resultSet.getDate("check_out_date").toLocalDate(), resultSet.getTimestamp("order_time").toLocalDateTime(),
                    resultSet.getInt("person_amount"), resultSet.getBigDecimal("reservation_cost_per_person"),
                    resultSet.getBigDecimal("reservation_cost_per_night"), resultSet.getBigDecimal("reservation_animal_cost"),
                    resultSet.getBigDecimal("total_cost"), user, apartment, status);
            reservations.add(reservation);
        }
        return reservations;
    }

    @Override
    public boolean updateReservation(Reservation reservation) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_UPDATE_RESERVATION)) {
            ps.setDate(1, Date.valueOf(reservation.getCheckInDate()), Calendar.getInstance());
            ps.setDate(2, Date.valueOf(reservation.getCheckOutDate()), Calendar.getInstance());
            ps.setTimestamp(3, Timestamp.valueOf(reservation.getOrderTime()));
            ps.setInt(4, reservation.getPersonAmount());
            ps.setBigDecimal(5, reservation.getCostPerNight());
            ps.setBigDecimal(6, reservation.getCostPerNight());
            ps.setBigDecimal(7, reservation.getAnimalCost());
            ps.setBigDecimal(8, reservation.getTotalCost());
            ps.setLong(9, reservation.getUser().getId());
            ps.setLong(10, reservation.getApartment().getId());
            ps.setString(11, reservation.getStatus().toString());
            ps.setLong(12, reservation.getId());
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}

