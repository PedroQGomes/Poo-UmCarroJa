import java.time.LocalDate;

public class Client extends GeneralUser
{
    private static final long serialVersionUID = 1934567219L;
    private Posicao pos;

    /**
     * Construtor do cliente
     * @param _email
     * @param _name
     * @param _password
     * @param _morada
     * @param _birthDate
     * @param _nif
     */
    public Client(String _email, String _name, String _password, String _morada, LocalDate _birthDate,String _nif)
    {
        super(_email,_name,_password,_morada,_birthDate,_nif);
        this.pos = new Posicao();
    }

    /**
     * Construtor do cliente
     * @param clt
     */
    public Client(Client clt)
    {
        super(clt);
        this.pos = clt.getPos().clone();
    }

    /**
     * Faz set da posição
     * @param mPos
     */
    public void setPos(Posicao mPos) {this.pos = mPos.clone();}

    /**
     * Retorna a posição do cliente
     * @return Posicao
     */
    public Posicao getPos() {
        return this.pos.clone();
    }

    /**
     * Faz o clone do Cliente
     * @return Client
     */
    public Client clone() {
        return new Client(this);
    }

    /**
     * Verifica a igualdade entre owners
     * @param o
     * @return true se forem iguais, false se não forem
     */
    public boolean equals(Object o){
        if(this == o) return true;
        if((o == null) || o.getClass() != this.getClass()) return false;
        Client p = (Client) o;
        return(super.equals(o) && p.getPos().equals(this.pos));
    }
}
