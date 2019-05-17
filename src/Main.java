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
        Data mData = null;
        try {
            FileInputStream fis = new FileInputStream("data.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            mData = (Data) ois.readObject();
            System.out.println("Dados Lidos");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (mData == null) mData = new Data();
        Menus menus = new Menus(mData);
        menus.initMenu();
        try {
            FileOutputStream fos = new FileOutputStream("data.tmp");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mData);
            System.out.println("Dados Gravados");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
    
   
}
