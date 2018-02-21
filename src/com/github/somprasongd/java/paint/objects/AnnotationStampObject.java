package com.github.somprasongd.java.paint.objects;

import annotation.AnnotationPaintPanel;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 *
 * @author  Somprasong Damyos
 */

public class AnnotationStampObject extends AnnotationObject {
    private Font font;
    private String text;
    private TextLayout layout;
    private AnnotationPaintPanel panel;
    private Point2D locationDrag;
    private Color textColor;
    private Color bgColor;
    private Color bdColor;
    private boolean bgTransparent;
    private boolean hasBorder;
    private boolean hasBG;
    private Rectangle2D bounds;
    private Stroke strokes;
    private Graphics2D graphics2D;
    private AnnotationPaintPanel paintPanel;
    private double strokeWidth = 1.0;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    private Vector edit_user_vector = new Vector();
    private Vector del_user_vector = new Vector();
private Vector view_user_vector = new Vector();
    public AnnotationStampObject(){
       
    }
    
    public AnnotationStampObject(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user, String owner_user, 
            Font font, Color textColor, Color bgColor, Color brColor, Point2D location, String text, boolean antialiased, Stroke strokes,boolean bgTransparent, boolean hasBorder, boolean hasBG) {
       super(textColor, location, antialiased);
       this.paintPanel = paintPanel;
       this.font = font;
       this.text = text;
       this.textColor = textColor;
       this.bgColor = bgColor;
       this.bdColor = brColor;
       this.strokes = strokes;
       this.bgTransparent = bgTransparent;
       this.hasBorder =hasBorder;
       this.hasBG = hasBG;
       this.view_user = view_user;
       this.edit_user = edit_user;
       this.del_user = del_user;
       this.owner_user = owner_user;
       if (this.view_user.equals("alluser>")) {
            view_user_vector.add(this.view_user);
        } else {
            setView_user(this.view_user);
        }
       if (this.edit_user.equals("alluser>")) {
            edit_user_vector.add(this.edit_user);
        } else {
            setEdit_user(this.edit_user);
        }
        if (this.del_user.equals("alluser>")) {
            del_user_vector.add(this.del_user);
        } else {
            setDel_user(this.del_user);
        }
    }
    
    public void addGraphics(Graphics2D g) {
       g.setColor(textColor);
       g.setFont(font);
       FontRenderContext frc = g.getFontRenderContext();
       layout = new TextLayout(text, font, frc);
       
       float xLoc = (float) (this.getLocation().getX() + this.getTranslation()[0]);
       float yLoc = (float) (this.getLocation().getY() + this.getTranslation()[1]);
       
       if(hasBG){
           if(bgTransparent)
               g.setPaint(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 70));
           else
               g.setPaint(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 202));
               g.fill(new Rectangle((int)xLoc - 2, 
                       (int)yLoc - (int)layout.getAscent() - 2,
                       (int)layout.getAdvance() + 4, 
                       (int)layout.getAscent() + 
                       (int)layout.getDescent() + 4));
           }
       
       g.setColor(this.getColor());
       layout.draw(g, xLoc, yLoc);
       
       if(hasBorder){
           g.setStroke(strokes);
           g.setColor(bdColor);
           bounds = layout.getBounds();
           bounds.setRect(xLoc - 2, yLoc - layout.getAscent() - 2,layout.getAdvance() + 4, layout.getAscent() + layout.getDescent() + 4);   
           g.draw(bounds);
       }
       
       if (this.isSelected()) {
           if(!hasBorder){
               g.setStroke(new BasicStroke(2));
               g.setColor(bdColor);
               bounds = layout.getBounds();
               bounds.setRect(xLoc - 2, yLoc - layout.getAscent() - 2,layout.getAdvance() + 4, layout.getAscent() + layout.getDescent() + 4);   
               g.draw(bounds);
           }
       }
       graphics2D = g;
   }
   
   public Graphics2D getGraphics2D(){
       return graphics2D;
   }
   public void setLocationDrag(Point2D point) {
       double xMove = point.getX() - this.getStartDragLoc().getX();
       double yMove = point.getY() - this.getStartDragLoc().getY();
       this.setLocation(new Point2D.Double(locationDrag.getX() + xMove, locationDrag.getY() + yMove));
   }
   
   public Point2D[] getHighlightPoints() {
       Point2D[] points = new Point2D[]{this.getLocation()};
       return points;
   }
   
   public void updateLocation(Point2D point) {
       
       if (this.getCurrentPointIndex() == 0) {
           this.setLocationDrag(point);
       }
       else {
       }
   }
   public double getStrokeWidth(){
        return this.strokeWidth;
   }
   public void setStrokeWidth(double width){
        this.strokeWidth = width;
   }
   public Font getFont() {
       return font;
   }
   
   public void setFont(Font font) {
       this.font = font;
   }
   
   public String getText() {
       return text;
   }
   public void setText(String text) {
       this.text = text;
   }
   public boolean finished(Point2D point) {
       return true;
   }

   public Stroke getStroke(){
       return strokes;
   }
   
   public double[] getTranslation() {
       return new double[]{0.0, 0.0};
   }

   public void setStartDragLocs() {
       locationDrag = new Point2D.Double(this.getLocation().getX(), this.getLocation().getY());
   }

   public void setPanel(AnnotationPaintPanel panel) {
       this.panel = panel;
   }

   public AnnotationObject createCopy(int xOffset, int yOffset) {
       Point2D first = this.getLocation();
       AnnotationStampObject copy = new AnnotationStampObject(this.paintPanel, this.view_user, this.edit_user, this.del_user, this.owner_user,
               this.getFont(), this.gettextColor(), this.getBGColor(), this.getBDColor(), new Point2D.Double(first.getX() + xOffset, 
               first.getY() + yOffset), this.getText(), this.isAntialiased(), this.getStroke(), this.isBackgroundTransparent(),
               this.hasBoder(), this.hasBackground());
       
       return copy; 
   }
   
   @Override
   public String getInfo() {
       Point2D first = this.getLocation();
       
       String filename = paintPanel.getCurrentImageFilename();
       
       String stroke = new StringBuilder().append(((BasicStroke)this.getStroke()).getLineWidth()).toString();
       
       String fonts =new StringBuilder().append(this.getFont().getName()).append("|").append(this.getFont().getStyle()).append("|").append(this.getFont().getSize()).toString();
        
       String point =  new StringBuilder().append(first.getX()).append("|").append(first.getY()).toString();
        
       return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).append("|").append(filename).append("|StampText|").
               append(fonts).append("|").append(textColor.getRGB()).append("|").append(point).append("|").
               append(this.getText()).append("|").append(this.isAntialiased()).append("|").append(this.getAlpha()).
               append("|").append(this.bgColor.getRGB()).append("|").append(this.bdColor.getRGB()).append("|").
               append(stroke).append("|").append(this.bgTransparent).append("|").append(this.hasBorder).append("|").
               append(this.hasBG).append("|").toString();
   }
   
   public AnnotationObject loadSText(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, Font font, Color textcolor, Color bgcolor, Color bdcolor, Point2D p2d, String text, 
            boolean antia, float alpha, Stroke strokes, boolean bg_Transparent, boolean hasBorder,boolean hasBG) {
       AnnotationStampObject copy = new AnnotationStampObject(paintPanel, view_user, edit_user, del_user, owner_user, font, textcolor, bgcolor, bdcolor, p2d, text, antia, strokes, bg_Transparent, hasBorder, hasBG);
       return copy;
    }
    
    public Color gettextColor(){
        return textColor;
    }
    public Color getBGColor(){
        return bgColor;
    }
    public Color getBDColor(){
        return bdColor;
    }
    
    public boolean hasBoder(){
        return hasBorder;
    }
    public boolean hasBackground(){
        return hasBG;
    }
    public boolean isBackgroundTransparent(){
        return bgTransparent;
    }
    

    @Override
    public Font getTextFont() {
        return font;
    }

    @Override
    public String getTextNote() {
        return text;
    }
    @Override
    public void updateViewUser(Vector user) {
        view_user_vector = user;
        if (user.get(0).toString().equals("alluser>")) {
            view_user = "alluser>";
        } else {
            String new_user = "";
            for (int j = 0; j < user.size(); j++) {
                new_user += user.get(j).toString();
            }
            view_user = new_user;
            System.out.println("view user : " + view_user);
        }
    }

     @Override
    public void updateEditUser(Vector user) {
         edit_user_vector = user;
        if (user.get(0).toString().equals("alluser>")) {
            edit_user = "alluser>";
        } else {
            String new_user = "";
            for (int j = 0; j < user.size(); j++) {
                new_user += user.get(j).toString();
            }
            edit_user = new_user;
            System.out.println("edit user : " + edit_user);
        }
    }

    @Override
    public void updateDeleteUser(Vector user) {
        del_user_vector = user;
        if (user.get(0).toString().equals("alluser>")) {
            del_user = "alluser>";
        } else {
            String new_user = "";
            for (int j = 0; j < user.size(); j++) {
                new_user += user.get(j).toString();
            }
            del_user = new_user;
            System.out.println("del user : " + del_user);
        }
    }

    @Override
    public String getOwnerUser() {
        return owner_user;
    }

        private void setDel_user(String del_user) {
        Vector user = new Vector();
        int end;
        int i = 0;
        int start = 0;
        while (start != del_user.length()) {
            end = del_user.indexOf('>', start);
            if (end >= 0) {
                user.add(del_user.substring(start, end + 1));
                start = end + 1;
                i++;
            }
        }
        del_user_vector = user;
    }

    @Override
    public Vector getDel_user() {
        return del_user_vector;
    }

    private void setEdit_user(String edit_user) {
        Vector user = new Vector();
        int end;
        int i = 0;
        int start = 0;
        while (start != edit_user.length()) {
            end = edit_user.indexOf('>', start);
            if (end >= 0) {
                user.add(edit_user.substring(start, end + 1));
                start = end + 1;
                i++;
            }
        }
        edit_user_vector = user;
    }

    @Override
    public Vector getEdit_user() {
        return edit_user_vector;
    }
    
    private void setView_user(String view_user) {
        Vector user = new Vector();
        int end;
        int i = 0;
        int start = 0;
        while (start != view_user.length()) {
            end = view_user.indexOf('>', start);
            if (end >= 0) {
                user.add(view_user.substring(start, end + 1));
                start = end + 1;
                i++;
            }
        }
        view_user_vector = user;
    }

    @Override
    public Vector getView_user() {
        return view_user_vector;
    }

}
