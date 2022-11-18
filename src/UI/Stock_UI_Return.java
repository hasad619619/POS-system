/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.Loger;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Stock_UI_Return extends javax.swing.JFrame {

    /**
     * Creates new form Stock_UI
     *
     *
     */
    int KEY;

    JDBC jdbc = JDBC.getInstance();
    String P_ID;
    String Title;
    String Qtt_available;
    double buying;
    double Selling;
    double NetPrice;
    String Added_date;
    String Added_time;
    String Val;
    String Seller_id;
    String Qtt;
    String V;
    String PID;
    public static String N_ID;

    int IsSet = 0;

    public void inEnterPID() {
        String ID = _PID.getText();
        int I = ID.length();

        if (I == 4) {
            IsSet = 0;
        }
        if (I > 4) {
            IsSet = 0;
        }

        if (IsSet == 0) {
            try {
                String Sql = "SELECT id FROM product WHERE bid='" + ID + "' ";
                ResultSet RS =JDBC.getInstance().getData(Sql);
                int count = 0;
                while (RS.next()) {
                    count++;
                    PID = RS.getString("id");

                }
                if (count == 1) {
                    getData(PID);
                    _qtt.grabFocus();
                }
                if (count == 0) {
                    JOptionPane.showMessageDialog(this, "Wrong Product ID !");
                    
                    clear();
                    _PID.setText("");
                    _PID.setEditable(true);
                    _PID.grabFocus();
                }
                if (count > 1) {
                    InvoiceDuplicateItems_stock_return dialog = new InvoiceDuplicateItems_stock_return(this, true, ID);
                    dialog.setVisible(true);

                }
            } catch (Exception ex) {
                System.out.println("ccc" + ex);
            }

        }
        if (IsSet == 1) {

            if (!(_PID.getText().equals(""))) {

                getData(ID);

            } else {
                JOptionPane.showMessageDialog(this, "Plaese Enter Product ID !");

            }

        }

    }

    public void loadDataToTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockLoadingDialog sld = new StockLoadingDialog(null, false);
                sld.setVisible(true);
                DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
                dtm1.setRowCount(0);
                String Sql2 = "select * from stock_return";

                try {
                    ResultSet RS = JDBC.getInstance().getData(Sql2);
                    while (RS.next()) {
                        Vector vec = new Vector();
                        vec.add(RS.getString("id"));
                        vec.add(RS.getString("product_id"));
                        vec.add(RS.getString("description"));
                        vec.add(RS.getString("qtt"));
                        vec.add(RS.getString("date"));
                        vec.add(RS.getString("time"));
                        String S = "SELECT val FROM product WHERE id='" + RS.getString("product_id") + "'";
                        ResultSet R = jdbc.getData(S);
                        if (R.next()) {
                            String VV = R.getString("val");
                            if (VV.equals("0")) {
                                vec.add("Kg");
                            } else if (VV.equals("1")) {
                                vec.add("Pieces");
                            }

                        }

                        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                        dtm.addRow(vec);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                sld.dispose();
            }
        }).start();

    }

    public void UpdateQtt() {
        System.out.println("OK");
        
        double NewQtt = ((Double.parseDouble(Qtt_available))-(Double.parseDouble(Qtt)));
        String SQL2 = "UPDATE product SET qtt='" + NewQtt + "' WHERE id='" + P_ID + "'";
        try {
            jdbc.putData(SQL2);
            System.out.println(NewQtt);
            System.out.println(P_ID);
        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Product/UpdateQtt" + " : " + e);

        }
        
        
        

    }

    public void setData() {
        Qtt = _qtt.getText();
        NetPrice = ((Double.parseDouble(Qtt)) * buying);
        
        Added_date = _date.getText();
        Added_time = _time.getText();

    }

    public boolean saveData() {
        boolean VAL = false;
        if(Double.parseDouble(Qtt_available)>0.0 && Double.parseDouble(Qtt_available) >= Double.parseDouble(Qtt)){
        
        String DESC=JOptionPane.showInputDialog("Please Enter Description");
        if(DESC.isEmpty()){
            DESC="N/A";
        
        }
        
        String SQL = "INSERT INTO stock_return(description, date, time, product_id, qtt)"
                + "VALUES('"+DESC+"','"+Added_date+"','"+Added_time+"','"+P_ID+"','"+Qtt+"')";
        try {
            int I = jdbc.putData(SQL);
            if (I == 1) {
                VAL = true;
                UpdateQtt();
            }

        } catch (Exception ex) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Product/savedata" + " : " + ex);
            System.out.println(ex);
        }
        }else{
            JOptionPane.showMessageDialog(this,"Cant Return Stock , Qty Is 0 OR Qty > Available Stocks !");
            clear();
        
        }

        return VAL;
    }

    public void getData(String id) {
        System.out.println("In Get Data" + id);
        try {
            String Sql = "SELECT * FROM product WHERE id='" + id + "'";
            ResultSet RS2 = jdbc.getData(Sql);

            if (RS2.next()) {
                P_ID = RS2.getString("id");

                Title = RS2.getString("title");
                buying = RS2.getDouble("buying_price");
                Selling = RS2.getDouble("selling_price");
                Qtt_available = RS2.getString("qtt");
                Seller_id = RS2.getString("Seller_id");
                V = RS2.getString("val");
                if (V.equals("0")) {
                    Val = "Kg";
                } else if (V.equals("1")) {
                    Val = "Pieces";
                }
                _title.setText(Title);
                _buying.setText("" + buying);
                _selling.setText("" + Selling);
                _qtt_available.setText(Qtt_available);
                _qtt_type.setText(Val);
                _qtt.setEditable(true);
                _qtt.grabFocus();

            }

        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Product/getdata" + " : " + e);
        }

    }

    public void setDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat datetime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        _date.setText(dateFormat.format(cal.getTime()));
        _time.setText(datetime.format(cal.getTime()));
    }

    public void clear() {
        _title.setText("");
        _buying.setText("");
        _selling.setText("");
        _qtt.setText("");
        _qtt_available.setText("");
        _title.setText("");
        _date.setText("");
        _time.setText("");
        IsSet = 0;
        _PID.setText("");
        _PID.setEditable(true);
       
        setDateTime();
        _qtt.setEditable(false);
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        loadDataToTable();
         _PID.grabFocus();
    }

    public void onDuplicate(String BID, String ID) {
        IsSet = 0;

        _PID.setText(BID);

        getData(ID);
        _qtt.grabFocus();

    }

    public Stock_UI_Return() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        clear();
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

        _date = new javax.swing.JTextField();
        _time = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        _PID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        _qtt = new javax.swing.JTextField();
        _qtt_type = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        _title = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        _qtt_available = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        _buying = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        _selling = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        _date.setEditable(false);
        _date.setBackground(new java.awt.Color(41, 128, 185));
        _date.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _date.setForeground(new java.awt.Color(255, 255, 0));
        _date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _dateActionPerformed(evt);
            }
        });

        _time.setEditable(false);
        _time.setBackground(new java.awt.Color(41, 128, 185));
        _time.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _time.setForeground(new java.awt.Color(255, 255, 0));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PURCHASE RETURN");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("* press f1 to choose Product");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("* press f2 to New");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(41, 128, 185));
        jLabel9.setText("#PURCHASE RETURN ");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel11.setText("Purchase History :");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PURCHASE ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 51))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Bacord #");

        _PID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        _PID.setForeground(new java.awt.Color(39, 174, 96));
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

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setText("Returned Qty :");

        _qtt.setEditable(false);
        _qtt.setBackground(new java.awt.Color(255, 255, 255));
        _qtt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        _qtt.setForeground(new java.awt.Color(39, 174, 96));
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

        _qtt_type.setEditable(false);
        _qtt_type.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        _qtt_type.setForeground(new java.awt.Color(39, 174, 96));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel2.setText("Product title :");

        _title.setEditable(false);
        _title.setBackground(new java.awt.Color(204, 204, 204));
        _title.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _title.setForeground(new java.awt.Color(51, 51, 51));
        _title.setBorder(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Qty Available :");

        _qtt_available.setEditable(false);
        _qtt_available.setBackground(new java.awt.Color(204, 204, 204));
        _qtt_available.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _qtt_available.setForeground(new java.awt.Color(51, 51, 51));
        _qtt_available.setBorder(null);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setText("Buying Price : RS.");

        _buying.setEditable(false);
        _buying.setBackground(new java.awt.Color(204, 204, 204));
        _buying.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _buying.setForeground(new java.awt.Color(51, 51, 51));
        _buying.setBorder(null);
        _buying.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _buyingActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setText("Selling Price : RS.");

        _selling.setEditable(false);
        _selling.setBackground(new java.awt.Color(204, 204, 204));
        _selling.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _selling.setForeground(new java.awt.Color(51, 51, 51));
        _selling.setBorder(null);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(_buying, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(_selling))
                    .addComponent(_title, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_qtt_available, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(340, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(_qtt_available, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(_title, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(_buying, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(_selling, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add16.png"))); // NOI18N
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Close_x16.png"))); // NOI18N
        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(_PID, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(_qtt, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                                .addGap(4, 4, 4)
                                .addComponent(_qtt_type, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_PID, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_qtt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(_qtt_type, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 153, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock Return ID", "Product ID", "Description", "Returned Qtt", "Return Date", "Return Time", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(18);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(15, 15, 15)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void _qttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__qttActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__qttActionPerformed

    private void _buyingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__buyingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__buyingActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!(_qtt.getText().equals("")) & IsSet == 1) {

            setData();
            boolean VV = saveData();
            if (VV) {
                JOptionPane.showMessageDialog(this, " Saved Successfully !");
                clear();
            } else {
                JOptionPane.showMessageDialog(this, " Can't Save Please Retry ! !");
                clear();
                _PID.setText("");
                _PID.grabFocus();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Select Product And Enter Qtt !");
            clear();
            _PID.setText("");
            _PID.grabFocus();
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void _qttKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyTyped
        KEY = evt.getKeyCode();
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__qttKeyTyped

    private void _qttKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyReleased
        if (KEY == 113) {
            clear();
            _PID.setText("");
            _PID.grabFocus();

        }
    }//GEN-LAST:event__qttKeyReleased

    private void _dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__dateActionPerformed

    private void _PIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__PIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__PIDActionPerformed

    private void _PIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyPressed
        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {
            if (!(_PID.getText().equals(""))) {
                inEnterPID();
                _PID.setEditable(false);

            } else {
                JOptionPane.showMessageDialog(this, "Please Choose Product !");
                _PID.grabFocus();

            }

        }
    }//GEN-LAST:event__PIDKeyPressed

    public void clickPid(String ID) {
        _PID.setText(ID);
        inEnterPID();
    }

    private void _PIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {

            evt.consume();

        }

    }//GEN-LAST:event__PIDKeyTyped

    private void _PIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyReleased
        if (KEY == 112) {

            
            IsSet = 0;
            ItemNameSearchDialog_stock_return dialog = new ItemNameSearchDialog_stock_return(this, true);
            dialog.setVisible(true);

        }
        if (KEY == 113) {
            clear();
            _PID.setText("");
            _PID.grabFocus();

        }

    }//GEN-LAST:event__PIDKeyReleased

    private void _qttKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyPressed
        if (evt.getKeyCode() == 10) {
            if (!(_qtt.getText().equals(""))) {

                setData();
                boolean VV = saveData();
                if (VV) {
                    JOptionPane.showMessageDialog(this, " Saved Successfully !");
                    clear();
                } else {
                    JOptionPane.showMessageDialog(this, " Can't Save Please Retry ! !");
                    clear();
                    _PID.setText("");
                    _PID.grabFocus();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Select Product And Enter Qtt !");
                clear();
                _PID.setText("");
                _PID.grabFocus();
            }

        }
    }//GEN-LAST:event__qttKeyPressed

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
            java.util.logging.Logger.getLogger(Stock_UI_Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Stock_UI_Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Stock_UI_Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Stock_UI_Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stock_UI_Return().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField _PID;
    private javax.swing.JTextField _buying;
    private javax.swing.JTextField _date;
    private javax.swing.JTextField _qtt;
    private javax.swing.JTextField _qtt_available;
    private javax.swing.JTextField _qtt_type;
    private javax.swing.JTextField _selling;
    private javax.swing.JTextField _time;
    private javax.swing.JTextField _title;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void UpdateExp() {

        //String SQL = "UPDATE product SET expdate='" + _exp.getText() + "'";
        try {
            //jdbc.putData(SQL);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
