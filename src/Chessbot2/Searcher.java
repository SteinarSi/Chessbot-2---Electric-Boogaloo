package Chessbot2;

import java.util.ArrayList;
import java.util.Random;

public class Searcher {

    static int depth = 3;
    private static String print;

    public static Move<Integer, Integer> findMove(Position currentBoard){
        Random random = new Random();
        ArrayList<Move<Integer, Integer>> moves = currentBoard.gen_moves();
        return moves.get(random.nextInt(moves.size()));

    }
    public static Move<Integer, Integer> findOkMove(Position currentBoard){
        int bestvalue = -100_000_000;
        Move<Integer, Integer> bestmove = null;
        ArrayList<Move<Integer, Integer>> moves = currentBoard.gen_moves();
        for(Move<Integer, Integer> move : moves){
            int initvalue = currentBoard.value(move);
            Position copy = currentBoard.copy();
            copy = copy.move(move);
            copy = copy.rotate();

            int bestvalue2 = -100_000_000;
            Move<Integer, Integer> bestmove2 = null;
            ArrayList<Move<Integer, Integer>> moves2 = copy.gen_moves();
            for(Move<Integer, Integer> move2 : moves2){
                int score2 = copy.value(move2);
                Position copy2 = copy.copy();
                copy2 = copy2.move(move2);
                copy2 = copy2.rotate();

                int bestvalue3 = -100_000_000;
                ArrayList<Move<Integer, Integer>> moves3 = copy2.gen_moves();
                for(Move move3 : moves3){
                    int score3 = copy2.value(move3);
                    Position copy3 = copy2.copy();
                    copy3 = copy3.move(move3);
                    copy3 = copy3.rotate();

                    int bestvalue4 = -100_000_000;
                    ArrayList<Move<Integer, Integer>> moves4 = copy3.gen_moves();
                    for(Move move4 : moves4){
                        int score4 = copy3.value(move4);
                        Position copy4 = copy3.copy();
                        copy4 = copy4.move(move4);
                        copy4 = copy4.rotate();

                        int bestvalue5 = -100_000_000;
                        ArrayList<Move<Integer, Integer>> moves5 = copy4.gen_moves();
                        for(Move move5 : moves5){
                            int score5 = copy4.value(move5);
                            if(score5 > bestvalue5){
                                bestvalue5 = score5;
                            }
                        }
                        score4 -= bestvalue5;
                        if(score4 > bestvalue4){
                            bestvalue4 = score4;
                        }
                    }
                    score3 -= bestvalue4;
                    if(score3 > bestvalue3){
                        bestvalue3 = score3;
                    }
                }
                score2 -= bestvalue3;
                if(score2 > bestvalue2){
                    bestvalue2 = score2;
                    bestmove2 = move2;
                }
            }initvalue -= bestvalue2;
            if(initvalue > bestvalue){
                bestvalue = initvalue;
                bestmove = move;
                print = "Svart: " + bestmove + " Hvit: " + bestmove2;
            }
        }
        System.out.println(print);

        return bestmove;
    }
}
