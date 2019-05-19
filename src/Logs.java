
/**
 * Class to handle methods regarding the program's Logs.
 */

public class Logs
{
    StringBuilder stringBuilder;
    public Logs()
    {
        stringBuilder = new StringBuilder();
    }

    public void addToLogVehicle(Vehicle vehicle) {
        stringBuilder.append(vehicle);
    }

    public void addToLogUser(GeneralUser user){
        stringBuilder.append(user);
    }

    public void addToLogRent(Rent rent) {
        stringBuilder.append(rent);
    }

    public String toString() {
        return stringBuilder.toString();
    }
}
