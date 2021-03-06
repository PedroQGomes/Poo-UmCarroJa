/**
 * Write a description of class Utilizador here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public abstract class GeneralUser implements Serializable
{
    // instance variables - replace the example below with your own
    private static final long serialVersionUID = 1234567219L;
    private String email,name,password,morada,nif;
    private LocalDate birthDate;
    private double rating;
    private int nRate;
    private List<Rent> rentList;
    /**
     * Constructor for objects of class Utilizador
     */
    public GeneralUser(String email,String name, String password, String morada, LocalDate birthDate,String nif)
    {
        // initialise instance variables
        this.nif = nif;
        this.email = email;
        this.name = name;
        this.password = password;
        this.morada = morada;
        this.birthDate = birthDate;
        this.nif = nif;
        this.rating = 0.0;
        this.nRate = 0;
        this.rentList = new ArrayList<>();
    }

    public GeneralUser(GeneralUser generalUser) {
        this.email = generalUser.getEmail();
        this.name = generalUser.getName();
        this.password = generalUser.getPassword();
        this.morada = generalUser.getMorada();
        this.birthDate = generalUser.getBirthDate();
        this.nif = generalUser.getNif();
        this.rating = generalUser.getRating();
        this.nRate = generalUser.getNRate();
        this.rentList = generalUser.getRentList();
    }

    public int getNRate() { return  this.nRate;}

    public List<Rent> getRentList() {
        return this.rentList.stream().map(Rent::clone).collect(Collectors.toList());
    }

    public String getNif(){return this.nif;}

    public String getEmail() {
        return this.email;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getMorada() {
        return this.morada;
    }

    public double getRating() {return this.rating;}


    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public abstract GeneralUser clone ( );
    
    public boolean equals(Object o) {
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) 
        return false;
        GeneralUser gU = (GeneralUser) o;
        return ( this.getNif().equals(gU.getNif())
                && this.getEmail().equals(gU.getEmail())
                && this.getName().equals(gU.getName())
                && this.getMorada().equals(gU.getMorada())
                && this.getBirthDate().equals(gU.getBirthDate()));
    }


    public void addRentToHistory(Rent rent){
        this.rentList.add(rent.clone());
    }


    public void updateRating(double rate) {
        this.nRate++;
        this.rating = calculateRating(rate);
    }

    private double calculateRating(double rate) {
        double tmp = 0.0;
        tmp = this.rating * (this.nRate-1);
        tmp += rate;
        return tmp/this.nRate;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer("General User");
        sb.append("Nome: ").append(this.name).append(", ");
        sb.append("Nif: ").append(this.nif).append(", ");
        sb.append("Email: ").append(this.email).append(", ");
        sb.append("Rating: ").append(this.rating).append(", ");
        return sb.toString();
    }
}
