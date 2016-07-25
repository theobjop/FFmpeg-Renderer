package github.theobjop.ffmpegrenderer;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.commons.lang3.StringUtils;

// Purpose: Create JProgressBar and update when the StreamGobbler comes back with a new frame count.
// Stores total frames that it needs to go through.
// Updates when it receives new info from the Stream.

@SuppressWarnings("serial")
public class ProgressStream extends JProgressBar implements StreamCallback, UncaughtExceptionHandler {
	
	FrameRetriever fr;
	String avs;
	
	// For NULL purposes (isSet())
	Integer frames;
	
	Process p;

	@Override
	// This is a callback for ALL of the Render process' callback
	public void callback(String uniqueIdentifier, Object obj) {
		
		Console.write("Callback: " + uniqueIdentifier);
		
		try {
			if (uniqueIdentifier == "frameComplete") {
				FrameRetriever fr = obj instanceof FrameRetriever ? (FrameRetriever) obj : null;
				if (fr != null) {
					float f = Float.parseFloat(fr.getOutput().replaceAll("[^0-9.]", ""));
					int fps = Integer.parseInt(VideoSettings.getFPS());
					
					// F is the time in seconds that we got from FFprobe.. Multiply that by the FPS given, we should have total amount of frames or REALLY close to it.
					this.setMaximum((int)((float)fps * f));
					this.setString(((this.getValue() / this.getMaximum()) * 100) + "%");
				}
			} else {
				String line = (String) obj;
				// Checks the callback message for frame or fps, if so, we have to up the progress.
				if (StringUtils.contains(line, "frame=") && StringUtils.contains(line, "fps=")) {
					Console.write("Passed \"Frame=\" and \"fps=\" check.");
					
					// Finds the fps
					int frameInd = StringUtils.indexOf(line, "frame=");
					int fpsInd = StringUtils.indexOf(line, "fps=");
					
					// Gets the values, one is between "frame= fps=" other is between "fps= q="
					String frameStr = StringUtils.substring(line, frameInd+6, fpsInd).trim();
					String fpsStr = StringUtils.substring(line, fpsInd+4, StringUtils.indexOf(line, "q=")).trim();
					int frame = (int)Double.parseDouble(frameStr.replaceAll(".,", ""));
					int fps = (int)Double.parseDouble(fpsStr.replaceAll(".,", ""));
					progress(frame, fps);
				}
			}
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			Console.write("Error: ");
			for (int i = 0; i < ste.length; i++) {
				Console.append(ste[i].toString());
			}
		}
	}
	
	public void progress(int frame, int fps) {
		if (frame >= this.getMaximum()) {
			this.setString("DONE!");
			Renderer.FFMPEGRENDERER.setTitle("FFmpeg Renderer");
			Renderer.getRender().renderBtn.setEnabled(true);
			SettingsContainer.enableElements();
			return;
		}
		this.setString(String.format("%.2f", this.getPercentComplete()*100) + "%");
		this.setValue(frame);
		Renderer.FFMPEGRENDERER.setTitle("FFmpeg Renderer - Estimated Time: " + timeConversion(fps));
	}
	
	private String timeConversion(int fps) {
		if (fps == 0)
			return "";
		int totalSeconds = (this.getMaximum() - this.getValue()) / fps;
	    int seconds = totalSeconds % 60;
	    int totalMinutes = totalSeconds / 60;
	    int minutes = totalMinutes % 60;
	    int hours = totalMinutes / 60;
	    return (hours > 0 ? hours + " hours " : "") + (minutes > 0 || totalSeconds > 60 ? minutes + " minutes, " : "") + seconds + " seconds";
	}

	// This happens when the thread we created throws an error.
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, "Something happened to the FFprobe.exe");
	}
}
