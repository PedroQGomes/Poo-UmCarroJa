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

    public void initMenu(Data data) {
        System.out.println("BEM VINDO A UMCARROJA");
        System.out.println("1 -> Login");
        System.out.println("2 -> Registar");
        Scanner sn = new Scanner(System.in);
        int res = sn.nextInt();
        switch(res) {
            case 1:
                login();
                break;
            case 2:
                register(data);
                break;
            default:
                break;
        }
    }
    private boolean login() {
    return false;
    }

    private void register(Data data) {
        Scanner sn = new Scanner(System.in);
        System.out.print("Registar como Owner(1) ou como Cliente(2)");
        int option = sn.nextInt();
        System.out.print("Email:");
        String email = sn.nextLine();
        System.out.print("Name:");
        String name = sn.nextLine();
        System.out.print("Password:");
        String password = sn.nextLine();
        System.out.print("Morada:");
        String morada = sn.nextLine();
        System.out.print("Nif:");
        String nif = sn.nextLine();
        System.out.print("Data de Nascimento (Formato: 15-01-2005):");
        String birthDateString = sn.nextLine();
        while(!(option == 1 || option == 2)) {
            option = sn.nextInt();
        }
        sn.close();
        LocalDate birthDate = LocalDate.of(2015,5,10);

        switch(option) {
            case 1:
                Owner owner = new Owner(email,name,password,morada,birthDate);
                data.addOwner(owner);
                break;
            case 2:
                Client client = new Client(email,name,password,morada,birthDate);
                data.addClient(client);
                break;
            default:
                break;
        }

    }
}
