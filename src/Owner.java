import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
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
    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String nif)
    {
        super(_email,_name,_password,_morada,_birthDate,nif);
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
    public void addVehicle(Vehicle a){
        this.arrListCar.add(a.clone());
    }
    public List<Vehicle> signalAvailable(Rent a){
        return this.arrListCar.stream().filter(l -> l.enoughAutonomy(a.getPosicao())).collect(Collectors.toList());
    }

    public void fuelCar(Vehicle a){
        Iterator<Vehicle> it = this.arrListCar.iterator();
        boolean flag = true;
        while(flag && it.hasNext()){
            Vehicle b = it.next();
            if(b.equals(a) == true){
                flag = false;
                b.abastece();
            }
        }
    }

    public void changePrice(Vehicle a,double price){
        Iterator<Vehicle> it = this.arrListCar.iterator();
        boolean flag = true;
        while(flag && it.hasNext()){
            Vehicle b = it.next();
            if(a.equals(b) == true){
                flag = false;
                b.setPrice(price);
            }
        }
    }





}
