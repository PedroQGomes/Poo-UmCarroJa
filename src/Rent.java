/**
 * Write a description of class Rent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Rent implements Serializable
{
    private static final long serialVersionUID = 1994567219L;


    private LocalDateTime date;
    private Duration rentTime;
    private double price;
    private Posicao pos; // posicao final



    private double distancia;
    private String nif;
    private String matricula;


    public Rent(Duration _rentTime, double _price,Posicao poss,String niff,String matricula,double distancia){
        this.date = LocalDateTime.now();
        this.rentTime = _rentTime;
        this.price = _price;
        this.pos = poss;
        this.nif = niff;
        this.matricula = matricula;
        this.distancia = distancia;
    }
    public Rent(Rent r) {
        this.date = r.getDate();
        this.rentTime = r.getRentTime();
        this.price = r.getPrice();
        this.nif = r.getNif();
        this.pos = r.getPosicao().clone();
        this.matricula= r.getMatricula();
        this.distancia = r.getDistancia();
    }

    public double getDistancia() {
        return this.distancia;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public Duration getRentTime() {
        return this.rentTime;
    }

    public double getPrice() {
        return this.price;
    }

    public Posicao getPosicao() { return this.pos.clone();}

    public String getNif() { return this.nif;}

    public String getMatricula(){return this.matricula;}


    public boolean equals(Object o){
        if(this == o)return true;
        if((o == null) || this.getClass() != o.getClass())return false;
        Rent a = (Rent) o;
        return(a.getNif().equals(this.getNif()) && this.getMatricula().equals(a.getMatricula()) && a.getDate() == this.getDate()  && this.getPosicao() == a.getPosicao() && this.getDistancia() == a.getDistancia() && this.getPrice() == a.getPrice() && this.rentTime == a.getRentTime());
    }

    public Rent clone(){
        return new Rent(this);
    }

    public int hashCode(){
        int hash = 5;
        long aux1,aux2;
        aux1 = Double.doubleToLongBits(this.price);
        hash = 31*hash + (int)(aux1 ^ (aux1 >>> 32));
        hash = 31*hash + this.matricula.hashCode();
        hash = 31*hash + this.nif.hashCode();
        hash = 31*hash + this.pos.hashCode();
        hash = 31*hash + this.date.hashCode();
        hash = 31*hash + this.rentTime.hashCode();
        
        return hash;
    }

    // da o carro mais perto de um dado ponto de uma dada lista
    private static Vehicle getNearCar(List<Vehicle> list, Posicao p) throws semVeiculosException{
        if(list.isEmpty()){throw new semVeiculosException("Sem veiculos");}
        double tmpDist = 0.0;
        double tmpfinal = Double.MAX_VALUE;
        Vehicle end = null;

        for(Vehicle tmp: list){
            tmpDist = tmp.getPos().distancia(p);
            if(tmpfinal > tmpDist){
                tmpfinal = tmpDist;
                end = tmp;
            }
        }
        return end;
    }

    // da o carro mais barato de uma dada lista
    private static Vehicle getCheapestCar(List<Vehicle> list) throws semVeiculosException{
        if(list.isEmpty()){throw new semVeiculosException("Sem veiculos");}
        double tmpPrice = 0;
        double finalPrice = Double.MAX_VALUE;
        Vehicle chosen = null;
        for(Vehicle v: list){
            tmpPrice = v.getPricePerKm();
            if(finalPrice > tmpPrice){
                finalPrice = tmpPrice;
                chosen = v;
            }
        }
        return chosen;
    }


    public static Vehicle RentCheapestCarOfType(UMCarroJa m , Posicao toWhere, Class<? extends Vehicle> a) throws semVeiculosException {
    Client clt = null;
    if(m.getLoggedInUser() instanceof Client) {
        clt = (Client) m.getLoggedInUser();
    }
    if(clt != null){
        Posicao mPos = clt.getPos();
        return Rent.getCheapestCar(m.getListOfCarType(a,toWhere,mPos));
    }
    return null;
}


    //solicita o carro mais barato de qualquer tipo
    public static Vehicle RentCheapestCar(UMCarroJa m , Posicao toWhere) throws semVeiculosException {
        Client clt = null;
        if(m.getLoggedInUser() instanceof Client) {
            clt = (Client) m.getLoggedInUser();
        }
        if(clt != null){
            return Rent.getCheapestCar(m.getAllAvailableVehiclesWithFuelToTripAndCloseToClientThanToPosition(toWhere,clt.getPos()));
        }
        return null;

    }

    public static Vehicle RentNearCar(UMCarroJa m , Posicao toWhere) throws semVeiculosException {
        Client clt = null;
        if(m.getLoggedInUser() instanceof Client) {
            clt = (Client) m.getLoggedInUser();
        }
        if(clt != null){
            Posicao mPos = clt.getPos();
            return Rent.getNearCar(m.getAllAvailableVehiclesWithFuelToTripAndCloseToClientThanToPosition(toWhere,mPos),mPos);
        }
        return null;
    }

    public static Vehicle RentNearCarOfType(UMCarroJa m , Posicao toWhere, Class<? extends Vehicle> a) throws semVeiculosException {
        Client clt = null;
        if(m.getLoggedInUser() instanceof Client) {
            clt = (Client) m.getLoggedInUser();
        }
        if(clt != null){
            Posicao mPos = clt.getPos();
            return Rent.getNearCar(m.getListOfCarType(a,toWhere,mPos),mPos);
        }
        return null;
    }

    public static Vehicle RentCarwithAutonomy(UMCarroJa m ,double autonomy , Posicao toWhere) throws semVeiculosException {
        Client clt = null;
        if(m.getLoggedInUser() instanceof Client) {
            clt = (Client) m.getLoggedInUser();
        }
        if(clt != null){

            return Rent.getRentCarWithAutonomy(m.getAllAvailableVehiclesWithFuelToTripAndCloseToClientThanToPosition(toWhere,clt.getPos()),autonomy,toWhere);
        }
        return null;
    }


    // Solicita o carro mais barato dentro de uma certa distancia
    public static Vehicle getRentCarWithAutonomy(List<Vehicle> list , double autonomy, Posicao toWhere) throws  semVeiculosException{
        List<Vehicle> carsWithAutonomy = list.stream().filter(v -> v.getAutonomy() > autonomy).collect(Collectors.toList());
        return getNearCar(carsWithAutonomy, toWhere);

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Aluguer: ");
        sb.append("Matricula: ").append(this.matricula).append(", ");
        sb.append("Nif do cliente: ").append(this.getNif()).append(", ");
        sb.append("Duração: ").append(this.getRentTime().toMinutes()).append("min , ");
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        sb.append("Distância: ").append(decimalFormat.format(this.getDistancia())).append("km , ");
        sb.append("Preço: ").append(this.getPrice()).append(", ");
        sb.append("em ").append(this.getDate());
        return sb.toString();
    }

}




