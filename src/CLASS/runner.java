/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import UI.Login_UI;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import javax.swing.UIManager;

/**
 *
 * @author Mayura Lakshan
 */
public class runner {
    public static void main(String[] args) {
        try    {
      UIManager.setLookAndFeel(new WindowsLookAndFeel());
    }   catch (Exception e)    {
      e.printStackTrace();
    }
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_UI().setVisible(true);
            }
        });
    }
    
}
