//------------------------------------------------------------------//
// Board.java                                                       //
//                                                                  //
// Class used to represent a 2048 game board                        //
//                                                                  //
// Author:  Brandon Williams                                        //
// Date:    1/17/15                                                 //
//------------------------------------------------------------------//
/*
 * Name: Hanna Goldman
 * Login : cs8balv
 * Date: 1/29/15
 * File: Board.java
 *
 * This program creates a game board for the game 2048. It can either start a
 * new game or load an old one. It contains methods to move the tiles
 * on the board and add a new random tile with each valid move and can also
 * check to see when the game is over.
 */

/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 */

import java.util.*;
import java.io.*;
/*
 * Name: Board
 * Purpose: This class contains constrcutors to create a new Board to begin a game
 *          and adds two random tles to the Board in random locations or
 *          creates a Board from an input file. It also uses the methods 
 *          saveBoard to save the current board to a file and addRandomTile
 *          which adds an int, either 2 or 4, to a random empty location on the
 *          Board. Board also has methods to move the tiles on the board and
 *          to check if there is a valid move to be made. Lastly, it contains
 *          a method to check if the game is over.
 */
public class Board
{
  // Instance variables

  // the number of tiles your game starts with
  public final int NUM_START_TILES = 2;
  // the probably that your random tile placed will be a two
  public final int TWO_PROBABILITY = 90;
  // the size of your grid
  public final int GRID_SIZE;

  // creates a random generator to be used when placing tiles
  private final Random random;
  // grid is a double integer array
  private int[][] grid;
  // the score of your game will be set to zero unless changed
  private int score = 0;

  /*
   * Name: Board(int boardSize, Random random)
   * Purpose: This is a constructor for a Board that has no input file. This
   *          constructor takes in an int and a Random as parameters. This 
   *          int is used as the size of the grid that is to be made and the
   *          random is just set as the Random for the board. The size is
   *          automatically set from our instance variable for the class.Then
   *          the grid is set to a new doule int array with our grid size as
   *          the size of the row and column arrays. Then it adds a certain
   *          numbers of random tiles, depending on what your NUM_START_TILES
   *          is equal to.
   * Parameters: The parameter boardSize is an integer and it determines
   *             the size of the Board (3x3, 4x4, nxn)
   *             The parameter random is the Random for the Board.
   */
  public Board(int boardSize, Random random)
  {
    this.random = random;
    GRID_SIZE = boardSize; 
    this.grid = new int[boardSize][boardSize];
    for ( int i=0; i < NUM_START_TILES; i++) {
      this.addRandomTile();
    }
  }

  /*
   * Name: Board(String inputBoard, Random random)
   * Purpose: This is a constructor for a Board that takes an input file as
   a parameter. It runs through the file and gets the grid size
   from the first integer in the array and the score from the 
   second integer. It then sets the grid as a double integer
   array with the size of the rows and columns as the grid size.
   It thens runs through the file row first, column second and 
   sets the values of the grid as the next integers in the file.
   * Parameters: The parameter inputBoard is a String. This String comes
   from a saved file from a game that was saved previously.
   The parater random is the Random for the Board.
   */
  public Board(String inputBoard, Random random) throws IOException
  {
    File file = new File( inputBoard );
    Scanner scanner = new Scanner( file );
    this.random = random;
    GRID_SIZE = scanner.nextInt();
    score = scanner.nextInt();
    this.grid = new int[GRID_SIZE][GRID_SIZE];
    // runs through rows first, columns second
    for ( int i=0; i< GRID_SIZE; i++) {
      for( int j=0; j< GRID_SIZE; j++) {
        grid[i][j] = scanner.nextInt();
      }
    }
    scanner.close();
  }

  /*
   * Name: saveBoard( String outputBoard )
   * Purpose: This method uses printwriter to turn your board into a file.
   In the file it first prints the grid size of your board, the 
   the score, and then runs through your board row first column
   second, and prints the values into your file in the form of
   a board.
   * Parameters: The parameter outputBoard is a String which the values from
   the board are saved to.              
   * Return: void              
   */
  public void saveBoard(String outputBoard) throws IOException
  {
    // Creates a new file named depending on your parameter
    PrintWriter printwriter = new PrintWriter( outputBoard );
    printwriter.println( GRID_SIZE );
    printwriter.println( score );
    // runs through row first, column second
    for ( int i=0; i< GRID_SIZE; i++) {
      for ( int j=0; j< GRID_SIZE; j++) {
        printwriter.print( grid[i][j] + " " );
      }
      printwriter.println();
    }
    printwriter.close();
  }

  /*
   * Name: addRandomTile()
   * Purpose: This method first sets count to zero and runs through the grid.
   *          If the value at any point is equal to zero, it adds to the 
   *          count. If the count at the end of the loop is zero, it exits 
   *          out of the method because there is nowhere to add a tile. 
   *          Otherwise it picks a random integer, called location, between
   *          zero and count. Then it picks a random integer, called value,
   *          between 0 and 100. It then sets count to -1 and runs through
   *          the grid row first, column second. Whenever it finds a zero 
   *          value and increases count. If your count is equal to your
   *          integer location, it then checks to see if your integer value
   *          is between 0 and 90. If it is, it sets the value at that point 
   *          in your grid equal to 2. If it is not between 0 and 90, it sets
   *          the value at the point in your grid equal to 4.
   * Parameters: none
   * Return: void
   */
  public void addRandomTile()
  {
    int count = 0;
    //runs through row first, column second
    for ( int  i=0; i < this.grid.length; i++) {
      for ( int  j=0; j < this.grid[i].length; j++) {
        // checks to see if the value at the point in the grid is zero
        if ( this.grid[i][j] == 0 ) {
          //increases count to check how many of your tiles equal zero
          count++;
        }
      }
    }  
    if ( count == 0 ) {
      // exits method if count is zero because there is nowhere to add a tile
      return;
    }
    int location = random.nextInt(count);
    int percent = 100;
    int value = random.nextInt(percent);
    int mostLikely = 2;
    int lessLikely = 4;
    count = -1;
    // runs through row first, column second
    for ( int i=0; i< this.grid.length; i++ ) {
      for ( int j=0; j< this.grid[i].length; j++ ) {
        if ( this.grid[i][j] == 0 ) {
          count++;
          // checks to see if your value for count is the same as the random
          // integer location
          if ( count == location ) {
            // 90% of the time, the random tile placed will be a two
            if ( value < TWO_PROBABILITY && value >=0 ) {
              this.grid[i][j] = mostLikely;
            }
            else {
              this.grid[i][j] = lessLikely;
            }
          }
        }  
      }
    }  
  }

  /*
   * Name: isGameOver()
   * Purpose: This method checks if it is possible to move the tiles on your
   *          board either up, down, left or right.
   * Parameters: none
   * Return: boolean, the method returns true if the game is over and false if
   *         it is not.
   */
  public boolean isGameOver()
  {
    if ( this.canMove(Direction.UP) || this.canMove(Direction.DOWN) ||
        this.canMove(Direction.LEFT) || this.canMove(Direction.RIGHT) ) {  
      return false;
    }
    else {
      return true;
    }  
  }

  /* 
   * Name: canMove( Direction direction )
   * Purpose: Checks to see if the tiles on the  board can move in the direction
   *          that the user is attempting to move
   * Parameters: Direction direction, represents the direction that the person
   *             entered to move the tiles on their board
   * Return: boolean, returns true if the board can move in the direction the
   *         user inputted. Returns false if it can not.
   */
  public boolean canMove(Direction direction)
  {
    if ( direction.equals( Direction.LEFT )) {
      return this.canMoveLeft();
    }
    if ( direction.equals( Direction.RIGHT )) {
      return this.canMoveRight();
    }
    if ( direction.equals( Direction.DOWN )) {
      return this.canMoveDown();
    }
    if ( direction.equals( Direction.UP )) {
      return this.canMoveUp();
    }
    return false;
  }

  /* 
   * Name: canMoveLeft()
   * Purpose: Checks to see if the user can move left by seeing if there is a
   *          zero to the left of any of your tiles or checks if there are 
   *          two tiles that are equal to one another in the same row.
   * Parameters: none 
   * Return: boolean, returns true if the tiles on your board can move left.
   *         Returns false if not.
   */
  private boolean canMoveLeft()
  {
    // runs through board row first, column second
    for ( int row = 0; row < grid.length ; row++ ) {
      for ( int column = 1; column < grid[row].length; column++ ) {

        // looks at tile directly to the left of the current tile
        int compare = column-1;
        if ( grid[row][column] != 0 ) {
          if ( grid[row][compare] == 0 || grid[row][column] ==
              grid[row][compare] ) {
            return true;
          }
        }
      }
    }  
    return false;
  }

  /* 
   * Name: canMoveRight()
   * Purpose: Checks to see if the user can move right by seeing if there is a
   *          zero to the right of any of your tiles or checks if there are 
   *          two tiles that are equal to one another in the same row.
   * Parameters: none
   * Return: boolean, returns true if the tiles on your board can move right.
   *         Returns false if not.
   */
  private boolean canMoveRight()
  {
    // runs through row first, column second
    for ( int row = 0; row < grid.length ; row++ ) {
      for ( int column = grid[row].length-2; column >=0 ; column-- ) {

        // looks at tile directly to the right of the current tile
        int compare = column + 1;
        if ( grid[row][column] != 0 ) {
          if ( grid[row][compare] == 0 || grid[row][column] ==
              grid[row][compare] ) {
            return true;  
          }
        }
      }
    }  
    return false;
  }

  /* 
   * Name: canMoveUp()
   * Purpose: Checks to see if the user can move up by seeing if there is a
   *          zero above any of your tiles or checks if there are two tiles
   *          that are equal to one another in the same column
   * Parameters: none
   * Return: boolean, returns true if the tiles on your board can move up.
   *         Returns false if not.
   */
  private boolean canMoveUp()
  {
    // runs through column first, row second
    for ( int column = 0; column < grid[0].length ; column++ ) {
      for ( int row = 1; row < grid.length; row++ ) {
        // looks at tile directly above the current tile
        int compare = row-1;
        if ( grid[row][column] != 0 ) {
          if ( grid[compare][column] == 0 || grid[row][column] ==
              grid[compare][column] ) {
            return true;
          }
        }
      }
    }  
    return false;
  }

  /* 
   * Name: canMoveDown()
   * Purpose: Checks to see if the user can move down by seeing if there is a
   *          zero below any of your tiles or checks if there are two tiles
   *          that are equal to one another in the same column.
   * Parameters: none
   * Return: boolean, returns true if the tiles on your board can move down.
   *         Returns false if not.
   */
  private boolean canMoveDown()
  {
    // runs through column first, row second
    for ( int column = 0; column < grid[0].length ; column++ ) {
      for ( int row = grid.length-2; row >=0; row-- ) {
        // looks at tile directly below the current tile
        int compare = row + 1;
        if ( grid[row][column] != 0 ) {
          if ( grid[compare][column] == 0 || grid[row][column] ==
              grid[compare][column] ) {
            return true;
          }
        }
      }
    }  
    return false;
  }

  /* 
   * Name: move( Direction direction )
   * Purpose: The purpose of this function is to move the tiles on your board
   *          in the direction that the user inputs.
   * Parameters: Direction direction, represents the direction that the user
   *             inputs to attempt to move the board
   * Return: boolean, returns true if the board moves in a direction. Returns
   *         false if the command the user inputted was not a correct direction
   *         symbol.
   */
  public boolean move(Direction direction)
  {
    if ( direction.equals( Direction.LEFT )) {
      this.moveLeft();
      return true;
    }
    else if ( direction.equals( Direction.RIGHT )) {
      this.moveRight();
      return true;
    }
    else if ( direction.equals( Direction.DOWN )) {
      this.moveDown();
      return true;
    }
    else if ( direction.equals( Direction.UP )) {
      this.moveUp();
      return true;
    } 
    else {
      return false;
    }  
  }

  /* 
   * Name: moveLeft()
   * Purpose: This method first runs through the board and moves one tile after
   *          another as far left as possible, as in past all the zeros in that
   *          row. If a tile encounters another tile of the same value while 
   *          moving left, the tiles combine to make one tile that is double 
   *          the original value. Once those tiles have combined, that tile can
   *          not combine with another tile in that row. This continues until
   *          all the non-zero tiles in that row are moved as far left as 
   *          possible.
   * Parameters: none
   * Return: void
   */
  private void moveLeft()
  {
    // runs through row first, column second
    for ( int row = 0; row < grid.length; row++) {

      // check is set to false, meaning no combinations have been made
      boolean check = false;
      for ( int column = 1; column < grid[row].length; column++) {
        int compare = column;
        if ( grid[row][compare] != 0 ) {

          // loop continues while there are still zero values to the left of a
          // tile
          while ( grid[row][compare-1] == 0 ) {

            // switches places of the current tile with the zero tile
            grid[row][compare-1] = grid[row][compare];
            grid[row][compare] = 0;
            compare --;

            // if you reach the end of your board, break from the loop
            if ( compare == 0 ){
              break;
            }
          }
          // if a combination was made on the previous loop, set check to false
          if ( check == true ){
            check = false;
          }
          // if a conbination wasn't made on the previous loop and two tiles
          // next to each other have the same value, combine them
          else if ( compare!=0 && grid[row][compare-1] == grid[row][compare]) {
            grid[row][compare-1] = 2*grid[row][compare-1];
            grid[row][compare] = 0;

            // sets check to true to indicate a combination was made
            check = true;

            // increases your score by the value of the combined tile
            score+= grid[row][compare-1];
          }  
        }
      }
    }
  } 

  /* 
   * Name: moveRight()
   * Purpose: This method first runs through the board and moves one tile after
   *          another as far right as possible, as in past all the zeros in that
   *          row. If a tile encounters another tile of the same value while 
   *          moving right, the tiles combine to make one tile that is double 
   *          the original value. Once those tiles have combined, that tile can
   *          not combine with another tile in that row. This continues until
   *          all the non-zero tiles in that row are moved as far right as 
   *          possible.
   * Parameters: none
   * Return: void
   */
  private void moveRight()
  {
    // runs through row first, column second
    for ( int row = 0; row < grid.length; row++) {

      // chek is set to false, meaning no combinations have been made
      boolean check = false;
      for ( int column = grid[row].length-2; column >= 0; column--) {
        int compare = column;
        if ( grid[row][compare] != 0 ) { 

          // loop continues while there are still zero values to the right of
          // the tile
          while ( grid[row][compare+1] == 0 ) {

            // switches places of the current tile with the zero tile
            grid[row][compare+1] = grid[row][compare];
            grid[row][compare] = 0;
            compare ++;

            // if you reach the end of the board, break from the loop
            if ( compare == grid[row].length-1 ){
              break;
            }
          }
          // if a combination was made on the previous loop, set check to false
          if ( check == true ){
            check = false;
          }
          // if a combination wasn't made on the previous loop and two tiles
          // next to each other have the same value, combine them
          else if ( compare != grid[row].length-1 && 
              grid[row][compare+1] == grid[row][compare] ) {
            grid[row][compare+1] = 2*grid[row][compare+1];
            grid[row][compare] = 0;

            // sets check to true to indicate a combination was made
            check = true;

            // increases your score by the value of the combined tiles
            score+=grid[row][compare+1];
          }  
        }
      }
    } 
  }

  /* 
   * Name: moveUp()
   * Purpose: This method first runs through the board and moves one tile after
   *          another as far up as possible, as in past all the zeros in that
   *          column. If a tile encounters another tile of the same value while 
   *          moving up, the tiles combine to make one tile that is double 
   *          the original value. Once those tiles have combined, that tile can
   *          not combine with another tile in that column. This continues until
   *          all the non-zero tiles in that column are moved as far up as  
   *          possible.
   * Parameters: none
   * Return: void
   */

  private void moveUp()
  {
    // runs through column first, row second
    for ( int column = 0; column < grid[0].length; column++) {

      // check is set to false, meaning no combinations have been made
      boolean check = false;
      for ( int row = 1; row < grid.length; row++) {
        int compare = row;
        if ( grid[compare][column] != 0 ) {  

          // loop continues while there are still zero values above the current
          // tile
          while ( grid[compare-1][column] == 0 ) {

            // switches places of the current tile with the zero tile
            grid[compare-1][column] = grid[compare][column];
            grid[compare][column] = 0;
            compare --;

            // if you reach the top of the board, break from the loop
            if ( compare == 0 ){
              break;
            }
          }
          // if a combination was made on the previous loop, set check to false
          if ( check == true ){
            check = false;
          }
          // if a combination wasn't made on the previous loop and two tiles
          // next to each other have the same value, combine them
          else if ( compare!=0 && 
              grid[compare-1][column] == grid[compare][column] ) {
            grid[compare-1][column] = 2*grid[compare-1][column];
            grid[compare][column] = 0;

            // sets check to true to indicate a combination was made
            check = true;

            // increases your score by the value of the combined tile
            score+= grid[compare-1][column];
          }  
        }
      }
    } 
  }
  /* 
   * Name: moveDown()
   * Purpose: This method first runs through the board and moves one tile after
   *          another as far down as possible, as in past all the zeros in that
   *          column. If a tile encounters another tile of the same value while 
   *          moving down, the tiles combine to make one tile that is double 
   *          the original value. Once those tiles have combined, that tile can
   *          not combine with another tile in that column. This continues until
   *          all the non-zero tiles in that column are moved as far down as 
   *          possible.
   * Parameters: none
   * Return: void
   */

  public void moveDown()
  {
    // runs through column first, row second
    for ( int column = 0; column < grid[0].length; column++) {

      // check is set to false, meaning no combinations have been made
      boolean check = false;
      for ( int row = grid.length-2; row >= 0; row--) {
        int compare = row;
        if ( grid[compare][column] != 0 ) {  

          // loop continues whiel there are still zero values below the current
          // tile
          while ( grid[compare+1][column] == 0 ) {

            // switches places of the current tile with the zero tile
            grid[compare+1][column] = grid[compare][column];
            grid[compare][column] = 0;
            compare ++;

            // if you reach the bottom of your board, break from the loop
            if ( compare == grid.length-1 ){
              break;
            }
          }
          // if a combination was made on the previous loop, set check to false
          if ( check == true ){
            check = false;
          }
          // if a combination wasn't made on the previous loop and two tiles 
          // next to each other have the same value, combine them
          else if ( compare != grid.length-1 &&
              grid[compare+1][column] == grid[compare][column]) {
            grid[compare+1][column] = 2*grid[compare+1][column];
            grid[compare][column] = 0;

            // sets check to true to indicate a combination was made
            check = true;

            // increases your score by the value of the combined tile
            score+= grid[compare+1][column];
          }  
        }
      }
    } 
  }
  // Return the reference to the 2048 Grid
  public int[][] getGrid()
  {
    return grid;
  }

  // Return the score
  public int getScore()
  {
    return score;
  }

  @Override
    public String toString()
    {
      StringBuilder outputString = new StringBuilder();
      outputString.append(String.format("Score: %d\n", score));
      for (int row = 0; row < GRID_SIZE; row++)
      {
        for (int column = 0; column < GRID_SIZE; column++)
          outputString.append(grid[row][column] == 0 ? "    -" :
              String.format("%5d", grid[row][column]));

        outputString.append("\n");
      }
      return outputString.toString();
    }
}
