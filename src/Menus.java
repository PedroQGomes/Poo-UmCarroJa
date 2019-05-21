import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Class to handle the different menus of the Interface, so as to not overload Main.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Menus
{
    private static Scanner sn;
    private Data data;
    private boolean running = true;
    public Menus(Data data) {
        this.data = data;
    }
    public void initMenu() {
        sn = new Scanner(System.in);
        while(running) {
            System.out.println("BEM VINDO A UMCARROJA");
            if(data.isLoggedIn()) {
                activeMenu();
            } else loginMenu();
            clearScreen();
        }
        sn.close();
    }

    private void activeMenu() {
        String str;
        boolean isOwner = true;
        if(data.getLoggedInUser().getClass() == Owner.class) {
            str = "Proprietário";
        } else {
            isOwner = false;
            str = "Cliente";
        }

        if(isOwner){
            Owner own = (Owner) data.getLoggedInUser();
            System.out.printf("Nome: %s  Rating: %.2f   Tipo de User: %s\n",own.getName(), own.getRating() ,str);
            ownerMenu();
        } else {
            Client clt = (Client) data.getLoggedInUser();
            System.out.printf("Nome: %s Nif: %s  Posicao: %s   Tipo de User: %s\n",clt.getName(), clt.getNif() ,clt.getPos().toString() ,str);
            clientMenu();
        }

    }

    private void ownerMenu() {
        System.out.println("1 -> Registar um carro");
        System.out.println("2 -> Ver registos dos carros");
        System.out.println("3 -> Ver histórico de aluguer");
        System.out.println("4 -> Abastecer um carro");
        System.out.println("5 -> Aceitar/Rejeitar o aluguer de um determinado cliente");
        System.out.println("6 -> Receitas da ultima Viagem");
        System.out.println("9 -> Sair");
        int res = sn.nextInt();
        switch (res) {
            case 1:
                vehicleRegister();
                break;
            case 2:
                viewOwnerCars();
                break;
            case 3:
                viewRentHistory();
                break;
            case 4:
                break;
            case 5:
                acceptRent();
                break;
            case 6:
                viewLastRentPrice();
                break;
            case 9:
                logout();
                break;
            default:
                break;
        }
    }

    private void acceptRent() {
        List<Rent> rentList = data.getPendingRentList();
        showList(rentList);
        int indexRent = sn.nextInt();
        if(rentList.size() >= indexRent) {
            data.acceptRent(rentList.get(indexRent-1));
       } else {

        }
    }

    private void viewRentHistory() {
        List<Rent> rentList = data.getLoggedInUser().getRentList();
        showList(rentList);
        sn.next();
    }

    private void viewOwnerCars() {
        Owner _owner = (Owner) data.getLoggedInUser();
        List <Vehicle> vehicleList = _owner.getListCar();
        showList(vehicleList);
        sn.next();
    }

    private void showList(List<?> list) {
        int i = 1;
        for(Object l:list) {
            System.out.println(i + " -> " + l.toString());
            i++;
        }
    }
    private void vehicleRegister() {
        boolean tmp = false;
        while(!tmp){
            Vehicle _vehicle = null;
            System.out.println("1 -> Carro hibrido");
            System.out.println("2 -> Carro eletrico");
            System.out.println("3 -> Carro a Gasóleo");
            System.out.println("4 -> Sair");
            int res = sn.nextInt();
            if(res != 1 && res != 2 && res != 3) break;
            _vehicle = newVehicleWithProperties(res);
            if(_vehicle != null)
                tmp = data.addCar(_vehicle);
            if(!tmp) System.out.println("\nJá existe essa Matricula\n");
        }

    }
    private Vehicle newVehicleWithProperties(int vehicleType) {
        Vehicle _car = null;

        System.out.print("Matricula: ");
        String matricula = sn.next();

        System.out.print("Preço por km:");
        double pricePerKm = sn.nextDouble();

        System.out.print("Velocidade Media:");
        int averageSpeed = sn.nextInt();

        System.out.print("Consumo por KM: ");
        double consumPerKm = sn.nextDouble();

        System.out.print("Posicao (Ex: x,y ) : ");
        String posString = sn.next();
        String[] arrPosString = posString.split(",");
        Posicao mPos = new Posicao(Double.parseDouble(arrPosString[0]),Double.parseDouble(arrPosString[1]));


        System.out.print("Marca :");
        String marca = sn.next();


        System.out.print("Quantidade de combustivel : ");
        double fuel = sn.nextDouble();

        switch(vehicleType) {
            case 1:
                _car = new HybridCar(marca,matricula,data.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
            case 2:
                _car = new EletricCar(marca,matricula,data.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
            case 3:
                _car = new GasCar(marca,matricula,data.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
        }
        return _car;
    }

    private void logout() {
        data.logout();
    }

    private void clientMenu() {
        System.out.println("1 -> Alugar um carro");
        System.out.println("2 -> Consultar Histórico de aluguer");
        System.out.println("3 -> Preço da ultima viagem");
        System.out.println("4 -> Dar rating aos  alugueres");
        System.out.println("5 -> Definir Posição");
        System.out.println("9 -> Sair");

        int res = sn.nextInt();
        switch (res) {
            case 1:
                aluguerMenu();
                break;
            case 2:
                viewRentHistory();
                break;
            case 3:
                viewLastRentPrice();
                break;
            case 4:
                giveRatingToRents();
                break;
            case 5:
                updateClientPositionMenu();
                break;
            default:
                logout();
                break;
        }
    }

    private void updateClientPositionMenu() {
        System.out.print("Posicao (Ex: x,y ) : ");
        String posString = sn.next();
        String[] arrPosString = posString.split(",");
        Posicao mPos = new Posicao(Double.parseDouble(arrPosString[0]),Double.parseDouble(arrPosString[1]));
        Client client = (Client) data.getLoggedInUser();
        client.setPos(mPos);
    }

    private void giveRatingToRents() {
        List<Rent> pendingRateList = data.getPendingRateList();
        showList(pendingRateList);
        int choice = sn.nextInt();
        if(pendingRateList.size() >= choice) {
            System.out.println("Rate (0.0-100.0): ");
            double rate = sn.nextDouble();
            data.giveRate(pendingRateList.get(choice-1),rate);
        }

    }
    private void viewLastRentPrice() { // TODO : MELHORAR ISTO
        List<Rent> rentList = data.getLoggedInUser().getRentList();
        if(!rentList.isEmpty()){
            Rent rent = rentList.get(rentList.size()-1);
            System.out.println(rent.getPrice());
            sn.next();
        } else {
            System.out.println("O cliente ainda não realizou alugueres");
        }
    }

    private void aluguerMenu() {
        Vehicle _rentVehicle = null;
        System.out.println("1 -> Solicitar o aluguer de um carro mais prox das sua Posicao");
        System.out.println("2 -> Solicitar o aluguer de um carro mais barato");
        System.out.println("3 -> Solicitar o aluguer de um carro especifico");
        System.out.println("4 -> Solicitar um aluguer de um carro com uma autonomia desejada");
        System.out.println("5 -> Voltar a trás");

        int res = sn.nextInt();
        switch (res) {
            case 1:
                try {
                    _rentVehicle = Rent.RentCheapestCar(data);
                } catch (semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 9:
                clientMenu();
                break;
            default:
                break;
        }
        if(_rentVehicle != null){
            Posicao toWhere = getPosicaoMenu();
            data.createRent(_rentVehicle,new Posicao(5,5));
        }
    }

    private Posicao getPosicaoMenu() {
        return null;
    }

    private void loginMenu() {

        System.out.println("1 -> Login");
        System.out.println("2 -> Registar");
        int res = sn.nextInt();
        switch (res) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            default:
                running = false;
                break;
        }
    }
    private void login() {
        System.out.print("Insira o email: ");
        String userName = sn.next();
        System.out.print("Insira a password: ");
        String pass = sn.next();
        if(data.loginOn(userName,pass)) LoggedInMenu();
    }

    private void LoggedInMenu() {
        System.out.println("Está logado!");
    }

    private void register() {
        System.out.print("Registar como Owner (1) ou como Cliente(2) ");
        int option = sn.nextInt();
        System.out.print("Email:");
        String email = sn.next();
        System.out.print("Name:");
        String name = sn.next();
        System.out.print("Password:");
        String password = sn.next();
        System.out.print("Morada:");
        String morada = sn.next();
        System.out.print("Nif:");
        String nif = sn.next();
        System.out.print("Data de Nascimento (Formato: 15-01-2005):");
        String birthDateString = sn.next();
        String[] arrStrBirth = birthDateString.split("-");
        while(!(option == 1 || option == 2)) {
            option = sn.nextInt();
        }
        while(arrStrBirth.length < 3) {
            System.out.println("Data de nascimento inválida , insira neste formato (15-01-2005):");
            birthDateString = sn.next();
            arrStrBirth = birthDateString.split("-");
        }
        LocalDate birthDate = LocalDate.of(Integer.parseInt(arrStrBirth[2]),Integer.parseInt(arrStrBirth[1]),Integer.parseInt(arrStrBirth[0]));
        switch(option) {
            case 1:
                Owner owner = new Owner(email,name,password,morada,birthDate,nif);
                data.addUser(owner);
                break;
            case 2:
                Client client = new Client(email,name,password,morada,birthDate,nif);
                data.addUser(client);
                break;
            default:
                break;
        }
    }

    public static void clearScreen() {
        //System.out.print("\033[H\033[2J");
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.flush();
    }
}
