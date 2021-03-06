/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeListener;

/**
 *
 * @author sompr
 */
public class FontChooserPanel extends javax.swing.JPanel {

    private String propName;
    private final String[] FONT_LIST = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    /**
     * Creates new form FontChooserPanel
     */
    public FontChooserPanel() {
        initComponents();

        reset();
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

        combFontName = new javax.swing.JComboBox(FONT_LIST);
        spinnerFontSize = new javax.swing.JSpinner();
        tglBold = new javax.swing.JToggleButton();
        tglItalic = new javax.swing.JToggleButton();
        btnColor = new com.github.somprasongd.java.paint.components.ColorButton();

        setLayout(new java.awt.GridBagLayout());

        combFontName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combFontNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(combFontName, gridBagConstraints);

        spinnerFontSize.setModel(new javax.swing.SpinnerNumberModel(14, 1, 500, 1));
        spinnerFontSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerFontSizeStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(spinnerFontSize, gridBagConstraints);

        tglBold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/textBold.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/github/somprasongd/java/paint/Bundle"); // NOI18N
        tglBold.setToolTipText(bundle.getString("FONT_BOLD")); // NOI18N
        tglBold.setMaximumSize(new java.awt.Dimension(26, 26));
        tglBold.setMinimumSize(new java.awt.Dimension(26, 26));
        tglBold.setPreferredSize(new java.awt.Dimension(26, 26));
        tglBold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(tglBold, gridBagConstraints);

        tglItalic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/textItalic.png"))); // NOI18N
        tglItalic.setToolTipText(bundle.getString("FONT_ITALIC")); // NOI18N
        tglItalic.setMaximumSize(new java.awt.Dimension(26, 26));
        tglItalic.setMinimumSize(new java.awt.Dimension(26, 26));
        tglItalic.setPreferredSize(new java.awt.Dimension(26, 26));
        tglItalic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglItalicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(tglItalic, gridBagConstraints);

        String iconChooseColor = getClass().getResource("/com/github/somprasongd/java/paint/icons/selectColorText.png").toString();
        btnColor.setText("<html><body><img src=\"" + iconChooseColor + "\"></body></html>");
        btnColor.setPropName("fontColor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(btnColor, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void combFontNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combFontNameActionPerformed
        updateFont();
    }//GEN-LAST:event_combFontNameActionPerformed

    private void spinnerFontSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerFontSizeStateChanged
        updateFont();
    }//GEN-LAST:event_spinnerFontSizeStateChanged

    private void tglBoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBoldActionPerformed
        updateFont();
    }//GEN-LAST:event_tglBoldActionPerformed

    private void tglItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglItalicActionPerformed
        updateFont();
    }//GEN-LAST:event_tglItalicActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.github.somprasongd.java.paint.components.ColorButton btnColor;
    private javax.swing.JComboBox combFontName;
    private javax.swing.JSpinner spinnerFontSize;
    private javax.swing.JToggleButton tglBold;
    private javax.swing.JToggleButton tglItalic;
    // End of variables declaration//GEN-END:variables

    public Font getSelectedFont() {
        int b = Font.PLAIN;
        if (tglBold.isSelected()) {
            b = Font.BOLD;
        }
        int i = Font.PLAIN;
        if (tglItalic.isSelected()) {
            i = Font.ITALIC;
        }
        return new Font(combFontName.getSelectedItem().toString(),
                b | i, ((Integer) spinnerFontSize.getModel().getValue()));
    }
    
    private void updateFont() {
        Font oldF = this.getFont();
        
        this.firePropertyChange(propName, oldF, getSelectedFont());
    }

    public String getPropName() {
        return propName;
    }

    /**
     *
     * @param propName
     */
    public void setPropName(String propName) {
        this.propName = propName;
    }
    
    public void reset() {
        btnColor.setColor(Color.BLACK);
        String font = this.getFont().getFontName();
        String fontName = font;
        if (font.contains(".")) {
            fontName = font.substring(0, font.indexOf("."));
        }

        combFontName.setSelectedItem(fontName);
        spinnerFontSize.setValue(14);
        tglBold.setSelected(false);
        tglItalic.setSelected(false);
    }

    public void setSelectedFont(Font font) {
        if (font != null) {
            tglBold.setSelected(font.isBold());
            tglItalic.setSelected(font.isItalic());
            spinnerFontSize.setValue(font.getSize());
            String fontName = font.getFontName().substring(0, font.getFontName().indexOf("."));
            combFontName.setSelectedItem(fontName);
        }
    }
    
    public void addPropertyChangeListenerColorChooser(PropertyChangeListener pcl){
        btnColor.addPropertyChangeListener(pcl);
    }

    public Color getFontColor() {
        return btnColor.getColor();
    }

    public void setFontColor(Color fontColor) {
        btnColor.setColor(fontColor);
    }
}
