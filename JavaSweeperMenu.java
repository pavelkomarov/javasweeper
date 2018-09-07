import javax.swing.*;
import java.awt.event.*;

/*
 *JavaSweeperMenu creates the menu for JavaSweeper.
 *@author Pavel Komarov
 *@version 1
 */
public class JavaSweeperMenu{
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu;
	private JMenuItem newGameMenuItem,hintMenuItem,highScoresMenuItem;
	private int count=0;
	private MinePanel game;
	
	/*
	 *The JavaSweeperMenu constructor creates all the menu options
	 *necessary for JavaSweeper.
	 *@param mp The MinePanel currently displayed in JavaSweeper
	 */
	public JavaSweeperMenu(MinePanel mp){
		menu  = new JMenu("Game");
		menuBar.add(menu);
		game = mp;

		newGameMenuItem = new JMenuItem("New Game");
		newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(
			java.awt.event.KeyEvent.VK_N,java.awt.Event.CTRL_MASK));
		newGameMenuItem.addActionListener(new MenuListener());
		menu.add(newGameMenuItem);

		hintMenuItem = new JMenuItem("Hint");
		hintMenuItem.setAccelerator(KeyStroke.getKeyStroke(
			java.awt.event.KeyEvent.VK_H,java.awt.Event.CTRL_MASK));
		hintMenuItem.addActionListener(new MenuListener());
		menu.add(hintMenuItem);
		
		highScoresMenuItem = new JMenuItem("High Scores");
		highScoresMenuItem.addActionListener(new MenuListener());
		menu.add(highScoresMenuItem);
	}

	/*
	 *I learned menus are monitored for actions the same way
	 *JButtons are.
	 */
	private class MenuListener implements ActionListener{
	
		/*
		 *This implementation of actionPerformed listens for a menu item
		 *to be pressed. If it hears such an event, it identifies which
		 *item was selected and responds accordingly. Note the game.getHint()
		 *line under the first else if. That is the whole purpose of taking 
		 *in a MinePanel object in the constructor.
		 *@param event An event generated when a menu item is selected
		 */
		public void actionPerformed(ActionEvent event){
			if (event.getSource()==newGameMenuItem)
				JavaSweeper.startNewGame();
			else if (event.getSource()==hintMenuItem)
				game.getHint();
			else if (event.getSource()==highScoresMenuItem)
				JavaSweeperOptionPane.displayHighScores();
		} 
	}
	
	/*
	 *Since a JavaSweeperOptionPane object is not a JMenuBar
	 *object, but rather contains a JMenuBar object, and since
	 *it is this menu bar that is needed for display in the 
	 *JFrame, this method provides a way of access.
	 *@return A JMenuBar to be displayed in the JavaSweeper main frame
	 */
	public JMenuBar getMenuBar(){
		return menuBar;
	}
}