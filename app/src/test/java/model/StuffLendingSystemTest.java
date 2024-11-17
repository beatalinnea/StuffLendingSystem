package model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.domain.StuffLendingSystem;


public class StuffLendingSystemTest {

  @Test
  public void testMessageLengthGreaterThanZero() {
    StuffLendingSystem sut = new StuffLendingSystem();

    assertTrue(sut.getCurrentDay() >= 0, "The day is 0 or greater.");
  }
  
}
