package Chessbot2;

//import javafx.geometry.Pos;
import javax.swing.*;

import static Chessbot2.Search.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static Chessbot2.Chess.*;

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
        currentBoard = new Position(board, 0, WC, BC, 0, false, false);
        madeMoves.add(currentBoard);
    }

    public static String getBoard() { return currentBoard.getBoard(); }

    public void playerMove(Move move) {
        /* Forsøker å gjøre spillerens trekk.
        Tar hvilken som helst streng som input,
        og forsøker å oversette det til en tuppel før den sender det til Position og gjør trekket.
        Returnerer true/false, basert på om den klarte å gjøre noe.
        f. eks " \n  e2    e4sd jknsd bjsd\n" Blir oversatt til Tuple(85, 65), og returnerer true.
        f. eks "ijnsdfhjb e2e4 sdkjn sdfjl sd jsd  sdjnsd " er uforståelig og returnerer false uten å bli gjort.
         */
            currentBoard = currentBoard.move(move);
            paintPieces();
            madeMoves.add(currentBoard);
            spillerstur = false;
            botMove();
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
    public void botMove(){
        Move botmove;
        try {
            //Velger hvilken bot som skal brukes
            currentBoard = currentBoard.rotate();
            if(findOkMove) botmove = Searcher.findOkMove(currentBoard);
            else if(CalculateBestMove) botmove = Search.CalulateBestMove(currentBoard);
            else if(findRecursiveMove)botmove = Searcher.findRecursiveMove(currentBoard);
            else if(findFilthyMove) botmove = Searcher.findFilthyMove(currentBoard, 5);
            else botmove = Searcher.findRandomMove(currentBoard);

            currentBoard = currentBoard.move(botmove);
            currentBoard = currentBoard.rotate();
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
        currentBoard = new Position(board, 0, new Tuple(true, true), new Tuple(true, true), 0, true, false);
        madeMoves.add(currentBoard);
        paintPieces();
        chooseBot();
    }
    public static Position getCurrentBoard() { return currentBoard; }
}
