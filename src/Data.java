/**
 * Class to hold all data relevant for runtime.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.util.HashMap;

public class Data implements IData, Serializable
{
   private HashMap<Integer, Owner> owners;//HashMap que contém todos os users, tendo o email como chave
   private HashMap<Integer, Client> clients;//HashMap que contém todos os users, tendo o email como chave

}
