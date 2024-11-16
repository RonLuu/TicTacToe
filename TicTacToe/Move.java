package TicTacToe;

// A class to represent the move of the game
public class Move
{
    // The coordinate of the game
    private int x;
    private int y;
    // The piece of that move
    private char c;

    public Move(char c, int x, int y)
    {
        this.c = c;
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public char getPiece()
    {
        return c;
    }
    public void setMove(char c, int x, int y)
    {
        this.c = c;
        this.x = x;
        this.y = y;
    }
}
