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
        //UMCarroJa mUMCarroJa = UMCarroJa.recoverState();
        //mUMCarroJa.populateData();
        UMCarroJa mUMCarroJa = UMCarroJa.getDataFromBackupFile("logsPOO_carregamentoInicial.bak");
        Controller con = new Controller(mUMCarroJa);
        con.initControler();
        mUMCarroJa.saveState();
    }


    
   
}
