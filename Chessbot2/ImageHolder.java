package Chessbot2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

public class ImageHolder {

    static Hashtable<String, BufferedImage> imageTable;

    public static Hashtable<String, BufferedImage> Awake(){
        /* Oppretter en dict på der nøkkelen er navnet på brikken, og innholdet er et BufferedImage.
         */
        Hashtable<String, BufferedImage> imageTable = new Hashtable<>();
        for (String name : new String[] {
                "bishop_black",
                "bishop_white",
                "horse_black",
                "horse_hite",
                "king_black",
                "king_white",
                "pawn_black",
                "pawn_white",
                "queen_black",
                "queen_white",
                "rook_black",
                "rook_white"
        }) {
            try {
                File temp = new File("Chessbot 2 - Electric Boogaloo\\src\\Chessbot2\\Sjakkbrikker\\" + name + ".png");
                BufferedImage img = ImageIO.read(temp);
                imageTable.put(name, img);
            } catch (IOException e) {
                System.out.println("Cannot find \"" + name + ".png\"");
            }
        }
        return imageTable;
    }

    public static BufferedImage get(String key){
        BufferedImage ret = imageTable.get(key);
        if (ret == null) return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        else return ret;
    }
}
