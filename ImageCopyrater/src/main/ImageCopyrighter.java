package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class ImageCopyrighter extends JFrame {
	private final String APP_DIR_NAME = "ImageCopyrighter";
	private static final long serialVersionUID = 1L;
	private JButton startButton;
	private JTextField textField;
	private ImageList imgList;
	static int a;
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
		imgList.setImgIconed(true);
		JScrollPane spane = new JScrollPane(imgList);
		add(spane);
		
		JPanel cPanel = new JPanel(); 
		cPanel.setLayout(new BoxLayout(cPanel, BoxLayout.Y_AXIS));
		
		//Set Nuimbus LookAndFeel if exist
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {}
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
				File saveFile = new File(files[i].getParent() + "/" + APP_DIR_NAME + "/" + files[i].getName());
				String ext = saveFile.getName().substring(saveFile.getName().lastIndexOf('.') + 1);
				saveFile.mkdirs();
				
				BufferedImage img = ImageIO.read(files[i]); 
				drawCopyRight(img);
				ImageIO.write(img, ext, saveFile);
				//img.flush();
			} catch (IOException e) {		
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Draw text method
	 * @param img - BufferedImage
	 */
	private void drawCopyRight(BufferedImage img) {
		BufferedImage logo = null;
		ImageConf iconf = null;
		try {
			logo = ImageIO.read(getClass().getResource("/res/def_logo.png").openStream());
			iconf = new ImageConf(img, a++);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		iconf.setText("ImageCopyright Java");
		iconf.setFont(null, Font.PLAIN, 20);
		//iconf.setLogo(logo);
		
		Point tc = iconf.getTextPoint();
		//Point lc = iconf.getLogoPoint();
		
		Graphics g = img.getGraphics();
		g.setFont(iconf.getFont());
		g.setColor(Color.RED);
		
		//g.drawImage(logo, lc.x, lc.y, null);
		g.drawString(iconf.getText(), tc.x, tc.y);
		
		System.out.println("textPoint = " + tc);
		
		System.out.println("Done");
	}

}
