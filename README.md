This is Minesweeper. It's pretty great.

## Running this program
Note that back when I coded this we were in Java 6, so it is based on Swing, and today when I try to run it with openjdk versions of Javas 8 and 9 I get exceptions. Using Oracle's official Java 10.0.2 release seems to work.

Use `javac *.java` followed by `java JavaSweeper`. Or `java -jar JavaSweeper.jar`.

## Original notes from 2012
I implement the whole thing with only 6 classes and one interface, favoring few complicated classes that do a lot to many simple classes that might do less. This is not generally regarded as good practice, but it works fairly well here. (None of the classes are very large or complicated in the grand scheme anyway.)

For playing the game: It works just like any other minesweeper you've ever seen. Open and run JavaSweeper to start. Use the menu to start a new game, get a hint, or look up previous high scores. (And yes, some keyboard shortcuts are enabled.) Note: You will need to play a couple of games to create some high scores for the high scores option to show. It logs a maximum of 5 games, so if you play more than 5, the lower-scoring ones will be deleted.

At the beginnings or ends of games, modified JOptionPanes are displayed, giving the user the ability to give information to the program and the program the ability to give information (such as "You've won" or "You've lost") back to the user. Note that the mysterious text box on the pane after a win is for high-score-logging purposes.

Most nullPointerExceptions and the like are handled. I spent a great deal of time dealing with edge cases to make sure the program is unlikely to crash and burn during use. For example, if the user inputs strings that are not entirely numerical into the text fields on the new game popup panel, the underlying code (which is unable to parse those strings to ints) throws an error and causes the values for number of x and y tiles to default to 15.

A note: I have modified the provided code slightly. It matters because my code will not work without these modified versions of the provided functions, so just be sure to use the versions I uploaded when grading.
