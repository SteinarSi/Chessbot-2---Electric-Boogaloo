package Chessbot2;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static Chessbot2.Chess.*;

public class Position implements Comparable<Position> {
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

    public boolean check_player_move(Move move){
        /* Den tredje og siste funksjonen for å sjekke lovligheten til spillerens trekk.
        Denne sjekker om spilleren har noen brikker som kan flytte der,
        og dobbelsjekker at spilleren ikke har prøvd å sette seg selv i sjakk eller noe annet lurt.

        IsAMove -> parse -> check_player_move
         */

        boolean ret = false;
        ArrayList<Move> genmoves = gen_moves(); //Opretter en liste over nesten-lovlige trekk.
        for(Move genmove : genmoves) {
            if (genmove.equals(move)) {
                ret = true; //Om trekket er med i listen over nesten-lovligetrekk.
            }
        }
        if(ret){
            Position copy = copy();
            copy = copy.move(move); //Lager en kopi av brettet, og simulerer hvordan det ville sett ut om spilleren hadde gjort det valgte trekket.
            copy = copy.rotate();
            int King = 0;
            ArrayList<Move> botmoves = copy.gen_moves();
            for(int i=0; i<copy.board.length(); i++){ //Finner hvor spillerens konge er.
                if(copy.board.charAt(i) == 'k') King = i;
            }
            for(int i=0; i<botmoves.size(); i++) {
                Move mellom = botmoves.get(i);
                if (mellom.getY() == King) {
                    System.err.println("Don't put your king in check!");
                    return false; //Sjekker om det er noen trekk motstanderen nå kan gjøre for å ta kongen.
                }
            }
        }
        return ret;
    }

    public ArrayList<Move> gen_moves(){
        /* En funksjon som genererer en ArrayList av nesten-lovlige trekk en spiller har lov til å gjøre.
        Tar ikke hensyn til om trekket setter kongen i sjakk eller ikke, det må botten regne ut selv.
        Spillerens trekk kan sjekkes med check_player_move.
         */
        ArrayList<Move> lovligliste = new ArrayList<>();
        for (int fra = 0; fra < board.length(); fra++) {
            char brikke = board.charAt(fra);
            if (!Character.isUpperCase(brikke)) {
                continue;
            }
            //Sjekker om rokering er lovlig, legger det til i listen
            if(black) {
                if ((brikke == 'K' && (boolean) BC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligliste.add(new Move(95, 93));
                }
                if ((brikke == 'K' && (boolean) BC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligliste.add(new Move(95, 97));
                }
            } else {
                if ((brikke == 'K' && (boolean) WC.getY() && board.charAt(fra + W) == '.' && board.charAt(fra + W + W) == '.') && board.charAt(fra + W + W + W) == '.') {
                    lovligliste.add(new Move(95, 93));
                }
                if ((brikke == 'K' && (boolean) WC.getX() && board.charAt(fra + E) == '.' && board.charAt(fra + E + E) == '.')) {
                    lovligliste.add(new Move(95, 97));

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
                    lovligliste.add(new Move(fra, til));

                    //Om brikken bare kan flyttet et steg om gangen, vil dette bryte loopen etter det steget er lagt til.
                    //Brikker som kan flytte flere skritt vil fortsette å se etter ruter i samme himmelretning.
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
        return new Position(reverseCase(brettliste), -score, WC, BC, ep, kp);
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

    public Position move(Move move){
        /* Tar f. eks Tuple(85, 65) som input, IKKE Tuple(e2, e4).
        Returnerer ett nytt brett, der trekket er blitt gjort.
        Den oppdaterer også betingelser for rokade, osv.
         */
        int fra = move.getX();
        int til = move.getY();
        int newScore = score;
        char brikke = board.charAt(fra);

        /*if(!spillerstur)*/ newScore += value(move);
        //else newScore -= value(move);

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
                newScore += value(new Move(98, 96));
            }
            if(til == 92){ //Flytter tårnet om du rokerer langt.
                newboard.setCharAt(91, '.');
                newboard.setCharAt(93, 'R');
                newScore += value(new Move(91, 93));
            }
        }
        if(brikke == 'P') {
            if (til - fra == 2 * N) {
                ep = 119 - (fra + N); //Lagrer hvor det er greit å ta en passant
                TeP = 2; //Hvor mange trekk som kan gjøres før denne passenten ikke lenger er gyldig. TeP reduseres med 1 for hvert trekk.
            }
            if (til == ep) newboard.setCharAt(ep + S, '.'); //Drepper den passerte bonden i forbifarten

            if (til <= H8 && til >= A8) { //Om spilleren flytter bonden til øverste rad, skal han få velge hvilken brikke han vil promotere til.
                if (spillerstur && this == game.getCurrentBoard()) { //Sikrer at det er spillerens tur, og at dette trekket blir gjort på d
                    boolean done = false;
                    do {
                        Object result = JOptionPane.showInputDialog(gui, "Enter what you want to promote to: (q/n/r/b) ");
                        if (result.equals("q")) {
                            newboard.setCharAt(til, 'Q');
                            done = true;
                        } else if (result.equals("r")) {
                            newboard.setCharAt(til, 'R');
                            done = true;
                        } else if (result.equals("b")) {
                            newboard.setCharAt(til, 'B');
                            done = true;
                        } else if (result.equals("n")) {
                            newboard.setCharAt(til, 'N');
                            done = true;
                        }
                    } while (!done);
                } else //For botten er det ingen vits i å sjekke hva som er best, så han bare får en dronning uansett. #Stalemeta
                    newboard.setCharAt(til, 'Q');
            }
        }
        if(ep > 0){ //Om passant er lovlig, et eller annet sted
            TeP -= 1;
            if(TeP == 0) ep = 0; //Fjerner muligheten til å ta en passant, etter at 2 trekk er blitt gjort.
        }
        return new Position(newboard.toString(), newScore, WC, BC, ep, kp); //Returnerer et nytt brett, der trekket er gjort
    }

    public int value(Move move) {
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

        int deltascore = 0;
        if (brikke == '.') System.err.println("Hva faen, brikken er et punktum?");
        else deltascore = pst.get(brikke)[til] - pst.get(brikke)[fra];

        if(Character.isLowerCase(dreptbrikke)) deltascore += pst.get(Character.toUpperCase(dreptbrikke))[120-til] + pieceValue.get(Character.toUpperCase(dreptbrikke));

        if (brikke == 'P' && til <= H8 && til>= A8) deltascore += pst.get('Q')[til] - pst.get('P')[til]; //Ekstra score om du får en dronning.
        if (brikke == 'P' && til == ep) deltascore += pst.get('P')[ep+S]; //Ekstra score om du tok en brikke uten å ta på den (en passant)
        return deltascore;
    }
    public Position copy(){ return new Position(this.board, this.score, this.WC, this.BC, this.ep, this.kp); }

    public String getBoard(){ return this.board; }

    public int compareTo(Position pos) {
        Integer thisscore = this.score;
        Integer otherscore = pos.score;
        return thisscore.compareTo(otherscore);
    }
}