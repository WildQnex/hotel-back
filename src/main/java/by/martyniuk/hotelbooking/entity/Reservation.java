package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Serializable, Cloneable {
    private long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime orderTime;
    private int personAmount;
    private BigDecimal costPerPerson;
    private BigDecimal costPerNight;
    private BigDecimal totalCost;
    private User user;
    private Apartment apartment;
    private Status status;
}
