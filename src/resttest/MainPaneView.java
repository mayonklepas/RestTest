// Source code recreated by Apache Netbeans (NB Java Decompiler) 
/*
 * Decompiled with CFR 0.152.
 */
package resttest;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import resttest.MainPaneController;

public class MainPaneView
extends JFrame {
    public JButton bClearRes;
    public JButton bSendReq;
    public JComboBox<String> cmbReqMode;
    public JComboBox<String> cmbReqOption;
    public JComboBox<String> cmbReqType;
    public JComboBox<String> cmbUrl;
    public JTextField edDate;
    public JTextField edDelay;
    public JTextField edEteId;
    public JTextField edReqSize;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JScrollPane jScrollPane1;
    public JLabel lProcessTime;
    public JLabel lReceiving;
    public JLabel lSending;
    public JProgressBar pProgress;
    public JTable tblData;

    public MainPaneView() {
        this.initComponents();
        new MainPaneController(this);
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.jLabel2 = new JLabel();
        this.edDate = new JTextField();
        this.jLabel1 = new JLabel();
        this.edEteId = new JTextField();
        this.jLabel3 = new JLabel();
        this.edReqSize = new JTextField();
        this.jLabel4 = new JLabel();
        this.cmbReqMode = new JComboBox();
        this.jLabel5 = new JLabel();
        this.jLabel9 = new JLabel();
        this.cmbReqType = new JComboBox();
        this.jLabel10 = new JLabel();
        this.edDelay = new JTextField();
        this.jLabel6 = new JLabel();
        this.cmbReqOption = new JComboBox();
        this.cmbUrl = new JComboBox();
        this.jScrollPane1 = new JScrollPane();
        this.tblData = new JTable();
        this.jPanel2 = new JPanel();
        this.bSendReq = new JButton();
        this.bClearRes = new JButton();
        this.pProgress = new JProgressBar();
        this.lReceiving = new JLabel();
        this.lSending = new JLabel();
        this.lProcessTime = new JLabel();
        this.setDefaultCloseOperation(3);
        this.setTitle("SG TESTER");
        this.jPanel1.setBorder((Border)BorderFactory.createTitledBorder((Border)BorderFactory.createTitledBorder((String)""), (String)"Properties"));
        this.jLabel2.setText("Date");
        this.jLabel1.setText("ETE ID Prefix");
        this.jLabel3.setText("Request Size");
        this.jLabel4.setText("Request Mode");
        this.cmbReqMode.setModel((ComboBoxModel)new DefaultComboBoxModel((Object[])new String[]{"One by one", "All in one"}));
        this.jLabel5.setText("URL");
        this.jLabel9.setText("Request Type");
        this.cmbReqType.setModel((ComboBoxModel)new DefaultComboBoxModel((Object[])new String[]{"Account Enquiry", "Credit Transfer"}));
        this.jLabel10.setText("Delay");
        this.jLabel6.setText("Request Option");
        this.cmbReqOption.setModel((ComboBoxModel)new DefaultComboBoxModel((Object[])new String[]{"SMARTGATEWAY", "RAW GET", "RAW POST"}));
        this.cmbUrl.setEditable(true);
        GroupLayout jPanel1Layout = new GroupLayout((Container)this.jPanel1);
        this.jPanel1.setLayout((LayoutManager)jPanel1Layout);
        jPanel1Layout.setHorizontalGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel1Layout.createSequentialGroup().addContainerGap().addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.jLabel1).addComponent((Component)this.jLabel5).addComponent((Component)this.jLabel2).addComponent((Component)this.jLabel3)).addGap(27, 27, 27).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addGroup((GroupLayout.Group)jPanel1Layout.createSequentialGroup().addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent((Component)this.edEteId, GroupLayout.Alignment.LEADING).addComponent((Component)this.edDate, GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel1Layout.createSequentialGroup().addComponent((Component)this.edReqSize, -2, 48, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.jLabel10).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.edDelay, -2, 45, Short.MAX_VALUE))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.jLabel9).addComponent((Component)this.jLabel4).addComponent((Component)this.jLabel6)).addGap(18, 18, 18).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.cmbReqMode, GroupLayout.Alignment.LEADING, 0, -1, Short.MAX_VALUE).addComponent(this.cmbReqType, GroupLayout.Alignment.LEADING, 0, -1, Short.MAX_VALUE).addComponent(this.cmbReqOption, 0, -1, Short.MAX_VALUE))).addComponent(this.cmbUrl, 0, -1, Short.MAX_VALUE)).addContainerGap(33, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel1Layout.createSequentialGroup().addGap(9, 9, 9).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.jLabel5).addComponent(this.cmbUrl, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.edDate, -2, 22, -2).addComponent((Component)this.jLabel6).addComponent(this.cmbReqOption, -2, -1, -2).addComponent((Component)this.jLabel2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.jLabel1).addComponent((Component)this.edEteId, -2, -1, -2).addComponent((Component)this.jLabel9).addComponent(this.cmbReqType, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.jLabel3).addComponent((Component)this.edReqSize, -2, -1, -2).addComponent((Component)this.jLabel10).addComponent((Component)this.edDelay, -2, -1, -2).addComponent((Component)this.jLabel4)).addComponent(this.cmbReqMode, -2, -1, -2)).addContainerGap(-1, Short.MAX_VALUE)));
        this.tblData.setModel((TableModel)new DefaultTableModel((Object[][])new Object[][]{{null, null, null, null}, {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, (Object[])new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        this.jScrollPane1.setViewportView((Component)this.tblData);
        this.jPanel2.setBorder((Border)BorderFactory.createTitledBorder((Border)BorderFactory.createTitledBorder((String)""), (String)"Action"));
        this.jPanel2.setToolTipText("");
        this.bSendReq.setText("Send Request");
        this.bClearRes.setText("Stop & Clear");
        this.lReceiving.setText("Receiving : -");
        this.lSending.setText("Sending :  -");
        this.lProcessTime.setText("Process Time : -");
        GroupLayout jPanel2Layout = new GroupLayout((Container)this.jPanel2);
        this.jPanel2.setLayout((LayoutManager)jPanel2Layout);
        jPanel2Layout.setHorizontalGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel2Layout.createSequentialGroup().addContainerGap().addGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.pProgress, -1, -1, Short.MAX_VALUE).addGroup((GroupLayout.Group)jPanel2Layout.createSequentialGroup().addComponent((Component)this.lReceiving).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent((Component)this.lProcessTime)).addGroup((GroupLayout.Group)jPanel2Layout.createSequentialGroup().addGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.lSending).addGroup((GroupLayout.Group)jPanel2Layout.createSequentialGroup().addComponent((Component)this.bSendReq).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.bClearRes))).addGap(0, 18, Short.MAX_VALUE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)jPanel2Layout.createSequentialGroup().addContainerGap().addGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent((Component)this.bClearRes).addComponent((Component)this.bSendReq, -1, -1, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent((Component)this.lSending).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup((GroupLayout.Group)jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.lReceiving).addComponent((Component)this.lProcessTime)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.pProgress, -2, 16, -2).addContainerGap()));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout((LayoutManager)layout);
        layout.setHorizontalGroup((GroupLayout.Group)layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, (GroupLayout.Group)layout.createSequentialGroup().addContainerGap().addGroup((GroupLayout.Group)layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent((Component)this.jScrollPane1).addGroup((GroupLayout.Group)layout.createSequentialGroup().addComponent((Component)this.jPanel1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.jPanel2, -1, -1, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup((GroupLayout.Group)layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup((GroupLayout.Group)layout.createSequentialGroup().addContainerGap().addGroup((GroupLayout.Group)layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent((Component)this.jPanel2, -1, -1, Short.MAX_VALUE).addComponent((Component)this.jPanel1, -1, -1, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent((Component)this.jScrollPane1, -1, 342, Short.MAX_VALUE).addContainerGap()));
        this.pack();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (!"Nimbus".equals((Object)info.getName())) continue;
                UIManager.setLookAndFeel((String)info.getClassName());
                break;
            }
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger((String)MainPaneView.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger((String)MainPaneView.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger((String)MainPaneView.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger((String)MainPaneView.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        EventQueue.invokeLater((Runnable)new Runnable(){

            public void run() {
                new MainPaneView().setVisible(true);
            }
        });
    }
}
