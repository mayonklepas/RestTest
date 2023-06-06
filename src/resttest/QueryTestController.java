/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package resttest;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author mulyadi
 */
public class QueryTestController {

    MainPanelView view;
    DefaultTableModel dtmSide;
    DateTimeFormatter df = DateTimeFormatter.ofPattern((String) "dd/MM/yyyy HH:mm:ss");
    NumberFormat nf = NumberFormat.getInstance();

    public QueryTestController(MainPanelView view) {
        this.view = view;
        view.tqTableSide.setDefaultEditor(Object.class, null);
        view.tqtableData.setDefaultEditor(Object.class, null);
        dtmSide = new DefaultTableModel();
        view.tqEdUrl.setText("http://localhost:8080/SMARTGATEWAY/rest/dynamic-query");

        loadSideTable();
        executeQuery();
        showTable();
        selectTable();
        tableFilter();
    }

    private void loadSideTable() {
        dtmSide.setColumnIdentifiers(new String[]{"Table Name"});
        view.tqTableSide.setModel(dtmSide);
    }

    private void loadAndFillTable(List<Map<String, Object>> data) {
        DefaultTableModel dtm = new DefaultTableModel();
        dtm.setColumnIdentifiers(new String[]{});
        view.tqtableData.setModel(dtm);
        view.tqtableData.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        List<String> lsHeader = new ArrayList<>();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                List<Object> lsValue = new ArrayList<>();
                for (Map.Entry<String, Object> entry : data.get(i).entrySet()) {
                    if (i == 0) {
                        lsHeader.add(entry.getKey());
                    }
                    lsValue.add(entry.getValue());
                }
                if (i == 0) {
                    dtm.setColumnIdentifiers(lsHeader.toArray());
                }
                dtm.addRow(lsValue.toArray());
            }
        }

    }

    private void addDatatableSide(List<Map<String, Object>> lsName) {
        for (int i = 0; i < lsName.size(); i++) {
            dtmSide.addRow(new Object[]{lsName.get(i).get("name")});
        }

    }

    private void showTableRaw(String filter) {
        DefaultTableModel dm = (DefaultTableModel) view.tqTableSide.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
        if (filter.equals("")) {
            filter = "";
        } else {
            filter = "AND name like '" + filter + "'";
        }
        String query = "SELECT name FROM sysobjects WHERE xtype='U' " + filter + " ORDER BY name ASC";
        String param = "{\"query\":\"" + query + "\"}";

        ExecutorService execServ = Executors.newSingleThreadExecutor();
        execServ.execute(() -> {
            try {
                view.tqLsending.setText("Sending");

                LocalDateTime reqTime = LocalDateTime.now();
                String result = getRestData(param);

                JSONParser parser = new JSONParser();

                JSONObject mainJo = (JSONObject) parser.parse(result);
                JSONArray ja = (JSONArray) mainJo.get("data");

                List<Map<String, Object>> lsTable = ja;

                LocalDateTime resTime = LocalDateTime.now();
                view.tqLsending.setText("Complete");
                Duration dura = Duration.between(reqTime, resTime);
                view.tqLtime.setText(String.valueOf(dura.getSeconds()));

                addDatatableSide(lsTable);
            } catch (IOException ex) {
                Logger.getLogger(DqTestController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(QueryTestController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void showTable() {
        view.tqBshowTable.addActionListener((e) -> {
            showTableRaw("");
        });

    }

    private void executeQuery() {
        view.tqBExecute.addActionListener((e) -> {
            String param = "{\"query\":\"" + view.tqEdQuery.getText() + "\"}";

            ExecutorService execServ = Executors.newSingleThreadExecutor();
            execServ.execute(() -> {
                try {
                    view.tqLsending.setText("Sending");
                    LocalDateTime reqTime = LocalDateTime.now();
                    String result = getRestData(param);
                    JSONParser parser = new JSONParser();

                    JSONObject mainJo = (JSONObject) parser.parse(result);
                    JSONArray ja = (JSONArray) mainJo.get("data");

                    LocalDateTime resTime = LocalDateTime.now();
                    view.tqLsending.setText("Complete");
                    Duration dura = Duration.between(reqTime, resTime);
                    view.tqLtime.setText(String.valueOf(dura.getSeconds()));

                    loadAndFillTable(ja);
                } catch (IOException ex) {
                    Logger.getLogger(DqTestController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(QueryTestController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        });

    }

    private void selectTable() {
        view.tqTableSide.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String tbName = String.valueOf(dtmSide.getValueAt(view.tqTableSide.getSelectedRow(), 0));
                view.tqEdQuery.setText("SELECT * FROM " + tbName);
            }
        });
    }

    private String getRestData(String valRequest) throws IOException {
        URL url = new URL(this.view.tqEdUrl.getText().trim());
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

    private void tableFilter() {
        view.tqEdTableFilter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String value = "%" + view.tqEdTableFilter.getText() + "%";
                    showTableRaw(value);
                }
            }

        });
    }

}
