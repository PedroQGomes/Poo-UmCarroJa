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
    double rating;
    
    public static void main(String [] args)
    {
        Data mData = IData.recoverState();
        mData.populateData();
        Menus menus = new Menus(mData);
        menus.initMenu();
        mData.saveState();
    }


    
   
}
