/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLASS;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Mayura Lakshan
 */
public class JDBC {

    private static JDBC instance;

    String URL;

    public JDBC() {
    }
    private static final String ALGORm = "AES";
    private static final byte[] keyValue = new byte[]{'k', 'h', 'k', 'a', 'g', 'a',
        'l', 'n', 'g', 'u', 'Y', 'D', 'e', 'f', 'u', 'n'};

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORm);
        return key;
    }
    

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORm);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public static synchronized JDBC getInstance() {
        if (instance == null) {
            instance =new JDBC();
        }
        return instance;
    }

    public Connection con() throws Exception {
        //---------------------------------------
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //-------------------------------
        String host;
        String port;
        String un;
        String pw;
        String db ;
        byte[] bytesEncoded = prop.getProperty("G1").getBytes();
        host= decrypt(new String(bytesEncoded));
        
        byte[] bytesEncoded2 = prop.getProperty("G4").getBytes();
        port=decrypt(new String(bytesEncoded2));
        
        byte[] bytesEncoded3 = prop.getProperty("G5").getBytes();
        un=decrypt(new String(bytesEncoded3));
        
        byte[] bytesEncoded4 = prop.getProperty("G2").getBytes();
        pw= decrypt(new String(bytesEncoded4));
        
        byte[] bytesEncoded5 = prop.getProperty("G3").getBytes();
        db =  decrypt(new String(bytesEncoded5));
        
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?useUnicode=yes&characterEncoding=UTF-8", un, pw);

        return c;
    }

    public int putData(String sql) throws Exception {
        return con().createStatement().executeUpdate(sql);
    }

    public ResultSet getData(String sql) throws Exception {
        return con().createStatement().executeQuery(sql);
    }

}
