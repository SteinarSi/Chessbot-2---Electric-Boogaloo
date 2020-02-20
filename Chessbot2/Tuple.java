<<<<<<< HEAD:Chessbot2/Tuple.java
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
    public X getX() { return this.x; }
    public Y getY() { return this.y; }

    public void setX(Object x) { this.x = (X) x; }
    public void setY(Object y) { this.y = (Y) y; }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
    public boolean equals(Tuple obj){
        return (this.x == obj.getX() && this.y == obj.getY());
    }
}


=======
package Chessbot2;

class Tuple<X, Y> implements iMove<X, Y>{
    /*
    Definerer Tupler, som fra nå av er mutable. "Reality can be whatever I want" - Thanos
     */
    private X x;
    private Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public X getX() { return this.x; }
    public Y getY() { return this.y; }

    public void setX(Object x) { this.x = (X) x; }
    public void setY(Object y) { this.y = (Y) y; }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }
    public boolean equals(iMove obj){ return (this.x == obj.getX() && this.y == obj.getY()); }
}
>>>>>>> 0955cbd284b46503cd8c3461c2dcfa12c94acf2e:Tuple.java
