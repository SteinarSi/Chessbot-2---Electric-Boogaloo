package Chessbot2;

import java.util.ArrayList;
import java.util.List;

/**
 * Search
 */
public class Search
{
    static int stabilityCriteria = 0;

    static Move CalulateBestMove(Move[] moves) 
    {
        for (Move move : moves) 
        {
            
        }
    }
    
    static int AlphaBeta(Move node, int n, int alpha, int beta, boolean isMaximizingPlayer, Position board) 
    {
        int value;
        Position currPos = board;
        Position tempPos;
        if ((n == 0) || (node.stabilityIndex > stabilityCriteria))
        //return the node weight if the node is stable or has reached sufficent depth
            return currPos.value(node);
        if (isMaximizingPlayer)
        {
            value = -999999999;
            tempPos = currPos.move(node);//Simulate the move
            tempPos.rotate();//Switch to other POV
            for (Move move : tempPos.gen_moves()) //generate sub-nodes
            {
                value = Math.max(value, AlphaBeta(move, n - 1, alpha, beta, false, tempPos));//sim next 
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        if (!isMaximizingPlayer)
        {
            value = 999999999;
            tempPos = currPos.move(node);//Simulate the move
            tempPos.rotate();//Switch to other POV
            for (Move move : tempPos.gen_moves()) //Gen sub-nodes
            {
                value = Math.min(value, AlphaBeta(move, n - 1, alpha, beta, true, tempPos));
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        throw new Exception("Unexpected Exception occured");
    }

    static int CalculateStabilityIndex(Move move) 
    {
        return -1;
    }
    
}