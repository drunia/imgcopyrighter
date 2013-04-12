package main;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Preview class for manipulation with logo & text
 * @author druni@
 */
public class ImagePreview extends JPanel {
	private static final long serialVersionUID = 1L;
	
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
	public void preview(BufferedImage image) {
		Image img = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
	}
}
