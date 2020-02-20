package Chessbot2;

public class Move
{
    int x;
    int y;
    int stabilityIndex;
    int weight;

    public Move(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Move(int x, int y, int stabIndex, int weight)
    {
        this.x = x;
        this.y = y;
        this.stabilityIndex = stabIndex;
        this.weight = weight;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
    public boolean equals(Move obj){
        return (this.x == obj.getX() && this.y == obj.getY());
    }
}