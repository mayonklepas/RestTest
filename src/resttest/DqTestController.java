/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resttest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        this.loadTable();
    }

    private void senDq() {
        view.tdBsendDq.addActionListener((e) -> {

            int reqSize = Integer.parseInt(view.tdEdReqSize.getText());
            String dq = view.tdEdDqName.getText();
            String fl = view.tdtxfl.getText();
            String param = "{\"dq\":\"" + dq + "\",\"fl\":\"" + fl + "\"}";

            for (int i = 0; i < reqSize; i++) {
                ExecutorService execServ = Executors.newSingleThreadExecutor();
                execServ.execute(() -> {
                    try {
                        getRestData(param);
                    } catch (IOException ex) {
                        Logger.getLogger(DqTestController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        });

    }

    private String getRestData(String valRequest) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(this.view.tdEdUrl.getText().trim());
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
        Object[] header = new String[]{"No", "Request Name", "Request Time", "Response Time", "Process Time", "Result"};
        this.dtm.setColumnIdentifiers(header);
        this.view.trTableData.setModel((TableModel) this.dtm);
        this.view.trTableData.getSelectionModel().setSelectionMode(0);
        this.view.trTableData.getColumnModel().getColumn(0).setMaxWidth(40);
        this.view.trTableData.getColumnModel().getColumn(1).setMinWidth(150);
        this.view.trTableData.getColumnModel().getColumn(1).setMaxWidth(150);
        this.view.trTableData.getColumnModel().getColumn(2).setMinWidth(130);
        this.view.trTableData.getColumnModel().getColumn(2).setMaxWidth(130);
        this.view.trTableData.getColumnModel().getColumn(3).setMinWidth(130);
        this.view.trTableData.getColumnModel().getColumn(3).setMaxWidth(130);
        this.view.trTableData.getColumnModel().getColumn(4).setMinWidth(100);
        this.view.trTableData.getColumnModel().getColumn(4).setMaxWidth(100);
    }

    private void addDataTable(String no, String requestName, String reqTime, String resTime, String gapTime, String result) {
        this.dtm.addRow(new Object[]{no, requestName, reqTime, resTime, gapTime, result});
    }

}
