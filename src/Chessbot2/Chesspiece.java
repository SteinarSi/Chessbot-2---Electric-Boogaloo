package Chessbot2;

/**
 * Chesspiece
 */
public class Chesspiece 
{
    char Symbol;
    boolean isWhite;
    int[] legalMovementDirs;
    int[] legalAttackDirs;

    public Chesspiece(char symbol, boolean isWhite) 
    {
        this.Symbol = symbol;
        this.isWhite = isWhite;
        GenerateDirs();
    }

    private void GenerateDirs() 
    {
        switch (Symbol) {
            case "K":
                
                break;
            case "Q":
                break;
            case "R":
                break;
            case "B":
                break;
            case "N":
                break;
            case "P":
                break;
            default:
                break;
        }
    }
    
}