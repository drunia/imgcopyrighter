package main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ImageFileChooser extends JFileChooser {
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -2799438855523865110L;
	
	/**
	 * Array of assigned extensions
	 */
	private String[] exts = {"jpg", "jpeg", "png"};
	
	/**
	 * Class - filter for image extensions
	 * @author drunia
	 */
	private class ImageFilter extends FileFilter {
		/**
		 * Check compatible files
		 */
		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) return true;
			for (int i = 0; i < exts.length; i++) {
				if (f.getName().toLowerCase().endsWith(exts[i])) 
					return true;
			}
			return false;
		}

		/**
		 * Return ext description
		 */
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "ImageCopyrighter files";
		}
	}
	
	/**
	 * Default constructor
	 */
	public ImageFileChooser() {
		super();
		setFileFilter(new ImageFilter());
	}

}
