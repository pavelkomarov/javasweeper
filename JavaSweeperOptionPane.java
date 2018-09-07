import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/*
 *JavaSweeperOptionPane provides three key static methods for
 *displaying popup windows and taking/giving data from/to the
 *user.
 *@author Pavel Komarov
 *@version 1
 */
public class JavaSweeperOptionPane{
   
	private static int DEFAULT = 15;//default side length of board
	
	/*
	 *newGame creates a JOptionPane and fills it with a couple of
	 *Panels, each of which contain other things like JLabels, JTextFields,
	 *and one JSlider. It then returns the user input so that it may be
	 *used in generating a new MinePanel and hence a new game.
	 *@return An int[] array of values corresponding to user input
	 */
	public static int[] newGame(){
		int[] userInput;
      JTextField xField = new JTextField(5);
      JTextField yField = new JTextField(5);
		JSlider slider = new JSlider(1,99,20);
		slider.setMajorTickSpacing(14);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);      
		
		JPanel northPanel = new JPanel();
		northPanel.add(new JLabel("Enter values for board size and percentage of tiles containing mines."));
		
		JPanel centerPanel = new JPanel();
      centerPanel.add(new JLabel("board width:"));
      centerPanel.add(xField);
      centerPanel.add(Box.createHorizontalStrut(15));//a spacer
      centerPanel.add(new JLabel("board height:"));
      centerPanel.add(yField);
		centerPanel.add(new JLabel("mine concentration (%): "));
		centerPanel.add(slider);

		JPanel newGamePanel = new JPanel();
		newGamePanel.setLayout(new BorderLayout());
		newGamePanel.add(northPanel,BorderLayout.NORTH);
		newGamePanel.add(centerPanel,BorderLayout.CENTER);
		newGamePanel.setPreferredSize(new Dimension(400,100));

		Object[] options = {"OK", "Cancel"};
      int optionSelected = JOptionPane.showOptionDialog(null,newGamePanel, 
         "New Game",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,
			null,options,options[0]);
		boolean parsable = true;
		try{
			Integer.parseInt(xField.getText());
			Integer.parseInt(yField.getText());
		}catch(NumberFormatException e){
			parsable = false;
		}
      if (optionSelected == JOptionPane.OK_OPTION && parsable) {
   		userInput = new int[3];    
			userInput[0] = Integer.parseInt(xField.getText());
         userInput[1] = Integer.parseInt(yField.getText());
			userInput[2] = slider.getValue();
      }
		else if (optionSelected == JOptionPane.OK_OPTION && !parsable){
			userInput = new int[3]; 
			userInput[0] = DEFAULT;//if user input to textboxes is nonsense,
         userInput[1] = DEFAULT;//set to default values
			userInput[2] = slider.getValue();
		}
		else{
			userInput = new int[0];
		}
		return userInput;
	}
	
	/*
	 *gameOver displays a JOptionPane of one of two flavors: different things
	 *go on the Pane if it is greeting a winning user than if it is greeting
	 *a user who has just lost. If the user has won, then JavaSweeper.updateHighScores
	 *is called so that if the user's gameplay was recordworthy, it ends up in the records.
	 *@param bool Did the user win? true means yes; false means no.
	 *@param x The board's width
	 *@param y The board's height
	 *@param time The time it took the user to win or lose
	 */
	public static void gameOver(boolean bool,int x,int y,int time){
		JPanel gameOverPanel = new JPanel();
		JTextField playerNameField = new JTextField(10);
		if (bool){
			gameOverPanel.add(new JLabel("      Congratulations, you won the game!"));
			gameOverPanel.add(new JLabel("Your Name: "));
			gameOverPanel.add(playerNameField);
			gameOverPanel.setPreferredSize(new Dimension(200,50));
		}
		else{
			gameOverPanel.add(new JLabel("Sorry, you lost this game. Better luck next time!"));
		}
		
		Object[] options = {"Exit", "Play Again"};
		int optionSelected = JOptionPane.showOptionDialog(null,gameOverPanel,
			"Game Over",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,
			null,options,options[0]);
		if (bool){
			JavaSweeper.updateHighScores(x,y,time,playerNameField.getText());		
		}
		if (optionSelected == JOptionPane.YES_OPTION)
			System.exit(0);
		else if (optionSelected == JOptionPane.NO_OPTION){
			JavaSweeper.startNewGame();
		}
	}
		
	/*
	 *displayHighScores creates a JFrame and fills it with a table of
	 *high scores. These high scores are read in using JavaSweeper.getHighScores
	 *and displayed whenever the "High Scores" option is selected from the menu.
	 */
	public static void displayHighScores(){
		ArrayList<String> highScoresAL = JavaSweeper.getHighScores();
		JPanel highScoresPanel = new JPanel();
		highScoresPanel.setLayout(new GridLayout(0,3,5,5));
		highScoresPanel.add(new JLabel("  Grid"));
		highScoresPanel.add(new JLabel("Time"));
		highScoresPanel.add(new JLabel("Player  "));
		
		for (int i=0;i<highScoresAL.size();i++){
			String[] scores = highScoresAL.get(i).split(",");
			highScoresPanel.add(new JLabel("  "+scores[0]+"X"+scores[1]));
			highScoresPanel.add(new JLabel(scores[2]));
			highScoresPanel.add(new JLabel(scores[3]+"  "));
		}
		
		JFrame highScoresFrame = new JFrame("High Scores");
		highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		highScoresFrame.getContentPane().add(highScoresPanel);
		highScoresFrame.pack();
		highScoresFrame.setVisible(true);
	}
	
	
}