package Chessbot2;

import javax.imageio.stream.ImageInputStream;

public class Position {

    static int score;
    static Tuple wc;
    static Tuple bc;
    static boolean ep;
    static boolean kp;
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
        char[] temp2 = board.toCharArray();
        System.out.println(temp2);
        for(int i=0; i<temp2.length/2; i++){
            int temp = temp2[i];
            temp2[i] = temp2[temp2.length -i -1];
            temp2[temp2.length -i -1] = (char) temp;
        } // TODO: 28.01.2020 Fiks at case blir endret
        return new Position(reverseCase(temp2), this.score, this.wc, this.bc, this.ep, this.kp);
    }
    public static String reverseCase(char[] chars) {
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
}

