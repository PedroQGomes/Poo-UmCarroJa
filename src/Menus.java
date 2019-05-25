import java.util.*;
import java.util.stream.Collectors;


public class Menus
{
    private List<String> menuOptions;
    private int choice;

    public Menus(){
        this.menuOptions = new ArrayList<>();
        this.choice = 0;
    }


    public Menus(String[] opcoes){
        this.menuOptions = Arrays.asList(opcoes);
        this.choice = 0;

    }

    public int readOption(){
       Scanner s = new Scanner(System.in);
       int op = 0;
       try {
           op = s.nextInt();
       }
       catch (InputMismatchException e){op = 0;}

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
            System.out.print(" - ");
            System.out.println(s);
            count++;
        }
    }

    public void printMenu(int x){
        System.out.print(this.menuOptions.get(x));
    }

    public void executeMenu(){
        printMenu();
        this.choice = readOption();
    }


    public int getChoice(){
        return this.choice;
    }
}

