package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    private long id;
    private String number;
    private int floor;
    private boolean isAnimalsAllowed;
    private boolean isSmokingAllowed;
    private ApartmentClass apartmentClass;
}
