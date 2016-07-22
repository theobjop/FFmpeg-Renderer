package github.theobjop.ffmpegrenderer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesWriter {
	
	// The one and only
	@SuppressWarnings("unused")
	private static PropertiesWriter SINGLETON;
	private static Properties PROP_SINGLETON;
	
	// File constant
	public static final File FILE = new File(Renderer.USER_HOME + "\\opt.ini");
	
	public static void CreateSingleton() {
		PROP_SINGLETON = new Properties();
		SINGLETON = new PropertiesWriter();
	}
	
	private PropertiesWriter() {
		try {
			FileInputStream fis = new FileInputStream(FILE);
			PROP_SINGLETON.load(fis);
			fis.close();
		} catch (Exception e1) {
			Renderer.log("Locations file not found. No big deal. Will recreate.\n");
			createDefault();
		}
	}
	
	public static void createDefault() {
		Renderer.log("Creating Default.\n");
		try {
			PROP_SINGLETON.clear();
			PROP_SINGLETON.setProperty("Exe", "");
			PROP_SINGLETON.setProperty("Avi", "");
			PROP_SINGLETON.putAll(VideoSettings.getDefaultSettings());
			PROP_SINGLETON.putAll(AudioSettings.getDefaultSettings());
			OutputStream out = new FileOutputStream(FILE);
			PROP_SINGLETON.store(out, "Settings");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		String exeLoc = "", aviLoc = "";
		
		try { exeLoc = Renderer.getLocation();
		} catch (Exception e) {
			exeLoc = "";
		}
		try { aviLoc = Renderer.getAbsoluteStreamLocation();
		} catch (Exception e) {
			aviLoc = "";
		}
		
		try {
			PROP_SINGLETON.setProperty("Exe", exeLoc);
			PROP_SINGLETON.setProperty("Avi", aviLoc);
			PROP_SINGLETON.putAll(VideoSettings.getSaveSettings());
			PROP_SINGLETON.putAll(AudioSettings.getSaveSettings());
			OutputStream out = new FileOutputStream(FILE);
			PROP_SINGLETON.store(out, "Settings");
			out.close();
		} catch (Exception e) {
			Renderer.log("Somethings wrong: " + e.getMessage() + "\n");
		}
		Renderer.log("Saved.\n");
	}
	
	public static String get(String str) { 
		return PROP_SINGLETON.getProperty(str);
	}
}
