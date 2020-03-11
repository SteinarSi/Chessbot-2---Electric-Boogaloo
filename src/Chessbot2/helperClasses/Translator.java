package Chessbot2.helperClasses;

/**
 * numberToMove
 */
public class Translator {

    private static char[] rows = {'a','b','c','c','e','f','g','h'};

    public static String ConvertToString(int i) 
    {
        String s = Integer.toString(i);
        return Character.toString(rows[Integer.parseInt(s.substring(0, 1)) - 1]) + s.substring(1, 2);
    }

    public static int ConvertToNumber(String i) 
    {

    }
    
    public static int ConvertFromAlgebraicNotation(String i)
    {
        char[] j = i.toCharArray();
        if(j.length == 2)
            return ConvertToNumber(i);
        else
        {
            
        }
    } 
}