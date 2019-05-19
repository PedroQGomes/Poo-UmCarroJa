/**
 * Write a description of class Client here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
            if(r.getRating() == 0.0) tmpNoRate.add(r);
        }
        return tmpNoRate;
    }

    public Vehicle getNearCar(List<Vehicle> list,Posicao p) throws semVeiculosException{
    if(list.isEmpty()){throw new semVeiculosException("Sem veiculos");}
    double tmpDist = 0;
    double tmpfinal = (-1);
    Vehicle end = null;

    for(Vehicle tmp: list){
        tmpDist = tmp.getPos().distancia(p);
        if(tmpfinal < 0){
            tmpfinal = tmpDist;
            end = tmp;
        }else {
            if(tmpfinal > tmpDist){
                tmpfinal = tmpDist;
                end = tmp;
            }
        }
    }
    return end;
}


    public void RentNearCarOfType(Data p,Vehicle a){
        List<Vehicle> tmp = p.getListOfCarType(a);
        Vehicle chosen = getNearCar(tmp,this.pos);

    }



}
