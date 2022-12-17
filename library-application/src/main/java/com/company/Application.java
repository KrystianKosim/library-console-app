package com.company;

import com.company.controller.menu.MainMenuController;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {

    private final MainMenuController mainMenuController;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Method to test operation of the application, simply multiply 2 values
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        mainMenuController.run();
    }


}
