package Chessbot2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Searcher {

    static int depth = 3;
    private static String print;

    public static Move findRandomMove(Position currentBoard){
        Random random = new Random();
        ArrayList<Move> moves = currentBoard.gen_moves();
        return moves.get(random.nextInt(moves.size()));

    }
    public static Move findRecursiveMove(Position currentBoard){
        ArrayList<Move> possiblemoves = currentBoard.gen_moves();
        for(Move move : possiblemoves){
            move.addWeight(currentBoard.value(move));
        }
        //Collections.sort(possiblemoves, Collections.reverseOrder());
        //possiblemoves.subList(0, possiblemoves.size()/2);
        for(int i=0; i<possiblemoves.size(); i++){
            Move move = possiblemoves.get(i);
            //System.out.println("Topptrekk: " + move);
            move.addWeight(RecursiveValue(currentBoard, move, depth, false));
            //System.out.println("verdi: " + move.getWeight());
        }
        Collections.sort(possiblemoves, Collections.reverseOrder());
        //System.out.println("Beste trekk: " + possiblemoves.get(0) + " med en vekt på " + possiblemoves.get(0).getWeight());
        return possiblemoves.get(0);
    }
    public static int RecursiveValue(Position currentBoard, Move move, int depth, boolean Max){
        if(depth>0){
            int ret = currentBoard.value(move);
            currentBoard = currentBoard.move(move);
            currentBoard = currentBoard.rotate();

            //Om det er botten sin tur
            if(Max){
                ArrayList<Move> responses = currentBoard.gen_moves();
                for(Move response : responses) response.addWeight(currentBoard.value(response));

                Collections.sort(responses, Collections.reverseOrder());
                responses.subList(0, responses.size()/3+1);
                for(int i=0; i<responses.size(); i++){
                    Move response = responses.get(i);
                    response.addWeight(RecursiveValue(currentBoard, response, depth-1, false));
                }
                Collections.sort(responses, Collections.reverseOrder());
                return ret + responses.get(0).getWeight();
            }

            //Om det er spillerens tur
            else {
                ArrayList<Move> responses = currentBoard.gen_moves();
                for (Move response : responses) response.addWeight(currentBoard.value(response));

                Collections.sort(responses, Collections.reverseOrder());
                responses.subList(0, responses.size()/3+1);
                for (int i=0; i<responses.size(); i++) {
                    Move response = responses.get(i);
                    response.addWeight(RecursiveValue(currentBoard, response, depth-1, true));
                }
                Collections.sort(responses, Collections.reverseOrder());
                return ret - responses.get(0).getWeight();
            }
        }
        //else System.out.println(currentBoard.board + " " + currentBoard.score);
        else return 0;
    }

    public static Move findOkMove(Position currentBoard){
        int bestvalue = -100_000_000;
        Move bestmove = null;
        ArrayList<Move> moves = currentBoard.gen_moves();
        for(Move move : moves){
            int initvalue = currentBoard.value(move);
            Position copy = currentBoard.copy();
            copy = copy.move(move);
            copy = copy.rotate();

            int bestvalue2 = -100_000_000;
            Move bestmove2 = null;
            ArrayList<Move> moves2 = copy.gen_moves();
            for(Move move2 : moves2){
                int score2 = copy.value(move2);
                Position copy2 = copy.copy();
                copy2 = copy2.move(move2);
                copy2 = copy2.rotate();

                int bestvalue3 = -100_000_000;
                ArrayList<Move> moves3 = copy2.gen_moves();
                for(Move move3 : moves3){
                    int score3 = copy2.value(move3);
                    Position copy3 = copy2.copy();
                    copy3 = copy3.move(move3);
                    copy3 = copy3.rotate();

                    int bestvalue4 = -100_000_000;
                    ArrayList<Move> moves4 = copy3.gen_moves();
                    for(Move move4 : moves4){
                        int score4 = copy3.value(move4);
                        Position copy4 = copy3.copy();
                        copy4 = copy4.move(move4);
                        copy4 = copy4.rotate();

                        int bestvalue5 = -100_000_000;
                        ArrayList<Move> moves5 = copy4.gen_moves();
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

    public static int filthyHelper(Position currentBoard, int dist){
        if (dist <= 0)
            return currentBoard.getScore();

        List<Position> futures = new ArrayList();
        for (Move m : currentBoard.gen_moves()) {
            futures.add(currentBoard.move(m).rotate());
        }

        Collections.sort(futures);

        int ret = -9999999;
        for(int i = 0; i < futures.size() && i < 5; i++) {
            int nVal = filthyHelper(futures.get(i), dist - 1);
            if (nVal > ret) ret = nVal;
        }

        return ret;
    }

    static Move findFilthyMove(Position current, int dist) {
        Move ret = new Move(0, 0);
        int score = -9999999;
        for (Move move : current.gen_moves()) {
            Position pos = current.move(move).rotate();
            int score2 = -9999999;
            for (Move m : pos.gen_moves()) {
                int n = filthyHelper(pos.move(m).rotate(), dist - 2);
                if (n > score2)
                    score2 = n;
            }
            if (score < score2) {
                score = score2;
                ret = move;
            }
        }

        return ret;
    }
}