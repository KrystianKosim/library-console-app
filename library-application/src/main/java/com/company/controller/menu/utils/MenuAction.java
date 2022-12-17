package com.company.controller.menu.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class MenuAction {

    @Lazy
    @Autowired
    private List<MenuAction> menuItems;

    /**
     * Method run action of specific menu item or print child (sub menu) and then run action
     */
    public void run() {
        List<MenuAction> childs = getChild();
        if (!childs.isEmpty()) {
            printSubMenu(childs);
        }
        action();
    }

    public abstract void action();

    public String code() {
        MenuItem menuItem = this.getClass().getDeclaredAnnotation(MenuItem.class);
        return menuItem.code();
    }

    public String description() {
        MenuItem menuItem = this.getClass().getDeclaredAnnotation(MenuItem.class);
        return menuItem.description();
    }

    public int priority() {
        MenuItem menuItem = this.getClass().getDeclaredAnnotation(MenuItem.class);
        return menuItem.priority();
    }

    public boolean hasChild() {
        return !getChild().isEmpty();
    }

    public List<String> parents() {
        MenuItem menuItem = this.getClass().getDeclaredAnnotation(MenuItem.class);
        return List.of(menuItem.parent());
    }

    public boolean hasParent(String parent) {
        MenuItem menuItem = this.getClass().getDeclaredAnnotation(MenuItem.class);
        return (parent.isEmpty() && menuItem.parent().length == 0) || List.of(menuItem.parent()).contains(parent);
    }

    /**
     * Return childs(sub menu) of current class
     *
     * @return List of menu options
     */
    public List<MenuAction> getChild() {
        return menuItems.stream()
                .filter(menuAction -> menuAction.hasParent(code()))
                .collect(Collectors.toList());
    }

    /**
     * Print childs(Menu options, sub menu) in console
     *
     * @param childs , menu options which should be printed
     */
    public void printSubMenu(List<MenuAction> childs) {
        childs = childs.stream()
                .sorted(Comparator.comparingInt(MenuAction::priority))
                .collect(Collectors.toList());
        System.out.println("");
        System.out.println("*** MENU: " + description() + " ***");
        childs.forEach(child -> System.out.println(" - " + child.description() + " [" + child.code() + "]"));
        System.out.println("");
    }

}
