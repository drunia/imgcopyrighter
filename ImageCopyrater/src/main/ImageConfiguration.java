/**
 * Configuration class for draw copyright method
 */
package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author druni@
 *
 */
public class ImageConfiguration {
	private Font font;
	private int orientation;
	private String text;
	private BufferedImage logoImg;
	private boolean logo;
	
	public static final int ORIENTATION_TOPLEFT      = 0;
	public static final int ORIENTATION_TOPCENTER    = 1;
	public static final int ORIENTATION_TOPRIGHT     = 2;
	public static final int ORIENTATION_BOTTOMLEFT   = 3;
	public static final int ORIENTATION_BOTTOMCENTER = 4;
	public static final int ORIENTATION_BOTTOMRIGHT  = 5;
	public static final int ORIENTATION_CENTER 		 = 6;
	
	/**
	 * Default constructor
	 * @param logo - Image
	 * @param orientation - ImgConfiguration.ORIENTATION_XXXXXXXXX
	 */
	public ImageConfiguration(Image logo, int orientation) {
		super();
		this.logo = (logo == null) ? false : true; 
	}
	
	/**
	 * Set copyright font
	 * @param name - Font name String
	 * @param style - Font style (Font.BOLD/ITALIC) int
	 * @param size - Font size int
	 */
	public void setFont(String name, int style, int size) {
		font = new Font(name, style, size);
	}
	
	/**
	 * Set copyright text
	 * @param text - String
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Set copyright logo
	 */
	public void setLogo(BufferedImage img) {
		this.logoImg = img;
	}

	/**
	 * Return initialized font
	 * @return
	 */
	public Font getFont() {
		if (font == null) setFont(null, Font.PLAIN, 12);
		return font;
	}

	/**
	 * Calculate text & logo orientation
	 * @return Dimension (X, Y) position
	 */
	public Dimension getOrientation(BufferedImage bimg) {
		Graphics g = bimg.getGraphics();
		FontMetrics fm = g.getFontMetrics(font);
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		int x = 0, y = 0;
		
		//get Y text center by logo
		if (logo) {
			textWidth += logoImg.getWidth(null) + 10; // 10 px space logo<->text
			textHeight = (int) (logoImg.getHeight(null) / 2) - (textHeight / 2);
		}
		
		//Calculate orientation
		switch (orientation) {
			case ORIENTATION_TOPLEFT:
				x = 0;
				y = 0;
				break;
			case ORIENTATION_TOPCENTER:
				x = (int) (bimg.getWidth() / 2) - (textWidth / 2); 
				y = 0;
				break;
			case ORIENTATION_TOPRIGHT:
				x = bimg.getWidth() - textWidth;
				y = 0;
				break;
		}
		
		return new Dimension(x, y);
	}

	/**
	 * Return copyright text
	 * @return String
	 */
	public String getText() {
		if (text == null) text = "ImageCopyright by drunia";
		return text;
	}

	/**
	 * Return copyright logo
	 * @return Image
	 */
	public Image getImg() {
		if (logoImg == null)
			try {
				logoImg = ImageIO.read(getClass().getResource("/res/def_logo.png").openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		return logoImg;
	}
}
