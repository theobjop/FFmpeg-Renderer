package github.theobjop.ffmpegrenderer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Renderer implements WindowListener {

	public static final String USER_HOME = System.getProperty("user.home");
	
	// Main JFrame and singleton
	public static Renderer $Renderer;
	public static JFrame FFMPEGRENDERER;

	// Styling
	private static FontRenderContext frc;
	private static FontUIResource fur;
	
	// Swing components inside the frame
	private Console consoleContainer;
	private Finder finderContainer;
	private SettingsContainer settingsContainer;
	private Render renderContainer;
	
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
		Renderer.log("Starting FFmpeg Renderer.\n");
		$Renderer = new Renderer();
	}
	
	private Renderer() {
		
		Renderer.log("Creating main frame.");
		FFMPEGRENDERER = new JFrame("FFmpeg Renderer");
		FFMPEGRENDERER.setLayout(null);
		FFMPEGRENDERER.setSize(new Dimension(457, 530));
		FFMPEGRENDERER.addWindowListener(this);
		FFMPEGRENDERER.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Renderer.appendLog(".. Finished.\nCreating Style.");
		fur = new FontUIResource("Arial", Font.PLAIN, 12);
		frc = new FontRenderContext(fur.getTransform(), true, true);
		setUIFont(fur);
		
		Renderer.appendLog(".. Finished.\nCreating Singletons.");
		//// Get settings after styling because the settings hold the JComponents
		PropertiesWriter.CreateSingleton();
		VideoSettings.CreateSingleton();
		AudioSettings.CreateSingleton();

		Renderer.appendLog(".. Finished.\nCreating Panels.");
		
		//// Create Finder
		finderContainer = new Finder(PropertiesWriter.get("Exe"), PropertiesWriter.get("Avi"));

		//// Create SettingsContainer
		settingsContainer = new SettingsContainer();
		
		//// Create the Console container
		consoleContainer = new Console();
		
		//// Create the Render container...... any more of this shit and I'm going to do something harmful.
		renderContainer = new Render();
		
		//// Finish by adding panels to the container and making frame
		FFMPEGRENDERER.add(finderContainer);
		FFMPEGRENDERER.add(settingsContainer);
		FFMPEGRENDERER.add(consoleContainer);
		FFMPEGRENDERER.add(renderContainer);
		
		FFMPEGRENDERER.setVisible(true);
		
		Renderer.appendLog(".. Finished!\n");
		Renderer.log("Frame created.\n");
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
	
	public static String getLocation() throws Exception {
		return $Renderer.finderContainer.getLoc();
	}
	
	public static String getStreamFile() throws Exception {
		return $Renderer.finderContainer.getStreamFile();
	}
	
	public static String getAbsoluteStreamLocation() throws Exception {
		return $Renderer.finderContainer.getAbsoluteStreamLocation();
	}
	
	public static Console getConsole() {
		return getInstance().consoleContainer;
	}
	
	public static Render getRender() {
		return getInstance().renderContainer;
	}
	
	public static Finder getFinder() {
		return getInstance().finderContainer;
	}
	
	public void destroy() {
		AvsWriter.deleteFile();
		renderContainer.stopProcesses();
		PropertiesWriter.save();
	}
	
	public static void log(Object o) {
		System.out.print("[FFmpeg Renderer] " + o);
	}
	
	public static void appendLog(Object o) {
		System.out.print(o);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) { }
	public void windowClosed(WindowEvent arg0) {
		Renderer.log("Window Closed.\n");
		destroy();
	}
	public void windowClosing(WindowEvent arg0) {
		Renderer.log("Window Closing.\n");
		destroy();
	}
	public void windowDeactivated(WindowEvent arg0) { }
	public void windowDeiconified(WindowEvent arg0) { }
	public void windowIconified(WindowEvent arg0) {	}
	public void windowOpened(WindowEvent arg0) { }

}
