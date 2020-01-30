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

    public Position(String board, int score, Tuple WC, Tuple BC, Boolean ep, Boolean kp) {
        Position.board = board;
        Position.score = score;
        Position.WC = WC;
        Position.BC = BC;
        Position.ep = ep;
        Position.kp = kp;
    }

    public boolean gen_player_moves(Tuple trekk) {
        // TODO: 28.01.2020 En passant
        /* Går igjennom alle brikkene til spilleren, og finner hvilke lovlige trekk hver enkelt brikke har.
        Oppretter en liste over lovlige trekk, og sjekker så om noen av disse samsvarer med trekket spilleren hadde lyst til å gjøre.
        Returnerer true/false.
         */
        ArrayList<Tuple> lovligeliste = new ArrayList<>();
        for (int fra = 0; fra < board.length(); fra++) {
            char brikke = board.charAt(fra);
            if (!Character.isUpperCase(brikke)) {
                continue;
            }
            //Sjekker om rokering er lovlig, legger det til i listen
            if (black) {
                if ((brikke == 'K' && (boolean) BC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    if (trekk.equals(new Tuple(95, 93))) return true; //Svart rokerer langt
                }
                if ((brikke == 'K' && (boolean) BC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    if (trekk.equals(new Tuple(95, 97))) return true; //Svart rokerer kort
                }
            } else {
                if ((brikke == 'K' && (boolean) WC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    if (trekk.equals(new Tuple(95, 93))) return true; //Hvit rokerer langt
                }
                if ((brikke == 'K' && (boolean) WC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    if (trekk.equals(new Tuple(95, 97))) return true; //Hvit rokerer kort
                }
            }
            //Legger til alle normale trekk
            for (int retning : directions.get(brikke)) {
                for (int til = fra + retning; true; til += retning) {
                    char mål = board.charAt(til);

                    if (Character.isUpperCase(mål) || Character.isWhitespace(mål))
                        break; //Om brikken prøver å ta en vennlig brikke, eller prøver å gå av brettet

                    if (brikke == 'P') {
                        if ((retning == N || retning == N * 2) && mål != '.') break;
                        if (retning == N * 2 && (fra < A1 + N || board.charAt(N + fra) != '.')) break;
                        if ((retning == N + E || retning == N + W) && mål == '.') break;
                    }
                    //Om det genererte trekket har kommet helt hit uten å bli brutt,
                    // og det er det samme som det trekket spilleren prøvde, er spillerens trekk offisielt et lovlig et.
                    if (trekk.equals(new Tuple(fra, til))) return true;

                    if (brikke == 'P' || brikke == 'N' || brikke == 'K' || Character.isLowerCase(mål)) break;
                }
            }
        }
        return false;
    }
    public ArrayList<Tuple<Integer, Integer>> gen_bot_moves(){
        // TODO: 30.01.2020 En passant, og yield
        /* En funksjon som genererer en Array av lovlige trekk en spiller har lov til å gjøre.
        Laget med hensyn på en bot, vi kan senere implementere noe yield-shit istedenfor lister.
         */
        ArrayList<Tuple<Integer, Integer>> lovligliste = new ArrayList<>();
        for (int fra = 0; fra < board.length(); fra++) {
            char brikke = board.charAt(fra);
            if (!Character.isUpperCase(brikke)) {
                continue;
            }
            //Sjekker om rokering er lovlig, legger det til i listen
            if(black) {
                if ((brikke == 'K' && (boolean) BC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligliste.add(new Tuple(95, 93)); //Svart rokerer langt
                }
                if ((brikke == 'K' && (boolean) BC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligliste.add(new Tuple(95, 97)); //Svart rokerer kort
                }
            } else {
                if ((brikke == 'K' && (boolean) WC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligliste.add(new Tuple(95, 93)); //Hvit rokerer langt
                }
                if ((brikke == 'K' && (boolean) WC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligliste.add(new Tuple(95, 97)); //Hvit rokerer kort
                }
            }
            //Legger til alle normale trekk
            for (int retning : directions.get(brikke)) {
                for (int til = fra + retning; true; til += retning) {
                    char mål = board.charAt(til);

                    //Om brikken prøver å ta en vennlig brikke, eller prøver å gå av brettet
                    if (Character.isUpperCase(mål) || Character.isWhitespace(mål)) break;

                    //Bondelogikk. Sørger for at bønder ikke kan ta noe framlengs, at de kun kan gå ett eller to skritt om gangen(og ikke 7, som dronningen).
                    if (brikke == 'P') {
                        if ((retning == N || retning == N * 2) && mål != '.') break;
                        if (retning == N * 2 && (fra < A1 + N || board.charAt(N + fra) != '.')) break;
                        if ((retning == N + E || retning == N + W) && mål == '.') break;
                    }
                    //Om det genererte trekket har kommet helt ned hit uten å bli brutt, er det offisielt et lovlig trekk, som botten må inspisere.
                    lovligliste.add(new Tuple(fra, til));

                    //Om brikken bare kan flyttet et steg om gangen, vil dette bryte loopen etter det steget er lagt til.
                    if (brikke == 'P' || brikke == 'N' || brikke == 'K' || Character.isLowerCase(mål)) break;
                }
            }
        }
        return lovligliste;
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
        if (black){
            if(fra == 21) BC.setX(false);
            if(fra == 28) BC.setY(false);
        } else{
            if(fra == 91) WC.setX(false);
            if(fra == 98) WC.setY(false);
        }
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

        if(Character.isLowerCase(dreptbrikke)) deltascore += pst.get(Character.toUpperCase(dreptbrikke))[119-til];
        if (brikke == 'P')
            if (til <= H8 && til>= A8) deltascore += pst.get('Q')[til] - pst.get('P')[til];
        // TODO: 29.01.2020 Passantlogikk

        return deltascore;
    }
}
