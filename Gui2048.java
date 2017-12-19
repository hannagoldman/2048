/** Gui2048.java */
/** PA8 Release */

import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*
 * Name: Hanna Goldman
 * Login: cs8balv
 * Date: 3/4/15
 * Class: Gui2048
 * Purpose: This class creates the GUI for the game 2048.
 */
public class Gui2048 extends Application
{
  private String outputBoard; // The filename for where to save the Board
  private Board board; // The 2048 Game Board

  // Fill colors for each of the Tile values
  private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
  private static final Color COLOR_2 = Color.rgb(238, 228, 218);
  private static final Color COLOR_4 = Color.rgb(237, 224, 200);
  private static final Color COLOR_8 = Color.rgb(242, 177, 121);
  private static final Color COLOR_16 = Color.rgb(245, 149, 99);
  private static final Color COLOR_32 = Color.rgb(246, 124, 95);
  private static final Color COLOR_64 = Color.rgb(246, 94, 59);
  private static final Color COLOR_128 = Color.rgb(237, 207, 114);
  private static final Color COLOR_256 = Color.rgb(237, 204, 97);
  private static final Color COLOR_512 = Color.rgb(237, 200, 80);
  private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
  private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
  private static final Color COLOR_OTHER = Color.BLACK;
  private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242); // For tiles >= 8
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101); // For tiles < 8

  /** Add your own Instance Variables here */

  private GridPane pane; // the pane that the board is created on
  private Label game; // The name of the game, 2048
  private Label score; // The score for the current board
  public static final double tileSize = 55.5; // the size of a tile plus the
  // distance between tiles
  public static final int rectangleSize = 50; // the size of a tile
  
  /*
   * Name: start (Stage primaryStage)
   * Purpose: This method takes in a Stage as a parameter. It creates a 
   *          GridPane, and then adds the board to the GridPane by inputting 
   *          the name 2048, the score and each tile as a Rectangle on the 
   *          board. It adjusts the spacing and font of the different elements
   *          added to the board. It then adds the pane to the Scene. After a
   *          KeyHandler class is created and the scene is set to take input 
   *          from the KeyHandler. Finally, the scene of the Stage is set to
   *          the scene that was created and the Stage is shown.
   * Parameters: Stage primaryStage
   * Return: void
   */
  @Override
    public void start(Stage primaryStage)
    {
      // Process Arguments and Initialize the Game Board
      processArgs(getParameters().getRaw().toArray(new String[0]));
      pane = new GridPane();
      pane.setAlignment(Pos.CENTER);
      
      // sets padding to manage the distance between the nodes and the edges
      // of the GridPane
      pane.setPadding(new Insets(11.5,12.5,13.5,14.5));
      
      // creates a gap between each row and column
      double gap = 5.5;
      pane.setHgap(gap);
      pane.setVgap(gap);

      // prints out the board, name of the game, and the score
      this.printBoard(game, score, pane, board );  
      primaryStage.setTitle("Gui2048");

      // sets the color of the background to a color based on the hue, 
      // saturation, and brightness
      pane.setStyle("-fx-background-color: hsb(25, 13%, 77%);");

      // adds the pane to the scene and sets the size of the scene
      Scene scene = new Scene(pane,tileSize*board.getGrid().length,
          tileSize*(board.getGrid().length + 1));

      // sets the minimum width and height of the window to show the whole board
      primaryStage.setMinWidth( tileSize*board.getGrid().length);
      primaryStage.setMinHeight( tileSize*(board.getGrid().length+1));
      KeyHandler handler = new KeyHandler();

      // scene takes the input from the key pressed
      scene.setOnKeyPressed(handler);
      primaryStage.setScene(scene);
      primaryStage.show();
    }

  /*
   * Name: printBoard(Label game,, Label score, GridPane pane, Board board)
   * Purpose: Prints the name 2048 and the score of your game at the top of the
   *          board. Then runs through the board and prints the tiles on the 
   *          pane with a color corresponding to their tile value.
   * Parameters: Label game, Label score, GridPane pane
   * Return: void
   */
  public static void printBoard (Label game, Label score, GridPane pane,
      Board board) {
    game = new Label("2048");
    score = new Label("Score: " + board.getScore());

    // if the board is 2x2 or 3x3
    if ( board.getGrid().length == 2 || board.getGrid().length == 3) {
      game.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 
            FontPosture.ITALIC, 14));
      score.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 10));

      // adds the name 2048 to the upper left corner of the game board and adds
      // the score the the upper right
      pane.add(game,0,0);
      pane.add(score,board.getGrid().length - 1,0);
    }
    else {
      game.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 
            FontPosture.ITALIC, 20));
      score.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));
      pane.add(game, 0,0 );

      // makes the Label span over two columns
      GridPane.setColumnSpan( game, 2);

      // adds the score of the game to the upper right of the board
      pane.add(score, board.getGrid().length - 2, 0);
      GridPane.setColumnSpan( score, 2);
    }

    // sets the name and score to the center of their column
    GridPane.setHalignment( game, HPos.CENTER);
    GridPane.setHalignment( score, HPos.CENTER);

    // runs through the game board row first, column second
    for ( int i = 0; i < board.getGrid().length ; i++) {
      for ( int j = 0; j < board.getGrid().length; j++) {

        // sets the tile color depending on the value of the tile
        Color tileColor = Gui2048.setcolor(i, j, board);
        Rectangle tile = new Rectangle( rectangleSize, rectangleSize,
            tileColor);
        
        // adds the tile to its location on the board
        pane.add(tile, j, i+1);
        
        // the value of the tile at row i, column j
        Text value = new Text("" + board.getGrid()[i][j] );

        // if the value is less than 8, sets the color of the number on the
        // tile to dark
        int tileValue = 8;
        if ( board.getGrid()[i][j] < tileValue ) {
          value.setFill( COLOR_VALUE_DARK );
        }
        else{

          // sets the color of the number to light
          value.setFill( COLOR_VALUE_LIGHT );
        }
        value.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 16));

        // only adds the value on the tile if it is greater than zero
        if ( board.getGrid()[i][j] > 0 ) {
          pane.add( value, j, i+1);
        }

        // centers the value to the middle of the tile
        GridPane.setHalignment(value, HPos.CENTER);
      }
    } 
  }

  /*
   * Name: setcolor( int row, int column, Board board )
   * Purpose: This method returns the color that the tile will be set to 
   *          depending on the tile's value
   * Parameters: int row, int column, Board board which determines which tile on
   *             the board we are looking at
   * Return: Color, which represents the color of the tile
   */
  public static Color setcolor(int row, int column, Board board) {
    if ( board.getGrid()[row][column] == 0 ) {
      return COLOR_EMPTY;
    }
    else if ( board.getGrid()[row][column] == 2 ) {
      return COLOR_2;
    }
    else if ( board.getGrid()[row][column] == 4 ) {
      return COLOR_4;
    }
    else if ( board.getGrid()[row][column] == 8 ) {
      return COLOR_8;
    }
    else if ( board.getGrid()[row][column] == 16 ) {
      return COLOR_16;
    }
    else if ( board.getGrid()[row][column] == 32 ) {
      return COLOR_32;
    }
    else if ( board.getGrid()[row][column] == 64 ) {
      return COLOR_64;
    }
    else if ( board.getGrid()[row][column] == 128 ) {
      return COLOR_128;
    }
    else if ( board.getGrid()[row][column] == 256 ) {
      return COLOR_256;
    }
    else if ( board.getGrid()[row][column] == 512 ) {
      return COLOR_512;
    }
    else if ( board.getGrid()[row][column] == 1024 ) {
      return COLOR_1024;
    }
    else if ( board.getGrid()[row][column] == 2048 ) {
      return COLOR_2048;
    }
    else {
      return COLOR_OTHER;
    }
  }

/*
 * Name: gameOverTile( Board board, GridPane pane)
 * Purpose: This method checks each turn to see if there are any more possible
 *          moves. If not then the game is over and a semi transparent overlay
 *          with the text Game Over! in printed over the whole window.
 * Parameters: Board board, GridPane pane which just pass in the board and pane
 *             the method will be adding to
 * Return: void
 */
  public static void gameOverTile(Board board, GridPane pane) {
    
    // checks to see if there are any more possible moves, if there aren't
    // .isGameOver() returns true
    if ( board.isGameOver() ) {
      
      // creates a semi-transparent overlay 
      Rectangle end = new Rectangle( tileSize*board.getGrid().length,
          tileSize*(board.getGrid().length + 1), COLOR_GAME_OVER ); 
      
      // adds the overlay 
      pane.add(end, 0, 0);
      
      // has the overlay span over the whole window
      GridPane.setColumnSpan(end, board.getGrid().length);
      GridPane.setRowSpan(end, board.getGrid().length + 1);
      Text gameOver = new Text("Game Over!");
      pane.add( gameOver, 0, 0);
      
      // also has the words Game Over! on the overlay span over the window
      GridPane.setColumnSpan(gameOver, board.getGrid().length);
      GridPane.setRowSpan(gameOver, board.getGrid().length + 1);
      
      // if the game is a 2x2 or 3x3 board
      if ( board.getGrid().length == 2 || board.getGrid().length == 3) {
        gameOver.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 15));
      }
      else {
        gameOver.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 30));
      }
      
      // centers the text to the middle of the window
      GridPane.setHalignment( gameOver, HPos.CENTER);
      GridPane.setValignment( gameOver, VPos.CENTER);
    }
  }
/*
 * Class: KeyHandler
 * Purpose: This class handles keyboard events that occur while focused on
 *          the Gui Window.
 */
  private class KeyHandler implements EventHandler<KeyEvent> {

    /*
     * Name: handle (KeyEvent e)
     * Purpose: This method interprets the key that the user presses and if the
     *          key was a directional arrow, moves the board in the direction
     *          and updates the board. If the key they pressed was the letter 
     *          s, the board is saved to a file. After each turn, the method 
     *          also checks to see whether the game is over and if so, prints
     *          the semi transparent overlay.
     * Parameters: KeyEvent e, which represents the key that the user presses
     * Return: void
     */
    public void handle(KeyEvent e) {
      
      // checks the code for the key that the user presses
      switch( e.getCode()) {
        
        // if the user presses the down arrow key
        case DOWN: if ( board.canMove(Direction.DOWN)) {
                     board.move(Direction.DOWN);
                     board.addRandomTile();
                     
                     // prints out the move to the terminal window
                     System.out.println("Moving Down");
                     
                     // clears the old board
                     pane.getChildren().clear();
                     
                     //prints the updated board
                     Gui2048.printBoard(game, score, pane,board);
                     
                     // checks to see if the game is over
                     Gui2048.gameOverTile(board, pane);
                   }; break;

        //  if the user presses the up arrow key
        case UP: if ( board.canMove(Direction.UP)) {
                   board.move(Direction.UP);
                   board.addRandomTile();
                   System.out.println("Moving Up");
                   pane.getChildren().clear();
                   Gui2048.printBoard(game, score, pane,board);
                   Gui2048.gameOverTile(board, pane);
                 }; break;

        // if the user presses the left arrow key         
        case LEFT: if ( board.canMove(Direction.LEFT)) {
                     board.move(Direction.LEFT);
                     board.addRandomTile();
                     System.out.println("Moving Left");
                     pane.getChildren().clear();
                     Gui2048.printBoard(game, score, pane,board);
                     Gui2048.gameOverTile(board, pane);
                   }; break;

        // if the user presses the right arrow key           
        case RIGHT: if ( board.canMove(Direction.RIGHT)) {
                      board.move(Direction.RIGHT);
                      board.addRandomTile();
                      System.out.println("Moving Right");
                      pane.getChildren().clear();
                      Gui2048.printBoard(game, score, pane,board);
                      Gui2048.gameOverTile(board, pane);
                    }; break;

        // if the user presses the letter s            
        case S: 
                    try {
                      
                      // saves the board to a file
                      board.saveBoard(outputBoard);
                    } 
                    catch(IOException a) {}

                    // prints out the file that it is saving the board to,
                    // to the terminal
                    System.out.println("Saving Board to " + outputBoard);
                    break;             
      }  
    }
  }  

  /** DO NOT EDIT BELOW */

  // The method used to process the command line arguments
  private void processArgs(String[] args)
  {
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 3;          // The Size of the Board

    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
      printUsage();
      System.exit(-1);
    }

    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
      if(args[i].equals("-i"))
      {   // We are processing the argument that specifies
        // the input file to be used to set the board
        inputBoard = args[i + 1];
      }
      else if(args[i].equals("-o"))
      {   // We are processing the argument that specifies
        // the output file to be used to save the board
        outputBoard = args[i + 1];
      }
      else if(args[i].equals("-s"))
      {   // We are processing the argument that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      }
      else
      {   // Incorrect Argument 
        printUsage();
        System.exit(-1);
      }
    }

    // Set the default output file if none specified
    if(outputBoard == null)
      outputBoard = "2048.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 2)
      boardSize = 4;

    // Initialize the Game Board
    try{
      if(inputBoard != null)
        board = new Board(inputBoard, new Random());
      else
        board = new Board(boardSize, new Random());
    }
    catch (Exception e)
    {
      System.out.println(e.getClass().getName() + " was thrown while creating a " +
          "Board from file " + inputBoard);
      System.out.println("Either your Board(String, Random) " +
          "Constructor is broken or the file isn't " +
          "formated correctly");
      System.exit(-1);
    }
  }

  // Print the Usage Message 
  private static void printUsage()
  {
    System.out.println("Gui2048");
    System.out.println("Usage:  Gui2048 [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
    System.out.println("                If none specified then the default \"2048.board\" file will be used");
    System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
    System.out.println("                specified.  If both -s and -i are used, then the size of the board");
    System.out.println("                will be determined by the input file. The default size is 4.");
  }
}
