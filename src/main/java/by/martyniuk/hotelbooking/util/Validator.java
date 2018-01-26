package by.martyniuk.hotelbooking.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Zа-яА-Я]{3,45}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("((\\w)([-.](\\w))?)+@((\\w)([-.](\\w))?)+.[a-zA-Zа-яА-Я]{2,4}");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("((\\+)?\\d+?-?\\d+-?\\d+)|((\\+\\d+)?(\\(\\d{3}\\))\\d{7})|" +
            "((\\+\\d+)?(\\(\\d{3}\\))(\\(\\d{3}\\))?-?\\d)|((\\+-?(\\d){3,18}))");
    private static final Pattern PERSON_AMOUNT_PATTERN = Pattern.compile("\\d");
    private static final Pattern ID_PATTERN = Pattern.compile("\\d{1,20}");

    public static boolean validateId(String id){
        if(id == null){
            return false;
        }
        Matcher matcher = ID_PATTERN.matcher(id);
        return matcher.matches();
    }

    public static boolean validateDateRange(LocalDate checkInDate, LocalDate checkOutDate){
        return checkInDate.isBefore(checkOutDate) && checkInDate.isAfter(LocalDateTime.now().toLocalDate());
    }

    public static boolean validatePersonAmount(String personAmount) {
        if(personAmount == null){
            return false;
        }
        Matcher matcher = PERSON_AMOUNT_PATTERN.matcher(personAmount);
        return matcher.matches();
    }

    public static boolean validateName(String name) {
        if(name == null){
            return false;
        }
        Matcher matcher = NAME_PATTERN.matcher(name);
        return matcher.matches();
    }

    public static boolean validateEmail(String email) {
        if(email == null){
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches() && email.length() <= 250;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber == null){
            return false;
        }
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return matcher.matches() && phoneNumber.length() <= 18;
    }

    public static boolean validatePassword(String password) {
        if(password == null){
            return false;
        }
        return password.length() <= 60 && password.length() >= 6;
    }

    public static boolean validatePasswordsEquals(String password, String confirmation){
        if(password == null || confirmation == null){
            return false;
        }
        return password.equals(confirmation);
    }
}
