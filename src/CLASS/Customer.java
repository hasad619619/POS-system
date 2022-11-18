/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import java.sql.ResultSet;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Customer {
    JDBC jdbc=JDBC.getInstance();
    
    public boolean saveData(String ID,String Name,String Description,String Adress,String Number,String Date,String Time){
        boolean val=false;
        String Sql="insert into customer(id,name, description, Adress, number, added_date, added_time,status) values ('"+ID+"','"+Name+"','"+Description+"','"+Adress+"','"+Number+"','"+Date+"','"+Time+"' ,'1')";
        
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
            log.info("Exception CLASS/Customer/saveData"+" : "+ex);
            
        }
        
        
        return val;
    }
    
    public boolean updateData(String ID,String Name,String Description,String Adress,String Number,String Date,String Time){
    
            boolean val=false;
        String Sql="UPDATE customer SET name='"+Name+"',description='"+Description+"',adress='"+Adress+"',number='"+Number+"' WHERE id='"+ID+"' ";
        
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
            log.info("Exception CLASS/Customer/updateData"+" : "+ex);
            
        }
        
        
        return val;
    }
    
    public Vector getDataToTable(){
        String Sql2="select * from customer";
        
            
        try {
            ResultSet RS=jdbc.getData(Sql2);
            while(RS.next()){
            Vector vec=new Vector();
            vec.add(RS.getString("id"));
            vec.add(RS.getString("name"));
            vec.add(RS.getString("description"));
            vec.add(RS.getString("adress"));
            vec.add(RS.getString("number"));
            vec.add(RS.getString("added_date"));
            vec.add(RS.getString("added_time"));
            return vec;
            }
        } catch (Exception e) {
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Customer/getDataToTable"+" : "+e);
            
        }
        
       return null; 
    } 
    
   public String[] getData(String ID){
       String[] DATA=new String[5];
       try {
           String Sql3="SELECT * FROM customer WHERE id='"+ID+"'";
           ResultSet RS2=jdbc.getData(Sql3);
           if(RS2.next()){
               DATA[0]=RS2.getString("id");
               DATA[1]=RS2.getString("name");
               DATA[2]=RS2.getString("description");
               DATA[3]=RS2.getString("adress");
               DATA[4]=RS2.getString("number");
               }
           
       } catch (Exception e) {
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/customer/getData"+" : "+e);
       }
       
       return DATA;
   }
}
