/**
 * Write a description of class Rent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public class Rent implements Serializable
{

    // instance variables - replace the example below with your own
    private LocalDate date;
    private Duration rentTime;
    private double price;
    private double rating;
    private Posicao pos; // posicao final
    private String nif; // pode ser de cliente ou do conduotor
    private String matricula;


    public Rent(Duration _rentTime, double _price,Posicao poss,String niff,String a){
        this.date = LocalDate.now();
        this.rentTime = _rentTime;
        this.price = _price;
        this.rating = 0;
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

    public LocalDate getDate() {
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




}




