package ee.taltech.iti03022023salonbackend.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidityCheck {

    /**
     * Control method to check password's validity.
     *
     * @param password to be controlled
     * @return boolean
     */
    public boolean isValidPassword(String password) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Integer counter = 0;
        if (password.length() < 8 || password.length() > 50) {
            return false;
        } else if (password.equals(password.toLowerCase())) {
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            if (alphabet.contains(password.substring(i, i + 1))) {
                counter++;
            }
        }
        return !counter.equals(password.length());
    }

    /**
     * Control method to check validity of phone number.
     *
     * @param number to be controlled
     * @return boolean
     */
    public boolean isValidPhoneNumber(String number) {
        // String numerics = "1234567890";
        List<String> numerics = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        if (number.length() < 7) {
            return false;
        }
        for (int i = 0; i < number.length(); i++) {
            if (!numerics.contains(number.substring(i, i + 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method for checking ID code's validity.
     *
     * @param idCode to be checked
     * @return boolean
     */
    public boolean isValidIdCode(String idCode) {
        // List<Integer> months1 = List.of(1, 3, 5, 7, 8, 10, 12);
        // List<Integer> months2 = List.of(4, 6, 9, 11);
        int genderNumber = Integer.parseInt(idCode.substring(0, 1));
        int yearNumber = Integer.parseInt(idCode.substring(1, 3));
        int monthNumber = Integer.parseInt(idCode.substring(3, 5));
        int dayNumber = Integer.parseInt(idCode.substring(5, 7));
        if (idCode.length() != 11) {
            return false;
        } else if (genderNumber > 6 || genderNumber < 1) {
            return false;
        } else if (yearNumber < 0 || yearNumber > 99) {
            return false;
        } else if (monthNumber < 1 || monthNumber > 12) {
            return false;
        } else return dayNumber <= 31 && dayNumber >= 1;
    }

    /**
     * Method for checking control number validity.
     *
     * @param idCode to be checked
     * @return boolean
     */
    public boolean isControlNumberCorrect(String idCode) {
        int one = Integer.parseInt(idCode.substring(0, 1));
        int two = Integer.parseInt(idCode.substring(1, 2));
        int three = Integer.parseInt(idCode.substring(2, 3));
        int four = Integer.parseInt(idCode.substring(3, 4));
        int five = Integer.parseInt(idCode.substring(4, 5));
        int six = Integer.parseInt(idCode.substring(5, 6));
        int seven = Integer.parseInt(idCode.substring(6, 7));
        int eight = Integer.parseInt(idCode.substring(7, 8));
        int nine = Integer.parseInt(idCode.substring(8, 9));
        int ten = Integer.parseInt(idCode.substring(9, 10));
        int last = Integer.parseInt(idCode.substring(10, 11));
        int first_algo = one + two * 2 + three * 3 + four * 4 + five * 5 + six * 6 + seven * 7 + eight * 8 + nine * 9 + ten;
        int second_algo = one * 3 + two * 4 + three * 5 + four * 6 + five * 7 + six * 8 + seven * 9 + eight + nine * 2 + ten * 3;
        if (first_algo % 11 == last) {
            return true;
        } else if (second_algo % 11 == last && first_algo % 11 >= 10) {
            return true;
        } else if (second_algo % 11 >= 10 && last == 0) {
            return true;
        }
        return false;
    }
}
