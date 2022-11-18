/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.Loger;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Mayura Lakshan
 */
public class Stock_Return_UI extends javax.swing.JFrame {

    /**
     * Creates new form Stock_UI
     *
     *
     */
    int KEY;

    JDBC jdbc =JDBC.getInstance();
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

    public boolean loadProduct() {
        boolean val = false;
        try {
            String Sql = "SELECT id,bid,title FROM product WHERE available='1'";
            ResultSet RS = jdbc.getData(Sql);
            Vector v = new Vector();

            while (RS.next()) {
                v.add(RS.getString("title") + "   " + RS.getString("bid")+"   "+RS.getString("id"));
                _product.setModel(new DefaultComboBoxModel(v));
                val = true;

            }
        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Stock/loadproduct" + " : " + e);
            val = false;
        }

        return val;
    }

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
                    String Sql2 = "SELECT id,bid,title FROM product WHERE bid='" + ID + "' ";
                    ResultSet RS2 =JDBC.getInstance().getData(Sql2);

                    Vector v = new Vector();
                    while (RS2.next()) {
                        v.add(RS2.getString("title") + "   " + RS2.getString("bid")+"   "+RS.getString("id"));
                        _product.setModel(new DefaultComboBoxModel(v));
                        _product.setModel(new DefaultComboBoxModel(v));

                    }
                    _product.setEnabled(true);
                    IsSet = 1;

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
        DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
        dtm1.setRowCount(0);
        String Sql2 = "select * from stock_return";

        try {
            ResultSet RS =JDBC.getInstance().getData(Sql2);
            while (RS.next()) {
                Vector vec = new Vector();
                vec.add(RS.getString("id"));
                vec.add(RS.getString("product_id"));
                vec.add(RS.getString("description"));
                vec.add(RS.getString("qtt"));
                vec.add(RS.getString("date"));
                vec.add(RS.getString("time"));
                String S="SELECT val FROM product WHERE id='"+RS.getString("product_id")+"'";
                ResultSet R=jdbc.getData(S);
                if(R.next()){
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
        if(Double.parseDouble(Qtt_available)>0.0){
        
        String DESC=JOptionPane.showInputDialog("Please Enter Description");
        if(DESC.equals("")){
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
            JOptionPane.showMessageDialog(this,"Cant Return Stock.Qtt Is 0");
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
        _product.setEnabled(false);
        setDateTime();
        _qtt.setEditable(false);
        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
        dtm.setRowCount(0);
        loadDataToTable();
        _PID.grabFocus();
    }

    public Stock_Return_UI() {
        initComponents();
        AutoCompleteDecorator.decorate(_product);
        _product.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(!(_product.getEditor().getItem().equals(""))){
                    
                    
                    if (e.getKeyCode() == 10) {
                        
                    IsSet = 0;
                    String VAL = _product.getSelectedItem().toString();
                    String[] V = VAL.split("   ");
                    _PID.setText(V[1]);
                    _product.setEnabled(false);
                    getData(V[2]);
                    _qtt.grabFocus();

                }
                
                
                }
            }
        });
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        _title = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        _buying = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        _selling = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        _qtt_available = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        _qtt = new javax.swing.JTextField();
        _qtt_type = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        _date = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        _time = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        _PID = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        _product = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(39, 174, 96));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(64, 115, 158));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 500));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel2.setText("Product title :");

        _title.setEditable(false);
        _title.setBackground(new java.awt.Color(41, 128, 185));
        _title.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _title.setForeground(new java.awt.Color(255, 255, 0));

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel3.setText("Buying Price :");

        _buying.setEditable(false);
        _buying.setBackground(new java.awt.Color(41, 128, 185));
        _buying.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _buying.setForeground(new java.awt.Color(255, 255, 0));
        _buying.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _buyingActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel4.setText("Selling Price :");

        _selling.setEditable(false);
        _selling.setBackground(new java.awt.Color(41, 128, 185));
        _selling.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _selling.setForeground(new java.awt.Color(255, 255, 0));

        jLabel5.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel5.setText("Qtt Available :");

        _qtt_available.setEditable(false);
        _qtt_available.setBackground(new java.awt.Color(41, 128, 185));
        _qtt_available.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _qtt_available.setForeground(new java.awt.Color(255, 255, 0));

        jLabel6.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel6.setText("Qtt :");

        _qtt.setEditable(false);
        _qtt.setBackground(new java.awt.Color(241, 196, 15));
        _qtt.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _qtt.setForeground(new java.awt.Color(51, 51, 51));
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
        _qtt_type.setBackground(new java.awt.Color(41, 128, 185));
        _qtt_type.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _qtt_type.setForeground(new java.awt.Color(255, 255, 0));

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel7.setText("Date :");

        _date.setEditable(false);
        _date.setBackground(new java.awt.Color(41, 128, 185));
        _date.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _date.setForeground(new java.awt.Color(255, 255, 0));
        _date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _dateActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel8.setText("Time :");

        _time.setEditable(false);
        _time.setBackground(new java.awt.Color(41, 128, 185));
        _time.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _time.setForeground(new java.awt.Color(255, 255, 0));

        jTable1.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
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
        jScrollPane1.setViewportView(jTable1);

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        _PID.setBackground(new java.awt.Color(241, 196, 15));
        _PID.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        _PID.setForeground(new java.awt.Color(51, 51, 51));
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

        jLabel10.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel10.setText("Bacord ID:");

        _product.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        _product.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CHOOSE PRODUCT" }));
        _product.setEnabled(false);
        _product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                _productMouseClicked(evt);
            }
        });
        _product.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _productActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 204));
        jLabel13.setText("* press f2 to New");

        jLabel14.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 204));
        jLabel14.setText("* press f1 to choose Product");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel10)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(_title, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(_qtt, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(_qtt_type, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(_selling, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(_date)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(_time, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(_qtt_available, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 229, Short.MAX_VALUE)
                                        .addComponent(jLabel3))
                                    .addComponent(_PID))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(_buying, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(_product, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(4, 4, 4))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1)))
                .addGap(20, 20, 20))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(_PID, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addComponent(_product, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_title, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(_buying, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(_selling, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(_qtt_available, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(_time, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(_date, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(_qtt, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(_qtt_type, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119))
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 960, 440));

        jLabel9.setFont(new java.awt.Font("Cambria", 0, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Stock Return Details");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 420, 40));

        jLabel11.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("* All Price Values Are LKR");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 40, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1068, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 499, Short.MAX_VALUE)
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

    private void _PIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {

            evt.consume();

        }

    }//GEN-LAST:event__PIDKeyTyped

    private void _PIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__PIDKeyReleased
        System.out.println(KEY);
        if (KEY == 112) {

            _product.setEnabled(true);
            loadProduct();
            IsSet = 0;

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

    private void _productActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__productActionPerformed
        
    }//GEN-LAST:event__productActionPerformed

    private void _productMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event__productMouseClicked

    }//GEN-LAST:event__productMouseClicked

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
            java.util.logging.Logger.getLogger(Stock_Return_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Stock_Return_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Stock_Return_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Stock_Return_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stock_Return_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField _PID;
    private javax.swing.JTextField _buying;
    private javax.swing.JTextField _date;
    private javax.swing.JComboBox<String> _product;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
