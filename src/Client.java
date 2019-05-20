

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

    // da o carro mais perto de um dado ponto de uma dada lista
    private Vehicle getNearCar(List<Vehicle> list,Posicao p) throws semVeiculosException{
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

    // da o carro mais barato de uma dada lista
    private Vehicle getcheapestCar(List<Vehicle> list) throws semVeiculosException{
        if(list.isEmpty()){throw new semVeiculosException("Sem veiculos");}
        double tmpPrice =0;
        double finalPrice = -1;
        Vehicle chosen = null;
        for(Vehicle v: list){
            tmpPrice = v.getPricePerKm();
            if(finalPrice < 0){
                finalPrice = tmpPrice;
                chosen = v;
            }else{
                if(finalPrice > tmpPrice){
                    finalPrice = tmpPrice;
                    chosen = v;
                }
            }
        }
        return chosen;
    }

    //solicitar o vaiculo mais perto de um certo tipo de carro
    public Vehicle RentNearCarOfType(Vehicle a,Data p) throws semVeiculosException{
        List<Vehicle> tmp = p.getListOfCarType(a);
        Vehicle chosen = getNearCar(tmp,this.pos);
        return chosen;
    }

    // solicitar o carro com autonomia desejada
    // se tiver varios escolhe o mais perto do cliente
    public Vehicle RentCarwithAutonomy(double autonomia,Data p) throws semVeiculosException{
        List<Vehicle> tmp;
        tmp = p.getAllVehicles().values().stream().filter(l-> l.getAutonomia() == autonomia).collect(Collectors.toList());
        return (getNearCar(tmp,this.pos));
    }


    // solicita o carro mais barato de um certo tipo de combustivel
    public Vehicle RentCheapestCarOfType(Vehicle a,Data p) throws semVeiculosException{
        List<Vehicle> tmp = p.getListOfCarType(a);
        return getcheapestCar(tmp);
    }


    //solicita o carro mais barato de qualquer tipo
    public Vehicle RentCheapestCar(Data p) throws semVeiculosException {
        List<Vehicle> tmp = p.getAllVehicles().values().stream().collect(Collectors.toList());
        return getcheapestCar(tmp);
    }


    // Solicita o carro mais barato dentro de uma certa distancia
    public Vehicle RentCheapestCarOfDistance(double dist,Data p) throws  semVeiculosException{
        List<Vehicle> tmp = p.getAllVehicles().values().stream().collect(Collectors.toList());
        return getcheapestCar(tmp.stream().filter(l->l.getPos().distancia(this.pos) < dist).collect(Collectors.toList()));
    }

}
