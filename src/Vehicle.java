import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public abstract class Vehicle implements Serializable {
    private int averageSpeed,rating;
    private double pricePerKm,consumptionPerKm;
    private Posicao pos;
    private String matricula;
    private boolean disponiblidade;
    private boolean needFuel;
    private List<Rent> alugueres;

    // construtores
    public  Vehicle(){
        this.averageSpeed = 10;
        this.rating = 0;
        this.pricePerKm = 2;
        this.consumptionPerKm = 2;
        this.pos.setPosX(0);
        this.pos.setPosY(0);
        this.matricula = " ";
        this.disponiblidade = true;
        this.needFuel = false;
        this.alugueres = new ArrayList<>();
    }
    
    public Vehicle(int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,String nome,List<Rent> a,boolean disp,boolean fuel){
        this.averageSpeed = averageSpeed;
        this.pricePerKm = pricePerKm;
        this.consumptionPerKm = consumptionPerKm;
        this.pos = mPos.clone();
        this.rating = 0;
        this.matricula = nome;
        this.disponiblidade = disp;
        this.needFuel = fuel;
        this.alugueres = new ArrayList<>();
        for(Rent l :a){this.alugueres.add(l.clone());}
    }
    public Vehicle(Vehicle v){
        this.averageSpeed = v.getAverageSpeed();
        this.rating = v.getRating();
        this.pricePerKm = v.getPricePerKm();
        this.consumptionPerKm = v.getConsumptionPerKm();
        this.pos = v.getPos();
        this.matricula = v.getMatricula();
        this.disponiblidade = v.getDisponiblidade();
        this.needFuel = getNeedFuel();
        this.alugueres = v.getAlugueres();
    }

    public boolean getNeedFuel(){return this.needFuel;}

    public boolean getDisponiblidade(){return this.disponiblidade;}

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
    
    public int getRating(){
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

    public void addRent(Rent a){
        this.alugueres.add(a.clone());
    }

    public void addRating(int a){
        int count = this.alugueres.size();
        int tmpRating = this.rating*count;
        tmpRating = tmpRating + a;
        count++;
        this.rating = tmpRating/count;
    }

    public double rentPrice(Rent a){
        double distancia = this.pos.distancia(a.getPosicao());
        double price = distancia*pricePerKm;
        return price;
    }

    public abstract void warningGas();
    public abstract boolean enoughAutonomy(Posicao x);
    public abstract void updateAutonomy(Posicao x);
    public abstract void abastece();

    public double exacuteTrip(Rent a){ // retorna qnd custa a viagem-> 0 se nao for possivel
        Posicao x = a.getPosicao();
        if(enoughAutonomy(x) == false) return 0; // verifica se o carro tem autonomia para realizar a viagem
        this.updateAutonomy(x); // update do combustivel
        this.setPos(x); // muda a posicao do carro
        this.addRent(a); // adiciona o aluguer a lista de alugueres do carro
        this.warningGas(); // verifica se o carro está com pouca autonomia
        double preco = this.rentPrice(a); // calcula e retorna o preço a pagar
        return preco;
    }

}
