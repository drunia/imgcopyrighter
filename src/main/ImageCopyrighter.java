package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;


public class ImageCopyrighter extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public final static String APP_VER = "1";
	private JPanel controlPanel;
	private JButton selectFilesBtn;
	private JButton doItBtn;
	private JTextField textField;
	private ImageList imgList;
	private JProgressBar progress;
	private JLabel infoLb;
	private FontComboBox fontCbx;
	private JComboBox orientCbx;
	private ImagePreview iPview;
	public Color fontColor = Color.RED;
	public int fontSize = 16;
	private Settings s;
	private String settingsFile = "icr.conf";
	private BufferedImage logo;
	public int alpha = 255;
	private JButton colorSizeBtn;
	private JButton logoBtn;
	private JSlider alphaSlider;
	private boolean changing;
	private File[] files;
	
	/**
	 * Start point
	 * @param args - array of parameters from command line 
	 */
	public static void main(String[] args) {
		/*
		 * Set Nimbus LookAndFeel if exist
		 */
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
	 * Create main menu 
	 * @return JMenuBar
	 */
	private JMenuBar createMainMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu settMenu = new JMenu("Настройки");
		settMenu.setIcon(null);
		JMenu questionMenu = new JMenu("?");
		questionMenu.setIcon(null);
		
		JCheckBoxMenuItem cbxUseIconedListMenuItem = new JCheckBoxMenuItem(
				"Иконизированый список (медленно)");
		cbxUseIconedListMenuItem.setSelected(s.readBoolean("useIconedList"));
		cbxUseIconedListMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem i = (JCheckBoxMenuItem) e.getSource();
				s.write("useIconedList", i.isSelected());
			}
		});

		JMenuItem aboutMenuItem = new JMenuItem("О программе", null);
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String aboutApp = "Программа для наложения " +
						"текста на изображения\nАвтор: Андрюнин Дмитрий (drunia)\n" +
						"Версия программы: " + APP_VER;
				JOptionPane.showMessageDialog(null, aboutApp, "О программе",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		settMenu.add(cbxUseIconedListMenuItem);
		questionMenu.add(aboutMenuItem);
		
		menuBar.add(settMenu);
		menuBar.add(questionMenu);
		return menuBar;
	}
	
	/**
	 * Default constructor
	 */
	public ImageCopyrighter() {
		super("ImageCopyright ver. " + APP_VER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);
		
		/*
		 * Init settings
		 */
		s = Settings.getSettings(settingsFile);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					s.save();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				super.windowClosing(e);
			}
		});
		
		/*
		 * MainMenu
		 */
		setJMenuBar(createMainMenu());
		
		JPanel mainPanel = new JPanel(); 
		mainPanel.setLayout(new BorderLayout(5, 5));
		
		Border grayBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		
		/*
		 * ImageList
		 */
		imgList = new ImageList();
		imgList.setImgIconed(s.readBoolean("useIconedList"));
		JScrollPane spane = new JScrollPane(imgList);
		spane.setBorder(grayBorder);
		spane.setPreferredSize(new Dimension(300, -1));
		mainPanel.add(spane, BorderLayout.WEST);
		
		/*
		 * Preview & controls panel
		 */
		JPanel prevControlPanel = new JPanel();
		prevControlPanel.setLayout(new BorderLayout(1, 5));
		
		/*
		 * Preview panel
		 */
	    iPview = new ImagePreview();
	    prevControlPanel.add(iPview, BorderLayout.CENTER);
	    
	    /*
	     * Control panel
	     */
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new GridBagLayout());
	    controlPanel.setBorder(grayBorder);
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    /*
	     * Select files
	     */
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

	    /*
	     * DoIt button
	     */
	    doItBtn = new JButton("Обработать все");
	    doItBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doIt(files);
			}
		});
	    doItBtn.setIcon(new ImageIcon(getClass().getResource("/res/start.gif")));
	    doItBtn.setVisible(true);
	    
	    JPanel doItPanel = new JPanel(new GridLayout(0, 2, 5, 5));
	    doItPanel.add(doItBtn);
	    doItPanel.add(selectFilesBtn);
	    
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.weightx = 0.7;
	    controlPanel.add(doItPanel, gbc);
	
	    /*
	     * Font
	     */
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
	    
	    colorSizeBtn = new JButton("Цвет и размер");
	    colorSizeBtn.setActionCommand("colorSizeBtn");
	    colorSizeBtn.addActionListener(this);
	    colorSizeBtn.setIcon(get32x16IconFromFontColorAndSize(fontColor, fontSize));
	    
	    fontPanel.add(fontCbx);
	    fontPanel.add(colorSizeBtn);
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.weightx = 0.7;
	    controlPanel.add(fontPanel, gbc);
	    
	    /*
	     * Text
	     */
	    JLabel textLb = new JLabel("Введите текст:");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.weightx = 1;
	    controlPanel.add(textLb, gbc);
	    
	    textField = new JTextField(100);
	    textField.addKeyListener(new KeyAdapter() {
	    	@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) return;
				//Generate event for recreating preview 
				ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
				ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
				lsl.valueChanged(lse);
			}
		});
	    textField.setText("Пример текста");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gbc.weightx = 0.7;
	    controlPanel.add(textField, gbc);
	    
	    /*
	     * Orientation
	     */
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
	    gbc.weightx = 0.7;
	    controlPanel.add(orientCbx, gbc);
	    
	    prevControlPanel.add(controlPanel, BorderLayout.SOUTH);
	    mainPanel.add(prevControlPanel);
	    add(mainPanel, BorderLayout.CENTER);
	    /*
	     * Logo
	     */
	    JLabel logoLb = new JLabel("Логотип (иконка):");
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gbc.weightx = 1;
	    controlPanel.add(logoLb, gbc);
	    
	    JPanel logoPanel = new JPanel(new GridLayout(0, 2, 3, 3));
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gbc.weightx = 0.7;
	    controlPanel.add(logoPanel, gbc);
	    
	    logoBtn = new JButton("Выбрать логотип");
	    logoBtn.setActionCommand("logoBtn");
	    logoBtn.addActionListener(this);
	    logoPanel.add(logoBtn);
	    
	    alphaSlider = new JSlider(0, 230, 0);
	    Hashtable<Integer, JLabel> lbTable = new Hashtable<Integer, JLabel>();
	    lbTable.put(115, new JLabel("Прозрачность"));
	    alphaSlider.setLabelTable(lbTable);
	    alphaSlider.setPaintLabels(true);
	    alphaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				alpha = 255 - slider.getValue();
				Color c = fontColor;
				fontColor = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
				colorSizeBtn.setIcon(get32x16IconFromFontColorAndSize(fontColor, fontSize));
				/*
				 * Alpha logo slow changes
				 */
				if (!changing) {
					changing = true;
					java.util.Timer changeTimer = new Timer();
					changeTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							changing = false;
							//Generate event for recreating preview 
							ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
							ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
							lsl.valueChanged(lse);
						}
					}, 0);
				}
			}
		});
	    logoPanel.add(alphaSlider);
	    
		/*
		 * Info panels
		 */
		JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 5));
		infoPanel.setPreferredSize(new Dimension(-1, 30));
		
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.X_AXIS));
	
		/*
		 * ProgressBar	
		 */
		progress = new JProgressBar(0, 100);
		progress.setVisible(false);
		infoPanel.add(progress);
		
		/*
		 * Label
		 */
		infoLb = new JLabel();
		infoLb.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
		msgPanel.add(infoLb);
		infoPanel.add(msgPanel);
		
		add(infoPanel, BorderLayout.SOUTH);
	    
	    /*
	     * ImageList selection handler
	     */
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
					/*
					 * Load preview in another thread
					 */
					SwingWorker<Void, Void> loader = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() {
							progress.setVisible(true);
							progress.setIndeterminate(true);
							progress.setStringPainted(false);
							infoLb.setText("Обработка ...");
							/*
							 * Set preview
							 */
							Font f = fontCbx.getSelectedFont(0, fontSize / 2);
							try {
								iPview.setPreview(img, logo, f, textField.getText(), orientCbx.getSelectedIndex(), fontColor);
							} catch (Exception e) {e.printStackTrace();}
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
	public void doIt(final File[] files) {
		if (changing || files == null || files.length == 0) return;
		changing = true;
		SwingWorker<Void, Void> doItWorker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				enableControls(false);
				progress.setVisible(true);
				progress.setIndeterminate(false);
				progress.setValue(0);
				infoLb.setText("Обработка списка ...");
				for (int i = 0; i < files.length; i++) {
					try {
						File saveFile = new File(files[i].getParent() + "/icr/" + files[i].getName());
						String ext = saveFile.getName().substring(saveFile.getName().lastIndexOf('.') + 1);
						saveFile.mkdirs();
						
						BufferedImage img = ImageIO.read(files[i]); 
						drawCopyRight(img);
						ImageIO.write(img, ext, saveFile);
						img.flush();
						int percents = (i + 1) * 100 / files.length;
						progress.setValue(percents);
						progress.setString("Обработано: " + percents + "% (" + (i + 1) + "/" + files.length + ")");
						progress.setStringPainted(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				progress.setVisible(false);
				progress.setString("");
				progress.setValue(0);
				progress.setStringPainted(false);
				String savePath = files[0].getParentFile().getAbsolutePath();
				if (!savePath.endsWith(File.separator)) 
					savePath += File.separator;
				infoLb.setText("Сохраненно в " + savePath + "icr" + File.separator);
				Thread.sleep(3000);
				infoLb.setText("");
				changing = false;
				enableControls(true);
				return null;
			}
		};
		doItWorker.execute();
	}
	
	/**
	 * Enamble/Disable controls while 
	 * program process images
	 * @param state - boolean state
	 */
	private void enableControls(boolean state) {
		imgList.setEnabled(state);
		selectFilesBtn.setEnabled(state);
		fontCbx.setEnabled(state);
		colorSizeBtn.setEnabled(state);
		orientCbx.setEnabled(state);
		textField.setEnabled(state);
		alphaSlider.setEnabled(state);
		logoBtn.setEnabled(state);
		doItBtn.setEnabled(state);
	}
	
	/**
	 * Draw text method
	 * @param img - BufferedImage
	 */
	private void drawCopyRight(BufferedImage img) {
		ImageConf imgConf = new ImageConf(img, orientCbx.getSelectedIndex());
		imgConf.setFont(fontCbx.getSelectedFont(0, fontSize));
		imgConf.setText(textField.getText());
		/*
		 * Draw copyright
		 */
		Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setFont(imgConf.getFont());
		if (logo != null) {
			imgConf.setLogo(logo);
			Point logoCoords = imgConf.getLogoCoords();
			g2d.drawImage(logo, logoCoords.x, logoCoords.y, null);
		}
		Point textCoords = imgConf.getTextCoords();
		g2d.setColor(fontColor);
		g2d.drawString(imgConf.getText(), textCoords.x, textCoords.y);
		g2d.dispose();
	}

	/**
	 * ActionEvent handler
	 * @param e - ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String aCommand = e.getActionCommand();
		/*
		 * Select files Buton
		 */
		if (aCommand.equalsIgnoreCase("selectFilesBtn")) {
			ImageFileChooser ifc = new ImageFileChooser(null);
			if (ifc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
				files = null;
				return;
			} else files = ifc.getSelectedFiles();
			
			imgList.setImgIconed(s.readBoolean("useIconedList"));
			progress.setVisible(true);
			progress.setStringPainted(false);
			progress.setIndeterminate(true);
			infoLb.setText("Загрузка изображений ...");
			
			SwingWorker<Void, Void> filesLoader = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() {
					enableControls(false);
					try {
						imgList.setElements(files);
					} catch (Exception e) {e.printStackTrace();}
					finally {
						progress.setIndeterminate(false);
						progress.setVisible(false);
						infoLb.setText("");
					}
					enableControls(true);
					return null;
				}
			};
			filesLoader.execute();
		}
		/*
		 * Select font size & color
		 */
		if (aCommand.equalsIgnoreCase("colorSizeBtn")) {
			JButton colorSizeButton = (JButton) e.getSource();
			new FontColorSizeDialog(this, fontColor, fontSize);
			colorSizeButton.setIcon(get32x16IconFromFontColorAndSize(fontColor, fontSize));
			/*
			 * Generate event for recreating preview 
			 */
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
			lsl.valueChanged(lse);
		}
		/*
		 * Select font 
		 */
		if (aCommand.equalsIgnoreCase("fontCbx")) {
			/*
			 * Generate event for recreating preview 
			 */
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
			lsl.valueChanged(lse);
		}
		/*
		 * Orientation changes
		 */
		if (aCommand.equalsIgnoreCase("orientCbx")) {
			/*
			 * Generate event for recreating preview 
			 */
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
			lsl.valueChanged(lse);
		}
		/*
		 * Select logo
		 */
		if (aCommand.equalsIgnoreCase("logoBtn")) {
			JButton logoBtn = (JButton) e.getSource();
			ImageFileChooser ifc = new ImageFileChooser(null);
			ifc.setMultiSelectionEnabled(false);
			if (ifc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				try {
					logo = ImageIO.read(ifc.getSelectedFile());
					ImageIcon icon = new ImageIcon(logo.getScaledInstance(24, 24, Image.SCALE_FAST));
					logoBtn.setIcon(icon);
					logoBtn.setText(ifc.getSelectedFile().getName());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				logoBtn.setIcon(null);
				logoBtn.setText("Выбрать логотип");
				logo = null;
			}
			/*
			 * Generate event for recreating preview 
			 */
			ListSelectionListener lsl = imgList.getListSelectionListeners()[0];
			ListSelectionEvent lse = new ListSelectionEvent(imgList, 0, 0, false);
			lsl.valueChanged(lse);
		}
	}
	
	/**
	 * Create 32 x 16 icon from size and color
	 * @param c - Font color
	 * @param size - Integer value for draw
	 * @return ImageIcon 32 x 16
	 */
	private ImageIcon get32x16IconFromFontColorAndSize(Color c, int size) {
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
