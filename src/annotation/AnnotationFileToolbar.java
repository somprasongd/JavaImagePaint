package annotation;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.github.somprasongd.java.paint.utils.Language;
import java.awt.Point;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Somprasong Damyos
 */
public class AnnotationFileToolbar extends JToolBar implements ActionListener {

    private AnnotationPaintPanel paintPanel;
    private JButton flipRight;
    private JButton flip180;
    private JButton flipLeft;
    private JButton zoomIn;
    private JButton zoomOut;
    private JButton set1To1;

    public AnnotationFileToolbar(AnnotationPaintPanel paintPanel) {
        this.paintPanel = paintPanel;
        
        ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_flip_right));
        flipRight = new JButton(icon);
        flipRight.setActionCommand("flipRight");
        flipRight.setToolTipText(Language.getStr_flipRight());
        flipRight.addActionListener(this);
        this.add(flipRight);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_flip_180));
        flip180 = new JButton(icon);
        flip180.setActionCommand("flip180");
        flip180.setToolTipText(Language.getStr_flip180());
        flip180.addActionListener(this);
        this.add(flip180);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_flip_left));
        flipLeft = new JButton(icon);
        flipLeft.setActionCommand("flipLeft");
        flipLeft.setToolTipText(Language.getStr_flipLeft());
        flipLeft.addActionListener(this);
        this.add(flipLeft);

        this.addSeparator();

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_zoomIn));
        zoomIn = new JButton(icon);
        zoomIn.setActionCommand("zoomIn");
        zoomIn.setToolTipText(Language.getStr_zoomIn());
        zoomIn.addActionListener(this);
        this.add(zoomIn);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_zoomOut));
        zoomOut = new JButton(icon);
        zoomOut.setActionCommand("zoomOut");
        zoomOut.setToolTipText(Language.getStr_zoomOut());
        zoomOut.addActionListener(this);
        this.add(zoomOut);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_1to1));
        set1To1 = new JButton(icon);
        set1To1.setActionCommand("set1To1");
        set1To1.setToolTipText(Language.getStr_set1To1());
        set1To1.addActionListener(this);
        this.add(set1To1);
        this.addSeparator();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("zoomIn")) {
            paintPanel.setZoom(paintPanel.getZoom() + 0.25);
            paintPanel.zoomed(new Point(0, 0));
        }
        if (e.getActionCommand().equals("zoomOut")) {
            paintPanel.setZoom(paintPanel.getZoom() - 0.25);
            paintPanel.zoomed(new Point(0, 0));
        }
        if (e.getActionCommand().equals("set1To1")) {
            paintPanel.setZoom(1);
            paintPanel.zoomed(new Point(0, 0));
        }
        if (e.getActionCommand().equals("flipRight")) {
            paintPanel.rotate(90.0);
        }
        if (e.getActionCommand().equals("flip180")) {
            paintPanel.rotate(180.0);
        }
        if (e.getActionCommand().equals("flipLeft")) {
            paintPanel.rotate(-90.0);
        }
    }
}
