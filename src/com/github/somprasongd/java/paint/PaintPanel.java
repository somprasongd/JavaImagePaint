/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint;

import com.github.somprasongd.java.paint.objects.AnnotationLineObject;
import com.github.somprasongd.java.paint.objects.AnnotationNoteObject;
import com.github.somprasongd.java.paint.objects.AnnotationObject;
import com.github.somprasongd.java.paint.objects.AnnotationOvalObject;
import com.github.somprasongd.java.paint.objects.AnnotationQuadArrowObject;
import com.github.somprasongd.java.paint.objects.AnnotationRectObject;
import com.github.somprasongd.java.paint.objects.AnnotationTextObject;
import com.github.somprasongd.java.paint.utils.BufferedImageTool;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author sompr
 */
public class PaintPanel extends javax.swing.JPanel {

    public static final int MODE_FREE_HAND = -1;
    public static final int MODE_LINE = 0;
    public static final int MODE_SCRIBBLE = 1;
    public static final int MODE_RECT = 2;
    public static final int MODE_OVAL = 3;
    public static final int MODE_TEXT = 4;
    public static final int MODE_SELECT = 5;
    public static final int MODE_ERASE = 6;
    public static final int MODE_FLOOD = 7;
    public static final int MODE_POINT = 8;
    public static final int MODE_QUADARROW = 9;
    public static final int MODE_COLORPICK = 10;
    public static final int MODE_NOTE = 11;
    public static final int MODE_NONE = 100;
    private List selectedObjects;
    private int currentMode;
    private JFileChooser saveChooser;
    private File currentFile;
    private BufferedImage img;
    private double zoom;
    private double currentAngle;
    private ArrayList paintObjects;
    private ArrayList tempObjects;
    private int img_width;
    private int img_height;
    private boolean filled;
    private Color textColor;
    private Color drawColor;
    private Stroke stroke;

    private PropertyChangeListener pclColor = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setDrawColor((Color) evt.getNewValue());
        }
    };

    private PropertyChangeListener pclFontColor = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setDrawColor((Color) evt.getNewValue());
        }
    };

    private PropertyChangeListener pclStroke = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setStroke((Stroke) evt.getNewValue());
        }
    };

    private PropertyChangeListener pclFont = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setTextFont((Font) evt.getNewValue());
        }
    };
    private float alpha;

    /**
     * Creates new form PaintPanel
     */
    public PaintPanel() {
        initComponents();
        setCurrentMode(PaintPanel.MODE_NONE);
        btnColor.addPropertyChangeListener(pclColor);
        strokeChooserPanel.addPropertyChangeListener(pclStroke);
        fontChooserPanel.addPropertyChangeListener(pclColor);
        fontChooserPanel.addPropertyChangeListenerColorChooser(pclFont);
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
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnSave = new javax.swing.JButton();
        panelDraw = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        bottomToolbar = new javax.swing.JToolBar();
        btnZoomReset = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        btnZoomIn = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        sliderAlpha = new javax.swing.JSlider();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
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
        topToolbar.add(tglBtnFillColor);

        String iconChooseColor = getClass().getResource("/com/github/somprasongd/java/paint/icons/selectColor.png").toString();
        btnColor.setText("<html><body><img src=\"" + iconChooseColor + "\"></body></html>");
        btnColor.setFocusable(false);
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

        fontChooserPanel.setMaximumSize(new java.awt.Dimension(350, 100));
        topToolbar.add(fontChooserPanel);
        topToolbar.add(jSeparator3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/save.png"))); // NOI18N
        btnSave.setToolTipText(bundle.getString("SAVE")); // NOI18N
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        topToolbar.add(btnSave);

        add(topToolbar, java.awt.BorderLayout.NORTH);

        panelDraw.setLayout(new java.awt.BorderLayout());

        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelDraw.add(scrollPane, java.awt.BorderLayout.CENTER);

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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/github/somprasongd/java/paint/icons/bin.png"))); // NOI18N
        jButton1.setToolTipText(bundle.getString("DELETE")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        bottomToolbar.add(jButton1);

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
        setCurrentMode(PaintPanel.MODE_SELECT);
    }//GEN-LAST:event_tglBtnSelectActionPerformed

    private void tglBtnDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawActionPerformed
        setCurrentMode(PaintPanel.MODE_FREE_HAND);
    }//GEN-LAST:event_tglBtnDrawActionPerformed

    private void tglBtnDrawLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawLineActionPerformed
        setCurrentMode(PaintPanel.MODE_LINE);
    }//GEN-LAST:event_tglBtnDrawLineActionPerformed

    private void tglBtnDrawRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawRectangleActionPerformed
        setCurrentMode(PaintPanel.MODE_RECT);
    }//GEN-LAST:event_tglBtnDrawRectangleActionPerformed

    private void tglBtnDrawOvalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawOvalActionPerformed
        setCurrentMode(PaintPanel.MODE_OVAL);
    }//GEN-LAST:event_tglBtnDrawOvalActionPerformed

    private void tglBtnDrawArrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawArrowActionPerformed
        setCurrentMode(PaintPanel.MODE_QUADARROW);
    }//GEN-LAST:event_tglBtnDrawArrowActionPerformed

    private void tglBtnDrawTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawTextActionPerformed
        setCurrentMode(PaintPanel.MODE_TEXT);
    }//GEN-LAST:event_tglBtnDrawTextActionPerformed

    private void tglBtnDrawNoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tglBtnDrawNoteActionPerformed
        setCurrentMode(PaintPanel.MODE_NOTE);
    }//GEN-LAST:event_tglBtnDrawNoteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomInActionPerformed
        this.setZoom(this.getZoom() + 0.25);
        this.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomInActionPerformed

    private void btnZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomOutActionPerformed
        this.setZoom(this.getZoom() - 0.25);
        this.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomOutActionPerformed

    private void btnZoomResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomResetActionPerformed
        this.setZoom(1);
        this.zoomed(new Point(0, 0));
    }//GEN-LAST:event_btnZoomResetActionPerformed

    private void sliderAlphaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderAlphaStateChanged
        float newVal = (float) (sliderAlpha.getValue()) / 255.0f;
        setAlpha(newVal);
    }//GEN-LAST:event_sliderAlphaStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        deleteSelected();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar bottomToolbar;
    private com.github.somprasongd.java.paint.components.ColorButton btnColor;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JButton btnZoomReset;
    private javax.swing.ButtonGroup buttonGroupAction;
    private com.github.somprasongd.java.paint.components.FontChooserPanel fontChooserPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private com.github.somprasongd.java.paint.components.LocationPanel locationPanel;
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
     * Update this panel
     */
    private void updatePanel() {
        this.revalidate();
        scrollPane.repaint();
    }

    public void setCurrentMode(int currentMode) {
        // set current mode
        this.currentMode = currentMode;
        // clear selected drawed objects
        selectedObjects = new ArrayList();
        switch (currentMode) {
            case PaintPanel.MODE_LINE:
            case PaintPanel.MODE_OVAL:
            case PaintPanel.MODE_RECT:
                this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                break;
            case PaintPanel.MODE_TEXT:
            case PaintPanel.MODE_NOTE:
                this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                break;
            case PaintPanel.MODE_SELECT:
            case PaintPanel.MODE_QUADARROW:
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
            default:
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                break;
        }
        this.updatePanel();
    }

    /**
     * load image to show on the screen that call by JImageThumbnailPanel
     *
     * @param url_anno url_anno object is a url_anno of an image
     */
    public void loadNewImage(URL url) {
        if (url != null) {
            this.currentFile = new File(url.getFile());
            try {
                img = ImageIO.read(url);
                if (img != null) {
                    img = BufferedImageTool.copy(img);
                    MediaTracker mt = new MediaTracker(this);
                    mt.addImage(img, 0);
                    try {
                        mt.waitForID(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.zoom = 1;
                    this.currentAngle = 0.0;
                    this.setPreferredSize(new Dimension((int) (img.getWidth() * zoom), (int) (img.getHeight() * zoom)));
                    paintObjects = new ArrayList();
                    tempObjects = new ArrayList();
                    selectedObjects = new ArrayList();
                    img_width = img.getWidth();
                    img_height = img.getHeight();
                }
            } catch (Exception e) {
            }
        }
        this.setCurrentMode(PaintPanel.MODE_NONE);
        this.updatePanel();
    }

    // Zoom action
    /**
     * Set number of zoom at a time
     *
     * @param zoom number of zoom that zoomed at a time
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
        if (this.zoom < 0.25) {
            this.zoom = 0.25;
        }
        if (this.zoom > 4.75) {
            this.zoom = 4.75;
        }
    }

    /**
     * Get zoom
     *
     * @return
     */
    public double getZoom() {
        return zoom;
    }

    /**
     *
     * @param center center point of zoomed
     */
    public void zoomed(Point center) {
        Dimension newSize = new Dimension((int) (img.getWidth() * zoom), (int) (img.getHeight() * zoom));
        if (scrollPane != null) {
            Dimension currentSize = this.getPreferredSize();
            double ratio = (double) newSize.getHeight() / (double) currentSize.getHeight();

            Point currentLoc = scrollPane.getViewport().getViewPosition();

            Dimension viewSize = scrollPane.getViewport().getExtentSize();
            int xOff = center.x - currentLoc.x;
            int yOff = center.y - currentLoc.y;

            Point newCent = new Point((int) (center.x * ratio), (int) (center.y * ratio));

            int newXLoc = newCent.x - xOff;
            int newYLoc = newCent.y - yOff;
            if (newXLoc > newSize.getWidth() - viewSize.getWidth()) {
                newXLoc = (int) newSize.getWidth() - (int) viewSize.getWidth();
            }
            if (newXLoc < 0) {
                newXLoc = 0;
            }
            if (newYLoc > newSize.getHeight() - viewSize.getHeight()) {
                newYLoc = (int) newSize.getHeight() - (int) viewSize.getHeight();
            }
            if (newYLoc < 0) {
                newYLoc = 0;
            }
            Point newLoc = new Point(newXLoc, newYLoc);

            this.setPreferredSize(newSize);
            this.revalidate();
            scrollPane.getViewport().setViewPosition(newLoc);

            if (locationPanel != null) {
                int x = (int) (center.getX() / zoom);
                int y = (int) (center.getY() / zoom);

                if (x >= 0 && x < img.getWidth() && y >= 0 && y < img.getHeight()) {
                    locationPanel.updateLocation(new Point(x, y));
                } else {
                    locationPanel.updateLocation(null);
                }
                locationPanel.repaint();
            }
            this.updatePanel();
        } else {
            this.setPreferredSize(newSize);
            this.revalidate();
            this.updatePanel();
        }
    }

    /**
     * Check the button fill was selected or not
     *
     * @return true - if it was selected
     * <br>
     * false - in otherwise
     *
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * Set Filled
     *
     * @param filled fill is true when this button selected and false in
     * otherwise
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
//        properties.setProperty("filled", "" + filled);
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationRectObject) {
                    ((AnnotationRectObject) paintObject).setFilled(filled);
                    continue;
                }
                if (paintObject instanceof AnnotationOvalObject) {
                    ((AnnotationOvalObject) paintObject).setFilled(filled);
                    continue;
                }
                if (paintObject instanceof AnnotationQuadArrowObject) {
                    ((AnnotationQuadArrowObject) paintObject).setFilled(filled);
                }
            }
            this.updatePanel();
        }
    }

    /**
     * Get color of the rectangle, oval, line or arrow objects
     *
     * @return
     *
     */
    public Color getDrawColor() {
        return drawColor;
    }

    /**
     * Set color of the rectangle, oval, line or arrow objects
     *
     * @param drawColor
     */
    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
//        properties.setProperty("drawcolor", "" + drawColor.getRGB());
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            if (paintObject instanceof AnnotationTextObject
                    || paintObject instanceof AnnotationNoteObject) {
                continue;
            }
            paintObject.setColor(drawColor);
        }
        this.updatePanel();
    }

    /**
     * Get text's color
     *
     * @return
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Set text's color
     *
     * @param textColor
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
//        properties.setProperty("textcolor", "" + textColor.getRGB());
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            if (paintObject instanceof AnnotationTextObject) {
                paintObject.setColor(textColor);
            }
        }
        this.updatePanel();
    }

    /**
     * Get stroke
     *
     * @return
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * set stroke
     *
     * @param stroke
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
//        properties.setProperty("stroke", "" + ((BasicStroke) stroke).getLineWidth());
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationRectObject) {
                    ((AnnotationRectObject) paintObject).setStroke(stroke);
                    continue;
                }
                if (paintObject instanceof AnnotationOvalObject) {
                    ((AnnotationOvalObject) paintObject).setStroke(stroke);
                    continue;
                }
                if (paintObject instanceof AnnotationLineObject) {
                    ((AnnotationLineObject) paintObject).setStroke(stroke);
                    continue;
                }
                if (paintObject instanceof AnnotationQuadArrowObject) {
                    ((AnnotationQuadArrowObject) paintObject).setStroke(stroke);
                }
            }
            this.updatePanel();
        }
    }

    public void setTextFont(Font font) {
//        if (properties != null) {
//            properties.setProperty("fontname", font.getName());
//            properties.setProperty("fonttype", "" + font.getStyle());
//            properties.setProperty("fontsize", "" + font.getSize());
//        }

        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                if (paintObject instanceof AnnotationTextObject) {
                    ((AnnotationTextObject) paintObject).setFont(font);
                }
            }
            this.updatePanel();
        }
    }

    /**
     * Get an alphas
     *
     * @return alpha
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Set an alpha
     *
     * @param alpha
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
//        properties.setProperty("alpha", "" + alpha);
        if (selectedObjects != null) {
            for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
                AnnotationObject paintObject = (AnnotationObject) iterator.next();
                paintObject.setAlpha(this.alpha);
            }
            this.updatePanel();
        }
    }

    public void deleteSelected() {
        for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
            AnnotationObject paintObject = (AnnotationObject) iterator.next();
            paintObjects.remove(paintObject);
            selectedObjects.remove(paintObject);
        }
        this.updatePanel();
    }
    /**
     * Save an iamge and all annotion together
     *
     * @return
     */
//    public boolean saveImageFile() {
//        if (saveChooser == null) {
//            saveChooser = new JFileChooser();
//            ArrayList filters = ImageIOFileFilter.getImageWriterFilters();
//            ImageIOFileFilter png = null;
//            for (int i = 0; i < filters.size(); i++) {
//                ImageIOFileFilter filter = (ImageIOFileFilter) filters.get(i);
//                if (filter.getPreferredExtString().equalsIgnoreCase("png")) {
//                    png = filter;
//                }
//                saveChooser.addChoosableFileFilter(filter);
//                saveChooser.setAcceptAllFileFilterUsed(false);
//            }
//            if (png != null) {
//                saveChooser.setFileFilter(png);
//            }
//        }
//        /* //set dir
//        File loadFile = currentFile;
//        if (loadFile != null && loadFile.isFile()) {
//        loadFile = new File(loadFile.getParent());
//        }
//        saveChooser.setCurrentDirectory(loadFile);*/
//
//        int ret = saveChooser.showSaveDialog(this);
//        if (ret == JFileChooser.APPROVE_OPTION) {
//            File file = saveChooser.getSelectedFile();
//            if (file != null && file.getName().length() > 0) {
//                ImageIOFileFilter filter = (ImageIOFileFilter) saveChooser.getFileFilter();
//                ImageWriter writer = filter.getImageWriter();
//                if (!file.getPath().endsWith("." + filter.getPreferredExtString())) {
//                    file = new File(file.getPath() + "." + filter.getPreferredExtString());
//                }
//                this.currentFile = file;
//                for (Iterator iterator = selectedObjects.iterator(); iterator.hasNext();) {
//                    AnnotationObject paintObject = (AnnotationObject) iterator.next();
//                    paintObject.setSelected(false);
//                }
//                selectedObjects.clear();
//                BufferedImage save = new BufferedImage(img.getWidth(), img.getHeight(), filter.getBiType());
//                Graphics2D g = (Graphics2D) save.getGraphics();
//                if (!filter.isSupportsAlpha()) {
//                    g.setColor(this.bgColor);
//                    g.fillRect(0, 0, img.getWidth(), img.getHeight());
//                }
//                g.drawImage(img, 0, 0, null);
//                g.drawImage(this.getObjectsImage(paintObjects), 0, 0, null);
//                if (file.exists()) {
//                    int retOK = JOptionPane.showConfirmDialog(this, "File " + file.getName() + " exists. Do you want to overwrite?");
//                    if (retOK == JOptionPane.OK_OPTION) {
//                        try {
//                            ImageOutputStream stream = new FileImageOutputStream(file);
//                            writer.setOutput(stream);
//                            writer.write(save);
//                            stream.close();
//
//                            this.updatePanel();
//                            return true;
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            this.updatePanel();
//                            return false;
//                        }
//                    } else {
//                        this.updatePanel();
//                        return false;
//                    }
//                } else {
//                    try {
//                        ImageOutputStream stream = new FileImageOutputStream(file);
//                        writer.setOutput(stream);
//                        writer.write(save);
//                        stream.close();
//                        this.updatePanel();
//                        return true;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        this.updatePanel();
//                        return false;
//                    }
//                }
//            }
//        } else {
//            return false;
//        }
//        return false;
//    }
}
