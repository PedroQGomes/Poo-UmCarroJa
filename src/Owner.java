import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    private static final long serialVersionUID = 1236567219L;
    private List<String> listCar;

    /**
     * Construtor do Owner
     * @param _email
     * @param _name
     * @param _password
     * @param _morada
     * @param _birthDate
     * @param _nif
     */
    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        listCar = new ArrayList<>();
    }

    /**
     * Construtor do Owner
     * @param own
     */
    public Owner(Owner own) {
        super(own);
        this.listCar = own.getListOfMatricula();
    }

    /**
     * Retorna a lista das matrículas do owner
     * @return List<String> Com as matrículas dos carros do owner
     */
    public List<String> getListOfMatricula () {
        return new ArrayList<>(listCar);
    }


    /**
     * Faz o clone do owner
     * @return Owner
     */
    public Owner clone() {
        return new Owner(this);
    }

    /**
     * Verifica a igualdade entre owners
     * @param o
     * @return true se forem iguais, false se não forem
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Owner p = (Owner) o;
        return(super.equals(o) && p.getListOfMatricula().equals(this.listCar));
    }

    /**
     * Hashcode
     * @return int(hashcode)
     */
    public int hashCode(){
        int hash = 5;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + this.listCar.stream().mapToInt(String::hashCode).sum();
        return hash;
    }

    /**
     * Adiciona à lista de matriculas do owner, a matricula do carro, verifica se já existe.
     * @param mat
     * @return boolean , true se não existir, false se existir.
     */
    public boolean addVehicle(String mat){
        if(listCar.contains(mat)) return false;
        this.listCar.add(mat);
        return true;
    }


    public boolean containsMatricula(String a){
        return this.listCar.contains(a);
    }

}

