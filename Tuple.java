package Chessbot2;

class Tuple<X, Y> {
    /*
    Definerer Tupler, som fra nå av er mutable. "Reality can be whatever I want" - Thanos
     */
    private X x;
    private Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public Object getX() { return this.x; }
    public Object getY() { return this.y; }

    public void setX(Object x) { this.x = (X) x; }
    public void setY(Object y) { this.y = (Y) y; }
}
