package Chessbot2;

import java.util.ArrayList;
import java.util.Random;

public class Searcher {
    public static Tuple<Integer, Integer> findMove(Position currentBoard){
        Random random = new Random();
        ArrayList<Tuple<Integer, Integer>> moves = currentBoard.gen_moves();
        return moves.get(random.nextInt(moves.size()));

    }
}
