import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Owner extends GeneralUser
{
    private static final long serialVersionUID = 1236567219L;
    private List<String> listCar;

    public Owner(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        listCar = new ArrayList<>();
    }

    public Owner(Owner own) {
        super(own);
        this.listCar = own.getListOfMatricula();
    }


    public List<String> getListOfMatricula () {
        return new ArrayList<>(listCar);
    }




    public Owner clone() {
        return new Owner(this);
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Owner p = (Owner) o;
        return(super.equals(o) && p.getListOfMatricula().equals(this.listCar));
    }

    public int hashCode(){
        int hash = 5;
        hash = 31*hash + super.hashCode();
        hash = 31*hash + this.listCar.stream().mapToInt(String::hashCode).sum();
        return hash;
    }

    public boolean addVehicle(String mat){
        if(listCar.contains(mat)) return false;
        this.listCar.add(mat);
        return false;
    }

}

