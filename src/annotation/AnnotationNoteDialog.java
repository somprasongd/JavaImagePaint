/*
 * AnnotationNoteDialog.java
 *
 * Created on 3 มิถุนายน 2551, 11:00 น.
 */

package annotation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import annotation.util.ColorButton;
import annotation.util.StrokeChooserPanel;



/**
 *
 * @author  Somprasong Damyos
 */
public class AnnotationNoteDialog extends javax.swing.JDialog implements PropertyChangeListener{
    
    private AnnotationPaintPanel paintPanel;
    private Point2D locationStart;
    private Color textsColor = Color.BLACK;
    private Color bgsColor =Color.YELLOW;
    private Color brColor = Color.BLUE;    
    private boolean antialiased;
    private Font font;
    private static int mode;
    private AnnotationFontChooserPanel fontChooser;
    private ColorButton textColor;
    private ColorButton bgColor;
    private ColorButton borderColor;
    private StrokeChooserPanel stroke;
    private static Stroke strokes;
    private JCheckBox jck;
    private boolean bgTransparent = false;
    private JLabel label;
    private AnnotationNoteObject noteObj = null;
    private String oldNote;
    private boolean fixNote = false;
    private double strokeWidth;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    /** Creates new form AnnotationNoteDialog */
    
    public AnnotationNoteDialog(AnnotationPaintPanel paintPanel, Point2D locationStart, String view_user, String edit_user, 
            String del_user, String owner_user, boolean antialiased) {
        super(null, DEFAULT_MODALITY_TYPE);
        this.paintPanel = paintPanel;
        this.locationStart = locationStart;
        this.font = new Font("Tahoma", 0, 10);
        this.antialiased = antialiased;
        AnnotationNoteDialog.strokes = new BasicStroke(1);
        this.strokeWidth = 1.0;
        this.view_user = view_user;
        this.edit_user = edit_user;
        this.del_user = del_user;
        this.owner_user = owner_user;
        initComponents();
        this.setLocationRelativeTo(null);
        
        this.show();
        
    }
    // แก้ใขโน๊ตเก่า
    public AnnotationNoteDialog(AnnotationPaintPanel paintPanel, AnnotationNoteObject noteObj, boolean antialiased){
        super(null, DEFAULT_MODALITY_TYPE);
        this.paintPanel = paintPanel;
        this.noteObj = noteObj;
        this.locationStart = this.noteObj.getLocation();
        this.font = this.noteObj.getFont();
        this.oldNote = this.noteObj.getText();
        this.antialiased = antialiased;
        this.textsColor = this.noteObj.gettextColor();
        this.bgsColor = this.noteObj.getBGColor();
        this.brColor = this.noteObj.getBDColor(); 
        this.bgTransparent = this.noteObj.isBackgroundTransparent();
        this.view_user = this.noteObj.getViewUser();
        this.edit_user = this.noteObj.getEditUser();
        this.del_user = this.noteObj.getDeleteUser();
        this.owner_user = this.noteObj.getOwnerUser();
        
        AnnotationNoteDialog.strokes = this.noteObj.getStroke();
        this.strokeWidth = this.noteObj.getStrokeWidth();
                
        fixNote = true;
        String str_note =  oldNote.replace(":", "\n");
        initComponents();
        note_area.setText(str_note);
        this.setLocationRelativeTo(null);
        this.show();
       
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        option_panel = new javax.swing.JPanel();
        font_Panel = new javax.swing.JPanel();
        colorChooser_Panel = new javax.swing.JPanel();
        button_Panel = new javax.swing.JPanel();
        ok_button = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        note_area = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("โน็ต");

        option_panel.setRequestFocusEnabled(false);
        option_panel.setLayout(new javax.swing.BoxLayout(option_panel, javax.swing.BoxLayout.Y_AXIS));
        option_panel.add(font_Panel);
        fontChooser = new AnnotationFontChooserPanel(font, "font", 1);
        fontChooser.addPropertyChangeListener(this);
        font_Panel.add(fontChooser);
        font_Panel.setEnabled(false);

        colorChooser_Panel.setLayout(new java.awt.GridLayout(3, 2, 10, 7));
        option_panel.add(colorChooser_Panel);
        textColor = new ColorButton(textsColor, "สีข้อความ", paintPanel, "เลือกสีข้อความ", "textColor");
        textColor.addPropertyChangeListener(this);
        colorChooser_Panel.add(textColor);

        borderColor = new ColorButton(brColor, "สีเส้นขอบ", paintPanel, "เลือกสีเส้นขอบ", "borderColor");
        borderColor.addPropertyChangeListener(this);
        colorChooser_Panel.add(borderColor);

        bgColor = new ColorButton(bgsColor, "สีพื้นหลัง", paintPanel, "เลือกสีพื้นหลัง", "bgColor");
        bgColor.addPropertyChangeListener(this);
        colorChooser_Panel.add(bgColor);

        jck = new JCheckBox("พื้นหลังโปร่งใส");
        if(bgTransparent)
        jck.setSelected(true);
        colorChooser_Panel.add(jck);
        jck.addItemListener( new ItemListener() {
            public void itemStateChanged(ItemEvent e){
                if (e.getStateChange() == e.SELECTED)
                bgTransparent = true;
                else
                bgTransparent = false;
            }
        } );

        label = new JLabel("ขนาดเส้นขอบ");
        colorChooser_Panel.add(label);

        stroke = new StrokeChooserPanel(strokes, "stroke",this.strokeWidth);
        stroke.addPropertyChangeListener(this);
        colorChooser_Panel.add(stroke);

        button_Panel.setPreferredSize(new java.awt.Dimension(60, 40));

        ok_button.setText("ตกลง");
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok_buttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout button_PanelLayout = new javax.swing.GroupLayout(button_Panel);
        button_Panel.setLayout(button_PanelLayout);
        button_PanelLayout.setHorizontalGroup(
            button_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
            .addGroup(button_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(button_PanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ok_button)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        button_PanelLayout.setVerticalGroup(
            button_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 109, Short.MAX_VALUE)
            .addGroup(button_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(button_PanelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(ok_button)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        option_panel.add(button_Panel);

        getContentPane().add(option_panel, java.awt.BorderLayout.LINE_END);

        note_area.setColumns(20);
        note_area.setLineWrap(true);
        note_area.setRows(5);
        jScrollPane1.setViewportView(note_area);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ok_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok_buttonActionPerformed
        String note_before = note_area.getText();
        
        String note_after = note_before.replace("\n", ":");
        
        if(fixNote){
            paintPanel.removeSelectNote(noteObj);
        }
        
        if(!note_after.equals("")){
            //JImageNoteObject notes = new JImageNoteObject(paintPanel, view_user, edit_user, del_user, owner_user, font, textsColor, bgsColor, brColor, new Point((int)locationStart.getX(), (int)locationStart.getY()), note_after, antialiased, strokes, bgTransparent);
            AnnotationNoteObject notes = new AnnotationNoteObject(paintPanel, view_user, edit_user, del_user, owner_user, font, textsColor, bgsColor, brColor, new Point((int)locationStart.getX(), (int)locationStart.getY()), note_after, antialiased, strokes, bgTransparent, 0.0, 0.0, 1.0f);
            notes.setStrokeWidth(this.stroke.getStrokeWidth());
            //notes.setPanel(paintPanel);
            paintPanel.addNoteObjects(notes);
            
        }
        
        this.dispose();
    }//GEN-LAST:event_ok_buttonActionPerformed
    public static void setCurrentMode(int mode){
        AnnotationNoteDialog.mode = mode;
    }
    
    
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("font")) {
         font = (Font) evt.getNewValue();
        }
        
        if (evt.getPropertyName().equals("textColor")) {
         textsColor = (Color) evt.getNewValue();
        }
        
        if (evt.getPropertyName().equals("bgColor")) {
         bgsColor = (Color) evt.getNewValue();
        }
        
        if (evt.getPropertyName().equals("borderColor")) {
         brColor = (Color) evt.getNewValue();
        }
        
        if (evt.getPropertyName().equals("stroke")) {
         strokes = (Stroke) evt.getNewValue();
        }
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel button_Panel;
    private javax.swing.JPanel colorChooser_Panel;
    private javax.swing.JPanel font_Panel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea note_area;
    private javax.swing.JButton ok_button;
    private javax.swing.JPanel option_panel;
    // End of variables declaration//GEN-END:variables
    
}