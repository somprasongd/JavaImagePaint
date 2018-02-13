/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import annotation.util.ColorButton;
import annotation.util.Language;
import annotation.util.StrokeChooserPanel;
import javax.swing.plaf.DimensionUIResource;

/**
 *
 * @author Somprasong Damyos
 */
public class AnnotationEditToolbar extends JToolBar implements PropertyChangeListener, ActionListener, ChangeListener {

    private AnnotationPaintPanel paintPanel;
    private JToggleButton antiAlias;
    private JToggleButton fillColor;
    private ColorButton fgColor;
    private StrokeChooserPanel stroke;
    private ColorButton textColor;
    private AnnotationFontChooserPanel font;
    private JSlider alpha;
    private JButton delete;

    public AnnotationEditToolbar(AnnotationPaintPanel paintPanel) {
        this.paintPanel = paintPanel;

        ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_antiAlias));
        antiAlias = new JToggleButton(icon);
        antiAlias.setToolTipText(Language.getStr_anitAlias());
        antiAlias.setSelected(paintPanel.isAntialiased());
        antiAlias.addActionListener(this);
        antiAlias.setActionCommand("antiAlias");
        this.add(antiAlias);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_fillColor));
        fillColor = new JToggleButton(icon);
        fillColor.setToolTipText(Language.getStr_fillColor());
        fillColor.setSelected(paintPanel.isFilled());
        fillColor.addActionListener(this);
        fillColor.setActionCommand("fillColor");
        this.add(fillColor);
        
        String str_fgColor = this.getClass().getResource(Icon.icon_selectColor).toString();
        fgColor = new ColorButton(paintPanel.getDrawColor(), "<html><body><img src=\""+str_fgColor+"\"></body></html>", paintPanel, "Foreground Color", "fgColor");
        fgColor.setMaximumSize(new DimensionUIResource(50, 25));
        fgColor.setToolTipText(Language.getStr_color());
        this.add(fgColor);
        fgColor.addPropertyChangeListener(this);

        stroke = new StrokeChooserPanel(paintPanel.getStroke(), "stroke");
        stroke.addPropertyChangeListener(this);
        this.add(stroke);

        this.addSeparator();
        String str_textColor = this.getClass().getResource(Icon.icon_selectColorText).toString();
        textColor = new ColorButton(paintPanel.getTextColor(), "<html><body><img src=\""+str_textColor+"\"></body></html>", paintPanel, "Foreground Color", "textColor");
        textColor.setMaximumSize(new DimensionUIResource(50, 25));
        textColor.setToolTipText(Language.getStr_textColor());
        this.add(textColor);
        textColor.addPropertyChangeListener(this);
              

        font = new AnnotationFontChooserPanel(paintPanel.getFont(), "font");
        font.addPropertyChangeListener(this);
        this.add(font);

        this.addSeparator();

        alpha = new JSlider(0, 0, 255, (int) (paintPanel.getAlpha() * 255));
        alpha.setToolTipText(Language.getStr_transparent());
        alpha.addChangeListener(this);
        alpha.setMaximumSize(new Dimension(100, 100));
        this.add(alpha);

        this.addSeparator();
        
        icon = new ImageIcon(this.getClass().getResource(Icon.icon_bin));
        delete = new JButton(icon);
        delete.setToolTipText(Language.getStr_delete());
        delete.setActionCommand("delete");
        delete.addActionListener(this);
        this.add(delete);
        this.addSeparator();
        
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("fgColor")) {
            paintPanel.setDrawColor((Color) evt.getNewValue());
        }
        if (evt.getPropertyName().equals("textColor")) {
            paintPanel.setTextColor((Color) evt.getNewValue());
        }

        if (evt.getPropertyName().equals("font")) {
            paintPanel.setFont((Font) evt.getNewValue());
        }
        if (evt.getPropertyName().equals("stroke")) {
            paintPanel.setStroke((Stroke) evt.getNewValue());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("antiAlias")) {
            antiAlias.setSelected(antiAlias.isSelected());
            paintPanel.setAntialiased(antiAlias.isSelected());
        }
        if (e.getActionCommand().equals("fillColor")) {
            fillColor.setSelected(fillColor.isSelected());
            paintPanel.setFilled(fillColor.isSelected());
        }
        if (e.getActionCommand().equals("delete")) {
            paintPanel.deleteSelected();
        }
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource().equals(alpha)) {
            float newVal = (float) (alpha.getValue()) / 255.0f;
            paintPanel.setAlpha(newVal);
        }
    }
}
