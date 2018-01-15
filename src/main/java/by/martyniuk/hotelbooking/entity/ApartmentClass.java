package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentClass {
    private long id;
    private String type;
    private int roomsAmount;
    private int maxCapacity;
    private int costPerNight;
    private int costPerPerson;
    private int animalCost;
    private String imagePath;
}
