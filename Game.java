package Chessbot2;

//import javafx.geometry.Pos;

import java.util.ArrayList;

import static Chessbot2.Chess.*;

public class Game {

    ArrayList<Position> madeMoves;
    Position currentBoard;

    public Game(){
        madeMoves = new ArrayList<>();
        Position currentBoard = new Position(board, 0, WC, BC, 0, true);
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
            currentBoard = madeMoves.get(-1);
        }else System.out.println("Can't go further back!");
    }

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
                currentBoard = currentBoard.rotate();
                madeMoves.add(currentBoard);
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

}
