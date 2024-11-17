package model.domain;

/**
 * Class representing the credits of an account.
 */
public class CreditAccount {
  private int currentAmount;

  public CreditAccount() {
    currentAmount = 0;
  }

  public CreditAccount(int amount) {
    currentAmount = amount;
  }

  protected void addCredits(int amount) {
    currentAmount += amount;
  }

  protected void removeCredits(int amount) {
    currentAmount -= amount;
  }

  public int getCurrentAmount() {
    return currentAmount;
  }
}
