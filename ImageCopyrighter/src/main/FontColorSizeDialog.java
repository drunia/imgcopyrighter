package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	 * Callback params initialize before dialog be closed
	 * 
	 * @param callBackColor - selected color
	 * @param callBackSize - selected size
	 */
	public FontColorSizeDialog(ImageCopyrighter owner) {
		super();
		this.owner = owner;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Выбор цвета и размера шрифта");
		setPreferredSize(new Dimension(480, 420));
		setModal(true);
		setLocationRelativeTo(owner);
		setResizable(false);
		
		//JColorChooser
		colorChooser = new JColorChooser(Color.WHITE);
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
		sizeCbx.setSelectedIndex(7);
		controlPanel.add(sizeCbx);
		
		//Accept button
		JButton acceptBtn = new JButton("Выбрать");
		acceptBtn.setActionCommand("acceptButton");
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
		owner.fontColor = colorChooser.getColor();
		owner.fontSize = Integer.parseInt((String) sizeCbx.getSelectedItem());
		dispose();
	}
}

