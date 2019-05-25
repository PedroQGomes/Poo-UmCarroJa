import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    private static final long serialVersionUID = 1236567219L;
    // instance variables

    private Map<String,Vehicle> mapCar;

    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        mapCar = new HashMap<>();
    }

    public Owner(Owner own) {
        super(own);
        this.mapCar = own.getMapCar();
    }


    public Map<String,Vehicle> getMapCar () {
        return this.mapCar.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, l->l.getValue().clone()));
    }

    public List<Vehicle> getListCar() {
        List<Vehicle> tmp = new ArrayList<>();
        for(Map.Entry<String,Vehicle> a : this.mapCar.entrySet()) {
            tmp.add(a.getValue().clone());
        }
        return tmp;
    }


    public Owner clone() {
        return new Owner(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Owner p = (Owner) o;
        return(super.equals(o) && p.getMapCar().equals(this.getMapCar()));
    }

    public int hashCode(){
        int hash = 5;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + this.mapCar.values().stream().mapToInt(Vehicle::hashCode).sum();
        return hash;
    }

    public boolean addVehicle(String mat,Vehicle a){
        if(!(mapCar.containsKey(mat))) {
            this.mapCar.put(mat,a.clone());
            return true;
        }
        return false;
    }


    // abastece o carro
    public void fuelCar(String matricula){
        if(this.mapCar.containsKey(matricula)){
            this.mapCar.get(matricula).abastece();
        }
    }

    //muda o preço por km de um carro
    public void changePrice(String matricula,double price){
        if(this.mapCar.containsKey(matricula)){
            this.mapCar.get(matricula).setPrice(price);
        }
    }

    // aceita a proposta e executa-a
    public void acceptRent(Rent a){
        if(a != null)
        this.mapCar.get(a.getMatricula()).executeTrip(a);
    }


}

