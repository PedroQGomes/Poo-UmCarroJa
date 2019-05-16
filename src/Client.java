/**
 * Write a description of class Client here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class Client extends GeneralUser
{
    // instance variables - replace the example below with your own
    private Posicao pos;
    /**
     * Constructor for objects of class Client
     */
    public Client(String _email, String _name, String _password, String _morada, LocalDate _birthDate)
    {
        super(_email,_name,_password,_morada,_birthDate);
        this.pos = new Posicao();
    }

    public Client(Client clt)
    {
        super(clt);
        this.pos = clt.getPos().clone();
    }
    
    public Posicao getPos() {
        return this.pos.clone();
    }
    
    public Client clone() {
        return new Client(this);
    }

    
}
