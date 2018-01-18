package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentClass implements Serializable, Cloneable {
    private long id;
    private String type;
    private int roomsAmount;
    private int maxCapacity;
    private BigDecimal costPerNight;
    private BigDecimal costPerPerson;
    private BigDecimal animalCost;
    private String imagePath;
}
