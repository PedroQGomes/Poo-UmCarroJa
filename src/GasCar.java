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
    private void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        this.gasoleo = this.gasoleo - gastaGota;
    }


    public double exacuteTrip(Rent a){ // retorna qnd custa a viagem-> 0 se nao for possivel
        Posicao x = a.getPosicao();
        if(enoughAutonomy(x) == false) return 0; // verifica se o carro tem autonomia para realizar a viagem
        this.updateAutonomy(x); // update do combustivel
        super.setPos(x); // muda a posicao do carro
        super.addRent(a); // adiciona o aluguer a lista de alugueres do carro
        this.warningGas(); // verifica se o carro está com pouca autonomia
        double preco = super.rentPrice(a); // calcula e retorna o preço a pagar
        return preco;
    }



}
