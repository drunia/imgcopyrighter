package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
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
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ImageCopyrighter extends JFrame {
	private final String APP_DIR_NAME = "ImageCopyrighter";
	private static final long serialVersionUID = 1L;
	private JButton startButton;
	private JTextField textField;
	private ImageList imgList;
	private ImagePreview iPview;
	
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
	
		int horizComponents = 2;
		JPanel cPanel = new JPanel(); 
		cPanel.setLayout(new GridLayout(1, horizComponents));
		
		imgList = new ImageList();
		imgList.setImgIconed(true);
		JScrollPane spane = new JScrollPane(imgList);
		cPanel.add(spane);
		
	    iPview = new ImagePreview();
	    cPanel.add(iPview);
	    
	    add(cPanel, BorderLayout.CENTER);
	    
	    //ImageList selection handler
		imgList.addListSelectionListener(new  ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ImageList l = (ImageList) e.getSource();
				ImageLabel lb = (ImageLabel) l.getSelectedValue();
				File image = lb.getImageFile();
				BufferedImage img;
				try {
					img = ImageIO.read(image);
					Font f = new Font(null, Font.ITALIC, 25);
					iPview.setPreview(img, null, f, "Пример текста", (int) (Math.random() * 7));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		//Info panel
		JPanel eventPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
		
		
		//Set Nimbus LookAndFeel if exist
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
			//try {
				File saveFile = new File(files[i].getParent() + "/" + APP_DIR_NAME + "/" + files[i].getName());
				String ext = saveFile.getName().substring(saveFile.getName().lastIndexOf('.') + 1);
				saveFile.mkdirs();
				
				//BufferedImage img = ImageIO.read(files[i]); 
				//drawCopyRight(img);
				//ImageIO.write(img, ext, saveFile);
				//img.flush();
			//} catch (IOException e) {		
			//	e.printStackTrace();
			//}
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
		} 
		
		iconf.setText("ImageCopyright Java");
		//iconf.setFont(null, Font.PLAIN, 14);
		iconf.setLogo(logo);
		
		Point tc = iconf.getTextCoords();
		Point lc = iconf.getLogoCoords();
		
		Graphics g = img.getGraphics();
		g.setFont(iconf.getFont());
		g.setColor(Color.RED);
		
		g.drawImage(logo, lc.x, lc.y, null);
		g.drawString(iconf.getText(), tc.x, tc.y);
		
		System.out.println("textPoint = " + tc);
		
		System.out.println("Done");
	}

}
