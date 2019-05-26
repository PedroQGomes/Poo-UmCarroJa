import java.io.*;
import java.time.LocalDateTime;

/**
 * Class to handle methods regarding the program's Logs.
 */

public class Logs
{
    private PrintWriter printWriter;


    public Logs() throws IOException {
        String filename = LocalDateTime.now().toString();
        //String filename = "log";
        FileWriter fw;
        File file = new File(filename);
        if(file.exists()) {
            fw = new FileWriter(file,true);
        } else {
            file.createNewFile();
            fw = new FileWriter(file);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        printWriter = new PrintWriter(bw);

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
        printWriter.println(stringBuilder.toString());
    }

    public void flushLog() {
        printWriter.flush();
    }

}
