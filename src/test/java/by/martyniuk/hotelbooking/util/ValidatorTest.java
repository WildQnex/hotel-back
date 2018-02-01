package by.martyniuk.hotelbooking.util;

import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ValidatorTest {

    User correctUser;
    User incorrectUser;


    @BeforeClass
    public void setUp() throws Exception {
        correctUser = new User(2, "Ivan", "Ivanovich", "Ivanov", new BigDecimal("1000.00"),
                "user@gmail.com", "+375291234567", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.USER, true);

        incorrectUser = new User(2, "I", "", null, new BigDecimal("1000.00"),
                "mail", "+375291234dfs2f", "", Role.USER, true);

    }

    @Test
    public void validateUserTest() {
        assertTrue(Validator.validateUser(correctUser));
    }

    @Test
    public void validateIncorrectUserTest() {
        assertFalse(Validator.validateUser(incorrectUser));
    }

    @Test
    public void validateNameTest() {
        assertTrue(Validator.validateName(correctUser.getFirstName()));
    }

    @Test
    public void validateIncorrectNameTest() {
        assertFalse(Validator.validateName(incorrectUser.getFirstName()));
    }

    @Test
    public void validateIdTest() {
        assertTrue(Validator.validateId("2355"));
    }

    @Test
    public void validateIncorrectIdTest() {
        assertFalse(Validator.validateName("42k"));
    }

    @Test
    public void validatePasswordEqualsTest() {
        assertTrue(Validator.validatePasswords("password", "password"));
    }

    @Test
    public void validatePasswordNotEqualsTest() {
        assertFalse(Validator.validatePasswords("password", "123"));
    }

    @Test
    public void validateDateRangeTest() {
        assertTrue(Validator.validateDateRange(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5)));
    }

    @Test
    public void validateInvalidDateRangeTest() {
        assertFalse(Validator.validateDateRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(4)));
    }

    @Test
    public void validateEmailTest() {
        assertTrue(Validator.validateEmail(correctUser.getEmail()));
    }

    @Test
    public void validateIncorrectMailTest() {
        assertFalse(Validator.validateEmail(incorrectUser.getEmail()));
    }

    @Test
    public void validatePersonAmountTest() {
        assertTrue(Validator.validatePersonAmount("5"));
    }

    @Test
    public void validateIncorrectPersonAmountTest() {
        assertFalse(Validator.validatePersonAmount("322gf"));
    }

    @Test
    public void validatePhonePasswordTest() {
        assertTrue(Validator.validatePhoneNumber(correctUser.getPhoneNumber()));
    }

    @Test
    public void validateIncorrectPhoneNumberTest() {
        assertFalse(Validator.validatePersonAmount(incorrectUser.getPhoneNumber()));
    }

    @Test
    public void validateStatusTest() {
        assertTrue(Validator.validateStatus(Status.WAITING_FOR_APPROVE.toString()));
    }

    @Test
    public void validateIncorrectStatusTest() {
        assertFalse(Validator.validateStatus(correctUser.getEmail()));
    }
}