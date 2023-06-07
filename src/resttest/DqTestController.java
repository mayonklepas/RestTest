/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resttest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author mulyadi
 */
public class DqTestController {

    MainPanelView view;
    DefaultTableModel dtm;
    DateTimeFormatter df = DateTimeFormatter.ofPattern((String) "dd/MM/yyyy HH:mm:ss");
    NumberFormat nf = NumberFormat.getInstance();

    public DqTestController(MainPanelView view) {
        this.view = view;
        view.tdEdReqSize.setText("1");
        dtm = new DefaultTableModel();
        loadTable();
        loadConfigCollection();
        sendDq();
        view.tdBClear.addActionListener((e) -> {
            clearData();
        });
        view.tqtableData.setRowHeight(30);
    }

    private void loadConfigCollection() {
        String urlConfig = WorkTools.urlData;

        for (String urlStr : urlConfig.split(";")) {
            this.view.tdCmbUrl.addItem(urlStr);
        }

        String dqConfig = WorkTools.dqHeadData;

        for (String dqStr : dqConfig.split(";")) {
            this.view.tdCmbDqName.addItem(dqStr);
        }

        view.tdCmbDqName.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String value = e.getItem().toString();
                view.tdEdDqName.setText(value.split("::")[0]);
                view.tdtxfl.setText(value.split("::")[1]);
            }
        });
        view.tdCmbUrl.setSelectedIndex(1);
    }

    private void sendDq() {
        view.tdBsendDq.addActionListener((e) -> {
            clearData();
            int reqSize = Integer.parseInt(view.tdEdReqSize.getText());
            String dq = view.tdEdDqName.getText();
            String fl = view.tdtxfl.getText();
            String param = "{\"dq\":\"" + dq + "\",\"fl\":\"" + fl + "\"}";

            LocalDateTime startTime = LocalDateTime.now();
            view.tdLsending.setText("Status : Sending");
            for (int i = 0; i < reqSize; i++) {
                ExecutorService execServ = Executors.newSingleThreadExecutor();
                final int fnumber = i;
                execServ.execute(() -> {
                    try {
                        LocalDateTime reqTime = LocalDateTime.now();
                        String result = getRestData(param);
                        LocalDateTime resTime = LocalDateTime.now();
                        String nofinale = String.valueOf(fnumber + 1);
                        addDataTable(nofinale, dq + " : " + fl, df.format(reqTime), df.format(resTime), result);
                    } catch (IOException ex) {
                        Logger.getLogger(DqTestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            LocalDateTime endTime = LocalDateTime.now();
            Duration dura = Duration.between(startTime, endTime);
            long duratime = dura.getSeconds();
            view.tdLtime.setText("Time : " + duratime + "s");
            view.tdLsending.setText("Status : Complete");
        });

    }

    private String getRestData(String valRequest) throws IOException {
        URL url = new URL(this.view.tdCmbUrl.getEditor().getItem().toString().trim());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        bw.write(valRequest);
        bw.flush();
        bw.close();
        conn.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String result = br.lines().collect(Collectors.joining(System.lineSeparator()));
        conn.disconnect();
        return result;
    }

    private void loadTable() {
        Object[] header = new String[]{"No", "Dq Name", "Request Time", "Response Time", "Result"};
        this.dtm.setColumnIdentifiers(header);
        this.view.tdTableData.setModel((TableModel) this.dtm);
        this.view.tdTableData.getSelectionModel().setSelectionMode(0);
        this.view.tdTableData.getColumnModel().getColumn(0).setMaxWidth(40);
        this.view.tdTableData.getColumnModel().getColumn(2).setMinWidth(150);
        this.view.tdTableData.getColumnModel().getColumn(2).setMaxWidth(150);
        this.view.tdTableData.getColumnModel().getColumn(3).setMinWidth(150);
        this.view.tdTableData.getColumnModel().getColumn(3).setMaxWidth(150);
        this.view.tdTableData.getColumnModel().getColumn(4).setMinWidth(200);
        this.view.tdTableData.getColumnModel().getColumn(4).setMaxWidth(200);
    }

    private void addDataTable(String no, String dqName, String reqTime, String resTime, String result) {
        this.dtm.addRow(new Object[]{no, dqName, reqTime, resTime, result});
    }

    private void clearData() {
        DefaultTableModel dm = (DefaultTableModel) view.tdTableData.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
    }

}
