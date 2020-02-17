package Chessbot2;

import java.util.ArrayList;
import java.util.Scanner;

import static Chessbot2.Chess.*;

public class Position {
    /* Brettet.
    Dette objektet husker hvor alle brikkene står,
    hvem som har lov til å rokere hvor, og hvor det er lov til å ta en passant.
    I tillegg har den funksjoner for å flytte brikker(og dermed opprette ett nytt brett med de nye posisjonene),
    generere lister over alle lovlige trekk, og å rotere brettet helt rundt.
     */
    String board;
    int score;
    Tuple WC;
    Tuple BC;
    int ep;
    boolean kp;

    public Position(String board, int score, Tuple WC, Tuple BC, int ep, Boolean kp) {
        this.board = board;
        this.score = score;
        this.WC = WC;
        this.BC = BC;
        this.ep = ep;
        this.kp = kp;
    }

    public boolean check_player_move(Tuple move){
        /* Den tredje og siste funksjonen for å sjekke lovligheten til spillerens trekk.
        Denne sjekker om spilleren har noen brikker som kan flytte der,
        og dobbelsjekker at spilleren ikke har prøvd å sette seg selv i sjakk eller noe annet lurt.

        IsAMove -> parse -> check_player_move
         */
        ArrayList<Tuple<Integer, Integer>> moves = gen_moves();
        if(!moves.contains(move)) return false;
        Position copy = copy();
        copy.move(move); //Lager en kopi av brettet, og simulerer hvordan det vil sett ut om spilleren hadde gjort det valgte trekket.
        copy = copy.rotate();
        int EnemyKing = 0;
        ArrayList<Tuple<Integer, Integer>> botmoves = copy.gen_moves();
        for(int i=0; i<copy.board.length(); i++){ //Finner hvor spillerens konge er.
            if(copy.board.charAt(i) == 'k') EnemyKing = i;
        }for(int i=0; i<botmoves.size(); i++) {
            Tuple<Integer, Integer> mellom = botmoves.get(i);
            if(mellom.getY() == EnemyKing) return false; //Sjekker om det er noen trekk motstanderen nå kan gjøre for å ta kongen.
        }return true;
    }

    public ArrayList<Tuple<Integer, Integer>> gen_moves(){
        // TODO: 30.01.2020 Yield
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
                        if ((retning == N + E || retning == N + W) && mål == '.' && til != ep) break;  //Om bonden prøver å gå skrått, og det ikke er mulig å ta en passant der.
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
        Returnerer en streng med de inverterte bokstavene, altså ikke en liste.
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

    public Position move(Tuple<Integer, Integer> move){
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

            if(til == 97){ //Flytter tårnet om du rokerer kort.
                newboard.setCharAt(98, '.');
                newboard.setCharAt(96, 'R');
                score += value(new Tuple(98, 96));
            }
            if(til == 92){ //Flytter tårnet om du rokerer langt.
                newboard.setCharAt(91, '.');
                newboard.setCharAt(93, 'R');
                score += value(new Tuple(91, 93));
            }
        }
        if(brikke == 'P') {
            if (til - fra == 2 * N) {
                ep = 119 - (fra + N); //Lagrer hvor det er greit å ta en passant
                TeP = 2; //Hvor mange trekk som kan gjøres før denne passenten ikke lenger er gyldig. TeP reduseres med 1 for hvert trekk.
            }
            if (til == ep) newboard.setCharAt(ep + S, '.'); //Drepper den passerte bonden i forbifarten
            if (til <= H8 && til >= A8) {
                if (spillerstur) { //Om spilleren flytter bonden til øverste rad, skal han få velge hvilken brikke han vil promotere til.
                    boolean promotert = false;
                    System.out.println("What piece do you want to promote to? Q/N/B/R ");
                    Scanner scanner = new Scanner(System.in);
                    while (!promotert) {
                        char nybrikke = scanner.next().charAt(0);
                        if (nybrikke == 'Q' || nybrikke == 'N' || nybrikke == 'B' || nybrikke == 'R') {
                            newboard.setCharAt(til, nybrikke);
                            promotert = true;
                        } else System.err.println("Try again. Did you remember upper case?");
                    }
                } else //For botten er det ingen vits i å sjekke hva som er best, så han bare får en dronning uansett. #Stalemeta
                    newboard.setCharAt(til, 'Q');
            }
        }
        if(ep > 0){ //Om passant er lovlig, et eller annet sted
            TeP -= 1;
            if(TeP == 0) ep = -1; //Fjerner muligheten til å ta en passant, etter at 2 trekk er blitt gjort.
        }
        return new Position(newboard.toString(), score, WC, BC, ep, kp); //Returnerer et nytt brett, der trekket er gjort
    }

    public int value(Tuple<Integer, Integer> move) {
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

        if(Character.isLowerCase(dreptbrikke)) deltascore += pst.get(Character.toUpperCase(dreptbrikke))[120-til];

        if (brikke == 'P')
            if (til <= H8 && til>= A8) deltascore += pst.get('Q')[til] - pst.get('P')[til]; //Ekstra score om du får en dronning.
        if (til == ep) {
            deltascore += pst.get('P')[ep+S]; //Ekstra score om du tok en brikke uten å ta på den (en passant)
        }
        return deltascore;
    }
    public Position copy(){ return new Position(this.board, this.score, this.WC, this.BC, this.ep, this.kp); }
}
//test2