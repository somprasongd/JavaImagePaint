/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somprasongd.java.paint.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.IndexColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.RGBImageFilter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Somprasong Damyos <somprasong@hospital-os.com>
 */
public class ImageUtils {

    private static final Logger LOG = Logger.getLogger(ImageUtils.class.getName());

    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
    public final static String BMP = "bmp";
    public final static String PNG = "png";
    public final static String GIF = "gif";
    public final static String TIFF = "tiff";
    public final static String TIF = "tif";

    public static byte[] toByteArray(BufferedImage originalImage) {
        return toByteArray(originalImage, JPG);
    }

    public static byte[] toByteArray(BufferedImage originalImage, String fileFormat) {
        byte[] imageInByte = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(originalImage, fileFormat, baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return imageInByte;
    }

    public static BufferedImage toBufferedImage(byte[] imageInByte) {
        BufferedImage image = null;
        try {
            InputStream in = new ByteArrayInputStream(imageInByte);
            image = ImageIO.read(in);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return image;
    }

    /**
     * This method returns true if the specified image has transparent pixels
     * This snippet was taken from:
     * http://www.exampledepot.com/egs/java.awt.image/HasAlpha.html
     *
     * @param image
     * @return True if the image has any transparency
     */
    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * This method returns a buffered image with the contents of an image This
     * snippet was taken from:
     * http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
     *
     * @param image
     * @return The buffered image
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static BufferedImage resize(BufferedImage origin, int width, int height) {
        return resize(origin, width, height, origin.getType());
    }

    public static BufferedImage resizeRGB(BufferedImage origin, int width, int height) {
        return resize(origin, width, height, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage resize(BufferedImage origin, int width, int height, int imageType) {
        BufferedImage resizedImage = new BufferedImage(width, height, imageType);
        Graphics2D g = resizedImage.createGraphics();
        double xScale = (double) width / origin.getWidth();
        double yScale = (double) height / origin.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
        g.drawRenderedImage(origin, at);
        g.dispose();
        return resizedImage;
    }

    public static ByteArrayOutputStream resize(ByteArrayInputStream bais, int width, int height) throws IOException {
        BufferedImage src = ImageIO.read(bais);
        BufferedImage dest = resizeRGB(src, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(dest, JPG, baos);
        return baos;
    }

    /**
     * Scales down an image into a box of maxSideLenght x maxSideLength.
     *
     * @param image the image to scale down. It remains untouched.
     * @param maxSideLength the maximum side length of the scaled down instance.
     * Has to be > 0.
     * @return the scaled image, the
     */
    public static BufferedImage scaleImage(BufferedImage image, int maxSideLength) {
        assert (maxSideLength > 0);
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();
        double scaleFactor;
        if (originalWidth > originalHeight) {
            scaleFactor = ((double) maxSideLength / originalWidth);
        } else {
            scaleFactor = ((double) maxSideLength / originalHeight);
        }
        // create smaller image
        BufferedImage img = new BufferedImage((int) (originalWidth * scaleFactor), (int) (originalHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
        // fast scale (Java 1.4 & 1.5)
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), null);
        return img;
    }

    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)
        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    /**
     * Clips the input image to the specified shape
     *
     * @param image the input image
     * @param clipVerts list of x, y pairs defining the clip shape, normalised
     * to image dimensions (think texture coordinates)
     * @return The smallest image containing those pixels that fall inside the
     * clip shape
     */
    public static BufferedImage clip(BufferedImage image, float... clipVerts) {
        assert clipVerts.length >= 6;
        assert clipVerts.length % 2 == 0;

        int[] xp = new int[clipVerts.length / 2];
        int[] yp = new int[xp.length];

        int minX = image.getWidth(), minY = image.getHeight(), maxX = 0, maxY = 0;

        for (int j = 0; j < xp.length; j++) {
            xp[j] = Math.round(clipVerts[2 * j] * image.getWidth());
            yp[j] = Math.round(clipVerts[2 * j + 1] * image.getHeight());

            minX = Math.min(minX, xp[j]);
            minY = Math.min(minY, yp[j]);
            maxX = Math.max(maxX, xp[j]);
            maxY = Math.max(maxY, yp[j]);
        }

        for (int i = 0; i < xp.length; i++) {
            xp[i] -= minX;
            yp[i] -= minY;
        }

        Polygon clip = new Polygon(xp, yp, xp.length);
        BufferedImage out = new BufferedImage(maxX - minX, maxY - minY, image.getType());
        Graphics g = out.getGraphics();
        g.setClip(clip);

        g.drawImage(image, -minX, -minY, null);
        g.dispose();

        return out;
    }

    public static ByteArrayOutputStream crop(ByteArrayInputStream bais, int width, int height) throws IOException {
        BufferedImage src = ImageIO.read(bais);
        BufferedImage clipping = crop(src, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(clipping, JPG, baos);
        return baos;
    }

    public static BufferedImage crop(BufferedImage src, int width, int height) throws IOException {
        int x = src.getWidth() / 2 - width / 2;
        int y = src.getHeight() / 2 - height / 2;

//        System.out.println("---" + src.getWidth() + " - " + src.getHeight() + " - " + x + " - " + y);
        BufferedImage clipping = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//src.getType());  
        Graphics2D area = (Graphics2D) clipping.getGraphics().create();
        area.drawImage(src, 0, 0, clipping.getWidth(), clipping.getHeight(), x, y, x + clipping.getWidth(),
                y + clipping.getHeight(), null);
        area.dispose();

        return clipping;
    }

    public static ByteArrayOutputStream fit(ByteArrayInputStream bais, int width, int height) throws IOException {
        BufferedImage src = ImageIO.read(bais);
        Float scale;
        if (src.getWidth() > src.getHeight()) {
            scale = (float) width / (float) src.getWidth();
        } else {
            scale = (float) height / (float) src.getHeight();
        }
        int newWidth = Float.valueOf(src.getWidth() * scale).intValue();
        int newHeight = Float.valueOf(src.getHeight() * scale).intValue();
        BufferedImage temp = resizeRGB(src, newWidth, newHeight);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(temp, JPG, baos);
        return baos;
    }
    
    public static BufferedImage fit(BufferedImage src, int width, int height) {
        Float scale;
        if (src.getWidth() > src.getHeight()) {
            scale = (float) width / (float) src.getWidth();
        } else {
            scale = (float) height / (float) src.getHeight();
        }
        int newWidth = Float.valueOf(src.getWidth() * scale).intValue();
        int newHeight = Float.valueOf(src.getHeight() * scale).intValue();
        return resizeRGB(src, newWidth, newHeight);
    }

    /**
     * get image orientation type
     *
     * @param imageFile
     * @return 0:Landscape, 1:Portrait
     */
    public static int getOrientation(File imageFile) {
        ImageIcon image = new ImageIcon(imageFile.getPath());
        return (image.getIconWidth() > image.getIconHeight()) ? 0 : 1;
    }

    public static Image makeColorTransparent(Image im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static BufferedImage makeImageTranslucent(BufferedImage source,
            double alpha) {
        BufferedImage target = new BufferedImage(source.getWidth(),
                source.getHeight(), java.awt.Transparency.TRANSLUCENT);
        // Get the images graphics
        Graphics2D g = target.createGraphics();
        // Set the Graphics composite to Alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                (float) alpha));
        // Draw the image into the prepared reciver image
        g.drawImage(source, null, 0, 0);
        // let go of all system resources in this Graphics
        g.dispose();
        // Return the image
        return target;
    }

    /**
     * Converts the source to 1-bit colour depth (monochrome). No transparency.
     *
     * @param src the source image to convert
     * @return a copy of the source image with a 1-bit colour depth.
     */
    public static BufferedImage convert1(BufferedImage src) {
        IndexColorModel icm = new IndexColorModel(1, 2, new byte[]{(byte) 0,
            (byte) 0xFF}, new byte[]{(byte) 0, (byte) 0xFF},
                new byte[]{(byte) 0, (byte) 0xFF});

        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY, icm);

        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);

        cco.filter(src, dest);

        return dest;
    }

    /**
     * Converts the source image to 4-bit colour using the default 16-colour
     * palette:
     * <ul>
     * <li>black</li>
     * <li>dark red</li>
     * <li>dark green</li>
     * <li>dark yellow</li>
     * <li>dark blue</li>
     * <li>dark magenta</li>
     * <li>dark cyan</li>
     * <li>dark grey</li>
     * <li>light grey</li>
     * <li>red</li>
     * <li>green</li>
     * <li>yellow</li>
     * <li>blue</li>
     * <li>magenta</li>
     * <li>cyan</li>
     * <li>white</li>
     * </ul>
     * No transparency.
     *
     * @param src the source image to convert
     * @return a copy of the source image with a 4-bit colour depth, with the
     * default colour pallette
     */
    public static BufferedImage convert4(BufferedImage src) {
        int[] cmap = new int[]{0x000000, 0x800000, 0x008000, 0x808000,
            0x000080, 0x800080, 0x008080, 0x808080, 0xC0C0C0, 0xFF0000,
            0x00FF00, 0xFFFF00, 0x0000FF, 0xFF00FF, 0x00FFFF, 0xFFFFFF};
        return convert4(src, cmap);
    }

    /**
     * Converts the source image to 4-bit colour using the given colour map. No
     * transparency.
     *
     * @param src the source image to convert
     * @param cmap the colour map, which should contain no more than 16 entries
     * The entries are in the form RRGGBB (hex).
     * @return a copy of the source image with a 4-bit colour depth, with the
     * custom colour pallette
     */
    public static BufferedImage convert4(BufferedImage src, int[] cmap) {
        IndexColorModel icm = new IndexColorModel(4, cmap.length, cmap, 0,
                false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY, icm);
        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);
        cco.filter(src, dest);

        return dest;
    }

    /**
     * Converts the source image to 8-bit colour using the default 256-colour
     * palette. No transparency.
     *
     * @param src the source image to convert
     * @return a copy of the source image with an 8-bit colour depth
     */
    public static BufferedImage convert8(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_INDEXED);
        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);
        cco.filter(src, dest);
        return dest;
    }

    /**
     * Converts the source image to 24-bit colour (RGB). No transparency.
     *
     * @param src the source image to convert
     * @return a copy of the source image with a 24-bit colour depth
     */
    public static BufferedImage convert24(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);
        cco.filter(src, dest);
        return dest;
    }

    /**
     * Converts the source image to 32-bit colour with transparency (ARGB).
     *
     * @param src the source image to convert
     * @return a copy of the source image with a 32-bit colour depth.
     */
    public static BufferedImage convert32(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp cco = new ColorConvertOp(src.getColorModel()
                .getColorSpace(), dest.getColorModel().getColorSpace(), null);
        cco.filter(src, dest);
        return dest;
    }

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = null;
        try {
            imgURL = new File(path).toURI().toURL();
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
