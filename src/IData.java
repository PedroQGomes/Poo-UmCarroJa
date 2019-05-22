import java.io.*;
import java.util.List;

public interface IData {
    boolean loginOn(String username,String password);
    void addUser(GeneralUser owner);
    boolean addCar(Vehicle mVehicle);
    void createRent(Vehicle rentVehicle, Posicao posicao);
    void acceptRent(Rent rent);
    void giveRate(Rent rent , double rating);
    void populateData();
    void saveState();
    void initLog();
    void updateUser(GeneralUser user);
    public List<Vehicle> getAllAvailableVehicles ();
    public List<Rent> getPendingRentList();
    public List<Rent> getPendingRateList();
    static Data recoverState() {
        Data mData = null;
        try {
            FileInputStream fis = new FileInputStream("data.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (Data) ois.readObject();
            System.out.println("Dados Lidos");
        } catch (InvalidClassException e) {
            System.out.println(e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        if (mData == null) mData = new Data();
        else mData.initLog();
        return mData;
    }
    boolean isLoggedIn();
    void logout();
    GeneralUser getLoggedInUser();
}
