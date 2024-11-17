package model.domain;

/**
 * Class representing the category of an item.
 */
public enum ItemCategory {
  TOOL("Tool"),
  VEHICLE("Vehicle"),
  GAME("Game"),
  TOY("Toy"),
  SPORT("Sport"),
  OTHER("Other");

  private String displayName;

  ItemCategory(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  /**
   * Returns an array of all ItemCategory values.
   */
  public ItemCategory[] getAllCategories() {
    return values();
  }
}
