/**
 * Class to hold all data relevant for runtime.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Data implements  Serializable ,IData
{
    private static final long serialVersionUID = 123456789L;
    private Map<String,String> emailToNif;
    private Map<String, GeneralUser> users; // HashMap que contém todos os users, tendo o nif como chave
    private Map<String,Vehicle> allVehicles;
    private GeneralUser loggedInUser = null;
    private Map<String,Rent> pendingRent;
    private Map<String,Rent> pendingRating;
    public boolean isLoggedIn () {
        return (loggedInUser != null);
    }


    public Data() {
        users = new HashMap<>();
        allVehicles = new HashMap<>();
        emailToNif = new HashMap<>();
        pendingRent = new HashMap<>();
        pendingRating = new HashMap<>();
    }

    public Map<String,Vehicle> getAllVehicles() {
        return this.allVehicles.entrySet().stream().collect(Collectors.toMap(l-> l.getKey(),l->l.getValue().clone()));
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean loginOn (String username, String pass) {
        GeneralUser generalUser = null;
        boolean login = false;
        if(emailToNif.containsKey(username)){
            generalUser = users.get(emailToNif.get(username));
            login = (generalUser.getEmail().equals(username) && generalUser.getPassword().equals(pass));
        }
        if(login) {
            loggedInUser = generalUser;
        }
        return login;
    }

    public GeneralUser getLoggedInUser() {
        return this.loggedInUser.clone();
    }


    public void addUser (GeneralUser generalUser) {
        emailToNif.put(generalUser.getEmail(),generalUser.getNif());
        users.put(generalUser.getNif(),generalUser);
    }

    public void populateData ( ) {

    }

    public void saveState ( ) {
        try {
            FileOutputStream fos = new FileOutputStream("data.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            System.out.println("Dados Gravados");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createRent (Vehicle rentVehicle, Posicao posicao) {
        Duration duration = Duration.ZERO;
        double _price = rentVehicle.rentPrice(posicao);
        Posicao pos = posicao;
        String nif = loggedInUser.getNif();
        String matricula = rentVehicle.getMatricula();
        Rent rent = new Rent(duration,_price,pos,nif,matricula);
        pendingRent.put(matricula,rent);

    }

    public void acceptRent(Rent rent) {
        pendingRent.remove(rent.getMatricula(),rent);
        pendingRating.put(rent.getNif(),rent);
    }

    public void giveRate(Rent rent , double rating) {
        pendingRating.remove(rent.getNif(),rent);
        rent.setRating(rating);
        loggedInUser.addRentToHistory(rent.clone());
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        _rentVehicle.addRent(rent.clone());
        Owner _ownerVehicle = (Owner) users.get(_rentVehicle.getNifOwner());
        _ownerVehicle.addRentToHistory(rent.clone());
        _ownerVehicle.updateRating(rating);
    }


    public boolean addCar(Vehicle mVehicle) {
        Owner _own = (Owner) loggedInUser;
        boolean isSuccess = _own.addVehicle(mVehicle.getMatricula(),mVehicle);
        if(isSuccess)
        allVehicles.put(mVehicle.getMatricula(),mVehicle);
        return isSuccess;
    }

    public List<Vehicle> getListOfCarType(Class<? extends Vehicle> a){
        return this.allVehicles.values().stream().filter(l-> l.getClass() == a).map(Vehicle::clone).collect(Collectors.toList());
    }

}
