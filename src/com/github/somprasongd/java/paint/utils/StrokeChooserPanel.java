package com.github.somprasongd.java.paint.utils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Currently only provides a JSpinner to set a Basic Strokes width.
 *
 * <p>Copyright (c) 2004 Alistair Dickie. All Rights Reserved.
 * See alistairdickie.com for contact details
 * See licence.txt for licence infomation</p>
 */
public class StrokeChooserPanel extends JPanel implements ActionListener, ChangeListener {
   private JSpinner strokeWidth;
   private String propName;
   private Stroke stroke;
   private double strokeWidths = 1.0;


   public StrokeChooserPanel(Stroke stroke, String propName) {
      super();
      this.propName = propName;
      this.setStroke(stroke);
      this.add(getStrokeWidthSpinner());
      this.setMaximumSize(new Dimension(55,100));
      this.setToolTipText("ขนาดเส้นขอบ");
   }
   
   public StrokeChooserPanel(Stroke stroke, String propName, double start) {
      super();
      this.propName = propName;
      
      this.setStroke(stroke);
      this.strokeWidths = start;
      System.out.println("start"+start);
      this.add(getStrokeWidthSpinner());
      

   }

   public StrokeChooserPanel(String propName) {
      this(new BasicStroke(1.0f), propName);

   }

   public Stroke getStroke() {
      return stroke;
   }

   public void setStroke(Stroke stroke) {
      this.stroke = stroke;
   }

   public void setStrokeWidth(double width){
       this.strokeWidths = width;
       
   }
   
   public double getStrokeWidth(){
       return this.strokeWidths;
       
   }

   public JSpinner getStrokeWidthSpinner() {
      SpinnerNumberModel model = new SpinnerNumberModel(strokeWidths, 0.5, 100.0, 0.5);
      strokeWidth = new JSpinner(model);
      strokeWidth.addChangeListener(this);
      return strokeWidth;
   }


   public void actionPerformed(ActionEvent e) {
      Stroke oldS = this.getStroke();
      this.setStroke(new BasicStroke(((Double) strokeWidth.getModel().getValue()).floatValue()));
      this.setStrokeWidth(((Double) strokeWidth.getModel().getValue()).doubleValue());
      this.firePropertyChange(propName, oldS, this.getStroke());
   }

   public void stateChanged(ChangeEvent e) {
      Stroke oldS = this.getStroke();
      this.setStroke(new BasicStroke(((Double) strokeWidth.getModel().getValue()).floatValue()));
      this.setStrokeWidth(((Double) strokeWidth.getModel().getValue()).doubleValue());
      this.firePropertyChange(propName, oldS, this.getStroke());
   }

    


}
