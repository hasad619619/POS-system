/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.Customer;
import CLASS.JDBC;
import CLASS.Seller;
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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mayura Lakshan
 */
public final class Supplier_UI extends javax.swing.JFrame {

    Seller CU = new Seller();
    int row;
    int isUpdate = 0;
    int nextID = 1;
    String ID;
    String Name;
    String Description;
    String Adress;
    String Number;
    String Date;
    String Time;
    static boolean flag = false;

    public void getNextId() {
        String Sql = "SELECT id FROM seller";
        try {
            ResultSet RS = JDBC.getInstance().getData(Sql);
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

            nextID = max + 1;

        } catch (Exception e) {

        }

    }

    public void loadData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                StockLoadingDialog sld = new StockLoadingDialog(null, false);
                sld.setVisible(true);
                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.setRowCount(0);
                String Sql2 = "select * from seller where status='1'";
                if (flag) {
                    Sql2 = "select * from seller where title like '%" + _q.getText() + "%' and status='1'";
                }

                try {
                    ResultSet RS = JDBC.getInstance().getData(Sql2);
                    while (RS.next()) {
                        Vector vec = new Vector();
                        vec.add(RS.getString("id"));
                        vec.add(RS.getString("title"));
                        vec.add(RS.getString("description"));
                        vec.add(RS.getString("Adress"));
                        vec.add(RS.getString("number"));
                        vec.add(RS.getString("added_date"));
                        vec.add(RS.getString("added_time"));
                        dtm.addRow(vec);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sld.dispose();
            }
        }).start();

    }

    public void setDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat datetime = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        _date.setText(dateFormat.format(cal.getTime()));
        _time.setText(datetime.format(cal.getTime()));

    }

    public void setValue() {
        ID = _id.getText();
        Name = _name.getText();
        Description = _description.getText();
        Adress = _adress.getText();
        Number = _number.getText();
        setDateTime();
        Date = _date.getText();
        Time = _time.getText();

    }

    public void clearValues() {
        _name.grabFocus();
        //_id.setEditable(true);
        _id.setText("");
        _name.setText("");
        _description.setText("");
        _adress.setText("");
        _number.setText("");
        _date.setText("");
        _time.setText("");

        ID = null;
        Name = null;
        Description = null;
        Adress = null;
        Number = null;
        Date = null;
        Time = null;
        isUpdate = 0;
        loadData();
        setDateTime();
        getNextId();
        _id.setText("" + nextID);

    }

    public boolean check() {
        boolean val = false;

        val = (!(_id.getText().equals("")) & !(_name.getText().equals("")) & !(_description.getText().equals("")) & !(_adress.getText().equals("")) & !(_date.equals("")) & !(_time.getText().equals("")) & !(_number.getText().equals("")));

        return val;

    }

    /**
     * Creates new form Seller_UI
     */
    public Supplier_UI() {
        initComponents();
        clearValues();
        setDateTime();
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
        _date = new javax.swing.JTextField();
        _time = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        _id = new javax.swing.JTextField();
        _name = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        _description = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        _number = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        _adress = new javax.swing.JTextArea();
        _q = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        _print = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jMenuItem1.setText("Update");
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
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

        _date.setEditable(false);
        _date.setBackground(new java.awt.Color(243, 240, 96));
        _date.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        _date.setForeground(new java.awt.Color(51, 51, 51));

        _time.setEditable(false);
        _time.setBackground(new java.awt.Color(243, 240, 96));
        _time.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        _time.setForeground(new java.awt.Color(51, 51, 51));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Supplier Details");
        setResizable(false);

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(51, 51, 51));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Title", "Description", "Adress", "Contact Number", "Added Date", "Added Time"
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jTable1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTable1MouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(41, 128, 185));
        jLabel1.setText("Supplier Details");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SAVE SUPPLIER", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(51, 51, 51))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Supplier ID      :");

        _id.setEditable(false);
        _id.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _id.setForeground(new java.awt.Color(155, 89, 182));
        _id.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _id.setMargin(new java.awt.Insets(50, 2, 2, 2));
        _id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _idKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _idKeyTyped(evt);
            }
        });

        _name.setBackground(new java.awt.Color(240, 240, 240));
        _name.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _name.setForeground(new java.awt.Color(155, 89, 182));
        _name.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _nameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _nameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _nameKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Name                 :");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Description        :");

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

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText(" Adress               :");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Contact Num.     :");

        _number.setBackground(new java.awt.Color(240, 240, 240));
        _number.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _number.setForeground(new java.awt.Color(155, 89, 182));
        _number.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        _number.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _numberKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                _numberKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _numberKeyTyped(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Add16.png"))); // NOI18N
        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Close_x16.png"))); // NOI18N
        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        _adress.setBackground(new java.awt.Color(240, 240, 240));
        _adress.setColumns(20);
        _adress.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        _adress.setForeground(new java.awt.Color(155, 89, 182));
        _adress.setRows(5);
        _adress.setBorder(null);
        _adress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _adressKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(_adress);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(_number, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(_description)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_name)
                            .addComponent(_id))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(_id, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_name, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_description, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_number, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65))
        );

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

        _print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/print.png"))); // NOI18N
        _print.setText("PRINT ALL SUPPLIER DETAILS");
        _print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _printActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel2.setText("Supplier Name :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_print, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(_q, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4)
                                .addGap(0, 78, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(_q)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_print, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        boolean ToDo = check();
        if (ToDo) {
            setValue();

            if (isUpdate == 0) {
                boolean IsSave = CU.saveData(ID, Name, Description, Adress, Number, Date, Time);
                if (IsSave) {
                    JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                    clearValues();
                } else {
                    JOptionPane.showMessageDialog(this, "Can't Save Date ! Please Try Again !");
                    clearValues();
                }
            }
            if (isUpdate == 1) {

                boolean IsOk = CU.updateData(ID, Name, Description, Adress, Number, Date, Time);

                if (IsOk) {

                    JOptionPane.showMessageDialog(this, "Update Successfuly !");
                    clearValues();
                } else {
                    JOptionPane.showMessageDialog(this, "Can't Update  ! Please Try Again !");
                    clearValues();
                }

            }

        } else {
            JOptionPane.showMessageDialog(this, "Please Fill All Fields !");
            clearValues();

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clearValues();
    }//GEN-LAST:event_jButton3ActionPerformed

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

    private void jTable1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseExited

    }//GEN-LAST:event_jTable1MouseExited

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getButton() == 1) {

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked

    }//GEN-LAST:event_jMenuItem1MouseClicked

    private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
        if (evt.getButton() == 1) {
            ID = jTable1.getValueAt(row, 0).toString();

            String[] DATA = CU.getData(ID);

            _id.setText(DATA[0]);
            _id.setEditable(false);
            _name.setText(DATA[1]);
            _description.setText(DATA[2]);
            _adress.setText(DATA[3]);
            _number.setText(DATA[4]);
            isUpdate = 1;
            
        }
    }//GEN-LAST:event_jMenuItem1MousePressed

    private void jMenuItem2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem2MousePressed
        if (evt.getButton() == 1) {
            ID = jTable1.getValueAt(row, 0).toString();
            try {
                    String SQL="UPDATE seller set status='0' WHERE id='"+ID+"'";
                    int I=JDBC.getInstance().putData(SQL);
                    if(I > 0){
                        JOptionPane.showMessageDialog(this, "Deleted !");
                        clearValues();
                    }else{
                        JOptionPane.showMessageDialog(this, "Can't Delete !");
                    }
                } catch (Exception e) {
                }
        }
    }//GEN-LAST:event_jMenuItem2MousePressed

    private void _nameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__nameKeyReleased

    }//GEN-LAST:event__nameKeyReleased

    private void _descriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__descriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__descriptionActionPerformed

    private void _descriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyReleased

    }//GEN-LAST:event__descriptionKeyReleased

    private void _numberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__numberKeyReleased

    }//GEN-LAST:event__numberKeyReleased

    private void _nameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__nameKeyTyped
        if (Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event__nameKeyTyped

    private void _descriptionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyTyped

    }//GEN-LAST:event__descriptionKeyTyped

    private void _numberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__numberKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
        if (_number.getText().length() == 10) {
            evt.consume();
        }
    }//GEN-LAST:event__numberKeyTyped

    private void _idKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__idKeyReleased
        if (evt.getKeyCode() == 10) {
            _name.grabFocus();
        }
    }//GEN-LAST:event__idKeyReleased

    private void _idKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__idKeyTyped
        if (!Character.isDigit(evt.getKeyChar())) {
            evt.consume();
        }
    }//GEN-LAST:event__idKeyTyped

    private void _numberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__numberKeyPressed
        if (evt.getKeyCode() == 10) {

            boolean ToDo = check();
            if (ToDo) {
                setValue();

                if (isUpdate == 0) {
                    boolean IsSave = CU.saveData(ID, Name, Description, Adress, Number, Date, Time);
                    if (IsSave) {
                        JOptionPane.showMessageDialog(this, "Saved Successfuly !");
                        clearValues();
                    } else {
                        JOptionPane.showMessageDialog(this, "Can't Save Date ! Please Try Again !");
                        clearValues();
                    }
                }
                if (isUpdate == 1) {
                    boolean IsOk = CU.updateData(ID, Name, Description, Adress, Number, Date, Time);
                    if (IsOk) {
                        JOptionPane.showMessageDialog(this, "Update Successfuly !");
                        clearValues();
                    } else {
                        JOptionPane.showMessageDialog(this, "Can't Update Date ! Please Try Again !");
                        clearValues();
                    }

                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All Fields !");
                clearValues();

            }

        }
    }//GEN-LAST:event__numberKeyPressed

    private void _nameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__nameKeyPressed
        if (evt.getKeyCode() == 10) {
            _description.grabFocus();
        }
    }//GEN-LAST:event__nameKeyPressed

    private void _descriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__descriptionKeyPressed
        if (evt.getKeyCode() == 10) {
            _adress.grabFocus();
        }
    }//GEN-LAST:event__descriptionKeyPressed

    private void _adressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__adressKeyPressed
        if (evt.getKeyCode() == 10) {
            _number.grabFocus();
        }
    }//GEN-LAST:event__adressKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        flag = true;
        loadData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        flag = false;
        loadData();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void _qKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__qKeyReleased
        if (!_q.getText().equals("")) {
            flag = true;
            loadData();

        }
    }//GEN-LAST:event__qKeyReleased

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
                    InputStream reportSource = getClass().getResourceAsStream("/reports/Supplier_Details.jasper");
                    Map<String, Object> para = new HashMap<String, Object>();
                    para.put("Date", dateFormat.format(cal.getTime()));
                    para.put("na", NAME);
                    para.put("cn", CONTACT);
                    para.put("ad", ADDRESS);
                    JasperPrint jp = JasperFillManager.fillReport(reportSource, para, JDBC.getInstance().con());
                    JasperViewer.viewReport(jp, false);
                    _print.setText("PRINT ALL SUPPLIER DETAILS");
                    _print.setEnabled(true);

                } catch (Exception e) {
                    _print.setText("PRINT ALL SUPPLIER DETAILS");
                    _print.setEnabled(true);
                    e.printStackTrace();
                }
            }
        }).start();

    }//GEN-LAST:event__printActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java CU 6) is not available, stay with the default look and feel.
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
            java.util.logging.Logger.getLogger(Supplier_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Supplier_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Supplier_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Supplier_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Supplier_UI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea _adress;
    private javax.swing.JTextField _date;
    private javax.swing.JTextField _description;
    private javax.swing.JTextField _id;
    private javax.swing.JTextField _name;
    private javax.swing.JTextField _number;
    private javax.swing.JButton _print;
    private javax.swing.JTextField _q;
    private javax.swing.JTextField _time;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
