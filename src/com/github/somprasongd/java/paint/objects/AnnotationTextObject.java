package com.github.somprasongd.java.paint.objects;

import annotation.AnnotationPaintPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/**
 * Draws some text.
 *
 * <p>Copyright (c) 2004 Alistair Dickie. All Rights Reserved.
 * See alistairdickie.com for contact details
 * See licence.txt for licence infomation</p>
 */
public class AnnotationTextObject extends AnnotationObject {

    private Font font;
    private String text;
    private TextLayout layout;
    private int caretPos;
    private AnnotationPaintPanel panel;
    private Point2D locationDrag;
    private AnnotationPaintPanel paintPanel;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    private Vector edit_user_vector = new Vector();
    private Vector del_user_vector = new Vector();
    private Vector view_user_vector = new Vector();

    public AnnotationTextObject() {

    }

    public AnnotationTextObject(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user, String owner_user,
            Font font, Color color, Point2D location, String text, boolean antialiased) {
        super(color, location, antialiased);
        this.paintPanel = paintPanel;
        this.font = font;
        this.text = text;
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
        caretPos = 0;
    }

    public void addGraphics(Graphics2D g) {
        g.setColor(this.getColor());
        g.setFont(font);
        FontRenderContext frc = g.getFontRenderContext();
        layout = new TextLayout(text, font, frc);
        float xLoc = (float) (this.getLocation().getX() + this.getTranslation()[0]);
        float yLoc = (float) (this.getLocation().getY() + this.getTranslation()[1]);
        layout.draw(g, xLoc, yLoc);

        if (this.isSelected()) {
            g.setColor(Color.magenta);
            Rectangle2D bounds = layout.getBounds();
            bounds.setRect(xLoc - 2, yLoc - layout.getAscent() - 2,
                    layout.getAdvance() + 4, layout.getAscent() + layout.getDescent() + 4);

            g.draw(bounds);

            g.translate((double) xLoc, (double) yLoc);
            g.setColor(Color.GREEN);
            Shape[] carets = layout.getCaretShapes(caretPos, bounds);
            g.draw(carets[0]);
            g.translate(-(double) xLoc, -(double) yLoc);
        }
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
        } else {

        }
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

    public double[] getTranslation() {
        return new double[]{0.0, 0.0};
    }

    public void setStartDragLocs() {
        locationDrag = new Point2D.Double(this.getLocation().getX(), this.getLocation().getY());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String prior = text.substring(0, caretPos);
        String after = text.substring(caretPos, text.length());
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (prior.length() > 0) {
                text = prior.substring(0, prior.length() - 1) + after;
                if (text.length() == 0) {
                    text = " ";
                }
                caretPos -= 1;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            if (after.length() > 0) {
                text = prior + after.substring(1, after.length());
                if (text.length() == 0) {
                    text = " ";
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            caretPos += 1;
            if (caretPos > text.length()) {
                caretPos = text.length();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            caretPos -= 1;
            if (caretPos < 0) {
                caretPos = 0;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            caretPos = 0;
            panel.textFinished(this);
        } else {
            text = prior + e.getKeyChar() + after;
            caretPos += 1;
        }
    }

    public void setPanel(AnnotationPaintPanel panel) {
        this.panel = panel;
    }

    public AnnotationObject createCopy(int xOffset, int yOffset) {
        Point2D first = this.getLocation();
        AnnotationTextObject copy = new AnnotationTextObject(this.paintPanel, this.view_user, this.edit_user, this.del_user, this.owner_user,
                this.getFont(), this.getColor(), new Point2D.Double(first.getX() + xOffset, first.getY() + yOffset), this.getText(),
                this.isAntialiased());
        return copy;
    }

    @Override
    public String getInfo() {
        Point2D first = this.getLocation();

        String filename = paintPanel.getCurrentImageFilename();

        String fonts = new StringBuilder().append(this.getFont().getName()).append("|").append(this.getFont().getStyle()).append("|").append(this.getFont().getSize()).toString();

        String point = new StringBuilder().append(first.getX()).append("|").append(first.getY()).toString();

        return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).
                append("|").append(filename).append("|Text|").append(fonts).append("|").append(this.getColor().getRGB()).
                append("|").append(point).append("|").append(this.getText()).append("|").append(this.isAntialiased()).
                append("|").append(this.getAlpha()).append("|").toString();
    }

    public AnnotationObject loadText(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, Font font, Color color, Point2D p2d, String text, boolean antia, float alpha) {
        AnnotationTextObject copy = new AnnotationTextObject(paintPanel, view_user, edit_user, del_user, owner_user,
                font, color, p2d, text, antia);
        return copy;
    }

    @Override
    public Font getTextFont() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTextNote() {
        throw new UnsupportedOperationException("Not supported yet.");
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
