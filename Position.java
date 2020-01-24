package Chessbot2;

import java.util.Dictionary;

import static Chessbot2.Chess.directions;

public class Position {
    static String board =
            "         \n"+
            "         \n"+
            " rnbqkbnr\n"+
            " pppppppp\n"+
            " ........\n"+
            " ........\n"+
            " ........\n"+
            " ........\n"+
            " PPPPPPPP\n"+
            " RNBQKBNR\n"+
            "         \n"+
            "         ";
    public void/*Tuple<Integer, Integer>*/ gen_moves(){
        for (int i=0; i<board.length(); i++){
            char p = board.charAt(i);
            if (!Character.isUpperCase(p)){
                continue;
            }
            for(int d : Chess.directions.get(board.charAt(i))){
                for (int a = i+d; true; a+=d){
                    char q = board.charAt(a);

                    if(Character.isUpperCase(q) || Character.isWhitespace(q)) break;

                    if(p == 'P') {
                        if ((d == Chess.N || d == Chess.N * 2) && q != '.') break;
                        if (d==Chess.N*2 && (i<Chess.A1+Chess.N || board.charAt(Chess.N+i)!= '.')) break;
                        if ((d==Chess.N + Chess.E || d== Chess.N + Chess.W) && q == '.') break;
                    }
                    System.out.println(i + " til " + a);
                    if (p == 'P' || p == 'N' || p == 'K' || Character.isLowerCase(q)) break;
                }
            }
        }
    }

    /*
    public Position rotate(){
        char[] temp = board.toCharArray();
    }

    */

}

