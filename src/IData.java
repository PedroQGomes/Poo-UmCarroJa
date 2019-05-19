import java.io.*;

public interface IData {
    boolean loginOn(String username,String password);
    void addUser(GeneralUser owner);
    boolean addCar(Vehicle mVehicle);
    void populateData();
    void saveState();
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
        return mData;
    }
    boolean isLoggedIn();
    void logout();
    GeneralUser getLoggedInUser();
}
