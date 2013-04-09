/**
 * Configuration class for draw copyright method
 */
package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author druni@
 *
 */
public class ImgConfiguration {
	private Font font;
	private Dimension orientation;
	private String text;
	private Image img;
	
	public static final int ORIENTATION_TOPLEFT      = 0;
	public static final int ORIENTATION_TOPCENTER    = 1;
	public static final int ORIENTATION_TOPRIGHT     = 2;
	public static final int ORIENTATION_BOTTOMLEFT   = 3;
	public static final int ORIENTATION_BOTTOMCENTER = 4;
	public static final int ORIENTATION_BOTTOMRIGHT  = 5;
	public static final int ORIENTATION_CENTER 		 = 6;
	
	/**
	 * Default constructor
	 */
	public ImgConfiguration() {
		super();
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
	public void setLogo(Image img) {
		this.img = img;
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
	public Dimension getOrientation() {
		return orientation;
	}

	/**
	 * Return copyright text
	 * @return String
	 */
	public String getText() {
		if (text == null) text = "ImageCopyright by drunia";
		return text;
	}

	public Image getImg() {
		if (img == null) 
			img = new ImageIcon(getClass().getResource("/res/def_logo.png")).getImage();
		return img;
	}
}
