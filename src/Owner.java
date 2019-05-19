import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    // instance variables
    private int rating;
    private Map<String,Vehicle> ListCar;

    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        this.rating = 0;
        ListCar = new HashMap<>();
    }

    public Owner(Owner own) {
        super(own);
        this.rating = own.getRating();
        this.ListCar = own.getListOfCars();
    }


    public Map<String,Vehicle> getListOfCars() {
        Map<String,Vehicle> temp = new HashMap<>();
        for(Map.Entry<String,Vehicle> a : this.ListCar.entrySet()){
            temp.put(a.getKey(),a.getValue().clone());
        }
        return temp;
    }


    public int getRating() { return this.rating;}

    public Owner clone() {
        return new Owner(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Owner p = (Owner) o;
        return(super.equals(o) && p.getRating() == this.rating && p.getListOfCars().equals(this.getListOfCars()));
    }

    public int hashCode(){
        int hash = 5;
        hash = 31*hash + this.rating;
        hash = 31*hash + this.ListCar.values().stream().mapToInt(Vehicle::hashCode).sum();
        return hash;

    }


    private void updateRating(int mRating) { // ASSUMINDO QUE ISTO É CHAMADO ANTES DE ADICIONAR O RENT A LISTA
        int tmp = rating * ListCar.size();
        tmp += mRating;
        tmp /= ListCar.size()+1;
        this.rating = tmp;
    }
    public void addVehicle(String mat,Vehicle a){
        this.ListCar.put(mat,a.clone());
    }

    // da uma lista com todos os carros com autonomia para fazer o aluguer
    public List<Vehicle> signalAvailable(Rent a){
        return this.ListCar.values().stream().filter(l -> l.enoughAutonomy(a.getPosicao())).map(Vehicle::clone).collect(Collectors.toList());
    }

    // abastece o carro
    public void fuelCar(String matricula){
        if(this.ListCar.containsKey(matricula)){
            this.ListCar.get(matricula).abastece();
        }
    }

    //muda o preço por km de um carro
    public void changePrice(String matricula,double price){
        if(this.ListCar.containsKey(matricula)){
            this.ListCar.get(matricula).setPrice(price);
        }
    }


    // o propriatario verifica se quer aceitar ou nao a proposta
    public boolean manageOfer(Rent a,String matricula){
        if(getRatingHistoryOfClient() > 30){
            acceptRent(a,matricula);
            return true;
        }
        else {return false;}
    }

    // aceita a proposta e executa-a
    public void acceptRent(Rent a,String matricula){
        this.ListCar.get(matricula).executeTrip(a);
    }

    //vai buscar o rating dos clientes
    public double getRatingHistoryOfClient(){
        //return this.ListCar.values().stream().mapToDouble(Vehicle::getRating).sum();
        return 100;
    }



}
