import java.io.Serializable;

/**
 * Write a description of class Posicao here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Posicao implements Serializable
{
    private static final long serialVersionUID = 1334567219L;
    // instance variables
    private double x,y;

    /**
     * Constructor for objects of class Posicao
     */
    public Posicao()
    {
        // initialise instance variables
        this.x = 0.0;
        this.y = 0.0;
    }
    
    public Posicao(double x,double y) {
        this.x = x;
        this.y = y;
    }
    
    public Posicao(Posicao pos) {
        this.x = pos.getPosX();
        this.y = pos.getPosY();
    }


    public double getPosX() {
        return this.x;
    }
    
    public double getPosY() {
        return this.y;
    }
    
    public void setPosX(int x){
        this.x = x;
    }
    
    public void setPosY(int y) {
        this.y = y;
    }
    
    public Posicao clone() {
        return new Posicao(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || this.getClass() != o.getClass()) return false;
        Posicao p = (Posicao) o;
        return (p.getPosX() == this.getPosX() && p.getPosY() == this.getPosY());
    }


    public int hashCode(){
        int hash = 5;
        long aux1,aux2;
        aux1 = Double.doubleToLongBits(this.x);
        hash = 31*hash + (int)(aux1 ^ (aux1 >>> 32));
        aux2 = Double.doubleToLongBits(this.y);
        hash = 31*hash + (int)(aux2 ^ (aux2 >>> 32));
        return hash;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(this.getPosX());
        stringBuilder.append(",");
        stringBuilder.append(this.getPosY());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
    public double distancia(Posicao x){
        double x1 = this.x;
        double y1 = this.y;
        double x2 = x.getPosX();
        double y2 = x.getPosY();
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
