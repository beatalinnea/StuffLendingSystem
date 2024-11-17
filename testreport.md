# Stuff Lending Test Report
Test Results for Stuff Lending System.

## Example Test Report

- Version #abb5fc4e
- Date: 2023.10.09
- Environment: MacOS, Java (OpenJDK) version 17.0.5
- Performed by: Beata Eriksson

| Case | Result | Note |
| ---- | ------ | ---- |
| 1.1  | ok     |      |
| 1.2  | ok     |      |
| 1.3  | ok     |      |
| 2.1  | ok     |      |
| 2.2  | ok     |      |
| 2.3  | ok     |      |
| 3.1  | ok     |      |
| 3.2  | ok     |      |
| 3.3  | ok     |      |
| 3.4  | ok     |      |
| 3.5  | ok     |      |
| 3.6  | ok     |      |
| 4.1  | ok     |      |
| 5.1  | ok     |      |
| ...  | ...    | ...  |

## Test Cases

### 1.1 Create Member
**Requirement:** 1.1, 1.1.1, 1.4

- Create a member with name: "Allan Turing", email: "allan@enigma.com", phone: "12345678".
- Check that the member is created correctly with an id according to the requirement by checking the member's full information.
- Quit the application.

### 1.2 Create Member - Duplicate Email and Phone
**Requirement:** 1.1, 1.1.1, 1.1.2, 1.4

- Create a member with name: "Allan", email: "allan@enigma.com", phone: "12345678".
- Check that the member is created correctly with an id according to the requirement by checking the member's full information.
- Create a member with name: "Turing", email: "allan@enigma.com", phone: "123123123".
- Check that the member is not created (duplicate email).
- Create a member with name: "Turing", email: "turing@enigma.com", phone: "12345678".
- Check that the member is not created (duplicate phone).
- Create a member with name "Turing", email: "turing@enigma.com", phone: "123123123".
- Check that the member is created correctly with an id according to the requirement by checking the member's full information.
- Quit the application.

### 1.3 Delete Member
**Requirement:** 1.1, 1.1.1, 1.1.2, 1.4

- Create a member with name: "Allan", email: "allan@enigma.com", phone: "12345678".
- Check that the member is created correctly with an id according to the requirement by checking the member's full information.
- Delete the member.
- Check that the member is deleted by listing all members (simple).
- Create a member with name: "Allan", email: "allan@enigma.com", phone: "12345678".
- Check that the member is created correctly with an id according to the requirement by checking the member's full information.
- Quit the application.

### 2.1 Create Item
- Create an item for a Member.
- Check that the item is created and part of the Member's items by inspecting the member's details.
- Check that the owner member has increased its credits with 100.

### 2.2 Delete Item
- Select a member with one or several items.
- Delete one of the member's items that is not involved in any contract.
- Check that the item was deleted from the member's owned items.

### 2.3 Delete Item
- Select a member with one or several items.
- Delete one of the member's items that is booked (i.e., a future contract).
- Check that the item was not (can't delete item if it has a future contract).

### 3.1 Create Contract
- Create a contract for I2 lending to M2 (M2 has enough funds), 3 days of lending, day 1 to and including day 3.
- Check that the contract was created.

### 3.2 Create Contract - not enough funds
- Create a contract for I1 lending to M2 (M2 does not have enough funds and is not the owner of the item), 3 days of lending (e.g., day 1 to and including day 3).
- Check that the contract was not created due to lack of funds.

### 3.3 Create Contract - not conflicting time
- Create a contract for I2 lending to M2, 3 days of lending, day 4 to and including day 6.
- Check that the contract was created.

### 3.4 Create Contract - conflicting time
- Create a contract for I2 lending to M2, 3 days of lending, day 6 to and including day 9.
- Check that the contract was not created due to conflicting time.

### 3.5 Create Contract - conflicting time
- Create a contract for I2 lending to M2, 3 days of lending, day 4 to and including day 9.
- Check that the contract was not created due to conflicting time.

### 3.6 Create Contract - conflicting time
- Create a contract for I2 lending to M2, 1 day of lending, day 9 to and including day 10.
- Check that the contract was not created due to conflicting time.

### 4.1 Advance Time
- Advance the time 10 days (10 times the command).
- Check that the contract are now set to 'Past' and all items are currently available.

### 5.1 Member Data
**Requirement:** 5

- Check that there are at least 3 Members.
- Check that one member (M1) has 500 credits. M1 has two items for lending, I1 with cost 50 and one cheap I2 cost 10.
- Check that one member (M2) with 100 credits has no items for lending.
- M2 initializes a contract to M1s item I1 for 1 days (current day 0 to next day 1)
- Check that M2 is currently borrowing the item
- Check that M2 has 50 credits
- Check that M1 has 550 credits
- Check that M1s item I1 is currently lent to M2 with the contract stating it is active when viewing item details