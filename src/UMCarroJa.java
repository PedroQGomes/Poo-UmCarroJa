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

public class UMCarroJa implements  Serializable ,IUMCarroJa
{
    private static final long serialVersionUID = 123456789L;
    private Map<String,String> emailToNif;
    private Map<String, GeneralUser> users; // HashMap que contém todos os users, tendo o nif como chave
    private Map<String,Vehicle> allVehicles;
    private GeneralUser loggedInUser = null;
    private Map<String,List<Rent>> pendingRating;
    private transient Logs log;
    private boolean backupDataRead = false;


    /**
     * Construtor da UMCarroJa
     */
    public UMCarroJa() {
        users = new HashMap<>();
        allVehicles = new HashMap<>();
        emailToNif = new HashMap<>();
        pendingRating = new HashMap<>();
        initLog();
    }

    /**
     * Inicia o Log
     */
    public void initLog() {
        try{
            log = new Logs();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Dá logout do utilizador
     */
    public void logout() {
        updateUser(this.loggedInUser);
        this.loggedInUser = null;
    }

    /**
     * Lê o ficheiro csv e faz o parse retornando o Objeto UMCarroJa já populado com a informação que o ficheiro csv tiver
     * @param fileName
     * @return UMCarroJa com a informação do ficheiro csv já parsed.
     */
    public static UMCarroJa getDataFromBackupFile(String fileName) {
        if(fileName == null) return null;
        UMCarroJa mUMCarroJa = null;
        try {
            List<String> dataString = UMCarroJa.readFromFile(fileName);
            mUMCarroJa = new UMCarroJa();
            UMCarroJa finalMUMCarroJa = mUMCarroJa;
            dataString.forEach(s -> parseStringAndAddToData(finalMUMCarroJa,s));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO EXCEPTION" + e.getMessage());
        }
        return mUMCarroJa;

    }

    /**
     * Dá parse da string tendo em conta o primeiro parâmetro do ficheiro csv
     * @param mUMCarroJa
     * @param s
     */
    private static void parseStringAndAddToData(UMCarroJa mUMCarroJa, String s) {
        String[] typeString = s.split(":");
        switch(typeString[0]) {
            case "NovoProp":
                addOwnerFromString(mUMCarroJa,typeString[1]);
                break;
            case "NovoCliente":
                addClientFromString(mUMCarroJa,typeString[1]);
                break;
            case "NovoCarro":
                addVehicleFromString(mUMCarroJa,typeString[1]);
                break;
            case "Aluguer":
                addAluguerFromString(mUMCarroJa,typeString[1]);
                break;
            case "Classificar":
                addRateFromString(mUMCarroJa,typeString[1]);
                break;
            default:
                break;
        }

    }

    /**
     *  Adiciona o Rating à aplicação através dos parâmetros do ficheiro csv.
     * @param mUMCarroJa
     * @param string
     */
    private static void addRateFromString(UMCarroJa mUMCarroJa, String string) {
        String[] fields = string.split(",");
        if(fields.length == 2) {
            if (fields[0].split("-").length > 1) {
                Vehicle vehicle = mUMCarroJa.allVehicles.get(fields[0]);
                vehicle.updateRating(Double.parseDouble(fields[1]));
            } else {
                GeneralUser user = mUMCarroJa.users.get(fields[0]);
                user.updateRating(Double.parseDouble(fields[1]));
            }
        }
    }

    /**
     * Adiciona o Proprietário à aplicação através dos parâmetros do ficheiro csv.
     * @param mUMCarroJa
     * @param string
     */
    private static void addOwnerFromString(UMCarroJa mUMCarroJa, String string) {
        String[] fields = string.split(",");
        if(fields.length == 4) {
            Owner own = new Owner(fields[2], fields[0], "asd", fields[3], LocalDate.now(), fields[1]);
            try {
                mUMCarroJa.addUser(own);
            } catch (utilizadorJaExiste e) {
            }
        }
    }

    /**
     * Adiciona o cliente à aplicação através dos parâmetros do ficheiro csv.
     * @param mUMCarroJa
     * @param string
     */
    private static void addClientFromString(UMCarroJa mUMCarroJa, String string) {
        String[] fields = string.split(",");
        if(fields.length == 6) {
            Client clt = new Client(fields[2], fields[0], "asd", fields[3], LocalDate.now(), fields[1]);
            try {
                clt.setPos(new Posicao(Double.parseDouble(fields[4]), Double.parseDouble(fields[5])));
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println(e.getMessage());
            }
            try {
                mUMCarroJa.addUser(clt);
            } catch (utilizadorJaExiste e) {
            }
        }
    }

    /**
     * Adiciona o aluguer à aplicação tendo em conta os parâmetros do ficheiro csv.
     * @param mUMCarroJa
     * @param string
     */
    private static void addAluguerFromString(UMCarroJa mUMCarroJa, String string) {
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
                if (fields[4].equals("MaisPerto")) _vehicle = Rent.getNearCar(mUMCarroJa.getListOfCarType(carType), p);
                else if (fields[4].equals("MaisBarato"))
                    _vehicle = Rent.getCheapestCar(mUMCarroJa.getListOfCarType(carType));
            } catch (semVeiculosException e) {
                return;
            }
            if (_vehicle != null) {
                mUMCarroJa.loggedInUser = mUMCarroJa.users.get(fields[0]);
                if (mUMCarroJa.getLoggedInUser() != null)
                    mUMCarroJa.createRent(_vehicle, p);
            }
            mUMCarroJa.loggedInUser = null;
        }
    }

    /**
     * Adiciona o veiculo à aplicação tendo em conta os parâmetros no ficheiro csv
     * @param mUMCarroJa
     * @param string
     */
    private static void addVehicleFromString(UMCarroJa mUMCarroJa, String string) {
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
            mUMCarroJa.loggedInUser = mUMCarroJa.users.get(fields[3]);
            mUMCarroJa.addCar(_mVehicle);
            mUMCarroJa.loggedInUser = null;
        }
    }

    /**
     * Lê de um ficheiro para uma List<String>
     * @param fileName
     * @return List<String> que leu do ficheiro
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static List<String> readFromFile(String fileName) throws FileNotFoundException, IOException {
        List<String> linhas = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String linha;
        while ((linha = br.readLine()) != null) linhas.add(linha);
        br.close();
        return linhas;
    }

    /**
     * Faz login na aplicação confirmando o username e a password.
     * @param username
     * @param pass
     * @return true caso o login seja bem sucedido , false caso contrário.
     */
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

    /**
     * Retorna o utilizador que está logado.
     * @return o utilizador
     */
    public GeneralUser getLoggedInUser() {
        if(this.loggedInUser == null) return null;
        return this.loggedInUser.clone();
    }

    /**
     * Adiciona um utilizador à aplicação
     * @param generalUser
     * @throws utilizadorJaExiste
     */
    public void addUser (GeneralUser generalUser) throws utilizadorJaExiste {
        if(emailToNif.get(generalUser.getEmail()) != null || users.get(generalUser.getNif()) != null) throw new utilizadorJaExiste("Utilizador já existe");
        emailToNif.put(generalUser.getEmail(),generalUser.getNif());
        users.put(generalUser.getNif(),generalUser);
        if(log != null)
        log.addToLogUser(generalUser);
    }

    /**
     * Da update ao map dos users de forma a guardar a informação que se modificou
     * @param user
     */
    public void updateUser (GeneralUser user ) {
        users.put(user.getNif(),user.clone());
        loggedInUser = user;
    }

    /**
     * Verifica se já foi lida a data do ficheiro .bak
     * @return true se sim, falso se não
     */
    public boolean isBackupDataRead() {return this.backupDataRead;}

    /**
     * Mete a true se a data do ficheiro .bak já foi lida
     */
    public void setBackupDataRead() {this.backupDataRead = true;}

    /**
     * Verifica se há algum user logado
     * @return true, se sim, false se não
     */
    public boolean isLoggedIn () {
        return (loggedInUser != null);
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


    /**
     * Guarda o estado num object file
     */
    public void saveState ( ) {
        try {
            FileOutputStream fos = new FileOutputStream("data.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            System.out.println("Dados Gravados");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if(log != null)
        log.flushLog();
    }

    /**
     * Cria o aluguer e executa-o no carro e adiciona aos respetivos históricos, atualiza a posição do cliente para onde ele vai , e adiciona o aluguer ao log
     * @param rentVehicle
     * @param posicao
     */
    public void createRent (Vehicle rentVehicle,Posicao posicao) {
        Duration duration = rentVehicle.rentTime(posicao);
        double _price = rentVehicle.rentPrice(posicao);
        Posicao pos = posicao;
        String nif = this.loggedInUser.getNif();
        String matricula = rentVehicle.getMatricula();
        Rent rent = new Rent(duration,_price,pos,nif,matricula);
        updateVehicleRent(rentVehicle,rent.clone());
        addToPendingRating(rent.clone(),rentVehicle.getNifOwner());
        addToPendingRating(rent.clone(),nif);
        addRentToHistory(rent,rentVehicle.getNifOwner());
        ((Client)loggedInUser).setPos(rent.getPosicao().clone());
        if(log != null)
        log.addToLogRent(rent);
    }

    private void addRentToHistory(Rent rent, String nifOwner) {
        Client clt = (Client) this.users.get(rent.getNif());
        Owner own = (Owner) this.users.get(nifOwner);
        clt.addRentToHistory(rent);
        own.addRentToHistory(rent);
    }

    /**
     * Atualiza no allVehicles o carro
     * @param mVehicle
     */
    public void updateVehicle(Vehicle mVehicle) {
        allVehicles.put(mVehicle.getMatricula(),mVehicle.clone());
    }

    /**
     * Da execute ao rent (atualizando combustivel,etc) e guarda no histórico do carro a rent
     * @param mVehicle
     * @param rent
     */
    public void updateVehicleRent (Vehicle mVehicle, Rent rent) {
        mVehicle.executeTrip(rent);
        mVehicle.addRent(rent);
        updateVehicle(mVehicle);
    }

    /**
     * Remove da pending Rating o aluguer tendo em conta o nif , para quando já foi dado o rate não aparecer que ainda está pending
     * @param rent
     * @param nif
     */
    private void removeFromPendingRating(Rent rent,String nif) {
        String nifRent = nif;
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

    /**
     * Adiciona à lista pendingRating para o utilizador saber que ainda tem ratings por dar
     * @param rent
     * @param nif
     */
    private void addToPendingRating(Rent rent, String nif) {
        List<Rent> tmp = pendingRating.get(nif);
        if(tmp == null) {
            tmp = new ArrayList<>();
        }
        tmp.add(rent);
        pendingRating.put(nif,tmp);
    }

    /**
     * Dá rate ao owner e ao veiculo juntos.
     * @param rent
     * @param rating
     */
    public void giveRate(Rent rent , double rating) {
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        Owner _ownerVehicle = (Owner) users.get(_rentVehicle.getNifOwner());
        _ownerVehicle.updateRating(rating);
        _rentVehicle.updateRating(rating);
        removeFromPendingRating(rent,loggedInUser.getNif());
    }

    /**
     * Dá rate ao cliente.
     * @param rent
     * @param rating
     */
    public void giveRateClient(Rent rent,double rating) {
        Client _client = (Client) this.users.get(rent.getNif());
        _client.updateRating(rating);
        removeFromPendingRating(rent,this.getLoggedInUser().getNif());
    }


    /**
     * Dá rate ao owner e ao veículo em separado
     * @param rent
     * @param rating
     * @param ratingCar
     */
    public void giveRate(Rent rent , double rating, double ratingCar) {
        Vehicle _rentVehicle = allVehicles.get(rent.getMatricula());
        Owner _ownerVehicle = (Owner) users.get(_rentVehicle.getNifOwner());
        _ownerVehicle.updateRating(rating);
        _rentVehicle.updateRating(ratingCar);
        removeFromPendingRating(rent,loggedInUser.getNif());
    }


    /**
     * Adiciona carro à aplicação, verificando se já existe
     * @param mVehicle
     * @return
     */
    public boolean addCar(Vehicle mVehicle) {
        Owner _own = (Owner) loggedInUser;
        boolean isSuccess = _own.addVehicle(mVehicle.getMatricula());
        if(isSuccess) {
            allVehicles.put(mVehicle.getMatricula(),mVehicle);
            if(log != null)
            log.addToLogVehicle(mVehicle);
        }
        return isSuccess;
    }


    /**
     * Recupera o estado da aplicação
     * @return UMCarroJa
     */
    public static UMCarroJa recoverState() {
        UMCarroJa mUMCarroJa = null;
        try {
            FileInputStream fis = new FileInputStream("data.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mUMCarroJa = (UMCarroJa) ois.readObject();
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
        if (mUMCarroJa == null) mUMCarroJa = new UMCarroJa();
        else mUMCarroJa.initLog();
        return mUMCarroJa;
    }

    /**
     * Devolve a lista de alugueres pendentes a dar rating
     * @param nif
     * @return
     */
    public List<Rent> getPendingRateList(String nif) {
        return this.pendingRating.get(nif);
    }

    /**
     * Retorna todos os veículos da aplicação
     * @return List<Vehicle> de todos os veículos da aplicação
     */
    public List<Vehicle> getAllAvailableVehicles () {
        return this.allVehicles.values().stream().map(Vehicle::clone).collect(Collectors.toList());
    }

    /**
     * Retorna todos os veículos de um certo tipo da aplicação
     * @param a
     * @return List<Vehicle>
     */
    public List<Vehicle> getListOfCarType(Class<? extends Vehicle> a){
        return this.allVehicles.values().stream().filter(l-> l.getClass() == a).map(Vehicle::clone).collect(Collectors.toList());
    }


    /**
     * Retorna a lista dos carros do Proprietário que está logado
     * @return List<Vehicle> dos carros do Proprietário que está logado.
     */
    public List<Vehicle> getListOfCarOwned() {
        if(!(getLoggedInUser() instanceof Owner)) return null;
        Owner owner = (Owner) getLoggedInUser();
        List<Vehicle> tmp = new ArrayList<>();
        List<String> tmpMat = owner.getListOfMatricula();
        for(String plat:tmpMat) {
            tmp.add(this.allVehicles.get(plat));
        }
        return tmp;
    }

    /**
     * Retorna a lista de carros que precisam de ser abastecidos
     * @return List<Vehicle> lista de carros que precisam de ser abastecidos
     */
    public List<Vehicle> getListOfCarsFuelNeeded() {
        List<Vehicle> tmp = getListOfCarOwned();
        return tmp.stream().filter(Vehicle::getNeedFuel).collect(Collectors.toList());
    }

    public void abasteceCarro(String a){
        if(this.allVehicles.get(a) == null){
            System.out.println("Este carro nao está no sistema");
        } else{
            this.allVehicles.get(a).abastece();
        }
    }

}
