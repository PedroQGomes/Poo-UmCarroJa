import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class Controller {
    private Menus menuPrincipal;
    private Menus client;
    private Menus owner;
    private Menus aluguer;
    private boolean running = true;
    private Data data;
    private Scanner sn;



    public Controller(Data date){
        String[] listmenuPrincipal = {"Login", "Registar","Sair"};
        String[] listClient = {"Alugar um carro","Consultar Histórico de aluguer","Preço da ultima viagem",
                "Dar rating aos  alugueres","Definir Posição","Sair"};
        String[] listOwner = {"Registar um carro","Ver registos dos carros",
                "Ver histórico de aluguer","Abastecer um carro","Receitas da ultima Viagem","Dar rating aos clientes","Sair"};
        String[] listAluger = {"Solicitar o aluguer de um carro mais prox das sua Posicao","Solicitar o aluguer de um carro mais barato",
                "Solicitar o aluguer de um carro especifico","Solicitar um aluguer de um carro com uma autonomia desejada","Voltar a trás"};
        this.menuPrincipal = new Menus(listmenuPrincipal);
        this.client = new Menus(listClient);
        this.owner = new Menus(listOwner);
        this.aluguer = new Menus(listAluger);
        this.data = date;
    }


    public void initControler(){
        sn = new Scanner(System.in);
        while(running) {
            System.out.println("BEM VINDO A UMCARROJA");
            if(data.isLoggedIn()) {
                activeMenu();
            } else mainMenu();
            clearScreen();
        }

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
        if(isOwner){ // pode ser melhorado
            Owner own = (Owner) data.getLoggedInUser();
            System.out.printf("Nome: %s  Rating: %.2f   Tipo de User: %s\n",own.getName(), own.getRating() ,str);
            ownerMenu();
        } else { // pode ser melhoraoo
            Client clt = (Client) data.getLoggedInUser();
            System.out.printf("Nome: %s Nif: %s  Posicao: %s   Tipo de User: %s\n",clt.getName(), clt.getNif() ,clt.getPos().toString() ,str);
            clientMenu();
        }

    }


    private void mainMenu(){
        this.menuPrincipal.executeMenu();
        switch (this.menuPrincipal.getChoice()) {
            case 1:
                loginUser();
                break;
            case 2:
                registaUser();
                break;
                default:
                    running = false;
                    break;

        }

    }

    private void loginUser() {
        System.out.print("Insira o email: ");
        String userName = sn.next();
        System.out.print("Insira a password: ");
        String pass = sn.next();
        if(data.loginOn(userName,pass)){
            System.out.println("Está logado!");
        }
    }


    private void registaUser(){

        System.out.print("Registar como Owner (1) ou como Cliente(2) ");
        int option = 0;
        option = this.aluguer.readOption();
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
                try{
                    data.addUser(owner);
                } catch(utilizadorJaExiste e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                Client client = new Client(email,name,password,morada,birthDate,nif);
                try{
                    data.addUser(client);
                } catch(utilizadorJaExiste e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                break;
        }
    }
    /*
    private void registaUUser(){
        System.out.print("Registar como Owner (1) ou como Cliente(2) ");
        int option = this.aluguer.readOption();
        int i;
        List<String> tmp = new ArrayList<>();
        for(i=0;i > this.regista.size();i++){
            System.out.print(this.regista.get(i));
            String a = sn.next();
            tmp.add(a);
        }
        String[] arrStrBirth =tmp.get(tmp.size()-1).split("-");
    }*/

    private void clientMenu(){
        this.client.executeMenu();
        switch (this.client.getChoice()) {
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
                giveRatingToRentsMenu();
                break;
            case 5:
                Client user = ((Client) data.getLoggedInUser());
                user.setPos(getPositionMenu());
                data.updateUser(user);
                break;
            default:
                logout();
                break;
        }
    }

    private void giveRatingToRentsMenu(){
        System.out.println("1-Separado");
        System.out.println("2-Junto");
        int a = sn.nextInt();
        switch (a){
            case 1:
                //giveRatingToOwner();
                //giveRatingToVehicle();
                break;
            case 2:
                giveRatingToRents();
                break;
        }
    }


    private void ownerMenu(){
        this.owner.executeMenu();
        switch (this.owner.getChoice()) {
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
                viewLastRentPrice();
                break;
            case 6:
                //rateClient();
                break;
            case 9:
                logout();
                break;
            default:
                break;
        }
    }




    private void aluguerMenu(){
        Vehicle _rentVehicle = null;
        this.aluguer.executeMenu();
        Client clt = (Client) data.getLoggedInUser();
        switch (this.aluguer.getChoice()) {
            case 1:
                try {
                    _rentVehicle = Rent.getNearCar(data.getAllAvailableVehicles(),clt.getPos());
                } catch (semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            case 2:
                try {
                    _rentVehicle = Rent.getCheapestCar(data.getAllAvailableVehicles());
                } catch(semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
        if(_rentVehicle != null){
            Posicao toWhere = getPositionMenu();
            data.createRent(_rentVehicle,toWhere);
        }
    }



    private Posicao getPositionMenu () {
        System.out.print("Posicao (Ex: x,y ) : ");
        String posString = sn.next();
        String[] arrPosString = posString.split(",");
        return new Posicao(Double.parseDouble(arrPosString[0]),Double.parseDouble(arrPosString[1]));
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

        Posicao mPos = getPositionMenu();


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

    public static void clearScreen() {
        //System.out.print("\033[H\033[2J");
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.flush();
    }



}
