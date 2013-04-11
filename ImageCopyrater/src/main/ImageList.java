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
	private JLabel[] elements;

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
		elements = new JLabel[files.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new JLabel(files[i].getAbsolutePath());
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
		String imgPath = listElement.getText();
		
		listElement.setToolTipText(imgPath);
		listElement.setText(imgPath.substring(imgPath.lastIndexOf(File.separatorChar) + 1));
		listElement.setBorder(BorderFactory.createRaisedBevelBorder());
		return listElement;
	}
}

/**
 * JLabel class with File specified fields
 * @author druni@
 */
class ImageLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	private long imageSize;
	private String imagePath;
	private String hLikeSize;
	
	/**
	 * Method return Human like size format
	 * @return the imageSize String
	 */
	public String getImageSize() {
		return hLikeSize;
	}
	
	/**
	 * Set file size
	 * @param imageSize the imageSize to set
	 */
	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
		hLikeSize = calculateHumanImageSize(imageSize);
	}
	
	/**
	 * Return path of image 
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}
	
	/**
	 * Set path of image
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	/**
	 * Method calculate human like format of image size
	 * @param size - Image size
	 */
	private String calculateHumanImageSize(long size) {
		String dataName = "bytes";
		//KBytes
		if (imageSize > 1024) {
			size = imageSize / 1024;
			dataName = "KBytes";
		}
		//MBytes
		if (imageSize > (1024 * 1024)) {
			size = imageSize / (1024 * 1024);
			dataName = "MBytes";
		}
		//GBytes
		if (imageSize > (1024 * 1024 * 1024)) {
			size = imageSize / (1024 * 1024 * 1024);
			dataName = "GBytes";
		}		
		return size + " " + dataName;
	}
}