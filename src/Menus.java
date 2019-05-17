import java.time.LocalDate;
import java.time.LocalDateTime;
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
            clearScreen();
            System.out.println("BEM VINDO A UMCARROJA");
            if(data.isLoggedIn()) {
                activeMenu();
            } else loginMenu();
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
        System.out.printf("Nome: %s         Tipo de User: %s\n",data.getLoggedInUser().getName(), str);
        if(isOwner) ownerMenu();
        else clientMenu();

    }

    private void ownerMenu() {
        System.out.println("1 -> ETC");
        System.out.println("9 -> Sair");
        System.out.println("aceitar/rejeitar o aluguer de um determinado cliente");
        System.out.println("registar qnt custou a viagem");
        int res = sn.nextInt();
        switch (res) {
            case 1:
                break;
            case 9:
                logout();
                break;
            default:
                break;
        }
    }

    private void logout() {
        data.logout();
    }

    private void clientMenu() {
        System.out.println("1 -> ETC");
        System.out.println("9 -> Sair");
        System.out.println("solicitar um aluguer de um carro mais prox das sua Posicao");
        System.out.println("solicitar um aluguer de um carro mais barato");
        System.out.println("solicitar um aluguer de um carro mais barato dentro uma dist q podem andar");
        System.out.println("solicitar um aluguer de um carro especifico");
        System.out.println("solicitar um aluguer de um carro com uma autonomia desejada");
        int res = sn.nextInt();
        switch (res) {
            case 1:
                break;
            case 9:
                logout();
                break;
            default:
                break;
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
                data.addUser(owner);
                break;
            case 2:
                Client client = new Client(email,name,password,morada,birthDate,nif);
                data.addUser(client);
                break;
            default:
                break;
        }
        login();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
