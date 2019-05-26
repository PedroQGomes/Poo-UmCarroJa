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
        UMCarroJa mUMCarroJa = UMCarroJa.recoverState();
        if(!mUMCarroJa.isBackupDataRead()) {
            mUMCarroJa = UMCarroJa.getDataFromBackupFile("logsPOO_carregamentoInicial.bak",mUMCarroJa);
            mUMCarroJa.setBackupDataRead();
        }
        Controller con = new Controller(mUMCarroJa);
        con.initControler();
        mUMCarroJa.saveState();
    }


    
   
}
