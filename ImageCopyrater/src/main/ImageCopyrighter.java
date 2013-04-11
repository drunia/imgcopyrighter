package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class ImageCopyrighter extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton startButton;
	private JTextField textField;
	private ImageList imgList;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ImageCopyrighter ic = new ImageCopyrighter();
		ic.setVisible(true);
		ImageFileChooser ifc = new ImageFileChooser(null);
		ifc.showOpenDialog(null);
		File[] files = ifc.getSelectedFiles();
		ic.doIt(files);
		
	}
	
	/**
	 * Default constructor
	 */
	public ImageCopyrighter() {
		super("ImageCopyright by drunia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);
	
		imgList = new ImageList();
		imgList.setImgIconed(false);
		JScrollPane spane = new JScrollPane(imgList);
		add(spane);
		
		JPanel cPanel = new JPanel(); 
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Main method
	 * @param files - array of selected file/s
	 */
	public void doIt(File[] files) {
		try {
			imgList.setElements(files);
		} catch (IOException e) {e.printStackTrace();}
		for (int i = 0; i < files.length; i++) {
			try {
				System.out.println(files[i].getAbsolutePath());
				File saveFile = new File(
						files[i].getParent() + "/ImageCopyrighter/" + files[i].getName());
				saveFile.mkdirs();
				BufferedImage img = ImageIO.read(files[i]); 
				drawCopyRight(img);
				ImageIO.write(img, "png", saveFile);
			} catch (IOException e) {		
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Draw text method
	 * @param g
	 */
	private void drawCopyRight(BufferedImage img) {
		Graphics g = img.getGraphics();
		int fontSize = 20 * (((img.getHeight() * img.getWidth()) / 1000000) + 1);
		g.setFont(new Font(null, 0, fontSize));
		g.setColor(Color.RED);
		g.drawString("drunia", 0, 20);
	}

}
