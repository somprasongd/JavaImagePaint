package annotation;

import com.github.somprasongd.java.paint.utils.Language;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A JToolBar used to change the mode of a AnnotationPaintPanel.
 */
public class AnnotationToolsToolbar extends JToolBar implements PropertyChangeListener, ActionListener {

    private AnnotationPaintPanel paintPanel;
    private JToggleButton select;
    private JToggleButton line;
    private JToggleButton rect;
    private JToggleButton oval;
    private JToggleButton note;
    private JToggleButton text;
    private JToggleButton arrow;
    private JButton saveAnno;

    public AnnotationToolsToolbar(AnnotationPaintPanel paintPanel, String host, boolean can_add) {
        this.paintPanel = paintPanel;
        initModeButtons();
    }

    public void initModeButtons() {
        ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_cursor));
        select = new JToggleButton(icon);
        select.setToolTipText(Language.getStr_select());
        select.setActionCommand("select");
        select.addActionListener(this);
        //select.setSelected(true);
        this.add(select);
        this.addSeparator();

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_line));
        line = new JToggleButton(icon);
        line.setToolTipText(Language.getStr_line());
        line.setActionCommand("line");
        line.addActionListener(this);
        //line.setSelected(false);
//        line.setEnabled(can_add);
        this.add(line);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_ractangle));
        rect = new JToggleButton(icon);
        rect.setToolTipText(Language.getStr_rectangle());
        rect.setActionCommand("rect");
        rect.addActionListener(this);
//        rect.setEnabled(can_add);
        this.add(rect);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_oval));
        oval = new JToggleButton(icon);
        oval.setToolTipText(Language.getStr_oval());
        oval.setActionCommand("oval");
        oval.addActionListener(this);
//        oval.setEnabled(can_add);
        this.add(oval);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_arrow));
        arrow = new JToggleButton(icon);
        arrow.setToolTipText(Language.getStr_arrow());
        arrow.setActionCommand("arrow");
        arrow.addActionListener(this);
//        arrow.setEnabled(can_add);
        this.add(arrow);
        
        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_text));
        text = new JToggleButton(icon);
        text.setToolTipText(Language.getStr_text());
        text.setActionCommand("text");
        text.addActionListener(this);
//        text.setEnabled(can_add);
        this.add(text);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_draw_note));
        note = new JToggleButton(icon);
        note.setToolTipText(Language.getStr_note());
        note.setActionCommand("note");
        note.addActionListener(this);
//        note.setEnabled(can_add);
        this.add(note);

        this.addSeparator();

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_save));
        saveAnno = new JButton(icon);
        saveAnno.setActionCommand("saveAnno");
        saveAnno.setToolTipText(Language.getStr_save());
//        saveAnno.setEnabled(can_add);
        saveAnno.addActionListener(this);
        this.add(saveAnno);

        this.addSeparator();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("mode")) {
            int val = ((Integer) evt.getNewValue()).intValue();

            select.setSelected(false);
            line.setSelected(false);
            rect.setSelected(false);
            oval.setSelected(false);
            note.setSelected(false);
            text.setSelected(false);
            arrow.setSelected(false);

            if (val == AnnotationPaintPanel.MODE_SELECT) {
                select.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_LINE) {
                line.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_RECT) {
                rect.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_OVAL) {
                oval.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_NOTE) {
                note.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_TEXT) {
                text.setSelected(true);
            }
            if (val == AnnotationPaintPanel.MODE_QUADARROW) {
                arrow.setSelected(true);
            }
            this.revalidate();
        }
    }

    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().equals("select")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_SELECT);
//            if (can_add) {
//                private_user.setEnabled(true);
//            }
        }
        if (e.getActionCommand().equals("line")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_LINE);
        }
        if (e.getActionCommand().equals("rect")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_RECT);
        }
        if (e.getActionCommand().equals("oval")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_OVAL);
        }
        if (e.getActionCommand().equals("text")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_TEXT);
        }
        if (e.getActionCommand().equals("note")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_NOTE);
        }
        if (e.getActionCommand().equals("arrow")) {
            paintPanel.setCurrentMode(AnnotationPaintPanel.MODE_QUADARROW);
        }        
        if (e.getActionCommand().equals("saveAnno")) {
            paintPanel.saveAllAnnotation();
        }
    }
}
