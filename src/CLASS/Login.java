/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import java.sql.ResultSet;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Login {

    JDBC jdbc =JDBC.getInstance();

    public boolean[] checkUser(String Username, char[] Password) {
        System.out.println("1");
        boolean Statement[] = new boolean[2];
        System.out.println("2");
        Statement[0] = false;
        Statement[1] = false;
        try {
            String SQL = "SELECT * FROM admin WHERE username='" + Username + "'";
            ResultSet RS = jdbc.getData(SQL);
            if (RS.next()) {
                String id = RS.getString("admin_id");
                if ("1".equals(id)) {
                    String SQL2 = "SELECT * FROM admin WHERE admin_id='1'";
                    ResultSet RS2 = jdbc.getData(SQL2);
                    if (RS2.next()) {
                        if (Username.equals(RS2.getString("username")) & String.valueOf(Password).equals(RS2.getString("password"))) {

                            Statement[0] = true;
                            Statement[1] = false;

                        }

                    }

                }
                if ("2".equals(id)) {

                    String SQL3 = "SELECT * FROM admin WHERE admin_id='2'";
                    ResultSet RS3 = jdbc.getData(SQL3);
                    if (RS3.next()) {
                        if (Username.equals(RS3.getString("username")) & String.valueOf(Password).equals(RS3.getString("password"))) {

                            Statement[0] = true;
                            Statement[1] = true;

                        }

                    }

                }
            }

        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Login" + " : " + e);
            System.out.println(e);
        }
        return Statement;

    }

}
