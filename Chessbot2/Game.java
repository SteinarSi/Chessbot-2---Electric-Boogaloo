<<<<<<< HEAD:Chessbot2/Game.java
package Chessbot2;

//import javafx.geometry.Pos;

import java.util.ArrayList;

import static Chessbot2.Chess.*;

public class Game {

    static ArrayList<Position> madeMoves;
    static Position currentBoard;

    public Game(){
        madeMoves = new ArrayList<>();
        currentBoard = new Position(board, 0, WC, BC, 0, true);
        madeMoves.add(currentBoard);
    }

    public String getPosition() {
        return madeMoves.get(madeMoves.size()-1).board;
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
        }else System.out.println("Can't go further back!");
    }

    public static String getBoard() { return currentBoard.getBoard(); }

    public boolean playerMove(String command) {
        /* Forsøker å gjøre spillerens trekk.
        Tar hvilken som helst streng som input,
        og forsøker å oversette det til en tuppel før den sender det til Position og gjør trekket.
        Returnerer true/false, basert på om den klarte å gjøre noe.
        f. eks " \n  e2    e4sd jknsd bjsd\n" Blir oversatt til Tuple(85, 65), og returnerer true.
        f. eks "ijnsdfhjb e2e4 sdkjn sdfjl sd jsd  sdjnsd " er uforståelig og returnerer false uten å bli gjort.
         */
        if(IsAMove(command)) {
            Tuple<Integer, Integer> com = parse(command);
            if (currentBoard.check_player_move(com)) {
                currentBoard = currentBoard.move(com);
                madeMoves.add(currentBoard);
                black = true;
                spillerstur = false;
                paintPieces();
                currentBoard = currentBoard.rotate();
                Tuple botmove = Searcher.findMove(currentBoard);
                botMove(botmove);
                paintPieces();
                black = false;
                spillerstur = true;
                return true;
            } else System.err.println("Not a legal move!");
        } else System.err.println("Try typing a move on the format 'letter number letter number'");

        return false;
    }
    public void botMove(Tuple<Integer, Integer> command){
        currentBoard = currentBoard.move(command);
        currentBoard = currentBoard.rotate();
        madeMoves.add(currentBoard);
    }

    public void newGame(){
        madeMoves.clear();
        currentBoard = new Position(board, 0, new Tuple(true, true), new Tuple(true, true), 0, true);
        madeMoves.add(currentBoard);
        paintPieces();
    }
}
=======
package Chessbot2;

//import javafx.geometry.Pos;

import java.util.ArrayList;

import static Chessbot2.Chess.*;

public class Game {

    static ArrayList<Position> madeMoves;
    static Position currentBoard;

    public Game(){
        madeMoves = new ArrayList<>();
        currentBoard = new Position(board, 0, WC, BC, 0, true);
        madeMoves.add(currentBoard);
    }

    public String getPosition() {
        return madeMoves.get(madeMoves.size()-1).board;
    }

    public static String getBoard() { return currentBoard.getBoard(); }

    public boolean playerMove(String command) {
        /* Forsøker å gjøre spillerens trekk.
        Tar hvilken som helst streng som input,
        og forsøker å oversette det til en tuppel før den sender det til Position og gjør trekket.
        Returnerer true/false, basert på om den klarte å gjøre noe.
        f. eks " \n  e2    e4sd jknsd bjsd\n" Blir oversatt til Tuple(85, 65), og returnerer true.
        f. eks "ijnsdfhjb e2e4 sdkjn sdfjl sd jsd  sdjnsd " er uforståelig og returnerer false uten å bli gjort.
         */
        if(IsAMove(command)) {
            Tuple<Integer, Integer> com = parse(command);
            if (currentBoard.check_player_move(com)) {
                //Gjør trekk for spilleren
                currentBoard = currentBoard.move(com);
                madeMoves.add(currentBoard);
                black = true;
                spillerstur = false;
                paintPieces();

                //Gjør trekk for botten
                currentBoard = currentBoard.rotate();
                iMove<Integer, Integer> botmove = Searcher.findMove(currentBoard);
                botMove(botmove);
                paintPieces();
                black = false;
                spillerstur = true;
                return true;


            } else System.err.println("Not a legal move!");
        } else System.err.println("Try typing a move on the format 'letter number letter number'");
        return false;
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
        }else System.out.println("Can't go further back!");
    }
    public void botMove(iMove<Integer, Integer> command){
        currentBoard = currentBoard.move(command);
        currentBoard = currentBoard.rotate();
        madeMoves.add(currentBoard);
    }
    public void newGame(){
        madeMoves.clear();
        currentBoard = new Position(board, 0, new Tuple(true, true), new Tuple(true, true), 0, true);
        madeMoves.add(currentBoard);
        paintPieces();
    }
}
>>>>>>> 0955cbd284b46503cd8c3461c2dcfa12c94acf2e:Game.java
