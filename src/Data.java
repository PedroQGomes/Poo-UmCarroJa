/**
 * Class to hold all data relevant for runtime.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.*;
import java.util.*;

public class Data implements  Serializable ,IData
{
    private static final long serialVersionUID = 123456789L;
    private Map<String, GeneralUser> users; // HashMap que contém todos os users, tendo o email como chave
    private List<Vehicle> allVehicles;
    private GeneralUser loggedInUser = null;

    public boolean isLoggedIn () {
        return (loggedInUser != null);
    }


    public Data() {
        users = new HashMap<>();
        allVehicles = new ArrayList<>();
    }

    public void logout() {
        loggedInUser = null;
    }

    public boolean loginOn (String username, String pass) {
        GeneralUser generalUser = null;
        boolean login = false;
        if(users.containsKey(username)){
            generalUser = users.get(username);
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
        String key = generalUser.getEmail();
        users.put(key,generalUser);
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

    public void addCar(Vehicle mVehicle) {
        Owner _own = (Owner) loggedInUser;
        _own.addVehicle(mVehicle);
        allVehicles.add(mVehicle);
    }



}
