package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.UUID;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

@SuppressWarnings("serial")
public class Render extends JPanel implements ActionListener, StreamCallback, UncaughtExceptionHandler {
	
	private static final String IN_OPT = "-i";
	
	String uniqueID;
	
	JButton renderBtn;
	JFileChooser openFile;
	ProgressStream progStream;
	int maxFrame;
	FrameRetriever fr;
	RenderProcess rp;
	
	ProcessBuilder pb;
	String inloc, exeloc, outloc;
	
	public Render() {
		Renderer.log.info("Creating Render.");
		this.setBounds(7, 456, 427, 35);
		
		uniqueID = UUID.randomUUID().toString();
		
		renderBtn = new JButton("Render");
		renderBtn.setBounds(357, 4, 77, 24);
		renderBtn.addActionListener(this);
		
		progStream = new ProgressStream();
		progStream.setStringPainted(true);
		progStream.setBounds(0, 4, 346, 24);
		
		openFile = new JFileChooser("Save");
		openFile.setDialogType(JFileChooser.SAVE_DIALOG);
		openFile.setFileFilter(new FileNameExtensionFilter("MP4 Files","mp4"));

		SpringLayout sl = new SpringLayout();
		sl.putConstraint(SpringLayout.NORTH, renderBtn, 4, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, progStream, 4, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.SOUTH, progStream, 0, SpringLayout.SOUTH, renderBtn);
		
		sl.putConstraint(SpringLayout.WEST, progStream, 0, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.EAST, progStream, -4, SpringLayout.WEST, renderBtn);
		sl.putConstraint(SpringLayout.EAST, renderBtn, 0, SpringLayout.EAST, this);
		
		this.setLayout(sl);
		
		this.add(progStream);
		this.add(renderBtn);
	}
	
	public void execute(String fileName) {
		Renderer.log.info("Executing with: " + fileName);
		if (!FilenameUtils.getExtension(fileName).equalsIgnoreCase("mp4")) {
			fileName += ".mp4";  // append .xml if "foo.jpg.mp4" is OK
		}

		// Attempt to create process with all the settings, if error then don't continue.
		try {
			//// Get all locations
			exeloc = Renderer.getExeLocation();
			inloc = AvsWriter.CreateAVS(Renderer.getStreamFile()).getAbsolutePath();
			outloc = fileName;
			///////////////////////////////////////////////////////////////
			
			//// Build Process
			String[] exe_input = { exeloc, IN_OPT, inloc, "-y" };
			String[] vid_aud = Renderer.getSettings().getRenderSettings();
			String[] def = DefaultSettings.getSettings(Renderer.getSettings().getFPS());
			String[] output = { outloc };
			
			String[] CMD_ARRAY;
			CMD_ARRAY = (String[])ArrayUtils.addAll(exe_input, vid_aud);
			CMD_ARRAY = (String[])ArrayUtils.addAll(CMD_ARRAY, def);
			CMD_ARRAY = (String[])ArrayUtils.addAll(CMD_ARRAY, output);
			
			pb = new ProcessBuilder(CMD_ARRAY);
			
			// Start the frame retriever
			// When it completes, the renderer will run. This is so the progress bar works.
			progStream.setString("Calculating how long video is...");
			fr = new FrameRetriever(this, inloc);
			fr.start();
		} catch (Exception err) {
			Renderer.log.log(Level.SEVERE, "Render_Execute", err);
			JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, err.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void reset() {
		Renderer.getRender().renderBtn.setEnabled(true);
		Renderer.getRender().progStream.setValue(0);
		Renderer.getRender().progStream.setMinimum(0);
		Renderer.getRender().progStream.setMaximum(0);
		Renderer.getRender().progStream.setString("0%");
	}
	
	// FrameRetriever has completed
	// Now we start the renderer
	@Override
	public void callback(String uid, Object o) {
		if (uid == "frameComplete") {
			try {
				// We'll pass it to the progStream too along with the FrameRetriever object so it can do what it needs to.
				progStream.callback("frameComplete", fr);
				
				//// Run the Renderer
				renderBtn.setEnabled(false);
				SettingsContainer.setElementsEnabled(false);
				rp = new RenderProcess(pb, progStream);
				rp.start();
			} catch (Exception err) {
				Renderer.log.log(Level.SEVERE, "Callback error", err);
				JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, err.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void stopProcesses() {
		if (rp != null)
			rp.kill();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		if (e.getSource() == renderBtn) {
			try {
				File f = new File(Renderer.getAbsoluteStreamLocation());
				String fileName = Renderer.getStreamFile();
				int returnVal = 0;
				if (f.isFile()) {
					returnVal = openFile.showSaveDialog(Renderer.FFMPEGRENDERER);
				
					if (returnVal == JFileChooser.APPROVE_OPTION)
						execute(openFile.getSelectedFile().getAbsolutePath());
				} else {
					execute(fileName);
				}
			} catch (Exception e1) {
				Renderer.log.log(Level.SEVERE, "Render_ActionPerformed", e1);
				JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, e1.getMessage());
			}
		}
	}


	@Override
	public void uncaughtException(Thread t, Throwable e) {
		Renderer.log.log(Level.SEVERE, "Render_uncaughtException", e);
	}
}

class RenderProcess extends Thread {
	ProcessBuilder pb;
	StreamCallback cb;
	Process process;
	public RenderProcess(ProcessBuilder pb, StreamCallback cb) {
		this.pb = pb;
		this.cb = cb;
	}
	
	public void kill() {
		if (process != null)
			process.destroyForcibly();
	}
	
	@Override
	public void run() {
		try {
			process = pb.start();
			
			// Create StreamGobbler to read the Process, use ProgressStream (JProgressBar) as a callback for the read.
			StreamGobbler streamGobbler = new StreamGobbler(cb, process.getErrorStream(), process.getInputStream());
			streamGobbler.start();
		} catch (IOException e1) {
			Renderer.log.log(Level.SEVERE, "Render_RenderProcess_run", e1);
			JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, "Invalid FFmpeg.exe location.", "Error.", JOptionPane.ERROR_MESSAGE);
		}		
	}
}
