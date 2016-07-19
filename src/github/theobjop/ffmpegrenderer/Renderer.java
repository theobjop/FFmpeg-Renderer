package github.theobjop.ffmpegrenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Renderer implements WindowListener {
	
	public static Renderer $Renderer;
	
	public static JFrame FFMPEGRENDERER;
	public static Font font;
	public static FontRenderContext frc;
	
	private Finder finderContainer;
	private SettingsContainer settingsContainer;
	private Console console;
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
		FFMPEGRENDERER.addWindowListener(this);
		FFMPEGRENDERER.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FFMPEGRENDERER.setLayout(null);
		FFMPEGRENDERER.setSize(new Dimension(448, 512));

		Renderer.appendLog(".. Finished\n... Creating Classic XP Style.");	
		
		//// Create Styling standards for the entire application
		font = new Font("Tahoma", Font.PLAIN, 12);
		AffineTransform affinetransform = new AffineTransform();
		frc = new FontRenderContext(affinetransform, true, true);
		
		// Set UI to look like Windows Classic because Java's GUI is a pain-in-the-ass.
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			System.out.println("User is not on Windows OS. Some UI elements might be buggy and out of place.");
		}		
		setUIFont (new javax.swing.plaf.FontUIResource(font));
		
		Renderer.appendLog(".. Finished.\n... Creating Singletons.");
		
		//// Get settings after styling because the settings hold the JComponents
		PropertiesWriter.CreateSingleton();
		VideoSettings.CreateSingleton();
		AudioSettings.CreateSingleton();
		

		Renderer.appendLog(".. Finished.\n... Creating Panels.");
		
		//// Create the "FFmpeg Location: <text box> [Browse]" GUI
		finderContainer = new Finder(PropertiesWriter.get("Exe"), PropertiesWriter.get("Avi"));

		//// Create SettingsContainer
		settingsContainer = new SettingsContainer();
		
		//// Create the Console container		
		JPanel consoleContainer = new JPanel();
		consoleContainer.setBounds(0,340,441,116);
		consoleContainer.setLayout(new BorderLayout());
		console = new Console();
		consoleContainer.add(console, BorderLayout.CENTER);
		
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
	
	public Console getConsole() {
		return console;
	}
	
	public Render getRender() {
		return renderContainer;
	}
	
	public void destroy() {
		AvsWriter.deleteFile();
		
		if (RenderProcess.process != null) {
			if (RenderProcess.process.isAlive())
				RenderProcess.process.destroyForcibly();
		}
		
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
