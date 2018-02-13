package annotation;

import annotation.util.BufferedImageTool;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 *
 * @author  Somprasong Damyos
 */
public class AnnotationStampImageObject extends AnnotationObject {

    private BufferedImage img;
    private Point2D bottomRight;
    private String image_path;
    private AnnotationPaintPanel paintPanel;
    private String view_user;
    private String edit_user;
    private String del_user;
    private String owner_user;
    private Vector edit_user_vector = new Vector();
    private Vector del_user_vector = new Vector();
    private Vector view_user_vector = new Vector();

    public AnnotationStampImageObject() {

    }

    public AnnotationStampImageObject(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user, String owner_user,
            Image image, Point2D location, float alpha, String image_path, double img_width, double img_hieght) {
        super(null, new Point((int) (location.getX()), (int) (location.getY())), false, alpha);
        this.paintPanel = paintPanel;
        this.img = BufferedImageTool.copy(image);
        if (img_width == 0 && img_hieght == 0) {
            this.bottomRight = new Point2D.Double(this.getLocation().getX() + img.getWidth(),
                    this.getLocation().getY() + img.getHeight());
        } else {
            this.bottomRight = new Point2D.Double(img_width, img_hieght);
        }
        this.image_path = image_path;
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
        int width = (int) (bottomRight.getX() - this.getLocation().getX());
        int height = (int) (bottomRight.getY() - this.getLocation().getY());
        Image sub = img.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);

        g.drawImage(sub, (int) (this.getLocation().getX()), (int) (this.getLocation().getY()), null);

    }

    public void setBottomRightLocation(Point2D point) {
        double threshold = 3.0;
        double newX = point.getX();
        double newY = point.getY();
        if (newX < this.getLocation().getX() + threshold) {
            newX = this.getLocation().getX() + threshold;
        }
        if (newY < this.getLocation().getY() + threshold) {
            newY = this.getLocation().getY() + threshold;
        }
        this.bottomRight = new Point2D.Double(newX, newY);

    }

    @Override
    public void setLocation(Point2D point) {
        double xDiff = point.getX() - this.getLocation().getX();
        double yDiff = point.getY() - this.getLocation().getY();
        super.setLocation(point);
        this.bottomRight = new Point2D.Double(bottomRight.getX() + xDiff, bottomRight.getY() + yDiff);
    }

    public void setStartDragLocs() {

    }

    public void updateLocation(Point2D point) {
        if (this.getCurrentPointIndex() == 1) {
            this.setBottomRightLocation(point);
        } else if (this.getCurrentPointIndex() == 0) {
            this.setLocation(point);
        }
    }

    public BufferedImage getImage() {
        return img;
    }

    public String getImagePath() {
        return this.image_path;
    }

    public Point2D[] getHighlightPoints() {
        Point2D[] points = new Point2D[]{this.getLocation(), new Point((int) bottomRight.getX(), (int) (bottomRight.getY()))};

        return points;
    }

    public boolean finished(Point2D point) {
        return true;
    }

    public double[] getTranslation() {
        return new double[]{0.0, 0.0};
    }

    public AnnotationObject createCopy(int xOffset, int yOffset) {
        Point2D new_point = new Point2D.Double(this.getLocation().getX() + xOffset, this.getLocation().getY() + yOffset);
        AnnotationStampImageObject copy = new AnnotationStampImageObject(this.paintPanel, this.view_user, this.edit_user, this.del_user,
                this.owner_user, this.getImage(), new_point, this.getAlpha(), this.getImagePath(), bottomRight.getX(), bottomRight.getY());

        return copy;
    }

    @Override
    public String getInfo() {
        String filename = paintPanel.getCurrentImageFilename();

//        return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).append("|").
//                append(filename).append("|StampImage").append("|").append(image_path).append("|").
//                append(this.getLocation().getX()).append("|").append(this.getLocation().getY()).append("|").
//                append(this.getAlpha()).append("|").toString();
        return (new StringBuilder()).append(view_user + "|" + edit_user + "|" + del_user + "|" + owner_user).append("|").
                append(filename).append("|StampImage").append("|").append(image_path).append("|").
                append(this.getLocation().getX()).append("|").append(this.getLocation().getY()).append("|").
                append(this.getAlpha()).append("|").append(bottomRight.getX()).append("|").
                append(bottomRight.getY()).append("|").toString();
    }

    public AnnotationObject loadImage(AnnotationPaintPanel paintPanel, String view_user, String edit_user, String del_user,
            String owner_user, String path, Point2D point, float alpha, double img_width, double img_hieght) {


        BufferedImage image = null;
        try {
            URL imageFile = new URL(path);
            image = ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println("Cannt load this image");
        }

        AnnotationStampImageObject copy = new AnnotationStampImageObject(paintPanel, view_user, edit_user, del_user, owner_user,
                image, point, alpha, path, img_width, img_hieght);

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
