package model.domain;

/**
 * Class representing the day counter of the application.
 */
public class DayCounter {
  private int currentDay;

  public DayCounter() {
    currentDay = 0;
  }

  protected void incrementDay() {
    currentDay++;
  }

  protected int getCurrentDay() {
    return currentDay;
  }
}
