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
	private final String APP_NAME = "ImageCopyrighter";
	private static final long serialVersionUID = 1L;
	private JPanel controlPanel;
	private JButton selectFilesBtn;
	private JTextField textField;
	private ImageList imgList;
	private JProgressBar progress;
	private JLabel infoLb;
	private FontComboBox fontCbx;
	private ImagePreview iPview;
	public Color fontColor = Color.WHITE;
	public int fontSize = 14;
	
	static int a;
	
	
	/**
	 * Start point
	 * @param args - array of parameters from command line 
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
	
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout(5, 5));
		
		Border grayBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		
		//ImageList
		imgList = new ImageList();
		imgList.setImgIconed(true);
		JScrollPane spane = new JScrollPane(imgList);
		spane.setBorder(grayBorder);
		spane.setPreferredSize(new Dimension(350, -1));
		mainPanel.add(spane, BorderLayout.WEST);
		
		//Preview & controls panel
		JPanel prevControlPanel = new JPanel();
		prevControlPanel.setLayout(new BorderLayout(1, 5));
		
		//Preview panel
	    iPview = new ImagePreview();
	    prevControlPanel.add(iPview, BorderLayout.CENTER);
	    
	    //Control panel
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new GridLayout(0, 2, 5, 1));
	    controlPanel.setBorder(grayBorder);
	    
	    //Select files
	    JLabel selFilesLb = new JLabel("Выберите файлы:");
	
	    selectFilesBtn = new JButton("Выбрать файлы");
	    selectFilesBtn.setActionCommand("selectFilesBtn");
	    selectFilesBtn.addActionListener(this);
	    
	    controlPanel.add(selFilesLb);
	    controlPanel.add(selectFilesBtn);
	
	    //Font]
	    JPanel fontPanel = new JPanel();
	    fontPanel.setLayout(new GridLayout(0, 2, 2, 2));
	    
	    JLabel fontSelLb = new JLabel("Выберите шрифт:");
	    fontCbx = new FontComboBox();
	    controlPanel.add(fontSelLb);
	    
	    JButton colorSizeBtn = new JButton("Цвет шрифта");
	    colorSizeBtn.setActionCommand("colorSizeBtn");
	    colorSizeBtn.addActionListener(this);
	    
	    fontPanel.add(fontCbx);
	    fontPanel.add(colorSizeBtn);
	    controlPanel.add(fontPanel);
	    
	    prevControlPanel.add(controlPanel, BorderLayout.SOUTH);
	    mainPanel.add(prevControlPanel);
	    add(mainPanel, BorderLayout.CENTER);
	    
		//Info panels
		JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 5));
		infoPanel.setBorder(BorderFactory.createTitledBorder(grayBorder, "Информационная панель:"));
		infoPanel.setPreferredSize(new Dimension(-1, 75));
		
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.X_AXIS));
	
		//ProgressBar	
		progress = new JProgressBar(0, 100);
		progress.setVisible(false);
		infoPanel.add(progress);
		
		//Label
		infoLb = new JLabel();
		infoLb.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
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
				if (e.getValueIsAdjusting()) return;

				ImageList l = (ImageList) e.getSource();
				lastIndex =  l.getSelectedIndex();
				
				if (lastIndex < 0) return;
				ImageLabel lb = (ImageLabel) l.getModel().getElementAt(lastIndex);
				
				final File image = lb.getImageFile();
				final BufferedImage img;
				
				try {
					img = ImageIO.read(image);
					
					//Load preview in another thread
					SwingWorker<Void, Void> loader = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() {
							progress.setVisible(true);
							progress.setIndeterminate(true);
							infoLb.setText("Обработка ...");
							
							//Set preview
							iPview.setPreview(img, null, fontCbx.getSelectedFont(0, 20), "Пример текста", (int) (Math.random() * 7));
							img.flush();
							
							progress.setIndeterminate(false);
							progress.setVisible(false);
							infoLb.setText("");
							
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
			if (files.length == 0) return;
			
			progress.setVisible(true);
			progress.setIndeterminate(true);
			infoLb.setText("Загрузка изображений ...");
			
			SwingWorker<Void, Void> filesLoader = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() {
					try {
						imgList.setElements(files);
					} catch (Exception e) {e.printStackTrace();}
					finally {
						progress.setIndeterminate(false);
						progress.setVisible(false);
						infoLb.setText("");
					}
					return null;
				}
			};
			filesLoader.execute();
		}
		
		//Select font size & color
		if (aCommand.equalsIgnoreCase("colorSizeBtn")) {
			
			new FontColorSizeDialog(this);
			System.out.println("color: " + fontColor + " size: " + fontSize);
		}
		
	}
}
