/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author sompr
 */
public class ColorButton extends JButton implements ActionListener{

    public ColorButton() {
        super();
    }

    public ColorButton(Icon icon) {
        super(icon);
    }

    public ColorButton(String text) {
        super(text);
    }

    public ColorButton(Action a) {
        super(a);
    }

    public ColorButton(String text, Icon icon) {
        super(text, icon);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
