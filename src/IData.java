public interface IData {
    boolean checkClientExists(String nifClient);
    boolean checkOwnerExists(String nifOwner);
    void addOwner(Owner owner);
    void addClient(Client client);
    void populateData();
    void saveState();
    void recoverState();
}
