/**
 * Configuration class for draw copyright method
 */
package main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author druni@
 *
 */
public class ImageConf {
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
	public static final int ORIENTATION_CENTER		 = 6;
	
	/**
	 * Default constructor
	 * @param logo - Image
	 * @param orientation - ImgConfiguration.ORIENTATION_XXXXXXXXX
	 */
	public ImageConf(BufferedImage logo, int orientation) {
		super();
		this.logo = (logo == null) ? false : true; 
		if (this.logo) setLogo(logo);
		this.orientation = orientation;
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
	public Point getOrientation(BufferedImage bimg) {
		int imgW = bimg.getWidth();
		int imgH = bimg.getHeight();
		
		//Calculate font
		int fontSize = font.getSize() * (((imgH * imgW) / 1000000) + 1);
		font = new Font(font.getName(), font.getStyle(), fontSize);
		FontMetrics fm = bimg.getGraphics().getFontMetrics(font);
		
		//Calculate indents 1 % from image size
		int indentX = imgW / 100;
		int indentY = imgH / 100;
		
		int textWidth = fm.stringWidth(text);
		int textHeight = fm.getHeight();
		int x = 0, y = 0;	
		
		//Get Y text center by logo
		if (logo) {
			textWidth += logoImg.getWidth() + 10; // 10 px space logo<->text
			textHeight = (logoImg.getHeight() / 2) - (textHeight / 2);
		}
		
		//Calculate orientation
		switch (orientation) {
			case ORIENTATION_TOPLEFT:
				x = indentX;
				y = indentY;
				break;
			case ORIENTATION_TOPCENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = indentY;
				break;
			case ORIENTATION_TOPRIGHT:
				x = imgW - (textWidth + indentX) ; 
				y = indentY; 
				break;
			case ORIENTATION_CENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = (imgH / 2) - (textHeight / 2); 
				break;
			case ORIENTATION_BOTTOMLEFT:
				x = indentX;
				y = imgH - (textHeight + indentY);;
				break;
			case ORIENTATION_BOTTOMCENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = imgH - (textHeight + indentY);; 
				break;
			case ORIENTATION_BOTTOMRIGHT:
				x = imgW - (textWidth + indentX);
				y = imgH - (textHeight + indentY);
				break;
		}
		return new Point(x, y);
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
