/**
 * Class to hold all data relevant for runtime.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Data implements  Serializable ,IData
{
    private static final long serialVersionUID = 123456789L;
    private Map<String,String> emailToNif;
    private Map<String, GeneralUser> users; // HashMap que contém todos os users, tendo o nif como chave
    private Map<String,Vehicle> allVehicles;
    private GeneralUser loggedInUser = null;
    private Map<String,Rent> pendingRent;
    private Map<String,List<Rent>> pendingRating;
    private transient Logs log;
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

    public void initLog() { log = new Logs();}

    public void logout() {
        updateUser(this.loggedInUser);
        this.loggedInUser = null;
    }

    public boolean loginOn (String username, String pass) {
        GeneralUser generalUser = null;
        boolean status = false;
        if(emailToNif.containsKey(username)){
            generalUser = users.get(emailToNif.get(username));
            status = (generalUser.getEmail().equals(username) && generalUser.getPassword().equals(pass));
        }
        if(status) {
            loggedInUser = generalUser;
        }
        return status;
    }

    public GeneralUser getLoggedInUser() {
        return this.loggedInUser;
    }


    public void addUser (GeneralUser generalUser) {
        emailToNif.put(generalUser.getEmail(),generalUser.getNif());
        users.put(generalUser.getNif(),generalUser);
        log.addToLogUser(generalUser);
    }


    public void updateUser (GeneralUser user ) {
        users.put(user.getNif(),user.clone());
    }

    public void populateData ( ) {
        LocalDate date = LocalDate.now();
        Owner _owner = new Owner("own","own","asd","asd",date,"10");
        Client _client = new Client("clt","clt","asd","asd",date,"1000");
        Vehicle _vehicle = new GasCar("Opel","01-EH-33","10",100,1,1,new Posicao(1,1),500);
        addUser(_owner);
        addUser(_client);
        loggedInUser = _owner;
        addCar(_vehicle);
        Rent rent = new Rent(Duration.ZERO,10.0,new Posicao(5,5),"1000","01-EH-33");
        acceptRent(rent);
        loggedInUser = _client;
        giveRate(rent,50);
        loggedInUser = null;
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
        log.flushLog();
    }

    public void createRent (Vehicle rentVehicle,Posicao posicao) {
        Duration duration = Duration.ZERO;
        double _price = rentVehicle.rentPrice(posicao);
        Posicao pos = posicao;
        String nif = loggedInUser.getNif();
        String matricula = rentVehicle.getMatricula();
        Rent rent = new Rent(duration,_price,pos,nif,matricula);
        pendingRent.put(matricula,rent);
    }

    public void acceptRent(Rent rent) {
        addToPendingRating(rent);
        Client _clientRent = (Client) users.get(rent.getNif());
        _clientRent.setPos(rent.getPosicao());
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        pendingRent.remove(rent.getMatricula(),rent);
        log.addToLogRent(rent);
    }

    private void removeFromPendingRating(Rent rent) {
        String nifRent = rent.getNif();
        List<Rent> tmp = pendingRating.get(nifRent);
        Iterator it = tmp.iterator();
        while(it.hasNext()) {
            Rent tmpRent = (Rent) it.next();
            if(tmpRent.equals(rent)) {
                it.remove();
                break;
            }
        }
    }

    private void addToPendingRating(Rent rent) {
        String nifRent = rent.getNif();
        List<Rent> tmp = pendingRating.get(nifRent);
        if(tmp == null) {
            tmp = new ArrayList<>();
        }
        tmp.add(rent);
        pendingRating.put(nifRent,tmp);
    }

    public void giveRate(Rent rent , double rating) {
        rent.setRating(rating);
        loggedInUser.addRentToHistory(rent.clone());
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        _rentVehicle.addRent(rent.clone());
        Owner _ownerVehicle = (Owner) users.get(_rentVehicle.getNifOwner());
        _ownerVehicle.addRentToHistory(rent.clone());
        _ownerVehicle.updateRating(rating);
        removeFromPendingRating(rent);
    }


    public boolean addCar(Vehicle mVehicle) {
        Owner _own = (Owner) loggedInUser;
        boolean isSuccess = _own.addVehicle(mVehicle.getMatricula(),mVehicle);
        if(isSuccess) {
            allVehicles.put(mVehicle.getMatricula(),mVehicle);
            log.addToLogVehicle(mVehicle);
        }
        return isSuccess;
    }

    public List<Rent> getPendingRentList() {
        return new ArrayList<>(pendingRent.values());
    }
    public List<Rent> getPendingRateList() {
        Collection<List<Rent>> tmp = pendingRating.values();
        return tmp.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Vehicle> getAllAvailableVehicles () {
        return this.allVehicles.values().stream().map(Vehicle::clone).collect(Collectors.toList());
    }

    public List<Vehicle> getListOfCarType(Class<? extends Vehicle> a){
        return this.allVehicles.values().stream().filter(l-> l.getClass() == a).map(Vehicle::clone).collect(Collectors.toList());
    }

}
