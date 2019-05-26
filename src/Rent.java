/**
 * Write a description of class Rent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import java.io.Serializable;
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
    private String nif;
    private String matricula;


    public Rent(Duration _rentTime, double _price,Posicao poss,String niff,String matricula){
        this.date = LocalDateTime.now();
        this.rentTime = _rentTime;
        this.price = _price;
        this.pos = poss;
        this.nif = niff;
        this.matricula = matricula;
    }
    public Rent(Rent r) {
        this.date = r.getDate();
        this.rentTime = r.getRentTime();
        this.price = r.getPrice();
        this.nif = r.getNif();
        this.pos = r.getPosicao().clone();
        this.matricula= r.getMatricula();
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
        return(a.getNif().equals(this.getNif()) && this.getMatricula().equals(a.getMatricula()) && a.getDate() == this.getDate()  && this.getPosicao() == a.getPosicao());
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
    public static Vehicle getNearCar(List<Vehicle> list, Posicao p) throws semVeiculosException{
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
    public static Vehicle getCheapestCar(List<Vehicle> list) throws semVeiculosException{
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

    //solicitar o vaiculo mais perto de um certo tipo de carro
    public static Vehicle RentNearCarOfType(Class<? extends Vehicle> a, UMCarroJa d) throws semVeiculosException{
        List<Vehicle> tmp = d.getListOfCarType(a);
        Client clt = (Client) d.getLoggedInUser();
        Vehicle chosen = getNearCar(tmp,clt.getPos());
        return chosen;
    }

    // solicitar o carro com autonomia desejada
    // se tiver varios escolhe o mais perto do cliente
    public static Vehicle RentCarwithAutonomy(double autonomia, UMCarroJa d) throws semVeiculosException{
        List<Vehicle> tmp;
        tmp = d.getAllAvailableVehicles();
        Client clt = (Client) d.getLoggedInUser();
        return (getNearCar(tmp,clt.getPos()));
    }


    // solicita o carro mais barato de um certo tipo de combustivel
    public static Vehicle RentCheapestCarOfType(Class<? extends Vehicle> a, UMCarroJa d) throws semVeiculosException{
        List<Vehicle> tmp = d.getListOfCarType(a);
        Client clt = (Client) d.getLoggedInUser();
        return (getNearCar(tmp,clt.getPos()));
    }


    //solicita o carro mais barato de qualquer tipo
    public static Vehicle RentCheapestCar(UMCarroJa p) throws semVeiculosException {
        List<Vehicle> tmp = p.getAllAvailableVehicles();
        return getCheapestCar(tmp);
    }


    // Solicita o carro mais barato dentro de uma certa distancia
    public static Vehicle RentCheapestCarOfDistance(double dist, UMCarroJa d) throws  semVeiculosException{
        List<Vehicle> tmp = d.getAllAvailableVehicles();
        Client clt = (Client) d.getLoggedInUser();
        return getCheapestCar(tmp.stream().filter(l->l.getPos().distancia(clt.getPos()) < dist).collect(Collectors.toList()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Aluguer: ");
        sb.append("Matricula: ").append(this.matricula).append(", ");
        sb.append("Nif do cliente: ").append(this.getNif()).append(", ");
        sb.append("Duração: ").append(this.getRentTime().toMinutes()).append("min , ");
        sb.append("Preço: ").append(this.getPrice()).append(", ");
        sb.append("em ").append(this.getDate());
        return sb.toString();
    }

}




