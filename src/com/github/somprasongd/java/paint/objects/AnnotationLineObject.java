package com.github.somprasongd.java.paint.objects;

import annotation.AnnotationPaintPanel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * Draws a Line2D.
 *
 * <p>Copyright (c) 2004 Alistair Dickie. All Rights Reserved.
 * See alistairdickie.com for contact details
 * See licence.txt for licence infomation</p>
 */
public class AnnotationLineObject extends AnnotationObject {

    private Point2D locationStart;
    private Point2D locationEnd;
    private Point2D locStartDrag;
    private Point2D locEndDrag;
    private Stroke stroke;
    private AnnotationPaintPanel paintPanel;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    private Vector edit_user_vector = new Vector();
    private Vector del_user_vector = new Vector();
    private Vector view_user_vector = new Vector();

    public AnnotationLineObject() {

    }

    public AnnotationLineObject(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user, String owner_user,
            Color color, Point2D locationStart, Stroke stroke, boolean antialiased, float alpha) {

        super(color, null, antialiased, alpha);
        this.paintPanel = paintPanel;
        this.locationStart = locationStart;
        this.locationEnd = locationStart;
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

    public void updateMiddle() {
        double x = locationEnd.getX() + (locationStart.getX() - locationEnd.getX()) / 2.0;
        double y = locationEnd.getY() + (locationStart.getY() - locationEnd.getY()) / 2.0;

        super.setLocation(new Point2D.Double(x, y));
    }

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

    public void setStartDragLocs() {
        this.locStartDrag = new Point2D.Double(this.locationStart.getX(), this.locationStart.getY());
        this.locEndDrag = new Point2D.Double(this.locationEnd.getX(), this.locationEnd.getY());
    }

    public void updateLocation(Point2D point) {
        if (this.getCurrentPointIndex() == 1) {
            this.setLocationStart(point);
            this.updateMiddle();
        } else if (this.getCurrentPointIndex() == 2) {
            this.setLocationEnd(point);
            this.updateMiddle();
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
        Point2D[] points = new Point2D[]{this.getLocation(), locationStart, locationEnd};
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

    public double[] getTranslation() {
        return new double[]{0.5, 0.5};
    }

    public AnnotationObject createCopy(int xOffset, int yOffset) {
        AnnotationLineObject copy = new AnnotationLineObject(this.paintPanel, this.view_user, this.edit_user, this.del_user, this.owner_user,
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

        return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).append("|").
                append(filename).append("|Line|").append(locatStart).append("|").append(locatEnd).append("|").
                append(this.getColor().getRGB()).append("|").append(getlocatStart).append("|").append(strokes).
                append("|").append(this.isAntialiased()).append("|").append(false).append("|").append(this.getAlpha()).
                append("|").toString();
    }

    public AnnotationObject loadLine(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, double xStart, double yEnd, double xEnd, double yStart, Color color, Point2D local,
            Stroke stroke, boolean antia, float alpha) {

        AnnotationLineObject copy = new AnnotationLineObject(paintPanel, view_user, edit_user, del_user, owner_user,
                color, local, stroke, antia, alpha);
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
            /*int i = 0;
            int start = 0;
            int end;
            String same_user = "";
            String new_user = "";
            Vector old_user = new Vector();
            while (start != view_user.length()) {
            end = view_user.indexOf('>', start);
            if (end >= 0) {
            old_user.add(view_user.substring(start, end));
            start = end + 1;
            i++;
            }
            }
            for (int j = 0; j < user.size(); j++) {
            for (int k = 0; k < old_user.size(); k++) {
            if(user.get(j).toString().equals(old_user.get(k).toString())){
            same_user += user.get(j).toString();
            }else{
            new_user  += user.get(j).toString();
            }
            }
            }*/
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

    @Override
    public String getOwnerUser() {
        return owner_user;
    }

    @Override
    public Vector getDel_user() {
        return del_user_vector;
    }

    @Override
    public Vector getEdit_user() {
        return edit_user_vector;
    }
}
