package Chessbot2;

public class Move implements Comparable
{
    int x;
    int y;
    boolean stabilityIndex;
    int weight;

    public Move(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.weight = 0;
        this.stabilityIndex = false;
    }
    public Move(int x, int y, boolean stabIndex, int weight)
    {
        this.x = x;
        this.y = y;
        this.stabilityIndex = stabIndex;
        this.weight = weight;
    }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getWeight() {return this.weight; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void addWeight(int n) {this.weight += n; }

    public String toString() {return "(" + this.x + ", " + this.y + ")"; }

    public boolean equals(Move obj) {
        return (this.x == obj.getX() && this.y == obj.getY());
    }
    @Override
    public int compareTo(Object m) 
    {
        Move i = (Move)m;
        Integer foo = this.weight;
        Integer bar = i.weight;

        return foo.compareTo(bar);
    }

}