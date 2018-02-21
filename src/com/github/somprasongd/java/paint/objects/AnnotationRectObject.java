package com.github.somprasongd.java.paint.objects;

import annotation.AnnotationPaintPanel;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

/**
 * Draws a Rectangle2D.
 *
 * <p>Copyright (c) 2004 Alistair Dickie. All Rights Reserved.
 * See alistairdickie.com for contact details
 * See licence.txt for licence infomation</p>
 */
public class AnnotationRectObject extends AnnotationObject {

    private Point2D locationStart;
    private Point2D locationEnd;
    private Point2D locStartDrag;
    private Point2D locEndDrag;
    private Point2D arcPoint;
    private Point2D arcPointDrag;
    private Stroke stroke;
    private boolean filled;
    private double arcWidth;
    private double arcWidthDrag;
    private double arcHeight;
    private double arcHeightDrag;
    private String text;
    private AnnotationPaintPanel paintPanel;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    private Vector edit_user_vector = new Vector();
    private Vector del_user_vector = new Vector();
    private Vector view_user_vector = new Vector();

    public AnnotationRectObject() {

    }

    public AnnotationRectObject(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user, String owner_user,
            Color color, Point2D locationStart, Stroke stroke, boolean antialiased, boolean filled, double arcWidth,
            double arcHeight, float alpha) {

        super(color, null, antialiased, alpha);
        this.paintPanel = paintPanel;
        this.locationStart = locationStart;
        this.locationEnd = locationStart;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.filled = filled;
        this.stroke = stroke;
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
        this.updateMiddle();
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

        if (arcWidth > 0.0 || arcHeight > 0.0) {
            RoundRectangle2D.Double rect = new RoundRectangle2D.Double(s.x, s.y, width, height, arcWidth, arcHeight);
            if (this.isFilled()) {
                g.fill(rect);
            } else {
                g.draw(rect);
            }
        } else {
            Rectangle2D.Double rect = new Rectangle2D.Double(s.x, s.y, width, height);
            if (this.isFilled()) {
                g.fill(rect);
            } else {
                g.draw(rect);
            }
        }
    //g.setColor(Color.RED);
    //g.drawString("balsafdafkjfasdfsdkhflsafd", (int)s.x+20, (int)s.y+20);
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

    public void setStartDragLocs() {
        this.locStartDrag = new Point2D.Double(this.locationStart.getX(), this.locationStart.getY());
        this.locEndDrag = new Point2D.Double(this.locationEnd.getX(), this.locationEnd.getY());
        this.arcPointDrag = new Point2D.Double(arcPoint.getX(), arcPoint.getY());
        this.arcWidthDrag = arcWidth;
        this.arcHeightDrag = arcHeight;
    }

    public void updateLocation(Point2D point) {

        if (this.getCurrentPointIndex() == 1) {
            this.setLocationStart(point);
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 2) {
            this.setLocationEnd(point);
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 3) {
            this.setLocationEnd(new Point2D.Double(locationEnd.getX(), point.getY()));
            this.setLocationStart(new Point2D.Double(point.getX(), locationStart.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 4) {
            this.setLocationEnd(new Point2D.Double(point.getX(), locationEnd.getY()));
            this.setLocationStart(new Point2D.Double(locationStart.getX(), point.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 5) {
            this.setLocationStart(new Point2D.Double(locationStart.getX(), point.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 6) {
            this.setLocationEnd(new Point2D.Double(locationEnd.getX(), point.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 7) {
            this.setLocationStart(new Point2D.Double(point.getX(), locationStart.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 8) {
            this.setLocationEnd(new Point2D.Double(point.getX(), locationEnd.getY()));
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 9) {
            this.setArcWidths(point);
        } else if (this.getCurrentPointIndex() == 0) {
            this.setLocationMiddle(point);
        } else {
            this.setLocationEnd(point);
            this.updateMiddle();
        }
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

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

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
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

    public double[] getTranslation() {
        return new double[]{0.5, 0.5};
    }

    public AnnotationObject createCopy(int xOffset, int yOffset) {
        AnnotationRectObject copy = new AnnotationRectObject(this.paintPanel, this.view_user, this.edit_user, this.del_user, this.owner_user,
                this.getColor(), this.getLocationStart(), this.getStroke(), this.isAntialiased(), this.isFilled(), this.getArcWidth(),
                this.getArcHeight(), this.getAlpha());
        copy.locationEnd = new Point2D.Double(this.locationEnd.getX() + xOffset, this.locationEnd.getY() + yOffset);
        copy.locationStart = new Point2D.Double(this.locationStart.getX() + xOffset, this.locationStart.getY() + yOffset);
        return copy;
    }

    @Override
    public String getInfo() {
        String filename = paintPanel.getCurrentImageFilename();

        String locatStart = new StringBuilder().append(this.locationStart.getX()).append("|").append(this.locationStart.getY()).toString();

        String locatEnd = new StringBuilder().append(this.locationEnd.getX()).append("|").append(this.locationEnd.getY()).toString();

        String getlocatStart = new StringBuilder().append(this.getLocationStart().getX()).append("|").append(this.getLocationStart().getY()).toString();

        String strokes = new StringBuilder().append(((BasicStroke) this.getStroke()).getLineWidth()).toString();

        return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).append("|").
                append(filename).append("|Rect|").append(locatStart).append("|").append(locatEnd).append("|").append(this.getColor().
                getRGB()).append("|").append(getlocatStart).append("|").append(strokes).append("|").append(this.isAntialiased()).
                append("|").append(this.isFilled()).append("|").append(this.getAlpha()).append("|").append(this.getArcWidth()).
                append("|").append(this.getArcHeight()).append("|").toString();
    }

    public AnnotationObject loadRect(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, double xStart, double yEnd, double xEnd, double yStart, Color color, Point2D local, Stroke stroke,
            boolean antia, boolean fill, float alpha, double arcWidth, double arcHeight) {

        AnnotationRectObject copy = new AnnotationRectObject(paintPanel, view_user, edit_user, del_user, owner_user,
                color, local, stroke, antia, fill, arcWidth, arcHeight, alpha);
        copy.locationEnd = new Point2D.Double(xEnd, yEnd);
        copy.locationStart = new Point2D.Double(xStart, yStart);
        copy.updateMiddle();
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
