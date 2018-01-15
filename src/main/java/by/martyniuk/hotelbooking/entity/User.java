package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, Cloneable {
    private long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private BigDecimal balance;
    private String email;
    private String phoneNumber;
    private String password;
    private Role role;
    private boolean active;
}
