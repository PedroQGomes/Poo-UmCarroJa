import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Menus
{
    private List<String> menuOptions;
    private int choise;

    public Menus(){
        this.menuOptions = new ArrayList<>();
        this.choise = 0;
    }

<<<<<<< HEAD
    private void ownerMenu() {
        System.out.println("1 -> Registar um carro");
        System.out.println("2 -> Ver registos dos carros");
        System.out.println("3 -> Ver histórico de aluguer");
        System.out.println("4 -> Abastecer um carro");
        System.out.println("5 -> Receitas da ultima Viagem");
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
                viewLastRentPrice();
                break;
            case 9:
                logout();
                break;
            default:
                break;
        }
    }

=======
    public Menus(List<String> a){
        this.menuOptions = new ArrayList<>(a);
        this.choise = 0;
    }

    public int readOption(){
       Scanner s = new Scanner(System.in);
       int op = 0;
       try {
           op = s.nextInt();
       }
       catch (InputMismatchException e){op = 0;}
>>>>>>> 8aafa144df2f2214a29d7f2e2a9aa4a8b9852da8

       if(op < 0){
           System.out.println("Escolha um numero positivo");
           op = 0;
       }
       return op;
    }


    public void printMenu(){
        int count = 1;
        for(String s : this.menuOptions){
            System.out.print(count);
            System.out.print("-");
            System.out.println(s);
            count++;
        }
    }

    public void printMenu(int x){
        System.out.print(this.menuOptions.get(x));
    }

    public void exacuteMenu(){
        printMenu();
        this.choise = readOption();
    }


<<<<<<< HEAD
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

    private void aluguerMenu() {
        Vehicle _rentVehicle = null;
        System.out.println("1 -> Solicitar o aluguer de um carro mais prox das sua Posicao");
        System.out.println("2 -> Solicitar o aluguer de um carro mais barato");
        System.out.println("3 -> Solicitar o aluguer de um carro especifico");
        System.out.println("4 -> Solicitar um aluguer de um carro com uma autonomia desejada");
        System.out.println("9 -> Voltar a trás");
        int res = sn.nextInt();
        Client clt = (Client) data.getLoggedInUser();
        switch (res) {
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
                try {
                    data.addUser(owner);
                }catch(utilizadorJaExiste e) {}
                break;
            case 2:
                Client client = new Client(email,name,password,morada,birthDate,nif);
                try {
                    data.addUser(client);
                }catch(utilizadorJaExiste e) {}
                break;
            default:
                break;
        }
    }

    public static void clearScreen() {
        //System.out.print("\033[H\033[2J");
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.flush();
=======
    public int getChoise(){
        return this.choise;
>>>>>>> 8aafa144df2f2214a29d7f2e2a9aa4a8b9852da8
    }
}

