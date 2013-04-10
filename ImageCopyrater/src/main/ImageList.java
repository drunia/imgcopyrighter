package main;

import java.awt.Component;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

public class ImageList extends JList {
	private static final long serialVersionUID = 1L;
	private boolean imgIconded = false;
	private ImageIcon[] icons;

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
	 */
	public void setElements(File[] files) {
		ImageIcon icon;
		JLabel[] elements = new JLabel[files.length];
		icons = new ImageIcon[elements.length];
		
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new JLabel(files[i].getAbsolutePath());
			//Initialize icons for list items
			if (isImgIconed()) 
				icon = new ImageIcon(files[i].getAbsolutePath());
			else {
				if (icons[0] == null)
					icon = new ImageIcon(getClass().getResource("/res/image.png"));
				else icon = icons[0];
			}
			icon = new ImageIcon(icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
			icons[i] = icon;
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
	
	/**
	 * Return icon by index
	 * @param index - int
	 * @return ImageIcon
	 */
	public ImageIcon getListIcon(int index) {
		return icons[index];
	}
	
	/**
	 * Returns icons[].length
	 * @return - int
	 */
	public int getListIconsLength() {
		return icons.length;
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
		
		listElement.setIcon(((ImageList) list).getListIcon(index));
		listElement.setToolTipText(imgPath);
		listElement.setText(imgPath.substring(imgPath.lastIndexOf(File.separatorChar) + 1));
		listElement.setBorder(BorderFactory.createRaisedBevelBorder());
		return listElement;
	}
}