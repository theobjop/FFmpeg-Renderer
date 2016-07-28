package github.theobjop.ffmpegrenderer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.io.FilenameUtils;

public class Renderer implements WindowListener {

	public static final String USER_HOME = System.getProperty("user.home");
	
	// Create logger for storing errors in a log file, makes it easier
	private static final String LOG_FILE = USER_HOME + "\\ffmpeg.log";
	public static final Logger log = Logger.getLogger("FFMPEGRENDERER");
	
	// Main JFrame and singleton
	public static Renderer $Renderer;
	public static JFrame FFMPEGRENDERER;

	// Styling
	private static FontRenderContext frc;
	private static FontUIResource fur;
	
	// Swing components inside the frame
	private static Console consoleContainer;
	private static Finder finderContainer;
	private static PresetSelector presetSelector;
	private static SettingsContainer settingsContainer;
	private static Render renderContainer;
	
    private static Settings settings;
	
	// Leeched from Romain Hippeau from Stackoverflow.com
	@SuppressWarnings("rawtypes")
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
		java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value != null && value instanceof javax.swing.plaf.FontUIResource)
	    	  UIManager.put (key, f);
    	}
	}
	
	public static void main(String[] args) {
		try {
			log.setLevel(Level.INFO);
			SimpleFormatter sf = new SimpleFormatter();
			FileHandler fh = new FileHandler(LOG_FILE);
			fh.setFormatter(sf);
			log.addHandler(fh);
			
			Renderer.log.info("Starting FFmpeg Renderer.\n");
			$Renderer = new Renderer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Renderer() {
		FFMPEGRENDERER = new JFrame("FFmpeg Renderer");
		FFMPEGRENDERER.setLayout(null);
		FFMPEGRENDERER.setSize(new Dimension(457, 530));
		FFMPEGRENDERER.addWindowListener(this);
		FFMPEGRENDERER.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fur = new FontUIResource("Arial", Font.PLAIN, 12);
		frc = new FontRenderContext(fur.getTransform(), true, true);
		setUIFont(fur);

		//// Create Finder
		finderContainer = new Finder();
		
		//// Create SettingsContainer
		settingsContainer = new SettingsContainer();
		presetSelector = new PresetSelector();

		//// Create the Console container
		consoleContainer = new Console();

		//// Create the Render container...... any more of this shit and I'm going to do something harmful.
		renderContainer = new Render();

		//// Finish by adding panels to the container and making frame
		FFMPEGRENDERER.add(finderContainer);
		FFMPEGRENDERER.add(presetSelector.getParent());
		FFMPEGRENDERER.add(settingsContainer);
		FFMPEGRENDERER.add(consoleContainer);
		FFMPEGRENDERER.add(renderContainer);

		FFMPEGRENDERER.setVisible(true);
		
		try {
			SetSettings(PropertiesWriter.getLastPreset().getSettingsClass());
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "SetSettings", e);
		}
	}

	public static void LoadPreset(String preset) {
		try {
			SetSettings(PropertiesWriter.getPreset(preset).getSettingsClass());
		} catch (Exception e) {
			Renderer.log.log(Level.SEVERE, "LoadPreset", e);
		}
	}
	
	public static void SetSettings(Class<? extends Settings> c) throws Exception {
		if (settings != null) {
			PropertiesWriter.save();
			getSettingsContainer().removeAll();
		}
		SetSettings(c.newInstance());
	}
	
	public static void SetSettings(Settings set) throws Exception {
		if (settings != null) {
			PropertiesWriter.save();
			getSettingsContainer().removeAll();
        }
		
		settings = set;
        getSettingsContainer().addSettings(settings);
    }
	
	//// Getters	
    public static Settings getSettings() {
        return settings;
    }
	
    public static FontRenderContext getFontRenderContext() {
		return frc;
	}
	
	public static FontUIResource getFontUIResource() {
		return fur;
	}
	
	public static Rectangle2D getStringBounds(String str) {
		return fur.getStringBounds(str, frc);
	}
	
	public static Renderer getInstance() {
		return $Renderer;
	}
	
	public static String getExeLocation() throws Exception {
		// Easier than making a check "ifDirectory or exists"
		// Just remove the exe at the end of the link
		// doesn't even matter if they chose some bullshit other exe
		// as long as FFmpeg.exe is inside.
		return getFinder().getLoc();
	}
	
	public static String getStreamFile() throws Exception {
		return getFinder().getStreamFile();
	}
	
	public static String getAbsoluteStreamLocation() throws Exception {
		return getFinder().getAbsoluteStreamLocation();
	}
	
	public static Console getConsole() {
		return consoleContainer;
	}
	
	public static Render getRender() {
		return renderContainer;
	}
	
	public static Finder getFinder() {
		return finderContainer;
	}
	
	public static SettingsContainer getSettingsContainer() {
        return settingsContainer;
    }
    
	public void destroy() {
		AvsWriter.deleteFile();
		renderContainer.stopProcesses();
		PropertiesWriter.putLastPreset();
		PropertiesWriter.saveLocations();
		PropertiesWriter.save();
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) { }
	public void windowClosed(WindowEvent arg0) {
		Renderer.log.info("Window Closed.\n");
		destroy();
	}
	public void windowClosing(WindowEvent arg0) {
		Renderer.log.info("Window Closing.\n");
		destroy();
	}
	public void windowDeactivated(WindowEvent arg0) { }
	public void windowDeiconified(WindowEvent arg0) { }
	public void windowIconified(WindowEvent arg0) {	}
	public void windowOpened(WindowEvent arg0) { }

}
