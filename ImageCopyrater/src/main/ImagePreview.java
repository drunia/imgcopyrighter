package main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Preview class for manipulation with logo & text
 * @author druni@
 */
public class ImagePreview extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage previewImage;
	private Point logoCoords;
	private Point textCoords;
	private int prevX, prevY;
	
	/**
	 * Default constructor
	 */
	public ImagePreview() {
		super();
	}
	
	/**
	 * Method make preview of image with text and logo
	 * @param image - {@link BufferedImage}
	 * @param logo  - {@link BufferedImage}
	 * @param font  - {@link Font}
	 * @param text  - {@link String}
	 * @param orientation - int
	 */
	public void setPreview(BufferedImage image, BufferedImage logo, Font font, String text, int orientation) {
		Image img = image.getScaledInstance(getWidth(), -1, Image.SCALE_SMOOTH);
		previewImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = previewImage.createGraphics();
		 g2d.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		 g2d.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2d.drawImage(img, 0, 0, null);
		
		ImageConf imgConf = new ImageConf(previewImage, orientation);
		imgConf.setFont(font);
		imgConf.setText(text);
		
		//Prepared coordinates
		logoCoords = imgConf.getLogoCoords();
		textCoords = imgConf.getTextCoords();
		
		//Draw on panel coordinates
		prevX = (getWidth() / 2) - (previewImage.getWidth() / 2);
		prevY = (getHeight() / 2) - (previewImage.getHeight() / 2);
		
		//Draw copyright
		g2d.setFont(imgConf.getFont());
		if (logo != null) {
			imgConf.setLogo(logo);
			g2d.drawImage(logo, logoCoords.x, logoCoords.y, null);
		}
		g2d.drawString(imgConf.getText(), textCoords.x, textCoords.y);
		repaint();
	}
	
	/**
	 * Draw preview image 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (previewImage != null)
			g.drawImage(previewImage, prevX, prevY, null);
	}
}
