/*
 * AnnotationApplet.java
 *
 * Created on 12 มิถุนายน 2551, 14:48 น.
 */
package annotation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Label;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import annotation.util.JavaVersionCheck;
import annotation.util.Language;
import java.io.OutputStreamWriter;

/**
 *
 * @author  Somprasong
 */
public class AnnotationApplet extends javax.swing.JApplet {
     private final static File temp_file = new File(System.getProperty("user.home") + "\\ann_temp.ann");
    private AnnotationPaintPanel annotationPaintPanel;
    private ArrayList ary_images = new ArrayList();
    private ArrayList ary_stamps = new ArrayList();
    private boolean is_editMode = false;
    private boolean is_addAnn = false;
    public static Properties properties;
    private AnnotationPaintPanel panel;
    private String str_usrID = "";
    private String str_hostURL = "";
    private String str_annotationURL = "";
    private String str_userName ="";

    /** Initializes the applet AnnotationApplet */
    @Override
    public void init() {
        getParameter();
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (InstantiationException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (UnsupportedLookAndFeelException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    if (!JavaVersionCheck.checkMin(1.4)) {

                        JOptionPane.showMessageDialog(null, "You do not seem to have a Java version 1.4 or later\nDownload an updated version from http://java.sun.com", "Java Version Check",
                                JOptionPane.ERROR_MESSAGE);
                        System.exit(-1);
                    }
                    loadProperties();
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // get parameter
    private void getParameter() {
        try {
            String isEditor = getParameter("isEditor");
            this.is_editMode = Boolean.valueOf(isEditor);

            String isAddAnn = getParameter("isAddAnn");
            this.is_addAnn = Boolean.valueOf(isAddAnn);

            String str_tmpUsr = getParameter("user");
            String[] str_tmp = str_tmpUsr.split(",");
            str_usrID = str_tmp[0];
            str_userName = str_tmp[1];

            String hosturl = getParameter("url");
            this.str_hostURL = hosturl;

            String stamp[] = getParameters("stamp");
            for (int i = 0; i < stamp.length; i++) {
                ary_stamps.add(stamp[i]);
            }

            String files[] = getParameters("files");
            for (int i = 0; i < files.length; i++) {
                ary_images.add(files[i]);
            }

            String str_Ann = getParameter("fileAnn");
            // เซ็ทค่า url ของ annotaion
            if (str_Ann == null || str_Ann.equals("")) {
                str_annotationURL = temp_file.toURL().toString();
            }else{
                str_annotationURL = str_Ann;
            }
            
            String str_language = getParameter("lang");            
            Language.setStr_lang(str_language);

        } catch (Exception e) {
        }
    }
    // get parameters
    public String[] getParameters(String paramName) {
        Vector vector = new Vector();

        for (int i = 0; true; i++) {
            String paramValue = getParameter(paramName + i);
            if (paramValue == null) {
                break;
            }
            vector.addElement(paramValue);
        }

        String[] returnValues = new String[vector.size()];

        vector.copyInto((Object[]) returnValues);

        return returnValues;
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 600));
        jPanel1.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        try {
            // เซ็ทค่า url ของรูป
            String[] str_imageURLs;
            if (ary_images.size() > 0) {
                str_imageURLs = new String[ary_images.size()];
                for (int i = 0; i < ary_images.size(); i++) {
                    str_imageURLs[i] = ary_images.get(i).toString();
                }
            } else {
                str_imageURLs = new String[]{null};
            }
            // เซ็ทค่า url ของรูป stamp
            String[] str_stampURLs;
            if (ary_stamps.size() > 0) {
                str_stampURLs = new String[ary_stamps.size()];
                for (int i = 0; i < ary_stamps.size(); i++) {
                    str_stampURLs[i] = ary_stamps.get(i).toString();
                }
            } else {
                str_stampURLs = new String[]{null};
            }

            // set urls of images and annotation
            URL url_Annotation = new URL(str_annotationURL);
            URL[] url_Images = convert2URL(str_imageURLs);
            URL[] url_Stamps = convert2URL(str_stampURLs);

            // load annotation from url to save in temp annotationFileToolbar on client
            this.readAnnotation(url_Annotation);

            this.setLayout(new BorderLayout());

            // show Image annotationPaintPanel
            annotationPaintPanel = new AnnotationPaintPanel(temp_file.getPath(), str_usrID, str_hostURL, url_Images[0], url_Stamps);
            JScrollPane scrollPanel = new JScrollPane(annotationPaintPanel);
            annotationPaintPanel.setScroll(scrollPanel);
            // navigator bar
            AnnotationNavigatorBar annotationNavigatorBar = new AnnotationNavigatorBar(url_Images, annotationPaintPanel);
            annotationNavigatorBar.setFloatable(false);
            // thumbnail bar
            AnnotationThumbnailPanel annotationThumbnailPanel = new AnnotationThumbnailPanel(url_Images, annotationPaintPanel, annotationNavigatorBar);
            // tools bar
            AnnotationToolsToolbar annotationToolsToolbar = new AnnotationToolsToolbar(annotationPaintPanel, str_hostURL, is_addAnn);
            annotationToolsToolbar.setFloatable(false);
            //annotationToolsToolbar.setOrientation(JToolBar.VERTICAL);
            annotationPaintPanel.addPropertyChangeListener(annotationToolsToolbar);
            annotationPaintPanel.setCurrentMode(AnnotationPaintPanel.MODE_NONE);
            //
            AnnotationFileToolbar annotationFileToolbar = new AnnotationFileToolbar(annotationPaintPanel);
            annotationFileToolbar.setFloatable(false);
            //annotationFileToolbar.addPropertyChangeListener(annotationToolsToolbar);

            AnnotationEditToolbar annotationEditToolbar = new AnnotationEditToolbar(annotationPaintPanel);
            annotationEditToolbar.addPropertyChangeListener(annotationToolsToolbar);
            annotationEditToolbar.setFloatable(false);

            //user_panel = new JImagePaintSelectUser(annotationPaintPanel);
            AnnotationLocationPanel annotationLocationPanel = new AnnotationLocationPanel("");
            annotationPaintPanel.setLocationPanel(annotationLocationPanel);

            // old
            //            JPanel panel_inNorth = new JPanel();
            //            panel_inNorth.setLayout(new BorderLayout(0, 2));
            //            panel_inNorth.add(new JLabel("ผู้ใช้ : " + str_userName + "     "), BorderLayout.EAST);
            //            panel_inNorth.add(annotationFileToolbar, BorderLayout.WEST);

            JPanel panel_north = new JPanel();
            panel_north.setLayout(new BorderLayout(0, 2));
            panel_north.add(annotationFileToolbar, BorderLayout.WEST);        // new
            //            panel_north.add(panel_inNorth, BorderLayout.NORTH);               // old
            if (is_editMode == true) {
                //                panel_north.add(annotationEditToolbar, BorderLayout.SOUTH);   // old
                panel_north.add(annotationEditToolbar, BorderLayout.CENTER);      // new
            }

            //            panel_inNorth.add(annotationNavigatorBar, BorderLayout.CENTER);       // old

            JPanel panel_south = new JPanel();
            panel_south.setLayout(new BorderLayout());

            if (is_editMode == true) {
                // old
                //                JPanel panel_inSouth = new JPanel();
                //                panel_inSouth.setLayout(new FlowLayout(FlowLayout.LEFT));
                //                panel_inSouth.add(annotationToolsToolbar);
                //                panel_inSouth.add(annotationLocationPanel);
                //                panel_south.add(panel_inSouth, BorderLayout.WEST);
                panel_south.add(annotationToolsToolbar, BorderLayout.WEST);
            }
            panel_south.add(annotationNavigatorBar, BorderLayout.CENTER);
            Label label_aoa = new Label("(c) Copyright 2008 Alpha Office Automation Co., Ltd");
            panel_south.add(label_aoa, BorderLayout.EAST);

            this.add(panel_north, BorderLayout.NORTH);
            this.add(scrollPanel, BorderLayout.CENTER);
            this.add(panel_south, BorderLayout.SOUTH);
            this.add(annotationThumbnailPanel, BorderLayout.EAST);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Annotation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
    public static void loadProperties() {

        properties = new Properties();
        properties.setProperty("width", "600");
        properties.setProperty("height", "420");
        properties.setProperty("bgcolor", "" + new Color(236, 233, 216).getRGB());
        properties.setProperty("drawcolor", "" + Color.black.getRGB());
        properties.setProperty("textcolor", "" + Color.black.getRGB());
        properties.setProperty("alpha", "1.0");
        properties.setProperty("fontname", "Serif");
        properties.setProperty("fonttype", "1");
        properties.setProperty("fontsize", "22");
        properties.setProperty("filled", "false");
        properties.setProperty("antialiased", "true");
        properties.setProperty("arcwidth", "0.0");
        properties.setProperty("archeight", "0.0");
        properties.setProperty("strokewidth", "1.0");
        properties.setProperty("eraserad", "3");

    }

    public URL[] convert2URL(String[] args) {
        if (args.length > 0) {
            URL url[] = new URL[args.length];
            for (int i = 0; i < args.length; i++) {
                try {
                    url[i] = new URL(args[i]);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Annotation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return url;
        }
        return null;
    }
 // load annotaion of all images from url
    private void readAnnotation(URL url) {
        ArrayList ary_annList = new ArrayList();
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            // System.out.println("Connection opened...");
            BufferedReader data = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            // System.out.println("Reading data...");
            StringBuffer buf = new StringBuffer();
            String line = "";
            while ((line = data.readLine()) != null) {
                line = ASCII2Unicode(line);
                ary_annList.add(line);
                buf.append(line + "\n");
            }
            // System.out.println(buf.toString());
            data.close();
        } catch (IOException e) {
            System.out.println("IO Error:" + e.getMessage());
            System.out.println("No annotation file or cann't access this file!!!!!");
        }
        this.saveAnnotion2TmpFile(ary_annList);
    }
    // convert string from ascii to unicode
    private String ASCII2Unicode(String ascii) {
        StringBuffer unicode = new StringBuffer(ascii);
        int code;
        for (int i = 0; i < ascii.length(); i++) {
            code = (int) ascii.charAt(i);
            if ((0xA1 <= code) && (code <= 0xFB)) { // ตรวจสอบว่าอยู่ในช่วงภาษาไทยของ ASCII หรือไม่
                unicode.setCharAt(i, (char) (code + 0xD60)); // หากใช้แปลงเป็นภาษาไทยในช่วงของ Unicode
            }
        }
        return unicode.toString(); // แปลงข้อมูลกลับไปเป็นแบบ String เพื่อใช้งานต่อไป
    }
//    private void setAnnotionTemp(ArrayList temp) {
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(temp_file));
//            for (int i = 0; i < temp.size(); i++) {
//                bw.write(temp.get(i).toString());
//                bw.write("\n");
//            }
//            bw.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
     // save all annotaions to temp annotationFileToolbar
    private void saveAnnotion2TmpFile(ArrayList ary_annList) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_file), "UTF-8"));
            for (int i = 0; i < ary_annList.size(); i++) {
                bw.write(ary_annList.get(i).toString());
                bw.write("\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void destroy() {
        //panel.delTempFile();
        try {
            temp_file.delete();
            System.out.println("del secc");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("bye");
    }
}
