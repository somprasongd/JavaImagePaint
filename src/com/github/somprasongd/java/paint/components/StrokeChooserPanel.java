/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.components;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 *
 * @author sompr
 */
public class StrokeChooserPanel extends javax.swing.JPanel {

    private String propName = "CHOOSED_STROKE";

    /**
     * Creates new form StrokeChooserPanel
     */
    public StrokeChooserPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSpinner1 = new javax.swing.JSpinner();

        setLayout(new java.awt.GridBagLayout());

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.5d, 100.0d, 0.5d));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/github/somprasongd/java/paint/Bundle"); // NOI18N
        jSpinner1.setToolTipText(bundle.getString("STROKE")); // NOI18N
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(jSpinner1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
      this.firePropertyChange(propName, null, new BasicStroke(((Double) jSpinner1.getModel().getValue()).floatValue()));
    }//GEN-LAST:event_jSpinner1StateChanged

    public String getPropName() {
        return propName;
    }

    /**
     * 
     * @param propName This will fire a PropertyChagneEvent when the stroke has been changed
     */
    public void setPropName(String propName) {
        this.propName = propName;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner jSpinner1;
    // End of variables declaration//GEN-END:variables
}
