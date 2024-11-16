package TicTacToe;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

// A class to handle to logic of the game
public class TicTacToe
{
    // The size of the tic-tac-toe
    private final int size = 3;
    // The maximum turn of the game
    private final int maxPiece = size*size;
    // The number of piece currently in the game
    private int numOfPiece = 4;
    // A character to store the player's piece
    private char p = 'X';

    // A character to store the second player/bot's piece
    private char b = 'O';

    // A scanner to read the input
    private final Scanner scanner;

    private final Move curMove;


    // A board to draw
    private final Board board;

    // A position table to keep track of the pieces
//    private final char[][] positions = new char[size][size];

    private final char[][] positions;

    // The final state of the game
    private GameState gameState;

    private int counter = 0;

    public TicTacToe() throws FileNotFoundException
    {
        // Initialise the table
//        fillPosition();

        positions = new char[][]{
                {' ', 'O', ' '},
                {' ', 'O', ' '},
                {' ', 'X', 'X'}
        };

        // Create the board to represent the game visually
        board = new Board();
        // Draw the board
        board.drawBoard(positions);
        // Current move at the start of the game (which is nothing)
        curMove = new Move(' ',-1,-1);

        // Get the file for input
        File file = new File("src\\TicTacToe\\text4.txt");
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

    // A function to start the game with one player, one bot
    public void startOnePlayer()
    {
        // While the game hasn't ended
        while(true)
        {
            // Get player 1's move first
            getMove("Player 1's move: ");
            makeMove();
            // After a player move
            // check if the game has reached the end
            if (checkEnd())
            {
                break;
            }

            // Get bot's move
            getBotMove();
            makeMove();
            if (checkEnd())
            {
                break;
            }
        }
    }

    // A function that generate moves for the bot
    private void getBotMove()
    {
        int bestValue = Integer.MAX_VALUE;

        // Move bestMove = new Move(b,-1, -1);
        System.out.print("Bot's move: ");
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (positions[i][j] == ' ')
                {
                    int value = minimax(positions, numOfPiece, b, p);
                    if (value < bestValue)
                    {
                        bestValue = value;
                        curMove.setMove(b,j,i);
                    }
                }
            }
        }

        System.out.print("O-"+curMove.getX()+","+curMove.getY() + "\n");
    }


    // A table to represent the current state of the game
    // Number of piece to check if it reaches a tie
    // A character to represent the current player/bot piece
    // A character to represent the player one's piece
    private int minimax(char[][] positions, int numOfPiece, char c, char p)
    {
        counter++;
        if (counter == 100)
        {
            //System.exit(0);
        }
        GameState gameState = terminal(positions, numOfPiece, p);

        if (gameState != GameState.NOT_END)
        {
            System.out.println("The game has reached the end");
            if (gameState == GameState.LOSE)
            {
                System.out.println("Bot wins");
            }

            if (gameState == GameState.WIN)
            {
                System.out.println("Player wins");
            }

            if (gameState == GameState.TIE)
            {
                System.out.println("Tie");
            }
            System.out.print("");
            return value(gameState);
        }
        System.out.println("The game hasn't reached the end");
        System.out.println("\nEntering minimax on " + c + " turn");
        board.drawBoard(positions);
        System.out.print("");

        if (checkTurn(c) == Player.BOT)
        {
            System.out.println("It's bot turn now");
            System.out.print("");
            int value = Integer.MAX_VALUE;
            System.out.println("Printing all possible move for bot");
            ArrayList<Move> possibleMoves = generateMoves(positions, c);

            printPossibleMove(possibleMoves);
            System.out.print("");
            for(Move move : possibleMoves)
            {
                System.out.println("The current value of bot is " + value);
                System.out.println("The current move of bot is " + move.getPiece() + " at " + move.getX() + ", " + move.getY());
                System.out.print("");
                applyAction(positions, move, c);
                numOfPiece++;
                c = nextTurn(c);
                board.drawBoard(positions);
                System.out.print("");
                value = Integer.min(value, minimax(positions, numOfPiece, c, p));
                System.out.println("The current value after minimax of bot is " + value);
                System.out.println();
                System.out.println("Unapplying bot " + move.getPiece() + " at " + move.getX() + ", " + move.getY());
                System.out.print("");
                unapplyAction(positions, move);
                c = nextTurn(c);
                numOfPiece--;
                board.drawBoard(positions);
                System.out.print("");
            }

            return value;
        }

        if (checkTurn(c) == Player.PLAYER)
        {
            System.out.println("It's player turn now");
            System.out.print("");
            int value = Integer.MIN_VALUE;

            ArrayList<Move> possibleMoves = generateMoves(positions, c);
            System.out.println("Printing all possible move for player");

            printPossibleMove(possibleMoves);
            System.out.print("");
            for(Move move : possibleMoves)
            {
                System.out.println("The current value is of player is " + value);
                System.out.println("The current move of bot is " + move.getPiece() + " at " + move.getX() + ", " + move.getY());
                System.out.print("");
                applyAction(positions, move, c);

                numOfPiece++;
                c = nextTurn(c);
                board.drawBoard(positions);
                System.out.print("");
                value = Integer.max(value, minimax(positions, numOfPiece, c, p));
                System.out.println("The current value after minimax of player is " + value);
                System.out.println();
                System.out.println("Unapplying player " + move.getPiece() + " at " + move.getX() + ", " + move.getY());
                System.out.print("");
                unapplyAction(positions, move);
                c = nextTurn(c);
                numOfPiece--;
                board.drawBoard(positions);
                System.out.print("");
            }

            return value;
        }

        return -2;
    }

    // For debugging purpose
    private void printPossibleMove(ArrayList<Move> possibleMoves)
    {
        System.out.print(possibleMoves.getFirst().getPiece() + ": ");
        for(Move move:possibleMoves)
        {
            System.out.print(move.getX() + "," + move.getY() +" ");
        }
        System.out.println();
    }
    private void unapplyAction(char[][] positions, Move move)
    {
        positions[move.getY()][move.getX()] = ' ';
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
    private ArrayList<Move> generateMoves(char[][] positions, char c)
    {
        System.out.println("Generating move now");
        ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (positions[i][j] == ' ')
                {
                    possibleMoves.add(new Move(c, j, i));
                }

            }
        }

        return possibleMoves;
    }

    private void applyAction(char[][] positions, Move move, char c)
    {
        positions[move.getY()][move.getX()] = c;
    }
    // By default, if the first player/player wins, GameState is win
    //             if the second player/bot wins, GameState is lost
    private GameState terminal(char[][] positions, int numOfPiece, char p)
    {

        GameState gameState = checkLeft(positions, p);
        if (gameState != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = checkDown(positions, p);
        // Check every vertical line
        if (gameState != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = checkDownRight(positions, p);
        // Check the diagonal line from top left
        if(checkDownRight(positions, p) != GameState.NOT_END)
        {
            return gameState;
        }

        gameState = checkUpRight(positions, p);
        // Check the diagonal line from bottom left
        if (checkUpRight(positions, p) != GameState.NOT_END)
        {
            return gameState;
        }

        if (numOfPiece == maxPiece)
        {
            return GameState.TIE;
        }
        return GameState.NOT_END;
    }

    private Player checkTurn(char c)
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
                if (curMove.getPiece() != move.charAt(0))
                {
                    // If this is the first move
                    if (curMove.getPiece() == ' ')
                    {
                        // Set player one's piece to this piece
                        p = move.charAt(0);
                        setSecondPlayerPiece();
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
                        curMove.setMove(move.charAt(0), x, y);
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
    private void setSecondPlayerPiece()
    {
        if (p == 'X')
        {
            b = 'O';
        }
        else
        {
            b = 'X';
        }
    }

    // A function to move the piece into the table
    public void makeMove()
    {
        positions[curMove.getY()][curMove.getX()] = curMove.getPiece();
        board.drawBoard(positions);
        numOfPiece++;
    }


    // A function to check if the game has reached the end
    public boolean checkEnd()
    {
        // Check every horizontal line
        if (checkLeft(positions, p) != GameState.NOT_END)
        {
            System.out.println("true left");
            return true;
        }

        // Check every horizontal line
        if (checkDown(positions, p) != GameState.NOT_END)
        {
            System.out.println("true down");
            return true;
        }

        // Check the diagonal line from top left
        if(checkDownRight(positions, p) != GameState.NOT_END)
        {
            System.out.println("true down-left");
            return true;
        }

        // Check the diagonal line from bottom left
        if (checkUpRight(positions, p) != GameState.NOT_END)
        {
            System.out.println("true up-left");
            return true;
        }

        if (numOfPiece == maxPiece)
        {
            System.out.println("tie");
            setState(GameState.TIE);
            return true;
        }

        System.out.println("No one wins yet");
        return false;
    }

    // A function to check every horizontal line
    private GameState checkLeft(char[][] board, char p)
    {
        // Check every horizontal line
        for (int y = 0; y < size; y++)
        {
            GameState gameState = goingLeft(board,p,y);
            if(gameState != GameState.NOT_END)
            {
                return gameState;
            }
        }

        return GameState.NOT_END;
    }
    // A function to check one horizontal line
    private GameState goingLeft(char[][] board, char p, int y)
    {
        char c = board[y][0];
        if (c == ' ')
        {
            return GameState.NOT_END;
        }

        // For one specific horizontal line
        for (int x = 1; x < size; x++)
        {
            if (c != board[y][x])
            {
                return GameState.NOT_END;
            }
        }

        // Set the end game based on the piece the function is checking
        return setState(c, p);
    }
    // A function to check every vertical line
    private GameState checkDown(char[][] board, char p)
    {
        // Checking every vertical line
        for (int x = 0; x < size; x++)
        {
            GameState gameState = goingDown(board,p,x);
            if(gameState != GameState.NOT_END)
            {
                return gameState;
            }
        }

        return GameState.NOT_END;
    }
    // A function to check one vertical line
    private GameState goingDown(char[][] board, char p, int x)
    {
        char c = board[0][x];
        if (c == ' ')
        {
            return GameState.NOT_END;
        }

        // For one specific horizontal line
        for (int y = 1; y < size; y++)
        {
            if (c != board[y][x])
            {
                return GameState.NOT_END;
            }
        }

        // Set the end game based on the piece the function is checking
        return setState(c, p);
    }
    // A function to set the final state of the game
    // Depending on the last move has made
    private GameState setState(char c, char p)
    {
        // If the winning piece is the same as the first player
        if (c == p)
        {
            // The first player wins
            gameState = GameState.WIN;
        }
        else
        {
            // The first player loses
            gameState = GameState.LOSE;
        }
        return gameState;
    }

    // A function to set the final state of the game
    // to a tie
    private void setState(GameState gameState)
    {
        this.gameState = gameState;
    }

    // A function to check if the game ends on one row


    private GameState checkDownRight(char[][] positions, char p)
    {
        // Represent the piece that the function is checking
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
        // Set the end game based on the piece the function is checking
        return setState(c, p);
    }

    private GameState checkUpRight(char[][] positions, char p)
    {
        // Represent the piece that the function is checking
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
        // Set the end game based on the piece the function is checking
        return setState(c, p);
    }
}
