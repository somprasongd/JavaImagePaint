/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint;

import com.github.somprasongd.java.paint.components.PaintPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author sompr
 */
public class PaintApp extends javax.swing.JPanel {

    public static final int MODE_VIEW = 0;
    public static final int MODE_PAINT = 1;

    private PropertyChangeListener pclColor = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("fgColor".equals(evt.getPropertyName())) {
                paintPanel.setDrawColor((Color) evt.getNewValue());
            }
        }
    };

    private PropertyChangeListener pclFontColor = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("fontColor".equals(evt.getPropertyName())) {
                paintPanel.setTextColor((Color) evt.getNewValue());
            }
        }
    };

    private PropertyChangeListener pclStroke = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("stroke".equals(evt.getPropertyName())) {
                paintPanel.setStroke((Stroke) evt.getNewValue());
            }
        }
    };

    private PropertyChangeListener pclFont = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("font".equals(evt.getPropertyName())) {
                paintPanel.setTextFont((Font) evt.getNewValue());
            }
        }
    };

    /**
     * Creates new form PaintPanel
     */
    public PaintApp() {
        initComponents();
        tglBtnDraw.setVisible(false);
        paintPanel.setScroll(scrollPane);
        paintPanel.setLocationPanel(locationPanel);
        paintPanel.setCurrentMode(PaintPanel.MODE_NONE);
        btnColor.addPropertyChangeListener(pclColor);
        strokeChooserPanel.addPropertyChangeListener(pclStroke);
        fontChooserPanel.addPropertyChangeListener(pclFont);
        fontChooserPanel.addPropertyChangeListenerColorChooser(pclFontColor);
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

        buttonGroupAction = new javax.swing.ButtonGroup();
        topToolbar = new javax.swing.JToolBar();
        tglBtnSelect = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        tglBtnDraw = new javax.swing.JToggleButton();
        tglBtnDrawLine = new javax.swing.JToggleButton();
        tglBtnDrawRectangle = new javax.swing.JToggleButton();
        tglBtnDrawOval = new javax.swing.JToggleButton();
        tglBtnDrawArrow = new javax.swing.JToggleButton();
        tglBtnFillColor = new javax.swing.JToggleButton();
        btnColor = new com.github.somprasongd.java.paint.components.ColorButton();
        strokeChooserPanel = new com.github.somprasongd.java.paint.components.StrokeChooserPanel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        tglBtnDrawNote = new javax.swing.JToggleButton();
        tglBtnDrawText = new javax.swing.JToggleButton();
        fontChooserPanel = new com.github.somprasongd.java.paint.components.FontChooserPanel();
        panelDraw = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        paintPanel = new com.github.somprasongd.java.paint.components.PaintPanel();
        jPanel1 = new javax.swing.JPanel();
        bottomToolbar = new javax.swing.JToolBar();
        btnZoomReset = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        btnZoomIn = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        sliderAlpha = new javax.swing.JSlider();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        btnDelete = new javax.swing.JButton();
        locationPanel = new com.github.somprasongd.java.paint.components.LocationPanel();

        setLayout(new java.awt.BorderLayout());

        topToolbar.setFloatable(false);
        topToolbar.setRollover(true);

        buttonGroupAction.add(tglBtnSelect);
        tglBtnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_cursor.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/github/somprasongd/java/paint/Bundle"); // NOI18N
        tglBtnSelect.setToolTipText(bundle.getString("SELECT")); // NOI18N
        tglBtnSelect.setFocusable(false);
        tglBtnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnSelectActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnSelect);
        topToolbar.add(jSeparator1);

        buttonGroupAction.add(tglBtnDraw);
        tglBtnDraw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_free_hand.png"))); // NOI18N
        tglBtnDraw.setToolTipText(bundle.getString("DRAW")); // NOI18N
        tglBtnDraw.setFocusable(false);
        tglBtnDraw.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDraw.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDraw);

        buttonGroupAction.add(tglBtnDrawLine);
        tglBtnDrawLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_line.png"))); // NOI18N
        tglBtnDrawLine.setToolTipText(bundle.getString("DRAW_LINE")); // NOI18N
        tglBtnDrawLine.setFocusable(false);
        tglBtnDrawLine.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawLine.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawLineActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawLine);

        buttonGroupAction.add(tglBtnDrawRectangle);
        tglBtnDrawRectangle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_square.png"))); // NOI18N
        tglBtnDrawRectangle.setToolTipText(bundle.getString("DRAW_RECTANGLE")); // NOI18N
        tglBtnDrawRectangle.setFocusable(false);
        tglBtnDrawRectangle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawRectangle.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawRectangleActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawRectangle);

        buttonGroupAction.add(tglBtnDrawOval);
        tglBtnDrawOval.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_circle.png"))); // NOI18N
        tglBtnDrawOval.setToolTipText(bundle.getString("DRAW_OVAL")); // NOI18N
        tglBtnDrawOval.setFocusable(false);
        tglBtnDrawOval.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawOval.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawOval.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawOvalActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawOval);

        buttonGroupAction.add(tglBtnDrawArrow);
        tglBtnDrawArrow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_right.png"))); // NOI18N
        tglBtnDrawArrow.setToolTipText(bundle.getString("DRAW_ARROW")); // NOI18N
        tglBtnDrawArrow.setFocusable(false);
        tglBtnDrawArrow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawArrow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawArrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawArrowActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawArrow);

        tglBtnFillColor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/fillColor.png"))); // NOI18N
        tglBtnFillColor.setToolTipText(bundle.getString("FILL_COLOR")); // NOI18N
        tglBtnFillColor.setFocusable(false);
        tglBtnFillColor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnFillColor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnFillColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnFillColorActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnFillColor);

        String iconChooseColor = getClass().getResource("/com/github/somprasongd/java/paint/icons/selectColor.png").toString();
        btnColor.setText("<html><body><img src=\"" + iconChooseColor + "\"></body></html>");
        btnColor.setFocusable(false);
        btnColor.setMaximumSize(new java.awt.Dimension(45, 25));
        btnColor.setMinimumSize(new java.awt.Dimension(40, 25));
        btnColor.setPreferredSize(new java.awt.Dimension(40, 25));
        btnColor.setPropName("fgColor");
        topToolbar.add(btnColor);

        strokeChooserPanel.setMaximumSize(new java.awt.Dimension(50, 50));
        strokeChooserPanel.setPropName("stroke");
        topToolbar.add(strokeChooserPanel);
        topToolbar.add(jSeparator2);

        buttonGroupAction.add(tglBtnDrawNote);
        tglBtnDrawNote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_note.png"))); // NOI18N
        tglBtnDrawNote.setToolTipText(bundle.getString("DRAW_NOTE")); // NOI18N
        tglBtnDrawNote.setFocusable(false);
        tglBtnDrawNote.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawNote.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawNote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawNoteActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawNote);

        buttonGroupAction.add(tglBtnDrawText);
        tglBtnDrawText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/draw_text.png"))); // NOI18N
        tglBtnDrawText.setToolTipText(bundle.getString("DRAW_TEXT")); // NOI18N
        tglBtnDrawText.setFocusable(false);
        tglBtnDrawText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tglBtnDrawText.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tglBtnDrawText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tglBtnDrawTextActionPerformed(evt);
            }
        });
        topToolbar.add(tglBtnDrawText);

        fontChooserPanel.setMaximumSize(new java.awt.Dimension(400, 100));
        fontChooserPanel.setPropName("font");
        topToolbar.add(fontChooserPanel);

        add(topToolbar, java.awt.BorderLayout.NORTH);

        panelDraw.setLayout(new java.awt.GridBagLayout());

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout paintPanelLayout = new javax.swing.GroupLayout(paintPanel);
        paintPanel.setLayout(paintPanelLayout);
        paintPanelLayout.setHorizontalGroup(
            paintPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 867, Short.MAX_VALUE)
        );
        paintPanelLayout.setVerticalGroup(
            paintPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(paintPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panelDraw.add(scrollPane, gridBagConstraints);

        add(panelDraw, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        bottomToolbar.setFloatable(false);
        bottomToolbar.setRollover(true);

        btnZoomReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/1-1.png"))); // NOI18N
        btnZoomReset.setToolTipText(bundle.getString("ONE_TO_ONE")); // NOI18N
        btnZoomReset.setFocusable(false);
        btnZoomReset.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomReset.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomResetActionPerformed(evt);
            }
        });
        bottomToolbar.add(btnZoomReset);

        btnZoomOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/zoomOut.png"))); // NOI18N
        btnZoomOut.setToolTipText(bundle.getString("ZOOM_OUT")); // NOI18N
        btnZoomOut.setFocusable(false);
        btnZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomOutActionPerformed(evt);
            }
        });
        bottomToolbar.add(btnZoomOut);

        btnZoomIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/zoomIn.png"))); // NOI18N
        btnZoomIn.setToolTipText(bundle.getString("ZOOM_IN")); // NOI18N
        btnZoomIn.setFocusable(false);
        btnZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomInActionPerformed(evt);
            }
        });
        bottomToolbar.add(btnZoomIn);
        bottomToolbar.add(jSeparator4);

        sliderAlpha.setMaximum(255);
        sliderAlpha.setToolTipText(bundle.getString("ALPHA")); // NOI18N
        sliderAlpha.setValue(255);
        sliderAlpha.setMaximumSize(new java.awt.Dimension(150, 26));
        sliderAlpha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderAlphaStateChanged(evt);
            }
        });
        bottomToolbar.add(sliderAlpha);
        bottomToolbar.add(jSeparator5);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/bin.png"))); // NOI18N
        btnDelete.setToolTipText(bundle.getString("DELETE")); // NOI18N
        btnDelete.setFocusable(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        bottomToolbar.add(btnDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(bottomToolbar, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(locationPanel, gridBagConstraints);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void tglBtnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnSelectActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_SELECT);
    }//GEN-LAST:event_tglBtnSelectActionPerformed

    private void tglBtnDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_FREE_HAND);
    }//GEN-LAST:event_tglBtnDrawActionPerformed

    private void tglBtnDrawLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawLineActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_LINE);
    }//GEN-LAST:event_tglBtnDrawLineActionPerformed

    private void tglBtnDrawRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawRectangleActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_RECT);
    }//GEN-LAST:event_tglBtnDrawRectangleActionPerformed

    private void tglBtnDrawOvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawOvalActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_OVAL);
    }//GEN-LAST:event_tglBtnDrawOvalActionPerformed

    private void tglBtnDrawArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawArrowActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_QUADARROW);
    }//GEN-LAST:event_tglBtnDrawArrowActionPerformed

    private void tglBtnDrawTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawTextActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_TEXT);
    }//GEN-LAST:event_tglBtnDrawTextActionPerformed

    private void tglBtnDrawNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawNoteActionPerformed
        paintPanel.setCurrentMode(PaintPanel.MODE_NOTE);
    }//GEN-LAST:event_tglBtnDrawNoteActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        paintPanel.setZoom(paintPanel.getZoom() + 0.25);
        paintPanel.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        paintPanel.setZoom(paintPanel.getZoom() - 0.25);
        paintPanel.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomResetActionPerformed
        paintPanel.setZoom(1);
        paintPanel.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomResetActionPerformed

    private void sliderAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderAlphaStateChanged
        float newVal = (float) (sliderAlpha.getValue()) / 255.0f;
        paintPanel.setAlpha(newVal);
    }//GEN-LAST:event_sliderAlphaStateChanged

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        paintPanel.deleteSelected();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tglBtnFillColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnFillColorActionPerformed
        paintPanel.setFilled(tglBtnFillColor.isSelected());
    }//GEN-LAST:event_tglBtnFillColorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar bottomToolbar;
    private com.github.somprasongd.java.paint.components.ColorButton btnColor;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JButton btnZoomReset;
    private javax.swing.ButtonGroup buttonGroupAction;
    private com.github.somprasongd.java.paint.components.FontChooserPanel fontChooserPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private com.github.somprasongd.java.paint.components.LocationPanel locationPanel;
    private com.github.somprasongd.java.paint.components.PaintPanel paintPanel;
    private javax.swing.JPanel panelDraw;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JSlider sliderAlpha;
    private com.github.somprasongd.java.paint.components.StrokeChooserPanel strokeChooserPanel;
    private javax.swing.JToggleButton tglBtnDraw;
    private javax.swing.JToggleButton tglBtnDrawArrow;
    private javax.swing.JToggleButton tglBtnDrawLine;
    private javax.swing.JToggleButton tglBtnDrawNote;
    private javax.swing.JToggleButton tglBtnDrawOval;
    private javax.swing.JToggleButton tglBtnDrawRectangle;
    private javax.swing.JToggleButton tglBtnDrawText;
    private javax.swing.JToggleButton tglBtnFillColor;
    private javax.swing.JToggleButton tglBtnSelect;
    private javax.swing.JToolBar topToolbar;
    // End of variables declaration//GEN-END:variables

    /**
     * 
     * @param url 
     */
    public void setImage(URL url) {
        paintPanel.setImage(url);
    }

    /**
     * 
     * @param file 
     */
    public void setImage(File file) {
        paintPanel.setImage(file);
    }
    
    /**
     * 
     * @param imageIcon 
     */
    public void setImage(ImageIcon imageIcon) {
        paintPanel.setImage(imageIcon);
    }
    
    /**
     * 
     * @param image 
     */
    public void setImage(Image image) {
        paintPanel.setImage(image);
    }

    /**
     * 
     * @param img 
     */
    public void setImage(BufferedImage img) {
        paintPanel.setImage(img);
    }

    public File getImage(File file) {
        return paintPanel.getPaintedImage(file);
    }

    public BufferedImage getImage() {
        return paintPanel.getPaintedImage();
    }

    /**
     * 
     * @param mode 
     */
    public void setMode(int mode) {
        topToolbar.setVisible(mode == MODE_PAINT);
        jSeparator4.setVisible(mode == MODE_PAINT);
        sliderAlpha.setVisible(mode == MODE_PAINT);
        jSeparator5.setVisible(mode == MODE_PAINT);
        btnDelete.setVisible(mode == MODE_PAINT);
        if (mode == MODE_VIEW) {
            paintPanel.setCurrentMode(PaintPanel.MODE_VIEW_ONLY);
        }
    }
}
