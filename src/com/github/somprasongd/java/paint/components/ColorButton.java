/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

/**
 *
 * @author sompr
 */
public class ColorButton extends JButton implements ActionListener {

    private Color color = Color.BLACK;
    private String propName = "CHOOSED_COLOR";
    private Component chooserParent;
    private String chooserTitle;
    private JDialog chooserDialog;
    private JColorChooser chooser;

    public ColorButton() {
        super();
        init();
    }

    public ColorButton(Icon icon) {
        super(icon);
        init();
    }

    public ColorButton(String text) {
        super(text);
        init();
    }

    public ColorButton(Action a) {
        super(a);
        init();
    }

    public ColorButton(String text, Icon icon) {
        super(text, icon);
        init();
    }

    private void init() {
        setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        chooser = new JColorChooser();
        chooser.setColor(color);
        chooserDialog = JColorChooser.createDialog(chooserParent, chooserTitle, true, chooser, this, this);

        this.setMargin(new Insets(2, 2, 2, 2));
        this.buildImage();
        this.addActionListener(this);
    }

    public String getPropName() {
        return propName;
    }

    /**
     *
     * @param propName Name of the property to file. This button will fire a
     * PropertyChagneEvent when the color has been changed with an old value of
     * oldColor and a new value of newColor
     */
    public void setPropName(String propName) {
        this.propName = propName;
    }

    public Component getChooserParent() {
        return chooserParent;
    }

    public void setChooserParent(Component chooserParent) {
        this.chooserParent = chooserParent;
    }

    public String getChooserTitle() {
        return chooserTitle;
    }

    public void setChooserTitle(String chooserTitle) {
        this.chooserTitle = chooserTitle;
    }

    public void setColor(Color color) {
        this.color = color;
        this.buildImage();
    }

    public Color getColor() {
        return color;
    }

    private void buildImage() {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 16, 16);
        this.setIcon(new ImageIcon(img));
        this.repaint();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this && !chooserDialog.isVisible()) {
            chooserDialog.setVisible(true);
            return;
        }
        if (e.getActionCommand().equals("OK")) {
            Color sel = chooser.getColor();
            if (sel != null) {
                this.firePropertyChange(propName, color, sel);
                this.setColor(sel);
            }
        }
    }

}
