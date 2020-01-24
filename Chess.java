package Chessbot2;

import java.util.Dictionary;
import java.util.Hashtable;

public class Chess {
    static String board =
            "          \n"+
            "          \n"+
            " rnbqkbnr \n"+
            " pppppppp \n"+
            " ........ \n"+
            " ........ \n"+
            " ........ \n"+
            " ........ \n"+
            " PPPPPPPP \n"+
            " RNBQKBNR \n"+
            "          \n"+
            "          ";

    //Himmeldirectionser
    static int N = -10;
    static int E = 1;
    static int S = 10;
    static int W = -1;
    static int A1 =  91;
    static int H1 = 98;
    static int A8 = 21;
    static int H8 = 28;

    static Dictionary<Character, int []> directions = new Hashtable();

    public static void main(String[] args) {
        directions.put('P', new int[] {N, N+N, N+W, N+E});
        directions.put('N', new int[] {N+N+W, E+N+E, E+S+E, S+S+E, S+S+W, W+S+W, W+N+W, N+N+W});
        directions.put('B', new int[] {N+E, S+E, S+W, N+W});
        directions.put('R', new int[] {N, E, S, W});
        directions.put('Q', new int[] {N, E, S, W, N+E, S+E, S+W, N+W});
        directions.put('K', new int[] {N, E, S, W, N+E, S+E, S+W, N+W});
        Position P = new Position();
        P.gen_moves();

    }



}

class Tuple<X, Y> {
    public final X x;
    public final Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}

