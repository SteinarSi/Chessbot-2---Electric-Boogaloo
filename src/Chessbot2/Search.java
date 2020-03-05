
package Chessbot2;

import java.util.*;

/**
 * Search
 */

public class Search
{
    static int plies = 4;
    static int alpha = -999999999;
    static int beta = 999999999;
    List<Position> moves = new ArrayList<>();//Already known moves

    public static Move CalulateBestMove(Position board)
    {
        List<Move> moves = board.gen_moves();
        Move[] moveArr = new Move[moves.size()];

        for (int i=0; i<moves.size(); i++)
        {
            try 
            {
                moves.get(i).addWeight(AlphaBeta(moves.get(i), plies, alpha, beta, false, board));
                System.out.println("Move: " + moves.get(i) + "Value: " + moves.get(i).getWeight());
            } 
            catch (Exception e) 
            {
                e.getStackTrace();
                e.getMessage();
            }
            
            moveArr[i] = moves.get(i);
        }
        Arrays.sort(moveArr);//sort all the scores
        return moveArr[moveArr.length - 1];//return the best move
    }

    public static int AlphaBeta(Move node, int n, int alpha, int beta, boolean isMaximizingPlayer, Position board) {
        int value;
        Position currPos = board;
        Position tempPos;
        if ((n == 0) || node.getStabIndex())
            //return the node weight if the node is stable or has reached sufficent depth
            return currPos.value(node);
        if (isMaximizingPlayer) {
            value = -999999999;
            tempPos = currPos.move(node);//Simulate the move
            tempPos = tempPos.rotate();//Switch to other POV
            for (Move move : tempPos.gen_moves()) //generate sub-nodes
            {
                value = Math.max(value, AlphaBeta(move, n - 1, alpha, beta, false, tempPos));//sim next
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        if (!isMaximizingPlayer) {
            value = 999999999;
            tempPos = currPos.move(node);//Simulate the move
            tempPos = tempPos.rotate();//Switch to other POV
            for (Move move : tempPos.gen_moves()) //Gen sub-nodes
            {xxxxxxxxxxxxxxxxxxxxx
                value = Math.min(value, AlphaBeta(move, n - 1, alpha, beta, true, tempPos));
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
        for (Move i : tempPos.gen_moves())
        {
            if(tempPos.value(i) > stabilityCriteria)
                return false;
        }
        return true;
    }
}
