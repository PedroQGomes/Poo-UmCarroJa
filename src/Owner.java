import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    private static final long serialVersionUID = 1236567219L;
    // instance variables
    private Rating rating;
    private Map<String,Vehicle> mapCar;

    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        this.rating = new Rating();
        mapCar = new HashMap<>();
    }

    public Owner(Owner own) {
        super(own);
        this.rating = own.getRating();
        this.mapCar = own.getMapCar();
    }


    public Map<String,Vehicle> getMapCar () {
        Map<String,Vehicle> temp = new HashMap<>();
        for(Map.Entry<String,Vehicle> a : this.mapCar.entrySet()){
            temp.put(a.getKey(),a.getValue().clone());
        }
        return temp;
    }
    public List<Vehicle> getListCar() {
        List<Vehicle> tmp = new ArrayList<>();
        for(Map.Entry<String,Vehicle> a : this.mapCar.entrySet()) {
            tmp.add(a.getValue().clone());
        }
        return tmp;
    }

    public Rating getRating() { return this.rating.clone();}

    public Owner clone() {
        return new Owner(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Owner p = (Owner) o;
        return(super.equals(o) && p.getRating() == this.rating && p.getMapCar().equals(this.getMapCar()));
    }

    public int hashCode(){
        int hash = 5;
        hash = 31*hash + (int)this.rating.getRating();
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

    // da uma lista com todos os carros com autonomia para fazer o aluguer
    public List<Vehicle> signalAvailable(Rent a){
        return this.mapCar.values().stream().filter(l -> l.enoughAutonomy(a.getPosicao())).map(Vehicle::clone).collect(Collectors.toList());
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

    /* NAO NECESSARIO
    // o propriatario verifica se quer aceitar ou nao a proposta
    public boolean manageOfer(Rent a,String matricula){
        if(getRatingHistoryOfClient() > 30){
            acceptRent(a,matricula);
            return true;
        }
        else {return false;}
    } */

    // aceita a proposta e executa-a
    public void acceptRent(Rent a,String matricula){
        this.mapCar.get(matricula).executeTrip(a);
    }

    //vai buscar o rating dos clientes
    /* FUNCAO AMBIGUA
    public double getRatingHistoryOfClient(){
        //return this.mapCar.values().stream().mapToDouble(Vehicle::getRating).sum();
        return 100;
    } */



}
