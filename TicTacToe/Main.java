package TicTacToe;

import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        // Side quest
        // Create the game
        TicTacToe ttt = new TicTacToe();

        // input format: X-1,1
        // O-0,0
        // X-2,1

        // for one player
        // Start the game
        ttt.startOnePlayer();

        // for two player
        // ttt.start();


//        new MyFrame();
//        Chess chess = new Chess();
//        chess.play();
    }
}