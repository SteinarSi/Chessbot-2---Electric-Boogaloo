public class Move implements Comparable<Move>
{
    int x;
    int y;
    boolean stabilityIndex;
    int weight;

    public Move(int x, int y)
    {
        this.x = x;
        this.y = y;
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

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean equals(Move obj) {
        return (this.x == obj.getX() && this.y == obj.getY());
    }

    public int compareTo(Move move) 
    {
        Integer foo = this.weight;
        Integer bar = move.weight;
        return foo.compareTo(bar);
    }
}