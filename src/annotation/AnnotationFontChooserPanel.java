package annotation;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.github.somprasongd.java.paint.utils.Language;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Somprasong Damyos
 */

public class AnnotationFontChooserPanel extends JPanel implements ActionListener, ChangeListener {
   private JComboBox fontNameBox;
   private JToggleButton textBold;
   private JToggleButton textItalic;
   //private JToggleButton textUnderlined;
   private JSpinner size;
   private String propName;

   public AnnotationFontChooserPanel(Font font, String propName) {
      super();
      this.propName = propName;
      this.setFont(font);
      this.add(getFontNameBox());
      this.add(getSizeSpinner());
      this.add(getBoldButton());
      this.add(getItalicButton());
      this.setMaximumSize(new Dimension(320,100));
      //this.add(getUnderlinedButton());

   }
   
   public AnnotationFontChooserPanel(Font font, String propName, int style) {
      super();
      this.propName = propName;
      this.setFont(font);
      
      JPanel fontChooser = new JPanel();
      fontChooser.setLayout(new FlowLayout(FlowLayout.LEFT));
      fontChooser.add(getFontNameBox());
      fontChooser.setToolTipText(Language.getStr_fontChooser());
      
      JPanel sizeChooser = new JPanel();
      sizeChooser.setLayout(new FlowLayout(FlowLayout.LEFT));
      sizeChooser.add(getSizeSpinner());
      sizeChooser.add(getBoldButton());
      sizeChooser.add(getItalicButton());
      sizeChooser.setToolTipText(Language.getStr_sizeChooser());
      //this.add(getUnderlinedButton());
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.add(fontChooser);
      this.add(sizeChooser);
   }

   public JComboBox getFontNameBox() {
      String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
      fontNameBox = new JComboBox(fonts);
      String font = this.getFont().getFontName();
      String fontName = font;
      if (font.indexOf(".") > -1) {
         fontName = font.substring(0, font.indexOf("."));
      }


      fontNameBox.setSelectedItem(fontName);
      fontNameBox.addActionListener(this);
      return fontNameBox;
   }

   public JSpinner getSizeSpinner() {
      SpinnerNumberModel model = new SpinnerNumberModel(this.getFont().getSize(), 1, 1000, 1);
      size = new JSpinner(model);
      size.addChangeListener(this);
      return size;
   }

   public JToggleButton getBoldButton() {
      ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_textBold));
      textBold = new JToggleButton(icon);

      textBold.setMargin(new Insets(2, 2, 2, 2));
      textBold.setToolTipText(Language.getStr_textBold());
      textBold.setFocusPainted(false);
      textBold.addChangeListener(this);
      return textBold;
   }

   public JToggleButton getItalicButton() {
      ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_textItalic));
      textItalic = new JToggleButton(icon);

      textItalic.setMargin(new Insets(2, 2, 2, 2));
      textItalic.setFocusPainted(false);
      textItalic.setToolTipText(Language.getStr_textItalic());
      textItalic.addChangeListener(this);
      return textItalic;
   }

   /*public JToggleButton getUnderlinedButton()
    {
        ImageIcon imageicon = new ImageIcon(getClass().getResource("graphics/underlined.png"));
        textUnderlined = new JToggleButton(imageicon);
        textUnderlined.setFocusable(false);
        textUnderlined.addActionListener(this);
        textUnderlined.setMargin(new Insets(2, 2, 2, 2));
        textUnderlined.setToolTipText("Underlined (Not work!!!!!!!!)");
        return textUnderlined;
    }*/

   public void actionPerformed(ActionEvent e) {
      Font oldF = this.getFont();
      int b = Font.PLAIN;
      if (textBold.isSelected()) {
         b = Font.BOLD;
      }
      int i = Font.PLAIN;
      if (textItalic.isSelected()) {
         i = Font.ITALIC;
      }
      Font newF = new Font(fontNameBox.getSelectedItem().toString(), b | i, ((Integer) size.getModel().getValue()).intValue());
      this.firePropertyChange(propName, oldF, newF);
      
   }
    
   public void stateChanged(ChangeEvent e) {
      Font oldF = this.getFont();
      int b = Font.PLAIN;
      if (textBold.isSelected()) {
         b = Font.BOLD;
      }
      int i = Font.PLAIN;
      if (textItalic.isSelected()) {
         i = Font.ITALIC;
      }
      Font newF = new Font(fontNameBox.getSelectedItem().toString(), b | i, ((Integer) size.getModel().getValue()).intValue());
      this.firePropertyChange(propName, oldF, newF);
   }

    @Override
   public void setFont(Font font) {
      super.setFont(font);
      if (font != null && textBold != null) {
         textBold.setSelected(font.isBold());
         textItalic.setSelected(font.isItalic());
         size.setValue(new Integer(font.getSize()));
         String fontName = font.getFontName().substring(0, font.getFontName().indexOf("."));
         fontNameBox.setSelectedItem(fontName);
      }
   }
    
   /*public void setUnderlined(boolean flag)
    {
        if(textUnderlined != null)
            textUnderlined.setSelected(flag);
    }*/
}
