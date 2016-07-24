package github.theobjop.ffmpegrenderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JProgressBar;

public class ProgressGobbler extends StreamGobbler {

	InputStream is;
	String type;
	JProgressBar progBar;

	public ProgressGobbler(InputStream is, String type, JProgressBar progBar) {
		super(is, type);
		this.is = is;
		this.type = type;
		this.progBar = progBar;
	}

	@Override
    public void run() {
		try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
        		System.out.println(type + "> " + line);
            	Renderer.getConsole().append(line);
            	Render.progress(getFrameNum(line), getFPSProgress(line));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

	public int getFrameNum(String line) {
		String[] split = line.split(" +");
		for (int i = 0; i < split.length; i++) {
			if (split[i].equalsIgnoreCase("frame=")) {
				return Integer.parseInt(split[i+1]);
			}
		}
		return 0;
	}

	public int getFPSProgress(String line) {
		String[] split = line.split(" +");
		for (int i = 0; i < split.length; i++) {
			if (split[i].equalsIgnoreCase("fps=")) {
				return Integer.parseInt(split[i+1]);
			}
		}
		return 0;
	}
}
