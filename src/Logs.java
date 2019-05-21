import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Class to handle methods regarding the program's Logs.
 */

public class Logs
{
    private PrintWriter printWriter;
    private String filename;

    public Logs()
    {
        //filename = LocalDateTime.now().toString();
        filename = "log";
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            printWriter = new PrintWriter(bw);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToLogVehicle(Vehicle vehicle) {
        StringBuilder stringBuilder = new StringBuilder("Veiculo criado em ");
        stringBuilder.append(LocalDateTime.now());
        stringBuilder.append(" com a Matrícula ");
        stringBuilder.append(vehicle.getMatricula());
        stringBuilder.append(" pertencente ao nif ");
        stringBuilder.append(vehicle.getNifOwner());
        printWriter.println(stringBuilder.toString());
    }

    public void addToLogUser(GeneralUser user){
        StringBuilder stringBuilder = new StringBuilder("Utilizador criado em ");
        stringBuilder.append(LocalDateTime.now());
        stringBuilder.append(" com o nome ");
        stringBuilder.append(user.getName());
        stringBuilder.append(" , email ");
        stringBuilder.append(user.getEmail());
        stringBuilder.append(" , do tipo ");
        stringBuilder.append(user.getClass().toString());
        stringBuilder.append(" , e com o nif ");
        stringBuilder.append(user.getNif());
        printWriter.println(stringBuilder.toString());
    }

    public void addToLogRent(Rent rent) {
        StringBuilder stringBuilder = new StringBuilder("Aluguer criado em ");
        stringBuilder.append(LocalDateTime.now());
        stringBuilder.append(" com a matricula  ");
        stringBuilder.append(rent.getMatricula());
        stringBuilder.append(" , nif ");
        stringBuilder.append(rent.getNif());
        stringBuilder.append(" , e com o rating ");
        stringBuilder.append(rent.getRating());
        printWriter.println(stringBuilder.toString());
    }

    public void flushLog() {
        printWriter.flush();
    }

}
