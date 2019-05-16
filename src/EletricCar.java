import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EletricCar extends Vehicle {
    private static double maxBateria = 30;

    public static  double getMaxBateria(){return maxBateria;}

    private double bateria;

    public EletricCar(){
        super();
        this.bateria = 0.0;
    }
    public EletricCar(int averageSpeed,double pricePerKm, double consumptionPerKm,Posicao mPos,String nome,List<Rent> a,boolean disp,boolean fuel,double bateria){
        super(averageSpeed,pricePerKm,consumptionPerKm,mPos,nome,a,disp,fuel);
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

    public boolean EletricCar(Object o){
        if(this == o) return true;

        if((o == null) || o.getClass() != this.getClass()) return false;
        EletricCar p = (EletricCar) o;
        return (super.equals(o) && this.bateria == p.getBateria());
    }

    public boolean enoughAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double autonomia = distancia*super.getConsumptionPerKm();
        if(autonomia > bateria){return false;}else {return true;}
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
    private void updateAutonomy(Posicao x){
        double distancia = super.getPos().distancia(x);
        double gastaGota = distancia*super.getConsumptionPerKm();
        this.bateria = this.bateria - gastaGota;
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
