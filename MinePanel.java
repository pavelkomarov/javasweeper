import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

/*
 *Perhaps the most extensive class of this project, MinePanel
 *is the heart and soul of the game. It manages a game and displays
 *that game on a Graphics object.
 *@author Pavel Komarov
 *@version 1
 */
public class MinePanel extends JPanel{
	
	private Tile[][] board;
	private int numXTiles,numYTiles,numMines,numFlags=0,
								seconds=0,TILESIZE=20;
	private boolean firstClick;
	private Random rand;
	private Timer clock = new Timer(1000,new TimerListener());
	private BufferedImage all,blank,flag,mine,red_mine,zero,one,
								two,three,four,five,six,seven,eight;
	private ArrayList<Tile> tilesWithMines = new ArrayList<Tile>();
	
	/*
	 *The MinePanel constructor starts by loading in all the necessary
	 *images, adds a MouseListener to itself, initializes important
	 *variables according to parameter values,sets up a board (a 2D
	 *array of Tile objects) and starts the clock. 
	 *@param wid The number of tiles wide the board will be
	 *@param hei The number of tiles high the board will be
	 *@param num The percentage tiles to get mines 
	 */
	public MinePanel(int wid,int hei,int num){
		loadImages();
		addMouseListener(new MinePanelListener());
		numXTiles = wid;
		numYTiles = hei;
		numMines = (int)Math.floor(wid*hei*(double)num/100);
		firstClick = true;
		rand = new Random();
		board = new Tile[numXTiles][numYTiles];
		for (int i=0; i<board.length;i++){
			for (int j=0; j<board[0].length;j++){
				board[i][j] = new Tile(i,j,blank);//create a playing board
			}
		}
		setPreferredSize(new Dimension(numXTiles*TILESIZE,numYTiles*TILESIZE+40));
		clock.start();
	}
	
	/*
	 *I discovered getSubImage. It's great. Originally, this private method's
	 *home was the Tile object, but I quickly realized that loading in hundreds
	 *of copies of each of these images (one for each Tile) was not nearly as
	 *efficient as just keeping one set of images in the MinePanel.
	 */
	private void loadImages(){
		try{//having only one image to load simplifies things
			all = ImageIO.read(new File("minesweeper_tiles.png"));//move to panel
			blank = all.getSubimage(0,0,64,64);				//too cumbersome for each
			flag = all.getSubimage(64,0,64,64);				//tile to hold all images
			mine = all.getSubimage(128,0,64,64);
			red_mine = all.getSubimage(0,192,64,64);
			zero = all.getSubimage(192,0,64,64);
			one = all.getSubimage(0,64,64,64);
			two = all.getSubimage(64,64,64,64);
			three = all.getSubimage(128,64,64,64);
			four = all.getSubimage(192,64,64,64);
			five = all.getSubimage(0,128,64,64);
			six = all.getSubimage(64,128,64,64);
			seven = all.getSubimage(128,128,64,64);
			eight = all.getSubimage(192,128,64,64);
		}
		catch (IOException e){}
	}
	
	/*
	 *paintComponent first clears a rectangle. This is necessary because
	 *old boards do not erase themselves. It then draws a couple of strings
	 *(the Timer and Mines Remaining) and draws each Tile object according
	 *to it's location in board and specific BufferedImage.
	 *@param page A Graphics object on which the method draws
	 */
	public void paintComponent(Graphics page){
		if (board.length*TILESIZE<205)
			page.clearRect(0,0,210,board[0].length*TILESIZE+40);
		else
			page.clearRect(0,0,board.length*TILESIZE,board[0].length*TILESIZE+40);
		page.drawString(Integer.toString(seconds),15,board[0].length*TILESIZE+25);
		page.drawString("Mines Remaining: "+Integer.toString(numMines-numFlags),
			board.length*TILESIZE-130,board[0].length*TILESIZE+25);
		for (int i=0; i<board.length;i++){
			for (int j=0; j<board[0].length;j++){
				page.drawImage(board[i][j].getImage(),i*TILESIZE,
					j*TILESIZE,TILESIZE,TILESIZE,null);
			}
		}
	}
	
	/*
	 *A MouseListener. Definitely the largest I have written.
	 */
	private class MinePanelListener implements MouseListener{
	
		/*
		 *mouseClicked contains listens for right and left clicks and takes
		 *the appropriate action according to where the click was and which
		 *button was pressed. If the click is not on a tile, the method does
		 *nothing other than call repaint().
		 *@param event A MouseEvent generated when the user clicks
		 */
		public void mouseClicked(MouseEvent event){
			int button = event.getButton();
			Tile t = getTileAtPoint(event.getPoint());
			if (button==MouseEvent.BUTTON1 && t!=null){
				if (t.getImage().equals(blank)){//if the tile is not already pressed or is not flagged
					if (firstClick){
						placeMines(t);
						firstClick = false;
					}
					if (t.containsMine()){
						showMines(t);
						gameLost();
					}
					else if (t.getAdjacentMines(board)==0){
						ArrayList<Tile> tilesToOpen = MinesweeperUtils.expandTile(t,board);
						for (Tile square : tilesToOpen){
							square.setImage(getImageFromCode(square.getAdjacentMines(board)));
						}
					}
					else{
						t.setImage(getImageFromCode(t.getAdjacentMines(board)));
					}
					testGameWon();
				}
			}
			else if (button==MouseEvent.BUTTON3 && t!=null){
				if (t.getImage().equals(blank)){
					t.setImage(flag);
					numFlags+=1;
				}
				else if (t.getImage().equals(flag)){
					t.setImage(blank);
					numFlags-=1;
				}
			}
			repaint();
		}
	
		/*
		 *It is useful to be able to edit a tile clicked or know
		 *which tile was clicked. This method finds such a tile
		 *given a point, which can easily be found from a MouseEvent
		 *via getPoint
		 *@param p The point at which to find the Tile
		 *@return The Tile found
		 */
		private Tile getTileAtPoint(Point p){//return the tile at the point clicked
			int i = (int)p.getX()/TILESIZE;
			int j = (int)p.getY()/TILESIZE;
			if (i<board.length && j<board[0].length)
				return board[i][j];
			else
				return null;
		}
	
		/*
		 *placeMines takes in a Tile and uses a Random object to place 
		 *mines in Tiles on the board excepting that Tile. This is useful
		 *after the user's first click. Note also: tilesWithMines is
		 *filled here.
		 *@param t A Tile a mine should not be placed on
		 */
		private void placeMines(Tile t){
			boolean addMine;
			ArrayList<Point> tilePointArrayList = new ArrayList<Point>();
			tilePointArrayList.add(new Point(t.getCol(),t.getRow()));//tiles with points logged in the
			int n = 0;															//ArrayList are either the first tile
			while (n<numMines){												//clicked or already have a mine.
				addMine = true;												//duplicates and mines on first tile
				int i = rand.nextInt(numXTiles);							//are not allowed by this method.
				int j = rand.nextInt(numYTiles);
				for (Point p : tilePointArrayList){
					if (p.equals(new Point(i,j)))
						addMine = false;
				}
				if (addMine){
					board[i][j].setMine();
					tilePointArrayList.add(new Point(i,j));
					tilesWithMines.add(board[i][j]);
					n++;	
				}
			}
		}
		
		/*
		 *When testing how many mines surround a tile, the obvious
		 *response is an int. This method takes in that int and returns
		 *the image corresponding to it so that the-tile-investigated's
		 *image might be changed accordingly.
		 *@param i The number of mines surrounding a Tile
		 *@return The corresponding BufferedImage
		 */
		private BufferedImage getImageFromCode(int i){
			if (i==0)
				return zero;
			else if (i==1)
				return one;
			else if (i==2)
				return two;
			else if (i==3)
				return three;
			else if (i==4)
				return four;
			else if (i==5)
				return five;
			else if (i==6)
				return six;
			else if (i==7)
				return seven;
			else if (i==8)
				return eight;
			else
				return blank;
		}
		
		/*
		 *showMines changes all the Tiles-containing-mines' images to
		 *that of a mine. The particular tile clicked gets a red background.
		 *@param t A Tile to be assigned the special Image red_mine
		 */
		private void showMines(Tile t){
			for (int i=0; i<board.length;i++){
				for (int j=0; j<board[0].length;j++){
					if (board[i][j].containsMine() &&
						(i!=t.getCol()||j!=t.getRow()))
						board[i][j].setImage(mine);
					else if(board[i][j].containsMine() &&//marks the mine clicked as red
						(i==t.getCol()&&j==t.getRow()))
						board[i][j].setImage(red_mine);
				}
			}
			repaint();
		}
		
		/*
		 *When the game ends, several things need to happen. This method stops
		 *the clock and calls the gameOver method in JavaSweeperOptionPane with
		 *parameteres such that the ensuing pane contains messages concerning a loss.
		 */
		private void gameLost(){
			clock.stop();
			JavaSweeperOptionPane.gameOver(false,numXTiles,numYTiles,seconds);
		}
		
		/*
		 *testGameWon is so named because it is automatically called after
		 *every click so that a win may be caught and dealt with immediately.
		 *If the number of tiles flagged and not-pressed sums to be the number
		 *of mines known to be on the board, then the user has won. In this
		 *event the clock is stopped, and gameOver is again called, but this
		 *time with parameters such that the ensuing pane will contain a
		 *congratulatory message and a text box so they may enter their name in
		 *the high scores.
		 */
		private void testGameWon(){
			int numTilesLeft=0;
			for (int i=0; i<board.length;i++){
				for (int j=0; j<board[0].length;j++){
					if (board[i][j].getImage().equals(blank) ||
						board[i][j].getImage().equals(flag))
						numTilesLeft+=1;			
				}
			}
			if (numTilesLeft==numMines){
				clock.stop();
				repaint();
				JavaSweeperOptionPane.gameOver(true,numXTiles,numYTiles,seconds);
			}
		}
		
		/*
		 *Other mouseListener methods must be dealt with.
		 */
		public void mousePressed(MouseEvent event){}
		public void mouseReleased(MouseEvent event){}
		public void mouseEntered(MouseEvent event){}
		public void mouseExited(MouseEvent event){}
	}
	
	/*
	 *The Timer is set up to generate ActionEvents every 1000ms.
	 *This listener class deals with such a time increment.
	 */
	private class TimerListener implements ActionListener{
		/*
		 *@param event An ActionEvent generated by the Panel's Timer
		 */
		public void actionPerformed(ActionEvent event){
			seconds+=1;
			repaint();
		}
	}

	/*
	 *getHint uses an array of tiles known to contain mines to find a mine.
	 *This random, unflagged mine is then displayed and everything updated
	 *accordingly.
	 */
	public void getHint(){
		if (tilesWithMines.size()>0){
			boolean found = false;
			while (!found){
				int i = rand.nextInt(tilesWithMines.size());
				if (tilesWithMines.get(i).containsMine() &&
					tilesWithMines.get(i).getImage().equals(blank)){
					tilesWithMines.remove(i).setImage(mine);
					numFlags++;
					found = true;
				}
			}
			repaint();
		}
	}
	
}