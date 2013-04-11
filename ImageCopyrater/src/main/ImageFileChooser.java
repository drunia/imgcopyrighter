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
		UIManager.put("FileChooser.openButtonText", "Открыть");
		UIManager.put("FileChooser.cancelButtonText", "Отмена");
		UIManager.put("FileChooser.lookInLabelText", "Смотреть в");
		UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файла");

		UIManager.put("FileChooser.saveButtonText", "Сохранить");
		UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
		UIManager.put("FileChooser.openButtonText", "Открыть");
		UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
		UIManager.put("FileChooser.cancelButtonText", "Отмена");
		UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");

		UIManager.put("FileChooser.lookInLabelText", "Папка");
		UIManager.put("FileChooser.saveInLabelText", "Папка");
		UIManager.put("FileChooser.fileNameLabelText", "Имя файла");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Тип файлов");

		UIManager.put("FileChooser.upFolderToolTipText", "На один уровень вверх");
		UIManager.put("FileChooser.newFolderToolTipText", "Создание новой папки");
		UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
		UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
		UIManager.put("FileChooser.fileNameHeaderText", "Имя");
		UIManager.put("FileChooser.fileSizeHeaderText", "Размер");
		UIManager.put("FileChooser.fileTypeHeaderText", "Тип");
		UIManager.put("FileChooser.fileDateHeaderText", "Изменен");
		UIManager.put("FileChooser.fileAttrHeaderText", "Атрибуты");

		UIManager.put("FileChooser.acceptAllFileFilterText", "Все файлы");
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
