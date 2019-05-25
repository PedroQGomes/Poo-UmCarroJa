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
    private Map<String,List<Rent>> pendingRating;
    private transient Logs log;
    public boolean isLoggedIn () {
        return (loggedInUser != null);
    }


    public Data() {
        users = new HashMap<>();
        allVehicles = new HashMap<>();
        emailToNif = new HashMap<>();
        pendingRating = new HashMap<>();
        initLog();
    }

    public void initLog() { log = new Logs();}

    public void logout() {
        updateUser(this.loggedInUser);
        this.loggedInUser = null;
    }


    public static Data getDataFromBackupFile(String fileName) {
        if(fileName == null) return null;
        Data mData = null;
        try {
            List<String> dataString = Data.readFromFile(fileName);
            mData = new Data();
            Data finalMData = mData;
            dataString.forEach(s -> parseStringAndAddToData(finalMData,s));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO EXCEPTION" + e.getMessage());
        }
        return mData;

    }

    private static void parseStringAndAddToData(Data mData, String s) {
        String[] typeString = s.split(":");
        switch(typeString[0]) {
            case "NovoProp":
                addOwnerFromString(mData,typeString[1]);
                break;
            case "NovoCliente":
                addClientFromString(mData,typeString[1]);
                break;
            case "NovoCarro":
                addVehicleFromString(mData,typeString[1]);
                break;
            case "Aluguer":
                addAluguerFromString(mData,typeString[1]);
                break;
            case "Classificar":
                addRateFromString(mData,typeString[1]);
                break;
            default:
                break;
        }

    }
    private static void addRateFromString(Data mData,String string) {
        String[] fields = string.split(",");
        if(fields.length == 2) {
            if (fields[0].split("-").length > 1) {
                Vehicle vehicle = mData.allVehicles.get(fields[0]);
                vehicle.updateRating(Double.parseDouble(fields[1]));
            } else {
                GeneralUser user = mData.users.get(fields[0]);
                user.updateRating(Double.parseDouble(fields[1]));
            }
        }
    }

    private static void addOwnerFromString(Data mData , String string) {
        String[] fields = string.split(",");
        if(fields.length == 4) {
            Owner own = new Owner(fields[2], fields[0], "asd", fields[3], LocalDate.now(), fields[1]);
            try {
                mData.addUser(own);
            } catch (utilizadorJaExiste e) {
            }
        }
    }
    private static void addClientFromString(Data mData , String string) {
        String[] fields = string.split(",");
        if(fields.length == 6) {
            Client clt = new Client(fields[2], fields[0], "asd", fields[3], LocalDate.now(), fields[1]);
            try {
                clt.setPos(new Posicao(Double.parseDouble(fields[4]), Double.parseDouble(fields[5])));
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println(e.getMessage());
            }
            try {
                mData.addUser(clt);
            } catch (utilizadorJaExiste e) {
            }
        }
    }

    private static void addAluguerFromString(Data mData, String string) {
        String[] fields = string.split(",");
        if(fields.length == 5) {
            Class<? extends Vehicle> carType = null;
            Vehicle _vehicle = null;
            Posicao p = new Posicao(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]));
            try {
                switch (fields[3]) {
                    case "Gasolina":
                        carType = GasCar.class;
                        break;
                    case "Eletrico":
                        carType = EletricCar.class;
                        break;
                    default:
                        carType = HybridCar.class;
                        break;
                }
                if (fields[4].equals("MaisPerto")) _vehicle = Rent.getNearCar(mData.getListOfCarType(carType), p);
                else if (fields[4].equals("MaisBarato"))
                    _vehicle = Rent.getCheapestCar(mData.getListOfCarType(carType));
            } catch (semVeiculosException e) {
                return;
            }
            if (_vehicle != null) {
                mData.loggedInUser = mData.users.get(fields[0]);
                if (mData.getLoggedInUser() != null)
                    mData.createRent(_vehicle, p);
            }
            mData.loggedInUser = null;
        }
    }

    private static void addVehicleFromString(Data mData , String string) {
        String[] fields = string.split(",");
        if(fields.length == 10) {
            Vehicle _mVehicle;
            int averagespeed = Integer.parseInt(fields[4]);
            double pricePerKm = Double.parseDouble(fields[5]);
            double consumptionPerKm = Double.parseDouble(fields[6]);
            double fuel = Double.parseDouble(fields[7]);
            Posicao mpos = new Posicao(Double.parseDouble(fields[8]), Double.parseDouble(fields[9]));
            switch (fields[0]) {
                case "Electrico":
                    _mVehicle = new EletricCar(fields[1], fields[2], fields[3], averagespeed, pricePerKm, consumptionPerKm, mpos, fuel);
                    break;
                case "Gasolina":
                    _mVehicle = new GasCar(fields[1], fields[2], fields[3], averagespeed, pricePerKm, consumptionPerKm, mpos, fuel);
                    break;
                default:
                    _mVehicle = new HybridCar(fields[1], fields[2], fields[3], averagespeed, pricePerKm, consumptionPerKm, mpos, fuel);
                    break;
            }
            mData.loggedInUser = mData.users.get(fields[3]);
            mData.addCar(_mVehicle);
            mData.loggedInUser = null;
        }
    }

    private static List<String> readFromFile(String fileName) throws FileNotFoundException, IOException {
        List<String> linhas = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String linha;
        while ((linha = br.readLine()) != null) linhas.add(linha);
        br.close();
        return linhas;
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
        if(this.loggedInUser == null) return null;
        return this.loggedInUser.clone();
    }


    public void addUser (GeneralUser generalUser) throws utilizadorJaExiste {
        if(emailToNif.get(generalUser.getEmail()) != null) throw new utilizadorJaExiste("Utilizador já existe");
        emailToNif.put(generalUser.getEmail(),generalUser.getNif());
        users.put(generalUser.getNif(),generalUser);
        log.addToLogUser(generalUser);
    }


    public void updateUser (GeneralUser user ) {
        users.put(user.getNif(),user.clone());
        loggedInUser = user;
    }

    /*public void populateData ( ) {
        LocalDate date = LocalDate.now();
        Owner _owner = new Owner("own","own","asd","asd",date,"10");
        Client _client = new Client("clt","clt","asd","asd",date,"1000");
        Vehicle _vehicle = new GasCar("Opel","01-EH-33","10",100,1,1,new Posicao(1,1),500);
        addUser(_owner);
        addUser(_client);
        loggedInUser = _owner;
        addCar(_vehicle);
        loggedInUser = _client;
        createRent(_vehicle,new Posicao(5,5));
        loggedInUser = null;
    } */

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
        updateVehicle(rentVehicle,rent.clone());
        addToPendingRating(rent.clone(),rentVehicle.getNifOwner());
        addToPendingRating(rent.clone(),nif);
        ((Client)loggedInUser).setPos(rent.getPosicao().clone());
        log.addToLogRent(rent);
    }



    public void updateVehicle (Vehicle mVehicle, Rent rent) {
        mVehicle.executeTrip(rent);
        allVehicles.put(mVehicle.getMatricula(),mVehicle.clone());
        Owner vehicleOwner = (Owner) users.get(mVehicle.getNifOwner());
        vehicleOwner.acceptRent(rent.clone());
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

    private void addToPendingRating(Rent rent, String nif) {
        List<Rent> tmp = pendingRating.get(nif);
        if(tmp == null) {
            tmp = new ArrayList<>();
        }
        tmp.add(rent);
        pendingRating.put(nif,tmp);
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

    public void giveRateVehicle(Rent rent , double rating) {
        rent.setRating(rating);
        //loggedInUser.addRentToHistory(rent.clone());
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        _rentVehicle.addRent(rent.clone());
        Owner _ownerVehicle = (Owner) users.get(_rentVehicle.getNifOwner());
        _ownerVehicle.updateVehicle(_rentVehicle);

        //_ownerVehicle.addRentToHistory(rent.clone());
        //_ownerVehicle.updateRating(rating);
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
    public static Data recoverState() {
        Data mData = null;
        try {
            FileInputStream fis = new FileInputStream("data.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (Data) ois.readObject();
            System.out.println("Dados Lidos");
        } catch (InvalidClassException e) {
            System.out.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro de carregamento de dados não existe");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        if (mData == null) mData = new Data();
        else mData.initLog();
        return mData;
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
