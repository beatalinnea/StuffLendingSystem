package view;

import java.util.Scanner;
import model.domain.Item;
import model.domain.ItemCategory;
import model.domain.Member;

/**
 * Menu for items.
 */
public class ItemMenu extends MainMenu {
  /**
   * Represents the Item menu actions.
   */
  public static enum ItemMenuEvent {
    AddItem,
    EditItem,
    Back,
    DeleteItem,
    ViewItem,
    Invalid
  }

  public ItemMenu(Scanner scanner) {
    super(scanner);
  }

  /**
   * Views the item menu and returns the users selected action.
   *
   * @param member the member to view.
   * @return the users selected action.
   */
  public ItemMenuEvent viewItemMenu(Member member) {
    final String addString = "add";
    final String editString = "edit";
    final String deleteString = "delete";
    final String viewString = "view";

    System.out.println("\n == Item Menu ==");
    System.out.println(" " + addString + " - Add item");
    System.out.println(" " + editString + " - Edit item");
    System.out.println(" " + viewString + " - View item details");
    System.out.println(" " + deleteString + " - Delete item");
    System.out.println(" b - back");

    String choice = getInput();

    if (choice.equals("b")) {
      return ItemMenuEvent.Back;
    } else if (choice.equals(addString)) {
      return ItemMenuEvent.AddItem;
    } else if (choice.equals(editString)) {
      return ItemMenuEvent.EditItem;
    } else if (choice.equals(deleteString)) {
      return ItemMenuEvent.DeleteItem;
    } else if (choice.equals(viewString)) {
      return ItemMenuEvent.ViewItem;
    } else {
      return ItemMenuEvent.Invalid;
    }
  }

  /**
   * Prompts the user for item index for item to be edited.
   *
   * @param member the selected member.
   * @return index of item to be edited.
   */
  public int selectItem(Member member) {
    System.out.println("\n == Select item: == ");
    int i = 1;
    for (model.domain.Item item : member.getItems()) {
      System.out.println(" " + i + ". " + item.getCategory() + ": " + item.getItemName()
          + ", cost: " + item.getCostPerDay());
      i++;
    }
    String choice = getInput();
    return Integer.parseInt(choice) - 1;
  }

  /**
   * Prompts the user for item description.
   *
   * @return description.
   */
  public String promptItemName() {
    System.out.println("\n == Enter item description: == ");
    String choice = getInput();
    return choice;
  }

  /**
   * Prompts the user for item cost.
   *
   * @return - The cost of the item.
   */
  public int promptItemCost() {
    System.out.println("\n == Enter item cost: == ");
    String choice = getInput();
    return Integer.parseInt(choice);
  }

  /**
   * Prompts the user for item category.
   *
   * @return - The chosen category.
   */
  public ItemCategory promptItemCategory() {
    ItemCategory[] categories = ItemCategory.values();
    System.out.println("\n == Choose item category: == ");
    for (int i = 0; i < categories.length; i++) {
      System.out.println(" " + (i + 1) + ". " + categories[i].getDisplayName());
    }
    String choice = getInput();
    int index = Integer.parseInt(choice) - 1;
    return categories[index];
  }

  /**
   * Prompts the user for item index for item to be selected.
   *
   * @param itemList - The list of items to choose from.
   * @return index of item to be selected.
   */
  public int listAllItems(String itemList) {
    System.out.println("\n == Choose item: == ");
    System.out.println(itemList);
    String choice = getInput();
    return Integer.parseInt(choice) - 1;
  }

  /**
   * Views details about a selected item.
   *
   * @param item - The item to show details for.
   * @return the users selected action.
   */
  public ItemMenuEvent viewItemDetails(Item item) {
    System.out.println("\n == Item details: == ");
    System.out.println(item.detailedToString());
    System.out.println("\n == Choose: == ");
    System.out.println(" b - back");
    String choice = getInput();
    if (choice.equals("b")) {
      return ItemMenuEvent.Back;
    } else {
      return ItemMenuEvent.Invalid;
    }
  }

  /**
   * Prompts the user for from day to lend the item from.
   *
   * @return int the day to start lending.
   */
  public int promptFromDay() {
    System.out.println("\n == Enter from day: == ");
    String choice = getInput();
    return Integer.parseInt(choice);
  }

  /**
   * Prompts the user for to day to lend the item to.
   *
   * @return int the day.
   */
  public int promptToDay() {
    System.out.println("\n == Enter to day: == ");
    String choice = getInput();
    return Integer.parseInt(choice);
  }
}
