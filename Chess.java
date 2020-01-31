package Chessbot2;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class Chess {
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
    "          ";   // 111 - 118
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
    //Hvem som kan rokere, og hvor
    static Tuple WC = new Tuple(true, true);
    static Tuple BC = new Tuple(true, true);
    //Genererer dicter for brettverdier og himmelretningene til hver enkelt brikke
    public static Dictionary<Character, Integer[]> pst = Generator.makePst();
    public static Dictionary<Character, Integer[]> directions = Generator.makeDirections();

    static boolean black = false;
    static boolean play = true;
    static boolean lovlig = false;
    static boolean gjorttrekk = false;
    static boolean spillerstur = true;
    static int TeP = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Position P = new Position(board, 0, WC, BC, 0, true);
        System.out.println(board);
        outer: while(play) {
            lovlig = false;
            gjorttrekk = false;

            while (!gjorttrekk) {
                System.out.println("Ditt trekk: ");
                String input = scanner.nextLine();
                input = input.replaceAll("\\s", "");
                if (input.equals("quit")){
                    scanner.close();
                    break outer;
                }



                Tuple<Integer, Integer> trekk = parse(input);
                if (P.gen_player_moves(trekk)) {
                    P = P.move(trekk);
                    gjorttrekk = true;
                } else System.err.println("Ulovlig trekk. Prøv igjen.");
            }
            P = P.rotate();
            System.out.println(P.board);

        }
    }
    public static Tuple<Integer, Integer> parse(String c){
        /* Konverterer posisjonen på brettet fra bokstav+tall+bokstav+tall til kun en tuppel av tall.
        f. eks. "e2 e4" blir Tuple(85, 65).
        Planen er at kun Chess skal få se syntakser som e2e4,
        og at den skal konvertere alt slikt til Tupler og tall før den sender det til Position og Searcher.
         */
        int filfra = (int) c.charAt(0) - 'a';
        int rankfra = c.charAt(1) - '1';
        int x = A1 + filfra - 10*rankfra; //Koordinat fra

        int filftil = (int) c.charAt(2) - 'a';
        int ranktil = c.charAt(3) - '1';
        int y = A1 + filftil - 10*ranktil; //Koordinat til

        return new Tuple<>(x, y);
    }
    public static boolean IsAMove(String input){
        /* En funskjon for å sjekke om spilleren skrev noe som kan tolkes som et trekk eller ikke. 
        "e2 e4" returnerer true, "sdlfnsjgbskjøfbnskjfba" returner false.
          */
        // TODO: 31.01.2020 Skriv ferdig denne 
        Character første = input.charAt(0);
        Character andre = input.charAt(1);
        Character tredje = input.charAt(2);
        Character fjerde = input.charAt(3);
        return true;
    }
}

