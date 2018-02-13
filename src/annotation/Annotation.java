// >java -jar Annotation_webstart_edit.jar true true 123456789,cms http://192.168.0.14:9080/EDCWeb/ViewerUtilServlet annotationFileToolbar:///C:\images\1.jpg annotationFileToolbar:///C:\images\2.jpg annotationFileToolbar:///C:\anno01.ann [stamp]annotationFileToolbar:///C:\stamp_images\approved.gif [stamp]annotationFileToolbar:///C:\stamp_images\downlbtn.gif
package annotation;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import annotation.util.JavaVersionCheck;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Annotation extends JPanel implements WindowListener {

    private final static File temp_file = new File(System.getProperty("user.home") + "\\ann_temp.ann");
    public static JFrame frame;
    private AnnotationPaintPanel annotationPaintPanel;
    private ArrayList ary_images = new ArrayList();
    private ArrayList ary_stamps = new ArrayList();
    private boolean is_editMode = false;
    private boolean is_addAnn = false;
    private String str_user = "";
    private String str_hostURL = "";
    private String str_annotationURL = "";

    public Annotation(String[] args) {
        try {
            // แยก args ที่รับมา
            this.checkArguments(args);
            // แยก user id กลับ user name
            String[] str_tmp = str_user.split(",");
            String str_usrID = str_tmp[0];
            String str_userName = str_tmp[1];
            frame.setTitle("EDC Image Annotation - "+"ผู้ใช้ : " + str_userName);
            // เซ็ทค่า url ของ annotaion
            if (str_annotationURL == null || str_annotationURL.equals("")) {
                str_annotationURL = temp_file.toURL().toString();
            }
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
//            URL url_Annotation = new URL(str_annotationURL);
//            URL[] url_Images = convert2URL(str_imageURLs);
//            URL[] url_Stamps = convert2URL(str_stampURLs);
//
//            // load annotation from url to save in temp annotationFileToolbar on client
//            this.readAnnotation(url_Annotation);

            this.setLayout(new BorderLayout());
            
            // show Image annotationPaintPanel
            annotationPaintPanel = new AnnotationPaintPanel();//temp_file.getPath(), str_usrID, str_hostURL, url_Images[0], url_Stamps);
            JScrollPane scrollPanel = new JScrollPane(annotationPaintPanel);
            annotationPaintPanel.setScroll(scrollPanel);
            // navigator bar - no
            AnnotationNavigatorBar annotationNavigatorBar = new AnnotationNavigatorBar(new URL[0]//url_Images
                    , annotationPaintPanel);
            annotationNavigatorBar.setFloatable(false);
            // thumbnail bar - no
            AnnotationThumbnailPanel annotationThumbnailPanel = new AnnotationThumbnailPanel(new URL[0] //url_Images
                    , annotationPaintPanel, annotationNavigatorBar);
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
    }

    public AnnotationPaintPanel getPanel() {
        return annotationPaintPanel;
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        try {
            if(temp_file.delete()) System.out.println("delete temp file seccessful!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Can not delete temp file!");
        }
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

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
    // convert each string to URL
    private URL[] convert2URL(String[] str_url) {
        if (str_url.length > 0) {
            URL url[] = new URL[str_url.length];
            for (int i = 0; i < str_url.length; i++) {
                try {
                    url[i] = new URL(str_url[i]);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Annotation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return url;
        }
        return new URL[]{null};
    }

    /**  
     * แยก args ที่รับมาว่าแต่ละตัวเป็นอะไรบ้าง
     * @param args 
     * argument ตัวที่ 1.คือ การกำหนดว่าเป็น Viwer(==false) หรือ Editor(==true)
     * argument ตัวที่ 2.คือ การกำหนดว่าผู้ใช้ในโหมด Edittor นั้นสามารถเพิ่ม annotation ได้หรือไม่ (ได้==true และ ไม่ false)
     * argument ตัวที่ 3.คือ ผู้ใช้ จะส่งมาทั้ง id และ username จากตัวอย่างคือ 123456789,cms
     * argument ตัวที่ 4.คือ url ที่ใช้ในการบันทึก annotation และ get user ที่ใช้สำหรับการกำหนดผู้ใช้ให้กับ annotation แต่ละตัว (http://192.168.0.14:9080/EDCWeb/ViewerUtilServlet)
     * argument ที่เหลือ คือ url ของ images, stamp's images และ annotation ซึ่งจะวางลำดับยังไงก็ได้
     * - เช่น annotationFileToolbar:///C:\images\1.jpg คือ รูปภาพที่ผู้ใช้เรียกดู
     * - เช่น annotationFileToolbar:///C:\images\2.jpg คือ รูปภาพที่ผู้ใช้เรียกดู
     * - เช่น annotationFileToolbar:///C:\anno01.ann   คือ ไฟล์ annotation ของรูปภาพที่ผู้ใช้เรียกดู
     * - เช่น [stamp]annotationFileToolbar:///C:\stamp_images\approved.gif คือ รูปภาพที่ใช้ในการทำ Stamp ***
     * - เช่น [stamp]annotationFileToolbar:///C:\stamp_images\downlbtn.gif คือ รูปภาพที่ใช้ในการทำ Stamp ***
     * ***รูปภาพที่ใช้ในการทำ Stamp จะต้องพิมพ์ [stamp] ไว้หน้า url ของภาพ เพื่อใช้ในการระบุว่าภาพนี้ใช้สำหรับการทำ Stamp มิฉะนั้นภาพนั้นจะถูกมองว่าเป็นรูปภาพที่ผู้ใช้ต้องการเรียกดู และใช้วาด Annotation ลงไป
     */
    private void checkArguments(String[] args) {
        is_editMode = true;//Boolean.valueOf(args[0]);
        is_addAnn = true;//Boolean.valueOf(args[1]);
        str_user = "123456789,cms";//args[2];
        str_hostURL = "http://192.168.0.14:9080/EDCWeb/ViewerUtilServlet";//args[3];
//        for (int j = 4; j < args.length; j++) {
//            String str_temp = args[j].toString();
//            int i_index = str_temp.lastIndexOf(".");
//            String str_extension = str_temp.substring(i_index, str_temp.length());
//            if (str_extension.equals(".ann")) {
//                str_annotationURL = str_temp;
//            } else {
//                if (str_temp.substring(0, 7).equals("[stamp]")) {
//                    ary_stamps.add(str_temp.substring(7, str_temp.length()));
//                } else {
//                    ary_images.add(str_temp);
//                }
//            }
//        }
    }
    // main method
    public static void main(String[] args) {
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
        frame = new JFrame();
        // Java vertion check
        if (!JavaVersionCheck.checkMin(1.4)) {
            JOptionPane.showMessageDialog(frame, "You do not seem to have a Java version 1.4 or later\nDownload an updated version from http://java.sun.com", "Java Version Check",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        Annotation contents = null;
        if (args.length > 0) {
            contents = new Annotation(args);
        } else {
            contents = new Annotation(null);
        }
        frame.setContentPane(contents);
        frame.addWindowListener(contents);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
