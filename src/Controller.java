import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class Controller {
    private Menus menuPrincipal;
    private Menus client;
    private Menus owner;
    private Menus aluguer;
    private Menus registar;
    private boolean running = true;
    private UMCarroJa mUMCarroJa;
    private Scanner sn;



    public Controller(UMCarroJa date){
        String[] listmenuPrincipal = {"Login", "Registar","Sair"};
        String[] listClient = {"Alugar um carro","Consultar Histórico de aluguer","Preço da ultima viagem",
                "Dar rating aos  alugueres","Definir Posição","Sair"};
        String[] listOwner = {"Registar um carro","Ver registos dos carros",
                "Ver histórico de aluguer","Abastecer um carro","Receitas da ultima Viagem","Dar rating aos clientes","Mudar o preço de um carro","Sair"};
        String[] listAluger = {"Solicitar o aluguer de um carro mais prox da sua Posicao","Solicitar o aluguer de um carro mais barato",
                "Solicitar o aluguer de um carro especifico","Solicitar um aluguer de um carro com uma autonomia desejada","Voltar a trás"};
        String[] listRegistar = {"Email:","Nome:","Password:","Morada:","Nif:","Data de Nascimento (Formato: 15-01-2005):"};
        this.menuPrincipal = new Menus(listmenuPrincipal);
        this.client = new Menus(listClient);
        this.owner = new Menus(listOwner);
        this.aluguer = new Menus(listAluger);
        this.registar = new Menus(listRegistar);
        this.mUMCarroJa = date;
    }


    public void initControler(){
        sn = new Scanner(System.in);
        while(running) {
            System.out.println("BEM VINDO A UMCARROJA");
            if(mUMCarroJa.isLoggedIn()) {
                activeMenu();
            } else mainMenu();
        }

    }
    private void activeMenu() {
        String str;
        boolean isOwner = true;
        if(mUMCarroJa.getLoggedInUser().getClass() == Owner.class) {
            str = "Proprietário";
        } else {
            isOwner = false;
            str = "Cliente";
        }
        if(isOwner){
            Owner own = (Owner) mUMCarroJa.getLoggedInUser();
            System.out.printf("Nome: %s  Rating: %.2f Nif: %s  Tipo de User: %s\n",own.getName(), own.getRating(), own.getNif() ,str);
            ownerMenu();
        } else {
            Client clt = (Client) mUMCarroJa.getLoggedInUser();
            System.out.printf("Nome: %s Rating: %.2f Nif: %s  Posicao: %s   Tipo de User: %s\n",clt.getName(),clt.getRating() , clt.getNif() ,clt.getPos().toString() ,str);
            clientMenu();
        }

    }


    private void mainMenu(){
        this.menuPrincipal.executeMenu();
        switch (this.menuPrincipal.getChoice()) {
            case 1:
                loginUser();
                break;
            case 2:
                registaUser();
                break;
                default:
                    running = false;
                    break;

        }

    }

    private void loginUser() {
        System.out.print("Insira o email: ");
        String userName = sn.next();
        System.out.print("Insira a password: ");
        String pass = sn.next();
        if(mUMCarroJa.loginOn(userName,pass)){
            System.out.println("Está logado!");
        }
    }

    private boolean emailIsValid(String email) {
        String[] fields  = email.split("@");
        return (fields.length == 2);
    }

    private boolean nifIsValid(String nif) {
        boolean nifIsNumeric = nif.chars().allMatch(Character::isDigit);
        return (nifIsNumeric && nif.length() == 9);
    }

    private void registaUser(){
        System.out.print("Registar como Owner (1) ou como Cliente(2) ");
        int option = this.aluguer.readOption();
        int i;
        List<String> tmp = new ArrayList<>();
        for(i=0;i < this.registar.getSizeMenu();i++){
            this.registar.printMenu(i);
            String a = sn.next();
            while(i == 0  && (!emailIsValid(a))) {
                System.out.print("Email Invalido\n");
                System.out.print("Email:");
                a = sn.next();
            }
            while(i == 4 && !nifIsValid(a))  {
                System.out.print("Nif invalido\n");
                System.out.print("Nif:");
                a = sn.next();
            }
            tmp.add(a);
        }
        String[] arrStrBirth = tmp.get(tmp.size()-1).split("-");
        while(!(option == 1 || option == 2)) {
            option = this.aluguer.readOption();
        }
        while(arrStrBirth.length < 3) {
            System.out.println("Data de nascimento inválida , insira neste formato (15-01-2005):");
            String birthDateString = sn.next();
            arrStrBirth = birthDateString.split("-");
        }
        LocalDate birthDate = LocalDate.of(Integer.parseInt(arrStrBirth[2]),Integer.parseInt(arrStrBirth[1]),Integer.parseInt(arrStrBirth[0]));
        switch(option) {
            case 1:
                Owner owner = new Owner(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),birthDate,tmp.get(4));
                try{
                    mUMCarroJa.addUser(owner);
                } catch(utilizadorJaExiste e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                Client client = new Client(tmp.get(0),tmp.get(1),tmp.get(2),tmp.get(3),birthDate,tmp.get(4));
                try{
                    mUMCarroJa.addUser(client);
                } catch(utilizadorJaExiste e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                break;
        }

    }


    private void clientMenu(){
        this.client.executeMenu();
        switch (this.client.getChoice()) {
            case 1:
                aluguerMenu();
                break;
            case 2:
                viewRentHistory();
                break;
            case 3:
                viewLastRentPrice();
                break;
            case 4:
                giveRatingToRentsMenu();
                break;
            case 5:
                Client user = ((Client) mUMCarroJa.getLoggedInUser());
                user.setPos(getPositionMenu());
                mUMCarroJa.updateUser(user); //TODO: verificar se é necessario , acho que nao pq o logout ja vai dar update quando o user sair da sessao
                break;
            default:
                logout();
                break;
        }
    }

    private void giveRatingToRentsMenu() {
        List<Rent> pendingRateList = mUMCarroJa.getPendingRateList(mUMCarroJa.getLoggedInUser().getNif());
        showList(pendingRateList);
        int choice = getIntInput();
        if (pendingRateList.size() >= choice) {
            System.out.println("1-Separado");
            System.out.println("2-Junto");
            int a = getIntInput();
            Rent _rent = pendingRateList.get(choice-1);
            switch (a) {
                case 1:
                    giveRatingToRentsSeparated(_rent);
                    break;
                case 2:
                    giveRatingToRents(_rent);
                    break;
            }
        }
    }


    private void ownerMenu(){
        this.owner.executeMenu();
        switch (this.owner.getChoice()) {
            case 1:
                vehicleRegister();
                break;
            case 2:
                viewOwnerCars();
                break;
            case 3:
                viewRentHistory();
                break;
            case 4:
                fuelCarMenu();
                break;
            case 5:
                viewLastRentPrice();
                break;
            case 6:
                rateClient();
                break;
            case 7:
                changePrice();
                break;
            default:
                logout();
                break;
        }
    }

    private void rateClient() {
        List<Rent> pendingRateList = mUMCarroJa.getPendingRateList(mUMCarroJa.getLoggedInUser().getNif());
        showList(pendingRateList);
        if(pendingRateList == null) return;
        int choice = getIntInput();
        if (pendingRateList.size() >= choice) {
            double rate = giveRateMenu();
            mUMCarroJa.giveRateClient(pendingRateList.get(choice-1),rate);
        }
    }
    private void vehicleRegister() {
        boolean tmp = false;
        while(!tmp){
            Vehicle _vehicle = null;
            System.out.println("1 -> Carro hibrido");
            System.out.println("2 -> Carro eletrico");
            System.out.println("3 -> Carro a Gasóleo");
            System.out.println("4 -> Sair");
            int res = getIntInput();
            if(res != 1 && res != 2 && res != 3) break;
            _vehicle = newVehicleWithProperties(res);
            if(_vehicle != null)
                tmp = mUMCarroJa.addCar(_vehicle);
            if(!tmp) System.out.println("\nJá existe essa Matricula\n");
        }

    }

    private void viewOwnerCars() {
        List <Vehicle> vehicleList = mUMCarroJa.getListOfCarOwned();
        if(!vehicleList.isEmpty())
        showList(vehicleList);
        else {
            System.out.println("Não tem carros associados!");
        }
        detailedInfo(vehicleList);
    }

    private void detailedInfo(List <Vehicle> vehicleList){
        System.out.println("Insira o numero correspondente do carro para mais informaçao");
        int a = getIntInput();
        if(a <= vehicleList.size()){
            if(vehicleList.get(a-1).getAlugueres().isEmpty()){
                System.out.println("Este carro ainda nao realizou alugueres");
            }else{
                vehicleList.get(a-1).showinfo();
            }
            sn.next();
        }
    }

    private void changePrice(){
        System.out.println("Insira a matricula do carro a mudar o preço");
        String a = sn.next();
        System.out.println("Insira o preço desejado para o carro");
        List <Vehicle> vehicleList = mUMCarroJa.getListOfCarOwned();
        double p = getDoubleInput();
        for(Vehicle r : vehicleList){
            if(r.getMatricula().equals(a) && p > 0){
                r.setPrice(p);
                this.mUMCarroJa.updateVehicle(r);
                System.out.println("Preço atualizado");
            }
            else{System.out.println("Erro matricula inexistente");}
        }



    }

    private void fuelCarMenu(){
        Owner a = (Owner) mUMCarroJa.getLoggedInUser();
        System.out.println("1 - Mostrar os carros com menos de 10% de autonomia");
        System.out.println("2 - Abastecer carro dada a matricula");
        int option = this.owner.readOption();
        switch (option){
            case 1:
                showList(mUMCarroJa.getListOfCarsFuelNeeded());
                break;
            case 2:
                fuelCar();
                break;
        }

    }

    private void fuelCar(){ //TODO: através da lista de abastecer pegar no carro
        Owner a = (Owner) mUMCarroJa.getLoggedInUser();
        String r = sn.next();
        if(a.containsMatricula(r)){
            this.mUMCarroJa.abasteceCarro(r);
            System.out.println("Carro abastecido");
        }else{
            System.out.println("Nao contem nenhum carro com essa matricula");
        }
    }

    private void viewRentHistory() {
        List<Rent> rentList = mUMCarroJa.getLoggedInUser().getRentList();
        showList(rentList);
        if(!rentList.isEmpty())
        sn.next();
    }


    private void aluguerMenu(){
        Vehicle _rentVehicle = null;
        this.aluguer.executeMenu();
        Posicao toWhere = getPositionMenu();
        switch (this.aluguer.getChoice()) {
            case 1:
                try {
                    _rentVehicle = Rent.RentNearCar(mUMCarroJa,toWhere);
                } catch (semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            case 2:
                try {
                    _rentVehicle = Rent.RentCheapestCar(mUMCarroJa,toWhere);
                } catch(semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            case 3:
                System.out.print("Matricula:");
                String matricula = sn.next();
                try{
                    _rentVehicle = mUMCarroJa.getVehicle(matricula);
                    if(!_rentVehicle.enoughAutonomy(toWhere)){
                        _rentVehicle = null;
                    }
                } catch (semVeiculosException e) {
                    System.out.println("Não existe esse veículo");
                }
                break;
            case 4:
                System.out.println("Insira a autonomia desejada:");
                double autonomy = getDoubleInput();
                try {
                    _rentVehicle = Rent.RentCarwithAutonomy(mUMCarroJa,autonomy,toWhere);
                } catch(semVeiculosException e) {
                    System.out.println("Não existem veículos");
                    sn.next();
                }
                break;
            default:
                break;
        }
        if(_rentVehicle != null){
            mUMCarroJa.createRent(_rentVehicle,toWhere);
            System.out.println("Aluguer Concluido");
        }
    }



    private Posicao getPositionMenu () {
        System.out.print("Posicao (Ex: x,y ) : ");
        String posString = sn.next();
        String[] arrPosString = posString.split(",");
        return new Posicao(Double.parseDouble(arrPosString[0]),Double.parseDouble(arrPosString[1]));
    }

    private double giveRateMenu() {
        System.out.println("Rate (0.0-100.0): ");
        return (getDoubleInput());
    }

    private void giveRatingToRents(Rent mRent) {
            double rate = giveRateMenu();
            mUMCarroJa.giveRate(mRent,rate);
    }

    private void giveRatingToRentsSeparated(Rent mRent) {

            System.out.println("Rate ao Proprietario:");
            double rate  = giveRateMenu();
            System.out.println("Rate ao carro:");
            double rateCar = giveRateMenu();
            mUMCarroJa.giveRate(mRent,rate,rateCar);
        }


    private void viewLastRentPrice() {
        List<Rent> rentList = mUMCarroJa.getLoggedInUser().getRentList();
        if(!rentList.isEmpty()){
            Rent rent = rentList.get(rentList.size()-1);
            System.out.println(rent.getPrice());
        } else {
            System.out.println("O cliente ainda não realizou alugueres");
        }
        sn.next();
    }



    private void showList(List<?> list) {
        if(list == null || list.isEmpty()){
            System.out.println("Não há informaçao para mostrar");
            sn.next();
            return;
        }
        int i = 1;
        for(Object l:list) {
            System.out.println(i + " - " + l.toString());
            i++;
        }
    }

    private boolean isMatriculaRightFormated(String matricula) {
        String[] isValid = matricula.split("-");
        return (isValid.length == 3);
    }

    private Vehicle newVehicleWithProperties(int vehicleType) {
        Vehicle _car = null;

        System.out.print("Matricula: ");
        String matricula = sn.next();
        boolean matriculaFormat = isMatriculaRightFormated(matricula);
        while(!matriculaFormat) {
            System.out.println("Matricula no formato errado");
            System.out.print("Matricula: ");
            matricula = sn.next();
            matriculaFormat = isMatriculaRightFormated(matricula);
        }
        System.out.print("Preço por km:");
        double pricePerKm = getDoubleInput();

        System.out.print("Velocidade Media:");
        int averageSpeed = getIntInput();

        System.out.print("Consumo por KM: ");
        double consumPerKm = getDoubleInput();

        Posicao mPos = getPositionMenu();


        System.out.print("Marca :");
        String marca = sn.next();


        System.out.print("Quantidade de combustivel : ");
        double fuel = getDoubleInput();

        switch(vehicleType) {
            case 1:
                _car = new HybridCar(marca,matricula, mUMCarroJa.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
            case 2:
                _car = new EletricCar(marca,matricula, mUMCarroJa.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
            case 3:
                _car = new GasCar(marca,matricula, mUMCarroJa.getLoggedInUser().getNif(),averageSpeed,pricePerKm,consumPerKm,mPos,fuel);
                break;
        }
        return _car;
    }

    private void logout() {
        mUMCarroJa.logout();
    }

    public double getDoubleInput() {
        boolean flag = false;
        double a = 0;
        while (!flag) {
           try {
                a = sn.nextDouble();

               flag = true;
           }catch (InputMismatchException e){
               System.out.println("Formato errado,insira um double");
           }
        }
        return a;
    }

    public int getIntInput() {
        boolean flag = false;
        int a = 0;
        while (!flag) {
            try {
                a = sn.nextInt();
                flag = true;
            }catch (InputMismatchException e){
                System.out.println("Formato errado,insira um inteiro");
            }
        }
        return a;
    }
}
