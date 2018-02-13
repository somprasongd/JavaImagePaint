/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package annotation;

import annotation.util.Language;
import annotation.util.NumberDocument;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author Somprasong Damyos 2/9/2008
 */
public class AnnotationNavigatorBar extends JToolBar implements ActionListener {

    private final static int FIRSTIMAGE = 0;
    private JTextField currentImage;
    private URL[] url_Images;
    private AnnotationPaintPanel paintPanel;
    private JButton nv_firstImage;
    private JButton nv_previousImage;
    private JButton nv_nextImage;
    private JButton nv_lastImage;
    private int i_currentImage = 0;
    

    public AnnotationNavigatorBar() {
    }

    public AnnotationNavigatorBar(URL[] url_Images, AnnotationPaintPanel paintPanel) {
        this.url_Images = url_Images;
        this.paintPanel = paintPanel;
        int i_totalImage = url_Images.length;

        ImageIcon icon = new ImageIcon(this.getClass().getResource(Icon.icon_nv_first));
        nv_firstImage = new JButton(icon);
        nv_firstImage.setActionCommand("nv_firstImage");
        nv_firstImage.setToolTipText(Language.getStr_nv_firstImage());
        nv_firstImage.addActionListener(this);
        this.add(nv_firstImage);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_nv_prev));
        nv_previousImage = new JButton(icon);
        nv_previousImage.setActionCommand("nv_previousImage");
        nv_previousImage.setToolTipText(Language.getStr_nv_previousImage());
        nv_previousImage.addActionListener(this);
        this.add(nv_previousImage);

        currentImage = new JTextField("", 4);
        currentImage.setPreferredSize(new Dimension(3, 10));
        currentImage.setMaximumSize(new Dimension(30, 20));
        currentImage.setDocument(new NumberDocument());
        currentImage.setActionCommand("currentImage");
        currentImage.addActionListener(this);
        this.add(currentImage);
        JLabel label = new JLabel(" / " + i_totalImage);
        this.add(label);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_nv_next));
        nv_nextImage = new JButton(icon);
        nv_nextImage.setActionCommand("nv_nextImage");
        nv_nextImage.setToolTipText(Language.getStr_nv_nextImage());
        nv_nextImage.addActionListener(this);
        this.add(nv_nextImage);

        icon = new ImageIcon(this.getClass().getResource(Icon.icon_nv_last));
        nv_lastImage = new JButton(icon);
        nv_lastImage.setActionCommand("nv_lastImage");
        nv_lastImage.setToolTipText(Language.getStr_nv_lastImage());
        nv_lastImage.addActionListener(this);
        this.add(nv_lastImage);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("nv_firstImage")) {
            this.nv_nextImage.setEnabled(true);
            this.nv_previousImage.setEnabled(false);
            this.i_currentImage = FIRSTIMAGE;
            this.currentImage.setText("" + (i_currentImage+1));
            this.paintPanel.loadNewImage(url_Images[FIRSTIMAGE]);
        }
        if (e.getActionCommand().equals("nv_previousImage")) {
            gotoImage(i_currentImage - 1);
        }
        if (e.getActionCommand().equals("nv_nextImage")) {
            gotoImage(i_currentImage + 1);
        }
        if (e.getActionCommand().equals("nv_lastImage")) {
            this.nv_previousImage.setEnabled(true);
            this.nv_nextImage.setEnabled(false);
            this.i_currentImage = url_Images.length - 1;
            this.currentImage.setText("" + (i_currentImage+1));
            this.paintPanel.loadNewImage(url_Images[url_Images.length - 1]);
        }
        if (e.getActionCommand().equals("currentImage")) {
            int i_imageno = Integer.parseInt(currentImage.getText()) - 1;
            gotoImage(i_imageno);
        }
    }

    public void gotoImage(int i_image) {
        if (i_image >= FIRSTIMAGE && i_image < url_Images.length) {
            this.i_currentImage = i_image;
            this.currentImage.setText("" + (i_currentImage + 1));
            this.chkCurrImage();
            this.paintPanel.loadNewImage(url_Images[i_image]);
        }else{
             JOptionPane.showMessageDialog(null, Language.getStr_errorMessage(), Language.getStr_errorTitle(),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chkCurrImage() {
        nv_previousImage.setEnabled(true);
        nv_nextImage.setEnabled(true);
        if (i_currentImage == 0) {
            nv_previousImage.setEnabled(false);
        }
        if (i_currentImage == url_Images.length - 1) {
            nv_nextImage.setEnabled(false);
        }
    }
}
