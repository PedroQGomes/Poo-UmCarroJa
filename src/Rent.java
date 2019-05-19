/**
 * Write a description of class Rent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rent implements Serializable
{

    // instance variables - replace the example below with your own
    private LocalDateTime date;
    private Duration rentTime;
    private double price;
    private double rating;
    private Posicao pos; // posicao final
    private String nif;
    private String matricula;


    public Rent(Duration _rentTime, double _price,Posicao poss,String niff,String a){
        this.date = LocalDateTime.now();
        this.rentTime = _rentTime;
        this.price = _price;
        this.rating = -1;
        this.pos = poss;
        this.nif = niff;
        this.matricula = a;
    }
    public Rent(Rent r) {
        this.rating = r.getRating();
        this.date = r.getDate();
        this.rentTime = r.getRentTime();
        this.price = r.getPrice();
        this.nif = r.getNif();
        this.pos = r.getPosicao().clone();
        this.matricula= r.getMatricula();
    }

    public double getRating(){ return this.rating;}

    public LocalDateTime getDate() {
        return this.date;
    }

    public Duration getRentTime() {
        return this.rentTime;
    }

    public double getPrice() {
        return this.price;
    }

    public Posicao getPosicao() { return this.pos.clone();}

    public String getNif() { return this.nif;}

    public String getMatricula(){return this.matricula;}

    public void setRating(double a){this.rating = a;}

    public boolean equals(Object o){
        if(this == o)return true;
        if((o == null) || this.getClass() != o.getClass())return false;
        Rent a = (Rent) o;
        return(a.getNif() == this.getNif() && this.getMatricula() == a.getMatricula() && a.getDate() == this.getDate() && a.getRating() == this.getRating() && this.getPosicao() == a.getPosicao());
    }

    public Rent clone(){
        return new Rent(this);
    }

    public int hashCode(){
        int hash = 5;
        long aux1,aux2;
        aux1 = Double.doubleToLongBits(this.price);
        hash = 31*hash + (int)(aux1 ^ (aux1 >>> 32));
        aux2 = Double.doubleToLongBits(this.rating);
        hash = 31*hash + (int)(aux2 ^ (aux2 >>> 32));
        hash = 31*hash + this.matricula.hashCode();
        hash = 31*hash + this.nif.hashCode();
        hash = 31*hash + this.pos.hashCode();
        hash = 31*hash + this.date.hashCode();
        hash = 31*hash + this.rentTime.hashCode();
        
        return hash;
    }



}




