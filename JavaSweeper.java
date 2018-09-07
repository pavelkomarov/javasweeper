import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

/*
 *JavaSweeper: my ripoff of minesweeper.
 *@author Pavel Komarov
 *@version 1
 *I completed this assignment alone, using only course materials.
 */
public class JavaSweeper{

	private static JFrame frame = new JFrame("JavaSweeper");
	
	/*
	 *The JavaSweeper main calls the static newGame method from
	 *JavaSweeperOptionPane to take input from the user, creates 
	 *and fills a JFrame with a MinePanel, and adds a menu bar.
	 *@param args A string of inputs from the command line (empty here)
	 */
	public static void main(String[] args){
		int[] userInput = JavaSweeperOptionPane.newGame();
		if (userInput.length!=0){
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			MinePanel game = new MinePanel(userInput[0],userInput[1],userInput[2]);
			frame.getContentPane().add(game);
			frame.setJMenuBar(new JavaSweeperMenu(game).getMenuBar());
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	/*
	 *startNewGame begins a new game by calling for more user input,
	 *clearing the JFrame, and then repopulating the JFrame with a
	 *new MinePanel and a new JMenuBar
	 */
	public static void startNewGame(){
		int[] userInput = JavaSweeperOptionPane.newGame();//if the user doesn't hit cancel
		if (userInput.length!=0){							//do everything necessary to start a
			frame.getContentPane().removeAll();			//new game
			MinePanel game = new MinePanel(userInput[0],userInput[1],userInput[2]);
			frame.getContentPane().add(game);
			frame.setJMenuBar(new JavaSweeperMenu(game).getMenuBar());
			frame.pack();
			frame.validate();
		}
	}
	
	/*
	 *updateHighScores calls getHighScores to get an ArrayList of
	 *high scores as written to highscores.txt. It then inserts (or
	 *doesn't) the current player's score into the highscore table
	 *as appropriate. At the end, the ArrayList is written back to
	 *the file.
	 *@param x The number of columns in the MinePanel
	 *@param y The number of rows in the MinePanel
	 *@param	player A user-specified string indicating their name
	 */
	public static void updateHighScores(int x,int y,int time,String player){
		File scoresFile = new File("highscores.txt");
		ArrayList<String> highScoresArrayList = getHighScores();
		PrintWriter outFile;
		
		if (player.equals(""))
			player = "Anonymous";
		try{
			boolean inserted = false;
			for (int i=0;i<highScoresArrayList.size();i++){//insert score into appropriate ArrayList position
				String[] score = highScoresArrayList.get(i).split(",");
				if ((x*y > Integer.parseInt(score[0])*
					Integer.parseInt(score[1]) || 
					(x*y == Integer.parseInt(score[0])*
					Integer.parseInt(score[1]) && time <=
					Integer.parseInt(score[2]))) && !inserted){
        			highScoresArrayList.add(i,new String(x+","+y+","+time+","+player));
					inserted = true;
            }
			}
			if (!inserted)
				highScoresArrayList.add(new String(x+","+y+","+time+","+player));
			if (highScoresArrayList.size() > 5){//trim ArrayList if it's gotten too long
				highScoresArrayList.remove(5);
			}
			outFile = new PrintWriter(new BufferedWriter(new FileWriter(scoresFile)));
			for (int i=0;i<highScoresArrayList.size();i++){//write to file
				outFile.println(highScoresArrayList.get(i));
			}
			outFile.close();   		
		}
		catch (IOException e){System.out.println("what?");}
	}
	
	/*
	 *getHighScores reads high scores from a file and returns them
	 *in the form of an ArrayList.
	 *@return An ArrayList of high score Strings
	 */
	public static ArrayList<String> getHighScores(){
		BufferedReader inFile;
		File scoresFile = new File("highscores.txt");
		ArrayList<String> highScoresAL = new ArrayList<String>();
		if (!scoresFile.exists()){
			try{
				scoresFile.createNewFile();
			}
			catch (Exception e){}
		}
		try{
			inFile = new BufferedReader(new FileReader(scoresFile));
			String scoreLine = inFile.readLine();
			while (scoreLine!=null){
				highScoresAL.add(scoreLine);
				scoreLine = inFile.readLine();
			}
			inFile.close();
		}
		catch(Exception e){}
		return highScoresAL;
	}

}