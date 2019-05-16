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
    private String email,name,password,morada,nif;
    private LocalDate birthDate;
    private List<Rent> rentList;
    /**
     * Constructor for objects of class Utilizador
     */
    public GeneralUser(String email,String name, String password, String morada, LocalDate birthDate)
    {
        // initialise instance variables
        this.email = email;
        this.name = name;
        this.password = password;
        this.morada = morada;
        this.birthDate = birthDate;
        this.rentList = new ArrayList<>();
    }

    public GeneralUser(GeneralUser generalUser) {
        this.email = generalUser.getEmail();
        this.name = generalUser.getName();
        this.password = generalUser.getPassword();
        this.morada = generalUser.getMorada();
        this.birthDate = generalUser.getBirthDate();
        this.rentList = generalUser.getRentList();
    }

    public List<Rent> getRentList() {
        return this.rentList.stream().map(Rent::clone).collect(Collectors.toList());
    }


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
    
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public abstract GeneralUser clone ( );
    
    public boolean equals(Object o) {
        if(this == o) return true;
        if((o == null) || (this.getClass() != o.getClass())) 
        return false;
        GeneralUser gU = (GeneralUser) o;
        return (this.getEmail().equals(gU.getEmail()) 
                && this.getName().equals(gU.getName())
                && this.getMorada().equals(gU.getMorada())
                && this.getBirthDate().equals(gU.getBirthDate()));
    }
        
    
    
        
}
