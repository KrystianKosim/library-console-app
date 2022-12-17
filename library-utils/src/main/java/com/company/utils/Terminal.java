package com.company.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@RequiredArgsConstructor
@Component
public class Terminal {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR_NAME = "authorName";
    public static final String AUTHOR_SURNAME = "authorSurname";
    public static final String QUANTITY = "quantity";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String TYPE = "type";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String PARENT_ID = "parentId";
    public static final String ENTER_ID = "Enter id: ";
    public static final String ENTER_TITLE = "Enter title: ";
    public static final String ENTER_QUANTITY = "Enter quantity: ";
    public static final String ENTER_AUTHOR_DATA = "Enter author data: ";
    public static final String ENTER_NAME = "Enter name: ";
    public static final String ENTER_SURNAME = "Enter surname: ";
    public static final String ENTER_YEAR = "Enter year: ";
    public static final String ENTER_MONTH = "Enter month: ";
    public static final String ENTER_DAY = "Enter day: ";
    public static final String ENTER_BIRTHDATE = "Enter birthdate: ";
    public static final String ENTER_ADDRESS = "Enter address: ";
    public static final String ENTER_PHONE_NUMBER = "Enter phone number: ";
    public static final String INCORRECT_ID = "Incorrect id";
    public static final String INCORRECT_LOGIN = "Incorrect Login";
    public static final String YOU_HAVE_TO_RETURN_BOOKS = "You have to return books!";
    public static final String NOT_FOUND_READER_WITH_GIVEN_PARAMETERS = "Not found reader with given parameters";
    public static final String NOT_FOUND_PARENT_WITH_GIVEN_PARAMETERS = "Not found parent with given parameters";
    public static final String NOT_FOUND_AUTHOR_WITH_GIVEN_PARAMS = "Not found author with given params";
    public static final String NOT_FOUND_BOOK_WITH_GIVEN_PARAMS = "Not found book with given params";
    public static final String INCORRECT_VALUE = "Incorrect value";
    public static final String TRY_AGAIN = " try again!";
    public static final String AUTHOR_HAVE_BOOKS_YOU_CAN_T_DELETE_HIM = "Author have books, you can't delete him!";
    public static final String YOU_HAVEN_T_GOT_ANY_LOANS = "You haven't got any loans";
    private static final String YES = "yes";
    private static final String EMPTY = "";
    public static final String PARENT = "parent";
    public static final String CHILD = "child";
    public static final String READER = "READER";
    public static final String ENTER_PERSON_TYPE_PARENT_CHILD_OR_PRESS_ENTER_IF_YOU_DONT_KNOW = "Enter person type parent/child or press enter if you dont know";
    public static final String DO_YOU_WANT_STILL_DELETE_WITH_EXISTED_LOAN_WRITE_YES_OR_NO = "Do you want still delete with existed loan? Write yes or no";
    public static final String DO_YOU_KNOW_YOUR_PARENT_ID_NUMBER_WRITE_YES_OR_NO = "Do you know your parent id number? Write yes or no";
    public static final String ENTER_YOUR_ADMIN_PASSWORD_ADMIN = "Enter your admin password: ADMIN";
    public static final String ENTER_ACTION = "Enter action ";
    public static final String MENU = " menu ";
    public static final String ENTER = "Enter ";
    public static final String BORROW_DATE = "BorrowDate ";
    public static final String RETURN_DATE = "ReturnDate ";
    public static final String ENTER_FILTER_VALUES = "Enter filter values";
    public static final String ENTER_NEW_VALUES = "Enter new values";
    public static final String YOU_CAN_T_BORROW_A_BOOK = "You can't borrow a book";
    public static final String BOOK_IS_NOT_AVAILABLE = "Book is not available";
    public static final String YOU_HAVE_THIS_BOOK = "You have this book!";
    public static final String FOUNDED_MORE_THAN_ONE_BOOK = "Founded more than one book";


    public String getStringValue(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        String value = scanner.nextLine().toUpperCase();
        return value;
    }

    public void displayList(List<?> list, String message) {
        if (list.isEmpty()) {
            System.out.println(message);
            return;
        }
        list.forEach(System.out::println);
    }

    public String getReaderType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_PERSON_TYPE_PARENT_CHILD_OR_PRESS_ENTER_IF_YOU_DONT_KNOW);
        String type = scanner.nextLine();
        type = type.toLowerCase();
        if (type.contains(PARENT)) {
            return PARENT;
        } else if (type.contains(CHILD)) {
            return CHILD;
        }
        return EMPTY;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void displayLoansForBook(List loans) {
        for (Object loan : loans) {
            Object[] loanTab = (Object[]) loan;
            System.out.println(loanTab[0] + " " + loanTab[1] + " " + loanTab[2] + " " + loanTab[3] + " " + loanTab[4] + " " + loanTab[5]);
        }
    }

    public boolean getDecisionValueForDeleteFromLoans() {
        System.out.print(DO_YOU_WANT_STILL_DELETE_WITH_EXISTED_LOAN_WRITE_YES_OR_NO);
        Scanner scanner = new Scanner(System.in);
        String decisionValue = scanner.nextLine().toLowerCase();
        return decisionValue.contains(YES);
    }


    public void displayLoansForReader(List loans) {
        for (Object loan : loans) {
            Object[] loanTab = (Object[]) loan;
            Object returnDate = loanTab[3] == null ? EMPTY : loanTab[3];
            System.out.println(ID + loanTab[0] +
                    BORROW_DATE + loanTab[2] +
                    RETURN_DATE + returnDate +
                    TITLE + loanTab[4]);
        }
    }

    public boolean isKnowParentId() {
        System.out.println(DO_YOU_KNOW_YOUR_PARENT_ID_NUMBER_WRITE_YES_OR_NO);
        Scanner scanner = new Scanner(System.in);
        String decisionValue = scanner.nextLine().toLowerCase();
        return decisionValue.contains(YES);
    }

    public int getValueToConfigurationTable(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER + message);
        String valueAsString = scanner.nextLine();
        int value;
        try {
            value = Integer.parseInt(valueAsString);
            if (value >= 0) {
                return value;
            }
        } catch (NumberFormatException e) {
        }
        System.out.println(INCORRECT_VALUE + TRY_AGAIN);
        return getValueToConfigurationTable(message);
    }


    public String getAction(String msg) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(ENTER_ACTION + msg + MENU);
        String action = scanner.nextLine().toUpperCase().trim();
        return action;
    }

    public String getPassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ENTER_YOUR_ADMIN_PASSWORD_ADMIN);
        String userType = scanner.nextLine().toUpperCase();
        return userType;
    }

    public Map<String, String> getBookData(boolean isNeedId, String message) {
        System.out.println(message);
        String id = isNeedId ? getStringValue(ENTER_ID) : null;
        String title = getStringValue(ENTER_TITLE).toUpperCase();
        String quantity = getStringValue(ENTER_QUANTITY).toUpperCase();
        System.out.println(ENTER_AUTHOR_DATA);
        String authorName = getStringValue(ENTER_NAME).toUpperCase();
        String authorSurname = getStringValue(ENTER_SURNAME).toUpperCase();
        Map<String, String> data = new HashMap<>();
        data.put(ID, id);
        data.put(TITLE, title);
        data.put(AUTHOR_NAME, authorName);
        data.put(AUTHOR_SURNAME, authorSurname);
        data.put(QUANTITY, quantity);
        return data;
    }

    public Map<String, String> getReaderData(boolean isNeedId, boolean isNeedType, String message) {
        System.out.println(message);
        String id = isNeedId ? getStringValue(ENTER_ID) : EMPTY;
        String name = getStringValue(ENTER_NAME).toUpperCase();
        String surname = getStringValue(ENTER_SURNAME).toUpperCase();
        System.out.println(ENTER_BIRTHDATE);
        String year = getStringValue(ENTER_YEAR);
        String month = getStringValue(ENTER_MONTH);
        String day = getStringValue(ENTER_DAY);
        String type = isNeedType ? getReaderType() : EMPTY;
        Map<String, String> data = new HashMap<>();
        data.put(ID, id);
        data.put(NAME, name);
        data.put(SURNAME, surname);
        data.put(YEAR, year);
        data.put(MONTH, month);
        data.put(DAY, day);
        data.put(TYPE, type);
        return data;
    }

    public Map<String, String> getAuthorData(boolean isNeedId, String message) {
        System.out.println(message);
        String id = isNeedId ? getStringValue(ENTER_ID) : EMPTY;
        String name = getStringValue(ENTER_NAME).toUpperCase();
        String surname = getStringValue(ENTER_SURNAME).toUpperCase();
        Map<String, String> data = new HashMap<>();
        data.put(ID, id);
        data.put(NAME, name);
        data.put(SURNAME, surname);
        return data;
    }

    public Map<String, String> getParentData() {
        String address = getStringValue(ENTER_ADDRESS).toUpperCase();
        String phoneNumber = getStringValue(ENTER_PHONE_NUMBER).toUpperCase();
        Map<String, String> data = new HashMap<>();
        data.put(ADDRESS, address);
        data.put(PHONE_NUMBER, phoneNumber);
        return data;
    }

    public Map<String, String> getChildData() {
        String parentId = getStringValue(ENTER_ID);
        Map<String, String> data = new HashMap<>();
        data.put(PARENT_ID, parentId);
        return data;
    }
}
