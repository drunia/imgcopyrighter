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

	/**
	 * Default constructor
	 */
	public ImageList() {
		super();
		setCellRenderer(new ILCellRenderer());
	}
	
	/**
	 * Set elements to list from File[] array
	 * @param files
	 */
	public void setElements(File[] files) {
		JLabel[] elements = new JLabel[files.length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = new JLabel(files[i].getAbsolutePath());
		}
		setListData(elements);
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
		ImageIcon icon = new ImageIcon(listElement.getText());
		icon = new ImageIcon(icon.getImage().getScaledInstance(48, -1, Image.SCALE_DEFAULT));
		listElement.setIcon(icon);
		listElement.setToolTipText(listElement.getText());
		listElement.setBorder(BorderFactory.createRaisedBevelBorder());
		return listElement;
	}
}