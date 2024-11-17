package controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import view.MemberMenu;

/**
 * Responsible for the main menu.
 */
public class MenuManager {
  private view.MainMenu mainMenu;
  private model.domain.StuffLendingSystem stuffLendingSystem;
  private view.MemberMenu memberMenu;
  private view.ItemMenu itemMenu;

  /**
   * Constructor for MainMenu.
   *
   * @param mainMenu - the main menu.
   * @param memberMenu - the member menu.
   * @param sls - the stuff lending system.
   */
  @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "I want to achieve this")
  public MenuManager(view.MainMenu mainMenu, view.MemberMenu memberMenu, 
      view.ItemMenu itemMenu, model.domain.StuffLendingSystem sls) {
    this.stuffLendingSystem = sls;
    this.mainMenu = mainMenu;
    this.itemMenu = itemMenu;
    this.memberMenu = memberMenu;
  }

  /**
   * Starts the main menu of the application. Taking inspiration for the implementation from course example.
   */
  public void runMainMenu() {
    boolean running = true;
    model.domain.Member selectedMember = null;
    while (running) {
      try {
        if (selectedMember != null) {
          if (!runMemberMenu(selectedMember)) {
            selectedMember = null;
          }
        } else {
          // Show the current day when the main menu is shown.
          mainMenu.dayMessage(stuffLendingSystem.getCurrentDay());
          view.MainMenu.MainMenuEvent action = mainMenu.showMainMenu();

          switch (action) {
            case AddMember:
              String name = memberMenu.promptName();
              String email = memberMenu.promptEmail();
              String phoneNumber = memberMenu.promptPhoneNumber();
              model.domain.Member createdMember = stuffLendingSystem.addMember(name, email, phoneNumber);
              memberMenu.successMessage("Member " + createdMember.getName() + " was added!");
              break;
            case SelectMember:
              selectedMember = memberMenu.chooseMember(stuffLendingSystem.getMembers());
              if (selectedMember == null) {
                memberMenu.errorMessage("Invalid choice!");
              }
              break;
            case AdvanceDay:
              stuffLendingSystem.advanceDay();
              break;
            case Quit:
              running = false;
              break;
            case ListMembers:
              memberMenu.printMembers(stuffLendingSystem.getMembers());
              break;
            default:
              mainMenu.errorMessage("Invalid choice!");
              break;
          }
        }
      } catch (Exception e) {
        // Shows the error and runs the menu again.
        mainMenu.errorMessage(e.getMessage());
        selectedMember = null;
      }
    }
  }

  /**
   * Runs the member menu.
   *
   * @param selectedMember - the member to run the menu for.
   * @return true if the menu should be run again, false if the user wants to go back.
   */
  public boolean runMemberMenu(model.domain.Member selectedMember) {
    MemberMenu.MemberMenuEvent action = memberMenu.viewMemberMenu(selectedMember);
    switch (action) {
      case EditMail:
        stuffLendingSystem.setEmailToMember(selectedMember, memberMenu.promptEmail());
        mainMenu.successMessage("Email was changed!");
        break;
      case EditName:
        selectedMember.setName(memberMenu.promptName());
        mainMenu.successMessage("Name was changed!");
        break;
      case EditPhoneNumber:
        stuffLendingSystem.setPhoneNumberToMember(selectedMember, memberMenu.promptPhoneNumber());
        mainMenu.successMessage("Phone number was changed!");
        break;
      case ManageItems:
        runItemMenu(selectedMember);
        break;
      case DeleteMember:
        stuffLendingSystem.deleteMember(selectedMember);
        mainMenu.successMessage("Member was deleted!");
        return false;
      case BorrowItem:
        int chosenItem = itemMenu.listAllItems(stuffLendingSystem.getItemsList());
        int from = itemMenu.promptFromDay();
        int to = itemMenu.promptToDay();
        // No need to send invalid dates to the system.
        if (from > to || from < stuffLendingSystem.getCurrentDay()) {
          throw new IllegalArgumentException("Invalid dates!");
        }
        stuffLendingSystem.initContract(selectedMember, chosenItem, from, to);
        mainMenu.successMessage("Item was borrowed!");
        break;
      case Back:
        return false;
      default:
        mainMenu.errorMessage("Invalid choice!");
        break;
    }
    return true;
  } 

  /**
   * Runs the item menu.
   *
   * @param selectedMember - the member to run the menu for.
   */
  public void runItemMenu(model.domain.Member selectedMember) {
    view.ItemMenu.ItemMenuEvent action = itemMenu.viewItemMenu(selectedMember);
    switch (action) {
      case ViewItem:
        int selectedIndex = itemMenu.selectItem(selectedMember);
        itemMenu.viewItemDetails(selectedMember.getItem(selectedIndex));
        break;
      case EditItem:
        int editIndex = itemMenu.selectItem(selectedMember);
        String newItemName = itemMenu.promptItemName();
        int newCost = itemMenu.promptItemCost();
        stuffLendingSystem.changeItemInfo(selectedMember, editIndex, newCost, newItemName);
        break;
      case AddItem:
        model.domain.ItemCategory chosen = itemMenu.promptItemCategory();
        String description = itemMenu.promptItemName();
        int itemCost = itemMenu.promptItemCost();
        stuffLendingSystem.addItem(selectedMember, itemCost, chosen, description);
        mainMenu.successMessage("Item was added!");
        break;
      case DeleteItem:
        int index = itemMenu.selectItem(selectedMember);
        stuffLendingSystem.deleteItem(selectedMember, index);
        mainMenu.successMessage("Item was deleted!");
        break;
      case Back:
        break;
      default:
        mainMenu.errorMessage("Invalid choice!");
        break;
    }
  }
}
