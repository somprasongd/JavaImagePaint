package annotation;

import annotation.util.BufferedImageTool;
import annotation.util.ImageIOFileFilter;
import annotation.util.TransferableImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

public class AnnotationPaintPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    public static final int MODE_LINE = 0;
    public static final int MODE_SCRIBBLE = 1;
    public static final int MODE_RECT = 2;
    public static final int MODE_OVAL = 3;
    public static final int MODE_TEXT = 4;
    public static final int MODE_SELECT = 5;
    public static final int MODE_ERASE = 6;
    public static final int MODE_FLOOD = 7;
    public static final int MODE_POINT = 8;
    public static final int MODE_QUADARROW = 9;
    public static final int MODE_COLORPICK = 10;
    public static final int MODE_NOTE = 11;
    public static final int MODE_NONE = 100;
    private BufferedImage img;
    private int currentMode;
    private int oldMode;
    private int eraseRad;
    private int arcWidth;
    private int arcHeight;
    private float alpha;
    private double zoom;
    private double currentAngle;
    private Stroke stroke;
    private Color bgColor;
    private Color drawColor;
    private Color textColor;
    private boolean antialiased;
    private boolean filled;
    private boolean drag;
    private ArrayList paintObjects;
    private ArrayList selectedObjects;
    private ArrayList tempObjects;
    private ArrayList displayObjects;
    private JScrollPane scroll;
    private AnnotationLocationPanel locationPanel;
    private JFileChooser saveChooser;
    private Point mousePoint;
    private Point dragDown;
    private File currentFile;
    private String currentImage_filename = " ";
    private String str_userid;
    private Cursor preCursor;
    private Clipboard clipboard;
    private Properties properties;
    private int img_width;
    private int img_height;
    private int new_width;
    private int new_height;
    //private URL url_anno;
    private  File temp_file;//= new File(System.getProperty("user.home") + "\\ann_temp.ann");
    private Vector temp_anno;
    private String view_user = "alluser>";
    private String edit_user = "alluser>";
    private String del_user = "alluser>";
    private String str_hostURL;
    private String annotation_name;
    private URL[] str_stampURL;

    public AnnotationPaintPanel() {
        this.str_userid = "123";
        this.str_hostURL = "";
        this.str_stampURL = new URL[0];
        this.annotation_name = "";
        this.temp_file = new File(System.getProperty("user.dir") + "\\ann_temp.ann");
        this.init();
    }

    /**
     * Creates a new image
     */
    public AnnotationPaintPanel(String tempfile, String userid, String host_url, URL url_images, URL[] url_stamp) {
        this.str_userid = userid;
        this.str_hostURL = host_url;
        this.str_stampURL = url_stamp;
        this.annotation_name = getAnnotationName(url_images);
        this.temp_file = new File(tempfile);
        this.init();
    }

    private void init() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.zoom = 1.0;
        this.paintObjects = new ArrayList();
        this.tempObjects = new ArrayList();
        this.selectedObjects = new ArrayList();
        this.displayObjects = new ArrayList();
        this.loadProperties();
        
        String width = properties.getProperty("width");
        if (width == null) {
            width = "300";
        }

        String height = properties.getProperty("height");
        if (height == null) {
            height = "300";
        }
        if (img == null) {
            this.setImageSize(Integer.parseInt(width), Integer.parseInt(height), false);
        } else {
            properties.setProperty("width", "" + img.getWidth());
            properties.setProperty("height", "" + img.getHeight());
        }

        String bgcolor = properties.getProperty("bgcolor");
        if (bgcolor == null) {
            bgcolor = "" + new Color(236, 233, 216).getRGB();
        }
        this.setBgColor(new Color(Integer.parseInt(bgcolor)));

        String fgcolor = properties.getProperty("drawcolor");
        if (fgcolor == null) {
            fgcolor = "" + Color.black.getRGB();
        }
        this.setDrawColor(new Color(Integer.parseInt(fgcolor)));

        String textcolor = properties.getProperty("textcolor");
        if (textcolor == null) {
            textcolor = "" + Color.black.getRGB();
        }
        this.setTextColor(new Color(Integer.parseInt(textcolor)));

        String fontName = properties.getProperty("fontname");
        if (fontName == null) {
            fontName = "Serif";
        }

        String fonttype = properties.getProperty("fonttype");
        if (fonttype == null) {
            fonttype = "1";
        }

        String fontsize = properties.getProperty("fontsize");
        if (fontsize == null) {
            fontsize = "22";
        }
        this.setFont(new Font(fontName, Integer.parseInt(fonttype), Integer.parseInt(fontsize)));

        String fill = properties.getProperty("filled");
        if (fill == null) {
            fill = "fasle";
        }
        this.setFilled(Boolean.valueOf(fill).booleanValue());

        String strokewidth = properties.getProperty("strokewidth");
        if (strokewidth == null) {
            strokewidth = "1.0";
        }
        this.setStroke(new BasicStroke(Float.parseFloat(strokewidth)));

        String alphas = properties.getProperty("alpha");
        if (alphas == null) {
            alphas = "1.0";
        }
        this.setAlpha(Float.parseFloat(alphas));

        String antialiase = properties.getProperty("antialiased");
        if (antialiase == null) {
            antialiase = "1.0";
        }
        this.setAntialiased(Boolean.valueOf(antialiase).booleanValue());

        String eraserad = properties.getProperty("eraserad");
        if (eraserad == null) {
            eraserad = "3";
        }
        eraseRad = Integer.parseInt(eraserad);

        this.setPreferredSize(new Dimension((int) (img.getWidth() * zoom), (int) (img.getHeight() * zoom)));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);
    }

    /**
     * Set image size
     */
    private void setImageSize(int width, int height, boolean copyOld) {
        BufferedImage old = img;
        properties.setProperty("width", "" + width);
        properties.setProperty("height", "" + height);
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        if (old != null && copyOld) {
            Graphics g = img.getGraphics();
            g.drawImage(old, 0, 0, null);
        }
    }

    /**
     * Set url_anno name of current image
     * @param filename
     * filename is url_anno name of current image on the screen
     */
    private void setCurrentImageFilename(String filename) {
        int index = filename.lastIndexOf("/");
        this.currentImage_filename = filename.substring(index + 1, filename.length());
        System.out.println("file name: " + currentImage_filename);
    }

    /**
     * 
     * @return
     * current url_anno name of the current image that show on the screen
     */
    public String getCurrentImageFilename() {
        return currentImage_filename;
    }

    /**
     *  Add a stamp object on the screen
     * @param obj
     *  is a stamp object
     */
    public void addStampObjects(AnnotationStampObject obj) {
        //tempObjects.add(obj);
        selectedObjects.add(obj);
        paintObjects.add(obj);
        autoSaveAnnotaion();
        this.setCurrentMode(AnnotationPaintPanel.MODE_NONE);

    }

    /**
     * Add stamp image object into the screen
     * @param url_anno
     * is iamge url_anno
     */
    public void addStampImage(URL url) {
        BufferedImage image = loadStampImage(url);
        BufferedImage save = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) save.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(this.getObjectsImage(paintObjects), 0, 0, null);

        TransferableImage trans = new TransferableImage(save);

        clipboard.setContents(trans, null);
        String image_Path = url.toString();
        createStampImage(new Point2D.Double(30.0d, 30.0d), image_Path);
    }

    /**
     * Load an image url_anno and returns it as a BufferedImage.
     * @param image_url
     * @return
     */
    private BufferedImage loadStampImage(URL image_url) {
        try {
            BufferedImage image = ImageIO.read(image_url);
            if (image == null || (image.getWidth() < 0)) // probably bad url_stamp_images format
            {
                return null;
            }
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Load stamp image 
     * @param clickPoint
     * @param image_Path
     */
    public void createStampImage(Point2D clickPoint, String image_Path) {
        if (clipboard != null) {
            Transferable trans = clipboard.getContents(this);
            if (trans != null && trans.isDataFlavorSupported(DataFlavor.imageFlavor)) {

                try {
                    Image clipImage = (Image) trans.getTransferData(DataFlavor.imageFlavor);
                    paintObjects.add(new AnnotationStampImageObject(this, view_user, edit_user, del_user, str_userid + ">", clipImage, clickPoint, alpha, image_Path, 0, 0));
                    this.repaint();
                } catch (UnsupportedFlavorException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        this.setCurrentMode(AnnotationPaintPanel.MODE_NONE);
        autoSaveAnnotaion();
    }

    /**
     * Open stamp diaolg
     */
    public void OpenStampDialog() {
        new AnnotationStampDialog(this, view_user, edit_user, del_user, str_userid + ">", str_stampURL);
    }

    /**
     * Open select user diaolg
     */
    public void OpenSelectUserDialog(String host) {
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            String owner_users = paintObject.getOwnerUser();
            Vector view_users = paintObject.getView_user();
            Vector edit_users = paintObject.getEdit_user();
            Vector del_users = paintObject.getDel_user();
            if (owner_users.equals(str_userid + ">")) {
                new AnnotationSelectUserDialog(this, host, str_userid, view_users, edit_users, del_users);
            } else {
                JOptionPane.showMessageDialog(null, "คุณมีไม่สิทธิ์ในการกำหนดสิทธิ์ผู้ใช้ให้กับ Annotaion นี้", "คำเตือน!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        this.setCurrentMode(AnnotationPaintPanel.MODE_NONE);
    }

    /**
     * Add note object to the screen
     */
    public void addNoteObjects(AnnotationNoteObject note) {
        //tempObjects.add(note);
        selectedObjects.add(note);
        paintObjects.add(note);
        autoSaveAnnotaion();
        this.setCurrentMode(AnnotationPaintPanel.MODE_NONE);
    }

    /**
     * Remove an note object that selected
     */
    public void removeSelectNote(AnnotationObject noteObj) {
        paintObjects.remove(noteObj);
        autoSaveAnnotaion();
        selectedObjects.remove(noteObj);
    }

    /**
     * Get lacation panel
     * @return
     */
    public AnnotationLocationPanel getLocationPanel() {
        return locationPanel;
    }

    /**
     * Set location panel
     */
    public void setLocationPanel(AnnotationLocationPanel panel) {
        this.locationPanel = panel;
    }

    /**
     * Get background color
     * @return
     * backgraound color
     */
    public Color getBgColor() {
        return bgColor;
    }

    /**
     * Set background color
     * @param bgColor
     * background color
     */
    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
        properties.setProperty("bgcolor", "" + bgColor.getRGB());
    }

    /**
     * Get an alphas
     * @return
     * alpha
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Set an alpha
     * @param alpha
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        properties.setProperty("alpha", "" + alpha);
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                Vector edit_users = new Vector();
                edit_users = paintObject.getEdit_user();
                if (edit_users.get(0).toString().equals("alluser>")) {
                    paintObject.setAlpha(this.alpha);
                } else {
                    for (int j = 0; j < edit_users.size(); j++) {
                        if (edit_users.get(j).toString().equals(str_userid + ">")) {
                            paintObject.setAlpha(this.alpha);
                        }
                    }
                }
                autoSaveAnnotaion();
                this.updatePanel();
            }
        }
    }

    /**
     * Get stroke
     * @return
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * set stroke
     * @param stroke
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        properties.setProperty("stroke", "" + ((BasicStroke) stroke).getLineWidth());
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationRectObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationRectObject) paintObject).setStroke(stroke);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationRectObject) paintObject).setStroke(stroke);
                            }
                        }
                    }
                }
                if (paintObject instanceof AnnotationOvalObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationOvalObject) paintObject).setStroke(stroke);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationOvalObject) paintObject).setStroke(stroke);
                            }
                        }
                    }
                }
                if (paintObject instanceof AnnotationLineObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationLineObject) paintObject).setStroke(stroke);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationLineObject) paintObject).setStroke(stroke);
                            }
                        }
                    }
                }
                if (paintObject instanceof AnnotationQuadArrowObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationQuadArrowObject) paintObject).setStroke(stroke);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationQuadArrowObject) paintObject).setStroke(stroke);
                            }
                        }
                    }
                }
                autoSaveAnnotaion();
                this.updatePanel();
            }
        }
    }

    /**
     * Set font
     * @param font
     * 
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (properties != null) {
            properties.setProperty("fontname", font.getName());
            properties.setProperty("fonttype", "" + font.getStyle());
            properties.setProperty("fontsize", "" + font.getSize());
        }

        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationTextObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationTextObject) paintObject).setFont(font);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationTextObject) paintObject).setFont(font);
                            }
                        }
                    }
                    autoSaveAnnotaion();
                    this.updatePanel();
                }
            }
        }
    }

    /**
     * Get current mode
     * @return
     * current mode
     */
    public int getCurrentMode() {
        return currentMode;
    }

    /**
     * Set current mode
     * @param currentmode
     * number of current mode
     */
    public void setCurrentMode(int currentmode) {
        oldMode = currentMode;
        this.currentMode = currentmode;
        this.firePropertyChange("mode", new Integer(oldMode), new Integer(currentMode));

        selectedObjects = new ArrayList();
        if (currentMode == AnnotationPaintPanel.MODE_LINE ||
                currentMode == AnnotationPaintPanel.MODE_OVAL ||
                currentMode == AnnotationPaintPanel.MODE_RECT) {
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == AnnotationPaintPanel.MODE_TEXT || currentMode == AnnotationPaintPanel.MODE_NOTE) {
            this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        } else if (currentMode == AnnotationPaintPanel.MODE_SELECT ||
                currentMode == AnnotationPaintPanel.MODE_QUADARROW) {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        //autoSaveAnnotaion();System.out.println("current");
        this.updatePanel();
    }

    /**
     * Get scroll
     * @return
     */
    public JScrollPane getScroll() {
        return scroll;
    }

    /**
     * Set score
     * @param scroll
     */
    public void setScroll(JScrollPane scroll) {
        this.scroll = scroll;
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Check is antialiase or not
     * @return
     * true - if it was selected
     * <br>
     * false - in otherwise 
     */
    public boolean isAntialiased() {
        return antialiased;
    }

    /**
     * Set antialiase
     * @param antialiase
     * antialiase is true when this button selected and false in otherwise
     */
    public void setAntialiased(boolean antialiased) {
        this.antialiased = antialiased;
        properties.setProperty("antialiased", "" + antialiased);
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
            Vector edit_users = new Vector();
            edit_users = paintObject.getEdit_user();
            if (edit_users.get(0).toString().equals("alluser>")) {
                paintObject.setAntialiased(antialiased);
            } else {
                for (int j = 0; j < edit_users.size(); j++) {
                    if (edit_users.get(j).toString().equals(str_userid + ">")) {
                        paintObject.setAntialiased(antialiased);
                    }
                }
            }
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    /**
     * Check the button fill was selected or not
     * @return
     * true - if it was selected
     * <br>
     * false - in otherwise 
     * 
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * Set Filled
     * @param fill
     * fill is true when this button selected and false in otherwise
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
        properties.setProperty("filled", "" + filled);
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationRectObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationRectObject) paintObject).setFilled(filled);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationRectObject) paintObject).setFilled(filled);
                            }
                        }
                    }
                }
                if (paintObject instanceof AnnotationOvalObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationOvalObject) paintObject).setFilled(filled);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationOvalObject) paintObject).setFilled(filled);
                            }
                        }
                    }
                }
                if (paintObject instanceof AnnotationQuadArrowObject) {
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = paintObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        ((AnnotationQuadArrowObject) paintObject).setFilled(filled);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                ((AnnotationQuadArrowObject) paintObject).setFilled(filled);
                            }
                        }
                    }
                }
                autoSaveAnnotaion();
                this.updatePanel();
            }
        }
    }

    /**
     * Get color of the rectangle, oval, line or arrow objects
     * @return
     * 
     */
    public Color getDrawColor() {
        return drawColor;
    }

    /**
     * Set color of the rectangle, oval, line or arrow objects
     * @param drawColor
     */
    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
        properties.setProperty("drawcolor", "" + drawColor.getRGB());
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
            Vector edit_users = new Vector();
            edit_users = paintObject.getEdit_user();
            if (edit_users.get(0).toString().equals("alluser>")) {
                paintObject.setColor(drawColor);
            } else {
                for (int j = 0; j < edit_users.size(); j++) {
                    if (edit_users.get(j).toString().equals(str_userid + ">")) {
                        paintObject.setColor(drawColor);
                    }
                }
            }
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    /**
     * Get text's color
     * @return
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Set text's color
     * @param textColor
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        properties.setProperty("textcolor", "" + textColor.getRGB());
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            if (paintObject instanceof AnnotationTextObject) {
                //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                Vector edit_users = new Vector();
                edit_users = paintObject.getEdit_user();
                if (edit_users.get(0).toString().equals("alluser>")) {
                    paintObject.setColor(textColor);
                } else {
                    for (int j = 0; j < edit_users.size(); j++) {
                        if (edit_users.get(j).toString().equals(str_userid + ">")) {
                            paintObject.setColor(textColor);
                        }
                    }
                }
            }
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    public void setViewUser(Vector user) {
        //this.ispublic_view = ispublic;
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObject.updateViewUser(user);
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    public void setEditUser(Vector user) {
        //this.ispublic_view = ispublic;
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObject.updateEditUser(user);
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    public void setDeleteUser(Vector user) {
        //this.ispublic_view = ispublic;
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObject.updateDeleteUser(user);
            autoSaveAnnotaion();
            this.updatePanel();
        }
    }

    public boolean getOwnerUser() {

        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            String owner = paintObject.getOwnerUser();
            if (owner.equals(str_userid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get paint objects
     * @return
     */
    public ArrayList getPaintObjects() {
        return paintObjects;
    }

    /**
     * Get object image
     * @param list
     * @return
     */
    public BufferedImage getObjectsImage(ArrayList list) {
        BufferedImage temp = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) temp.getGraphics();
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObject.addToGraphics(g2);
        }
        return temp;
    }

    /**
     * Set number of zoom at a time  
     * @param zoom
     * number of zoom that zoomed at a time 
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        if (this.zoom < 0.25) {
            this.zoom = 0.25;
        }
        if (this.zoom > 4.75) {
            this.zoom = 4.75;
        }
    }

    /**
     * Get zoom
     * @return
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * 
     * @param center
     * center point of zoomed
     */
    public void zoomed(Point center) {
        Dimension newSize = new Dimension((int) (img.getWidth() * zoom), (int) (img.getHeight() * zoom));
        if (scroll != null) {
            Dimension currentSize = this.getPreferredSize();
            double ratio = (double) newSize.getHeight() / (double) currentSize.getHeight();

            Point currentLoc = scroll.getViewport().getViewPosition();

            Dimension viewSize = scroll.getViewport().getExtentSize();
            int xOff = center.x - currentLoc.x;
            int yOff = center.y - currentLoc.y;

            Point newCent = new Point((int) (center.x * ratio), (int) (center.y * ratio));

            int newXLoc = newCent.x - xOff;
            int newYLoc = newCent.y - yOff;
            if (newXLoc > newSize.getWidth() - viewSize.getWidth()) {
                newXLoc = (int) newSize.getWidth() - (int) viewSize.getWidth();
            }
            if (newXLoc < 0) {
                newXLoc = 0;
            }
            if (newYLoc > newSize.getHeight() - viewSize.getHeight()) {
                newYLoc = (int) newSize.getHeight() - (int) viewSize.getHeight();
            }
            if (newYLoc < 0) {
                newYLoc = 0;
            }
            Point newLoc = new Point(newXLoc, newYLoc);

            this.setPreferredSize(newSize);
            this.revalidate();
            scroll.getViewport().setViewPosition(newLoc);

            if (locationPanel != null) {
                int x = (int) (center.getX() / zoom);
                int y = (int) (center.getY() / zoom);

                if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
                    locationPanel.updateLocation(new Point(x, y));
                } else {
                    locationPanel.updateLocation(null);
                }
                locationPanel.repaint();
            }
            this.updatePanel();
        } else {
            this.setPreferredSize(newSize);
            this.revalidate();
            this.updatePanel();
        }
    }

    /**
     * rotate d degrees at a time
     * @param d
     * number of degrees that rotated at a time
     */
    public void rotate(double d) {
        currentAngle += d;
        if (currentAngle >= 360.0) {
            currentAngle = 0;
        }
        if (currentAngle == -90.0) {
            currentAngle = 270;
        }

        System.out.println("angle : " + currentAngle);

        if (currentAngle == 0.0 || currentAngle == 180) {
            new_width = this.img_width;
            new_height = this.img_height;
        }
        if (currentAngle == 90.0 || currentAngle == 270.0) {
            new_width = this.img_height;
            new_height = this.img_width;
        }

        this.setPreferredSize(new Dimension((int) (new_width * zoom), (int) (new_height * zoom)));
        this.revalidate();
        this.repaint();
        this.updatePanel();
    }

    /**
     * Update this panel
     */
    private void updatePanel() {
        if (scroll != null) {
            this.revalidate();
            scroll.repaint();
        } else {
            this.repaint();
        }
    }

    /**
     * 
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        AffineTransform origXform = g2.getTransform();
        AffineTransform newXform = (AffineTransform) (origXform.clone());

        //center of rotation is center of the image
        int xRot = (int) (img.getWidth() * zoom) / 2;
        int yRot = (int) (img.getHeight() * zoom) / 2;
        newXform.rotate(Math.toRadians(currentAngle), xRot, yRot);

        double transW = xRot - (int) (new_width * zoom) / 2;
        double transH = yRot - (int) (new_height * zoom) / 2;
        if (transW < 0) {
            transW *= -1;
        }
        if (transH < 0) {
            transH *= -1;
        }

        if (img.getWidth() - img.getHeight() > 0) {
            if (currentAngle == 90.0) {
                newXform.translate(transW * zoom, transH * zoom);
            }
            if (currentAngle == 270.0) {
                newXform.translate(-transW * zoom, -transH * zoom);
            }
        } else {
            if (currentAngle == 90.0) {
                newXform.translate(-transW * zoom, -transH * zoom);
            }
            if (currentAngle == 270.0) {
                newXform.translate(transW * zoom, transH * zoom);
            }
        }

        g2.setTransform(newXform);

        //zoomed bits
        g2.scale((double) zoom, (double) zoom);
        g2.setColor(bgColor);
        g2.fillRect(0, 0, img.getWidth(), img.getHeight());

        //unzoomded bits

        g2.drawImage(img, 0, 0, null);
        //g2.setTransform(origXform);

        if (zoom >= 5) {
            g2.scale(1.0 / (double) zoom, 1.0 / (double) zoom);
            for (int x = 0; x < img.getWidth() * zoom; x += zoom) {
                g2.setColor(Color.lightGray);
                g2.drawLine(x, 0, x, (int) (img.getHeight() * zoom));
            }
            for (int y = 0; y < img.getHeight() * zoom; y += zoom) {
                g2.setColor(Color.lightGray);
                g2.drawLine(0, y, (int) (img.getWidth() * zoom), y);
            }
            g2.scale(zoom, zoom);
        }

        //displayObjects
        for (int i = 0; i < displayObjects.size(); i++) {
            AnnotationObject paintObject = (AnnotationObject) displayObjects.get(i);
            paintObject.addToGraphics(g2);
        }

        //paint objects
        g2.drawImage(getObjectsImage(paintObjects), 0, 0, null);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //paint highlight points for those objects selected
        if (currentMode == AnnotationPaintPanel.MODE_SELECT) {
            for (int o = 0; o < selectedObjects.size(); o++) {
                g2.setColor(Color.magenta);
                AnnotationObject paintObject = (AnnotationObject) selectedObjects.get(o);
                Point2D[] points = paintObject.getHighlightPoints();
                for (int i = 0; i < points.length; i++) {
                    Point2D point = points[i];
                    g2.translate(paintObject.getTranslation()[0], paintObject.getTranslation()[1]);
                    g2.setStroke(new BasicStroke((float) (1.0 / zoom)));
                    RoundRectangle2D.Double rect = new RoundRectangle2D.Double((point.getX() - 2), (point.getY() - 2), 5, 5, 2, 2);
                    g2.draw(rect);
                    g2.translate(-paintObject.getTranslation()[0], -paintObject.getTranslation()[1]);
                }
            }
        }

        //paint temp objects &  hightliged points
        for (int t = 0; t < tempObjects.size(); t++) {
            AnnotationObject tempObject = (AnnotationObject) tempObjects.get(t);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            tempObject.addToGraphics(g2);
            if (tempObject.isSelected() && currentMode == AnnotationPaintPanel.MODE_SELECT) {
                Point2D[] points = tempObject.getHighlightPoints();
                for (int i = 0; i < points.length; i++) {
                    Point2D point = points[i];
                    g2.setColor(Color.magenta);
                    g2.translate(tempObject.getTranslation()[0], tempObject.getTranslation()[1]);
                    g2.setStroke(new BasicStroke((float) (1.0 / zoom)));
                    RoundRectangle2D.Double rect = new RoundRectangle2D.Double((point.getX() - 2), (point.getY() - 2), 5, 5, 2, 2);
                    g2.draw(rect);
                    int index = tempObject.getCurrentPointIndex();
                    if (index == i) {
                        g2.fill(rect);
                    }
                    g2.translate(-tempObject.getTranslation()[0], -tempObject.getTranslation()[1]);
                }
            }
        }

    }

    /**
     * load image to show on the screen that call by JImageThumbnailPanel
     * @param url_anno
     * url_anno object is a url_anno of an image
     */
    public void loadNewImage(URL url) {
        if (url != null) {
            this.currentFile = new File(url.getFile());
            try {
                img = ImageIO.read(url);
                if (img != null) {
                    img = BufferedImageTool.copy(img);
                    MediaTracker mt = new MediaTracker(this);
                    mt.addImage(img, 0);
                    try {
                        mt.waitForID(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.zoom = 1;
                    this.currentAngle = 0.0;
                    this.setPreferredSize(new Dimension((int) (img.getWidth() * zoom), (int) (img.getHeight() * zoom)));
                    paintObjects = new ArrayList();
                    tempObjects = new ArrayList();
                    selectedObjects = new ArrayList();
                    img_width = img.getWidth();
                    img_height = img.getHeight();
                }
            } catch (Exception e) {
            }
        }
        setCurrentImageFilename(url.getFile());
        loadAnnotationFile(temp_file);
        //loadAnnotationFile2(url_anno);
        this.setCurrentMode(AnnotationPaintPanel.MODE_NONE);
        this.updatePanel();
    }

    /**
     * Save an iamge and all annotion together
     * @return
     */
    public boolean saveImageFile() {
        if (saveChooser == null) {
            saveChooser = new JFileChooser();
            ArrayList filters = ImageIOFileFilter.getImageWriterFilters();
            ImageIOFileFilter png = null;
            for (int i = 0; i < filters.size(); i++) {
                ImageIOFileFilter filter = (ImageIOFileFilter) filters.get(i);
                if (filter.getPreferredExtString().equalsIgnoreCase("png")) {
                    png = filter;
                }
                saveChooser.addChoosableFileFilter(filter);
                saveChooser.setAcceptAllFileFilterUsed(false);
            }
            if (png != null) {
                saveChooser.setFileFilter(png);
            }
        }
        /* //set dir
        File loadFile = currentFile;
        if (loadFile != null && loadFile.isFile()) {
        loadFile = new File(loadFile.getParent());
        }
        saveChooser.setCurrentDirectory(loadFile);*/

        int ret = saveChooser.showSaveDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = saveChooser.getSelectedFile();
            if (file != null && file.getName().length() > 0) {
                ImageIOFileFilter filter = (ImageIOFileFilter) saveChooser.getFileFilter();
                ImageWriter writer = filter.getImageWriter();
                if (!file.getPath().endsWith("." + filter.getPreferredExtString())) {
                    file = new File(file.getPath() + "." + filter.getPreferredExtString());
                }
                this.currentFile = file;
                for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                    AnnotationObject paintObject = (AnnotationObject) iterator.next();
                    paintObject.setSelected(false);
                }
                selectedObjects.clear();
                BufferedImage save = new BufferedImage(img.getWidth(), img.getHeight(), filter.getBiType());
                Graphics2D g = (Graphics2D) save.getGraphics();
                if (!filter.isSupportsAlpha()) {
                    g.setColor(this.bgColor);
                    g.fillRect(0, 0, img.getWidth(), img.getHeight());
                }
                g.drawImage(img, 0, 0, null);
                g.drawImage(this.getObjectsImage(paintObjects), 0, 0, null);
                if (file.exists()) {
                    int retOK = JOptionPane.showConfirmDialog(this, "File " + file.getName() + " exists. Do you want to overwrite?");
                    if (retOK == JOptionPane.OK_OPTION) {
                        try {
                            ImageOutputStream stream = new FileImageOutputStream(file);
                            writer.setOutput(stream);
                            writer.write(save);
                            stream.close();

                            this.updatePanel();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            this.updatePanel();
                            return false;
                        }
                    } else {
                        this.updatePanel();
                        return false;
                    }
                } else {
                    try {
                        ImageOutputStream stream = new FileImageOutputStream(file);
                        writer.setOutput(stream);
                        writer.write(save);
                        stream.close();
                        this.updatePanel();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        this.updatePanel();
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Load annotaion url_anno 
     * @param url_anno
     */
    private void loadAnnotationFile(File file) {
        if (file.exists()) {
            try {
                ArrayList toCopy = new ArrayList();
                //BufferedReader buffRD = new BufferedReader(new FileReader(file));
                BufferedReader buffRD = new BufferedReader(new InputStreamReader(new FileInputStream(temp_file), "UTF-8"));
                String temp;
                String arrStr[] = new String[27];
                int i, start, end;
                while ((temp = buffRD.readLine()) != null) {

                    i = 0;
                    start = 0;
                    while (start != temp.length()) {
                        end = temp.indexOf('|', start);
                        if (end >= 0) {
                            arrStr[i] = temp.substring(start, end);
                            start = end + 1;
                            i++;
                        }
                    }
                    // ใส่สตริงชุดแรกที่ระบบรายชื่อ user ไว้ แล้วเอาไป chk กับ str_userid ที่รับมาว่าตรงกันมั้ย
                    String view_users = arrStr[0];
                    // ให้เรียกใช้อีก fn ในการตรวจแล้ว return boolean กลับมา
                    boolean can_see = chk_user(view_users);
                    if (can_see) {
                        String edit_users = arrStr[1];
                        String del_users = arrStr[2];
                        String owner_user = arrStr[3];
                        System.out.println("this user can see this annotation: " + arrStr[5]);
                        String chkfilename = arrStr[4];
                        String nameObj = arrStr[5];
                        String image_path = "";
                        double xStart = 0;
                        double yEnd = 0;
                        double xEnd = 0;
                        double yStart = 0;
                        double xCtrl = 0;
                        double yCtrl = 0;
                        double arcWidths = 0;
                        double arcHeights = 0;
                        double headMult = 0;
                        double endWidth = 0;
                        double tipWidth = 0;
                        double startWidth = 0;
                        double strokeWidth = 0;
                        double width;
                        double hieght;
                        Color color = null;
                        Color bg_Color = null;
                        Color br_Color = null;
                        Point2D p2d = null;
                        Stroke strokes = null;
                        boolean antia = false;
                        boolean fill = false;
                        boolean bg_Transparent = false;
                        boolean hasBorder = false;
                        boolean noBG = false;
                        float alphas = 0;
                        Font font;
                        String text;
                        if (chkfilename.equals(this.getCurrentImageFilename())) {
                            if (nameObj.equals("StampImage")) {
                                image_path = arrStr[6];
                                p2d = new Point2D.Double(Double.parseDouble(arrStr[7]), Double.parseDouble(arrStr[8]));
                                alphas = Float.parseFloat(arrStr[9]);
                                width = Double.parseDouble(arrStr[10]);
                                hieght = Double.parseDouble(arrStr[11]);

                                AnnotationStampImageObject copyImage = new AnnotationStampImageObject();
                                AnnotationObject copy = copyImage.loadImage(this, view_users, edit_users, del_users, owner_user, image_path, p2d, alphas, width, hieght);
                                toCopy.add(copy);
                            } else if (nameObj.equals("Text") || nameObj.equals("Note") || nameObj.equals("StampText")) {
                                font = new Font(arrStr[6], Integer.parseInt(arrStr[7]), Integer.parseInt(arrStr[8]));
                                color = new Color(Integer.parseInt(arrStr[9]));
                                p2d = new Point2D.Double(Double.parseDouble(arrStr[10]), Double.parseDouble(arrStr[11]));
                                text = arrStr[12];
                                System.out.println("text : " + text);
                                antia = Boolean.valueOf(arrStr[13]);
                                alphas = Float.parseFloat(arrStr[14]);

                                if (nameObj.equals("Text")) {
                                    AnnotationTextObject copyText = new AnnotationTextObject();
                                    AnnotationObject copy = copyText.loadText(this, view_users, edit_users, del_users, owner_user, font, color, p2d, text, antia, alphas);
                                    toCopy.add(copy);
                                } else if (nameObj.equals("Note")) {
                                    bg_Color = new Color(Integer.parseInt(arrStr[15]));
                                    br_Color = new Color(Integer.parseInt(arrStr[16]));
                                    strokes = new BasicStroke(Float.parseFloat(arrStr[17]));
                                    bg_Transparent = Boolean.valueOf(arrStr[18]);
                                    strokeWidth = Double.parseDouble(arrStr[19]);
                                    xStart = Double.parseDouble(arrStr[20]);
                                    yStart = Double.parseDouble(arrStr[21]);
                                    xEnd = Double.parseDouble(arrStr[22]);
                                    yEnd = Double.parseDouble(arrStr[23]);
                                    arcWidths = Double.parseDouble(arrStr[24]);
                                    arcHeights = Double.parseDouble(arrStr[25]);
                                    AnnotationNoteObject copyNote = new AnnotationNoteObject();
                                    AnnotationObject copy = copyNote.loadNote(this, view_users, edit_users, del_users, owner_user,
                                            xStart, yStart, xEnd, yEnd, p2d, font, color, text, bg_Color, br_Color, antia, alphas,
                                            strokes, bg_Transparent, strokeWidth, arcWidths, arcHeights);
                                    toCopy.add(copy);
                                } else {
                                    bg_Color = new Color(Integer.parseInt(arrStr[15]));
                                    br_Color = new Color(Integer.parseInt(arrStr[16]));
                                    strokes = new BasicStroke(Float.parseFloat(arrStr[17]));
                                    bg_Transparent = Boolean.valueOf(arrStr[18]);
                                    hasBorder = Boolean.valueOf(arrStr[19]);
                                    noBG = Boolean.valueOf(arrStr[20]);

                                    AnnotationStampObject sTextNote = new AnnotationStampObject();
                                    AnnotationObject copy = sTextNote.loadSText(this, view_users, edit_users, del_users, owner_user, font, color, bg_Color, br_Color, p2d, text, antia, alphas, strokes, bg_Transparent, hasBorder, noBG);
                                    toCopy.add(copy);
                                }
                            } else {
                                xStart = Double.parseDouble(arrStr[6]);
                                yStart = Double.parseDouble(arrStr[7]);
                                xEnd = Double.parseDouble(arrStr[8]);
                                yEnd = Double.parseDouble(arrStr[9]);
                                color = new Color(Integer.parseInt(arrStr[10]));
                                p2d = new Point2D.Double(Double.parseDouble(arrStr[11]), Double.parseDouble(arrStr[12]));
                                strokes = new BasicStroke(Float.parseFloat(arrStr[13]));
                                antia = Boolean.valueOf(arrStr[14]);
                                fill = Boolean.valueOf(arrStr[15]);
                                alphas = Float.parseFloat(arrStr[16]);

                                if (nameObj.equals("Oval")) {
                                    AnnotationOvalObject copyOval = new AnnotationOvalObject();
                                    AnnotationObject copy = copyOval.loadOval(this, view_users, edit_users, owner_user, del_users, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, fill, alphas);
                                    toCopy.add(copy);
                                } else if (nameObj.equals("Line")) {
                                    AnnotationLineObject copyLine = new AnnotationLineObject();
                                    AnnotationObject copy = copyLine.loadLine(this, view_users, edit_users, del_users, owner_user, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, alphas);
                                    toCopy.add(copy);
                                } else if (nameObj.equals("Rect")) {
                                    arcWidths = Double.parseDouble(arrStr[17]);
                                    arcHeights = Double.parseDouble(arrStr[18]);

                                    AnnotationRectObject copyRect = new AnnotationRectObject();
                                    AnnotationObject copy = copyRect.loadRect(this, view_users, edit_users, del_users, owner_user, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, fill, alphas, arcWidths, arcHeights);
                                    toCopy.add(copy);
                                } else if (nameObj.equals("Arrow")) {
                                    xCtrl = Double.parseDouble(arrStr[17]);
                                    yCtrl = Double.parseDouble(arrStr[18]);
                                    headMult = Double.parseDouble(arrStr[19]);
                                    endWidth = Double.parseDouble(arrStr[20]);
                                    tipWidth = Double.parseDouble(arrStr[21]);
                                    startWidth = Double.parseDouble(arrStr[22]);

                                    AnnotationQuadArrowObject copyArrow = new AnnotationQuadArrowObject();
                                    AnnotationObject copy = copyArrow.loadArrow(this, view_users, edit_users, del_users, owner_user, xStart, yStart, xEnd, yEnd, xCtrl, yCtrl, headMult, endWidth, tipWidth, startWidth, color, p2d, strokes, antia, fill, alphas);
                                    toCopy.add(copy);
                                }
                            }
                        }

                    } else {
                        System.out.println("this user can not see this annotation: " + arrStr[5]);
                    }


                }
                for (Iterator iterator = toCopy.iterator(); iterator.hasNext();) {
                    AnnotationObject object = (AnnotationObject) iterator.next();
                    paintObjects.add(object);
                    selectedObjects.add(object);
                }
                this.updatePanel();
                buffRD.close();
            } catch (IOException event) {
                System.out.println(event);
            }
        } else {
            System.out.println("no annotation file of this image!!!!!");
        }
    }

    private boolean chk_user(String users) {
        int i = 0;
        int start = 0;
        int end;
        Vector user_vector = new Vector();
        while (start != users.length()) {
            end = users.indexOf('>', start);
            if (end >= 0) {
                user_vector.add(users.substring(start, end));
                start = end + 1;
                i++;
            }
        }
        //boolean chk_user = Boolean.valueOf(user_vector.get(0).toString());
        if (user_vector.get(0).equals("alluser")) {
            return true;
        } else {
            for (int j = 0; j < user_vector.size(); j++) {
                String user = user_vector.get(j).toString();
                if (user.equalsIgnoreCase(str_userid)) {
                    return true;
                }
            }
        }


        return false;
    }
    /*
    private void loadAnnotationFile2(URL url_anno) {
    URLConnection conn = null;
    DataInputStream data = null;
    String line;
    StringBuffer buf = new StringBuffer();
    ArrayList toCopy = new ArrayList();
    String arrStr[] = new String[19];
    int i, start, end;
    try {
    conn = url_anno.openConnection();
    conn.connect();
    System.out.println("Connection opened...");
    data = new DataInputStream(new BufferedInputStream(conn.getInputStream()));
    System.out.println("Reading data...");
    while ((line = data.readLine()) != null) {
    buf.append(line + "\n");
    // add new
    i = 0;
    start = 0;
    while (start != line.length()) {
    end = line.indexOf('|', start);
    if (end >= 0) {
    arrStr[i] = line.substring(start, end);
    start = end + 1;
    i++;
    }
    }
    String chkfilename = arrStr[4];
    String nameObj = arrStr[5];
    String image_path = "";
    double xStart = 0;
    double yEnd = 0;
    double xEnd = 0;
    double yStart = 0;
    double xCtrl = 0;
    double yCtrl = 0;
    double arcWidths = 0;
    double arcHeights = 0;
    double headMult = 0;
    double endWidth = 0;
    double tipWidth = 0;
    double startWidth = 0;
    double strokeWidth = 0;
    Color color = null;
    Color bg_Color = null;
    Color br_Color = null;
    Point2D p2d = null;
    Stroke strokes = null;
    boolean antia = false;
    boolean fill = false;
    boolean bg_Transparent = false;
    boolean hasBorder = false;
    boolean noBG = false;
    float alphas = 0;
    Font font;
    String text;
    if (chkfilename.equals(this.getCurrentImageFilename())) {
    if (nameObj.equals("StampImage")) {
    image_path = arrStr[6];
    p2d = new Point2D.Double(Double.parseDouble(arrStr[7]), Double.parseDouble(arrStr[8]));
    alphas = Float.parseFloat(arrStr[9]);
    AnnotationStampImageObject copyImage = new AnnotationStampImageObject();
    AnnotationObject copy = copyImage.loadImage(this, image_path, p2d, alphas);
    toCopy.add(copy);
    } else if (nameObj.equals("Text") || nameObj.equals("Note") || nameObj.equals("StampText")) {
    font = new Font(arrStr[6], Integer.parseInt(arrStr[7]), Integer.parseInt(arrStr[8]));
    color = new Color(Integer.parseInt(arrStr[9]));
    p2d = new Point2D.Double(Double.parseDouble(arrStr[10]), Double.parseDouble(arrStr[11]));
    text = arrStr[12];
    antia = Boolean.valueOf(arrStr[13]);
    alphas = Float.parseFloat(arrStr[14]);
    if (nameObj.equals("Text")) {
    AnnotationTextObject copyText = new AnnotationTextObject();
    AnnotationObject copy = copyText.loadText(this, font, color, p2d, text, antia, alphas);
    toCopy.add(copy);
    } else if (nameObj.equals("Note")) {
    bg_Color = new Color(Integer.parseInt(arrStr[15]));
    br_Color = new Color(Integer.parseInt(arrStr[16]));
    strokes = new BasicStroke(Float.parseFloat(arrStr[17]));
    bg_Transparent = Boolean.valueOf(arrStr[18]);
    strokeWidth = Double.parseDouble(arrStr[19]);
    JImageNoteObject copyNote = new JImageNoteObject();
    AnnotationObject copy = copyNote.loadNote(this, font, color, bg_Color, br_Color, p2d, text, antia, alphas, strokes, bg_Transparent, strokeWidth);
    toCopy.add(copy);
    } else {
    bg_Color = new Color(Integer.parseInt(arrStr[15]));
    br_Color = new Color(Integer.parseInt(arrStr[16]));
    strokes = new BasicStroke(Float.parseFloat(arrStr[17]));
    bg_Transparent = Boolean.valueOf(arrStr[18]);
    hasBorder = Boolean.valueOf(arrStr[19]);
    noBG = Boolean.valueOf(arrStr[20]);
    AnnotationStampObject sTextNote = new AnnotationStampObject();
    AnnotationObject copy = sTextNote.loadSText(this, font, color, bg_Color, br_Color, p2d, text, antia, alphas, strokes, bg_Transparent, hasBorder, noBG);
    toCopy.add(copy);
    }
    } else {
    xStart = Double.parseDouble(arrStr[6]);
    yStart = Double.parseDouble(arrStr[7]);
    xEnd = Double.parseDouble(arrStr[8]);
    yEnd = Double.parseDouble(arrStr[9]);
    color = new Color(Integer.parseInt(arrStr[10]));
    p2d = new Point2D.Double(Double.parseDouble(arrStr[11]), Double.parseDouble(arrStr[12]));
    strokes = new BasicStroke(Float.parseFloat(arrStr[13]));
    antia = Boolean.valueOf(arrStr[14]);
    fill = Boolean.valueOf(arrStr[15]);
    alphas = Float.parseFloat(arrStr[16]);
    if (nameObj.equals("Oval")) {
    AnnotationOvalObject copyOval = new AnnotationOvalObject();
    AnnotationObject copy = copyOval.loadOval(this, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, fill, alphas);
    toCopy.add(copy);
    } else if (nameObj.equals("Line")) {
    AnnotationLineObject copyLine = new AnnotationLineObject();
    AnnotationObject copy = copyLine.loadLine(this, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, alphas);
    toCopy.add(copy);
    } else if (nameObj.equals("Rect")) {
    arcWidths = Double.parseDouble(arrStr[17]);
    arcHeights = Double.parseDouble(arrStr[18]);
    AnnotationRectObject copyRect = new AnnotationRectObject();
    AnnotationObject copy = copyRect.loadRect(this, xStart, yEnd, xEnd, yStart, color, p2d, strokes, antia, fill, alphas, arcWidths, arcHeights);
    toCopy.add(copy);
    } else if (nameObj.equals("Arrow")) {
    xCtrl = Double.parseDouble(arrStr[17]);
    yCtrl = Double.parseDouble(arrStr[18]);
    headMult = Double.parseDouble(arrStr[19]);
    endWidth = Double.parseDouble(arrStr[20]);
    tipWidth = Double.parseDouble(arrStr[21]);
    startWidth = Double.parseDouble(arrStr[22]);
    AnnotationQuadArrowObject copyArrow = new AnnotationQuadArrowObject();
    AnnotationObject copy = copyArrow.loadArrow(this, xStart, yStart, xEnd, yEnd, xCtrl, yCtrl, headMult, endWidth, tipWidth, startWidth, color, p2d, strokes, antia, fill, alphas);
    toCopy.add(copy);
    }
    }
    }
    }
    for (Iterator iterators = toCopy.iterators(); iterators.hasNext();) {
    AnnotationObject object = (AnnotationObject) iterators.next();
    paintObjects.add(object);
    selectedObjects.add(object);
    }
    this.updatePanel();
    System.out.println(buf.toString());
    data.close();
    } catch (IOException e) {
    System.out.println("IO Error:" + e.getMessage());
    System.out.println("No annotation url or cann't access this image!!!!!");
    }
    }
     */

    /**
     * Save all annotation on the screen into temp url
     * @param url_anno
     */
    public void autoSaveAnnotaion() {
        if (this.getCurrentImageFilename().equals(" ")) {

        } else {
            ArrayList oldAnnotation = new ArrayList();
            temp_anno = new Vector();
            try {
                //BufferedReader buffRD = new BufferedReader(new FileReader(temp_file));
                BufferedReader buffRD = new BufferedReader(new InputStreamReader(new FileInputStream(temp_file), "UTF-8"));
                String temp;

                while ((temp = buffRD.readLine()) != null) {
                    System.out.println("save");
                    //temp = ASCII2Uni(temp);
                    oldAnnotation.add(temp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //BufferedWriter bw = new BufferedWriter(new FileWriter(temp_file));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp_file), "UTF-8"));
                String temp = null;
                for (Iterator iterator = oldAnnotation.iterator(); iterator.hasNext();) {

                    temp = (String) iterator.next();
                    int index1 = temp.indexOf("|");
                    String str_user = temp.substring(0, index1);
                    boolean this_user = chk_user(str_user);
                    String str_1 = temp.substring(index1 + 1, temp.length());
                    int index2 = str_1.indexOf("|");
                    String str_edit_user = str_1.substring(0, index2);
                    String str_2 = str_1.substring(index2 + 1, str_1.length());
                    int index3 = str_2.indexOf("|");
                    String str_del_user = str_2.substring(0, index3);
                    String str_3 = str_2.substring(index3 + 1, str_2.length());
                    int index4 = str_3.indexOf("|");
                    String str_owneruser = str_3.substring(0, index4);
                    String str_4 = str_3.substring(index4 + 1, str_3.length());
                    int index5 = str_4.indexOf("|");
                    String str_imagename = str_4.substring(0, index5);

                    if (this_user == false || !str_imagename.equals(this.getCurrentImageFilename())) {
                        bw.write(temp);
                        bw.write("\n");
                        temp_anno.add(temp);
                    }
                }
                System.out.println("temp size : " + temp_anno.size());
                System.out.println("temp size : " + paintObjects.size());
                if (paintObjects.size() > 0) {
                    for (Iterator iterator = paintObjects.iterator(); iterator.hasNext();) {
                        AnnotationObject paintObject = (AnnotationObject) iterator.next();
                        String aaa = paintObject.getInfo();
                        bw.write(paintObject.getInfo());
                        bw.write("\n");
                        temp_anno.add(paintObject.getInfo());
                    }
                }

                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.updatePanel();
        }
    }

    private String getAnnotationName(URL url) {
        String temp_filename = null;
        String temp = url.toString();
        int index = temp.lastIndexOf("/");
        int end = temp.lastIndexOf("_");
        if (end > 0) {
            temp_filename = temp.substring(index + 1, end);

        } else {
            int end2 = temp.lastIndexOf(".");
            temp_filename = temp.substring(index + 1, end2);

        }
        //annotation_name = temp_filename + ".ann";
        return temp_filename + ".ann";
    }

    /**
     * Save all annotation on the screen into url_anno
     */
    public void saveAllAnnotation() {
        autoSaveAnnotaion();

        try {
            URL urlServlet = new URL(str_hostURL);
            URLConnection connection = urlServlet.openConnection();
            connection.setRequestProperty("CONTENT_TYPE", "text/plain");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            PrintWriter p = new PrintWriter(connection.getOutputStream());
            p.println("1");
            p.println(annotation_name);//name url
            System.out.println("annotation_name : " + annotation_name);
            for (int i = 0; i < temp_anno.size(); i++) {
                System.out.println("save: " + temp_anno.get(i).toString());
                p.println(temp_anno.get(i).toString());
            }
            p.close();
            InputStreamReader ir = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            String str_msg = br.readLine();
            System.out.println("return msg:  " + str_msg);
            if (str_msg.equals("0")) {
                JOptionPane.showMessageDialog(null, "เซฟเรียบร้อย", "เซฟเรียบร้อย", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "ไม่สามารถเซฟได้", "เกิดข้อผิดพลาด", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.updatePanel();
    }

    /**
     * Delete selected object
     */
    public void deleteSelected() {
        ArrayList toDelete = new ArrayList();
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์มนการลบหรือไม่
            Vector delete_user = new Vector();
            delete_user = paintObject.getDel_user();
            if (delete_user.get(0).toString().equals("alluser>")) {
                toDelete.add(paintObject);
            } else {
                for (int j = 0; j < delete_user.size(); j++) {
                    if (delete_user.get(j).toString().equals(str_userid + ">")) {
                        toDelete.add(paintObject);
                    }
                }
            }
        }
        for (Iterator iterator = toDelete.iterator(); iterator.hasNext();) {
            AnnotationObject object = (AnnotationObject) iterator.next();
            paintObjects.remove(object);
            selectedObjects.remove(object);
        }
        autoSaveAnnotaion();
        this.updatePanel();
    }

    /**
     * selecte all object on the screen
     */
    public void selectAll() {
        this.setCurrentMode(AnnotationPaintPanel.MODE_SELECT);
        for (Iterator iterator = paintObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObject.setSelected(true);
            selectedObjects.add(paintObject);
            this.firePropertyChange("selection", null, selectedObjects);
        }
        this.updatePanel();
    }

    public void textFinished(AnnotationTextObject textObject) {
        if (textObject.getText().trim().length() <= 0) {
            paintObjects.remove(textObject);
        }
        selectedObjects.remove(textObject);
        autoSaveAnnotaion();
        textObject.setSelected(false);
    }

    public void textFinished(AnnotationNoteObject noteObject) {
        if (noteObject.getText().trim().length() <= 0) {
            paintObjects.remove(noteObject);
        }
        selectedObjects.remove(noteObject);
        autoSaveAnnotaion();
        noteObject.setSelected(false);
    }

    public void textFinished(AnnotationStampObject stampTextObject) {
        if (stampTextObject.getText().trim().length() <= 0) {
            paintObjects.remove(stampTextObject);
        }
        selectedObjects.remove(stampTextObject);
        autoSaveAnnotaion();
        stampTextObject.setSelected(false);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteSelected();
        }

        for (int i = 0; i < selectedObjects.size(); i++) {

            AnnotationObject paintObject = (AnnotationObject) selectedObjects.get(i);

            paintObject.keyPressed(e);
        }
        this.updatePanel();
    }

    public void keyReleased(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

        if (currentMode == AnnotationPaintPanel.MODE_SELECT && e.getButton() == MouseEvent.BUTTON1) {
            boolean ctrl = (e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK;
            if (!ctrl) {
                selectedObjects = new ArrayList();
            }
            AnnotationObject closest = null;

            double closestDist = Double.MAX_VALUE;

            for (Iterator iterator = paintObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (!ctrl) {
                    paintObject.setSelected(false);
                }

                Point2D[] points = paintObject.getHighlightPoints();
                for (int i = 0; i < points.length; i++) {
                    Point2D point = points[i];
                    double dist = point.distance(new Point2D.Double(e.getPoint().getX() / zoom, e.getPoint().getY() / zoom));
                    if (dist < closestDist) {
                        closest = paintObject;
                        closestDist = dist;
                    }
                }
            }
            if (closest != null) {
                if (closest.isSelected()) {
                    closest.setSelected(false);
                    selectedObjects.remove(closest);

                } else {
                    closest.setSelected(true);

                    selectedObjects.add(closest);
                    this.firePropertyChange("selection", null, selectedObjects);
                }

            }


        } else if (currentMode == AnnotationPaintPanel.MODE_COLORPICK) {
            Color color = new Color(img.getRGB((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)));

            this.firePropertyChange("pickcolor", this.drawColor, color);
            this.setDrawColor(color);
            this.setCurrentMode(oldMode);

        }
        this.updatePanel();
    }

    public void mousePressed(MouseEvent e) {
        this.requestFocus();
        if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
            dragDown = e.getPoint();
            this.preCursor = this.getCursor();
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            if (currentMode == AnnotationPaintPanel.MODE_FLOOD) {
                int rgb = img.getRGB((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                this.updatePanel();
            } else if (currentMode == AnnotationPaintPanel.MODE_OVAL) {
                tempObjects.add(new AnnotationOvalObject(this, view_user, edit_user, del_user, str_userid + ">", this.drawColor, new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)), this.getStroke(), antialiased, filled, alpha));
            } else if (currentMode == AnnotationPaintPanel.MODE_RECT) {
                tempObjects.add(new AnnotationRectObject(this, view_user, edit_user, del_user, str_userid + ">", this.drawColor, new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)), this.getStroke(), antialiased, filled, arcWidth, arcHeight, alpha));
            } else if (currentMode == AnnotationPaintPanel.MODE_LINE) {
                tempObjects.add(new AnnotationLineObject(this, view_user, edit_user, del_user, str_userid + ">", this.drawColor, new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)), this.getStroke(), antialiased, alpha));
            } else if (currentMode == AnnotationPaintPanel.MODE_QUADARROW) {
                tempObjects.add(new AnnotationQuadArrowObject(this, view_user, edit_user, del_user, str_userid + ">", this.drawColor, new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)), this.getStroke(), antialiased, filled, alpha));
            } else if (currentMode == AnnotationPaintPanel.MODE_TEXT) {
                AnnotationTextObject text = new AnnotationTextObject(this, view_user, edit_user, del_user, str_userid + ">", this.getFont(), this.textColor, new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)), " ", antialiased);
                text.setPanel(this);
                tempObjects.add(text);
                this.setCurrentMode(AnnotationPaintPanel.MODE_SELECT);
                selectedObjects.add(text);
            } else if (currentMode == AnnotationPaintPanel.MODE_NOTE) {
                Point2D locationStart = new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                new AnnotationNoteDialog(this, locationStart, view_user, edit_user, del_user, str_userid + ">", antialiased);
            } else if (currentMode == AnnotationPaintPanel.MODE_SELECT) {
                double closestDist = Double.MAX_VALUE;
                if (selectedObjects.size() == 1) {
                    AnnotationObject selectedObject = (AnnotationObject) selectedObjects.get(0);
                    selectedObject.setCurrentPointIndex(-1);
                    Point2D[] points = selectedObject.getHighlightPoints();
                    for (int i = 0; i < points.length; i++) {
                        Point2D point = points[i];
                        double dist = point.distance(new Point2D.Double(e.getPoint().getX() / zoom, e.getPoint().getY() / zoom));
                        if (dist < closestDist) {
                            selectedObject.setCurrentPointIndex(i);
                            closestDist = dist;
                        }
                    }
                    if (closestDist < 5.0) {

                        paintObjects.remove(selectedObject);
                        tempObjects.add(selectedObject);
                    } else {
                        selectedObject.setCurrentPointIndex(-1);
                    }

                } else {

                    for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                        AnnotationObject paintObject = (AnnotationObject) iterator.next();
                        paintObject.setCurrentPointIndex(-1);
                        Point2D[] points = paintObject.getHighlightPoints();
                        for (int i = 0; i < points.length; i++) {
                            Point2D point = points[i];
                            double dist = point.distance(new Point2D.Double(e.getPoint().getX() / zoom, e.getPoint().getY() / zoom));
                            if (dist < closestDist) {
                                closestDist = dist;
                            }
                        }
                    }

                    if (closestDist < 5.0) {
                        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                            AnnotationObject paintObject = (AnnotationObject) iterator.next();

                            paintObject.setCurrentPointIndex(0);
                            paintObjects.remove(paintObject);
                            tempObjects.add(paintObject);
                        }
                    }

                }


            } else if (currentMode == AnnotationPaintPanel.MODE_ERASE) {
                Point loc = new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                int startX = loc.x - (eraseRad - 1) / 2;
                int startY = loc.y - (eraseRad - 1) / 2;
                int endX = loc.x + (eraseRad - 1) / 2;
                int endY = loc.y + (eraseRad - 1) / 2;
                if (startX < 0) {
                    startX = 0;
                }
                if (startY < 0) {
                    startY = 0;
                }
                if (endX > img.getWidth() - 1) {
                    endX = img.getWidth() - 1;
                }
                if (endY > img.getHeight() - 1) {
                    endY = img.getHeight() - 1;
                }

                WritableRaster raster = img.getRaster();
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        raster.setSample(x, y, 3, 0);
                    }
                }
            } else if (currentMode == AnnotationPaintPanel.MODE_POINT) {
                Point loc = new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                int startX = loc.x - (eraseRad - 1) / 2;
                int startY = loc.y - (eraseRad - 1) / 2;
                int endX = loc.x + (eraseRad - 1) / 2;
                int endY = loc.y + (eraseRad - 1) / 2;
                if (startX < 0) {
                    startX = 0;
                }
                if (startY < 0) {
                    startY = 0;
                }
                if (endX > img.getWidth() - 1) {
                    endX = img.getWidth() - 1;
                }
                if (endY > img.getHeight() - 1) {
                    endY = img.getHeight() - 1;
                }

                WritableRaster raster = img.getRaster();
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {

                        img.setRGB(x, y, this.getDrawColor().getRGB());
                        raster.setSample(x, y, 3, (int) (255 * this.getAlpha()));

                    }
                }
            }
        }
        drag = false;
        autoSaveAnnotaion();
        this.updatePanel();
    }

    public void mouseReleased(MouseEvent e) {
        AnnotationNoteDialog.setCurrentMode(currentMode);
        if (currentMode == AnnotationPaintPanel.MODE_ERASE || currentMode == AnnotationPaintPanel.MODE_POINT) {
            mousePoint = new Point((int) (e.getX() / zoom), (int) (e.getY() / zoom));

        }

        if (preCursor != null) {
            this.setCursor(preCursor);
            preCursor = null;
        }
        dragDown = null;


        for (Iterator iterator = tempObjects.iterator(); iterator.hasNext();) {
            AnnotationObject tempObject = (AnnotationObject) iterator.next();

            if (tempObject.finished(new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom)))) {
                paintObjects.add(tempObject);
            }
            tempObject.setStartDragLoc(null);
            tempObject.setCurrentPointIndex(-1);
        }

        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject selObject = (AnnotationObject) iterator.next();
            selObject.setCurrentPointIndex(-1);
        }

        tempObjects = new ArrayList();

        if (e.isPopupTrigger() & !drag & currentMode == AnnotationPaintPanel.MODE_SELECT) {

            ArrayList toDelete = new ArrayList();
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                toDelete.add(paintObject);
            }
            for (Iterator iterator = toDelete.iterator(); iterator.hasNext();) {
                AnnotationObject object = (AnnotationObject) iterator.next();


                if (object instanceof AnnotationNoteObject) {
                    AnnotationNoteObject notes = (AnnotationNoteObject) object;
                    Vector edit_users = new Vector();
                    edit_users = object.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        new AnnotationNoteDialog(this, notes, antialiased);
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                new AnnotationNoteDialog(this, notes, antialiased);
                            }
                        }
                    }
                }
            }
        }
        autoSaveAnnotaion();
        this.updatePanel();
    }

    public void mouseEntered(MouseEvent e) {
        if (locationPanel != null) {
            int x = (int) (e.getX() / zoom);
            int y = (int) (e.getY() / zoom);
            if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
                locationPanel.updateLocation(new Point(x, y));
            } else {
                locationPanel.updateLocation(null);
            }

            locationPanel.repaint();
        }
        if (currentMode == AnnotationPaintPanel.MODE_ERASE || currentMode == AnnotationPaintPanel.MODE_POINT) {
            mousePoint = new Point((int) (e.getX() / zoom), (int) (e.getY() / zoom));
            this.updatePanel();
        }

    }

    public void mouseExited(MouseEvent e) {
        if (locationPanel != null) {
            locationPanel.updateLocation(null);
            locationPanel.repaint();
        }
        if (currentMode == AnnotationPaintPanel.MODE_ERASE || currentMode == AnnotationPaintPanel.MODE_POINT) {
            mousePoint = null;
            this.repaint();
        }
    }

    private boolean chk_current_user(Vector edit_users) {
        if (edit_users.get(0).toString().equals("alluser>")) {
            return true;
        } else {
            for (int j = 0; j < edit_users.size(); j++) {
                if (edit_users.get(j).toString().equals(str_userid + ">")) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseDragged(MouseEvent e) {
        drag = true;
        if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
            System.out.println("darg2");
            if (scroll != null) {
                System.out.println("darg3");
                Point current = e.getPoint();
                int xMove = current.x - dragDown.x;
                int yMove = current.y - dragDown.y;

                Point origin = scroll.getViewport().getViewPosition();
                int newX = origin.x - xMove;
                int newY = origin.y - yMove;
                int xMax = this.getWidth() - scroll.getViewport().getWidth();
                int yMax = this.getHeight() - scroll.getViewport().getHeight();
                if (newX < 0) {
                    System.out.println("darg4");
                    newX = 0;
                }
                if (newY < 0) {
                    System.out.println("darg5");
                    newY = 0;
                }
                if (newX > xMax) {
                    System.out.println("darg6");
                    newX = xMax;
                }
                if (newY > yMax) {
                    System.out.println("darg7");
                    newY = yMax;
                }
                scroll.getViewport().setViewPosition(new Point(newX, newY));


            }

        } else if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            if (locationPanel != null) {
                int x = (int) (e.getX() / zoom);
                int y = (int) (e.getY() / zoom);
                if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
                    locationPanel.updateLocation(new Point(x, y));
                } else {
                    locationPanel.updateLocation(null);
                }

                locationPanel.repaint();
            }
            if (currentMode == AnnotationPaintPanel.MODE_ERASE) {
                mousePoint = null;
                Point loc = new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                int startX = loc.x - (eraseRad - 1) / 2;
                int startY = loc.y - (eraseRad - 1) / 2;
                int endX = loc.x + (eraseRad - 1) / 2;
                int endY = loc.y + (eraseRad - 1) / 2;
                if (startX < 0) {
                    startX = 0;
                }
                if (startY < 0) {
                    startY = 0;
                }
                if (endX > img.getWidth() - 1) {
                    endX = img.getWidth() - 1;
                }
                if (endY > img.getHeight() - 1) {
                    endY = img.getHeight() - 1;
                }
                WritableRaster raster = img.getRaster();
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        //          img.setRGB(x,y,bgColor.getRGB());
                        raster.setSample(x, y, 3, 0);
                    }
                }
            } else if (currentMode == AnnotationPaintPanel.MODE_POINT) {
                Point loc = new Point((int) (e.getPoint().x / zoom), (int) (e.getPoint().y / zoom));
                int startX = loc.x - (eraseRad - 1) / 2;
                int startY = loc.y - (eraseRad - 1) / 2;
                int endX = loc.x + (eraseRad - 1) / 2;
                int endY = loc.y + (eraseRad - 1) / 2;
                if (startX < 0) {
                    startX = 0;
                }
                if (startY < 0) {
                    startY = 0;
                }
                if (endX > img.getWidth() - 1) {
                    endX = img.getWidth() - 1;
                }
                if (endY > img.getHeight() - 1) {
                    endY = img.getHeight() - 1;
                }

                WritableRaster raster = img.getRaster();
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {

                        img.setRGB(x, y, this.getDrawColor().getRGB());
                        raster.setSample(x, y, 3, (int) (255 * this.getAlpha()));
                    }
                }
            } else {
                for (Iterator iterator = tempObjects.iterator(); iterator.hasNext();) {
                    int x = e.getX();
                    int y = e.getY();
                    if (x < 0) {
                        x = 0;
                    }
                    if (y < 0) {
                        y = 0;
                    }
                    if (x > this.getPreferredSize().width) {
                        x = this.getPreferredSize().width;
                    }
                    if (y > this.getPreferredSize().height) {
                        y = this.getPreferredSize().height;
                    }
                    AnnotationObject tempObject = (AnnotationObject) iterator.next();
                    if (tempObject.getStartDragLoc() == null) {
                        tempObject.setStartDragLoc(new Point((int) (x / zoom), (int) (y / zoom)));
                    }
                    //  ตรวจสอบดูก่อนว่าชิ้นที่เลือกเอาไว้นั้น ผู้ใช้คนนี้มีสิทธิ์หรือไม่
                    Vector edit_users = new Vector();
                    edit_users = tempObject.getEdit_user();
                    if (edit_users.get(0).toString().equals("alluser>")) {
                        tempObject.updateLocation(new Point((int) (x / zoom), (int) (y / zoom)));
                    } else {
                        for (int j = 0; j < edit_users.size(); j++) {
                            if (edit_users.get(j).toString().equals(str_userid + ">")) {
                                tempObject.updateLocation(new Point((int) (x / zoom), (int) (y / zoom)));
                            }
                        }
                    }
                }
            }
        }
        autoSaveAnnotaion();
        this.updatePanel();
    }

    public void mouseMoved(MouseEvent e) {
        if (locationPanel != null) {
            int x = (int) (e.getX() / zoom);
            int y = (int) (e.getY() / zoom);
            if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
                locationPanel.updateLocation(new Point(x, y));
            } else {
                locationPanel.updateLocation(null);
            }
            locationPanel.repaint();
        }
        if (currentMode == AnnotationPaintPanel.MODE_ERASE || currentMode == AnnotationPaintPanel.MODE_POINT) {
            mousePoint = new Point((int) (e.getX() / zoom), (int) (e.getY() / zoom));

            this.updatePanel();
        }

    }

    public void mouseWheelMoved(MouseWheelEvent e) {
//
//        if (currentMode == AnnotationPaintPanel.MODE_ERASE || currentMode == AnnotationPaintPanel.MODE_POINT) {
//            mousePoint = new Point((int) (e.getX() / zoom), (int) (e.getY() / zoom));
//
//        }
//        boolean ctrl = (e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK;
//        if (currentMode == AnnotationPaintPanel.MODE_ERASE && ctrl || currentMode == AnnotationPaintPanel.MODE_POINT && ctrl) {
//            if (e.getWheelRotation() > 0) {
//                eraseRad -= 2;
//                if (eraseRad < 1) {
//                    eraseRad = 1;
//                }
//            }
//            if (e.getWheelRotation() < 0) {
//                eraseRad += 2;
//            }
//            this.updatePanel();
//        } else {
        if (e.getWheelRotation() > 0) {
            this.setZoom(this.getZoom() - 0.25);
//                zoom--;
//                if (zoom < 1) {
//                    zoom = 1;
//                }
        }
        if (e.getWheelRotation() < 0) {
            //zoom++;
            this.setZoom(this.getZoom() + 0.25);
        }
        this.zoomed(e.getPoint());
    //}
    }
    
    public void loadProperties() {
//        File file = new File("jimgpnt.properties");
//        if (file.exists()) {
//            properties = new Properties();
//            try {
//                properties.load(new FileInputStream(file));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
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
//        }
    }

//    public void saveProperties() {
//        File file = new File("jimgpnt.properties");
//        try {
//            FileOutputStream output = new FileOutputStream(file);
//            properties.store(output, "JImagePaint Properties version 1.0");
//            output.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
