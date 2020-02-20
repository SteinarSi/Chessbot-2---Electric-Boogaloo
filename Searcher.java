package Chessbot2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static Chessbot2.Chess.*;

public class Searcher {
    public static Tuple<Integer, Integer> findMove(Position currentBoard){
        Random random = new Random();
        ArrayList<Tuple<Integer, Integer>> moves = currentBoard.gen_moves();
        return moves.get(random.nextInt(moves.size()));

    }
    public static Tuple<Integer, Integer> findOkMove(Position currentBoard){
        ArrayList<Tuple<Integer, Integer>> moves = currentBoard.gen_moves();
        int bestvalue = -100_000_000;
        Tuple<Integer, Integer> bestmove = null;

        for(Tuple<Integer, Integer> move : moves){
            int initvalue = 0;
            Position copy = currentBoard.copy();
            initvalue += currentBoard.value(move);
            copy = copy.move(move);
            copy.rotate();

            int newbestvalue = -100_000_000;
            Tuple<Integer, Integer> newbestmove = null;
            ArrayList<Tuple<Integer, Integer>> newmoves = copy.gen_moves();
            for(Tuple<Integer, Integer> newmove : newmoves){
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
