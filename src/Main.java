import java.io.*;
import java.util.ArrayList;
import java.time.LocalDateTime;

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
<<<<<<< HEAD
        Data mData = Data.recoverState();
        //mData.populateData();
        //Data mData = Data.getDataFromBackupFile("logsPOO_carregamentoInicial.bak");
        Menus menus = new Menus(mData);
        menus.initMenu();
=======
        Data mData = IData.recoverState();
        mData.populateData();
        Controler con = new Controler(mData);
        con.initControler();
>>>>>>> 8aafa144df2f2214a29d7f2e2a9aa4a8b9852da8
        mData.saveState();
    }


    
   
}
