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
public class Seller {
    JDBC jdbc=new JDBC();
    
    public boolean saveData(String ID,String Title,String Description,String Adress,String Number,String Date,String Time){
        boolean val=false;
        String Sql="insert into seller(id,title, description, Adress, number, added_date, added_time,status) values ('"+ID+"','"+Title+"','"+Description+"','"+Adress+"','"+Number+"','"+Date+"','"+Time+"','1' )";
        
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
            log.info("Exception CLASS/Seller/saveData"+" : "+ex);
            
        }
        
        
        return val;
    }
    
    public boolean updateData(String ID,String Title,String Description,String Adress,String Number,String Date,String Time){
    
            boolean val=false;
        String Sql="UPDATE seller SET title='"+Title+"',description='"+Description+"',adress='"+Adress+"',number='"+Number+"' WHERE id='"+ID+"' ";
        
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
            log.info("Exception CLASS/Seller/updateData"+" : "+ex);
            
        }
        
        
        return val;
    }
    
    public Vector getDataToTable(){
        String Sql2="select * from seller";
        
            
        try {
            ResultSet RS=jdbc.getData(Sql2);
            while(RS.next()){
            Vector vec=new Vector();
            vec.add(RS.getString("id"));
            vec.add(RS.getString("title"));
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
            log.info("Exception CLASS/Seller/getDataToTable"+" : "+e);
            
        }
        
       return null; 
    } 
    
   public String[] getData(String ID){
       String[] DATA=new String[5];
       try {
           String Sql3="SELECT * FROM seller WHERE id='"+ID+"'";
           ResultSet RS2=jdbc.getData(Sql3);
           if(RS2.next()){
               DATA[0]=RS2.getString("id");
               DATA[1]=RS2.getString("title");
               DATA[2]=RS2.getString("description");
               DATA[3]=RS2.getString("adress");
               DATA[4]=RS2.getString("number");
               }
           
       } catch (Exception e) {
            Loger.setLoger();
            Logger log=Logger.getLogger("Log");
            log.info("Exception CLASS/Seller/getData"+" : "+e);
       }
       
       return DATA;
   }
}
