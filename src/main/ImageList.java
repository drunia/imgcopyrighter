package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

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
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	/**
	 * Set elements to list from File[] array
	 * @param files
	 * @throws IOException 
	 */
	public void setElements(File[] files) throws IOException {
		Icon icon;
		Image image;
		ImageLabel.itemNumber = 0;
		elements = new ImageLabel[files.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new ImageLabel();
			elements[i].setImageFile(files[i]);
			/*
			 * Initialize icons for list items
			 */
			if (isImgIconed()) {
				image = new ImageIcon(files[i].getAbsolutePath()).getImage();
				icon = new ImageIcon(image.getScaledInstance(64, 64, Image.SCALE_FAST));
				image.flush();
			} else {
				if (elements[0].getIcon() == null)
					icon = new ImageIcon(getClass().getResource("/res/image.png"));
				else icon = elements[0].getIcon();
			}
			elements[i].setIcon(icon);
		}
		setListData(elements);
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
		if (isSelected) 
			listElement.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 3));
		else
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
	public static int itemNumber;
	private File imageFile;
	
	/**
	 * Default constructor
	 */
	public ImageLabel() {
		setPreferredSize(new Dimension(-1, 64));
	}
	
	/**
	 * Return a file associated with list item
	 * @return - File
	 */
	public File getImageFile() {
		return imageFile;
	}
	
	/**
	 * Set formatted HTML info to this.setText()
	 * @param imageFile - File of image
	 */
	public void setImageFile(File imageFile) {
		String fileName = imageFile.getName();
		if (fileName.length() > 30) 
			fileName = fileName.substring(0, 29) + "...";
		String html = "<html>" +
				"<table width=100% border=0 cellspacing=0 cellpadding=5>" +
				"<tr><td rowspan=2><font size=5 color=green>" + ++itemNumber +
				"</font></td><td>" + fileName + "</td>" + "</tr>" +
				"<tr><td><code>" + calculateHumanImageSize(imageFile.length()) +
				"</code></td></tr></table></html>";
		setText(html);	
		setToolTipText(imageFile.getAbsolutePath());
		setBorder(BorderFactory.createRaisedBevelBorder());
		this.imageFile = imageFile;
	}
	
	/**
	 * Method calculate human like format of image size
	 * @param size - Image size
	 */
	private String calculateHumanImageSize(long imageSize) {
		double size = imageSize;
		String dataName = "����";
		
		//KBytes
		if (imageSize > 1024) {
			size = imageSize / 1024f;
			dataName = "�����";
		}
		//MBytes
		if (imageSize > (1024 * 1024)) {
			size = imageSize / (1024f * 1024f);
			dataName = "�����";
		}
		return String.format("%.2f %s", size, dataName);
	}
}