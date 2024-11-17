package model.persistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.domain.ItemCategory;
import model.domain.Member;
import model.domain.StuffLendingSystem;

/**
 * Responsible for reading files.
 */
public class FileReader implements FileReaderInterface {

  /**
   * Reads a file and adds members and items to the system.
   * Separation of concerns - Reading files in persistance folder.
   * Our application does not depend on wheter we are saving / reading data or not.
   *
   * @param theFile the file to read from.
   * @param stuffLendingSystem the system to add members and items to.
   */
  public void readFile(File theFile, StuffLendingSystem stuffLendingSystem) {
    // try catch with resources, closes the scanner even if an exception is thrown.
    try (Scanner scanner = new Scanner(theFile, "UTF-8")) {
      if (theFile == null) {
        throw new FileNotFoundException("File is null");
      }
      Member currentMember = null; // Initialize a variable to keep track of the current member

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();

        if (line.startsWith("-")) {
          // Create and add an item to the current member
          if (currentMember != null) {
            String[] itemInfo = line.substring(1).split(":");
            if (itemInfo.length == 3) {
              if (itemInfo[0].equals("TOOL")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.TOOL,
                    itemInfo[0]);
              } else if (itemInfo[0].equals("VEHICLE")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.VEHICLE,
                    itemInfo[1]);
              } else if (itemInfo[0].equals("OTHER")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.OTHER,
                    itemInfo[1]);
              } else if (itemInfo[0].equals("GAME")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.GAME,
                    itemInfo[1]);
              } else if (itemInfo[0].equals("TOY")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.TOY,
                    itemInfo[1]);
              } else if (itemInfo[0].equals("SPORT")) {
                stuffLendingSystem.addItem(currentMember, Integer.parseInt(itemInfo[2]), ItemCategory.SPORT,
                    itemInfo[1]);
              }
            }
          }
        } else {
          // Create a new member
          String[] memberInfo = line.split(":");
          if (memberInfo.length == 3) {
            currentMember = createMember(memberInfo, stuffLendingSystem);
          }
        }
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private Member createMember(String[] memberInfo, StuffLendingSystem stuffLendingSystem) {
    if (memberInfo.length == 3) {
      Member member = stuffLendingSystem.addMember(memberInfo[0], memberInfo[1], memberInfo[2]);
      return member;
    }
    return null;
  }
}
