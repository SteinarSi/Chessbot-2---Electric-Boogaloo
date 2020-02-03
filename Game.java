package Chessbot2;

import javafx.geometry.Pos;

import java.util.ArrayList;

import static Chessbot2.Chess.*;

public class Game {

    ArrayList<Position> madeMoves;

    public Game(){
        madeMoves = new ArrayList<>();
        madeMoves.add(new Position(board, 0, WC, BC, 0, true));
    }

    public String getPosition() {
        return madeMoves.get(madeMoves.size()-1).board;
    }

    public void back() {
        madeMoves.remove(madeMoves.size()-1);
    }

    public boolean move(String command) {

        if (!IsAMove(command)) System.err.println("Ugyldig input");
        else {
            Tuple<Integer, Integer> com = parse(command);

            Position last = madeMoves.get(madeMoves.size()-1);

            if (last.gen_player_moves(com)) {
                madeMoves.add(last.move(com));
                return true;
            }
        }

        return false;
    }
}
