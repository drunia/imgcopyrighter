package main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Class positions calculator
 * @author druni@
 */
public class ImageConf {
	private Font font;
	private Font calcFont;
	private int orientation;
	private String text;
	private BufferedImage logoImg;
	private BufferedImage proccessImage;
	private int textWidth;
	private int textHeight;
	private Point logoPoint;
	private Point textPoint;
	
	/**
	 * Position constants
	 */
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
	 * @throws Exception - When image is null
	 */
	public ImageConf(BufferedImage processImage, int orientation) {
		super();
		this.proccessImage = processImage;
		this.orientation = orientation;
	}
	
	
	/**
	 * Set copyright font 
	 * @param font - Font
	 */
	public void setFont(Font font) {
		this.font = font;
		if (text != null) calculatePositions();
	}

	
	/**
	 * Set copyright text
	 * @param text - String
	 */
	public void setText(String text) {
		this.text = text;
		if (font != null) calculatePositions();
	}
	
	/**
	 * Set copyright logo
	 */
	public void setLogo(BufferedImage logo) {
		logoImg = logo;
		if (font != null && text != null)
			calculatePositions();
	}

	/**
	 * Return logo orientation
	 * @return Dimension (X, Y) position
	 */
	public Point getLogoCoords() {
		return logoPoint;
	}
	
	/**
	 * Return text orientation
	 * @return Dimension (X, Y) position
	 */
	public Point getTextCoords() {
		return textPoint;
	}
	
	/**
	 * Calculate text orientation
	 */
	private void calculatePositions() {
		int imgW = proccessImage.getWidth();
		int imgH = proccessImage.getHeight();
		
		//Calculate font size
		int fontSize = font.getSize() + ((imgH + imgW) / 100);
		calcFont = new Font(font.getName(), font.getStyle(), fontSize);
		FontMetrics fm = proccessImage.getGraphics().getFontMetrics(calcFont);
		
		textWidth = fm.stringWidth(text);
		textHeight = fm.getHeight();
		int x = 0, y = 0;	
		
		//Calculate indent
		int indent = textHeight / 4;
		
		//Calculate orientation
		switch (orientation) {
			case ORIENTATION_TOPLEFT:
				x = indent;
				y = textHeight - indent;
				break;
			case ORIENTATION_TOPCENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = textHeight - indent;
				break;
			case ORIENTATION_TOPRIGHT:
				x = imgW - textWidth; 
				y = textHeight - indent; 
				break;
			case ORIENTATION_CENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = (imgH / 2) - (textHeight / 2); 
				break;
			case ORIENTATION_BOTTOMLEFT:
				x = indent;
				y = imgH - indent;
				break;
			case ORIENTATION_BOTTOMCENTER:
				x = (imgW / 2) - (textWidth / 2); 
				y = imgH - indent;
				break;
			case ORIENTATION_BOTTOMRIGHT:
				x = imgW - (textWidth + indent);
				y = imgH - indent;
				break;
		}	
		
		if (logoImg == null) {
			textPoint = new Point(x, y);
		} else {
			y -= indent;
			int h = logoImg.getHeight();
			int w = logoImg.getWidth();
			
			logoPoint = new Point(x, y);
			textPoint = new Point(x + w, y + (h / 2) + indent);
			switch (orientation) {
				case ORIENTATION_TOPCENTER:
					logoPoint = new Point(x - (w / 2), y);
					textPoint = new Point(x + (w / 2), y + (h / 2) + indent);
					break;
				case ORIENTATION_TOPRIGHT:
					logoPoint = new Point(x - w, y);
					textPoint = new Point(x, y + (h / 2) + indent);
					break;
				case ORIENTATION_CENTER:	
					logoPoint = new Point(x - (w / 2), y - (h / 2));
					textPoint = new Point(x + (w / 2), y + indent);
					break;
				case ORIENTATION_BOTTOMCENTER:
					logoPoint = new Point(x - (w / 2), y - h);
					textPoint = new Point(x + (w / 2), y - (h / 2) + indent);
					break;
				case ORIENTATION_BOTTOMRIGHT:
					logoPoint = new Point(x - w, y - h);
					textPoint = new Point(x, y - (h / 2) + indent);
					break;
				case ORIENTATION_BOTTOMLEFT:
					logoPoint = new Point(x, y - h);
					textPoint = new Point(x + w, y - (h / 2) + indent);
					break;
			} 
		}		
	}

	/**
	 * Return copyright text
	 * @return String
	 */
	public String getText() {
		return text;
	}

	/**
	 * Return copyright logo
	 * @return Image
	 */
	public Image getLogo() {
		return logoImg;
	}
	
	/**
	 * Return size-calculated font
	 * @return
	 */
	public Font getFont() {
		return calcFont;
	}
}
