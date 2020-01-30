package Chessbot2;

import java.util.ArrayList;

import static Chessbot2.Chess.*;

public class Position {
    /* Brettet.
    Dette objektet husker hvor alle brikkene står,
    hvem som har lov til å rokerer hvor, og om det er lov til å ta en passant.
    I tillegg har den funksjoner for å flytte brikker(og dermed opprette ett nytt brett med de nye posisjonene),
    generere lister over alle lovlige trekk, og å rotere brettet helt rundt.
     */
    static int score;
    static Tuple WC;
    static Tuple BC;
    static boolean ep;
    static boolean kp;
    static String board;

    public Position(String board, int score, Tuple WC, Tuple BC, Boolean ep, Boolean kp){
        Position.board = board;
        Position.score = score;
        Position.WC = WC;
        Position.BC = BC;
        Position.ep = ep;
        Position.kp = kp;
    }
    public void/*Tuple<Integer, Integer>*/ gen_moves(Tuple trekk) {
        // TODO: 28.01.2020 Fiks rokader, en passant, yield
        /* Går igjennom alle brikkene til spilleren som skal flytte, og finner hvilke lovlige trekk hver enkelt brikke har.
        Hvis det er et menneske som skal flytte tar den det potensielle trekket som input,
        og sjekker om det trekket er lovlig. Hvis ja, endrer den Chess.lovlig til true, så trekket blir gjort.
        For nå bare printer den trekket, men planen er å få den til å kontinuerlig sende disse trekkene til Searcher(),
        så den kan vurdere trekk samtidig som den finner nye trekk.
         */
        for (int i = 0; i < board.length(); i++) {
            char p = board.charAt(i);
            if (!Character.isUpperCase(p)) {
                continue;
            }
            for (int d : directions.get(p)) {
                for (int a = i + d; true; a += d) {
                    char q = board.charAt(a);

                    if (Character.isUpperCase(q) || Character.isWhitespace(q)) break; //Om brikken prøver å ta en vennlig brikke, eller prøver å gå av brettet

                    if (p == 'P') {
                        if ((d == N || d == N * 2) && q != '.') break;
                        if (d == N * 2 && (i < A1 + N || board.charAt(N + i) != '.')) break;
                        if ((d == N + E || d == N + W) && q == '.') break;
                    }
                    if(trekk.equals(new Tuple(i, a))) lovlig = true; //Om det genererte trekket er lik trekket spilleren har lyst til å gjøre

                    //System.out.println(i + " til " + a);
                    if (p == 'P' || p == 'N' || p == 'K' || Character.isLowerCase(q)) break;
                }
            }
        }
    }
    public Position rotate(){
        /* Flipper brettet helt rundt, og inverterer casen til alle bokstavene.
        Returnerer et nytt brett.
         */
        char[] brettliste = board.toCharArray();
        for(int i=0; i<brettliste.length/2; i++){
            int temp = brettliste[i];
            brettliste[i] = brettliste[brettliste.length -i -1];
            brettliste[brettliste.length -i -1] = (char) temp;
        }
        return new Position(reverseCase(brettliste), score, WC, BC, ep, kp);
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

    public static Position move(Tuple<Integer, Integer> move){
        /* Tar f. eks Tuple(85, 65) som input, ikke Tuple(e2, e4).
        Returnerer ett nytt brett, der trekket er blitt gjort.
        Den oppdaterer også betingelser for rokade, osv.
         */
        int fra = move.getX();
        int til = move.getY();
        char brikke = board.charAt(fra);

        score += value(move);

        //Flytter brikken
        StringBuilder newboard = new StringBuilder(board);
        newboard.setCharAt(til, brikke);
        newboard.setCharAt(fra, '.');

        //Oppdaterer rokadebetingelser
        if(fra == 91) WC.setX(false);
        if(fra == 98) WC.setY(false);
        if(fra == 21) BC.setX(false);
        if(fra == 28) BC.setY(false);

        //Rokerer
        if (brikke == 'K'){
            if(Chess.black) BC = new Tuple(false, false);
            else WC = new Tuple(false, false);

            if(til == 97){
                newboard.setCharAt(98, '.');
                newboard.setCharAt(96, 'R');
                score += value(new Tuple(98, 96));
            }
            if(til == 92){
                newboard.setCharAt(91, '.');
                newboard.setCharAt(93, 'R');
                score += value(new Tuple(91, 93));
            }
        }
        //Promoterer bønder, eventuelt en passant
        if (brikke == 'P'){
            // TODO: 29.01.2020 En passant, og at spilleren skal få velge hvilken brikke han vil promotere til
            if(til <= H8 && til >= A8){
                newboard.setCharAt(til, 'Q');
            }
        }
        return new Position(newboard.toString(), score, WC, BC, kp, ep);
    }

    public static int value(Tuple<Integer, Integer> move) {
        /* Returnerer verdien til et spesifikt trekk.
        Om du tar en dronning sier denne "bra, +879 poeng",
        og om du flytter hesten ned til høyre hjørne sier han "Hva faen, -131 poeng".
        Istedet for å gå igjennom hele brettet og regne ut score på nytt om og om igjen,
        kan vi heller bruke denne funksjonen til å kontinuerlig oppdatere scoren hver gang den endres.
         */
        int fra = move.getX();
        int til = move.getY();
        char brikke = board.charAt(fra);
        char dreptbrikke = board.charAt(til);

        int deltascore = pst.get(brikke)[til] - pst.get(brikke)[fra];

        if(Character.isLowerCase(dreptbrikke)){
            deltascore += pst.get(Character.toUpperCase(dreptbrikke))[119-til];
        }
        if (brikke == 'P')
            if (til <= H8 && til>= A8) deltascore += pst.get('Q')[til] - pst.get('P')[til];
        // TODO: 29.01.2020 Passantlogikk

        return deltascore;
    }
}
