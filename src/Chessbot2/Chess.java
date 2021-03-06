package Chessbot2;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Chess/* implements ActionListener*/ {
    public final static String initialboard =
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
            "          ";  // 111 - 118
    //Himmeldirectionser
    final static int N = -10;
    final static int E = 1;
    final static int S = 10;
    final static int W = -1;
    //Hjørneindekser
    final static int A1 =  91;
    final static int H1 = 98;
    final static int A8 = 21;
    final static int H8 = 28;

    //Hvem som kan rokere, og hvor
    static final Tuple initWC = new Tuple(true, true);
    static final Tuple initBC = new Tuple(true, true);

    //Genererer dicter for brettverdier og himmelretningene til hver enkelt brikke
    public static Dictionary<Character, Integer[]> pst = Generator.makePst();
    public static Dictionary<Character, Integer[]> directions = Generator.makeDirections();
    public static ArrayList<Character> bokstaver = Generator.checkCharacter();
    public static ArrayList<Character> tall = Generator.checkInteger();
    public static Hashtable<String, BufferedImage> piecedict = Generator.Awake();
    public static HashMap<Character, String> charToString = Generator.charToString();
    public static HashMap<Character, Integer> pieceValue = Generator.makePieceValue();

    static boolean spillerstur = true;
    static int TeP = 0; //Passanttelleren
    static Character nybrikke;
    public static char botvalg = '0';

    public static Game game;
    public static String usertext;
    public static Tuple<Integer, Integer> pressedtuple = new Tuple(null, null);
    public static JTextField textField;
    public static final JPanel gui = new JPanel(new BorderLayout(3, 3));
    public static JButton[][] chessBoardSquares = new JButton[8][8];
    public static JPanel chessBoard;
    private static final String COLS = "ABCDEFGH";
    public static ArrayList<Tuple<Integer, JButton>> buttonlist = new ArrayList<>();

    public static JButton enter = new JButton("Enter");
    public static JButton back = new JButton("Go Back");
    public static JButton neww = new JButton("New Game");
    public static JButton quit = new JButton("Quit Game");

    Chess() {
        initializeGui();
    }

    public static void main(String[] args) {
        game = new Game();
        Runnable r = () -> {
            Chess cb = new Chess();
            JFrame frame = new JFrame("Chessbot2");
            frame.add(cb.getGui());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationByPlatform(true);
            frame.pack();
            frame.setMinimumSize(frame.getSize());
            paintPieces();
            frame.setVisible(true);
            Game.chooseBot();

        };
        SwingUtilities.invokeLater(r);
    }
    public static Tuple<Integer, Integer> indexToList(int index){
        index -= 20;
        Integer y = index/10;
        Integer x = index%10;
        return new Tuple(x, y);
    }
    public static int listToIndex(int ii, int jj){
        int ret = 20;
        ret += jj*10;
        ret += ii;
        return ret;
    }
    public static void paintPieces(){
        /* En funksjon for å oppdatere alt det visuelle på brettet. Denne må kalles hver gang noen har flyttet en brikke.
        Husk at denne tar utgangspunkt i at Hvit har brikker med store bokstaver, og Svart har små.
        Om du bruker rotate() en gang for mye eller en gang for lite, får alle brikkene invertert farge og posisjon.
         */
        String currentBoard = game.getBoard();
        for(int i=20; i<initialboard.length()-20; i++){
            Tuple<Integer, Integer> indekser = indexToList(i);
            Integer X = indekser.getX();
            Integer Y = indekser.getY();
            if(X == 0 || X == 9) continue;
            ImageIcon icon = new ImageIcon(new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB));

            //Gir ruten et bilde av en brikke, om det står en brikke oppå den. Om ikke blir bildet bare blankt.
            if(charToString.containsKey(currentBoard.charAt(i))) {
                icon.setImage(piecedict.get(charToString.get(currentBoard.charAt(i))));
            }
            chessBoardSquares[X-1][Y].setIcon(icon);
        }
    }
    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar toolbar2 = new JToolBar();
        toolbar2.add(quit);
        toolbar2.add(neww);
        toolbar2.add(back);
        gui.add(toolbar2, BorderLayout.PAGE_START);
        JToolBar toolbar = new JToolBar();
        gui.add(toolbar, BorderLayout.PAGE_END);
        JTextField text = new JTextField(20);
        textField = text;
        toolbar.add(text);
        enter.addActionListener(new Action());
        quit.addActionListener(new Action());
        back.addActionListener(new Action());
        textField.addKeyListener(new Action());
        neww.addActionListener(new Action());
        toolbar.add(enter);
        gui.add(new JLabel("?"), BorderLayout.LINE_START);
        chessBoard = new JPanel(new GridLayout(0, 8));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        gui.add(chessBoard);

        // create the chess board squares
        Insets buttonMargin = new Insets(0,0,0,0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {

                JButton b = new JButton();
                b.setMargin(buttonMargin);
                ImageIcon icon = new ImageIcon(new BufferedImage(84, 84, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) b.setBackground(Color.LIGHT_GRAY);
                else b.setBackground(Color.gray);
                b.addActionListener(new Action());
                buttonlist.add(new Tuple(listToIndex(jj+1, ii), b));
                chessBoardSquares[jj][ii] = b;
            }
        }
        //fill the chess board
        //chessBoard.add(new JLabel(""));
        //fill the top row
        //for (int ii = 0; ii < 8; ii++) {
          //  chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER));
        //}
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                //switch (jj) {
                  //  case 0:
                    //    chessBoard.add(new JLabel("" /*+ (8 - ii)*/, SwingConstants.CENTER));
                    //default:
                chessBoard.add(chessBoardSquares[jj][ii]);
                //}
            }
        }
    }
    public final JComponent getGui() { return gui; }
}