package Chessbot2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class ImageHolder {

    static Hashtable<String, BufferedImage> imageTable;

    public static void Awake(){
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
                imageTable.put(name, ImageIO.read(new File("Sjakkbrikker\\" + name + ".jpg")));
            } catch (IOException e) {
                System.out.println("finner ikke brikke \"" + name + ".jpg\"");
            }
        }
    }

    public static BufferedImage get(String key){
        BufferedImage ret = imageTable.get(key);
        if (ret == null) return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        else return ret;
    }
}
