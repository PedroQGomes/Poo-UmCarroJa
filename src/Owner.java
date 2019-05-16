import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
/**
 * Write a description of class Owner here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

public class Owner extends GeneralUser
{
    // instance variables
    private int rating;
    private List<Vehicle> arrListCar;
    /**
     * Constructor for objects of class Client
     */
    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate)
    {
        super(_email,_name,_password,_morada,_birthDate);
        this.rating = 0;
        arrListCar = new ArrayList<>();
    }

    public Owner(Owner own) {
        super(own);
        this.rating = own.getRating();
        this.arrListCar = own.getListOfCars();
    }
    public List<Vehicle> getListOfCars() {
        return this.arrListCar.stream().map(Vehicle::clone).collect(Collectors.toList());
    }
    public int getRating() { return this.rating;}

    public Owner clone() {
        return new Owner(this);
    }

    private void updateRating(int mRating) { // ASSUMINDO QUE ISTO É CHAMADO ANTES DE ADICIONAR O RENT A LISTA
        int tmp = rating * arrListCar.size();
        tmp += mRating;
        tmp /= arrListCar.size()+1;
        this.rating = tmp;
    }
}
