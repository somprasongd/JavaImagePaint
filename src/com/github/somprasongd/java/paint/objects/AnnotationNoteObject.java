package com.github.somprasongd.java.paint.objects;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

/**
 * Draws a Rectangle2D.
 *
 * <p>
 * Copyright (c) 2004 Alistair Dickie. All Rights Reserved. See
 * alistairdickie.com for contact details See licence.txt for licence
 * infomation</p>
 */
public class AnnotationNoteObject extends AnnotationObject {

    private Point2D locationStart;
    private Point2D locationEnd;
    private Point2D locStartDrag;
    private Point2D locEndDrag;
    private Point2D arcPoint;
    private Point2D arcPointDrag;
    private Stroke stroke;
    private double arcWidth;
    private double arcWidthDrag;
    private double arcHeight;
    private double arcHeightDrag;
    private String text;
    private Font font;
    private Color textColor;
    private Color bgColor;
    private Color bdColor;
    private boolean bgTransparent;
    private Hashtable map = new Hashtable();
    private AttributedString note_text;
    private AttributedCharacterIterator text_note;
    private TextLayout layout;
    private double strokeWidth;

    public AnnotationNoteObject() {

    }

    public AnnotationNoteObject(Font font, Color textColor, Color bgColor, Color brColor, Point2D locationStart, String text, boolean antialiased,
            Stroke stroke, boolean bgTransparent, double arcWidth, double arcHeight, float alpha) {

        super(textColor, null, antialiased, alpha);
        this.locationStart = locationStart;
        this.locationEnd = new Point2D.Double(locationStart.getX() + 200, locationStart.getY() + 100);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;

        this.stroke = stroke;
        this.font = font;
        this.text = text;
        this.textColor = textColor;
        this.bgColor = bgColor;
        this.bdColor = brColor;
        this.bgTransparent = bgTransparent;
        this.updateMiddle();
        init();
    }

    private void init() {
        map.put(TextAttribute.FONT, this.font);
        map.put(TextAttribute.FOREGROUND, this.textColor);
        String newline = "                                        ";
        String str_note = text.replace(":", newline);
        note_text = new AttributedString(str_note, this.map);
        text_note = note_text.getIterator();
    }

    public void updateArcPoint() {
        double x;
        double y;

        if (locationStart.getX() < locationEnd.getX()) {
            x = locationStart.getX() + arcWidth + 10;
        } else {
            x = locationStart.getX() - arcWidth - 10;
        }

        if (locationStart.getY() < locationEnd.getY()) {
            y = locationStart.getY() + arcHeight + 10;
        } else {
            y = locationStart.getY() - arcHeight - 10;
        }
        this.arcPoint = new Point2D.Double(x, y);
    }

    public void updateMiddle() {
        double x = locationEnd.getX() + (locationStart.getX() - locationEnd.getX()) / 2;
        double y = locationEnd.getY() + (locationStart.getY() - locationEnd.getY()) / 2;

        super.setLocation(new Point2D.Double(x, y));
        this.updateArcPoint();
    }

    public void setArcWidths(Point2D p) {
        if (locationStart.getX() < locationEnd.getX()) {
            arcWidth = arcWidthDrag + p.getX() - arcPointDrag.getX();
        } else {
            arcWidth = arcWidthDrag + arcPointDrag.getX() - p.getX();
        }

        if (locationStart.getY() < locationEnd.getY()) {
            arcHeight = arcHeightDrag + p.getY() - arcPointDrag.getY();
        } else {
            arcHeight = arcHeightDrag + arcPointDrag.getY() - p.getY();
        }

        if (arcWidth < 0.0) {
            arcWidth = 0.0;
        }

        if (arcHeight < 0.0) {
            arcHeight = 0.0;
        }

        this.updateArcPoint();
    }

    @Override
    public void addGraphics(Graphics2D g) {
        g.setStroke(stroke);
        g.setColor(this.getColor());
        Point2D.Double s = new Point2D.Double(locationStart.getX(), locationStart.getY());
        Point2D.Double e = new Point2D.Double(locationEnd.getX(), locationEnd.getY());

        double width = locationEnd.getX() - locationStart.getX();
        double height = locationEnd.getY() - locationStart.getY();
        if (width < 0.0) {
            double t = e.x;
            e.x = s.x;
            s.x = t;
            width = -width;
        }

        if (height < 0.0) {
            double t = e.y;
            e.y = s.y;
            s.y = t;
            height = -height;

        }
        //สี
        if (bgTransparent) {
            g.setPaint(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 202));
        } else {
            g.setPaint(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 255));
        }

        if (arcWidth > 0.0 || arcHeight > 0.0) {
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(s.x, s.y, width, height, arcWidth, arcHeight);
            g.fill(rect);
            //วาดกรอบโน็ต
            g.setStroke(stroke);
            g.setColor(bdColor);
            g.draw(rect);
        } else {
            Rectangle2D.Double rect = new Rectangle2D.Double(s.x, s.y, width, height);

            g.fill(rect);
            //วาดกรอบโน็ต
            g.setStroke(stroke);
            g.setColor(bdColor);
            g.draw(rect);
        }
        FontRenderContext frc = g.getFontRenderContext();

        float xLoc = (float) (s.x + 20 + this.getTranslation()[0]);
        float yLoc = (float) (s.y + 10 + this.getTranslation()[1]);

        int paragraphStart = text_note.getBeginIndex();
        int paragraphEnd = text_note.getEndIndex();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(text_note, frc);

        float widths = 150.0f;

        float drawPosY = yLoc;
        lineMeasurer.setPosition(paragraphStart);
        while (lineMeasurer.getPosition() < paragraphEnd) {
            this.layout = lineMeasurer.nextLayout(widths);

            drawPosY += this.layout.getAscent();

            float drawPosX;
            if (this.layout.isLeftToRight()) {
                drawPosX = xLoc;
            } else {
                drawPosX = widths - this.layout.getAdvance();
            }

            this.layout.draw(g, drawPosX, drawPosY);

            drawPosY += this.layout.getDescent() + this.layout.getLeading();
        }

    }

    public Point2D getLocationStart() {
        return locationStart;
    }

    public void setLocationStart(Point2D locationStart) {
        this.locationStart = new Point2D.Double(locationStart.getX(), locationStart.getY());
    }

    public Point2D getLocationEnd() {
        return locationEnd;
    }

    public void setLocationEnd(Point2D locationEnd) {
        this.locationEnd = new Point2D.Double(locationEnd.getX(), locationEnd.getY());
    }

    public void setLocationMiddle(Point2D point) {
        double xMove = point.getX() - this.getStartDragLoc().getX();
        double yMove = point.getY() - this.getStartDragLoc().getY();
        this.setLocationEnd(new Point2D.Double(locEndDrag.getX() + xMove, locEndDrag.getY() + yMove));
        this.setLocationStart(new Point2D.Double(locStartDrag.getX() + xMove, locStartDrag.getY() + yMove));
        this.updateMiddle();
    }

    @Override
    public void setStartDragLocs() {
        this.locStartDrag = new Point2D.Double(this.locationStart.getX(), this.locationStart.getY());
        this.locEndDrag = new Point2D.Double(this.locationEnd.getX(), this.locationEnd.getY());
        this.arcPointDrag = new Point2D.Double(arcPoint.getX(), arcPoint.getY());
        this.arcWidthDrag = arcWidth;
        this.arcHeightDrag = arcHeight;
    }

    @Override
    public void updateLocation(Point2D point) {

        switch (this.getCurrentPointIndex()) {
            case 1:
                this.setLocationStart(point);
                this.updateMiddle();
                break;
            case 2:
                this.setLocationEnd(point);
                this.updateMiddle();
                break;
            case 3:
                this.setLocationEnd(new Point2D.Double(locationEnd.getX(), point.getY()));
                this.setLocationStart(new Point2D.Double(point.getX(), locationStart.getY()));
                this.updateMiddle();
                break;
            case 4:
                this.setLocationEnd(new Point2D.Double(point.getX(), locationEnd.getY()));
                this.setLocationStart(new Point2D.Double(locationStart.getX(), point.getY()));
                this.updateMiddle();
                break;
            case 5:
                this.setLocationStart(new Point2D.Double(locationStart.getX(), point.getY()));
                this.updateMiddle();
                break;
            case 6:
                this.setLocationEnd(new Point2D.Double(locationEnd.getX(), point.getY()));
                this.updateMiddle();
                break;
            case 7:
                this.setLocationStart(new Point2D.Double(point.getX(), locationStart.getY()));
                this.updateMiddle();
                break;
            case 8:
                this.setLocationEnd(new Point2D.Double(point.getX(), locationEnd.getY()));
                this.updateMiddle();
                break;
            case 9:
                this.setArcWidths(point);
                break;
            case 0:
                this.setLocationMiddle(point);
                break;
            default:
                this.setLocationEnd(point);
                this.updateMiddle();
                break;
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

    public Color gettextColor() {
        return textColor;
    }

    public Color getBGColor() {
        return bgColor;
    }

    public Color getBDColor() {
        return bdColor;
    }

    public boolean isBackgroundTransparent() {
        return bgTransparent;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public void setStrokeWidth(double width) {
        this.strokeWidth = width;
    }

    public double getStrokeWidth() {
        return this.strokeWidth;
    }

    @Override
    public Point2D[] getHighlightPoints() {
        Point2D middle = this.getLocation();
        Point2D bottomLeft = new Point2D.Double(locationStart.getX(), locationEnd.getY());
        Point2D bottomRight = new Point2D.Double(locationEnd.getX(), locationStart.getY());
        Point2D middleTop = new Point2D.Double(middle.getX(), locationStart.getY());
        Point2D middleBottom = new Point2D.Double(middle.getX(), locationEnd.getY());
        Point2D middleLeft = new Point2D.Double(locationStart.getX(), middle.getY());
        Point2D middleRight = new Point2D.Double(locationEnd.getX(), middle.getY());
        Point2D[] points = new Point2D[]{middle, locationStart, locationEnd, bottomLeft, bottomRight, middleTop, middleBottom, middleLeft, middleRight, arcPoint};

        return points;
    }

    @Override
    public boolean finished(Point2D point) {
        if (this.getCurrentPointIndex() < 0) {
            double dist = point.distance(this.locationStart);
            if (dist < 0.5) {
                return false;
            }
        }
        if (this.locationStart.equals(this.locationEnd)) {
            return false;
        }

        this.updateMiddle();
        return true;
    }

    public double getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public double getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }

    @Override
    public double[] getTranslation() {
        return new double[]{0.5, 0.5};
    }

    @Override
    public String getInfo() {
//        String filename = paintPanel.getCurrentImageFilename();

        String strokes = new StringBuilder().append(((BasicStroke) this.getStroke()).getLineWidth()).toString();

        String fonts = new StringBuilder().append(this.getFont().getName()).append("|").append(this.getFont().getStyle()).append("|").append(this.getFont().getSize()).toString();

        String locatStart = new StringBuilder().append(this.locationStart.getX()).append("|").append(this.locationStart.getY()).toString();

        String locatEnd = new StringBuilder().append(this.locationEnd.getX()).append("|").append(this.locationEnd.getY()).toString();

        String getlocatStart = new StringBuilder().append(this.getLocationStart().getX()).append("|").append(this.getLocationStart().getY()).toString();

        return (new StringBuilder()).
                append("Note|").append(fonts).append("|").append(this.textColor.getRGB()).append("|").
                append(getlocatStart).append("|").append(this.getText()).append("|").append(this.isAntialiased()).
                append("|").append(this.getAlpha()).append("|").append(this.bgColor.getRGB()).append("|").append(this.bdColor.getRGB()).
                append("|").append(strokes).append("|").append(this.bgTransparent).append("|").append(this.getStrokeWidth()).
                append("|").append(locatStart).append("|").append(locatEnd).append("|").append(this.getArcWidth()).
                append("|").append(this.getArcHeight()).append("|").toString();
    }

    public AnnotationObject loadNote(String view_user, String edit_user, String del_user,
            String owner_user, double xStart, double yStart, double xEnd, double yEnd, Point2D locStart, Font font,
            Color textcolor, String text, Color bgcolor, Color brcolor,
            boolean antia, float alpha, Stroke stroke, boolean bg_Transparent, double width,
            double arcWidth, double arcHeight) {

        AnnotationNoteObject copy = new AnnotationNoteObject(
                font, textcolor, bgcolor, brcolor, locStart, text, antia, stroke, bg_Transparent, arcWidth, arcHeight, alpha);

        copy.locationEnd = new Point2D.Double(xEnd, yEnd);
        copy.locationStart = new Point2D.Double(xStart, yStart);
        copy.updateMiddle();
        copy.setStrokeWidth(width);
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

    @Override
    public AnnotationObject createCopy(int xOffset, int yOffset) {
        return null;
    }
}
