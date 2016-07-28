package github.theobjop.ffmpegrenderer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;

class Preset {
	File file;
	String type;
	Class<? extends Settings> _class;
	
	public Preset(File f, String t, Class<? extends Settings> cls) {
		this.file = f;
		this.type = t;
		this._class = cls;
	}
	
	// attempt to create preset out of just the class
	public Preset(Class<? extends Settings> cls) {
		try {
			this.file = (File)cls.getDeclaredField("FILE").get(null);
			this.type = (String)cls.getDeclaredField("TYPE").get(null);
			this._class = cls;
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public Class<? extends Settings> getSettingsClass() {
		return this._class;
	}
	
	@Override
	public String toString() {
		return "Class: " + _class.getCanonicalName() + "\n"
				+ "File: " + file.getPath() + "\n"
				+ "Type: " + type;
	}
}

public class PropertiesWriter {
	// File constants
	private static final File LASTSAVE = new File(Renderer.USER_HOME + "\\.ffmpegrenderer_lastsave");
	private static final File LOC_SAVE = new File(Renderer.USER_HOME + "\\.ffmpeg_loc");

	private static final Properties lastSaveProperties = new Properties();
	private static final Properties finderProperties = new Properties();
	private static Properties settingsProperties = new Properties();
	
	public static final Preset x264Preset = new Preset(x264Settings.FILE, x264Settings.TYPE, x264Settings.class);
	public static final Preset YoutubePreset = new Preset(YoutubeSettings.FILE, YoutubeSettings.TYPE, YoutubeSettings.class);
	
	// This is for iteration
	private static final Preset[] presetCollection = { x264Preset, YoutubePreset };
	
	private static Preset currentPreset;
	
	public static boolean LoadProperties(Preset p) {
		Renderer.log.info("Loading preset: " + p.toString());		
		currentPreset = p;
		
		try {
			// If it has been created, then the properties were not loaded.
			boolean retVal = !currentPreset.file.createNewFile();
			settingsProperties = new Properties();
			FileInputStream fis = new FileInputStream(currentPreset.file);
			settingsProperties.load(fis);
			fis.close();
			putLastPreset();
			Renderer.log.info("Was file created: " + String.valueOf(retVal));
			return retVal;
		} catch (Exception e) {
			// Properties weren't loaded.
			Renderer.log.log(Level.SEVERE, "LoadProperties", e);
			return false;
		}
	}
	
	// Another way of calling LoadProperties, instead uses file and retrieves it from Presets
	public static boolean LoadProperties(File f) {
		return LoadProperties(getPreset(f));
	}
	
	// Called whenever properties file is loaded.
	public static void putLastPreset() {
		Renderer.log.info("Saving last preset settings.");
		try {
			LASTSAVE.createNewFile();
			FileInputStream fs = new FileInputStream(LASTSAVE);
			lastSaveProperties.load(fs);
			fs.close();
			
			if (currentPreset == null)
				currentPreset = x264Preset;
			
			lastSaveProperties.put("preset", currentPreset.type);
			OutputStream out = new FileOutputStream(LASTSAVE);
			lastSaveProperties.store(out, "PutLastPreset");
			out.close();
		} catch (IOException e) {
			Renderer.log.log(Level.SEVERE, "PutLastPreset", e);
		}
	}
	
	public static Preset getLastPreset() {
		Renderer.log.info("Getting last preset.");
		try {
			LASTSAVE.createNewFile();
			FileInputStream fs = new FileInputStream(LASTSAVE);
			lastSaveProperties.load(fs);
			String prestName = lastSaveProperties.getProperty("preset");
			fs.close();
			
			if (YoutubePreset.type.equalsIgnoreCase(prestName)) {
				return YoutubePreset;
			}
		} catch (IOException e) {
			Renderer.log.log(Level.SEVERE, "getLastPreset", e);
		}
		return x264Preset;
	}
	
	public static void saveLocations() {
		String exeLoc = "", aviLoc = "";
		
		try { exeLoc = Renderer.getExeLocation();
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "saveLocations", e);
			exeLoc = "";
		}
		try { aviLoc = Renderer.getAbsoluteStreamLocation();
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "saveLocations", e);
			aviLoc = "";
		}
		
		try {
			OutputStream os = new FileOutputStream(LOC_SAVE);
			finderProperties.setProperty("Exe", exeLoc);
			finderProperties.setProperty("Avi", aviLoc);
			finderProperties.store(os, "EXESAVE");
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "saveLocations", e);
		}
	}
	
	public static void loadLocations() {
		Renderer.log.info("Loading Locations for Finder.");
		try {
			LOC_SAVE.createNewFile();
			finderProperties.load(new FileInputStream(LOC_SAVE));
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "Error trying to load save locations for finder.", e);
		}
	}
	
	public static void save() {
		save(Renderer.getSettings());
	}
	
	public static void save(Settings s) {
		Renderer.log.info("Saving properties.");
		try {
			settingsProperties.putAll(s.getSaveSettings());
			OutputStream out = new FileOutputStream(currentPreset.file);
			settingsProperties.store(out, "Settings");
			out.close();
			Renderer.log.info("Saved.\n");
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "Error during save process: " + e.getMessage() + "\n", e);
		}
	}
	
	public static Preset getPreset(Object o) {
		Renderer.log.info("Getting preset using Settings");
		for (int i = 0; i < presetCollection.length; i++) {
			if (presetCollection[i].type == o
			 || presetCollection[i].getSettingsClass() == o
			 || presetCollection[i] == o
			 || presetCollection[i].file == o
			 || presetCollection[i].getSettingsClass() == o.getClass())
				return presetCollection[i];
		}
		
		return x264Preset;
	}
	
	public static String get(String str) { 
		return settingsProperties.getProperty(str);
	}
	
	public static String getExeLocation() {
		return finderProperties.getProperty("Exe");
	}
	
	public static String getAviLocation() {
		return finderProperties.getProperty("Avi");
	}
}
