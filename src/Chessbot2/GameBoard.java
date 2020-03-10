package Chessbot2;

/**
 * GameBoard
 */
public class GameBoard 
{
    int[][] board;
    public GameBoard() 
    {
        board = 
        {
        {new Chesspiece(symbol, isWhite, legalMovementDirs, legalAttackDirs)},
        {},
        {},
        {},
        {},
        {},
        {},
        {}
        };
    }
}