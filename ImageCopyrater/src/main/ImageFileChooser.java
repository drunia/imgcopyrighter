package main;

import java.io.File;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
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
	 * Localize to russian language JFileChooser GUI
	 * @param choose - JFileChooser
	 * @return - Localized to russian JFileChooser
	 */
	private JFileChooser setUpdateUI(JFileChooser choose) {
		UIManager.put("FileChooser.openButtonText", "�������");
		UIManager.put("FileChooser.cancelButtonText", "������");
		UIManager.put("FileChooser.lookInLabelText", "�������� �");
		UIManager.put("FileChooser.fileNameLabelText", "��� �����");
		UIManager.put("FileChooser.filesOfTypeLabelText", "��� �����");

		UIManager.put("FileChooser.saveButtonText", "���������");
		UIManager.put("FileChooser.saveButtonToolTipText", "���������");
		UIManager.put("FileChooser.openButtonText", "�������");
		UIManager.put("FileChooser.openButtonToolTipText", "�������");
		UIManager.put("FileChooser.cancelButtonText", "������");
		UIManager.put("FileChooser.cancelButtonToolTipText", "������");

		UIManager.put("FileChooser.lookInLabelText", "�����");
		UIManager.put("FileChooser.saveInLabelText", "�����");
		UIManager.put("FileChooser.fileNameLabelText", "��� �����");
		UIManager.put("FileChooser.filesOfTypeLabelText", "��� ������");

		UIManager.put("FileChooser.upFolderToolTipText", "�� ���� ������� �����");
		UIManager.put("FileChooser.newFolderToolTipText", "�������� ����� �����");
		UIManager.put("FileChooser.listViewButtonToolTipText", "������");
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "�������");
		UIManager.put("FileChooser.fileNameHeaderText", "���");
		UIManager.put("FileChooser.fileSizeHeaderText", "������");
		UIManager.put("FileChooser.fileTypeHeaderText", "���");
		UIManager.put("FileChooser.fileDateHeaderText", "�������");
		UIManager.put("FileChooser.fileAttrHeaderText", "��������");

		UIManager.put("FileChooser.acceptAllFileFilterText", "��� �����");
		choose.updateUI();

		return choose;
	}
	
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
			return "ImageCopyrighter files: " + Arrays.toString(exts);
		}
	}
	
	/**
	 * Default constructor
	 */
	public ImageFileChooser(String[] exts) {
		super();
		if (exts != null) this.exts = exts;
		setMultiSelectionEnabled(true);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new ImageFilter());
		setUpdateUI(this);
	}

}
