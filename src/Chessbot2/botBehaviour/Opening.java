package Chessbot2.botBehaviour;

/**
 * Opening
 */
public class Opening {

    String name = "";//Name of the opening
    String[] oponentMoves;
    String[] counterMoves;
    double sigma;//the win chance for white

    public Opening(String[] opMoves, String[] coMoves,double sigma) 
    {
        this.oponentMoves = opMoves;
        this.counterMoves = coMoves;
        this.sigma = sigma;
    }
    public Opening(String[] opMoves, String[] coMoves,double sigma, String name) 
    {
        this.oponentMoves = opMoves;
        this.counterMoves = coMoves;
        this.sigma = sigma;
        this.name = name;
    }
    
}