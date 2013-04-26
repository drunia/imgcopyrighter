package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;


/**
 * Settings program
 * @author drunia
 */
public class Settings {
	/**
	 * Name of settings file
	 */
	private static String fileName;
	
	/**
	 * Instance of settings
	 */
	private static Settings settings;
	
	/**
	 * Store for settings
	 */
	private LinkedHashMap<String, Object> store;
	
	/**
	 * Create/Read settings
	 */
	private Settings() {
		try {
			store = readSettings();
		} catch (Exception e) {
			e.printStackTrace();
			store = new LinkedHashMap<String, Object>();
		}
		settings = this;
	}
	
	/**
	 * Read object (class) from disk
	 * @return {@link LinkedHashMap}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private LinkedHashMap<String, Object> readSettings() throws Exception {
		File f = new File(fileName);
		if (!f.exists()) return new LinkedHashMap<String, Object>();
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
		Object readedObj = ois.readObject();
		ois.close();
		return (LinkedHashMap<String, Object>) readedObj;
	}
	
	/**
	 * Save object (class) to disk
	 * @throws Exception
	 */
	public void save() throws Exception {
		File f = new File(fileName);
		if (!f.exists()) f.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		oos.writeObject(store);
		oos.close();
	}
	
	/**
	 * Initialize settings 
	 * @return
	 */
	public static synchronized Settings getSettings(String fName) {
		fileName = fName;
		if (settings == null) {
			Settings s = new Settings();
			return s;
		} else return settings;
	}
	
	/**
	 * Read boolean value
	 * @param key - Name of key
	 * @return Boolean
	 */
	public boolean readBoolean(String key) {
		return (Boolean) ((store.containsKey(key)) ? store.get(key) : false); 
	}
	
	/**
	 * Read string value
	 * @param key - Name of key
	 * @return String
	 */
	public String readString(String key) {
		return (String) store.get(key);
	}
	
	/**
	 * Read integer value
	 * @param key - Name of key
	 * @return Integer
	 */
	public int readInt(String key) {
		return (Integer) store.get(key);
	}
	
	/**
	 * Read Object value
	 * @param key - Name of key
	 * @return Object
	 */
	public Object readObj(String key) {
		return store.get(key);
	}
	
	/**
	 * Write settins value of any type
	 * @param key - Name of setting key
	 * @param value - Object value
	 */
	public void write(String key, Object value) {
		store.put(key, value);
	}
}
