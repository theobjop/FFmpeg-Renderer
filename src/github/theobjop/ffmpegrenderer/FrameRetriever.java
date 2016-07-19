package github.theobjop.ffmpegrenderer;
import java.io.IOException;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.apache.commons.io.FilenameUtils;

public class FrameRetriever extends SwingWorker<String, Object> {
	ProcessBuilder processBuilder;
	String finalOutput;
	
	JProgressBar progBar;
	String fps;
	
	RenderProcess proc;
	
	public FrameRetriever(RenderProcess proc, JProgressBar progBar, String fps, String exe, String avs) {
		this.proc = proc;
		this.progBar = progBar;
		this.fps = fps;
		exe = FilenameUtils.getFullPath(exe) + "ffprobe.exe";
		
		String[] cmd_arg = {
				exe,
				"-v", "error",
				"-show_entries", "format=duration",
				"-of", "default=noprint_wrappers=1:nokey=1",
				avs
		};
		
		processBuilder = new ProcessBuilder(cmd_arg);
	}

	@Override
	protected String doInBackground() throws Exception {
		try {
			Process p = processBuilder.start();
			
			StreamGobbler messageCollector = new StreamGobbler(p.getInputStream(), "ERROR", true);
			messageCollector.start();
			
			p.waitFor();
			finalOutput = messageCollector.getOutput();
		} catch (IOException e) {
			progBar.setString("Failed... skipping");
			throw new Exception("Could not find ffprobe.exe");
		}
		
		return finalOutput;
	}
	
	@Override
	protected void done() {
		if (finalOutput != null) {
			if (finalOutput.isEmpty()) {
				try {
					float f = Float.parseFloat(fps);
					float max = Float.parseFloat(finalOutput);
					System.out.println("MAX: " + max);
					progBar.setMaximum( (int)(f * max) );
					System.out.println("Frames: " + (int)(f*max));		
					progBar.setString( String.valueOf(progBar.getPercentComplete()*100) + "%" );
				} catch (Exception e) {
					progBar.setString("ERROR: " + e.getMessage());
				}
			}
		}
		proc.start();
	}
}
