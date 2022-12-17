package com.company.utils;

import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;

@Component
public class ValuesService {
    public static final String EMPTY = "";

    public boolean checkStringValueToFind(String value) {
        return !EMPTY.equals(value);
    }

    public boolean checkIdValue(String idString) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            return false;
        }
        return id > 0;
    }

    public boolean checkQuantityValue(String quantityString) {
        if (EMPTY.equals(quantityString)) {
            return false;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            return false;
        }
        return quantity >= 0;
    }

    public boolean checkBirthDateValue(LocalDate birthDate) {
        return birthDate != null;
    }

    public Integer getIdFromString(String parentIdString) {
        try {
            Integer parentId = Integer.parseInt(parentIdString);
            return parentId;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public LocalDate getBirthDate(String year, String month, String day) {
        try {
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            int dayInt = Integer.parseInt(day);
            LocalDate date = LocalDate.of(yearInt, monthInt, dayInt);
            return date;
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }


}
