/**
 * Write a description of class Rent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Duration;

public class Rent implements Serializable
{

    // instance variables - replace the example below with your own
    private LocalDateTime date;
    private Duration rentTime;
    private double price;
    private Posicao pos;
    private String nif;

    public LocalDateTime getDate ( ) {
        return this.date;
    }

    public Duration getRentTime ( ) {
        return this.rentTime;
    }

    public double getPrice ( ) {
        return this.price;
    }

    public Posicao getPosicao() { return this.pos.clone();}
    public String getNif() { return this.nif;}

    /**
     * Constructor for objects of class Rent
     */
    public Rent(Duration _rentTime, double _price, Vehicle _vehicle){
        this.date = LocalDateTime.now();
        this.rentTime = _rentTime;
        this.price = _price;
    }
    public Rent(Rent r) {
        this.date = getDate();
        this.rentTime = getRentTime();
        this.price = getPrice();
        this.nif = getNif();
        this.pos = r.getPosicao().clone();
    }

    public Rent clone(){
        return new Rent(this);
    }



}




