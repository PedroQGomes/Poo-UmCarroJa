

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Client extends GeneralUser
{
    private static final long serialVersionUID = 1934567219L;
    // instance variables - replace the example below with your own
    private Posicao pos;


    public Client(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        this.pos = new Posicao();
    }

    public Client(Client clt)
    {
        super(clt);
        this.pos = clt.getPos().clone();
    }

    public void setPos(Posicao mPos) {this.pos = mPos.clone();}

    public Posicao getPos() {
        return this.pos.clone();
    }
    
    public Client clone() {
        return new Client(this);
    }

    public List<Rent> getNoRatingList() {
        List <Rent> tmp = getRentList();
        List <Rent> tmpNoRate = new ArrayList<>();
        for(Rent r:tmp) {
            if(r.getRating() < 0) tmpNoRate.add(r);
        }
        return tmpNoRate;
    }



}
