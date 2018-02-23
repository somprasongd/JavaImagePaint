package com.github.somprasongd.java.paint.objects;

import com.github.somprasongd.java.paint.components.PaintPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Draws some text.
 *
 * <p>
 * Copyright (c) 2004 Alistair Dickie. All Rights Reserved. See
 * alistairdickie.com for contact details See licence.txt for licence
 * infomation</p>
 */
public class AnnotationTextObject extends AnnotationObject {

    private Font font;
    private String text;
    private TextLayout layout;
    private int caretPos;
    private PaintPanel panel;
    private Point2D locationDrag;

    public AnnotationTextObject() {

    }

    public AnnotationTextObject(
            Font font, Color color, Point2D location, String text, boolean antialiased) {
        super(color, location, antialiased);
        this.font = font;
        this.text = text;
        caretPos = 0;
    }

    @Override
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

    @Override
    public Point2D[] getHighlightPoints() {
        Point2D[] points = new Point2D[]{this.getLocation()};
        return points;
    }

    @Override
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

    @Override
    public boolean finished(Point2D point) {
        return true;
    }

    @Override
    public double[] getTranslation() {
        return new double[]{0.0, 0.0};
    }

    @Override
    public void setStartDragLocs() {
        locationDrag = new Point2D.Double(this.getLocation().getX(), this.getLocation().getY());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String prior = text.substring(0, caretPos);
        String after = text.substring(caretPos, text.length());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                if (prior.length() > 0) {
                    text = prior.substring(0, prior.length() - 1) + after;
                    if (text.length() == 0) {
                        text = " ";
                    }
                    caretPos -= 1;
                }
                break;
            case KeyEvent.VK_DELETE:
                if (after.length() > 0) {
                    text = prior + after.substring(1, after.length());
                    if (text.length() == 0) {
                        text = " ";
                    }
                }
                break;
            case KeyEvent.VK_RIGHT:
                caretPos += 1;
                if (caretPos > text.length()) {
                    caretPos = text.length();
                }
                break;
            case KeyEvent.VK_LEFT:
                caretPos -= 1;
                if (caretPos < 0) {
                    caretPos = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
                caretPos = 0;
                panel.textFinished(this);
                break;
            default:
                text = prior + e.getKeyChar() + after;
                caretPos += 1;
                break;
        }
    }

    public void setPanel(PaintPanel panel) {
        this.panel = panel;
    }

    public AnnotationObject createCopy(int xOffset, int yOffset) {
        Point2D first = this.getLocation();
        AnnotationTextObject copy = new AnnotationTextObject(
                this.getFont(), this.getColor(), new Point2D.Double(first.getX() + xOffset, first.getY() + yOffset), this.getText(),
                this.isAntialiased());
        return copy;
    }

    @Override
    public String getInfo() {
        Point2D first = this.getLocation();

//        String filename = paintPanel.getCurrentImageFilename();
        String fonts = new StringBuilder().append(this.getFont().getName()).append("|").append(this.getFont().getStyle()).append("|").append(this.getFont().getSize()).toString();

        String point = new StringBuilder().append(first.getX()).append("|").append(first.getY()).toString();

        return (new StringBuilder()).
                //                append(filename).
                append("|Text|").append(fonts).append("|").append(this.getColor().getRGB()).
                append("|").append(point).append("|").append(this.getText()).append("|").append(this.isAntialiased()).
                append("|").append(this.getAlpha()).append("|").toString();
    }

    public AnnotationObject loadText(String view_user, String edit_user, String del_user,
            String owner_user, Font font, Color color, Point2D p2d, String text, boolean antia, float alpha) {
        AnnotationTextObject copy = new AnnotationTextObject(
                font, color, p2d, text, antia);
        return copy;
    }

    @Override
    public Font getTextFont() {
        return null;
    }

    @Override
    public String getTextNote() {
        return null;
    }
}
