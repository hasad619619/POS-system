/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Product {
    JDBC jdbc=new JDBC();
    
    public boolean delete(String ID){
        
        boolean val=false;
        String Sql="UPDATE product SET available='0' WHERE id='"+ID+"'";
        
            try {
            int I=jdbc.putData(Sql);
            if(I==1){
                 val=true;
            }else{
                val=false;
            }
            
        } catch (Exception ex) {
            
            val=false;
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Product/saveData"+" : "+ex);
            
        }
        
        
        return val;
    }
    
    public boolean saveData(String PID,String BID,String SellerID,String Title,String Description,String Qtt,String Buying,String Selling,String Price,String Date,String Time,String Exp,String Val){
        boolean val=false;
        String Sql="insert into product(id,bid,seller_id,title, description,qtt, buying_price, selling_price,price, added_date, added_time,expdate,val,available) "
                + "values ('"+PID+"','"+BID+"','"+SellerID+"','"+Title+"','"+Description+"','"+Qtt+"','"+Buying+"','"+Selling+"','"+Price+"','"+Date+"','"+Time+"','"+Exp+"','"+Val+"','1' )";
        
        try {
            int I=jdbc.putData(Sql);
            if(I==1){
                 val=true;
            }else{
                val=false;
            }
            
        } catch (Exception ex) {
            
            val=false;
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Product/saveData"+" : "+ex);
            
        }
        
        
        return val;
    }

    public String[] getData(String ID) {
        String[] DATA=new String[8];
       try {
           String Sql3="SELECT * FROM product WHERE id='"+ID+"'";
           ResultSet RS2=jdbc.getData(Sql3);
           if(RS2.next()){
               DATA[0]=RS2.getString("id");
               DATA[1]=RS2.getString("title");
               DATA[2]=RS2.getString("description");
               DATA[3]=RS2.getString("qtt");
               DATA[4]=RS2.getString("buying_price");
               DATA[5]=RS2.getString("selling_price");
               DATA[6]=RS2.getString("expdate");
               DATA[7]=RS2.getString("bid");
               }
           
       } catch (Exception e) {
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Product/getData"+" : "+e);
       }
       
       return DATA;
        
    }

    public boolean updateData(String Title,String BID,String Description,double Buy ,double Sell , String ID) {
        boolean val=false;
        //id, bid, title, description, qtt, buying_price, selling_price, price, added_date, added_time, expdate, Seller_id, available, val
        String Sql="UPDATE product "
                + "SET title='"+Title+"',bid='"+BID+"',description='"+Description+"' ,buying_price='"+Buy+"' ,selling_price='"+Sell+"'  WHERE id='"+ID+"'   ";
        
        try {
            int I=jdbc.putData(Sql);
            if(I==1){
                 val=true;
            }else{
                val=false;
            }
            
        } catch (Exception ex) {
            
            val=false;
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Product/updateData"+" : "+ex);
            
        }
        
        
        return val;
    }
}
