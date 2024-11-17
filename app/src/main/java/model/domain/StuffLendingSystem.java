package model.domain;

import java.util.ArrayList;

/**
 * Class for Stuff Lending System - keeping track of its members, items and days. 
 * This class contains public methods reachable from the view.
 */
public class StuffLendingSystem {
  private DayCounter dayCounter = new DayCounter();
  private MemberRegistry memberRegistry = new MemberRegistry();
  private ItemCatalog itemCatalog = new ItemCatalog();

  public int getCurrentDay() {
    return dayCounter.getCurrentDay();
  }

  /**
   * Advances the day counter by one. When day is advanced, it updates all members contracts.
   */
  public void advanceDay() {
    dayCounter.incrementDay();
    for (Member m : memberRegistry.getMembers()) {
      m.updateContracts(getCurrentDay());
    }
  }

  /**
   * Adds new member to the system.
   *
   * @param name - Name of the member.
   * @param email - Email of the member.
   * @param phoneNumber - Phone number of the member.
   * @return new added member.
   */
  public Member addMember(String name, String email, String phoneNumber) {
    return memberRegistry.addNewMember(name, email, getCurrentDay(), phoneNumber);
  }

  /**
   * Deletes a member from the system.
   *
   * @param member - The member to delete.
   */
  public void deleteMember(Member member) {
    if (member.isCurrentlyBorrowingItem()) {
      throw new IllegalArgumentException("Can't delete a member with active contracts");
    } else if (member.owningItemsAreLent()) {
      throw new IllegalArgumentException("Can't delete a member with planned or active contracts for it's items");
    }
    member.deleteAllItems(itemCatalog);
    memberRegistry.deleteMember(member);
  }

  public ArrayList<Member> getMembers() {
    ArrayList<Member> members = memberRegistry.getMembers();
    return members;
  }

  /**
   * Creates a new item and adds it to the member.
   *
   * @param owner       - The member to get the item.
   * @param cost        - Cost of the item.
   * @param category    - Category of the item.
   * @param description - Description of the item.
   */
  public void addItem(Member owner, int cost, ItemCategory category, String description) {
    // Member gets credits when adding item.
    owner.addCredits(100);
    Item item = new Item(cost, category, owner, description);
    itemCatalog.addItem(item);
    item.setDayOfCreation(getCurrentDay());
  }

  /**
   * Deletes Item from Member.
   *
   * @param owner - The member to delete the item.
   * @param index - The index of the item to delete.
   */
  public void deleteItem(Member owner, int index) {
    owner.deleteItem(index, itemCatalog, getCurrentDay());
  }

  /**
   * Adds a contract to a member, with the item and timeframe.
   *
   * @param borrowingMember - Member wanting to borrow item.
   * @param itemIndex - The index of the item in the catalog to borrow.
   */
  public void initContract(Member borrowingMember, int itemIndex, int from, int to) {
    checkItemAvailability(itemIndex, from, to);
    // count the cost to be paid up front.
    int amountOfDays = to - from;
    Item item = itemCatalog.getAllItems().get(itemIndex);
    int totalCost = amountOfDays * item.getCostPerDay();
  
    String ownerId = item.getOwner().getId();
    if (borrowingMember.getAmountOfCredits() >= totalCost || ownerId.equals(borrowingMember.getId())) {
      itemCatalog.setContractToItem(itemIndex, borrowingMember, from, to, getCurrentDay());
      // If member "borrows" it's own item, no transaction.
      if (!ownerId.equals(borrowingMember.getId())) {
        transaction(borrowingMember, item, totalCost);
      }
    } else {
      throw new IllegalArgumentException("Not enough credits");
    }
  }

  private void checkItemAvailability(int itemIndex, int from, int to) {
    if (!itemCatalog.getAllItems().get(itemIndex).isAvailablePeriod(from, to)) {
      throw new IllegalArgumentException("Item not available");
    }
  }

  /**
   * Transfers credits from member to owner of item.
   *
   * @param member - The member to transfer credits from.
   * @param itemIndex - The index of the item in the catalog.
   * @param totalCost - The total cost of the contract.
   */
  private void transaction(Member member, Item item, int totalCost) {
    String id = item.getOwner().getId();
    for (Member m : memberRegistry.getMembers()) {
      if (m.getId().equals(id)) {
        m.addCredits(totalCost);
      }
    }
    member.removeCredits(totalCost);
  }
  
  public String getItemsList() {
    return itemCatalog.getListOfItems();
  }

  public void setEmailToMember(Member member, String email) {
    memberRegistry.setEmail(member, email);
  }

  public void setPhoneNumberToMember(Member member, String phoneNumber) {
    memberRegistry.setPhoneNumber(member, phoneNumber);
  }

  public void changeItemInfo(Member member, int index, int cost, String description) {
    member.changeItemInfo(index, cost, description);
  }
}
