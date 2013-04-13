package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Preview class for manipulation with logo & text
 * @author druni@
 */
public class ImagePreview extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage priviewImage;
	
	/**
	 * Default constructor
	 */
	public ImagePreview() {
		super();
	}
	
	/**
	 * Method make preview of image with text and logo
	 * @param image - BufferedImage
	 */
	public void setPreview(BufferedImage image) {
		//priviewImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
		//image
	}
	
	/**
	 * Draw preview image 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(priviewImage, 0, 0, null);
	}
}
