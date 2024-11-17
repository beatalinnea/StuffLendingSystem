package model.domain;

/**
 * Class representing an interval.
 */
public class Interval {
  private int from;
  private int to;

  public Interval(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public String toString() {
    return "days: " + from + " - " + to;
  }

  /**
   * Checks if this interval covers another interval.
   *
   * @param fromOther - from day to be compared with.
   * @param toOther   - to day to be compared with.
   * @return boolean
   */
  public boolean coversInterval(int fromOther, int toOther) {
    return (fromOther >= this.from && toOther <= this.to) || (fromOther <= this.from && toOther >= this.to) 
            || (fromOther <= this.from && toOther >= this.from && toOther <= this.to) 
            || (fromOther >= this.from && fromOther <= this.to && toOther >= this.to);
  }
}
