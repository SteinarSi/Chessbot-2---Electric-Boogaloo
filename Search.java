package Chessbot2;

import Chessbot2.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Search
 */
public class Search
{
    static int plies = 6;//how deep, remember that every second plie(?) is the oponents potential moves. 
    static int alpha = 0;//What does this do?
    static int beta = 0;//What does this do?

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
        System.out.println(moveArr.toString());
        Arrays.sort(moveArr);//sort all the scores
        for (Move move : moveArr) 
        {
            System.out.println(move.weight);    
        }
        return moveArr[moveArr.Length-1];//return the best move
    }

    public static int AlphaBeta(Move node, int n, int alpha, int beta, boolean isMaximizingPlayer, Position board)
    {
        int value;
        Position currPos = board;
        Position tempPos;
        if ((n == 0) || node.stabilityIndex)//stabilityIndex is a boolean that tracks if the node is Stable ala quiescence search
            //return the node weight if the node is stable or has reached sufficent depth
            return currPos.value(node);//value() calculates the value of a move
        if (isMaximizingPlayer) {
            value = -999999999;//why so negative?
            tempPos = currPos.move(node);//Simulate the move
            tempPos.rotate();//Switch to other POV
            for (iMove move : tempPos.gen_moves(false)) //generate sub-nodes
            {
                value = Math.max(value, AlphaBeta((Move) move, n - 1, alpha, beta, false, tempPos));//sim next plie
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        if (!isMaximizingPlayer) {
            value = 999999999;//Why so positive?
            tempPos = currPos.move(node);//Simulate the move
            tempPos.rotate();//Switch to other POV
            for (iMove move : tempPos.gen_moves(false)) //Gen sub-nodes
            {
                value = Math.min(value, AlphaBeta((Move) move, n - 1, alpha, beta, true, tempPos));
                if (alpha >= beta)
                    break;
            }
            return value;
        }
        return -1;
    }

    //Can this be implemented in AlphaBeta? Thats were its used anyhow and we may not need to iterate over the potential moves as much as we do now. 
    public static boolean CalculateStabilityIndex(Move move, Position board)
    {
        int stabilityCriteria = 250;
        Position currPos = board;
        Position tempPos = currPos.move(move);
        tempPos.rotate();
        for (iMove i : tempPos.gen_moves(false))
        {
            if(tempPos.value(i) > stabilityCriteria)
                return false;
        }
        return true;
    }

}