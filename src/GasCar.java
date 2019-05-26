import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GasCar extends Vehicle
{
    private static double maxGasoleo = 1000;

    public static double getMaxGasoleo(){return maxGasoleo;}


    private double gasoleo;
    public GasCar(){
        super();
        this.gasoleo = 0;
    }


    public GasCar(String marca,String matricula,String nifOwner,int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,double gas){
        super(marca,matricula,nifOwner,averageSpeed,pricePerKm,consumptionPerKm,mPos);
        this.gasoleo = gas;
    }

    public GasCar(GasCar v){
        super(v);
        this.gasoleo = v.getGasoleo();
    }

    public double getGasoleo(){
        return this.gasoleo;
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        GasCar p = (GasCar) o;
        return(super.equals(o) && this.gasoleo == p.getGasoleo());
    }

    public GasCar clone(){ return new GasCar(this); }

    public boolean enoughAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double autonomia = distancia*super.getConsumptionPerKm();
        if(autonomia > this.gasoleo){
            this.warningGas();
            return false;
        }else {return true;}
    }
    
    public void abastece(){

        this.gasoleo = maxGasoleo;
        super.setNeedFuel(false);
    }

    public void warningGas(){
        double tmp = 0.1* maxGasoleo;
        if(gasoleo < tmp){
            super.setNeedFuel(true);
        }else{super.setNeedFuel(false);}
    }

    // retira a gota que gasta na viagem ao carro
    public void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        this.gasoleo = this.gasoleo - gastaGota;
    }

    public double getCurrentFuel(){
        return this.gasoleo;
    }

    public double getAutonomy(){
        return this.gasoleo / super.getConsumptionPerKm();
    }

    public String toString(){
        StringBuffer sb = new StringBuffer("Veiculo a Gasoleo/Gasolina ");
        sb.append(super.toString()).append(", ");
        sb.append("Combustivel: ").append(this.getGasoleo()).append(", ");
        return sb.toString();
    }

}
