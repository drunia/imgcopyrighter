package main;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class ImageList extends JList {
	private static final long serialVersionUID = 1L;
	private boolean imgIconded = false;
	private ImageLabel[] elements;

	/**
	 * Default constructor
	 */
	public ImageList() {
		super();
		setCellRenderer(new ILCellRenderer());
		setLayoutOrientation(JList.VERTICAL);
	}
	
	/**
	 * Set elements to list from File[] array
	 * @param files
	 * @throws IOException 
	 */
	public void setElements(File[] files) throws IOException {
		Icon icon;
		Image image;
		elements = new ImageLabel[files.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new ImageLabel();
			elements[i].setImageFile(files[i]);
			//Initialize icons for list items
			if (isImgIconed()) {
				image = new ImageIcon(files[i].getAbsolutePath()).getImage();
				icon = new ImageIcon(image.getScaledInstance(48, 48, Image.SCALE_SMOOTH));
			} else {
				if (elements[0].getIcon() == null)
					icon = new ImageIcon(getClass().getResource("/res/image.png"));
				else icon = elements[0].getIcon();
			}
			elements[i].setIcon(icon);
		}
		setListData(elements);
		//Free memory after load icons
		System.gc();
	}
	
	/**
	 * Set iconed from images or simple iconed 
	 * @param imgIconed - boolean
	 */
	public void setImgIconed(boolean imgIconed) {
		this.imgIconded = imgIconed;
	}
	
	/**
	 * Return iconed from images or simple iconed
	 * @return - boolean
	 */
	public boolean isImgIconed() {
		return imgIconded;
	}
}

/**
 * Image list cell renderer  class
 * @author drunia
 */
class ILCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		JLabel listElement = (JLabel) value;	
		return listElement;
	}
}

/**
 * JLabel class with File specified fields
 * @author druni@
 */
class ImageLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	public static int itemNumber;
	
	/**
	 * Set formatted HTML info to this.setText()
	 * @param imageFile - File of image
	 */
	public void setImageFile(File imageFile) {
		String html = "<html>" +
				"<table width=100% border=0 cellspacing=0 cellpadding=5>" +
				"<tr><td rowspan=2><font size=5 color=green>" + ++itemNumber +
				"</font></td><td>" + imageFile.getName() + "</td>" + "</tr>" +
				"<tr><td><code>" + calculateHumanImageSize(imageFile.length()) +
				"</code></td></tr></table></html>";
		setText(html);	
		setToolTipText(imageFile.getAbsolutePath());
		setBorder(BorderFactory.createRaisedBevelBorder());
	}
	
	/**
	 * Method calculate human like format of image size
	 * @param size - Image size
	 */
	private String calculateHumanImageSize(long imageSize) {
		double size = 0;
		String dataName = "Bytes";
		
		//KBytes
		if (imageSize > 1024) {
			size = imageSize / 1024f;
			dataName = "KBytes";
		}
		//MBytes
		if (imageSize > (1024 * 1024)) {
			size = imageSize / (1024f * 1024f);
			dataName = "MBytes";
		}
		return String.format("%.2f %s", size, dataName);
	}
}