package Chessbot2;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class Chess implements ActionListener {
    static String board = 
        "         \n"+ // 1 - 8
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
        "          ";   // 111 - 118
    //Himmeldirectionser
    static int N = -10;
    static int E = 1;
    static int S = 10;
    static int W = -1;
    //Hjørneindekser
    static int A1 =  91;
    static int H1 = 98;
    static int A8 = 21;
    static int H8 = 28;
    //Hvem som kan rokere, og hvor
    static Tuple WC = new Tuple(true, true);
    static Tuple BC = new Tuple(true, true);
    //Genererer dicter for brettverdier og himmelretningene til hver enkelt brikke
    public static Dictionary<Character, Integer[]> pst = Generator.makePst();
    public static Dictionary<Character, Integer[]> directions = Generator.makeDirections();
    public static ArrayList<Character> bokstaver = Generator.checkCharacter();
    public static ArrayList<Character> tall = Generator.checkInteger();

    static boolean black = false;
    static boolean play = true;
    static boolean lovlig = false;
    static boolean gjorttrekk = false;
    static boolean spillerstur = true;
    static int TeP = 0; //Passanttelleren

    public static Game game;
    public static String usertext;
    private JTextField textField;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessBoard;
    private static final String COLS = "ABCDEFGH";

    Chess() {
        initializeGui();
    }
    public static void main(String[] args) {
        // TODO: 03.02.2020 Skriv hele main på nytt 
        game = new Game();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Chess cb = new Chess();

                JFrame f = new JFrame("Chessbot2");
                f.add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);

    }

    public final void initializeGui() {
        // TODO: 03.02.2020 Finn ut hvordan vise brikker på brettet 
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));

        JToolBar toolbar2 = new JToolBar();
        JButton quit = new JButton("Quit Game"); // TODO: 03.02.2020 Implementer disse knappene
        toolbar2.add(quit);
        JButton neww = new JButton("New Game");
        toolbar2.add(neww);
        JButton back = new JButton("Go Back");
        toolbar2.add(back);
        gui.add(toolbar2, BorderLayout.PAGE_START);

        JToolBar toolbar = new JToolBar();
        gui.add(toolbar, BorderLayout.PAGE_END);

        JTextField text = new JTextField(20);
        textField = text;
        toolbar.add(text);

        JButton end = new JButton("Enter");
        end.addActionListener(this);
        toolbar.add(end);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {

                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.LIGHT_GRAY);
                } else {
                    b.setBackground(Color.DARK_GRAY);
                }
                chessBoardSquares[jj][ii] = b;
            }
        }

        //fill the chess board
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                            SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (8 - ii),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
        }
    }
    public final JComponent getChessBoard() {
        return chessBoard;
    }

    public final JComponent getGui() {
        return gui;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("knapp trykket!");
        usertext = textField.getText();
        textField.setText("");
        System.out.println("Du skrev " + usertext);
    }

    public static Tuple<Integer, Integer> parse(String c){
        /* Konverterer posisjonen på brettet fra bokstav+tall+bokstav+tall til kun en tuppel av tall.
        f. eks. "e2 e4" blir Tuple(85, 65).
        Planen er at kun Chess skal få se syntakser som e2e4,
        og at den skal konvertere alt slikt til Tupler og tall før den sender det til Position og Searcher.
         */
        int filfra = (int) c.charAt(0) - 'a';
        int rankfra = c.charAt(1) - '1';
        int x = A1 + filfra - 10*rankfra; //Koordinat fra

        int filftil = (int) c.charAt(2) - 'a';
        int ranktil = c.charAt(3) - '1';
        int y = A1 + filftil - 10*ranktil; //Koordinat til

        return new Tuple<>(x, y);
    }
    public static boolean IsAMove(String input){
        /* En funskjon for å sjekke om spilleren skrev noe som kan tolkes som et trekk eller ikke.
        "e2e4" returnerer true, "sdlfnsjgbskjøfbnskjfba" returner false.
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
}

/*
Gamle main


        Scanner scanner = new Scanner(System.in);
        Position P = new Position(board, 0, WC, BC, 0, true);
        System.out.println(board);

        outer: while(play) {
            lovlig = false;
            gjorttrekk = false;

            while (!gjorttrekk) {
                System.out.println("Ditt trekk: ");
                String input = scanner.nextLine();
                input = input.replaceAll("\\s", "");
                if (input.equals("quit")){
                    scanner.close();
                    break outer;
                }
                if (IsAMove(input)){
                    Tuple<Integer, Integer> trekk = parse(input);
                    if (P.gen_player_moves(trekk)) {
                        P = P.move(trekk);
                        gjorttrekk = true;
                    } else System.err.println("Ulovlig trekk. Prøv igjen.");
                } else System.err.println("Forstår ikke. Prøv bokstaver fra a-h og tall fra 1-8, i rekkefølgen bokstav tall bokstav tall.");
            }
            P = P.rotate();
            System.out.println(P.board);
        }

      */
