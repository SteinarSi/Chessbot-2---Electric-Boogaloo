package Chessbot2;

import static Chessbot2.Searcher.findOkMove;

public class SearcherTest {
    public static void main(String[] args) {
        String board = "         \n"+ // 1 - 8
                "         \n"+ // 11 - 18
                " rnbqkbnr\n"+ // 21 - 28
                " pppppppp\n"+ // 31 - 38
                " ........\n"+ // 41 - 48
                " ........\n"+ // 51 - 58
                " ........\n"+ // 61 - 68
                " ........\n"+ // 71 - 78
                " PPPPPPPP\n"+ // 81 - 88
                " RNBQKBNR\n"+ // 91 - 98
                "         \n"+ // 101 - 108
                "          ";  // 111 - 118
        findOkMove(new Position(board, 0, new Tuple(true, true), new Tuple(true, true), 0, true ));
    }

}
