package model.domain;

/**
 * Class representing a lending contract.
 */
public class LendingContract {

  private Item item;
  private Interval interval;
  private ContractState state;
  private Member borrower;

  /**
   * Constructor for a lending contract.
   *
   * @param item - the item.
   * @param from - the starting day of the contract.
   * @param to - the ending day of the contract.
   * @param currentDay - the current day.
   */
  public LendingContract(Item item, int from, int to, int currentDay) {
    this.item = item;
    this.interval = new Interval(from, to); 
    setState(currentDay);
  }

  /**
   * Copy constructor.
   *
   * @param contract to be copied.
   */
  public LendingContract(LendingContract contract) {
    this.item = contract.item;
    this.interval = contract.interval;
    this.state = contract.state;
    this.borrower = contract.borrower;
  }

  /**
   * Enum representing the state of a contract.
   */
  public static enum ContractState {
    Active,
    Planned,
    Past
  }

  protected void setBorrower(Member m) {
    this.borrower = m;
  }

  protected void setState(int currentDay) {
    if (currentDay < interval.getFrom()) {
      state = ContractState.Planned;
    } else if (currentDay >= interval.getFrom() && currentDay <= interval.getTo()) {
      state = ContractState.Active;
    } else {
      state = ContractState.Past;
    }
  }

  protected boolean coversContract(int from, int to) {
    return this.interval.coversInterval(from, to);
  }

  /**
   * Checks if the contract is active.
   *
   * @return boolean - true if the contract is active, false otherwise.
   */
  public boolean isActive() {
    if (state == ContractState.Active) {
      return true;
    }
    return false;
  }

  // ==================== Public Getters ====================
  public ContractState getState() {
    return this.state;
  }

  /**
   * Returns name of the borrower.
   *
   * @return string name.
   */
  public String getBorrowerName() {
    return borrower.getName();
  }

  public String getIntervalString() {
    return interval.toString();
  }

  public int getStartingDay() {
    return interval.getFrom();
  }

  /**
   * Returns a string representation of the contract.
   *
   * @return a string representation of the contract.
   */
  public String toString() {
    return "\n  - " + item.getCategory() + ": " + item.getItemName() + "\n    cost per day: " 
        + item.getCostPerDay() + "\n    borrowing between days: " + interval.getFrom() + " - " 
        + interval.getTo(); 
  }
}
