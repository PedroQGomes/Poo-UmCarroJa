import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HybridCar extends Vehicle{
    private static double maxGasoleo = 15;
    private static double maxBateria = 15;

    public static double getMaxBateria(){return maxBateria;}
    public static double getMaxGasoleo(){return maxGasoleo;}


    private double gasoleo;
    private double bateria;

    public HybridCar(){
        super();
        this.gasoleo = 0;
        this.bateria = 0;
    }


    public HybridCar(int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,String nome,List<Rent> a,boolean disp,boolean fuel,double bat,double gas){
        super(averageSpeed,pricePerKm,consumptionPerKm,mPos,nome,a,disp,fuel);
        this.gasoleo = gas;
        this.bateria = bat;
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
        double total = gasoleo + bateria;
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




}
