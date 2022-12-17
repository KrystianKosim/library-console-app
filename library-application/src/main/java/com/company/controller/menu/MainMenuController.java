package com.company.controller.menu;

import com.company.controller.ConfigurationController;
import com.company.controller.menu.utils.MenuAction;
import com.company.utils.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class MainMenuController {
    public static final String MAIN = "MAIN";
    public static final String LOGIN_ADMIN = "LOGIN ADMIN";
    private static String USER_TYPE;
    private static boolean applicationOn = true;
    private static boolean isContinue = true;
    private static String action;
    private static Integer readerId;
    private Map<String, MenuAction> menuItems;
    private static List<String> history = new LinkedList<>();
    @Autowired
    private Terminal terminal;
    @Autowired
    private ConfigurationController configurationController;


    public MainMenuController(List<MenuAction> menuItems) {
        this.menuItems = new HashMap<>();
        menuItems.forEach(item -> this.menuItems.put(item.code(), item));
    }

    public static void deleteHistory() {
        history = new LinkedList<>();
    }

    public void run() {
        action = MAIN;
        history.add(action);
        while (applicationOn) {
            if (action.equals(LOGIN_ADMIN) || action.equals("LOGIN READER")) {
                runSelected(action);
                initConfigurationOfApplication();
                history.add(action);
                action = USER_TYPE;
                continue;
            }
            runSelected(action);
            if (!isContinue) {
                isContinue = true;
                continue;
            }
            if (menuItems.get(action).hasChild()) {
                history.add(action);
                action = terminal.getAction(action);
                checkIsCorrectOperations();
            } else if (applicationOn) {
                stayInOldPlaceInMenu();
            }
        }
    }

    private void checkIsCorrectOperations() {
        boolean isCorrect;
        if (!menuItems.keySet().contains(action)) {
            isCorrect = false;
        } else {
            String previousAction = history.get(history.size() - 1);
            isCorrect = menuItems.get(action).parents()
                    .stream().filter(parent -> parent.equals(previousAction))
                    .findAny()
                    .isPresent();
        }
        if (!isCorrect) {
            terminal.printMessage(Terminal.INCORRECT_VALUE);
            stayInOldPlaceInMenu();
        }
    }

    private void stayInOldPlaceInMenu() {
        if (history.isEmpty()) {
            return;
        }
        int lastIndex = history.size() - 1;
        String previous = history.get(lastIndex);
        history.remove(lastIndex);
        action = previous;
    }

    private void runSelected(String menuCode) {
        menuItems.get(menuCode).run();
    }

    public void initConfigurationOfApplication() {
        boolean isConfiguration = configurationController.isConfigurationDataInTable();
        if (isConfiguration) {
            configurationController.initRulesOfBorrow();
        } else {
            configurationController.setDefaultRulesOfBorrow();
        }

    }

    public static void setAp(boolean appState) {
        applicationOn = appState;
    }

    public static void setPreviousAction() {
        if (history.isEmpty()) {
            return;
        }
        int lastIndex = history.size() - 1;
        String previous = history.get(lastIndex - 1);
        history.remove(lastIndex);
        action = previous;
    }

    public static void setUserType(String userType) {
        USER_TYPE = userType;
    }

    public static void setAction(String newAction) {
        action = newAction;
    }

    public static void setIsContinue(boolean value) {
        isContinue = value;
    }

    public static void setReaderId(int id) {
        readerId = id;
    }

    public static void deleteReaderId() {
        readerId = null;
    }

    public static Integer getReaderId() {
        return readerId;
    }

    public static List<String> getHistory() {
        return history;
    }
}
