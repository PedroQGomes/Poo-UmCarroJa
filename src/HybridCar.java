import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HybridCar extends Vehicle{
    private static double maxGasoleo = 500;
    private static double maxBateria = 500;

    public static double getMaxBateria(){return maxBateria;}
    public static double getMaxGasoleo(){return maxGasoleo;}


    private double gasoleo;
    private double bateria;

    public HybridCar(){
        super();
        this.gasoleo = 0;
        this.bateria = 0;
    }


    public HybridCar(String marca,String nome,int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,double fuel){
        super(marca,nome,averageSpeed,pricePerKm,consumptionPerKm,mPos);
        this.gasoleo = 2*(fuel/3);
        this.bateria = fuel/3;
    }

    public HybridCar(HybridCar v){
        super(v);
        this.bateria = v.getBateria();
        this.gasoleo = v.getGasoleo();
    }

    public double getBateria(){return this.bateria;}
    public double getGasoleo(){return this.gasoleo;}

    public HybridCar clone(){return new HybridCar(this);}

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        HybridCar p = (HybridCar) o;
        return(super.equals(o) && this.bateria == p.getBateria() && this.gasoleo == p.getGasoleo());
    }

    public boolean enoughAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double autonomia = distancia*super.getConsumptionPerKm();
        double total = this.gasoleo + this.bateria;
        if(autonomia > total){
            this.warningGas();
            return false;
        }else {return true;}
    }

    public void abastece(){
        this.gasoleo = maxGasoleo;
        this.bateria = maxBateria;
    }

    public void warningGas(){
        double total = maxBateria + maxGasoleo;
        double autonomia = bateria + gasoleo;
        double tmp = 0.1* total;
        if(autonomia < tmp){
            super.setNeedFuel(true);
        }
    }

    // retira a gota que gasta na viagem ao carro
    public void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        if(gastaGota > this.gasoleo){
            this.gasoleo = 0.0;
            gastaGota = gastaGota - this.gasoleo;
            this.bateria = this.bateria - gastaGota;
        }else{
            this.gasoleo = this.gasoleo - gastaGota;
        }
    }

    public double getCurrentFuel(){
        return (this.bateria + this.gasoleo);
    }

    public double getAutonomy(){
        double total = this.bateria + this.gasoleo;
        return total / super.getConsumptionPerKm();
    }

    public String toString(){
        StringBuffer sb = new StringBuffer("Veiculo Hibrido ");
        sb.append("Marca: ").append(super.getMarca()).append(", ");
        sb.append("Matricula: ").append(super.getMatricula()).append(", ");
        sb.append("Posição: ").append(super.getPos()).append(", ");
        sb.append("Rating: ").append(super.getRating()).append(", ");
        sb.append("Combustivel: ").append(this.getGasoleo()).append(", ");
        sb.append("Bateria: ").append(this.getBateria()).append(", ");

        return sb.toString();
    }


}
