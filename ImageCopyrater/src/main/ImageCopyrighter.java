package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ImageCopyrighter extends JFrame {
	private JButton startButton;
	private JTextField textField;
	private ImageList imgList;
	private static final long serialVersionUID = 1453023172562886029L;
	private Font font;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImageCopyrighter mf = new ImageCopyrighter();
		mf.setVisible(true);
		File[] files = {new File("D:/1.jpg"), new File("d:/DSC_0034.JPG")};
		mf.doIt(files);
		
	}
	
	public ImageCopyrighter() {
		super("Copyright creator by drunia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
	
		imgList = new ImageList();
		add(imgList);
		
		JPanel cPanel = new JPanel(); 
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Main method
	 * @param files - array of selected file/s
	 */
	public void doIt(File[] files) {
		imgList.setElements(files);
		for (int i = 0; i < files.length; i++) {
			
		}
	}
	
	/**
	 * Draw text method
	 * @param g
	 */
	private void drawCopyRight(Graphics g) {
		g.setColor(Color.RED);
		g.drawString(textField.getText(), 0, 20);
	}

}
