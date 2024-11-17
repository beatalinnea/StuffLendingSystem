package view;

import java.util.ArrayList;
import java.util.Scanner;
import model.domain.Member;

/**
 * Class representing the member menu.
 */
public class MemberMenu extends MainMenu {
  /**
   * Represents the member menu actions.
   */
  public static enum MemberMenuEvent {
    EditMail,
    EditName,
    EditPhoneNumber,
    Back,
    ManageItems,
    BorrowItem,
    DeleteMember,
    Invalid
  }

  public MemberMenu(Scanner scanner) {
    super(scanner);
  }

  /**
   * Prints a list of members.
   *
   * @param members the members to list
   */
  public void printMembers(ArrayList<model.domain.Member> members) {
    System.out.println("\n == Members: == ");
    int i = 1;
    for (model.domain.Member member : members) {
      System.out.println(i + ". " + member.toString());
      i++;
    }
  }

  /**
   * Prompts the user to choose a member from a list.
   *
   * @param members the members to choose from.
   * @return the chosen member.
   */
  public model.domain.Member chooseMember(ArrayList<model.domain.Member> members) {
    printMembers(members);
    System.out.println("\n == Enter member number == ");
    final int selected = Integer.parseInt(getInput());

    if (members.size() >= selected) {
      return members.get(selected - 1);
    } else {
      return null;
    }
  }

  private void printSelectedMember(Member member) {
    System.out.println("\n == Selected member == ");
    System.out.println(member.toStringDetailed());
    System.out.println(member.currentlyBorrowingString());
    System.out.println(member.getOwnedItemsString());
  }

  /**
   * Views the menu and returns the users selected action.
   *
   * @param member the member to view.
   * @return the users selected action.
   */
  public MemberMenuEvent viewMemberMenu(Member member) {
    printSelectedMember(member);
    final String editString = "edit";
    final String itemString = "item";
    final String borrowString = "borrow";
    final String deleteString = "delete";

    System.out.println(" == Member Menu ==");
    System.out.println(" " + editString + " - Edit member info");
    System.out.println(" " + borrowString + " - Borrow new item");
    System.out.println(" " + itemString + " - Manage Own Items");
    System.out.println(" " + deleteString + " - Delete member");
    System.out.println(" b - back");

    String choice = getInput();

    if (choice.equals("b")) {
      return MemberMenuEvent.Back;
    } else if (choice.equals(editString)) {
      System.out.println("\n == Edit name, email or phone number? (n/e/p) ==");
      String newChoice = getInput();
      if (newChoice.equals("n")) {
        return MemberMenuEvent.EditName;
      } else if (newChoice.equals("e")) {
        return MemberMenuEvent.EditMail;
      } else if (newChoice.equals("p")) {
        return MemberMenuEvent.EditPhoneNumber;
      } else {
        return MemberMenuEvent.Invalid;
      }
    } else if (choice.equals(itemString)) {
      return MemberMenuEvent.ManageItems;
    } else if (choice.equals(borrowString)) {
      return MemberMenuEvent.BorrowItem;
    } else if (choice.equals(deleteString)) {
      return MemberMenuEvent.DeleteMember;
    } else {
      return MemberMenuEvent.Invalid;
    }
  }

  /**
   * Prompts the user for a new name.
   *
   * @return the wanted name.
   */
  public String promptName() {
    System.out.println("\n == Enter name: == ");
    String choice = getInput();
    return choice;
  }

  /**
   * Prompts the user for a new email.
   *
   * @return the wanted email.
   */
  public String promptEmail() {
    System.out.println("\n == Enter email: == ");
    String choice = getInput();
    return choice;
  }

  /**
   * Prompts the user for a new phone number.
   *
   * @return wanted phone number.
   */
  public String promptPhoneNumber() {
    System.out.println("\n == Enter phone number: == ");
    String choice = getInput();
    return choice;
  }

}
