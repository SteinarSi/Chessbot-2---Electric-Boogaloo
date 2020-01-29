package Chessbot2;

import javax.imageio.stream.ImageInputStream;

import static Chessbot2.Chess.A8;
import static Chessbot2.Chess.H8;

public class Position {

    static int score;
    static Tuple wc;
    static Tuple bc;
    static boolean ep;
    static boolean kp;
    static String board;

    public Position(String board, int score, Tuple wc, Tuple bc, Boolean ep, Boolean kp){
        this.board = board;
        this.score = score;
        this.wc = wc;
        this.bc = bc;
        this.ep = ep;
        this.kp = kp;
    }
    public void/*Tuple<Integer, Integer>*/ gen_moves() {
        // TODO: 28.01.2020 Fiks rokader, passant, yield
        /* Går igjennom alle brikkene til spilleren som skal flytte, og finner hvilke lovlige trekk brikken har.
        Nå bare printer den trekket, men planen er å få den til å kontinuerlig sende disse trekkene til Searcher(),
        så den kan vurdere trekk samtidig som den finner nye trekk.
         */
        for (int i = 0; i < board.length(); i++) {
            char p = board.charAt(i);
            if (!Character.isUpperCase(p)) {
                continue;
            }
            for (int d : Chess.directions.get(p)) {
                for (int a = i + d; true; a += d) {
                    char q = board.charAt(a);

                    if (Character.isUpperCase(q) || Character.isWhitespace(q)) break;

                    if (p == 'P') {
                        if ((d == Chess.N || d == Chess.N * 2) && q != '.') break;
                        if (d == Chess.N * 2 && (i < Chess.A1 + Chess.N || board.charAt(Chess.N + i) != '.')) break;
                        if ((d == Chess.N + Chess.E || d == Chess.N + Chess.W) && q == '.') break;
                    }
                    System.out.println(i + " til " + a);
                    if (p == 'P' || p == 'N' || p == 'K' || Character.isLowerCase(q)) break;
                }
            }
        }
    }
    public Position rotate(){
        /* Flipper brettet helt rundt, og inverterer casen til alle bokstavene.
         */
        char[] temp2 = board.toCharArray();
        for(int i=0; i<temp2.length/2; i++){
            int temp = temp2[i];
            temp2[i] = temp2[temp2.length -i -1];
            temp2[temp2.length -i -1] = (char) temp;
        }
        return new Position(reverseCase(temp2), this.score, this.wc, this.bc, this.ep, this.kp);
    }
    public static String reverseCase(char[] chars) {
        /* Går igjennom hele listen med bokstaver, og inverterer casen til alle bokstavene.
         */
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                chars[i] = Character.toLowerCase(c);
            }
            else if (Character.isLowerCase(c)) {
                chars[i] = Character.toUpperCase(c);
            }
        }
        return new String(chars);
    }

    public static Position move(Tuple move){
        /* Tar f. eks (85, 65) som input, ikke (e2, e4).
        Returnerer ett nytt brett, der trekket er blitt gjort.
         */
        int fra = (int) move.getX();
        int til = (int) move.getY();
        char piece = board.charAt(fra);

        // score = score + value(move);
        // TODO: 29.01.2020 lag Value()

        //Flytter brikken
        StringBuilder newboard = new StringBuilder(board);
        newboard.setCharAt(til, piece);
        newboard.setCharAt(fra, '.');

        //Oppdaterer rokadebetingelser
        if(fra == 91) wc.setX(false);
        if(fra == 98) wc.setY(false);
        if(fra == 21) bc.setX(false);
        if(fra == 28) bc.setY(false);

        //Rokerer
        if (piece == 'K'){
            if(til == 97){
                newboard.setCharAt(98, '.');
                newboard.setCharAt(96, 'R');
            }
            if(til == 92){
                newboard.setCharAt(91, '.');
                newboard.setCharAt(93, 'R');
            }
            // TODO: 29.01.2020 Logikk som sjekker om svart eller hvit flytter.
            //  Nå kan ikke hvit rokere etter svart
            wc.setX(false);
            wc.setY(false);
        }
        if (piece == 'P'){
            // TODO: 29.01.2020 En passant, og at spilleren skal få velge hvilken brikke han vil ha
            if(til <= H8){
                newboard.setCharAt(til, 'Q');
            }
        }



        return new Position(newboard.toString(), score, wc, bc, kp, ep);

    }

}
