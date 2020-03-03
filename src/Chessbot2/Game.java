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
        currentBoard = new Position(board, 0, WC, BC, 0, true);
        madeMoves.add(currentBoard);
    }

    public String getPosition() {
        return madeMoves.get(madeMoves.size()-1).board;
    }

    public static String getBoard() { return currentBoard.getBoard(); }

    public void playerMove(String command) {
        /* Forsøker å gjøre spillerens trekk.
        Tar hvilken som helst streng som input,
        og forsøker å oversette det til en tuppel før den sender det til Position og gjør trekket.
        Returnerer true/false, basert på om den klarte å gjøre noe.
        f. eks " \n  e2    e4sd jknsd bjsd\n" Blir oversatt til Tuple(85, 65), og returnerer true.
        f. eks "ijnsdfhjb e2e4 sdkjn sdfjl sd jsd  sdjnsd " er uforståelig og returnerer false uten å bli gjort.
         */
        if(IsAMove(command)) {
            Move com = parse(command);
            if (currentBoard.check_player_move(com)) {
                //Gjør trekk for spilleren
                currentBoard = currentBoard.move(com);
                paintPieces();
                madeMoves.add(currentBoard);
                black = true;
                spillerstur = false;
                botMove();

            } else System.err.println("Not a legal move!");
        } else System.err.println("Try typing a move on the format 'letter number letter number'");
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
            black = false;
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

    public void newGame(){
        madeMoves.clear();
        currentBoard = new Position(board, 0, new Tuple(true, true), new Tuple(true, true), 0, true);
        madeMoves.add(currentBoard);
        paintPieces();
        chooseBot();
    }
    public static Position getCurrentBoard() { return currentBoard; }
}
