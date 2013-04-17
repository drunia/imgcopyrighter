package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private JComboBox orientCbx;
	private ImagePreview iPview;
	public Color fontColor = Color.ORANGE;
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
		setSize(800, 500);
		//setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
	
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout(5, 5));
		
		Border grayBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		
		//ImageList
		imgList = new ImageList();
		imgList.setImgIconed(true);
		JScrollPane spane = new JScrollPane(imgList);
		spane.setBorder(grayBorder);
		spane.setPreferredSize(new Dimension(300, -1));
		mainPanel.add(spane, BorderLayout.WEST);
		
		//Preview & controls panel
		JPanel prevControlPanel = new JPanel();
		prevControlPanel.setLayout(new BorderLayout(1, 5));
		
		//Preview panel
	    iPview = new ImagePreview();
	    prevControlPanel.add(iPview, BorderLayout.CENTER);
	    
	    //Control panel
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new GridBagLayout());
	    controlPanel.setBorder(grayBorder);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    //Select files
	    JLabel selFilesLb = new JLabel("Выберите файлы:");
	    selFilesLb.setPreferredSize(new Dimension(150, 25));
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 1;
	    controlPanel.add(selFilesLb, gbc);
	    
	    selectFilesBtn = new JButton("Выбрать файлы");
	    selectFilesBtn.setActionCommand("selectFilesBtn");
	    selectFilesBtn.addActionListener(this);
	    ImageIcon imgIc = new ImageIcon(getClass().getResource("/res/gtk_directory.png"));
	    selectFilesBtn.setIcon(imgIc);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.weightx = 0.8;
	    controlPanel.add(selectFilesBtn, gbc);
	
	    //Font]
	    JPanel fontPanel = new JPanel();
	    fontPanel.setLayout(new GridLayout(0, 2, 2, 2));
	    fontPanel.setMinimumSize(new Dimension(300, 25));
	    
	    JLabel fontSelLb = new JLabel("Выберите шрифт:");
	    fontSelLb.setPreferredSize(new Dimension(150, 30));
	    fontCbx = new FontComboBox();
	    fontCbx.setActionCommand("fontCbx");
	    fontCbx.addActionListener(this);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.weightx = 1;
	    controlPanel.add(fontSelLb, gbc);
	    
	    JButton colorSizeBtn = new JButton("Цвет и размер");
	    colorSizeBtn.setActionCommand("colorSizeBtn");
	    colorSizeBtn.addActionListener(this);
	    colorSizeBtn.setIcon(get32х16IconFromFontColorAndSize(fontColor, fontSize));
	    
	    fontPanel.add(fontCbx);
	    fontPanel.add(colorSizeBtn);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.weightx = 0.8;
	    controlPanel.add(fontPanel, gbc);
	    
	    //Text
	    JLabel textLb = new JLabel("Введите текст:");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.weightx = 1;
	    controlPanel.add(textLb, gbc);
	    
	    textField = new JTextField(100);
	    textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) return;
				//Generate event for recreating preview 
				ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
				ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
				lsl.valueChanged(lse);
			}
		});
	    textField.setText("ImageCopyrighter by drunia");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gbc.weightx = 0.8;
	    controlPanel.add(textField, gbc);
	    
	    //Orientation
	    JLabel orientLb = new JLabel("Расположение текста:");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.weightx = 1;
	    controlPanel.add(orientLb, gbc);
	    
	    String[] orientItems = new String[] {
	    		"В левом верхнем углу", "Вверху по центру",
	    		"В правом верхнем углу", "В левом нижнем углу",
	    		"Внизу по центру", "В правом нижнем углу", "В центре"
	    };
	    orientCbx = new JComboBox(orientItems);
	    orientCbx.setActionCommand("orientCbx");
	    orientCbx.addActionListener(this);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gbc.weightx = 0.8;
	    controlPanel.add(orientCbx, gbc);
	    
	    prevControlPanel.add(controlPanel, BorderLayout.SOUTH);
	    mainPanel.add(prevControlPanel);
	    add(mainPanel, BorderLayout.CENTER);
	    
		//Info panels
		JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 5));
		infoPanel.setBorder(BorderFactory.createTitledBorder(grayBorder, "Информационная панель:"));
		infoPanel.setPreferredSize(new Dimension(-1, 50));
		
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
				if (!e.getValueIsAdjusting()) return;

				ImageList l = (ImageList) e.getSource();
				lastIndex =  l.getSelectedIndex();
				
				if (lastIndex < 0) return;
				ImageLabel lb = (ImageLabel) l.getModel().getElementAt(lastIndex);
				
				final File image = lb.getImageFile();
				final BufferedImage img;
				System.out.println("read new" + System.nanoTime());
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
							Font f = fontCbx.getSelectedFont(0, fontSize);
							iPview.setPreview(img, null, f, textField.getText(), orientCbx.getSelectedIndex(), fontColor);
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
			JButton colorSizeButton = (JButton) e.getSource();
			new FontColorSizeDialog(this, fontColor, fontSize);
			colorSizeButton.setIcon(get32х16IconFromFontColorAndSize(fontColor, fontSize));
			
			//Generate event for recreating preview 
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, true);
			lsl.valueChanged(lse);
		}
		
		//Select font 
		if (aCommand.equalsIgnoreCase("fontCbx")) {
			System.out.println("font changed at:" +e.getWhen());
			//Generate event for recreating preview 
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, true);
			lsl.valueChanged(lse);
		}
		
		//Orientation changes
		if (aCommand.equalsIgnoreCase("orientCbx")) {
			//Generate event for recreating preview 
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, true);
			lsl.valueChanged(lse);
		}
		
	}
	
	/**
	 * Create 32 x 16 icon from size and color
	 * @param c - Font color
	 * @param size - Integer value for draw
	 * @return ImageIcon 32 x 16
	 */
	private ImageIcon get32х16IconFromFontColorAndSize(Color c, int size) {
		BufferedImage btnIcon = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = btnIcon.createGraphics();
		g2d.setRenderingHint(
			        RenderingHints.KEY_TEXT_ANTIALIASING,
			        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(c);
		g2d.setFont(new Font(null, Font.BOLD, 16));
		g2d.drawString(Integer.toString(size), 0, 14);
		g2d.dispose();
		return new ImageIcon(btnIcon);
	}
}
