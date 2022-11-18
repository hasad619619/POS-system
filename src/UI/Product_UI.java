/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.Loger;
import CLASS.Product;
import static UI.Supplier_UI.flag;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Product_UI extends javax.swing.JFrame {

    /**
     * Creates new form Stock_UI
     */
    int KEY;
    Product PR = new Product();
    JDBC jdbc =JDBC.getInstance();
    String nextID = "0001";
    int isUpdate = 0;
    int row;
    String ID;
    String ProductID = "";
    int Seller_ID = 0;
    String Title;
    String Description;
    double Buying;
    double Selling;
    double Price = 0.0;
    String Exp;
    String Date;
    String Time;
    String Val;
    String BID;
    int Qtt;

    static boolean flag = false;

    public void getNextId() {
        String Sql = "SELECT id FROM product";
        try {
            ResultSet RS =JDBC.getInstance().getData(Sql);
            ArrayList<Integer> list = new ArrayList<Integer>();
            while (RS.next()) {
                int id = RS.getInt("id");

                list.add(id);
            }
            int min = list.get(0);
            int max = list.get(0);

            for (Integer i : list) {
                if (i < min) {
                    min = i;
                }
                if (i > max) {
                    max = i;
                }
            }

            int NEXTID = max + 1;
            String N = "" + NEXTID;
            int count = N.length();

            switch (count) {
                case 1:
                    nextID = "000" + NEXTID;
                    break;
                case 2:
                    nextID = "00" + NEXTID;
                    break;
                case 3:
                    nextID = "0" + NEXTID;
                    break;
                case 4:
                    if (NEXTID < 9999) {
                        nextID = "" + NEXTID;

                    } else if (NEXTID > 9999) {
                        JOptionPane.showMessageDialog(this, "New Product Limit Is Over Please Contact System provider!");
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "New Product Limit Is Over Please Contact System provider!");
                    break;
            }

        } catch (Exception e) {

        }

    }

    public void clearValues() {
        _product_id.setText("");
        _title.setText("");
        _description.setText("");
        _qtt.setText("0");
        _selling.setText("");
        _buying.setText("");
        _Bid.setText("");

        Seller_ID = 0;
        Val = null;
        Title = null;
        Description = null;
        Buying = 0.0;
        Selling = 0.0;
        Exp = null;
        Date = null;
        Time = null;
        BID = null;
        Qtt = 0;
        isUpdate = 0;
        _buying.setEditable(true);
        _selling.setEditable(true);
        _Bid.setEditable(true);
        _seller.setEnabled(true);
        _VAL.setEnabled(true);
        getNextId();
        _product_id.setText(nextID);
        loadSeller();
        setDateTime();
        loadDataToTable();
        _Bid.grabFocus();
    }

    public void loadDataToTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StockLoadingDialog sld = new StockLoadingDialog(null, false);
                sld.setVisible(true);
                DefaultTableModel dtm1 = (DefaultTableModel) jTable1.getModel();
                dtm1.setRowCount(0);
                String Sql2 = "select * from product WHERE available='1'";

                if (flag) {
                    Sql2 = "select * from product WHERE available='1' and title like '%" + _q.getText() + "%'";
                }

                try {
                    ResultSet RS =JDBC.getInstance().getData(Sql2);
                    while (RS.next()) {
                        Vector vec = new Vector();
                        vec.add(RS.getString("id"));
                        vec.add(RS.getString("title"));
                        vec.add(RS.getString("description"));
                        vec.add(RS.getString("qtt"));
                        String V = RS.getString("val");
                        if (V.equals("0")) {
                            vec.add("Kg");
                        } else if (V.equals("1")) {
                            vec.add("Pieces");
                        }
                        vec.add(RS.getString("buying_price"));
                        vec.add(RS.getString("selling_price"));
                        vec.add(RS.getString("added_date"));
                        vec.add(RS.getString("added_time"));
                        vec.add(RS.getString("Seller_id"));
                        String C = RS.getString("available");
                        if (C.equals("1")) {

                        } else if (C.equals("0")) {

                        }
                        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                        dtm.addRow(vec);
                    }
                } catch (Exception e) {

                }
                sld.dispose();
            }
        }).start();

    }

    public boolean setValue() {
        boolean val = false;
        if (true) {
            if (Seller_ID > 0) {
                BID = _Bid.getText();
                ProductID = _product_id.getText();
                Title = _title.getText();
                Description = _description.getText();
                Buying = Double.parseDouble(_buying.getText());
                Selling = Double.parseDouble(_selling.getText());
                Qtt = Integer.parseInt(_qtt.getText());
                String V = _VAL.getSelectedItem().toString();
                if (V.equals("Kg")) {
                    Val = "0";
                } else if (V.equals("Pieces")) {
                    Val = "1";
                }
                Date = _date.getText();
                Time = _time.getText();
                Exp = _date.getText();
                val = true;
            } else {
                JOptionPane.showMessageDialog(this, "Please Select Seler First !");
                _seller.grabFocus();
                val = false;
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Product ID First !");
            _product_id.grabFocus();
            val = false;
        }

        return val;

    }

    public boolean check() {

        boolean val = (!(_product_id.getText().equals("")) & !(_qtt.getText().equals("")) & !(_title.getText().equals("")) & !(_description.getText().equals("")) & !(_buying.getText().equals("")) & !(_selling.getText().equals("")));
        return val;

    }

    public void setDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat datetime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        _date.setText(dateFormat.format(cal.getTime()));
        _time.setText(datetime.format(cal.getTime()));
    }

    public void loadSeller() {
        try {
            String Sql = "SELECT id,title FROM seller";
            ResultSet RS = jdbc.getData(Sql);
            Vector v = new Vector();
            while (RS.next()) {
                v.add(RS.getString("id") + "/" + RS.getString("title"));
                _seller.setModel(new DefaultComboBoxModel(v));
            }
        } catch (Exception e) {
            Loger.setLoger();
            Logger log = Logger.getLogger("Log");
            log.info("Exception CLASS/Product/loadseller" + " : " + e);
        }

    }

    public Product_UI() {
        initComponents();
        clearValues();
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        _time = new javax.swing.JTextField();
        _date = new javax.swing.JTextField();
        _qtt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        _product_id = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        _Bid = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        _title = new javax.swing.JTextField();
        _seller = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        _VAL = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        _buying = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        _selling = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        _description = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        _print = new javax.swing.JButton();
        _q = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        jMenuItem1.setText("Update");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("Delete");
        jMenuItem2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem2MousePressed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        _time.setEditable(false);
        _time.setBackground(new java.awt.Color(243, 240, 96));
        _time.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        _time.setForeground(new java.awt.Color(51, 51, 255));
        _time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _timeActionPerformed(evt);
            }
        });

        _date.setEditable(false);
        _date.setBackground(new java.awt.Color(243, 240, 96));
        _date.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        _date.setForeground(new java.awt.Color(51, 51, 255));
        _date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _dateActionPerformed(evt);
            }
        });

        _qtt.setEditable(false);
        _qtt.setBackground(new java.awt.Color(243, 240, 96));
        _qtt.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        _qtt.setForeground(new java.awt.Color(51, 51, 255));
        _qtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _qttActionPerformed(evt);
            }
        });
        _qtt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _qttKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _qttKeyTyped(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stock Details");
        setResizable(false);

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(51, 51, 51));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Title", "Description", "Qty Available", "Qty Type", "Buying price", "Selling price", "Added Date", "Added Time", "Seller ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(18);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(41, 128, 185));
        jLabel8.setText("Stock Details");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SAVE NEW STOCK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Product  ID :");

        _product_id.setEditable(false);
        _product_id.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _product_id.setForeground(new java.awt.Color(155, 89, 182));
        _product_id.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _product_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _product_idActionPerformed(evt);
            }
        });
        _product_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _product_idKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Barcode  No :");

        _Bid.setBackground(new java.awt.Color(240, 240, 240));
        _Bid.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _Bid.setForeground(new java.awt.Color(155, 89, 182));
        _Bid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _Bid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _BidActionPerformed(evt);
            }
        });
        _Bid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _BidKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _BidKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Seller :");

        _title.setBackground(new java.awt.Color(240, 240, 240));
        _title.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _title.setForeground(new java.awt.Color(155, 89, 182));
        _title.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _titleActionPerformed(evt);
            }
        });
        _title.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _titleKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _titleKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _titleKeyTyped(evt);
            }
        });

        _seller.setBackground(new java.awt.Color(243, 240, 96));
        _seller.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _seller.setForeground(new java.awt.Color(155, 89, 182));
        _seller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _sellerActionPerformed(evt);
            }
        });
        _seller.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _sellerKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Product Title :");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Type:");

        _VAL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _VAL.setForeground(new java.awt.Color(155, 89, 182));
        _VAL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pieces", "Kg" }));
        _VAL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _VALActionPerformed(evt);
            }
        });
        _VAL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _VALKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Buying Price :");

        _buying.setBackground(new java.awt.Color(240, 240, 240));
        _buying.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _buying.setForeground(new java.awt.Color(155, 89, 182));
        _buying.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _buying.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _buyingActionPerformed(evt);
            }
        });
        _buying.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _buyingKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _buyingKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _buyingKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Selling Price :");

        _selling.setBackground(new java.awt.Color(240, 240, 240));
        _selling.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _selling.setForeground(new java.awt.Color(155, 89, 182));
        _selling.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _selling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _sellingActionPerformed(evt);
            }
        });
        _selling.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _sellingKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _sellingKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _sellingKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Description :");

        _description.setBackground(new java.awt.Color(240, 240, 240));
        _description.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _description.setForeground(new java.awt.Color(155, 89, 182));
        _description.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _description.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _descriptionActionPerformed(evt);
            }
        });
        _description.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _descriptionKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _descriptionKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _descriptionKeyTyped(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Close_x16.png"))); // NOI18N
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add16.png"))); // NOI18N
        jButton3.setText("Submit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1)
                    .addComponent(jLabel12)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_Bid)
                    .addComponent(_seller, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_title)
                    .addComponent(_VAL, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_buying)
                    .addComponent(_selling)
                    .addComponent(_description)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(_product_id, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 110, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_product_id, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_Bid, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_seller, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_title, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_VAL, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_buying, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_selling, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_description, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        _print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        _print.setText("PRINT ALL STOCK DETAILS");
        _print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _printActionPerformed(evt);
            }
        });

        _q.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _qActionPerformed(evt);
            }
        });
        _q.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _qKeyReleased(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_16.png"))); // NOI18N
        jButton1.setText("SEARCH");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/change_24.png"))); // NOI18N
        jButton4.setText("GET ALL");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel5.setText("Product Name :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(_print, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(_q, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton4))
                                    .addComponent(jLabel5))
                                .addGap(0, 247, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jScrollPane1)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(_print, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(_q)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void _titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__titleActionPerformed

    private void _product_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__product_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__product_idActionPerformed

    private void _sellingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__sellingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__sellingActionPerformed

    private void _dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__dateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__dateActionPerformed

    private void _descriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__descriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__descriptionActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clearValues();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        boolean ToDo = check();
        if (ToDo) {
            boolean val = setValue();
            if (val) {
                if (isUpdate == 0) {

                    if (BID.equals("")) {
                        int B = JOptionPane.showConfirmDialog(this, "Do You Want To Save Product Without Barcode ID !");
                        switch (B) {
                            case 0:
                                BID = ProductID;
                                boolean IsSave = PR.saveData(ProductID, BID, "" + Seller_ID, Title, Description, "" + Qtt, "" + Buying, "" + Selling, "" + Price, Date, Time, Exp, Val);
                                if (IsSave) {
                                    JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                                    clearValues();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Cant Save Date ! Please Try Again !");
                                    clearValues();
                                }
                                break;
                            case 1:
                                _Bid.grabFocus();
                                break;
                            default:
                                _Bid.grabFocus();
                                break;
                        }

                    } else {

                        boolean IsSave = PR.saveData(ProductID, BID, "" + Seller_ID, Title, Description, "" + Qtt, "" + Buying, "" + Selling, "" + Price, Date, Time, Exp, Val);
                        if (IsSave) {
                            JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                            clearValues();
                        } else {
                            JOptionPane.showMessageDialog(this, "Cant Save Date ! Please Try Again !");
                            clearValues();
                        }

                    }
                }
                if (isUpdate == 1) {
                    boolean IsOk = PR.updateData(Title, BID, Description, Buying, Selling, ID);
                    if (IsOk) {
                        JOptionPane.showMessageDialog(this, "Update Successfuly !");
                        clearValues();
                    } else {
                        JOptionPane.showMessageDialog(this, "Can't Update Data ! Please Try Again !");
                        clearValues();
                    }

                }

            } else {
                _seller.grabFocus();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Fill All Fields !");

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void _sellerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__sellerActionPerformed
        String VAL = _seller.getSelectedItem().toString();
        String[] V = VAL.split("/");
        Seller_ID = Integer.parseInt(V[0]);
        _title.grabFocus();
    }//GEN-LAST:event__sellerActionPerformed

    private void _titleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__titleKeyReleased

    }//GEN-LAST:event__titleKeyReleased

    private void _titleKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__titleKeyPressed
        if (evt.getKeyCode() == 10) {
            if (Seller_ID == 0) {
                JOptionPane.showMessageDialog(this, "Please Choose The Seller !");
                _seller.grabFocus();
            } else {
                _VAL.grabFocus();
            }

        }
    }//GEN-LAST:event__titleKeyPressed

    private void _titleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__titleKeyTyped
        if (Character.isDigit(evt.getKeyChar())) {

        }
    }//GEN-LAST:event__titleKeyTyped

    private void _descriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyTyped

    }//GEN-LAST:event__descriptionKeyTyped

    private void _descriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyReleased

    }//GEN-LAST:event__descriptionKeyReleased

    private void _qttKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyReleased
        if (evt.getKeyCode() == 10) {
            _buying.grabFocus();
        }
    }//GEN-LAST:event__qttKeyReleased

    private void _buyingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__buyingKeyReleased

    }//GEN-LAST:event__buyingKeyReleased

    private void _sellingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__sellingKeyReleased

    }//GEN-LAST:event__sellingKeyReleased

    private void _qttKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qttKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__qttKeyTyped

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
        if (evt.getButton() == 1) {
            ID = jTable1.getValueAt(row, 0).toString();
            String[] DATA = PR.getData(ID);
            _product_id.setText(DATA[0]);
            _title.setText(DATA[1]);
            _description.setText(DATA[2]);
            _qtt.setText(DATA[3]);
            _buying.setText(DATA[4]);
            _selling.setText(DATA[5]);
            _Bid.setText(DATA[7]);
            isUpdate = 1;
            Seller_ID = Integer.parseInt(ID);
            // _buying.setEditable(false);
            //_selling.setEditable(false);
            // _product_id.setEditable(false);
            // _Bid.setEditable(false);
            // _seller.setEnabled(false);
            // _VAL.setEnabled(false);

        }
    }//GEN-LAST:event_jMenuItem1MousePressed

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked

    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        if (evt.getButton() == 1) {
            ID = jTable1.getValueAt(row, 0).toString();
            boolean IsDeleted = PR.delete(ID);
            if (IsDeleted) {
                JOptionPane.showMessageDialog(this, "Successfully Deleted !");
                clearValues();
            } else {
                JOptionPane.showMessageDialog(this, "Cant Delete Please Try Again !");
                clearValues();
            }

        }
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
        if (evt.getButton() == 3) {
            row = jTable1.getSelectedRow();
        }
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        if (!(row < 0)) {
            if (evt.isPopupTrigger()) {

                jPopupMenu1.show(jTable1, evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_jTable1MouseReleased

    private void _buyingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__buyingKeyTyped
        if (KEY == 46 | KEY == 110) {

        } else if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__buyingKeyTyped

    private void _buyingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__buyingKeyPressed
        KEY = evt.getKeyCode();
        if (evt.getKeyCode() == 10) {
            _selling.grabFocus();
        }
    }//GEN-LAST:event__buyingKeyPressed

    private void _sellingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__sellingKeyTyped
        if (KEY == 46 | KEY == 110) {

        } else if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();
        }
    }//GEN-LAST:event__sellingKeyTyped

    private void _VALKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__VALKeyReleased
        if (evt.getKeyCode() == 10) {

        }
    }//GEN-LAST:event__VALKeyReleased

    private void _product_idKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__product_idKeyReleased
        if (evt.getKeyCode() == 10) {
            _Bid.grabFocus();
        }
    }//GEN-LAST:event__product_idKeyReleased

    private void _sellerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__sellerKeyReleased
        if (evt.getKeyCode() == 10) {

        }
    }//GEN-LAST:event__sellerKeyReleased

    private void _buyingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__buyingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__buyingActionPerformed

    private void _qttActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__qttActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__qttActionPerformed

    private void _timeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__timeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__timeActionPerformed

    private void _BidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__BidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__BidActionPerformed

    private void _BidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__BidKeyReleased

    }//GEN-LAST:event__BidKeyReleased

    private void _descriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyPressed
        if (evt.getKeyCode() == 10) {
            boolean ToDo = check();
            if (ToDo) {
                boolean val = setValue();
                if (val) {
                    if (isUpdate == 0) {

                        if (BID.equals("")) {
                            int B = JOptionPane.showConfirmDialog(this, "Do You Want To Save Product Without Barcode ID !");
                            switch (B) {
                                case 0:
                                    BID = ProductID;
                                    boolean IsSave = PR.saveData(ProductID, BID, "" + Seller_ID, Title, Description, "" + Qtt, "" + Buying, "" + Selling, "" + Price, Date, Time, Exp, Val);
                                    if (IsSave) {
                                        JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                                        clearValues();
                                    } else {
                                        JOptionPane.showMessageDialog(this, "Can't Save Date ! Please Try Again !");
                                        clearValues();
                                    }
                                    break;
                                case 1:
                                    _Bid.grabFocus();
                                    break;
                                default:
                                    _Bid.grabFocus();
                                    break;
                            }

                        } else {

                            boolean IsSave = PR.saveData(ProductID, BID, "" + Seller_ID, Title, Description, "" + Qtt, "" + Buying, "" + Selling, "" + Price, Date, Time, Exp, Val);
                            if (IsSave) {
                                JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                                clearValues();
                            } else {
                                JOptionPane.showMessageDialog(this, "Can't Save Date ! Please Try Again !");
                                clearValues();
                            }

                        }
                    }
                    if (isUpdate == 1) {
                        boolean IsOk = PR.updateData(Title, BID, Description, Buying, Selling, ID);
                        if (IsOk) {
                            JOptionPane.showMessageDialog(this, "Update Successfuly !");
                            clearValues();
                        } else {
                            JOptionPane.showMessageDialog(this, "Can't Update Data ! Please Try Again !");
                            clearValues();
                        }

                    }

                } else {
                    _seller.grabFocus();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All Fields !");

            }
        }
    }//GEN-LAST:event__descriptionKeyPressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void _printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__printActionPerformed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _print.setEnabled(false);
                    _print.setText("LOADING....");
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
                    _print.setText("PRINT ALL STOCK DETAILS");
                    _print.setEnabled(true);

                } catch (Exception e) {
                    _print.setText("PRINT ALL STOCK DETAILS");
                    _print.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();
    }//GEN-LAST:event__printActionPerformed

    private void _BidKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__BidKeyPressed
        if (evt.getKeyCode() == 10) {
            _seller.grabFocus();
        }
    }//GEN-LAST:event__BidKeyPressed

    private void _VALActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__VALActionPerformed
        _buying.grabFocus();
    }//GEN-LAST:event__VALActionPerformed

    private void _sellingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__sellingKeyPressed
        if (evt.getKeyCode() == 10) {
            _description.grabFocus();
        }
    }//GEN-LAST:event__sellingKeyPressed

    private void _qKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qKeyReleased
        if (!_q.getText().equals("")) {
            flag = true;
            loadDataToTable();

        }
    }//GEN-LAST:event__qKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        flag = true;
        loadDataToTable();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        flag = false;
        loadDataToTable();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void _qActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__qActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__qActionPerformed

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
            java.util.logging.Logger.getLogger(Product_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Product_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Product_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Product_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Product_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField _Bid;
    private javax.swing.JComboBox<String> _VAL;
    private javax.swing.JTextField _buying;
    private javax.swing.JTextField _date;
    private javax.swing.JTextField _description;
    private javax.swing.JButton _print;
    private javax.swing.JTextField _product_id;
    private javax.swing.JTextField _q;
    private javax.swing.JTextField _qtt;
    private javax.swing.JComboBox<String> _seller;
    private javax.swing.JTextField _selling;
    private javax.swing.JTextField _time;
    private javax.swing.JTextField _title;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
