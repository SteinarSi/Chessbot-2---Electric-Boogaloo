package Chessbot2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Chessbot2.Chess.*;

public class Searcher {
    public static iMove<Integer, Integer> findMove(Position currentBoard){
        Random random = new Random();
        ArrayList<iMove<Integer, Integer>> moves = currentBoard.gen_moves(true);
        return moves.get(random.nextInt(moves.size()));

    }
    public static iMove<Integer, Integer> findOkMove(Position currentBoard){
        ArrayList<iMove<Integer, Integer>> moves = currentBoard.gen_moves(true);
        int bestvalue = -100_000_000;
        iMove<Integer, Integer> bestmove = null;

        for(iMove<Integer, Integer> move : moves){
            int initvalue = 0;
            Position copy = currentBoard.copy();
            initvalue += currentBoard.value(move);
            copy = copy.move(move);
            copy.rotate();

            int newbestvalue = -100_000_000;
            iMove<Integer, Integer> newbestmove = null;
            ArrayList<iMove<Integer, Integer>> newmoves = copy.gen_moves(true);
            for(iMove<Integer, Integer> newmove : newmoves){
                int newscore = copy.value(newmove);
                if(newscore > newbestvalue){
                    newbestvalue = newscore;
                    newbestmove = newmove;
                }
            }
            initvalue -= newbestvalue;
            if(initvalue > bestvalue){
                bestvalue = initvalue;
                bestmove = move;
            }
        }
        System.out.println("Beste score var: " + bestvalue);
        System.out.println("Beste trekket var: " + bestmove);

        if(bestmove == null) System.out.println("Beste trekket finnes ikke!");
        return bestmove;
    }
}
