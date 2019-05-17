import java.io.FileInputStream;
import java.io.ObjectInputStream;

public interface IData {
    boolean loginOn(String username,String password);
    void addUser(GeneralUser owner);
    void populateData();
    void saveState();
    static Data recoverState() {
        Data mData = null;
        try {
            FileInputStream fis = new FileInputStream("data.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (Data) ois.readObject();
            System.out.println("Dados Lidos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (mData == null) mData = new Data();
        return mData;
    }
    boolean isLoggedIn();
    void logout();
    GeneralUser getLoggedInUser();
}
