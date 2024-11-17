package model.domain;

import java.util.ArrayList;
import model.domain.LendingContract.ContractState;

/**
 * Class representing an item catalog.
 */
public class ItemCatalog {
  private ArrayList<Item> registeredItems;

  public ItemCatalog() {
    registeredItems = new ArrayList<>();
  }

  /**
   * Adds a new item to the catalog.
   *
   * @param item - the item to be added
   * @return the added item
   */
  protected Item addItem(Item item) {
    registeredItems.add(item);
    return registeredItems.get(registeredItems.size() - 1);
  }

  /**
   * Returns a copy of the items in the catalog.
   *
   * @return the items in the catalog.
   */
  protected ArrayList<Item> getAllItems() {
    ArrayList<Item> copiedItems = new ArrayList<>();
    for (Item i : registeredItems) {
      copiedItems.add(new Item(i));
    }
    return copiedItems;
  }

  /**
   * Deletes an item if the item is in the catalog.
   *
   * @param item to be compared to and to be deleted.
   */
  protected void deleteEqual(Item item) {
    for (Item i : registeredItems) {
      if (i.equals(item)) {
        registeredItems.remove(i);
        break;
      }
    }
  }

  /**
   * Sets a contract to an item.
   *
   * @param index      - the index of the item in the catalog.
   * @param member     - the member that wants to borrow the item.
   * @param from       - the starting day of the contract.
   * @param to         - the ending day of the contract.
   * @param currentDay - the current day.
   */
  protected void setContractToItem(int index, Member member, int from, int to, int currentDay) {
    Item itemToBorrow = registeredItems.get(index);
    LendingContract newContract = new LendingContract(itemToBorrow, from, to, currentDay);
    newContract.setBorrower(member);
    member.addContract(newContract);
    itemToBorrow.setContract(newContract);
  }

  /**
   * Summarizes all items and its information.
   *
   * @return a string representing all the items in the catalog.
   */
  public String getListOfItems() {
    StringBuilder listOfItemsBuilder = new StringBuilder();
    int index = 1;
    for (Item i : registeredItems) {
      listOfItemsBuilder.append(index).append(". ")
          .append(i.getCategory()).append(": ")
          .append(i.getItemName()).append(", cost: ")
          .append(i.getCostPerDay()).append("\n");
      if (i.getContracts() != null) {
        for (LendingContract contract : i.getContracts()) {
          if (contract.getState() != ContractState.Past) {
            listOfItemsBuilder.append("  - Not available: ")
                .append(contract.getIntervalString()).append("\n");
          }
        }
      }
      index++;
    }
    return listOfItemsBuilder.toString();
  }
}
