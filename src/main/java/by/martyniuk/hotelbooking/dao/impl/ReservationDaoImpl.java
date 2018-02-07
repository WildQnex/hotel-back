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
import java.util.Optional;

/**
 * The Class ReservationDaoImpl.
 */
public class ReservationDaoImpl implements ReservationDao {


    @Override
    public boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personsAmount) throws DaoException {
        Connection cn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement psAvailability = cn.prepareStatement(SqlQuery.SQL_CHECK_AVAILABILITY);
             PreparedStatement psBalance = cn.prepareStatement(SqlQuery.SQL_WITHDRAW_USER_MONEY);
             PreparedStatement psReserve = cn.prepareStatement(SqlQuery.SQL_RESERVE_APARTMENT)) {

            cn.setAutoCommit(false);

            Date checkIn = Date.valueOf(checkInDate);
            Date checkOut = Date.valueOf(checkOutDate);
            psAvailability.setDate(1, checkIn, Calendar.getInstance());
            psAvailability.setDate(2, checkOut, Calendar.getInstance());
            psAvailability.setDate(3, checkIn, Calendar.getInstance());
            psAvailability.setDate(4, checkOut, Calendar.getInstance());
            psAvailability.setLong(5, apartment.getId());
            psAvailability.setString(6, Status.APPROVED.toString());
            psAvailability.setString(7, Status.WAITING_FOR_APPROVE.toString());
            ResultSet rs = psAvailability.executeQuery();
            if (rs.next()) {
                return false;
            }

            psBalance.setBigDecimal(1, totalCost);
            psBalance.setLong(2, user.getId());
            psBalance.setBigDecimal(3, totalCost);
            boolean result = psBalance.executeUpdate() != 0;

            psReserve.setDate(1, Date.valueOf(checkInDate), Calendar.getInstance());
            psReserve.setDate(2, Date.valueOf(checkOutDate), Calendar.getInstance());
            psReserve.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psReserve.setInt(4, personsAmount);
            psReserve.setBigDecimal(5, apartment.getApartmentClass().getCostPerPerson());
            psReserve.setBigDecimal(6, apartment.getApartmentClass().getCostPerNight());
            psReserve.setBigDecimal(7, totalCost);
            psReserve.setLong(8, user.getId());
            psReserve.setLong(9, apartment.getId());
            psReserve.setLong(10, 1);

            if (result && psReserve.executeUpdate() != 0) {
                cn.commit();
                return true;
            } else {
                cn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ignored) {

            }
            throw new DaoException(e);
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException ignored) {

                }
            }
        }
    }


    @Override
    public boolean isApartmentAvailable(long apartmentId, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException {

        try (Connection cn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = cn.prepareStatement(SqlQuery.SQL_CHECK_AVAILABILITY);
            Date checkIn = Date.valueOf(checkInDate);
            Date checkOut = Date.valueOf(checkOutDate);
            ps.setDate(1, checkIn, Calendar.getInstance());
            ps.setDate(2, checkOut, Calendar.getInstance());
            ps.setDate(3, checkIn, Calendar.getInstance());
            ps.setDate(4, checkOut, Calendar.getInstance());
            ps.setLong(5, apartmentId);
            ps.setString(6, Status.APPROVED.toString());
            ps.setString(7, Status.WAITING_FOR_APPROVE.toString());
            ResultSet rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Reservation> readReservationById(long id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery.SQL_SELECT_RESERVATION_BY_ID);
            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();
            Optional<Reservation> reservationOptional = Optional.empty();
            if (resultSet.next()) {
                reservationOptional = Optional.of(getReservation(resultSet));
            }
            return reservationOptional;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservations() throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement ps = connection.createStatement();
            ResultSet resultSet = ps.executeQuery(SqlQuery.SQL_SELECT_ALL_RESERVATIONS);
            ArrayList<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(getReservation(resultSet));
            }
            return reservations;
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
            ArrayList<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(getReservation(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Reservation> readAllReservationsByUserId(long userId) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SqlQuery.SQL_SELECT_ALL_RESERVATIONS_BY_USER_ID);
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Reservation> reservations = new ArrayList<>();
            while (resultSet.next()) {
                reservations.add(getReservation(resultSet));
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Gets the reservation.
     *
     * @param resultSet the result set
     * @return the reservation
     * @throws SQLException the SQL exception
     */
    private Reservation getReservation(ResultSet resultSet) throws SQLException {
        User user = new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                resultSet.getString("middle_name"), resultSet.getString("last_name"),
                new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                resultSet.getString("phone_number"), resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role").toUpperCase()),
                resultSet.getInt("user_active") != 0);
        Apartment apartment = new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                resultSet.getInt("floor"),
                new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                        resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                        resultSet.getBigDecimal("apartment_cost_per_night"), resultSet.getBigDecimal("apartment_cost_per_person"),
                        resultSet.getString("description"), resultSet.getString("image_path")), resultSet.getInt("apartment_active") != 0);
        Status status = Status.valueOf(resultSet.getString("status").toUpperCase());
        return new Reservation(resultSet.getLong("id_reservation"), resultSet.getDate("check_in_date").toLocalDate(),
                resultSet.getDate("check_out_date").toLocalDate(), resultSet.getTimestamp("order_time").toLocalDateTime(),
                resultSet.getInt("person_amount"), resultSet.getBigDecimal("reservation_cost_per_person"),
                resultSet.getBigDecimal("reservation_cost_per_night"), resultSet.getBigDecimal("total_cost"), user, apartment, status);
    }

    @Override
    public boolean updateReservationApartmentAndStatus(Reservation reservation, Status status) throws DaoException {
        Connection cn = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement psUpdate = cn.prepareStatement(SqlQuery.SQL_UPDATE_RESERVATION_STATUS);
             PreparedStatement psUpdateBalance = cn.prepareStatement(SqlQuery.SQL_DEPOSIT_MONEY)) {
            cn.setAutoCommit(false);
            boolean result = true;

            if (status.equals(Status.DECLINED) || status.equals(Status.CANCELED)) {
                psUpdateBalance.setBigDecimal(1, reservation.getTotalCost());
                psUpdateBalance.setLong(2, reservation.getUser().getId());
                result = psUpdateBalance.executeUpdate() != 0;
            }

            psUpdate.setLong(1, reservation.getApartment().getId());
            psUpdate.setString(2, status.toString());
            psUpdate.setLong(3, reservation.getId());
            if (psUpdate.executeUpdate() != 0 && result) {
                cn.commit();
                return true;
            } else {
                cn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                cn.rollback();
            } catch (SQLException ignored) {

            }
            throw new DaoException(e);
        } finally {
            if (cn != null) {
                try {
                    cn.setAutoCommit(true);
                    cn.close();
                } catch (SQLException ignored) {

                }
            }
        }
    }

}

