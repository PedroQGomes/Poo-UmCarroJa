public interface IData {
    boolean loginOn(String username,String password);
    void addUser(GeneralUser owner);
    void populateData();
    void saveState();
    void recoverState();
    boolean isLoggedIn();
    void logout();
    GeneralUser getLoggedInUser();
}
