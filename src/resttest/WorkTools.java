/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package resttest;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mulyadi
 */
public class WorkTools {

    public static String urlData = "";
    public static String dqHeadData = "";
    public static String utilData = "";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            File fl = new File("appconfig");
            if (!fl.exists()) {
                try {
                    fl.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger((String) SgTestController.class.getName()).log(Level.SEVERE, null, (Throwable) ex);
                }
            }
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                br = new BufferedReader((Reader) new FileReader(fl));
                String data = "";
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger((String) SgTestController.class.getName()).log(Level.SEVERE, null, (Throwable) ex);
            } catch (IOException ex) {
                Logger.getLogger((String) SgTestController.class.getName()).log(Level.SEVERE, null, (Throwable) ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger((String) SgTestController.class.getName()).log(Level.SEVERE, null, (Throwable) ex);
                }
            }

            String appConfig = sb.toString();
            urlData = appConfig.split("##")[0];
            dqHeadData = appConfig.split("##")[1];
            utilData = appConfig.split("##")[2];

            setUIFont(new javax.swing.plaf.FontUIResource("Sogoe ui", Font.PLAIN, 16));
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());

            MainPanelView v = new MainPanelView();
            v.setTitle("Web Test Toolkit");
            v.setLocationRelativeTo(null);
            v.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WorkTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(WorkTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(WorkTools.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(WorkTools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
