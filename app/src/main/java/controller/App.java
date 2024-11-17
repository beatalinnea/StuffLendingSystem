package controller;

import java.io.File;
import java.util.Scanner;

/**
 * Responsible for staring the application.
 */
public class App {
  /**
   * Application starting point.

   * @param args command line arguments.
   */
  public static void main(String[] args) {
    model.domain.StuffLendingSystem stuffLendingSystem = new model.domain.StuffLendingSystem();
  
    Scanner scanner = new Scanner(System.in, "UTF8");

    view.MainMenu mainMenu = new view.MainMenu(scanner);
    view.MemberMenu memberMenu = new view.MemberMenu(scanner);
    view.ItemMenu itemMenu = new view.ItemMenu(scanner);
    controller.MenuManager controllerMenu = 
        new controller.MenuManager(mainMenu, memberMenu, itemMenu, stuffLendingSystem);

    // Read from file
    File data = new File("data.txt");
    model.persistance.FileReader fileReader = new model.persistance.FileReader();
    fileReader.readFile(data, stuffLendingSystem);
    controllerMenu.runMainMenu();

    scanner.close();
  }
}
