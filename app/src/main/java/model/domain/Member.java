package model.domain;

import java.util.ArrayList;
import model.domain.LendingContract.ContractState;

/**
 * Class representing a Member.
 */
public class Member {
  private String email;
  private String name;
  private ArrayList<Item> ownedItems;
  private String id;
  private CreditAccount creditAccount;
  private ArrayList<LendingContract> lendingContracts;
  private int dayOfCreation;
  private String phoneNumber;

  /**
   * Initializing constructor.
   *
   * @param name  - The name of the member.
   * @param email - The email of the member.
   * @param id    - The id of the member.
   */
  public Member(String name, String email, String id, String phoneNumber, int dayOfCreation) {
    setName(name);
    setEmail(email);
    setPhoneNumber(phoneNumber);
    this.id = id;
    this.dayOfCreation = dayOfCreation;
    this.creditAccount = new CreditAccount();
    ownedItems = new ArrayList<>();
  }

  /**
   * Deep copy constructor.
   *
   * @param m the member to copy.
   */
  public Member(Member m) {
    this.email = m.email;
    this.name = m.name;
    this.id = m.id;
    this.creditAccount = m.creditAccount;
    this.dayOfCreation = m.dayOfCreation;
    this.phoneNumber = m.phoneNumber;
    ownedItems = new ArrayList<>(m.ownedItems.size());

    for (Item i : m.ownedItems) {
      ownedItems.add(new Item(i));
    }
  }

  protected void addItem(Item i) {
    ownedItems.add(i);
  }

  protected void changeItemInfo(int index, int cost, String name) {
    ownedItems.get(index).setCostPerDay(cost);
    ownedItems.get(index).setNewName(name);
  }

  /**
   * If member deletes an item, member deletes the item from the item catalog.
   *
   * @param index       - of the item to be deleted.
   * @param itemCatalog - the item catalog within the system.
   */
  protected void deleteItem(int index, ItemCatalog itemCatalog, int currentDay) {
    if (ownedItems.get(index).getContracts() != null) {
      for (LendingContract l : ownedItems.get(index).getContracts()) {
        // Can not delete item if it has active or planned contracts.
        if (currentDay <= l.getStartingDay()) {
          throw new IllegalArgumentException("Cannot delete item with active or future contract");
        }
      }
    }
    // If can be removed - member removes its item.
    itemCatalog.deleteEqual(ownedItems.get(index));
    ownedItems.remove(index);
  }

  protected void deleteAllItems(ItemCatalog itemCatalog) {
    for (Item i : ownedItems) {
      itemCatalog.deleteEqual(i);
    }
    ownedItems.clear();
  }

  protected void addCredits(int amount) {
    creditAccount.addCredits(amount);
  }

  protected void removeCredits(int amount) {
    creditAccount.removeCredits(amount);
  }

  /**
   * Will return the lending contracts.
   *
   * @param contract - The contract to be added.
   */
  protected void addContract(LendingContract contract) {
    if (lendingContracts == null) {
      lendingContracts = new ArrayList<>();
    }
    lendingContracts.add(contract);
  }

  protected void updateContracts(int currentDay) {
    if (lendingContracts == null) {
      return;
    }
    for (LendingContract contract : lendingContracts) {
      contract.setState(currentDay);
    }
  }

  protected boolean isCurrentlyBorrowingItem() {
    if (lendingContracts == null) {
      return false;
    }
    for (LendingContract contract : lendingContracts) {
      if (contract.getState() == ContractState.Active
          || contract.getState() == ContractState.Planned) {
        return true;
      }
    }
    return false;
  }

  protected boolean owningItemsAreLent() {
    if (ownedItems == null) {
      return false;
    }
    for (Item i : ownedItems) {
      if (i.hasActiveOrPlannedContracts()) {
        return true;
      }
    }
    return false;
  }

  // ==================== Public Getters ====================
  public String getEmail() {
    return email;
  }

  public int getDayOfCreation() {
    return dayOfCreation;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public int getAmountOfCredits() {
    return creditAccount.getCurrentAmount();
  }

  /**
   * Gets (copies of) the items that the member owns.
   *
   * @return copied items.
   */
  public ArrayList<Item> getItems() {
    ArrayList<Item> copiedItems = new ArrayList<>();
    for (Item i : ownedItems) {
      copiedItems.add(new Item(i));
    }
    return copiedItems;
  }

  public Item getItem(int index) {
    Item originalItem = ownedItems.get(index);
    return new Item(originalItem);
  }

  public String toString() {
    return name + "\n email: " + email + "\n owns: " + ownedItems.size() + " items" + "\n credits: "
        + getAmountOfCredits();
  }

  public String toStringDetailed() {
    return name + "\n email: " + email + "\n phone number: " + phoneNumber + "\n member id: " + id + "\n member since day: "
        + getDayOfCreation() + "\n credits: " + getAmountOfCredits();
  }

  /**
   * Builds a string with information about the members currently active lending contracts.
   *
   * @return a string with information about the members currently active lending contracts.
   */
  public String currentlyBorrowingString() {
    if (lendingContracts == null || lendingContracts.isEmpty()) {
      return "Currently borrowing: 0 items";
    }

    StringBuilder borrowing = new StringBuilder("Currently borrowing: ");
    int currentlyBorrowing = 0;

    for (LendingContract c : lendingContracts) {
      if (c.isActive()) {
        if (currentlyBorrowing > 0) {
          borrowing.append(", "); // Add a comma if there are multiple contracts
        }
        borrowing.append(c.toString());
        currentlyBorrowing++;
      }
    }

    if (currentlyBorrowing == 0) {
      borrowing.append("0 items");
    }

    return borrowing.toString();
  }

  /**
   * Builds a string with information about the members own items.
   *
   * @return a string with the members own items.
   */
  public String getOwnedItemsString() {
    StringBuilder ownedItems = new StringBuilder();

    if (getItems().isEmpty()) {
      ownedItems.append("Owns items: 0");
    } else {
      ownedItems.append("Owns items: \n");
    }

    for (model.domain.Item item : getItems()) {
      ownedItems.append(item.toString()).append("\n");
      if (item.getItemBorrowerName() == null) {
        ownedItems.append("    Currently available\n");
      } else {
        ownedItems.append("    Currently lent to: ").append(item.getItemBorrowerName()).append("\n");
      }
    }

    return ownedItems.toString();
  }
  // ==================== Setters ====================

  /**
   * Sets the name of the member.
   *
   * @param newName - The chosen name of the member.
   */
  public void setName(String newName) {
    if (newName.length() < 2) {
      throw new IllegalArgumentException("Name must be at least 2 characters long");
    } else if (newName.length() > 20) {
      throw new IllegalArgumentException("Name must be at most 20 characters long");
    }
    name = newName;
  }

  protected void setEmail(String newEmail) {
    if (!newEmail.contains("@")) {
      throw new IllegalArgumentException("Invalid email");
    }
    email = newEmail;
  }

  protected void setPhoneNumber(String phoneNumber) {
    if (phoneNumber.length() < 8 || phoneNumber.length() > 12) {
      throw new IllegalArgumentException("Invalid phone number - length");
    }
    if (!phoneNumber.matches("[0-9]+")) {
      throw new IllegalArgumentException("Invalid phone number - characters");
    }
    this.phoneNumber = phoneNumber;
  }
}
