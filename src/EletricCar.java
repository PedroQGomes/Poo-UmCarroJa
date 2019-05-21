import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EletricCar extends Vehicle {
    private static double maxBateria = 1000;

    public static  double getMaxBateria(){return maxBateria;}

    private double bateria;

    public EletricCar(){
        super();
        this.bateria = 0.0;
    }
    public EletricCar(String marca,String matricula,String nifOwner,int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,double bateria){
        super(marca,matricula,nifOwner,averageSpeed,pricePerKm,consumptionPerKm,mPos);
        this.bateria = bateria;
    }

    public EletricCar(EletricCar c){
        super(c);
        this.bateria = c.getBateria();

    }

    public double getBateria(){
        return this.bateria;
    }

    public EletricCar clone(){
        return new EletricCar(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        EletricCar p = (EletricCar) o;
        return (super.equals(o) && this.bateria == p.getBateria());
    }

    public boolean enoughAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double autonomia = distancia*super.getConsumptionPerKm();
        if(autonomia > this.bateria){
            this.warningGas();
            return false;
        }else {return true;}
    }

    public void abastece(){
        this.bateria = maxBateria;
    }

    public void warningGas(){
        double tmp = 0.1* maxBateria;
        if(bateria < tmp){
            super.setNeedFuel(true);
        }
    }

    // retira a gota que gasta na viagem ao carro
    public void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        this.bateria = this.bateria - gastaGota;
    }

    public double getCurrentFuel(){
        return this.bateria;
    }

    public double getAutonomy(){
        return this.bateria / super.getConsumptionPerKm();
    }


    public String toString(){
        StringBuffer sb = new StringBuffer("Veiculo Eletrico ");
        sb.append("Marca: ").append(super.getMarca()).append(", ");
        sb.append("Matricula: ").append(super.getMatricula()).append(", ");
        sb.append("Posição: ").append(super.getPos()).append(", ");
        sb.append("Rating: ").append(super.getRating()).append(", ");
        sb.append("Bateria: ").append(this.getBateria()).append(", ");

        return sb.toString();
    }

    public double getAutonomia(){
        return this.bateria;
    }

}
