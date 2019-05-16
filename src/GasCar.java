import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GasCar extends Vehicle
{
    private static double maxGasoleo = 30;

    public static double getMaxGasoleo(){return maxGasoleo;}


    private double gasoleo;
    public GasCar(){
        super();
        this.gasoleo = 0;
    }


    public GasCar(int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,String nome,List<Rent> a,boolean disp,boolean fuel,double gas){
        super(averageSpeed,pricePerKm,consumptionPerKm,mPos,nome,a,disp,fuel);
        this.gasoleo = gas;
    }

    public GasCar(GasCar v){
        super(v);
        this.gasoleo = v.getGasoleo();
    }

    public double getGasoleo(){
        return this.gasoleo;
    }

    public GasCar clone(){ return new GasCar(this); }

    public boolean enoughAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double autonomia = distancia*super.getConsumptionPerKm();
        if(autonomia > gasoleo){return false;}else {return true;}
    }
    
    public void abastece(){
        this.gasoleo = maxGasoleo;
    }

    public void warningGas(){
        double tmp = 0.1* maxGasoleo;
        if(gasoleo < tmp){
            super.setNeedFuel(true);
        }
    }

    // retira a gota que gasta na viagem ao carro
    public void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        this.gasoleo = this.gasoleo - gastaGota;
    }




}
