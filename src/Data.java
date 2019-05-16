/**
 * Class to hold all data relevant for runtime.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Data implements  Serializable ,IData
{
   private Map<Integer, Owner> owners;//HashMap que contém todos os users, tendo o email como chave
   private Map<Integer, Client> clients;//HashMap que contém todos os users, tendo o email como chave

   public Data() {
      owners = new HashMap<>();
      clients = new HashMap<>();
   }

   public boolean checkClientExists (String nifClient) {
      return false;
   }

   public boolean checkOwnerExists (String nifOwner) {
      return false;
   }

   public void addOwner (Owner owner) {

   }

   public void addClient (Client client) {

   }

   public void populateData ( ) {

   }

   public void saveState ( ) {

   }

   public void recoverState ( ) {

   }
}
