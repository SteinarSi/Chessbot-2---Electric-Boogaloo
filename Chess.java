package Chessbot2;

import java.util.Dictionary;
import java.util.Scanner;

public class Chess<pst> {
    static String board =
    "         \n"+ // 1 - 8
    "         \n"+ // 11 - 18
    " rnbqkbnr\n"+ // 21 - 28
    " pppppppp\n"+ // 31 - 38
    " ........\n"+ // 41 - 48
    " ........\n"+ // 51 - 58
    " ........\n"+ // 61 - 68
    " ........\n"+ // 71 - 78
    " PPPPPPPP\n"+ // 81 - 88
    " RNBQKBNR\n"+ // 91 - 98
    "         \n"+ // 101 - 108
    "         ";   // 111 - 118
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
    //Hvem som kan rokere
    static Tuple WC = new Tuple(true, true);
    static Tuple BC = new Tuple(true, true);
    public static Dictionary<Character, Integer[]> pst = Generator.makePst();
    public static Dictionary<Character, Integer[]> directions;
    //Hvem som spiller
    static boolean black = false;
    static boolean play = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Dictionary<Character, Integer[]> directions = Generator.makeDirections();
        Position P = new Position(board, 0, WC, BC, true, true);
        System.out.println(board);
        while(play){
            System.out.println("Ditt trekk: ");
            String input = scanner.nextLine();
            if(input.equals("quit")) break;

            System.out.println(P.move(parse(input)).board);





        }
    }
    public static Tuple<Integer, Integer> parse(String c){
        /* Konverterer posisjonen på brettet fra bokstav+tall til kun tall. f. eks. e2 blir 85.
        Planen er at kun Chess får se syntakser som e2e4,
        og den konverterer slikt til tall før den sender det til Position og Searcher.
         */

        c = c.replaceAll("\\s", "");

        int filfra = (int) c.charAt(0) - 'a';
        int rankfra = c.charAt(1) - '1';
        int x = A1 + filfra - 10*rankfra;
        int filftil = (int) c.charAt(2) - 'a';
        int ranktil = c.charAt(3) - '1';
        int y = A1 + filftil - 10*ranktil;
        return new Tuple<Integer, Integer>(x, y);
    }
    public static int rotinp(int i){
        return 119-i;
    }

}

