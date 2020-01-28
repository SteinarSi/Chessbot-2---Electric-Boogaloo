package Chessbot2;

import java.util.Dictionary;
import java.util.Hashtable;

public class Chess {
    static String board =
            "          \n"+ // 0 - 9
            "          \n"+ // 10 - 19
            " rnbqkbnr \n"+ // 20 - 29
            " pppp.ppp \n"+ // 30 - 39
            " ........ \n"+ // 40 - 49
            " ....p... \n"+ // 50 - 59
            " ........ \n"+ // 60 - 69
            " .....N.. \n"+ // 70 - 79
            " PPPPPPPP \n"+ // 80 - 89
            " RNBQKB.R \n"+ // 90 - 99
            "          \n"+ // 100 - 109
            "          "; // 110 - 119

    //Himmeldirectionser
    static int N = -10;
    static int E = 1;
    static int S = 10;
    static int W = -1;

    //Hjørneindekser
    static int A1 =  91;
    static int H1 = 98;
    static int A8 = 21;
    static int H8 = 28;
    static Tuple WC = new Tuple(true, true);
    static Tuple BC = new Tuple(true, true);

    static Dictionary<Character, int []> directions = new Hashtable();

    public static void main(String[] args) {
        //Oppretter en dict med alle himmelretningene hver enkelt brikke kan bevege seg.
        directions.put('P', new int[] {N, N+N, N+W, N+E});
        directions.put('N', new int[] {N+N+W, E+N+E, E+S+E, S+S+E, S+S+W, W+S+W, W+N+W, N+N+W});
        directions.put('B', new int[] {N+E, S+E, S+W, N+W});
        directions.put('R', new int[] {N, E, S, W});
        directions.put('Q', new int[] {N, E, S, W, N+E, S+E, S+W, N+W});
        directions.put('K', new int[] {N, E, S, W, N+E, S+E, S+W, N+W});
        Position P = new Position(board, 0, WC, BC, true, true);
        System.out.println(board);
        System.out.println(P.rotate().board);


    }
    public static int parse(String c){
        // Konverterer poisjonen på brettet fra bokstav+tall til kun tall. f. eks. e2 blir 85.
        int fil = (int) c.charAt(0) - 'a';
        int rank = c.charAt(1) - '1';
        return A1 + fil - 10*rank;
    }
    public static int rotinp(int i){
        return 119-i;
    }


}

