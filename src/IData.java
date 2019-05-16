public interface IData {
    boolean checkClientExists();
    boolean checkOwnerExists();
    void addOwner();
    void addClient();
    void populateData();
    void saveState();
    void recoverState();
}
