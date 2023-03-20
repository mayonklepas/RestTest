// Source code recreated by Apache Netbeans (NB Java Decompiler) 
/*
 * Decompiled with CFR 0.152.
 */
package resttest;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import resttest.MainPaneView;

public class MainPaneController {
    MainPaneView view;
    DefaultTableModel dtm;
    DateTimeFormatter df = DateTimeFormatter.ofPattern((String)"dd/MM/yyyy HH:mm:ss");
    NumberFormat nf = NumberFormat.getInstance();
    int counterRes = 0;
    int counterReq = 0;
    boolean isStopThread = true;
    ExecutorService exeServ = null;
    String AEString = "";
    String CTString = "";
    Connection conn = null;
    String optionMode = "sg";
    String rawPostValue = "";
    String postValueType="";

    public MainPaneController(MainPaneView view) {
        this.readFile();
        this.view = view;
        this.loadUrlCollection();
        view.edDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        view.edEteId.setText("10000000");
        view.edReqSize.setText("1");
        view.edDelay.setText("0");
        this.dtm = new DefaultTableModel(){

            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        this.loadTable();
        view.bClearRes.addActionListener(e -> this.clearData());
        this.sendRequest();
        this.selectedTable();
        this.changeRequestOption();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void loadUrlCollection() {
        String[] urlArray;
        File fl = new File("url-list");
        if (!fl.exists()) {
            try {
                fl.createNewFile();
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
        }
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader((Reader)new FileReader(fl));
            String data = "";
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (IOException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        finally {
            try {
                br.close();
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
        }
        for (String urlStr : urlArray = sb.toString().split(";")) {
            this.view.cmbUrl.addItem(urlStr);
        }
    }

    private void loadTable() {
        Object[] header = new String[]{"No", "Request Name", "Request Time", "Response Time", "Process Time", "Result"};
        this.dtm.setColumnIdentifiers(header);
        this.view.tblData.setModel((TableModel)this.dtm);
        this.view.tblData.getSelectionModel().setSelectionMode(0);
        this.view.tblData.getColumnModel().getColumn(0).setMaxWidth(40);
        this.view.tblData.getColumnModel().getColumn(1).setMinWidth(150);
        this.view.tblData.getColumnModel().getColumn(1).setMaxWidth(150);
        this.view.tblData.getColumnModel().getColumn(2).setMinWidth(130);
        this.view.tblData.getColumnModel().getColumn(2).setMaxWidth(130);
        this.view.tblData.getColumnModel().getColumn(3).setMinWidth(130);
        this.view.tblData.getColumnModel().getColumn(3).setMaxWidth(130);
        this.view.tblData.getColumnModel().getColumn(4).setMinWidth(100);
        this.view.tblData.getColumnModel().getColumn(4).setMaxWidth(100);
    }

    private void addDataTable(String no, String requestName, String reqTime, String resTime, String gapTime, String result) {
        this.dtm.addRow(new Object[]{no, requestName, reqTime, resTime, gapTime, result});
    }

    private void changeRequestOption() {
        this.view.cmbReqOption.addItemListener(new ItemListener(){

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    if (e.getItem().equals((Object)"RAW POST")) {
                        JDialog jd = new JDialog();
                        jd.setTitle("RAW POST");
                        JPanel jPane = new JPanel();
                        jPane.setLayout((LayoutManager)new BoxLayout((Container)jPane, BoxLayout.PAGE_AXIS));
                        jPane.setPreferredSize(new Dimension(350, 400));
                        jPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        JComboBox jCmb = new JComboBox<>();
                        jCmb.addItem("JSON");
                        jCmb.addItem("XML");
                        jCmb.addItem("FORM");
                        jCmb.setBorder(BorderFactory.createEmptyBorder(0,0, 10, 0));
                       
                        JTextArea jta = new JTextArea();
                        jta.setAutoscrolls(true);
                        jta.setLineWrap(true);
                        jta.setText(MainPaneController.this.rawPostValue);
                       
                        
                        JButton bSave = new JButton("Save");
                        bSave.addActionListener(ev -> {
                            MainPaneController.this.rawPostValue = jta.getText();
                            postValueType=jCmb.getSelectedItem().toString();
                            JOptionPane.showMessageDialog(jPane, "Saved");
                            
                        });
                        jPane.add(jCmb);
                        jPane.add(jta);
                        jPane.add(bSave);
                        jd.add(jPane);
                        jd.pack();
                        jd.setLocationRelativeTo((Component)MainPaneController.this.view);
                        jd.setVisible(true);
                        MainPaneController.this.optionMode = "rp";
                    } else if (e.getItem().equals((Object)"RAW GET")) {
                        MainPaneController.this.optionMode = "rg";
                    } else if (e.getItem().equals((Object)"SMARTGATEWAY")) {
                        MainPaneController.this.optionMode = "sg";
                    }
                }
            }
        });
    }

    private void clearData() {
        this.dtm.getDataVector().removeAllElements();
        this.dtm.fireTableDataChanged();
        this.view.pProgress.setIndeterminate(false);
        this.view.pProgress.setMaximum(Integer.parseInt((String)this.view.edReqSize.getText()));
        this.view.pProgress.setValue(0);
        this.view.lSending.setText("Sending : - ");
        this.view.lReceiving.setText("Receiving : - ");
        this.counterRes = 0;
        this.counterReq = 0;
        this.view.bSendReq.setEnabled(true);
        this.view.lProcessTime.setText("Process Time : - ");
        this.isStopThread = true;
        if (this.exeServ != null) {
            this.exeServ.shutdownNow();
        }
        for (Map.Entry entry : Thread.getAllStackTraces().entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            if (!key.toString().contains((CharSequence)"simple-executors")) continue;
            System.out.println(key + " | " + val);
        }
    }

    private void selectedTable() {
        this.view.tblData.addMouseListener((MouseListener)new MouseAdapter(){

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = MainPaneController.this.view.tblData.getSelectedRow();
                    String name = MainPaneController.this.view.tblData.getValueAt(row, 1).toString();
                    String result = MainPaneController.this.view.tblData.getValueAt(row, 5).toString();
                    JDialog jd = new JDialog();
                    jd.setTitle("Result Detail");
                    JTextField jtf = new JTextField();
                    jtf.setText(name);
                    JTextArea jta = new JTextArea(35, 70);
                    jta.setAutoscrolls(true);
                    jta.setLineWrap(true);
                    jta.setText(MainPaneController.this.transFormString(result));
                    JScrollPane txtAreaScroll = new JScrollPane((Component)jta, 20, 30);
                    txtAreaScroll.setViewportView((Component)jta);
                    jd.add((Component)txtAreaScroll);
                    jd.pack();
                    jd.setLocationRelativeTo((Component)MainPaneController.this.view);
                    jd.setVisible(true);
                }
            }
        });
    }

    private void sendRequest() {
        this.view.bSendReq.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                MainPaneController.this.clearData();
                MainPaneController.this.view.pProgress.setIndeterminate(true);
                MainPaneController.this.view.bSendReq.setEnabled(false);
                int reqSize = Integer.parseInt((String)MainPaneController.this.view.edReqSize.getText());
                MainPaneController.this.exeServ = Executors.newSingleThreadExecutor((ThreadFactory)new simpleThreadFactory());
                if (MainPaneController.this.view.cmbReqMode.getSelectedIndex() == 1) {
                    MainPaneController.this.exeServ = Executors.newFixedThreadPool((int)reqSize, (ThreadFactory)new simpleThreadFactory());
                }
                int rawSuffix = Integer.parseInt((String)MainPaneController.this.view.edEteId.getText());
                final String date = MainPaneController.this.view.edDate.getText();
                final String dateId = MainPaneController.this.view.edDate.getText().replaceAll("-", "");
                final int type = MainPaneController.this.view.cmbReqType.getSelectedIndex();
                final LocalDateTime allReqTime = LocalDateTime.now();
                for (int i = 0; i < reqSize; ++i) {
                    int index = i + 1;
                    final int suffix = rawSuffix + index;
                    MainPaneController.this.exeServ.execute(new Runnable(){

                        /*
                         * WARNING - Removed try catching itself - possible behaviour change.
                         */
                        public void run() {
                            ++MainPaneController.this.counterReq;
                            MainPaneController.this.view.lSending.setText("Sending : " + MainPaneController.this.counterReq + " / " + MainPaneController.this.view.edReqSize.getText());
                            LocalDateTime reqTime = LocalDateTime.now();
                            String sReqTime = MainPaneController.this.df.format((TemporalAccessor)reqTime);
                            String strSuffix = String.valueOf((int)suffix);
                            try {
                                String request = "";
                                if (MainPaneController.this.optionMode.equals((Object)"sg")) {
                                    request = MainPaneController.this.aeXmlValue(dateId, date, String.valueOf((int)suffix));
                                    if (type == 1) {
                                        request = MainPaneController.this.ctXmlValue(dateId, date, strSuffix);
                                    }
                                } else {
                                    request = MainPaneController.this.rawPostValue;
                                }
                                String response = MainPaneController.this.getRestData(request);
                                LocalDateTime resTime = LocalDateTime.now();
                                String sResTime = MainPaneController.this.df.format((TemporalAccessor)resTime);
                                Duration duration = Duration.between((Temporal)reqTime, (Temporal)resTime);
                                long gapInSecon = duration.getSeconds();
                                ++MainPaneController.this.counterRes;
                                MainPaneController.this.addDataTable(MainPaneController.this.nf.format((long)MainPaneController.this.counterRes), strSuffix, sReqTime, sResTime, MainPaneController.this.nf.format(gapInSecon) + " second", response);
                            }
                            catch (IOException ex) {
                                ++MainPaneController.this.counterRes;
                                LocalDateTime resTime = LocalDateTime.now();
                                String sResTime = MainPaneController.this.df.format((TemporalAccessor)resTime);
                                Duration duration = Duration.between((Temporal)reqTime, (Temporal)resTime);
                                long gapInSecon = duration.getSeconds();
                                MainPaneController.this.addDataTable(MainPaneController.this.nf.format((long)MainPaneController.this.counterRes), strSuffix, sReqTime, sResTime, MainPaneController.this.nf.format(gapInSecon) + " second", ex.getMessage());
                            }
                            finally {
                                if (MainPaneController.this.view.pProgress.isIndeterminate()) {
                                    MainPaneController.this.view.pProgress.setIndeterminate(false);
                                }
                                MainPaneController.this.view.pProgress.setValue(MainPaneController.this.counterRes);
                                if (MainPaneController.this.counterRes == Integer.parseInt((String)MainPaneController.this.view.edReqSize.getText())) {
                                    MainPaneController.this.view.bSendReq.setEnabled(true);
                                }
                                MainPaneController.this.view.lReceiving.setText("Receiving " + MainPaneController.this.counterRes + " / " + MainPaneController.this.view.edReqSize.getText());
                                LocalDateTime resTime = LocalDateTime.now();
                                MainPaneController.this.view.lProcessTime.setText("Process Time : " + MainPaneController.this.calculateAllReqResTime(allReqTime, resTime) + " s");
                            }
                        }
                    });
                    int delay = Integer.parseInt((String)MainPaneController.this.view.edDelay.getText());
                    if (MainPaneController.this.view.cmbReqMode.getSelectedIndex() != 1 || delay <= 0) continue;
                    try {
                        Thread.sleep((long)delay);
                        continue;
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
                    }
                }
                MainPaneController.this.exeServ.shutdown();
            }
        });
    }

    private Connection h2Conn() {
        try {
            this.conn = DriverManager.getConnection((String)"jdbc:h2:mem:sglite;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false", (String)"sa", (String)"pass@word1");
            return this.conn;
        }
        catch (SQLException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            return null;
        }
    }

    private void getH2() {
        LocalDateTime lds = LocalDateTime.now();
        try {
            PreparedStatement preStat = this.h2Conn().prepareStatement("SELECT * FROM downstreammessage");
            ResultSet resSet = preStat.executeQuery();
            int counter = 1;
            while (resSet.next()) {
                System.out.println(counter);
                System.out.println(resSet.getString("fldbatchseqno"));
                System.out.println(resSet.getString("messagetype"));
                System.out.println(resSet.getString("responsemessage"));
                System.out.println(resSet.getString("respbusimessage"));
                System.out.println(resSet.getString("-------------------------------------------------"));
                ++counter;
            }
        }
        catch (SQLException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        LocalDateTime lde = LocalDateTime.now();
        Duration duration = Duration.between((Temporal)lds, (Temporal)lde);
        long gap = duration.getNano();
        System.out.println("Time Process : " + gap);
    }

    private String getRestData(String valRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(this.view.cmbUrl.getEditor().getItem().toString().trim());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        if (this.optionMode.equals((Object)"sg") || this.optionMode.equals((Object)"rp")) {
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            if (this.optionMode.equals((Object)"sg")) {
                conn.setRequestProperty("Content-Type", "application/xml");
            } else if (this.optionMode.equals((Object)"rp")) {
                if(postValueType.equals("JSON")){
                     conn.setRequestProperty("Content-Type", "application/json");
                }else if(postValueType.equals("XML")){
                    conn.setRequestProperty("Content-Type", "application/xml");
                }else{
                    String boundary = "===" + System.currentTimeMillis() + "===";
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary );
                }
               
            }
            BufferedWriter bw = new BufferedWriter((Writer)new OutputStreamWriter(conn.getOutputStream()));
            bw.write(valRequest);
            bw.flush();
            bw.close();
        }
        BufferedReader br = new BufferedReader((Reader)new InputStreamReader(conn.getInputStream()));
        String res = "";
        while ((res = br.readLine()) != null) {
            sb.append(res);
        }
        br.close();
        conn.disconnect();
        return sb.toString();
    }

    private String aeXmlValue(String dateId, String date, String suffix) {
        String data = this.AEString.replace((CharSequence)"{{dateId}}", (CharSequence)dateId).replace((CharSequence)"{{date}}", (CharSequence)date).replace((CharSequence)"{{suffix}}", (CharSequence)suffix);
        return data;
    }

    private String ctXmlValue(String dateId, String date, String suffix) {
        String data = this.CTString.replace((CharSequence)"{{dateId}}", (CharSequence)dateId).replace((CharSequence)"{{date}}", (CharSequence)date).replace((CharSequence)"{{suffix}}", (CharSequence)suffix);
        return data;
    }

    private String calculateAllReqResTime(LocalDateTime reqTime, LocalDateTime resTime) {
        Duration duration = Duration.between((Temporal)reqTime, (Temporal)resTime);
        long gapInSecon = duration.getSeconds();
        return this.nf.format(gapInSecon);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void readFile() {
        File flAE;
        File dirFile = new File("Request-Files");
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        if (!(flAE = new File("Request-Files/AE.XML")).exists()) {
            BufferedWriter bw = null;
            try {
                flAE.createNewFile();
                bw = new BufferedWriter((Writer)new FileWriter(flAE));
                bw.write(this.defAeXmlValue());
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
            finally {
                try {
                    bw.flush();
                    bw.close();
                }
                catch (IOException ex) {
                    Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
                }
            }
        }
        BufferedReader brAE = null;
        try {
            StringBuilder sb = new StringBuilder();
            brAE = new BufferedReader((Reader)new FileReader(flAE));
            String dataAe = "";
            while ((dataAe = brAE.readLine()) != null) {
                sb.append(dataAe);
            }
            this.AEString = sb.toString();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (IOException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        finally {
            try {
                brAE.close();
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
        }
        File flCT = new File("Request-Files/CT.XML");
        if (!flCT.exists()) {
            BufferedWriter bw = null;
            try {
                flCT.createNewFile();
                bw = new BufferedWriter((Writer)new FileWriter(flCT));
                bw.write(this.defCtXmlValue());
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
            finally {
                try {
                    bw.flush();
                    bw.close();
                }
                catch (IOException ex) {
                    Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
                }
            }
        }
        BufferedReader brCt = null;
        try {
            StringBuilder sb = new StringBuilder();
            brCt = new BufferedReader((Reader)new FileReader(flCT));
            String dataCt = "";
            while ((dataCt = brCt.readLine()) != null) {
                sb.append(dataCt);
            }
            this.CTString = sb.toString();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        catch (IOException ex) {
            Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
        }
        finally {
            try {
                brCt.close();
            }
            catch (IOException ex) {
                Logger.getLogger((String)MainPaneController.class.getName()).log(Level.SEVERE, null, (Throwable)ex);
            }
        }
    }

    private String defAeXmlValue() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ns:BusMsg xmlns:ns=\"urn:iso\" xmlns:ns1=\"urn:iso:std:iso:20022:tech:xsd:head.001.001.01\" xmlns:ns2=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08\">\n\t<ns:AppHdr>\n\t\t<ns1:Fr>\n\t\t\t<ns1:FIId>\n\t\t\t\t<ns1:FinInstnId>\n\t\t\t\t\t<ns1:Othr>\n\t\t\t\t\t\t<ns1:Id>CENAIDJA</ns1:Id>\n\t\t\t\t\t</ns1:Othr>\n\t\t\t\t</ns1:FinInstnId>\n\t\t\t</ns1:FIId>\n\t\t</ns1:Fr>\n\t\t<ns1:To>\n\t\t\t<ns1:FIId>\n\t\t\t\t<ns1:FinInstnId>\n\t\t\t\t\t<ns1:Othr>\n\t\t\t\t\t\t<ns1:Id>FASTIDJA</ns1:Id>\n\t\t\t\t\t</ns1:Othr>\n\t\t\t\t</ns1:FinInstnId>\n\t\t\t</ns1:FIId>\n\t\t</ns1:To>\n\t\t<ns1:BizMsgIdr>{{dateId}}CENAIDJA510R0164000001</ns1:BizMsgIdr>\n\t\t<ns1:MsgDefIdr>pacs.008.001.08</ns1:MsgDefIdr>\n\t\t<ns1:BizSvc>BI</ns1:BizSvc>\n\t\t<ns1:CreDt>{{date}}T15:56:59Z</ns1:CreDt>\n\t</ns:AppHdr>\n\t<ns:Document>\n\t\t<ns:FIToFICstmrCdtTrf>\n\t\t\t<ns2:GrpHdr>\n\t\t\t\t<ns2:MsgId>{{dateId}}CENAIDJA51000079103</ns2:MsgId>\n\t\t\t\t<ns2:CreDtTm>{{date}}T11:42:37.089</ns2:CreDtTm>\n\t\t\t\t<ns2:NbOfTxs>1</ns2:NbOfTxs>\n\t\t\t\t<ns2:SttlmInf>\n\t\t\t\t\t<ns2:SttlmMtd>CLRG</ns2:SttlmMtd>\n\t\t\t\t</ns2:SttlmInf>\n\t\t\t</ns2:GrpHdr>\n\t\t\t<ns2:CdtTrfTxInf>\n\t\t\t\t<ns2:PmtId>\n\t\t\t\t\t<ns2:EndToEndId>{{dateId}}CENAIDJA510R01{{suffix}}</ns2:EndToEndId>\n\t\t\t\t\t<ns2:TxId>{{dateId}}CENAIDJA51000079102</ns2:TxId>\n\t\t\t\t</ns2:PmtId>\n\t\t\t\t<ns2:PmtTpInf>\n\t\t\t\t\t<ns2:CtgyPurp>\n\t\t\t\t\t\t<ns2:Prtry>51001</ns2:Prtry>\n\t\t\t\t\t</ns2:CtgyPurp>\n\t\t\t\t</ns2:PmtTpInf>\n\t\t\t\t<ns2:IntrBkSttlmAmt Ccy=\"IDR\">26030198.14</ns2:IntrBkSttlmAmt>\n\t\t\t\t<ns2:ChrgBr>DEBT</ns2:ChrgBr>\n\t\t\t\t<ns2:Dbtr/>\n\t\t\t\t<ns2:DbtrAgt>\n\t\t\t\t\t<ns2:FinInstnId>\n\t\t\t\t\t\t<ns2:Othr>\n\t\t\t\t\t\t\t<ns2:Id>CENAIDJA</ns2:Id>\n\t\t\t\t\t\t</ns2:Othr>\n\t\t\t\t\t</ns2:FinInstnId>\n\t\t\t\t</ns2:DbtrAgt>\n\t\t\t\t<ns2:CdtrAgt>\n\t\t\t\t\t<ns2:FinInstnId>\n\t\t\t\t\t\t<ns2:Othr>\n\t\t\t\t\t\t\t<ns2:Id>PINBIDJA</ns2:Id>\n\t\t\t\t\t\t</ns2:Othr>\n\t\t\t\t\t</ns2:FinInstnId>\n\t\t\t\t</ns2:CdtrAgt>\n\t\t\t\t<ns2:Cdtr/>\n\t\t\t\t<ns2:CdtrAcct>\n\t\t\t\t\t<ns2:Id>\n\t\t\t\t\t\t<ns2:Othr>\n\t\t\t\t\t\t\t<ns2:Id>649214895</ns2:Id>\n\t\t\t\t\t\t</ns2:Othr>\n\t\t\t\t\t</ns2:Id>\n\t\t\t\t</ns2:CdtrAcct>\n\t\t\t</ns2:CdtTrfTxInf>\n\t\t</ns:FIToFICstmrCdtTrf>\n\t</ns:Document>\n</ns:BusMsg>";
        return data;
    }

    private String defCtXmlValue() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<ns:BusMsg xmlns:ns=\"urn:iso\" xmlns:ns1=\"urn:iso:std:iso:20022:tech:xsd:head.008.001.08\" xmlns:ns2=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:iso ../../../xsd/phase1/MainCIHub.xsd\">\n    <ns:AppHdr>\n        <ns1:Fr>\n            <ns1:FIId>\n                <ns1:FinInstnId>\n                    <ns1:Othr>\n                        <ns1:Id>CENAIDJA</ns1:Id>\n                    </ns1:Othr>\n                </ns1:FinInstnId>\n            </ns1:FIId>\n        </ns1:Fr>\n        <ns1:To>\n            <ns1:FIId>\n                <ns1:FinInstnId>\n                    <ns1:Othr>\n                        <ns1:Id>MHCCIDJA</ns1:Id>\n                    </ns1:Othr>\n                </ns1:FinInstnId>\n            </ns1:FIId>\n        </ns1:To>\n        <ns1:BizMsgIdr>{{dateId}}CENAIDJA110ORB10000001</ns1:BizMsgIdr>\n        <ns1:MsgDefIdr>pacs.008.001.08</ns1:MsgDefIdr>\n        <ns1:BizSvc>CLEAR</ns1:BizSvc>\n        <ns1:CreDt>{{date}}T19:00:00Z</ns1:CreDt>\n        <ns1:CpyDplct>CODU</ns1:CpyDplct>\n        <ns1:PssblDplct>true</ns1:PssblDplct>\n    </ns:AppHdr>\n    <ns:Document>\n        <ns:FIToFICstmrCdtTrf>\n            <ns2:GrpHdr>\n                <ns2:MsgId>{{dateId}}CENAIDJA11010000001</ns2:MsgId>\n                <ns2:CreDtTm>{{date}}T19:00:00.000</ns2:CreDtTm>\n                <ns2:NbOfTxs>1</ns2:NbOfTxs>\n                <ns2:SttlmInf>\n                    <ns2:SttlmMtd>CLRG</ns2:SttlmMtd>\n                </ns2:SttlmInf>\n            </ns2:GrpHdr>\n            <ns2:CdtTrfTxInf>\n                <ns2:PmtId>\n                    <ns2:EndToEndId>{{dateId}}CENAIDJA110ORB{{suffix}}</ns2:EndToEndId>\n                    <ns2:TxId>{{dateId}}CENAIDJA11010000001</ns2:TxId>\n                </ns2:PmtId>\n                <ns2:PmtTpInf>\n                    <ns2:CtgyPurp>\n                        <ns2:Prtry>11001</ns2:Prtry>\n                    </ns2:CtgyPurp>\n                    <ns2:LclInstrm>\n                        <ns2:Prtry>01</ns2:Prtry>\n                    </ns2:LclInstrm>\n                </ns2:PmtTpInf>\n                <ns2:IntrBkSttlmAmt Ccy=\"IDR\">11005.01</ns2:IntrBkSttlmAmt>\n                <ns2:ChrgBr>DEBT</ns2:ChrgBr>\n                <ns2:Dbtr>\n                    <ns2:Nm>JAMES BROWN</ns2:Nm>\n                    <ns2:Id>\n                        <ns2:PrvtId>\n                            <ns2:Othr>\n                                <ns2:Id>0102030405060708</ns2:Id>\n                            </ns2:Othr>\n                        </ns2:PrvtId>\n                    </ns2:Id>\n                </ns2:Dbtr>\n                <ns2:DbtrAgt>\n                    <ns2:FinInstnId>\n                        <ns2:Othr>\n                            <ns2:Id>CENAIDJA</ns2:Id>\n                        </ns2:Othr>\n                    </ns2:FinInstnId>\n                </ns2:DbtrAgt>\n                <ns2:DbtrAcct>\n                    <ns2:Id>\n                        <ns2:Othr>\n                            <ns2:Id>123456789</ns2:Id>\n                        </ns2:Othr>\n                    </ns2:Id>\n                    <ns2:Tp>\n                        <ns2:Prtry>SVGS</ns2:Prtry>\n                    </ns2:Tp>\n                </ns2:DbtrAcct>\n                <ns2:Cdtr>\n                    <ns2:Nm>JOHN SMITH</ns2:Nm>\n                    <ns2:Id>\n                        <ns2:PrvtId>\n                            <ns2:Othr>\n                                <ns2:Id>0102030405060708</ns2:Id>\n                            </ns2:Othr>\n                        </ns2:PrvtId>\n                    </ns2:Id>\n                </ns2:Cdtr>\n                <ns2:CdtrAgt>\n                    <ns2:FinInstnId>\n                        <ns2:Othr>\n                            <ns2:Id>MHCCIDJA</ns2:Id>\n                        </ns2:Othr>\n                    </ns2:FinInstnId>\n                </ns2:CdtrAgt>\n                <ns2:CdtrAcct>\n                    <ns2:Id>\n                        <ns2:Othr>\n                            <ns2:Id>987654321</ns2:Id>\n                        </ns2:Othr>\n                    </ns2:Id>\n                    <ns2:Tp>\n                        <ns2:Prtry>SVGS</ns2:Prtry>\n                    </ns2:Tp>\n                    <ns2:Prxy>\n                        <ns2:Tp>\n                            <ns2:Prtry>02</ns2:Prtry>\n                        </ns2:Tp>\n                        <ns2:Id>john.smith@example.com</ns2:Id>\n                    </ns2:Prxy>\n                </ns2:CdtrAcct>\n                <ns2:IntrBkSttlmDt>{{cdate}}</ns2:IntrBkSttlmDt>\n                <ns2:RmtInf>\n                    <ns2:Ustrd>Payment for housing</ns2:Ustrd>\n                </ns2:RmtInf>\n                <ns2:SplmtryData>\n                    <ns2:Envlp>\n                        <ns2:Dtl>\n                            <ns2:Cdtr>\n                                <ns2:Tp>01</ns2:Tp>\n                                <ns2:RsdntSts>01</ns2:RsdntSts>\n                                <ns2:TwnNm>0300</ns2:TwnNm>\n                            </ns2:Cdtr>\n                            <ns2:Dbtr>\n                                <ns2:Tp>01</ns2:Tp>\n                                <ns2:RsdntSts>01</ns2:RsdntSts>\n                                <ns2:TwnNm>0300</ns2:TwnNm>\n                            </ns2:Dbtr>\n                        </ns2:Dtl>\n                    </ns2:Envlp>\n                </ns2:SplmtryData>\n            </ns2:CdtTrfTxInf>\n        </ns:FIToFICstmrCdtTrf>\n    </ns:Document>\n</ns:BusMsg>";
        return data;
    }

    private String transFormString(String xmlString) {
        try {
            StreamSource xmlInput = new StreamSource((Reader)new StringReader(xmlString));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult((Writer)stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", (Object)10);
            transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", (Object)"");
            transformerFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", (Object)"");
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.transform((Source)xmlInput, (Result)xmlOutput);
            return xmlOutput.getWriter().toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return xmlString;
        }
    }

    public class simpleThreadFactory
    implements ThreadFactory {
        public Thread newThread(Runnable r) {
            Thread th = new Thread(r, "simple-executors");
            return th;
        }
    }
}
