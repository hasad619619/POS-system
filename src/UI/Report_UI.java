/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.data;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mayura Lakshan
 */
public class Report_UI extends javax.swing.JFrame {

    String path = new data().path();
    String Name = "";
    String C = "";
    JDBC jdbc = JDBC.getInstance();

    public void SalesReport(String MIN_DATE, String MAX_DATE) {
        
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultTableModel dtmm = (DefaultTableModel) jTable2.getModel();
        dtmm.setRowCount(0);
        double net = 0.0;
        double cost = 0.0;
        double profit = 0.0;
        try {
            jButton11.setEnabled(false);
            String SQL122 = "select bill_id from bill where added_date >='" + MIN_DATE + "'"
                    + "and added_date <='" + MAX_DATE + "'";
            ResultSet RS122 = JDBC.getInstance().getData(SQL122);
            while (RS122.next()) {
                String SQL22 = "SELECT * FROM cash_sale WHERE bill_id='" + RS122.getString("bill_id") + "'";
                ResultSet RS12 = jdbc.getData(SQL22);

                while (RS12.next()) {
                    Vector vec = new Vector();
                    vec.add(RS12.getString("product_id"));
                    String SQL7 = "SELECT title FROM product WHERE id='" + RS12.getString("product_id") + "'";
                    ResultSet RS7 = jdbc.getData(SQL7);
                    if (RS7.next()) {
                        vec.add(RS7.getString("title"));
                    }
                    vec.add(RS12.getString("Qtt"));
                    vec.add(RS12.getString("selling_price"));
                    vec.add(Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS12.getString("selling_price")));
                    net = Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS12.getString("selling_price")) + net;
                    String SQL14 = "SELECT buying_price FROM product WHERE id='" + RS12.getString("product_id") + "'";
                    ResultSet RS14 = jdbc.getData(SQL14);
                    if (RS14.next()) {
                        vec.add(RS14.getString("buying_price"));
                        vec.add(Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS14.getString("buying_price")));
                        cost = Double.parseDouble(RS12.getString("Qtt")) * Double.parseDouble(RS14.getString("buying_price")) + cost;
                    }
                    DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                    dtm.addRow(vec);
                    System.out.println(vec);
                }
            }
            String SQL = "select * from details where id='1'";
            ResultSet rs = jdbc.getData(SQL);
            String NAME = "NO DATA PROVIDE";
            String ADDRESS = "NO DATA PROVIDE";
            String CONTACT = "NO DATA PROVIDE";
            if (rs.next()) {
                NAME = rs.getString("nf");
                ADDRESS = rs.getString("ad");
                CONTACT = rs.getString("cn");
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Calendar cal = Calendar.getInstance();
            DefaultTableModel dtmmm = (DefaultTableModel) jTable2.getModel();
            JRTableModelDataSource data = new JRTableModelDataSource(dtmmm);
            InputStream jr = getClass().getResourceAsStream("/reports/Day_Sale.jasper");
            Map<String, Object> para = new HashMap<String, Object>();
            para.put("MIN_DATE", MIN_DATE.replace("-", "/"));
            para.put("MAX_DATE", MAX_DATE.replace("-", "/"));
            para.put("NET", "Rs" + " " + net);
            para.put("COST", "Rs" + " " + cost);
            para.put("Date", dateFormat.format(cal.getTime()));
            para.put("na", NAME);
            para.put("cn", CONTACT);
            para.put("ad", ADDRESS);
            double V = net - cost;
            para.put("PROFIT", "Rs" + " " + V);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, data);
            JasperViewer.viewReport(jp, false);
            jButton11.setEnabled(true);
        } catch (Exception ex) {
            jButton11.setEnabled(true);
            System.out.println(ex);
        }
            }
        }).start();

    }

    public void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String MIN = mindate.getText();
                    String[] M = MIN.split("/");
                    String NEW_M_DATE = "20" + M[2] + "-" + M[0] + "-" + M[1];
                    String MAX = maxdate.getText();
                    String[] MX = MAX.split("/");
                    String NEW_MX_DATE = "20" + MX[2] + "-" + MX[0] + "-" + MX[1];
                    DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                    dtm.setRowCount(0);
                    jButton4.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    String SQL1 = "SELECT id,name FROM customer";
                    ResultSet RS = JDBC.getInstance().getData(SQL1);
                    while (RS.next()) {
                        String C = RS.getString("id");
                        String N = RS.getString("name");

                        String SQL2 = "SELECT SUM(total) AS Total FROM bill where customer_id='" + C + "' AND added_date >='" + NEW_M_DATE + "'"
                                + "and added_date <='" + NEW_MX_DATE + "'";
                        ResultSet RS2 = JDBC.getInstance().getData(SQL2);
                        if (RS2.next()) {
                            double VAL = RS2.getDouble("Total");
                            if (VAL > 0.0) {
                                Vector vec = new Vector();
                                vec.add(C);
                                vec.add(N);
                                vec.add(VAL);

                                dtm.addRow(vec);
                            }
                        }
                    }
                    DefaultTableModel dtmm = (DefaultTableModel) jTable1.getModel();
                    JRTableModelDataSource data = new JRTableModelDataSource(dtmm);

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream jr = getClass().getResourceAsStream("/reports/All_Customer.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("fd", NEW_M_DATE.replace("-", "/"));
                    para.put("ed", NEW_MX_DATE.replace("-", "/"));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(jr, para, data);
                    JasperViewer.viewReport(jp, false);
                    jButton4.setEnabled(true);

                } catch (Exception e) {
                    jButton4.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();

        try {

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void GenarateCustomer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton5.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }

                    String MIN = mindate.getText();
                    String[] M = MIN.split("/");
                    String NEW_M_DATE = "20" + M[2] + "-" + M[0] + "-" + M[1];
                    String MAX = maxdate.getText();
                    String[] MX = MAX.split("/");
                    String NEW_MX_DATE = "20" + MX[2] + "-" + MX[0] + "-" + MX[1];

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream jr = getClass().getResourceAsStream("/reports/Customer_Credit_Details.jasper");

                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("minDate", NEW_M_DATE.replace("-", "/"));
                    para.put("MaxDate", NEW_MX_DATE.replace("-", "/"));
                    para.put("C_ID", C);
                    para.put("Name", Name);
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(jr, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton5.setEnabled(true);

                } catch (Exception e) {
                    jButton5.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * Creates new form Report_UI
     */
    public Report_UI() {
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image/home.png")));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        mindate2 = new datechooser.beans.DateChooserCombo();
        maxdate2 = new datechooser.beans.DateChooserCombo();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        mindate = new datechooser.beans.DateChooserCombo();
        maxdate = new datechooser.beans.DateChooserCombo();
        jButton4 = new javax.swing.JButton();
        C_ID = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        mindate3 = new datechooser.beans.DateChooserCombo();
        jLabel8 = new javax.swing.JLabel();
        maxdate3 = new datechooser.beans.DateChooserCombo();
        jButton11 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "data", "data", "data", "data", "data", "data", "data"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(41, 128, 185));
        jLabel1.setText("Reports");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "GENERAL REPORTS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 16), new java.awt.Color(39, 174, 96))); // NOI18N

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(153, 0, 153));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton2.setText("Customer Details Report ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 0, 153));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton1.setText("Supplier Details Report ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(153, 0, 153));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton3.setText("All Products Available Stocks Report");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(153, 0, 153));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton8.setText("Zero Stocks Products");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "PURCHASE REPORTS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        jButton7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 102, 0));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton7.setText("Genarate Purchase Report");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("To");

        jButton10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(0, 102, 0));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton10.setText("Genarate Product Return Report");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mindate2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(6, 6, 6)
                .addComponent(maxdate2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addGap(18, 18, 18)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(290, 290, 290))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxdate2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mindate2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "SALES REPORTS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(153, 0, 153));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton4.setText("By All Customers");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        C_ID.setToolTipText("Customer ID");
        C_ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                C_IDActionPerformed(evt);
            }
        });
        C_ID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                C_IDKeyPressed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 0, 204));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton5.setText("Genarate Report By Customer ID With Invoice Details");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel8.setText("To");

        jButton11.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(0, 102, 0));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print_24.png"))); // NOI18N
        jButton11.setText("Genarate Sales Report By Invoice");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("To");

        jLabel2.setText("Customer ID:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(mindate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(13, 13, 13)
                        .addComponent(maxdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(mindate3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(maxdate3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(C_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(mindate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(maxdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(C_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(20, 20, 20)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(mindate3, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(maxdate3, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mindate.getAccessibleContext().setAccessibleDescription("36");
        maxdate.getAccessibleContext().setAccessibleDescription("36");
        jButton4.getAccessibleContext().setAccessibleDescription("36");
        C_ID.getAccessibleContext().setAccessibleDescription("36");
        jButton5.getAccessibleContext().setAccessibleDescription("36");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton2.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream reportSource = getClass().getResourceAsStream("/reports/Customer_Details.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(reportSource, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton2.setEnabled(true);

                } catch (Exception e) {
                    jButton2.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton1.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream reportSource = getClass().getResourceAsStream("/reports/Supplier_Details.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(reportSource, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton1.setEnabled(true);

                } catch (Exception e) {
                    jButton1.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton3.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream reportSource = getClass().getResourceAsStream("/reports/Stock_Details.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(reportSource, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton3.setEnabled(true);

                } catch (Exception e) {
                    jButton3.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        getData();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!(C_ID.getText().equals(""))) {
            try {
                C = C_ID.getText();
                String SQL = "select name from customer where id='" + C + "'";
                ResultSet rs = JDBC.getInstance().getData(SQL);
                if (rs.next()) {
                    Name = rs.getString("name");
                    GenarateCustomer();
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong ID !");
                    C_ID.grabFocus();
                }
            } catch (Exception e) {
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Customer ID !");
            C_ID.grabFocus();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton7.setEnabled(false);
                    String MIN = mindate2.getText();
                    String[] M = MIN.split("/");
                    String NEW_M_DATE = "20" + M[2] + "-" + M[0] + "-" + M[1];
                    String MAX = maxdate2.getText();
                    String[] MX = MAX.split("/");
                    String NEW_MX_DATE = "20" + MX[2] + "-" + MX[0] + "-" + MX[1];

                    try {
                        String SQL = "select * from details where id='1'";
                        ResultSet rs = JDBC.getInstance().getData(SQL);
                        String NAME = "NO DATA PROVIDE";
                        String ADDRESS = "NO DATA PROVIDE";
                        String CONTACT = "NO DATA PROVIDE";
                        if (rs.next()) {
                            NAME = rs.getString("nf");
                            ADDRESS = rs.getString("ad");
                            CONTACT = rs.getString("cn");
                        }
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Calendar cal = Calendar.getInstance();
                        InputStream jr = getClass().getResourceAsStream("/reports/All_Stock_Details.jasper");
                        Map<String, Object> para = new HashMap<String, Object>();
                        para.put("Date", dateFormat.format(cal.getTime()));
                        para.put("minDate", NEW_M_DATE.replace("-", "/"));
                        para.put("MaxDate", NEW_MX_DATE.replace("-", "/"));
                        para.put("na", NAME);
                        para.put("cn", CONTACT);
                        para.put("ad", ADDRESS);
                        JasperPrint jp = JasperFillManager.fillReport(jr, para, JDBC.getInstance().con());
                        JasperViewer.viewReport(jp, false);

                    } catch (Exception e) {

                    }
                    jButton7.setEnabled(true);

                } catch (Exception e) {
                    jButton7.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();


    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton8.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream reportSource = getClass().getResourceAsStream("/reports/Stock_out.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(reportSource, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton8.setEnabled(true);

                } catch (Exception e) {
                    jButton8.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jButton10.setEnabled(false);
                    String SQL = "select * from details where id='1'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    String NAME = "NO DATA PROVIDE";
                    String ADDRESS = "NO DATA PROVIDE";
                    String CONTACT = "NO DATA PROVIDE";
                    if (rs.next()) {
                        NAME = rs.getString("nf");
                        ADDRESS = rs.getString("ad");
                        CONTACT = rs.getString("cn");
                    }
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Calendar cal = Calendar.getInstance();
                    InputStream jr = getClass().getResourceAsStream("/reports/All_Return.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));

                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(jr, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    jButton10.setEnabled(true);

                } catch (Exception e) {
                    jButton10.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        String MIN = mindate3.getText();
        String[] M = MIN.split("/");
        String NEW_M_DATE = "20" + M[2] + "-" + M[0] + "-" + M[1];
        String MAX = maxdate3.getText();
        String[] MX = MAX.split("/");
        String NEW_MX_DATE = "20" + MX[2] + "-" + MX[0] + "-" + MX[1];
        SalesReport(NEW_M_DATE, NEW_MX_DATE);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void C_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_C_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_C_IDActionPerformed

    private void C_IDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_C_IDKeyPressed
        if (evt.getKeyCode() == 10) {
            if (!(C_ID.getText().equals(""))) {
                try {
                    C = C_ID.getText();
                    String SQL = "select name from customer where id='" + C + "'";
                    ResultSet rs = JDBC.getInstance().getData(SQL);
                    if (rs.next()) {
                        Name = rs.getString("name");
                        GenarateCustomer();
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong ID !");
                        C_ID.grabFocus();
                    }
                } catch (Exception e) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Customer ID !");
                C_ID.grabFocus();
            }

        }
    }//GEN-LAST:event_C_IDKeyPressed

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
            java.util.logging.Logger.getLogger(Report_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Report_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Report_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Report_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Report_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField C_ID;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private datechooser.beans.DateChooserCombo maxdate;
    private datechooser.beans.DateChooserCombo maxdate2;
    private datechooser.beans.DateChooserCombo maxdate3;
    private datechooser.beans.DateChooserCombo mindate;
    private datechooser.beans.DateChooserCombo mindate2;
    private datechooser.beans.DateChooserCombo mindate3;
    // End of variables declaration//GEN-END:variables
}
