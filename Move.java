package Chessbot2;

public class Move<X, Y> implements iMove<X, Y>
{
    X x;
    Y y;
    int stabilityIndex;
    int weight;

    public Move(X x, Y y)
    {
        this.x = x;
        this.y = y;
    }
    public Move(X x, Y y, int stabIndex, int weight)
    {
        this.x = x;
        this.y = y;
        this.stabilityIndex = stabIndex;
        this.weight = weight;
    }
    public X getX() { return this.x; }
    public Y getY() { return this.y; }

    public void setX(X x) { this.x = x; }
    public void setY(Y y) { this.y = y; }

    public String toString() {return "(" + this.x + ", " + this.y + ")"; }

    public boolean equals(Move obj){ return (this.x == obj.getX() && this.y == obj.getY()); }
}