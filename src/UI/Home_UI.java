/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.Loger;
import CLASS.data;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public final class Home_UI extends javax.swing.JFrame {

    /**
     * Creates new form Home_UI
     */
    static DecimalFormat df = new DecimalFormat("##########0.00");

    static void onIdSelected(String id) {

    }

    private void getMessage() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            _message.setText("  Receiving Message From APKHUB....");
            String url = "http://apkhub.lk/getMessgage.php";
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(url);

            try {
                int statusCode = client.executeMethod(method);

                if (statusCode == HttpStatus.SC_OK) {
                    System.err.println();
                    _message.setText("  " + method.getResponseBodyAsString());
                }

            } catch (Exception e) {
                _message.setText("  " + "No Internet Connection ....");
            } finally {
                // Release the connection.
                method.releaseConnection();
            }

        }, 0, 10, TimeUnit.MINUTES);

    }

    int row;

    String path = new data().path();

    int KEY;
    int IsIdSet = 1;
    String Product_ID;
    String Product_Title;
    int Qtt_Available;
    double Selling_Price;

    String Customer_ID = "1";
    String PID;

    String NEWID;

    int type = 0;//1 for cash 2 for credit

    JDBC jdbc =JDBC.getInstance();

    public void sendMail(String Date) {

//        double net = 0.0;
//        double cost = 0.0;
//        double profit = 0.0;
//        try {
//            String SQL122 = "select bill_id from bill where added_date >='" + Date + "'"
//                    + "and added_date <='" + Date + "'";
//            ResultSet RS122 =JDBC.getInstance().getData(SQL122);
//            while (RS122.next()) {
//                String SQL22 = "SELECT * FROM cash_sale WHERE bill_id='" + RS122.getString("bill_id") + "'";
//                ResultSet RS12 =JDBC.getInstance().getData(SQL22);
//
//                while (RS12.next()) {
//                    net = Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS12.getString("selling_price")) + net;
//                    String SQL14 = "SELECT buying_price FROM product WHERE id='" + RS12.getString("product_id") + "'";
//                    ResultSet RS14 =JDBC.getInstance().getData(SQL14);
//                    if (RS14.next()) {
//                        cost = Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS14.getString("buying_price")) + cost;
//                    }
//
//                }
//            }
//
//            Email E = new Email();
//            E.sendMail("" + net, "" + profit, "" + cost);
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
    }

    public void Printbill(String BILLID) {
        DefaultTableModel dt = (DefaultTableModel) jTable2.getModel();
        dt.setRowCount(0);
        if (!(BILLID.equals(""))) {

            try {
                

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Calendar cal = Calendar.getInstance();
                String SQL2 = "select * from details where id='1'";
                ResultSet rs = JDBC.getInstance().getData(SQL2);
                String NAME = "NO DATA PROVIDE";
                String ADDRESS = "NO DATA PROVIDE";
                String CONTACT = "NO DATA PROVIDE";
                if (rs.next()) {
                    NAME = rs.getString("nf");
                    ADDRESS = rs.getString("ad");
                    CONTACT = rs.getString("cn");
                }

                InputStream jr = getClass().getResourceAsStream("/reports/Invoice.jasper");
                Map<String, Object> para = new HashMap<String, Object>();
                para.put("DATE", dateFormat.format(cal.getTime()));
                para.put("ID", BILLID);
                para.put("na", NAME);
                para.put("cn", CONTACT);
                para.put("ad", ADDRESS);

                String SQL = "SELECT * FROM bill WHERE bill_id='" + BILLID + "'";
                ResultSet RS4 = jdbc.getData(SQL);
                if (RS4.next()) {
                    para.put("TOTAL", "" + RS4.getDouble("total"));
                    para.put("DES", "" + RS4.getDouble("descount"));
                    para.put("NET", "" + RS4.getDouble("net_price"));
                    para.put("CASH", "" + RS4.getDouble("cash"));
                    para.put("BAL", "" + RS4.getDouble("balance"));
                   
                }
                JasperPrint jp = JasperFillManager.fillReport(jr, para, JDBC.getInstance().con());
                JasperPrintManager.printReport(jp, true);
                
                Clear();
                clearAll();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bill Print Is Not success ! Bill Data Is Saved ! EROR");
                Loger.setLoger();
                Logger log = Logger.getLogger("Log");
                log.info("Exception Print Bill" + " : " + ex);
                Clear();
                clearAll();
                System.out.println(ex);
            }

        } else {

            JOptionPane.showMessageDialog(this, "Bill Print Is Not success ! Bill Data Is Saved ! ID NULL");
            Clear();
            clearAll();

        }

    }

    public void beforCheckOut() {

        if (!(_IS_CO.isSelected())) {

            Customer_ID = "1";
            double CASH = Double.parseDouble(_cash.getText());
            double BAL = Double.parseDouble(_balance.getText());
            double TOT = Double.parseDouble(_tot.getText());
            double DESs = Double.parseDouble(VAL.getText());
            double NET = Double.parseDouble(_net_tot.getText());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat2 = new SimpleDateFormat("HH:MM");
            Calendar cal2 = Calendar.getInstance();
            String DATE = dateFormat.format(cal.getTime());
            String TIME = dateFormat2.format(cal2.getTime());

            if ((jTable1.getRowCount() > 0) & (CASH >= NET)) {

                try {
                    String SQL = "INSERT INTO bill(total, cash, balance,added_date, added_time, type, customer_id, descount, net_price)"
                            + "VALUES('" + TOT + "' , '" + CASH + "' , '" + BAL + "' , '" + DATE + "' , '" + TIME + "' ,1 , 1, '" + DESs + "' , '" + NET + "')";
                    int V = jdbc.putData(SQL);

                    if (V == 1) {
                        String SQL2 = "SELECT MAX(bill_id) AS LastID FROM bill;";
                        ResultSet RS = jdbc.getData(SQL2);
                        if (RS.next()) {
                            String ID = RS.getString("LastID");

                            for (int i = 0; i < jTable1.getRowCount(); i++) {
                                String PN = jTable1.getValueAt(i, 0).toString();
                                String PIDD = jTable1.getValueAt(i, 1).toString();
                                String PQTT = jTable1.getValueAt(i, 2).toString();
                                String D[] = PQTT.split(":");

                                String SQL5 = "SELECT qtt FROM product WHERE id='" + PIDD + "'";
                                ResultSet RS5 = jdbc.getData(SQL5);
                                if (RS5.next()) {
                                    double QTA = RS5.getDouble("qtt");
                                    double NEWQT = QTA - Double.parseDouble(D[0]);
                                    String SQL6 = "UPDATE product SET qtt='" + NEWQT + "' WHERE id='" + PIDD + "'";
                                    jdbc.putData(SQL6);

                                }

                                String PUP = jTable1.getValueAt(i, 3).toString();
                                String PP = jTable1.getValueAt(i, 4).toString();
                                String PD = jTable1.getValueAt(i, 5).toString();
                                String PNP = jTable1.getValueAt(i, 6).toString();

                                String SQL3 = "INSERT INTO cash_sale(Qtt, selling_price, price, product_id, bill_id, descount, net_price)"
                                        + "VALUES('" + D[0] + "' , '" + PUP + "' , '" + PP + "' , '" + PIDD + "' , '" + ID + "','" + PD + "','" + PNP + "')";
                                int P = jdbc.putData(SQL3);

                            }

                            int B = JOptionPane.showConfirmDialog(this, "Do You Want To Print Bill ?");
                            if (B == 0) {
                                Printbill(ID);

                            } else {
                                Clear();
                                clearAll();
                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(this, "can't pay bill System Restart !");
                        clearAll();
                    }

                } catch (Exception ex) {

                }

            } else {
                JOptionPane.showMessageDialog(this, "Can't Pay This Bill Check You Enterd Cash Value And Table Data !");
                _cash.setEditable(true);
                _cash.grabFocus();

            }

        }
        if (_IS_CO.isSelected()) {

            if (Integer.parseInt(Customer_ID) > 1) {

                int i = JOptionPane.showConfirmDialog(this, "Do You Want To Pay This Bill With Customer Id=" + Customer_ID);
                System.out.println(i + " c ccc");
                if (i == 0) {

                    double CASH = Double.parseDouble(_cash.getText());
                    double BAL = Double.parseDouble(_balance.getText());
                    double TOT = Double.parseDouble(_tot.getText());
                    double DESs = Double.parseDouble(VAL.getText());
                    double NET = Double.parseDouble(_net_tot.getText());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar cal = Calendar.getInstance();
                    DateFormat dateFormat2 = new SimpleDateFormat("HH:MM");
                    Calendar cal2 = Calendar.getInstance();
                    String DATE = dateFormat.format(cal.getTime());
                    String TIME = dateFormat2.format(cal2.getTime());

                    if ((jTable1.getRowCount() > 0) & (CASH >= NET)) {

                        try {
                            String SQL = "INSERT INTO bill(total, cash, balance,added_date, added_time, type, customer_id, descount, net_price)"
                                    + "VALUES('" + TOT + "' , '" + CASH + "' , '" + BAL + "' , '" + DATE + "' , '" + TIME + "' ,1 ,'" + Customer_ID + "', '" + DESs + "' , '" + NET + "')";
                            int V = jdbc.putData(SQL);

                            if (V == 1) {
                                String SQL2 = "SELECT MAX(bill_id) AS LastID FROM bill;";
                                ResultSet RS = jdbc.getData(SQL2);
                                if (RS.next()) {
                                    String ID = RS.getString("LastID");

                                    for (int ii = 0; ii < jTable1.getRowCount(); ii++) {
                                        String PN = jTable1.getValueAt(ii, 0).toString();
                                        String PIDD = jTable1.getValueAt(ii, 1).toString();
                                        String PQTT = jTable1.getValueAt(ii, 2).toString();
                                        String D[] = PQTT.split(":");
                                        String PUP = jTable1.getValueAt(ii, 3).toString();
                                        String PP = jTable1.getValueAt(ii, 4).toString();
                                        String PD = jTable1.getValueAt(ii, 5).toString();
                                        String PNP = jTable1.getValueAt(ii, 6).toString();

                                        String SQL5 = "SELECT qtt FROM product WHERE id='" + PIDD + "'";
                                        ResultSet RS5 = jdbc.getData(SQL5);
                                        if (RS5.next()) {
                                            double QTA = RS5.getDouble("qtt");
                                            double NEWQT = QTA - Double.parseDouble(D[0]);
                                            String SQL6 = "UPDATE product SET qtt='" + NEWQT + "' WHERE id='" + PIDD + "'";
                                            jdbc.putData(SQL6);

                                        }

                                        String SQL3 = "INSERT INTO cash_sale(Qtt, selling_price, price, product_id, bill_id, descount, net_price)"
                                                + "VALUES('" + D[0] + "' , '" + PUP + "' , '" + PP + "' , '" + PIDD + "' , '" + ID + "','" + PD + "','" + PNP + "')";
                                        int P = jdbc.putData(SQL3);

                                    }
                                    int B = JOptionPane.showConfirmDialog(this, "Do You Want To Print Bill ?");
                                    if (B == 0) {
                                        Printbill(ID);

                                    } else {
                                        Clear();
                                        clearAll();
                                    }

                                }

                            } else {

                                JOptionPane.showMessageDialog(this, "can't pay bill System Restart !");
                                clearAll();
                            }

                        } catch (Exception ex) {

                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Can't Pay This Bill.Check You Enterd Cash Value And Table Data !");
                        _cash.setEditable(true);
                        _cash.grabFocus();

                    }

                } else {
                    _IS_CO.setSelected(false);
                    Customer_ID = "1";

                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Select Customer !");

            }

        }

    }

    public void clearAll() {
        Customer_ID = "1";
        IsIdSet = 0;
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        VAL.setText("");
        _cash.setText("");
        _PID.setEditable(true);
        _qtt.setEditable(true);
        _D_P.setEditable(true);
        _cash.setEditable(true);
        _qtt.setEditable(false);
        _qtt.setText("");
        _sell.setText("");
        _IS_CO.setSelected(false);
        _customer.setEnabled(false);
        _balance.setText("");
        _net_tot.setText("0.00");
        _tot.setText("");
        _cash.setText("");
        _PID.setText("");
        _PID.grabFocus();
        _D_P.setText("");
        _D_T.setText("");
        isSet();
        getTot();

    }

    public void inEnterTD() {

        double TOT = Double.parseDouble(_tot.getText());
        double PR = Double.parseDouble(_D_T.getText());

        double DES = (TOT / 100) * PR;
        DES = Math.round(DES * 100.0) / 100.0;
        VAL.setText(df.format(DES));
        double net = TOT - DES;
        _net_tot.setText(df.format(net));

    }

    public void inEnderCash() {
        int RowCount = jTable1.getRowCount();
        if (RowCount > 0) {
            double TOT = Double.parseDouble(_net_tot.getText());
            double CASH = Double.parseDouble(_cash.getText());
            double balance = 0.0;
            if (CASH > TOT) {
                balance = CASH - TOT;
            }
            _balance.setText(df.format(balance));
            btn_Print.grabFocus();

        } else {
            JOptionPane.showMessageDialog(this, "Please Add Items First !");
            _PID.grabFocus();

        }

    }

    public void getuserdata() {
        String SQL = "SELECT * FROM details WHERE id='1'";
        try {
            ResultSet data = jdbc.getData(SQL);
            if (data.next()) {
                this.setTitle(data.getString("nf"));

            }
        } catch (Exception e) {
        }
    }

    public void InEnterPid() {

        if (!(_PID.getText().equals(""))) {
            String ID = _PID.getText();
            try {
                String Sql = "SELECT id FROM product WHERE bid='" + ID + "' AND available='1'";
                ResultSet RS =JDBC.getInstance().getData(Sql);
                int count = 0;
                while (RS.next()) {
                    count++;
                    PID = RS.getString("id");

                }
                if (count == 1) {
                    getProductData(PID);
                    _qtt.setEditable(true);
                    _qtt.grabFocus();

                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this, "Wrong Product ID !");
                    Clear();
                    _PID.setText("");
                    _PID.grabFocus();
                }
                if (count > 1) {
                    InvoiceDuplicateItems dialog = new InvoiceDuplicateItems(this, true, ID);
                    dialog.setVisible(true);

                }
            } catch (Exception ex) {

            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Product First !");

        }

    }

    public void ADDB() {

        if (check()) {
            String VAL = _type.getText();
            if (VAL.equals("Pieces")) {
                int QTT = Integer.parseInt(_qtt.getText());

                int sler = jTable1.getRowCount();

                int totq = 0;
                for (int i = 0; i < sler; i++) {

                    String v = jTable1.getValueAt(i, 1).toString();
                    if (v.equals(Product_ID)) {
                        String QTT_U = jTable1.getValueAt(i, 2).toString();
                        String[] QTT_N = QTT_U.split(":");
                        int N_QTT = Integer.parseInt(QTT_N[0]);
                        totq = totq + N_QTT;

                    } else {

                    }

                }
                System.out.println(totq);

                if (QTT <= Qtt_Available - totq) {
                    System.out.println("OK");
                    System.out.println(Qtt_Available - totq);
                    double Net = QTT * Selling_Price;
                    double DES = Double.parseDouble(_D_P.getText());
                    double Net_Price = Net - (DES * QTT);
                    Vector Vec = new Vector();
                    Vec.add(Product_Title);
                    Vec.add(Product_ID);
                    Vec.add(QTT + ":Pieces");
                    Vec.add(df.format(Selling_Price));
                    Vec.add(df.format(Net));
                    Vec.add(df.format(DES));
                    Vec.add(df.format(Net_Price));
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.addRow(Vec);
                    Clear();
                    getTot();
                } else {
                    int DDD = Qtt_Available - totq;

                    if (DDD > 0) {
                        int I = JOptionPane.showConfirmDialog(this, "Product Out Of Stock ! Only available '" + DDD + "' Pieces Only do you want to Add ?");
                        System.out.println(I);
                        if (I == 0) {
                            _qtt.setText("" + DDD);

                        } else if (I == 1) {
                            Clear();
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Product Out Of Stock !");
                        Clear();
                    }

                }

            } else if (VAL.equals("Kg")) {

                double QTT = Double.parseDouble(_qtt.getText());
                double Q_A = Qtt_Available * 1.0;

                int sler = jTable1.getRowCount();

                double totq = 0;
                for (int i = 0; i < sler; i++) {
                    String v = jTable1.getValueAt(i, 1).toString();
                    if (v.equals(Product_ID)) {
                        String QTT_U = jTable1.getValueAt(i, 2).toString();
                        String[] QTT_N = QTT_U.split(":");
                        double N_QTT = Double.parseDouble(QTT_N[0]);
                        totq = totq + N_QTT;

                    } else {

                    }

                    System.out.println("MEK TOT : " + totq);
                }
                double Q_B = Q_A - totq;
                if (QTT <= Q_B) {
                    double Net = QTT * Selling_Price;
                    double DES = (Net / 100) * Double.parseDouble(_D_P.getText());
                    double Net_Price = Net - DES;
                    Vector Vec = new Vector();
                    Vec.add(Product_Title);
                    Vec.add(Product_ID);
                    Vec.add(QTT + ":Kg");
                    Vec.add(Selling_Price);
                    Vec.add(Net);
                    Vec.add(DES);
                    Vec.add(Net_Price);
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.addRow(Vec);
                    Clear();
                    getTot();
                } else {
                    double Q_A_V = (Qtt_Available * 1.0) - totq;
                    if (Q_A_V > 0) {
                        int I = JOptionPane.showConfirmDialog(this, "Product Out Of Stock ! Only available '" + Q_A_V + "' KG Only do you want to Add ?");
                        System.out.println(I);
                        if (I == 0) {
                            _qtt.setText("" + Q_A_V);

                        } else if (I == 1) {
                            Clear();
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Product Out Of Stock !");
                        Clear();
                    }

                }

            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Please Select Product And Add Qtt You Want !");
            _PID.grabFocus();

        }

    }

    public void getTot() {

        try {
            int sler = jTable1.getRowCount();
            double tot = 0;
            for (int i = 0; i < sler; i++) {
                String v = jTable1.getValueAt(i, 6).toString();

                double j = Double.parseDouble(v);
                tot = tot + j;

            }

            _tot.setText(df.format(tot));

        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Stock/gettot" + " : " + e);
        }

    }

    public boolean check() {

        return (!(_sell.getText().equals("")) & !(_qtt.getText().equals("")) & !(_D_P.getText().equals("")));

    }

    public void Clear() {
        IsIdSet = 0;
        _title.setText("No Product Selected");
        VAL.setText("");
        _cash.setText("");
        _PID.setEditable(true);
        _qtt.setEditable(true);
        _D_P.setEditable(true);
        _cash.setEditable(true);
        _customer.setEditable(false);
        _customer.setEnabled(false);
        Customer_ID = "1";

        _qtt.setEditable(false);
        _qtt.setText("");
        _sell.setText("0.00");
        _balance.setText("");
        _tot.setText("");
        _net_tot.setText("0.00");
        _cash.setText("");
        _IS_CO.setSelected(false);
        _PID.setText("");
        _PID.grabFocus();
        _D_P.setText("");
        _D_T.setText("");
        isSet();
        getTot();

    }

    public void getProductData(String Product_id) {

        try {
            String Sql = "SELECT * FROM product WHERE id='" + Product_id + "'";

            ResultSet RS = jdbc.getData(Sql);
            if (RS.next()) {
                Product_ID = RS.getString("id");
                Selling_Price = RS.getDouble("selling_price");
                Qtt_Available = RS.getInt("qtt");
                Product_Title = RS.getString("title");
                String ED = RS.getString("title");
                _title.setText(ED);

                _sell.setText("" + Selling_Price);
                _PID.setText(RS.getString("bid"));

                String TY = RS.getString("val");
                if (TY.equals("1")) {
                    _type.setText("Pieces");
                } else if (TY.equals("0")) {
                    _type.setText("Kg");
                }
                _qtt.setEditable(true);
                _qtt.grabFocus();

            } else {
                JOptionPane.showMessageDialog(this, "Can't Load Product Data !");
                Clear();

            }

        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Stock/getProductdata" + " : " + e);

        }

    }

    public Home_UI() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        jTable1.getTableHeader().setFont(new Font("Segeo UI", Font.PLAIN, 16));

        getuserdata();
        Clear();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/home.png")));
        getMessage();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        VAL = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        _PID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        _qtt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        _type = new javax.swing.JTextField();
        _D_P = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        _title = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        _sell = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        _tot = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        _D_T = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        _net_tot = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        _customer = new javax.swing.JComboBox<>();
        _IS_CO = new javax.swing.JCheckBox();
        _cash = new javax.swing.JTextField();
        _balance = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        btn_Print = new javax.swing.JButton();
        btn_Cancel = new javax.swing.JButton();
        _message = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        jMenuItem2.setText("Delete");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "name", "qtt", "net"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        VAL.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        VAL.setBorder(null);
        VAL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VALActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setToolTipText("");

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 102, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Product ID", "Qtt", "Unit Price", "Price", "Discount", "Net Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(24);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("#INVOICE");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel4.setForeground(new java.awt.Color(52, 73, 94));

        _PID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _PID.setForeground(new java.awt.Color(51, 51, 51));
        _PID.setToolTipText("The unique code of the item / Barcode");
        _PID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        _PID.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        _PID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _PIDActionPerformed(evt);
            }
        });
        _PID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _PIDKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _PIDKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _PIDKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Quantity     :");

        _qtt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _qtt.setForeground(new java.awt.Color(51, 51, 51));
        _qtt.setToolTipText("Buying Quantity");
        _qtt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        _qtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _qttActionPerformed(evt);
            }
        });
        _qtt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _qttKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _qttKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _qttKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Barcode #   :");
        jLabel17.setToolTipText("");

        _type.setEditable(false);
        _type.setBackground(new java.awt.Color(255, 255, 255));
        _type.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _type.setForeground(new java.awt.Color(51, 51, 51));
        _type.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        _type.setBorder(null);

        _D_P.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _D_P.setForeground(new java.awt.Color(51, 51, 51));
        _D_P.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        _D_P.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _D_PActionPerformed(evt);
            }
        });
        _D_P.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _D_PKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _D_PKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _D_PKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("Discount (%)  :");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Item Name  :");
        jLabel24.setToolTipText("");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 51, 51));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel25.setText("Selling Price :");
        jLabel25.setToolTipText("");

        _title.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _title.setForeground(new java.awt.Color(51, 51, 51));
        _title.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        _title.setText("1100.00");
        _title.setToolTipText("");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Rs.");
        jLabel27.setToolTipText("");

        _sell.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _sell.setForeground(new java.awt.Color(51, 51, 51));
        _sell.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        _sell.setText("1100.00");
        _sell.setToolTipText("");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_sell, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_title, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(_title, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27)
                    .addComponent(_sell))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(51, 51, 51));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add16.png"))); // NOI18N
        jButton1.setText("Add");
        jButton1.setToolTipText("Add Item(s) to List");
        jButton1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(51, 51, 51));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Close_x16.png"))); // NOI18N
        jButton2.setText("Cancel");
        jButton2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Payment Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        jLabel20.setBackground(new java.awt.Color(51, 51, 51));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Total : RS.");

        _tot.setEditable(false);
        _tot.setBackground(new java.awt.Color(255, 255, 255));
        _tot.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        _tot.setForeground(new java.awt.Color(38, 222, 129));
        _tot.setText("0.00");
        _tot.setBorder(null);

        jLabel12.setBackground(new java.awt.Color(102, 102, 102));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Discount (%) :");

        _D_T.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _D_T.setForeground(new java.awt.Color(51, 51, 51));
        _D_T.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        _D_T.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _D_TKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _D_TKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _D_TKeyTyped(evt);
            }
        });

        jLabel8.setBackground(new java.awt.Color(102, 102, 102));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Net Total :Rs.");

        _net_tot.setEditable(false);
        _net_tot.setBackground(new java.awt.Color(255, 255, 255));
        _net_tot.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        _net_tot.setForeground(new java.awt.Color(41, 128, 185));
        _net_tot.setText("0.00");
        _net_tot.setBorder(null);

        jLabel10.setBackground(new java.awt.Color(102, 102, 102));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Customer:");

        _customer.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        _customer.setForeground(new java.awt.Color(0, 0, 255));
        _customer.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        _customer.setEnabled(false);
        _customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _customerActionPerformed(evt);
            }
        });

        _IS_CO.setBackground(new java.awt.Color(255, 255, 255));
        _IS_CO.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        _IS_CO.setForeground(new java.awt.Color(51, 51, 51));
        _IS_CO.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        _IS_CO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _IS_COActionPerformed(evt);
            }
        });

        _cash.setEditable(false);
        _cash.setBackground(new java.awt.Color(255, 255, 255));
        _cash.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        _cash.setForeground(new java.awt.Color(136, 84, 208));
        _cash.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        _cash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _cashKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _cashKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _cashKeyTyped(evt);
            }
        });

        _balance.setEditable(false);
        _balance.setBackground(new java.awt.Color(255, 255, 255));
        _balance.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        _balance.setForeground(new java.awt.Color(204, 0, 0));
        _balance.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel22.setBackground(new java.awt.Color(51, 51, 51));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("Cash : RS.");

        jLabel26.setBackground(new java.awt.Color(51, 51, 51));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 51, 51));
        jLabel26.setText("Balance : RS.");

        btn_Print.setBackground(new java.awt.Color(140, 201, 84));
        btn_Print.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_Print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer-32.png"))); // NOI18N
        btn_Print.setText("Print");
        btn_Print.setToolTipText("Save invoice and complete all process in the invoice with printout");
        btn_Print.setContentAreaFilled(false);
        btn_Print.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Print.setOpaque(true);
        btn_Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PrintActionPerformed(evt);
            }
        });

        btn_Cancel.setBackground(new java.awt.Color(255, 102, 102));
        btn_Cancel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btn_Cancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel.png"))); // NOI18N
        btn_Cancel.setText("Cancel");
        btn_Cancel.setToolTipText("Clear all added details in this invoice and refresh to a new Invoice");
        btn_Cancel.setContentAreaFilled(false);
        btn_Cancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Cancel.setOpaque(true);
        btn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(_D_T, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel26))
                                .addGap(27, 27, 27))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(_net_tot)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(_cash, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_balance, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_IS_CO)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_customer, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(_IS_CO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(_customer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_cash, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(_D_T, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(_net_tot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(_balance, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Print)
                            .addComponent(btn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel6))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(_qtt, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(_type, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel23))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(_D_P, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(_PID, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(100, 100, 100)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_PID, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_qtt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(_D_P, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(_type, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        _message.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _message.setForeground(new java.awt.Color(102, 102, 102));
        _message.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        _message.setText("      ------good morning from Apkhub-------");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("* press F1 to choose Product");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("* press F2 to add new item");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("* press F3 to add payment");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("* press F4 to clear all and get new bill");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("* All Price Values Are LKR");

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/change_price_24.png"))); // NOI18N
        jButton3.setText("NEW INVOICE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(_message, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel19)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel16)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_message, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel21)
                    .addComponent(jLabel16))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jMenuBar2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar2.setBorder(null);

        jMenu3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenu3.setText("File");
        jMenu3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem7.setText("Log Out");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuBar2.add(jMenu3);

        jMenu4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMenu4.setText("Menu");
        jMenu4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMenuItem3.setText("Customer Details");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem10.setText("Product Details");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuItem4.setText("Seller Details");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuItem6.setText("Reports");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem8.setText("Show Invoice");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem5.setText("Stock Details");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        if (evt.getButton() == 1) {
            DefaultTableModel DTM4 = (DefaultTableModel) jTable1.getModel();
            DTM4.removeRow(row);
            Clear();
            getTot();
            _PID.setText("");
            _PID.grabFocus();

        }
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void _customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__customerActionPerformed
        String VAL = _customer.getSelectedItem().toString();
        String[] V = VAL.split("   ");
        int Id = Integer.parseInt(V[0]);
        Customer_ID = V[0];
        System.out.println(Customer_ID);

    }//GEN-LAST:event__customerActionPerformed

    private void _cashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__cashKeyReleased
        if (KEY == 113) {
            Clear();

        }
        if (KEY == 115) {
            clearAll();

        }
    }//GEN-LAST:event__cashKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        ADDB();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void _qttKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyTyped
        if (evt.getKeyChar() == '0') {
            if (_qtt.getText().equals("")) {
                evt.consume();
            }
        }
        if (_type.getText().equals("Kg")) {
            String QC = _qtt.getText();
            if (QC.equals("")) {
                if (KEY == 46 | KEY == 110) {

                } else if (!(Character.isDigit(evt.getKeyChar()))) {
                    evt.consume();
                }

            } else {
                int counter = 0;
                for (int i = 0; i < QC.length(); i++) {
                    if (QC.charAt(i) == '.') {
                        counter++;
                    }
                }
                if (counter >= 1) {
                    if (KEY == 46 | KEY == 110) {
                        evt.consume();
                    }
                }

            }

        } else if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }

    }//GEN-LAST:event__qttKeyTyped

    private void _qttKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyReleased
        if (KEY == 113) {

            _PID.setText("");
            Clear();
        }
        if (KEY == 115) {
            clearAll();

        }
    }//GEN-LAST:event__qttKeyReleased

    private void _qttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__qttActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__qttActionPerformed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        if (!(row < 0)) {
            if (evt.isPopupTrigger()) {

                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        if (evt.getButton() == 3) {
            row = jTable1.getSelectedRow();
        }
    }//GEN-LAST:event_jTable1MousePressed

    private void _PIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__PIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__PIDActionPerformed

    private void _PIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyReleased

    }//GEN-LAST:event__PIDKeyReleased

    private void _PIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyTyped

        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__PIDKeyTyped

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        new Product_UI().setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void _D_PActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__D_PActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__D_PActionPerformed

    private void _PIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyPressed
        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {
            InEnterPid();
        }
        if (KEY == 112) {
            ItemNameSearchDialog dialog = new ItemNameSearchDialog(this, true);
            dialog.setVisible(true);
        }
        if (KEY == 113) {
            Clear();
        }
        if (KEY == 114) {
            _D_T.grabFocus();

        }
        if (KEY == 115) {
            clearAll();

        }

    }//GEN-LAST:event__PIDKeyPressed

    private void _qttKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyPressed
        KEY = evt.getKeyCode();
        System.out.println(KEY);
        if (evt.getKeyCode() == 10) {
            _D_P.grabFocus();
        }


    }//GEN-LAST:event__qttKeyPressed

    private void _D_PKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_PKeyPressed

        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {
            if (_D_P.getText().equals("")) {

                _D_P.setText(" 0");
            }
            if (_D_P.getText().length() == 1) {
                if (_D_P.getText().equals(".")) {

                    _D_P.setText(" 0");
                }
            }
            if (evt.getKeyCode() == 10) {
                ADDB();
            }

        }
    }//GEN-LAST:event__D_PKeyPressed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new Customer_UI().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new Supplier_UI().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new Report_UI().setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void _D_PKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_PKeyTyped
        if (KEY == 46 | KEY == 110) {
            String QC = _D_P.getText();
            int counter = 0;
            for (int i = 0; i < QC.length(); i++) {
                if (QC.charAt(i) == '.') {
                    counter++;
                }
            }
            if (counter >= 1) {
                if (KEY == 46 | KEY == 110) {
                    evt.consume();
                }
            }

        } else if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();

        }
    }//GEN-LAST:event__D_PKeyTyped

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        new Login_UI().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void _cashKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__cashKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__cashKeyTyped

    private void _cashKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__cashKeyPressed

        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {

            if (!(_cash.getText().equals(""))) {

                inEnderCash();

            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Cash Value !");
                _cash.grabFocus();
            }

        }
    }//GEN-LAST:event__cashKeyPressed

    private void _D_TKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_TKeyPressed
        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {
            if (_D_T.getText().equals("")) {

                _D_T.setText("0");

            }
            if (_D_T.getText().length() == 1) {
                if (_D_T.getText().equals(".")) {

                    _D_T.setText("0");
                }
            }
            inEnterTD();
            _cash.setEditable(true);
            _cash.grabFocus();

        }
    }//GEN-LAST:event__D_TKeyPressed

    private void _D_TKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_TKeyTyped
        if (KEY == 46 | KEY == 110) {
            String QC = _D_T.getText();
            int counter = 0;
            for (int i = 0; i < QC.length(); i++) {
                if (QC.charAt(i) == '.') {
                    counter++;

                }
            }
            if (counter >= 1) {
                if (KEY == 46 | KEY == 110) {
                    evt.consume();
                }
            }

        } else if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();

        }
    }//GEN-LAST:event__D_TKeyTyped

    private void _D_TKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_TKeyReleased
        if (!_D_T.getText().isEmpty()) {
            inEnterTD();
        }
    }//GEN-LAST:event__D_TKeyReleased

    private void _IS_COActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__IS_COActionPerformed
        if (!(_IS_CO.isSelected())) {
            _customer.setEnabled(false);
            Customer_ID = "1";

        }
        if (_IS_CO.isSelected()) {
            _customer.setEnabled(true);
            try {
                String Sql = "SELECT id,name FROM customer";
                ResultSet RS = jdbc.getData(Sql);
                Vector v = new Vector();

                while (RS.next()) {
                    v.add(RS.getString("title") + "   " + RS.getString("id"));
                    _customer.setModel(new DefaultComboBoxModel(v));

                }
            } catch (Exception e) {
                Loger.setLoger();
                Logger log = Logger.getLogger("Log");
                log.info("Exception CLASS/Stock/loadproduct" + " : " + e);

            }

            _customer.setEditable(true);
        }
    }//GEN-LAST:event__IS_COActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new Show_Bill().setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new Stock_UI().setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void _D_PKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__D_PKeyReleased
        if (KEY == 112) {
            _PID.setText("");
            ItemNameSearchDialog dialog = new ItemNameSearchDialog(this, true);
            dialog.setVisible(true);
        }
        if (KEY == 113) {
            Clear();
        }
        if (KEY == 114) {
            _cash.setEditable(true);
            _cash.grabFocus();

        }
        if (KEY == 115) {
            clearAll();

        }
    }//GEN-LAST:event__D_PKeyReleased

    private void VALActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VALActionPerformed

    }//GEN-LAST:event_VALActionPerformed

    private void btn_PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PrintActionPerformed
        if (JOptionPane.showConfirmDialog(rootPane, "Do You Want To Save Invoice ?") == 0) {
            beforCheckOut();
        }

    }//GEN-LAST:event_btn_PrintActionPerformed

    private void btn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelActionPerformed
        clearAll();

    }//GEN-LAST:event_btn_CancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new Home_UI().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField VAL;
    private javax.swing.JTextField _D_P;
    private javax.swing.JTextField _D_T;
    private javax.swing.JCheckBox _IS_CO;
    private javax.swing.JTextField _PID;
    private javax.swing.JTextField _balance;
    private javax.swing.JTextField _cash;
    private javax.swing.JComboBox<String> _customer;
    private javax.swing.JLabel _message;
    private javax.swing.JTextField _net_tot;
    private javax.swing.JTextField _qtt;
    private javax.swing.JLabel _sell;
    private javax.swing.JLabel _title;
    private javax.swing.JTextField _tot;
    private javax.swing.JTextField _type;
    private javax.swing.JButton btn_Cancel;
    private javax.swing.JButton btn_Print;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    // End of variables declaration//GEN-END:variables

    private boolean loadCustomer() {
        boolean val = false;
        try {
            String Sql = "SELECT id,name FROM customer WHERE id !='0'";
            ResultSet RS = jdbc.getData(Sql);
            Vector v = new Vector();

            while (RS.next()) {
                v.add(RS.getString("id") + "   " + RS.getString("name"));
                _customer.setModel(new DefaultComboBoxModel(v));
                val = true;

            }
        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Home/loadproduct" + " : " + e);
            val = false;
        }

        return val;
    }

    private void isSet() {
        type = 2;
        _cash.setText("");
        _cash.setEditable(false);
        _customer.setEnabled(false);
        loadCustomer();
    }

    private boolean checkOutCredit() {
        boolean val = false;
        String TOTAL = _tot.getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat datetime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        String DATE = dateFormat.format(cal.getTime());
        String TIME = datetime.format(cal.getTime());
        String TYPE = "credit";
        String CUSTOMER_ID = Customer_ID;

        try {
            String SQL = "INSERT INTO bill(total,cash,balance,added_date,added_time,type,customer_id) "
                    + "VALUES('" + TOTAL + "','0.0','0.0','" + DATE + "','" + TIME + "','" + TYPE + "','" + CUSTOMER_ID + "')";
            int I = jdbc.putData(SQL);
            if (I == 1) {
                String SQL2 = "select bill_id from bill where added_time='" + TIME + "' AND added_date='" + DATE + "'";
                ResultSet RS2 = jdbc.getData(SQL2);
                if (RS2.next()) {

                    String BILL_ID = RS2.getString("bill_id");
                    int sler = jTable1.getRowCount();

                    for (int i = 0; i < sler; i++) {
                        String P_ID = jTable1.getValueAt(i, 1).toString();
                        String Q = jTable1.getValueAt(i, 2).toString();
                        String[] QQ = Q.split(":");
                        String QTT = QQ[0];
                        String SE_PR = jTable1.getValueAt(i, 3).toString();
                        String PRICE = jTable1.getValueAt(i, 4).toString();

                        ResultSet RS3 = jdbc.getData("SELECT qtt FROM product WHERE id='" + P_ID + "'");
                        if (RS3.next()) {
                            double QTT_IN = RS3.getDouble("qtt");
                            double NEW_QTT = QTT_IN - (Double.parseDouble(QTT));

                            jdbc.putData("UPDATE product SET qtt='" + NEW_QTT + "' WHERE id='" + P_ID + "'");

                        }

                        System.out.println(BILL_ID);
                        String Sql3 = "INSERT INTO cash_sale(Qtt,selling_price,price,product_id,bill_id)"
                                + "VALUES('" + QTT + "','" + SE_PR + "','" + PRICE + "','" + P_ID + "','" + BILL_ID + "')";
                        jdbc.putData(Sql3);

                    }
                    Clear();
                    val = true;
                }

            } else {
                val = false;
            }

        } catch (Exception e) {
            val = false;
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Home/checkoutCredit" + " : " + e);
        }

        return val;
    }

    private boolean checkOutCash() {

        boolean val = false;
        double B = (Double.parseDouble(_cash.getText())) - (Double.parseDouble(_tot.getText()));
        _balance.setText("" + B);
        String BALANCE = _balance.getText();
        String CASH = _cash.getText();
        String TOTAL = _tot.getText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat datetime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        String DATE = dateFormat.format(cal.getTime());
        String TIME = datetime.format(cal.getTime());
        String TYPE = "cash";
        String CUSTOMER_ID = "0";

        try {
            String SQL = "INSERT INTO bill(total,cash,balance,added_date,added_time,type,customer_id) "
                    + "VALUES('" + TOTAL + "','" + CASH + "','" + BALANCE + "','" + DATE + "','" + TIME + "','" + TYPE + "','" + CUSTOMER_ID + "')";
            int I = jdbc.putData(SQL);
            if (I == 1) {
                String SQL2 = "select bill_id from bill where added_time='" + TIME + "' AND added_date='" + DATE + "'";
                ResultSet RS2 = jdbc.getData(SQL2);
                if (RS2.next()) {

                    String BILL_ID = RS2.getString("bill_id");
                    int sler = jTable1.getRowCount();

                    for (int i = 0; i < sler; i++) {
                        String P_ID = jTable1.getValueAt(i, 1).toString();
                        String Q = jTable1.getValueAt(i, 2).toString();
                        String[] QQ = Q.split(":");
                        String QTT = QQ[0];
                        String SE_PR = jTable1.getValueAt(i, 3).toString();
                        String PRICE = jTable1.getValueAt(i, 4).toString();

                        ResultSet RS3 = jdbc.getData("SELECT qtt FROM product WHERE id='" + P_ID + "'");
                        if (RS3.next()) {
                            double QTT_IN = RS3.getDouble("qtt");
                            double NEW_QTT = QTT_IN - (Double.parseDouble(QTT));

                            jdbc.putData("UPDATE product SET qtt='" + NEW_QTT + "' WHERE id='" + P_ID + "'");

                        }

                        String Sql3 = "INSERT INTO cash_sale(Qtt,selling_price,price,product_id,bill_id)"
                                + "VALUES('" + QTT + "','" + SE_PR + "','" + PRICE + "','" + P_ID + "','" + BILL_ID + "')";
                        jdbc.putData(Sql3);

                    }

                    val = true;

                }

            } else {
                val = false;
            }

        } catch (Exception e) {
            val = false;
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Home/checkoutCredit" + " : " + e);
        }

        return val;

    }

    private void printBill(String VAL) {
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        Clear();

    }

}
