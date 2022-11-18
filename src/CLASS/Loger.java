/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author Mayura Lakshan
 */
public class Loger {
    public static void setLoger(){
        try {
            String filepath="C:/KN_EP_LOG/logger.txt";
            PatternLayout layout =new PatternLayout("%-3p %-10d %m %n");
            RollingFileAppender appender=new RollingFileAppender(layout, filepath);
            
            appender.setName("MyLog");
            appender.setMaxFileSize("1MB");
            appender.activateOptions();
            
            Logger.getRootLogger().addAppender(appender);
            
        } catch (Exception e) {
            
            System.out.println(e);
        }
    
    
    }
}
