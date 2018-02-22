package com.github.somprasongd.java.paint.objects;

import annotation.AnnotationPaintPanel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Draws a Line2D.
 *
 * <p>
 * Copyright (c) 2004 Alistair Dickie. All Rights Reserved. See
 * alistairdickie.com for contact details See licence.txt for licence
 * infomation</p>
 */
public class AnnotationLineObject extends AnnotationObject {

    private Point2D locationStart;
    private Point2D locationEnd;
    private Point2D locStartDrag;
    private Point2D locEndDrag;
    private Stroke stroke;
    private AnnotationPaintPanel paintPanel;

    public AnnotationLineObject() {

    }

    public AnnotationLineObject(AnnotationPaintPanel paintPanel,
            Color color, Point2D locationStart, Stroke stroke, boolean antialiased, float alpha) {

        super(color, null, antialiased, alpha);
        this.paintPanel = paintPanel;
        this.locationStart = locationStart;
        this.locationEnd = locationStart;
        this.stroke = stroke;
        this.updateMiddle();
    }

    public void updateMiddle() {
        double x = locationEnd.getX() + (locationStart.getX() - locationEnd.getX()) / 2.0;
        double y = locationEnd.getY() + (locationStart.getY() - locationEnd.getY()) / 2.0;

        super.setLocation(new Point2D.Double(x, y));
    }

    @Override
    public void addGraphics(Graphics2D g) {
        g.setStroke(stroke);
        g.setColor(this.getColor());
        Line2D line = new Line2D.Double(locationStart.getX(), locationStart.getY(), locationEnd.getX(), locationEnd.getY());
        g.draw(line);
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
            case 0:
                this.setLocationMiddle(point);
                break;
            default:
                this.setLocationEnd(point);
                this.updateMiddle();
                break;
        }
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public Point2D[] getHighlightPoints() {
        Point2D[] points = new Point2D[]{this.getLocation(), locationStart, locationEnd};
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

    @Override
    public double[] getTranslation() {
        return new double[]{0.5, 0.5};
    }

    @Override
    public AnnotationObject createCopy(int xOffset, int yOffset) {
        AnnotationLineObject copy = new AnnotationLineObject(this.paintPanel,
                this.getColor(), this.getLocationStart(), this.getStroke(), this.isAntialiased(), this.getAlpha());
        copy.locationEnd = new Point2D.Double(this.locationEnd.getX() + xOffset, this.locationEnd.getY() + yOffset);
        copy.locationStart = new Point2D.Double(this.locationStart.getX() + xOffset, this.locationStart.getY() + yOffset);
        copy.updateMiddle();
        return copy;
    }

    @Override
    public String getInfo() {
        String filename = paintPanel.getCurrentImageFilename();

        String locatStart = new StringBuilder().append(this.locationStart.getX()).append("|").
                append(this.locationStart.getY()).toString();

        String locatEnd = new StringBuilder().append(this.locationEnd.getX()).append("|").
                append(this.locationEnd.getY()).toString();

        String getlocatStart = new StringBuilder().append(this.getLocationStart().getX()).
                append("|").append(this.getLocationStart().getY()).toString();

        String strokes = new StringBuilder().append(((BasicStroke) this.getStroke()).getLineWidth()).toString();

        return (new StringBuilder()).
                append(filename).append("|Line|").append(locatStart).append("|").append(locatEnd).append("|").
                append(this.getColor().getRGB()).append("|").append(getlocatStart).append("|").append(strokes).
                append("|").append(this.isAntialiased()).append("|").append(false).append("|").append(this.getAlpha()).
                append("|").toString();
    }

    public AnnotationObject loadLine(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, double xStart, double yEnd, double xEnd, double yStart, Color color, Point2D local,
            Stroke stroke, boolean antia, float alpha) {

        AnnotationLineObject copy = new AnnotationLineObject(paintPanel,
                color, local, stroke, antia, alpha);
        copy.locationEnd = new Point2D.Double(xEnd, yEnd);
        copy.locationStart = new Point2D.Double(xStart, yStart);
        copy.updateMiddle();
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
