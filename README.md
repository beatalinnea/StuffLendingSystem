# About 
This is a student project made in the course 1DV607 - Object Oriented Analysis and Design using UML.         

The application is built in Java using Gradle and is a console menu application.


Assignment 2 - Stuff Lending System
I (Beata Eriksson, be222gr) have worked alone in this assignment.

[design.md](design.md) contains the diagrams describing the systems architecture.

## Running
The application should start by running console command:  
`./gradlew run -q --console=plain`

## Usage
- The application contains options that you get presentet with through its menus. The application expects certain input given with each option. If you type in an unvalid input, that is not a given option or prompting you for a name etc, then the application will handle this and send you back to the start menu, not crash.

## Notes
- At first I thought that when the days gets pushed, the borrower pays for the item per day that it is lent. But finally decided to interpret the requirements as the borrower pays up front when initializing a contract. That's what I decided to go for. 
- My build is successful although there are FindBugs warning about mutual objects. I have supressed one of these warnings in controller.MenuManager for the build to go through. I get the warnings for storing externally mutual objects in to the class attributes. For this project I thought it was necessary to implement it this way.
- The problem description states that we are to use Item name and Item description - I did choose to not include both. I took this decision for readability purposes in the console. I am hoping this decision was OK to make, many questions regarding the requirements have come up for discussion and I got the intention that it was OK to take some own decisions regarding the details if we state why. If I were to implement functionality for both description and name for item I would have handled the same in my implementation, since they would both be of type String and contain a chosen text. I thought it was enough to have a category and a descriptive name - for it to be like "Tool: hammer" - and i preferred it that way.
- I also chose to to some editing for the tests - such as an item can't be deleted if it is contained within a contract that is either active or planned. Also I chose to not be able to delete a member if it is currently borrowing an item or is currently having one of its items borrowed to someone else.

## Building
The build must pass by running console command:  
`./gradlew build`  
Note that you should get a report over the quality like:
```
CodeQualityTests > codeQuality() STANDARD_OUT
    0 CheckStyle Issues in controller/App.java
    0 CheckStyle Issues in controller/Simple.java
    0 CheckStyle Issues in model/Simple.java
    0 CheckStyle Issues in view/Simple.java
    0 FindBugs Issues in controller/App.java
    0 FindBugs Issues in model/Simple.java
    0 FindBugs Issues in view/Simple.java
    0 FindBugs Issues in controller/Simple.java
```

Removing or manipulating the code quality checks results in an immediate assignment **Fail**. 
