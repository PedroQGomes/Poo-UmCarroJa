import java.util.List;

public interface IUMCarroJa {
    boolean loginOn(String username,String password);
    void addUser (GeneralUser generalUser) throws utilizadorJaExiste;
    boolean addCar(Vehicle mVehicle);
    void createRent(Vehicle rentVehicle, Posicao posicao);
    void giveRate(Rent rent , double rating);
    void giveRate(Rent rent, double rate, double rateCar);
    void giveRateClient(Rent rent,double rating);
    void saveState();
    void initLog();
    void updateUser(GeneralUser user);
    void updateVehicle(Vehicle mVehicle);
    List<Vehicle> getAllAvailableVehicles ();
    List<Rent> getPendingRateList(String nif);
    boolean isLoggedIn();
    void logout();
    GeneralUser getLoggedInUser();
}
