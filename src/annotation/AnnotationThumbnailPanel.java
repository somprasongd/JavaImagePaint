
package annotation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Somprasong Damyos
 */
public class AnnotationThumbnailPanel extends JPanel{
    
    private AnnotationMissingIcon annotationMissingIcon = new AnnotationMissingIcon();
    private AnnotationPaintPanel paintPanel = null;
    private  AnnotationNavigatorBar navigatorBar = null;
    private JToolBar buttonBar = new JToolBar();
    private JScrollPane scrollPane;
    private JPanel panel_button;
    private URL[] url_Images = null;
  
    
   
    public AnnotationThumbnailPanel(URL[] url_Images, AnnotationPaintPanel paintPanel, AnnotationNavigatorBar navigatorBar){
        this.url_Images = url_Images;
        this.paintPanel = paintPanel;
        this.navigatorBar = navigatorBar;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        scrollPane = new javax.swing.JScrollPane();
        panel_button = new javax.swing.JPanel();
      
        panel_button.setLayout(new BoxLayout(panel_button, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(panel_button);
        
        buttonBar.setRollover(true);
        
        add(scrollPane, BorderLayout.CENTER);
       
        loadimages.execute();
        
        this.setPreferredSize(new Dimension(110,300));
    }
    private void updatePanel() {
      if (scrollPane != null) {
         this.revalidate();
         scrollPane.repaint();         
      }
      else {
         this.repaint();
      }

   }
      /**
     * SwingWorker class that loads the images a background thread and calls publish
     * when a new one is ready to be displayed.
     *
     * We use Void as the first SwingWroker param as we do not need to return
     * anything from doInBackground().
     */
    private SwingWorker<Void, ThumbnailAction> loadimages = new SwingWorker<Void, ThumbnailAction>() {
        
        /**
         * Creates full size and thumbnail versions of the target image files.
         */
        @Override
        protected Void doInBackground() throws Exception {
            for (int i = 0; i < url_Images.length; i++) {
                ImageIcon icon;
                //icon = createImageIcon(imageFileNames[i], imageName[i]);
                icon = createImageIcon(url_Images[i].toURI().toString(), url_Images[i].getFile());
                ThumbnailAction thumbAction;
                if(icon != null){
                    ImageIcon thumbnailIcon = new ImageIcon(getScaledImage(icon.getImage(), 80, 80));
                    thumbAction = new ThumbnailAction(i, thumbnailIcon, null);
                }else{
                    // the image failed to load for some reason
                    // so load a placeholder instead
                    thumbAction = new ThumbnailAction(url_Images[i], annotationMissingIcon, null);
                }
                publish(thumbAction);
            }
            // unfortunately we must return something, and only null is valid to
            // return when the return type is void.
            return null;
        }
        
        /**
         * Process all loaded images.
         */
        @Override
        protected void process(List<ThumbnailAction> chunks) {
            for (ThumbnailAction thumbAction : chunks) {
                JButton thumbButton = new JButton(thumbAction);
                thumbButton.setOpaque(true);
                thumbButton.setPreferredSize(new Dimension(90, 90));
                thumbButton.setMaximumSize(new Dimension(90, 90));
                // add the new button BEFORE the last glue
                // this centers the buttons in the toolbar
               
                // buttonBar.add(thumbButton, buttonBar.getComponentCount() - 1);
                panel_button.add(thumbButton);
                
                updatePanel();
            }
        }
    };
    
    /**
     * Creates an ImageIcon if the path is valid.
     * @param String - resource path
     * @param String - description of the file
     */
    protected ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = null;
        //java.net.URL imgURL = getClass().getResource(path);
        try{
        imgURL = new URL(path);
        }catch(Exception e){}
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    /**
     * Action class that shows the image specified in it's constructor.
     */
    protected class ThumbnailAction extends AbstractAction{
        
        /**
         *The icon if the full image we want to display.
         */
        // private Icon displayPhoto;
        private URL displayImageFile;
        private int i_imageno;
        
        /**
         * @param Icon - The full size photo to show in the button.
         * @param Icon - The thumbnail to show in the button.
         * @param String - The descriptioon of the icon.
         */
        public ThumbnailAction(URL url, Icon thumb, String desc){
            //displayPhoto = photo;
            displayImageFile = url;
            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);
            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }
        
        public ThumbnailAction(int i_imageno, Icon thumb, String desc){
            //displayPhoto = photo;
            this.i_imageno = i_imageno;
            // The short description becomes the tooltip of a button.
            putValue(SHORT_DESCRIPTION, desc);
            // The LARGE_ICON_KEY is the key for setting the
            // icon when an Action is applied to a button.
            putValue(LARGE_ICON_KEY, thumb);
        }
        
        /**
         * Shows the full image in the main area and sets the application title.
         */
        public void actionPerformed(ActionEvent e) {
            navigatorBar.gotoImage(i_imageno);
            //paintPanel.loadNewImage(displayImageFile);
            // photographLabel.setIcon(displayPhoto);
            // setTitle("Icon Demo: " + getValue(SHORT_DESCRIPTION).toString());
        }
    }
}
