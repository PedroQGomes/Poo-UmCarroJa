import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class Vehicle implements Serializable {
    private static final long serialVersionUID = 1214567219L;
    private int averageSpeed;
    private double pricePerKm,consumptionPerKm,rating;
    private int nRate;
    private Posicao pos;
    private String matricula,marca,nifOwner;
    private boolean needFuel;
    private List<Rent> alugueres;

    // construtores
    public  Vehicle(){
        this.marca = " ";
        this.averageSpeed = 10;
        this.rating = 0.0;
        this.pricePerKm = 2;
        this.consumptionPerKm = 2;
        this.pos.setPosX(0);
        this.pos.setPosY(0);
        this.matricula = " ";
        this.nifOwner = " ";
        this.needFuel = false;
        this.warningGas();
        this.nRate = 0;
        this.alugueres = new ArrayList<>();
    }
    
    public Vehicle(String marca,String matricula,String nifOwner,int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos){
        this.marca = marca;
        this.averageSpeed = averageSpeed;
        this.nifOwner = nifOwner;
        this.pricePerKm = pricePerKm;
        this.consumptionPerKm = consumptionPerKm;
        this.pos = mPos.clone();
        this.rating = 0.0;
        this.matricula = matricula;
        this.needFuel = false;
        this.warningGas();
        this.alugueres = new ArrayList<>();
        this.nRate = 0;
    }
    public Vehicle(Vehicle v){
        this.marca = v.getMarca();
        this.nifOwner = v.getNifOwner();
        this.averageSpeed = v.getAverageSpeed();
        this.rating = v.getRating();
        this.pricePerKm = v.getPricePerKm();
        this.consumptionPerKm = v.getConsumptionPerKm();
        this.pos = v.getPos();
        this.matricula = v.getMatricula();
        this.needFuel = getNeedFuel();
        this.warningGas();
        this.alugueres = v.getAlugueres();
        this.nRate = getNRate();
    }

    public int getNRate() {return this.nRate;}

    public String getMarca(){return this.marca;}

    public String getNifOwner() { return this.nifOwner; }

    public boolean getNeedFuel(){return this.needFuel;}

    public String getMatricula() {
        return this.matricula;
    }

    public List<Rent> getAlugueres(){
        return this.alugueres.stream().map(Rent::clone).collect(Collectors.toList());
    }

    public int getAverageSpeed() {
        return this.averageSpeed;
    }
    
    public double getPricePerKm(){
        return this.pricePerKm;
    }
    
    public double getConsumptionPerKm(){
        return this.consumptionPerKm;
    }
    
    public double getRating(){
        return rating;
    }

    public Posicao getPos(){
        return pos.clone();
    }

    public void setNeedFuel(boolean a){this.needFuel = a;}

    public void setPos(Posicao x){
        this.pos = x.clone();
    }

    public void setPrice(double price){this.pricePerKm = price;}

    public boolean equals(Object o){
        if(o == this)return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Vehicle p = (Vehicle) o;
        return(p.getAverageSpeed() == this.getAverageSpeed() && p.getConsumptionPerKm() == this.getConsumptionPerKm() && p.getPricePerKm() == this.getPricePerKm() && this.getConsumptionPerKm() == p.getConsumptionPerKm() && this.matricula == p.getMatricula());

    }
    public abstract Vehicle clone();

    public int hashCode(){
        int hash = 5;
        long aux1,aux2;
        hash = 31*hash + this.averageSpeed;
        hash = 31*hash + (int)this.rating;
        aux1 = Double.doubleToLongBits(pricePerKm);
        hash = 31*hash + (int)(aux1 ^ (aux1 >>> 32));
        aux2 = Double.doubleToLongBits(consumptionPerKm);
        hash = 31*hash + (int)(aux2 ^ (aux2 >>> 32));
        hash = 31*hash + marca.hashCode();
        hash = 31*hash + matricula.hashCode();
        hash = 31*hash + pos.hashCode();
        hash = 31*hash + this.alugueres.stream().mapToInt(Rent::hashCode).sum();
        return hash;
    }

    public void addRent(Rent a){
        this.alugueres.add(a.clone());
    }

    public void updateRating(double rate) {
        this.nRate++;
        this.rating = calculateRating(rate);
    }

    private double calculateRating(double rate) {
        double tmp = 0.0;
        tmp = this.rating * (this.nRate-1);
        tmp += rate;
        return tmp/this.nRate;
    }

    public double rentPrice(Posicao mPos){
        double distancia = this.pos.distancia(mPos);
        return (distancia*pricePerKm);
    }

    public abstract void warningGas();
    public abstract boolean enoughAutonomy(Posicao x);
    public abstract void updateAutonomy(Posicao x);
    public abstract void abastece();
    public abstract double getAutonomy();
    public abstract double getCurrentFuel();



    public double executeTrip(Rent a){ // retorna qnd custa a viagem-> 0 se nao for possivel
        Posicao x = a.getPosicao();
        if(!enoughAutonomy(x)) return 0; // verifica se o carro tem autonomia para realizar a viagem
        this.updateAutonomy(x); // update do combustivel
        this.setPos(x); // muda a posicao do carro
        this.warningGas(); // verifica se o carro está com pouca autonomia
        return (this.rentPrice(x)); // calcula e retorna o preço a pagar
    }

    public Duration rentTime(Posicao x){
        double dist = this.pos.distancia(x);
        double tempo = (dist / this.averageSpeed)*60;
        long time = (long) tempo;
        return(Duration.ofMinutes(time) );
    }

    public void showinfo(){
        for(Rent a : this.alugueres){
            System.out.println(a.toString());
        }
    }

    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("Marca: ").append(this.marca).append(", ");
        sb.append("Matricula: ").append(this.matricula).append(", ");
        sb.append("Posição: ").append(this.pos).append(", ");
        sb.append("Rating: ").append(this.rating).append(", ");
        sb.append("Preco: ").append(this.pricePerKm).append(", ");
        return sb.toString();
    }
}
