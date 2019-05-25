/**
 * Write a description of class Main here.
 *
 * @author josepgrs
 * @version 1
 */
public class Main
{
    
    public static void main(String [] args)
    {
        Data mData = Data.recoverState();
        //mData.populateData();
        //Data mData = Data.getDataFromBackupFile("logsPOO_carregamentoInicial.bak");
        Controller con = new Controller(mData);
        con.initControler();
        mData.saveState();
    }


    
   
}
