package TicTacToe;

// Handling the visual aspect of the game
public class Board
{
    private final int size = 3;
    private final int sizeEachSquare = 5;

    public Board()
    {
    }

    public void drawBoard(char[][] positions)
    {
        drawTopLine();
        drawBoxes(positions);
        System.out.println();
    }

    private void drawTopLine()
    {
        for (int i = 0; i < size*sizeEachSquare; i++)
        {
            System.out.print(" ");
            if ((i-2)%5==0)
            {
                System.out.print((i-2)/5);
            }
        }
        System.out.print("\n"+" ");

        for (int i = 0; i < size * sizeEachSquare; i++)
        {
            if (i % sizeEachSquare == 0)
            {
                System.out.print(" ");
            }
            System.out.print("_");
        }
        System.out.println();
    }

    private void drawBoxes(char[][] positions)
    {
        // Draw the box vertically
        for (int i = 0; i < size; i++)
        {
            // Draw the body of the boxes horizontally
            for (int j = 0; j < size-1; j++)
            {
                if (j==1)
                {
                    System.out.print(i);
                }
                else
                {
                    System.out.print(" ");
                }
                for (int k = 0; k < size*sizeEachSquare+1; k++)
                {
                    if (k%sizeEachSquare == 0)
                    {
                        System.out.print("|");
                    }

                    if (j==1&&(k-2)%5==0)
                    {
                        System.out.print(positions[i][(k-2)/5]);
                    }
                    else
                    {
                        System.out.print(" ");
                    }


                }
                System.out.println();
            }

            // Draw the bottom of the box horizontally
            System.out.print(" ");
            for (int j = 0; j < size*sizeEachSquare+1; j++)
            {
                if (j%sizeEachSquare == 0)
                {
                    System.out.print("|");
                }
                if (j != size*sizeEachSquare)
                    System.out.print("_");
            }
            System.out.println();
        }
    }

}
