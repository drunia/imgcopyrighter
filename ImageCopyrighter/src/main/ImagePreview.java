package main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

/**
 * Preview class for manipulation with logo & text
 * @author druni@
 */
public class ImagePreview extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage previewImage;
	private BufferedImage origImage;
	private BufferedImage logo;
	private Font font;
	private String text;
	private Color fontColor;
	private int orientation;
	private Point logoCoords;
	private Point textCoords;
	private int prevX, prevY;
	private boolean updated;
	
	/**
	 * Default constructor
	 */
	public ImagePreview() {
		super();
		setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		addComponentListener(new ResizeHandler());
	}
	
	/**
	 * SwingWorker updater preview class
	 * Method setPreview() run in another thread
	 */
	private class PreviewUpdater extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() {
			setPreview(origImage, logo, font, text, orientation, fontColor);
			return null;
		}
	}
	
	/**
	 * Resize handler class
	 */
	private class ResizeHandler extends ComponentAdapter {
		/**
		 * Update up for 1 second
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			if (origImage != null && !updated) {
				updated = true;
				Timer updateTimer = new Timer();
				updateTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						new PreviewUpdater().execute();
						updated = false;
					}
				}, 1000);
			}
		}
	}
	
	/**
	 * Method make preview of image with text and logo
	 * @param image - {@link BufferedImage}
	 * @param logo  - {@link BufferedImage}
	 * @param font  - {@link Font}
	 * @param text  - {@link String}
	 * @param orientation - int
	 * @param color TODO
	 */
	public void setPreview(BufferedImage image, BufferedImage logo, Font font, String text, int orientation, Color color) {
		origImage = image;
		this.logo = logo;  
		this.font = font;
		this.text = text;
		this.fontColor = color;
		this.orientation = orientation;
		
		/*//Build preview
		Image img = null;
		if (image.getWidth() > image.getHeight()) { 
			img = image.getScaledInstance(getWidth(), -1, Image.SCALE_FAST);
			if (getWidth() > getHeight()) 
				img = img.getScaledInstance(getHeight(), -1, Image.SCALE_FAST);
		} else {
			img = image.getScaledInstance(-1, getHeight(), Image.SCALE_FAST);
			if (getHeight() > getWidth()) 
				img = img.getScaledInstance(-1, getWidth(), Image.SCALE_FAST);
		}
		*/
		Image img = image.getScaledInstance(-1, getWidth(), Image.SCALE_FAST);;

		int h = image.getHeight();
		int w = image.getWidth();
		int max = Math.max(h, w);
		int min = Math.min(h, w);
		double presc = (double) max / min;
		
		System.out.println("prec:" + presc);
		int h1 = 0, w1 = 0;
		
		if (w > h) {
			if (getWidth() > getHeight()) {
				h1 = getHeight();
				w1 = (int) (h1 * presc);
			} else {
				w1 = getWidth();
				h1 = (int) (w1 / presc);
			}
		} else {
			
		}
		
		img = image.getScaledInstance(w1, h1, Image.SCALE_FAST);
		
					
			
		if (previewImage != null) previewImage.flush();
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
		g2d.setColor(color);
		g2d.drawString(imgConf.getText(), textCoords.x, textCoords.y);
		g2d.dispose();
		
		//Send message to repaint component
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