package Chessbot2;

//import javafx.geometry.Pos;
import javax.swing.*;

import static  Chessbot2.Chess.*;

import java.util.ArrayList;

public class Game {

    static ArrayList<Position> madeMoves;
    static Position currentBoard;

    //Valg av bot som skal brukes, om alle er false brukes findRandomMove
    static boolean findRecursiveMove = false;
    static boolean findOkMove = false;
    static boolean CalculateBestMove = false;
    static boolean findFilthyMove = false;

    public Game(){
        madeMoves = new ArrayList<>();
        currentBoard = new Position(initialboard, 0, initWC, initBC, 0, false, false);
        madeMoves.add(currentBoard);
    }

    public static String getBoard() { return currentBoard.getBoard(); }

    public void playerMove(Move move) {
        /* Forsøker å gjøre spillerens trekk.
        Deretter sjekker den om det er sjakk matt eller noe sånt, før den kaller på botmove til å svare.
         */
            currentBoard = currentBoard.move(move);
            paintPieces();
            madeMoves.add(currentBoard);
            spillerstur = false;
            currentBoard = currentBoard.rotate();
            ArrayList<Move> botmoves = currentBoard.gen_moves();
            Tuple<Boolean, ArrayList<Move>> check = checkCheckMate(botmoves);
            if(check.getX() == null) System.out.println("Patt!"); 
            else if(check.getX() == true) System.out.println("You win!");
            else {
                botmoves = check.getY();
                botMove(botmoves);
            }
    }

    public void back() {
        /*Sletter de to siste brettene. Hvis spilleren gjør en tabbe,
        og botten utnytter det, er det meningsløst å kun slette de siste brettet,
        da bare gjør botten det samme en gang til. Derfor sletter vi to brett.
        */
        if(madeMoves.size() >= 2) {
            madeMoves.remove(madeMoves.size() - 1);
            madeMoves.remove(madeMoves.size() - 1);
            currentBoard = madeMoves.get(madeMoves.size()-1);
            paintPieces();
        }else System.err.println("Can't go further back!");
    }
    public void botMove(ArrayList<Move> botmoves){
        Move botmove;
        try {
            //Velger hvilken bot som skal brukes
            //currentBoard = currentBoard.rotate();
            if(findOkMove) botmove = Searcher.findOkMove(currentBoard);
            else if(CalculateBestMove) botmove = Search.CalulateBestMove(currentBoard, botmoves);
            else if(findRecursiveMove)botmove = Searcher.findRecursiveMove(currentBoard);
            else if(findFilthyMove) botmove = Searcher.findFilthyMove(currentBoard, 5);
            else botmove = Searcher.findRandomMove(currentBoard);

            //Gjør trekket, og sjekker om det er matt eller noe sånt.
            currentBoard = currentBoard.move(botmove);
            currentBoard = currentBoard.rotate();
            ArrayList<Move> playermoves = currentBoard.gen_moves();
            Tuple<Boolean, ArrayList<Move>> check = checkCheckMate(playermoves);
            if(check.getX() == null) System.out.println("Patt");
            else if(check.getX()) System.out.println("You lose!");
            madeMoves.add(currentBoard);
            paintPieces();
            spillerstur = true;

        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
            System.err.println("Botten fucket opp!");
            currentBoard = currentBoard.rotate();
            paintPieces();
        }
    }
    public Tuple<Boolean, ArrayList<Move>> checkCheckMate(ArrayList<Move> moves) {
        /* Sjekker om brettet er sjakk matt. Returnerer true om det er matt, null om det er patt/uavgjort, og false ellers.
        I tillegg endrer den på listen over trekk den har som input, og returnerer kun listne over helt lovlige trekk.
         */
        ArrayList<Move> retmoves = new ArrayList<>();
        for (Move move : moves) {
            if (currentBoard.checkCheck(move)) {
                retmoves.add(move);
            }
        }
        if (retmoves.size() > 0)
            return new Tuple(false, retmoves); //Om spilleren har mer enn 0 lovlige trekk, er det ikke matt, og spillet kan fortsette.
        else {
            Position copy = currentBoard.copy();
            copy = copy.rotate();
            int King = -1;
            ArrayList<Move> botmoves = copy.gen_moves();
            for (int i = 0; i < copy.getBoard().length(); i++) { //Finner hvor spillerens konge er.
                if (copy.getBoard().charAt(i) == 'k') King = i;
            }
            if (King == -1) return new Tuple(true, retmoves);
            for (int i = 0; i < botmoves.size(); i++) {
                Move mellom = botmoves.get(i);
                if (mellom.getY() == King) { //Om noen av motstanderens trekk ender med å ta kongen er det matt, og spillet avsluttes.
                    return new Tuple(true, retmoves);
                }
            }
            return new Tuple(null, retmoves); //Om det ikke er noen lovlige trekk tilgjengelig, og motstanderen likevel ikke truer kongen, blir det patt (/uavgjort).
        }
    }

    public static void chooseBot(){
        findRecursiveMove = false;
        findOkMove = false;
        CalculateBestMove = false;
        findFilthyMove = false;

        Object result = JOptionPane.showInputDialog(gui, "Select bot:\n1: FindOkMove\n 2: CalculateBestMove\n 3: FindRecursiveMove\n 4: findFilthyMove");
        botvalg = result.toString().charAt(0);
        if(botvalg == '1') {
            findOkMove = true;
            System.out.println("Bot selected: findOkMove");
        }
        else if(botvalg == '2') {
            CalculateBestMove = true;
            System.out.println("Bot selected: CalculateBestMove");
        }
        else if(botvalg == '3') {
            findRecursiveMove = true;
            System.out.println("Bot selected: findRecursiveMove");
        }
        else if(botvalg == '4'){
            findFilthyMove = true;
            System.out.println("Bot selected: findFilthyMove");
        }
        else{
            System.out.println("No bot selected. Will perform random moves instead.");
        }
    }
    public static Move parse(String c){
        /*Den andre funksjonen for å sjekke lovligheten til spillerens trekk.
        Denne oversetter spillerens streng til et trekk som Position og Game kan forstå.
        IsAMove -> parse -> check_player_move

        f. eks. "e2 e4" blir Tuple(85, 65).
        Planen er at kun Chess skal få se syntakser som e2e4,
        og at den skal konvertere alt slikt til Tupler og tall før den sender det til Position, Game, og Searcher.
         */
        c = c.replaceAll(" ", "");
        int filfra = (int) c.charAt(0) - 'a';
        int rankfra = c.charAt(1) - '1';
        int x = A1 + filfra - 10*rankfra; //Koordinat fra

        int filtil = (int) c.charAt(2) - 'a';
        int ranktil = c.charAt(3) - '1';
        int y = A1 + filtil - 10*ranktil; //Koordinat til

        return new Move(x, y);
    }
    public static boolean IsAMove(String input){
        /* Den første funksjonen for å sjekke lovligheten til spillerens trekk.
        Denne sjekker om spilleren skrev noe som kan tolkes som et trekk eller ikke.
        IsAMove -> parse -> check_player_move

        "e2 e4 asdsg w3" returnerer true, "sdlfnsjgbskjøfbnskjfba" returner false.
          */
        input = input.replaceAll(" ", "");
        if (input.length() >= 4) {
            Character første = input.charAt(0);
            Character andre = input.charAt(1);
            Character tredje = input.charAt(2);
            Character fjerde = input.charAt(3);
            return(bokstaver.contains(første) && tall.contains(andre) && bokstaver.contains(tredje) && tall.contains(fjerde));
        } else return false;
    }

    public void newGame(){
        madeMoves.clear();
        currentBoard = new Position(initialboard, 0, initWC, initBC , 0, true, false);
        madeMoves.add(currentBoard);
        paintPieces();
        chooseBot();
    }
    public static Position getCurrentBoard() { return currentBoard; }
}
