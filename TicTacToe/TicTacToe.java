package TicTacToe;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// A class to handle to logic of the game
public class TicTacToe
{
    // The size of the tic-tac-toe
    private final int size = 3;
    // The maximum turn of the game
    private final int maxPiece = size*size;
    // The number of piece currently in the game
    private int numOfPiece = 0;
    // A scanner to read the input
    private final Scanner scanner;

    // A character to store the recent piece
    private char c = ' ';
    // Two integers to store the recent move
    private int x = -1;
    private int y = -1;

    // A character to store the player's piece
    private char p;

    // A board to draw
    private final Board board;

    // A position table to keep track of the pieces
    private final char[][] positions;
    // The final state of the game
    private GameState gameState;

    public TicTacToe()
    {
        // Initialise the table
        positions = new char[size][size];
        fillPosition();

        // Create the board
        board = new Board();
        // Draw the board
        board.drawBoard(positions);

        // Get the file for input
        // File file = new File("src\\TicTacToe\\text2.txt");
        // Create a scanner to read the input file
        scanner = new Scanner(System.in);
    }

    private void fillPosition()
    {
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                positions[i][j] = ' ';
            }
        }
    }
    
    public void startOnePlayer()
    {

    }

    // A table to represent the current state of the game
    // Number of piece to keep an eye on a tie
    // A character to represent the current player piece
    // A character to represent the player one's piece
    private int minimax(char[][] positions, int numOfPiece, char c, char p)
    {
        GameState gameState = terminal(positions, numOfPiece, p);
        if (gameState != GameState.NOT_END)
        {
            return value(gameState);
        }

        if (checkTurn(c, p) == Player.BOT)
        {
            int value = Integer.MIN_VALUE;
            ArrayList<Coordinate> possibleMoves = generateMoves(positions);
            for(Coordinate coordinate : possibleMoves)
            {
                applyAction(positions, coordinate, c);
                numOfPiece++;
                c = nextTurn(c);

                value = Integer.max(value, minimax(positions, numOfPiece, c, p));

                unapplyAction(positions, coordinate);
                numOfPiece--;
            }

            return value;
        }

        if (checkTurn(c, p) == Player.PLAYER)
        {
            int value = Integer.MAX_VALUE;
            ArrayList<Coordinate> possibleMoves = generateMoves(positions);
            for(Coordinate coordinate : possibleMoves)
            {
                applyAction(positions, coordinate, c);
                numOfPiece++;
                c = nextTurn(c);

                value = Integer.min(value, minimax(positions, numOfPiece, c, p));

                unapplyAction(positions, coordinate);
                numOfPiece--;
            }

            return value;
        }

        return -2;
    }

    private void unapplyAction(char[][] positions, Coordinate coordinate)
    {
        positions[coordinate.getY()][coordinate.getX()] = ' ';
    }
    char nextTurn(char c)
    {
        if (c == 'X')
        {
            c = 'O';
        }
        else
        {
            c = 'X';
        }

        return c;
    }
    private ArrayList<Coordinate> generateMoves(char[][] positions)
    {
        ArrayList<Coordinate> possibleMoves = new ArrayList<Coordinate>();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (positions[i][j] == ' ')
                {
                    possibleMoves.add(new Coordinate(j, i));
                }
            }
        }

        return possibleMoves;
    }

    private void applyAction(char[][] positions, Coordinate coordinate, char c)
    {
        positions[coordinate.getY()][coordinate.getX()] = c;
    }
    private GameState terminal(char[][] positions, int numOfPiece, char p)
    {
        if (numOfPiece == maxPiece)
        {
            return GameState.TIE;
        }

        GameState gameState = goingLeft(positions, p);
        if (gameState != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = goingDown(positions, p);
        // Check every vertical line
        if (gameState != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = goingDownRight(positions, p);
        // Check the diagonal line from top left
        if(goingDownRight(positions, p) != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = goingUpRight(positions, p);
        // Check the diagonal line from bottom left
        if (goingUpRight(positions, p) != GameState.NOT_END)
        {
            return gameState;
        }

        return GameState.NOT_END;
    }
    
    private Player checkTurn(char c, char p)
    {
        // If the recent character is the same as player one's
        // The next turn is for player two
        if (c == p)
        {
            return Player.PLAYER;
        }
        else
        {
            return Player.BOT;
        }

    }
    private int value(GameState gameState)
    {
        if (gameState == GameState.WIN)
        {
            return 1;
        }
        else if(gameState == GameState.LOSE)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
    public boolean occupied(int x, int y)
    {
        return positions[y][x] != ' ';
    }

    public void start()
    {
        // The game will run until
        // there is an end
        while(true)
        {
            getMove("Player 1 move: \n");
            makeMove();
            // After a player move
            // check if the game has reached the end
            if (checkEnd())
            {
                break;
            }

            getMove("Player 2 move: \n");
            makeMove();
            // After a player move
            // check if the game has reached the end
            if (checkEnd())
            {
                break;
            }
        }

        if (gameState == GameState.WIN)
        {
            System.out.println("The winner is player 1");
        }
        else if (gameState == GameState.LOSE)
        {
            System.out.println("The winner is player 2");
        }
        else
        {
            System.out.println("The game is a tie");
        }
    }

    // A function to get the move from the players
    // UNTIL it is the CORRECT move
    public void getMove(String line)
    {
        Pattern pattern = Pattern.compile("[XO]-[0-2],[0-2]");
        String move;
        Matcher matcher;

        while(true)
        {
            System.out.print(line);
            move = scanner.nextLine();
            matcher = pattern.matcher(move);

            // If the move is syntactically correct
            if (matcher.matches())
            {
                // Check if the current piece is exactly the same as the previous one
                // The current player is using the opponent's piece
                if (c != move.charAt(0))
                {
                    // If this is the first move
                    if (c == ' ')
                    {
                        // Set player one's piece to this piece
                        p = move.charAt(0);
                    }

                    // Get the current move
                    int x = move.charAt(2)-'0';
                    int y = move.charAt(4)-'0';

                    // If there already is a piece here
                    if (occupied(x, y))
                    {
                        System.out.println("There is a piece here already!!!");
                    }
                    else
                    {
                        // Set the previous move to the current move
                        setCXY(move.charAt(0), x, y);
                        break;
                    }
                }
                else
                {
                    System.out.println("This is not your piece!!!");
                }
            }
            else
            {
                System.out.println("Please try again");
            }
        }
    }
    // A function to set the current move of the game
    public void setCXY(char c, int x, int y)
    {
        this.c = c;
        this.x = x;
        this.y = y;
    }
    // A function to move the piece into the table
    public void makeMove()
    {
        positions[y][x] = c;
        board.drawBoard(positions);
        numOfPiece++;
    }


    // A function to check if the game has reached the end
    public boolean checkEnd()
    {
        if (numOfPiece == maxPiece)
        {
            System.out.println("tie");
            setState(GameState.TIE);
            return true;
        }

        // Check every horizontal line
        if (goingLeft(positions, p) != GameState.NOT_END)
        {
            System.out.println("true left");
            return true;
        }

        // Check every vertical line
        if (goingDown(positions, p) != GameState.NOT_END)
        {
            System.out.println("true down");
            return true;
        }
        // Check the diagonal line from top left
        if(goingDownRight(positions, p) != GameState.NOT_END)
        {
            System.out.println("true down-left");
            return true;
        }

        // Check the diagonal line from bottom left
        if (goingUpRight(positions, p) != GameState.NOT_END)
        {
            System.out.println("true up-left");
            return true;
        }
        return false;
    }

    // A function to set the final state of the game
    // Depending on the last move has made
    private GameState setState(char c, char p)
    {
        if (c == p)
        {
            gameState = GameState.WIN;
            return gameState;
        }
        else
        {
            gameState = GameState.LOSE;
            return gameState;
        }
    }

    // A function to set the final state of the game
    // to a tie
    private void setState(GameState gameState)
    {
        this.gameState = gameState;
    }

    // A function to return the game's state horizontally
    private GameState goingLeft(char[][] board, char p)
    {
        char c = ' ';
        // Checking every horizontal line
        for (int y = 0; y < size; y++)
        {
            c = board[y][0];
            if (c == ' ')
            {
                return GameState.NOT_END;
            }

            for (int x = 1; x < size; x++)
            {
                if (c != board[y][x])
                {
                    return GameState.NOT_END;
                }
            }
        }

        return setState(c, p);
    }

    private GameState goingDown(char[][] positions, char p)
    {
        char c = ' ';
        // Checking every vertical line
        for (int x = 0; x < size; x++)
        {
            c = positions[y][0];
            if (c == ' ')
            {
                return GameState.NOT_END;
            }
            for (int y = 1; y < size; y++)
            {
                if (c != positions[y][x])
                {
                    return GameState.NOT_END;
                }
            }
        }

        return setState(c, p);
    }

    private GameState goingDownRight(char[][] positions, char p)
    {
        char c = positions[0][0];
        if (c == ' ')
        {
            return GameState.NOT_END;
        }
        for (int i = 1; i < size; i++)
        {
            if (c!= positions[i][i])
            {
                return GameState.NOT_END;
            }
        }

        return setState(c, p);
    }

    private GameState goingUpRight(char[][] positions, char p)
    {
        char c = positions[0][size-1];
        if (c == ' ')
        {
            return GameState.NOT_END;
        }
        for (int i = 1; i < size; i++)
        {
            if (c != positions[i][size-1-i])
            {

                return GameState.NOT_END;
            }
        }

        return setState(c, p);
    }
}
