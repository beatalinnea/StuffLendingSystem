package model.domain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class for Member Registry - keeping track of its members.
 */
public class MemberRegistry {
  private ArrayList<Member> registeredMembers;
  private Random random = new Random();

  /**
   * Constructor for Member Registry.
   */
  public MemberRegistry() {
    registeredMembers = new ArrayList<>();
  }

  protected ArrayList<Member> getMembers() {
    return registeredMembers;
  }

  /**
   * Adds a new member to the registry.
   *
   * @param name - Name of the member.
   * @param email - Email of the member.
   * @param currentDay - Day of registration.
   * @param phoneNumber - Phone number of the member.
   * @return the new added member.
   */
  protected Member addNewMember(String name, String email, int currentDay, String phoneNumber) {
    checkUniqueEmail(email);
    checkUniquePhoneNumber(phoneNumber);
    Member newMember = new Member(name, email, getUniqueId(), phoneNumber, currentDay);
    // newMember.setDayOfCreation(currentDay);
    registeredMembers.add(newMember);
    return registeredMembers.get(registeredMembers.size() - 1);
  }

  protected void deleteMember(Member member) {
    registeredMembers.remove(member);
  }

  /**
   * Generates a unique alpha numeric id for the member.
   *
   * @return the id.
   */
  private String getUniqueId() {
    while (true) {
      String id = generateId();
      boolean isUnique = true;
      for (Member m : registeredMembers) {
        if (m.getId().equals(id)) {
          isUnique = false;
          break;
        }
      }
      if (isUnique) {
        return id;
      }
    }
  }

  /**
   * Algorithm for generating unique alpha numeric  string.
   * Got inspired by:
   * https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/.
   *
   * @return generated string.
   */
  private String generateId() {
    String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
    StringBuilder sb = new StringBuilder(6);
    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(alphaNumericString.length());
      sb.append(alphaNumericString.charAt(index));
    }
    return sb.toString();
  }

  /**
   * Sets the email of the member, if the email is not already in use.
   *
   * @param member - the member to set the email.
   * @param newEmail - the new email.
   */
  protected void setEmail(Member member, String newEmail) {
    checkUniqueEmail(newEmail);
    member.setEmail(newEmail);
  }

  /**
   * Sets the email of the member.
   *
   * @param newEmail to be set.
   */
  private void checkUniqueEmail(String newEmail) {
    for (Member m : registeredMembers) {
      if (m.getEmail().equals(newEmail)) {
        throw new IllegalArgumentException("Email already exists");
      }
    }
  }

  /**
   * Sets the phone number of the member - if the phonenumber is not already in use.
   *
   * @param member - the member to set the phone number.
   * @param newPhoneNumber - the new phone number.
   */
  protected void setPhoneNumber(Member member, String newPhoneNumber) {
    checkUniquePhoneNumber(newPhoneNumber);
    member.setPhoneNumber(newPhoneNumber);
  }

  private void checkUniquePhoneNumber(String newPhoneNumber) {
    for (Member m : registeredMembers) {
      if (m.getPhoneNumber().equals(newPhoneNumber)) {
        throw new IllegalArgumentException("Phone number already exists");
      }
    }
  }
}
