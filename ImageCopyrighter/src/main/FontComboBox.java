package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Font select JComboBox
 * @author drunia
 */
public class FontComboBox extends JComboBox implements ActionListener {
	private static final long serialVersionUID = 1L;
	private String fonts[];
	private String selectedFont;

	/**
	 * JComboBox cell renderer
	 * @author drunia
	 */
	class FontCbxCellRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText((String) value);
			setFont(new Font(getText(), 0, 14));
			if (isSelected) 
				setBackground(Color.ORANGE);
			else
				setBackground(Color.WHITE);
			return this;
		}
	}
	
	/**
	 * Default constructor
	 */
	public FontComboBox() {			
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = ge.getAllFonts();
		
		for (int i = 0; i < fonts.length; i++) {
			if (fonts[i].canDisplayUpTo("ABCabcАБВабв") == -1) {
				addItem(fonts[i].getName());
			}
		}
		setRenderer(new FontCbxCellRenderer());
		setEditable(false);
		addActionListener(this);
	}
	
	/**
	 * Action events handler
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		FontComboBox fc = (FontComboBox) e.getSource();
		selectedFont = fonts[fc.getSelectedIndex()];
		System.out.println(selectedFont);
	}
	
	/**
	 * Return new Font from selected name from fonts list
	 * @return {@link Font}
	 */
	public Font getSelectedFont(int style, int size) {
		return new Font(selectedFont, style, size);
	}

}

