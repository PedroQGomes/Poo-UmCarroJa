import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    private static final long serialVersionUID = 1236567219L;
    // instance variables
    private double rating;
    private Map<String,Vehicle> mapCar;

    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        this.rating = 0.0;
        mapCar = new HashMap<>();
    }

    public Owner(Owner own) {
        super(own);
        this.rating = own.getRating();
        this.mapCar = own.getMapCar();
    }


    public Map<String,Vehicle> getMapCar () {
        return this.mapCar.entrySet().stream().collect(Collectors.toMap(l->l.getKey(),l->l.getValue().clone()));
    }

    public List<Vehicle> getListCar() {
        List<Vehicle> tmp = new ArrayList<>();
        for(Map.Entry<String,Vehicle> a : this.mapCar.entrySet()) {
            tmp.add(a.getValue().clone());
        }
        return tmp;
    }

    public double getRating() { return this.rating;}

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
        hash = 31*hash + (int)this.rating;
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

    public void updateRating(double rate) {
        this.rating = calculateRating(rate);
    }

    private double calculateRating(double rate) {
        double tmp = 0.0;
        int nClientRate = getRentList().size();
        tmp = this.rating * (nClientRate-1);
        tmp += rate;
        return tmp/nClientRate;
    }




    // aceita a proposta e executa-a
    public void acceptRent(Rent a,String matricula){
        this.mapCar.get(matricula).executeTrip(a);
    }

    /*//vai buscar o rating dos clientes NAO NECESSARIO
    public double getRatingHistoryOfClient(){
        //return this.mapCar.values().stream().mapToDouble(Vehicle::getRating).sum();
        return 100;
    } */
    /*// o propriatario verifica se quer aceitar ou nao a proposta
    public boolean manageOfer(Rent a,String matricula){
        if(getRatingHistoryOfClient() > 30){
            acceptRent(a,matricula);
            return true;
        }
        else {return false;}
    } */

    // falta registar o preço de um aluguer
    //

}

