package github.theobjop.ffmpegrenderer;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

class RenderProcess extends Thread implements Runnable {

	public static Process process;
	private static ProcessBuilder pb;
	
	private static final String IN_OPT = "-i";
	
	public RenderProcess(String exeloc, String inloc, String outloc) throws Exception {
		// To make this easier on me... -y is overwrite
		String[] exe_input = { exeloc, IN_OPT, inloc, "-y" };
		String[] vid = VideoSettings.getRenderSettings();		
		String[] def = DefaultSettings.getSettings(VideoSettings.getFPS());
		String[] aud = AudioSettings.getRenderSettings();
		String[] output = { outloc };
		
		String[] CMD_ARRAY;
		CMD_ARRAY = (String[])ArrayUtils.addAll(exe_input, vid);
		CMD_ARRAY = (String[])ArrayUtils.addAll(CMD_ARRAY, def);
		CMD_ARRAY = (String[])ArrayUtils.addAll(CMD_ARRAY, aud);
		CMD_ARRAY = (String[])ArrayUtils.addAll(CMD_ARRAY, output);
		
		pb = new ProcessBuilder(CMD_ARRAY);
	}
	
	@Override
	public void run() {
	// For the testing purposes I'll just do this.
		try {
			process = pb.start();
			
			ProgressGobbler progGobbler = new ProgressGobbler(process.getErrorStream(), "ERROR", Renderer.getInstance().getRender().progBar);
			progGobbler.start();
			
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
			outputGobbler.start();
			
			process.waitFor();		
		} catch (IOException | InterruptedException e1) {
			JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, "Invalid FFmpeg.exe location.", "Error.", JOptionPane.ERROR_MESSAGE);
			Render.reset();
		}
	}
}