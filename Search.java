package Chessbot2;

import Chessbot2.*;

import java.util.*;

/**
 * Search
 */
public class Search
{
    static int plies = 10;
    static int alpha = -999999999;
    static int beta = 999999999;

    public static Move CalulateBestMove(Position board)
    {
        List<Move> moves = new ArrayList<>();
        for (iMove move : board.gen_moves(false))
        {
            moves.add((Move) move);
        }
        Move[] moveArr = new Move[moves.size()];
        for (int i=0; i<moves.size(); i++)
        {
            moves.get(i).weight = AlphaBeta(moves.get(i), plies, alpha, beta, true, board);
            moveArr[i] = moves.get(i);
        }
        Arrays.sort(moveArr);//sort all the scores
        for (Move move: moveArr)
        {
            System.out.println(move.weight);
        }
        return moveArr[moveArr.length-1];//return the best move
    }

    public static int AlphaBeta(Move node, int n, int alpha, int beta, boolean isMaximizingPlayer, Position board)
    {
        int value;
        Position currPos = board;
        Position tempPos;
        if ((n == 0) || node.stabilityIndex)
            //return the node weight if the node is stable or has reached sufficent depth
            return currPos.value(node);
        if (isMaximizingPlayer)
        {
            value = -999999999;
            tempPos = currPos.move(node);//Simulate the move
            tempPos = tempPos.rotate();//Switch to other POV
            for (iMove move : tempPos.gen_moves(false)) //generate sub-nodes
            {
                value = Math.max(value, AlphaBeta((Move) move, n - 1, alpha, beta, false, tempPos));//sim next
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
            tempPos = tempPos.rotate();//Switch to other POV
            for (iMove move : tempPos.gen_moves(false)) //Gen sub-nodes
            {
                value = Math.min(value, AlphaBeta((Move) move, n - 1, alpha, beta, true, tempPos));
                beta = Math.min(beta, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        return -1;
    }

    public static boolean CalculateStabilityIndex(Move move, Position board)
    {
        int stabilityCriteria = 250;
        Position currPos = board;
        Position tempPos = currPos.move(move);
        tempPos = tempPos.rotate();
        for (iMove i : tempPos.gen_moves(false))
        {
            if(tempPos.value(i) > stabilityCriteria)
                return false;
        }
        return true;
    }

}