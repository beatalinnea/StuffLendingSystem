package model.domain;

import java.util.ArrayList;
import model.domain.LendingContract.ContractState;

/**
 * Item class.
 */
public class Item {
  private int costPerDay;
  private ItemCategory category;
  private String itemName;
  private Member owner;
  private ArrayList<LendingContract> contracts;
  private int dayOfCreation;

  /**
   * Initializing constructor.
   *
   * @param cost     of the item.
   * @param category of the item.
   * @param owner    of the item.
   * @param name     of the item.
   */
  public Item(int cost, ItemCategory category, Member owner, String name) {
    setCostPerDay(cost);
    setItemName(name);
    this.category = category;
    owner.addItem(this);
    this.owner = owner;
  }

  /**
   * Deep copy constructor.
   *
   * @param i - The item to copy.
   */
  public Item(Item i) {
    costPerDay = i.costPerDay;
    category = i.category;
    owner = i.owner;
    itemName = i.itemName;
    dayOfCreation = i.dayOfCreation;
    if (i.contracts != null) {
      contracts = new ArrayList<>();
      for (LendingContract contract : i.contracts) {
        contracts.add(new LendingContract(contract));
      }
    } else {
      contracts = null;
    }
  }

  protected boolean hasActiveOrPlannedContracts() {
    if (contracts == null) {
      return false;
    }
    for (LendingContract contract : contracts) {
      if (contract.getState() == ContractState.Active
          || contract.getState() == ContractState.Planned) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the item is available for lending in the given timeframe.
   *
   * @param from - the day to check availability from.
   * @param to - the day to check availability to.
   * @return true if the item is available, false otherwise.
   */
  protected boolean isAvailablePeriod(int from, int to) {
    if (contracts == null) {
      return true;
    }
    for (LendingContract contract : contracts) {
      if (contract.coversContract(from, to)) {
        return false;
      }
    }
    return true;
  }

  // ==================== Setters ====================
  private void setItemName(String name) {
    if (name.length() < 3) {
      throw new IllegalArgumentException("Item name must be descriptive");
    }
    this.itemName = name;
  }

  protected void setDayOfCreation(int dayOfCreation) {
    this.dayOfCreation = dayOfCreation;
  }

  /**
   * Adds a new contract to the item.
   *
   * @param contract - The contract to be added.
   */
  protected void setContract(LendingContract contract) {
    if (contracts == null) {
      contracts = new ArrayList<>();
    }
    contracts.add(contract);
  }

  protected void setNewName(String name) {
    setItemName(name);
  }

  protected void setCostPerDay(int newCost) {
    if (newCost < 1) {
      throw new IllegalArgumentException("Must cost at least 1 credit per day");
    }
    costPerDay = newCost;
  }

  // ==================== Public Getters ====================
  public String getItemName() {
    return itemName;
  }

  protected Member getOwner() {
    return new Member(owner);
  }

  public int getDayOfCreation() {
    return dayOfCreation;
  }

  /**
   * Get copies of contracts.
   *
   * @return copied contracts.
   */
  public ArrayList<LendingContract> getContracts() {
    if (contracts == null) {
      return null;
    }
    ArrayList<LendingContract> copiedContracts = new ArrayList<>();
    for (LendingContract l : contracts) {
      copiedContracts.add(new LendingContract(l));
    }
    return copiedContracts;
  }

  public int getCostPerDay() {
    return costPerDay;
  }

  public String getCategory() {
    return category.getDisplayName();
  }

  /**
   * Returns a string (if) the item is currently being borrowed, if so to whom and
   * which days.
   *
   * @return a string (if) the item is currently being borrowed, if so to whom and
   *         which days.
   */
  public String getItemBorrowerName() {
    if (contracts == null) {
      return null;
    }
    for (LendingContract contract : contracts) {
      if (contract.isActive()) {
        return contract.getBorrowerName() + ", between " + contract.getIntervalString();
      }
    }
    return null;
  }

  public String toString() {
    return "  - " + category.getDisplayName() + ": " + itemName + "\n    cost per day: "
        + costPerDay + "\n    was created day: " + dayOfCreation;
  }

  /**
   * Builds a string with information about the item and its contracts.
   *
   * @return a detailed string representation of the item and its contracts.
   */
  public String detailedToString() {
    StringBuilder details = new StringBuilder();

    details.append("Category: ").append(getCategory()).append("\n");
    details.append("Description: ").append(getItemName()).append("\n");
    details.append("Cost per day: ").append(getCostPerDay()).append("\n");
    details.append("Owner: ").append(owner.getName()).append("\n");
    details.append("Was created day: ").append(getDayOfCreation()).append("\n");

    if (getContracts() != null) {
      details.append("Contracts: \n");
      for (model.domain.LendingContract contract : getContracts()) {
        details.append("- ").append(contract.getState()).append("\n");
        details.append("  Lent to: ").append(contract.getBorrowerName()).append("\n");
        details.append("  Between: ").append(contract.getIntervalString()).append("\n");
      }
    }

    return details.toString();
  }
}
