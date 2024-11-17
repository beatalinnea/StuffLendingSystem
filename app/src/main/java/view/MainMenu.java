package view;

import java.util.Scanner;

/**
 * Class for viewing the main menu.
 */
public class MainMenu {
  private Scanner input;

  protected String getInput() {
    return input.nextLine(); 
  }

  /**
   * Main menu actions.
   */
  public static enum MainMenuEvent {
    AddMember,
    ListMembers,
    Quit,
    SelectMember,
    AdvanceDay,
    Invalid
  }

  public MainMenu(Scanner input) {
    this.input = input;
  }

  /**
   * Views the menu and returns the users selected action.
   *
   * @return the users selected action.
   */
  public MainMenuEvent showMainMenu() {
    final String addString = "add";
    final String selectString = "select";
    final String listString = "list";
    final String advanceString = "advance";

    System.out.println(" == Main Menu ==");
    System.out.println(" " + addString + " - Add New Member");
    System.out.println(" list - List Members");
    System.out.println(" " + selectString + "- Select a Member");
    System.out.println(" " + advanceString + " - Advance day");
    System.out.println(" q - quit");

    String choice = getInput();

    if (choice.equals("q")) {
      return MainMenuEvent.Quit;
    } else if (choice.equals(addString)) {
      return MainMenuEvent.AddMember;
    } else if (choice.equals(selectString)) {
      return MainMenuEvent.SelectMember;
    } else if (choice.equals(listString)) {
      return MainMenuEvent.ListMembers;
    } else if (choice.equals(advanceString)) {
      return MainMenuEvent.AdvanceDay;
    } else {
      return MainMenuEvent.Invalid;
    }
  }

  public void dayMessage(int day) {
    System.out.println("\n == Today is day " + day + " == \n");
  }

  public void successMessage(String message) {
    System.out.println("\n == Success! == ");
    System.out.println(message + "\n");
  }

  public void errorMessage(String message) {
    System.out.println("\n == Error! == ");
    System.out.println(message + "\n");
  }
}
