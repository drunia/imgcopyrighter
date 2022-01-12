package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class for choose color and size font
 * @author drunia
 */
public class FontColorSizeDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JColorChooser colorChooser;
	private JComboBox sizeCbx;
	private ImageCopyrighter owner;
	
	/**
	 * Constructor class
	 * @param owner - object of {@link ImageCopyrighter}
	 * @param initColor - initialized color
	 * @param initSize - initialized size
	 */
	public FontColorSizeDialog(ImageCopyrighter owner, Color initColor, int initSize) {
		super();
		this.owner = owner;

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Выбор цвета и размера шрифта");
		setPreferredSize(new Dimension(480, 420));
		setModal(true);
		setLocation(
				owner.getLocation().x + owner.getWidth() / 4,
				owner.getLocation().y + owner.getHeight() / 8);
		setResizable(false);
		
		//JColorChooser
		colorChooser = new JColorChooser(initColor);
		add(colorChooser, BorderLayout.CENTER);
		
		//Control panel
		JPanel controlPanel = new JPanel(new GridLayout(0, 3, 5, 5));
		controlPanel.setPreferredSize(new Dimension(-1, 35));
		controlPanel.setMinimumSize(new Dimension(-1, 35));
		
		//Font size
		JLabel sizeFontLb = new JLabel("Выберите размер шрифта:");
		controlPanel.add(sizeFontLb);
		
		String[] sizes = new String[95];
		for (int i = 0; i < sizes.length; i++)
			sizes[i] = Integer.toString(i + 5);
		
		sizeCbx = new JComboBox(sizes);
		sizeCbx.setSelectedItem(Integer.toString(initSize));
		controlPanel.add(sizeCbx);
		
		//Accept button
		JButton acceptBtn = new JButton("Применить");
		acceptBtn.setActionCommand("acceptButton");
		acceptBtn.setIcon(new ImageIcon(getClass().getResource("/res/ok.png")));
		acceptBtn.addActionListener(this);
		controlPanel.add(acceptBtn);
		
		add(controlPanel, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	/**
	 * Action events handler
	 * Initialize size and color callback variables
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Color c = colorChooser.getColor();
		owner.fontColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), owner.alpha);
		owner.fontSize = Integer.parseInt((String) sizeCbx.getSelectedItem());
		dispose();
	}
}

