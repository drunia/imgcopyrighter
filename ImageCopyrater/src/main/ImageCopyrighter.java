package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ImageCopyrighter extends JFrame implements ActionListener {
	private final String APP_DIR_NAME = "ImageCopyrighter";
	private static final long serialVersionUID = 1L;
	private JPanel controlPanel;
	private JButton selectFilesBtn;
	private JTextField textField;
	private ImageList imgList;
	private JProgressBar progress;
	private JLabel infoLb;
	private ImagePreview iPview;
	
	static int a;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Set Nimbus LookAndFeel if exist
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {}
			
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				ImageCopyrighter ic = new ImageCopyrighter();
				ic.setVisible(true);
			}
		});

		
	}
	
	/**
	 * Default constructor
	 */
	public ImageCopyrighter() {
		super("ImageCopyright by drunia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
	
		int horizComponents = 2;
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new GridLayout(1, horizComponents));
		
		Border grayBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		
		imgList = new ImageList();
		imgList.setImgIconed(true);
		JScrollPane spane = new JScrollPane(imgList);
		spane.setBorder(grayBorder);
		mainPanel.add(spane);
		
		//Preview & controls panel
		JPanel prevControlPanel = new JPanel();
		prevControlPanel.setLayout(new BoxLayout(prevControlPanel, BoxLayout.Y_AXIS));
		
		//Preview panel
	    iPview = new ImagePreview();
	    prevControlPanel.add(iPview);
	    
	    //Control panel
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new GridLayout(4, 2, 5, 5));
	    
	    //Select files Button
	    selectFilesBtn = new JButton("Выбрать файлы");
	    selectFilesBtn.setActionCommand("selectFilesBtn");
	    selectFilesBtn.addActionListener(this);
	    
	    
	    controlPanel.add(selectFilesBtn);
	    
	    controlPanel.setBorder(grayBorder);
	    controlPanel.setMaximumSize(new Dimension(getWidth() / 2, 150));
	    
	    prevControlPanel.add(controlPanel);
	    mainPanel.add(prevControlPanel);
	    add(mainPanel, BorderLayout.CENTER);
	    
		//Info panels
		JPanel infoPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		infoPanel.setBorder(BorderFactory.createTitledBorder(grayBorder, "Информационная панель:"));
		infoPanel.setPreferredSize(new Dimension(-1, 75));
		
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.X_AXIS));
	
		//ProgressBar
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));
		
		progress = new JProgressBar(0, 100);
		progress.setVisible(false);
		
		progressPanel.add(progress);
		infoPanel.add(progressPanel);
		
		//Label
		infoLb = new JLabel();
		ImageIcon icon = new ImageIcon(getClass().getResource("/res/info_icon.png"));
		infoLb.setIcon(icon);
		msgPanel.add(infoLb);
		infoPanel.add(msgPanel);
		
		add(infoPanel, BorderLayout.SOUTH);
	    
	    //ImageList selection handler
		imgList.addListSelectionListener(new  ListSelectionListener() {
			private int lastIndex;
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("first:"  + e.getFirstIndex());
				System.out.println("last:"  + e.getLastIndex());
				System.out.println("---");
				
				ImageList l = (ImageList) e.getSource();
				if (lastIndex == l.getSelectedIndex() && lastIndex != 0) return;
				lastIndex =  l.getSelectedIndex();
				if (lastIndex < 0) return;
				ImageLabel lb = (ImageLabel) l.getModel().getElementAt(lastIndex);
				
				final File image = lb.getImageFile();
				final BufferedImage img;
				final Font f = new Font(null, Font.ITALIC, 20);
				
				try {
					img = ImageIO.read(image);
										
					//Load preview in another thread
					SwingWorker<Object, Object> loader = new SwingWorker<Object, Object>() {
						@Override
						protected Void doInBackground() {
							iPview.setPreview(img, null, f, "Пример текста", (int) (Math.random() * 7));
							img.flush();
							return null;
						}
					};
					loader.execute();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});		
	}
	
	/**
	 * Main method
	 * @param files - array of selected file/s
	 */
	public void doIt(File[] files) {
		try {
			imgList.setElements(files);
		} catch (IOException e) {e.printStackTrace();}
	//	for (int i = 0; i < files.length; i++) {
			//try {
	//			File saveFile = new File(files[i].getParent() + "/" + APP_DIR_NAME + "/" + files[i].getName());
	//			String ext = saveFile.getName().substring(saveFile.getName().lastIndexOf('.') + 1);
	//			saveFile.mkdirs();
				
				//BufferedImage img = ImageIO.read(files[i]); 
				//drawCopyRight(img);
				//ImageIO.write(img, ext, saveFile);
				//img.flush();
			//} catch (IOException e) {		
			//	e.printStackTrace();
			//}
		//}
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
		
		System.out.println("Done!");
	}

	/**
	 * ActionEvent handler
	 * @param e - ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String aCommand = e.getActionCommand();
		
		//Select files Buton
		if (aCommand.equalsIgnoreCase("selectFilesBtn")) {
			ImageFileChooser ifc = new ImageFileChooser(null);
			ifc.showOpenDialog(null);
			final File[] files = ifc.getSelectedFiles();
			
			progress.setVisible(true);
			progress.setIndeterminate(true);
			
			SwingWorker<Object, Object> filesLoader = new SwingWorker<Object, Object>() {
				@Override
				protected Object doInBackground() {
					doIt(files);
					progress.setIndeterminate(false);
					progress.setVisible(false);
					return null;
				}
			};
			filesLoader.execute();
		}
		
		
		
	}

}
