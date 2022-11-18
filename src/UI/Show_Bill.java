/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import CLASS.JDBC;
import CLASS.Loger;
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.swing.JRViewer;
import org.apache.log4j.Logger;

/**
 *
 * @author Mayura Lakshan
 */
public class Show_Bill extends javax.swing.JFrame {

    /**
     * Creates new form Show_Bill
     */
    JDBC jdbc = JDBC.getInstance();

    public void getdataUsingId(String ID) {
        DefaultTableModel dtmm = (DefaultTableModel) jTable1.getModel();
        dtmm.setRowCount(0);
        try {

            String SQL = "SELECT * FROM bill WHERE bill_id='" + ID + "'";
            ResultSet RS = jdbc.getData(SQL);

            if (RS.next()) {
                Vector vec = new Vector();
                vec.add(RS.getString("bill_id"));
                vec.add(RS.getString("added_date"));

                DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                dtm.addRow(vec);

            } else {
                JOptionPane.showMessageDialog(this, "Wrong Invoice ID !");
                loaddat();
                _ID.setText("");
                _ID.grabFocus();

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void loaddat() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultTableModel dtmm = (DefaultTableModel) jTable1.getModel();
                dtmm.setRowCount(0);
                try {

                    String SQL = "SELECT * FROM bill WHERE  added_date  BETWEEN '" + _st_date.getText() + "' AND '" + _end_date.getText() + "' ";
                    ResultSet RS = jdbc.getData(SQL);

                    while (RS.next()) {
                        Vector vec = new Vector();
                        vec.add(RS.getString("bill_id"));
                        vec.add(RS.getString("added_date"));
                        DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                        dtm.addRow(vec);

                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

    }

    public void Printbill(String BILLID) {

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

                InputStream jr = getClass().getResourceAsStream("/reports/Invoice_copy.jasper");
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
                JRViewer j = new JRViewer(jp);
                j.remove(0);
                jTabbedPane_Report.removeAll();
                jTabbedPane_Report.add(j);
                jTabbedPane_Report.setTitleAt(0, "Invoice# - " + BILLID);
                j.setZoomRatio((float) 1.35);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Bill Print Is Not success ! Bill Data Is Saved ! EROR");
                Loger.setLoger();
                Logger log = Logger.getLogger("Log");
                log.info("Exception view" + " : " + ex);

            }

        } else {

        }

    }

    public Show_Bill() {
        initComponents();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        _st_date.setDateFormat(dateFormat);
        _end_date.setDateFormat(dateFormat);
        loaddat();
        _ID.grabFocus();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        _ID = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        _st_date = new datechooser.beans.DateChooserCombo();
        _end_date = new datechooser.beans.DateChooserCombo();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        _s_from_date = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane_Report = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SEARCH INVOICE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14))); // NOI18N

        jTable1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 0, 153));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill ID", "Added Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
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
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Invoice Id:");

        _ID.setBackground(new java.awt.Color(240, 240, 240));
        _ID.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        _ID.setForeground(new java.awt.Color(39, 174, 96));
        _ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _IDActionPerformed(evt);
            }
        });
        _ID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                _IDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                _IDKeyTyped(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_16.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("St. Date:");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("End Date:");

        _s_from_date.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_16.png"))); // NOI18N
        _s_from_date.setText("SEARCH FROM DATE");
        _s_from_date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _s_from_dateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_end_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(_st_date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(_ID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(_s_from_date, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_ID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_st_date, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_end_date, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_s_from_date)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(41, 128, 185));
        jLabel1.setText("View Invoice Details");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane_Report, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jTabbedPane_Report)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!(_ID.getText().equals(""))) {
            getdataUsingId(_ID.getText());

        } else {
            JOptionPane.showMessageDialog(this, "Please Enter Invoice ID !");
            _ID.grabFocus();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void _IDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__IDKeyTyped
        if (evt.getKeyCode() == 10) {
            if (!(_ID.getText().equals(""))) {
                getdataUsingId(_ID.getText());

            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Invoice ID !");
                _ID.grabFocus();

            }

        }
    }//GEN-LAST:event__IDKeyTyped

    private void _IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event__IDActionPerformed

    private void _s_from_dateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__s_from_dateActionPerformed
        loaddat();
    }//GEN-LAST:event__s_from_dateActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getButton() == 1 && evt.getClickCount() == 2) {
            Printbill((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));

        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void _IDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event__IDKeyPressed
        if (evt.getKeyCode() == 10) {
            if (!(_ID.getText().equals(""))) {
                getdataUsingId(_ID.getText());

            } else {
                JOptionPane.showMessageDialog(this, "Please Enter Invoice ID !");
                _ID.grabFocus();

            }
        }
    }//GEN-LAST:event__IDKeyPressed

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
            java.util.logging.Logger.getLogger(Show_Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Show_Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Show_Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Show_Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Show_Bill().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField _ID;
    private datechooser.beans.DateChooserCombo _end_date;
    private javax.swing.JButton _s_from_date;
    private datechooser.beans.DateChooserCombo _st_date;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane_Report;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
