import java.util.*;
import java.awt.image.*;

/*
 *Tile objects are the fundamental units of JavaSweeper.
 *@author Pavel Komarov
 *@version 1
 */
public class Tile implements MineTile{

	private BufferedImage currentImage;
	private int x,y;
	private boolean hasMine;

	/*
	 *The Tile constructor initializes the Tile's row,
	 *column, Image, and sets its boolean hasMine value
	 *to a default of false.
	 *@param col The Tile's column
	 *@param row The Tile's row
	 *@param i A BufferedImage to represent the object in the MinePanel
	 */
	public Tile(int col,int row,BufferedImage i){
		x = col;
		y = row;
		currentImage = i;
		hasMine = false;
	}
	
	/*
	 *@return An int corresponding to the Tile's row
	 */
	public int getRow(){
		return y;
	}
	
	/*
	 *@return An int corresponding to the Tile's row
	 */
	public int getCol(){
		return x;
	}
	
	/*
	 *getAdjacentMines calls MineUtils' getAdjacentTiles method
	 *to find valid Tiles surrounding the acted-upon Tile. It then
	 *counts the number of these Tiles with hasMine set to true.
	 *@param board A 2D array of Tile objects
	 *@return An int corresponding to the number of mines contained by surrounding Tiles
	 */
	public int getAdjacentMines(Tile[][] board){
		ArrayList<Tile> tilesToCheck = MinesweeperUtils
			.getAdjacentTiles(this.getCol(),this.getRow(),board);
		int numAdjacentMines = 0;
		for (Tile square : tilesToCheck){
			if (square.containsMine())
				numAdjacentMines++;
		}
		return numAdjacentMines;
	}
	
	/*
	 *@return The boolean value of the Tile's hasMine instance data
	 */
	public boolean containsMine(){
		return hasMine;
	}
	
	/*
	 *Make a Tile's hasMine true.
	 */
	public void setMine(){
		hasMine = true;
	}
	
	/*
	 *@return BufferedImage The Tile's currently-set image
	 */
	public BufferedImage getImage(){
		return currentImage;
	}
	
	/*
	 *@param i A BufferedImage to which the Tile's currentImage
	 *												should be set
	 */
	public void setImage(BufferedImage i){				 
		currentImage = i;
	}
	
}