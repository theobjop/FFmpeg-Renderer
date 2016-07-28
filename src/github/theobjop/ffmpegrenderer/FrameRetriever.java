package github.theobjop.ffmpegrenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;

public class FrameRetriever extends Thread {
	
	File sharedFile = new File(Renderer.USER_HOME, ".frames");
	
	StreamCallback cb;
	ProcessBuilder processBuilder;
	Process p;
	
	public FrameRetriever(StreamCallback cb, String avs) throws Exception {
		Renderer.log.info("Retrieving frames.");
		this.cb = cb;
		String exe;
		try {
			exe = Renderer.getExeLocation();
			exe = FilenameUtils.getFullPath(exe) + "ffprobe.exe";
		
			String[] cmd_arg = {
					exe,
					"-v", "error",
					"-show_entries", "format=duration",
					"-of", "default=noprint_wrappers=1:nokey=1",
					avs
			};
			
			processBuilder = new ProcessBuilder(cmd_arg);
			processBuilder.redirectErrorStream(true);
			processBuilder.redirectOutput(Redirect.to(sharedFile));
		} catch (Exception e) {
			throw new Exception(e.getCause().toString());
		}
	}

	@Override
	public void run() {
		try {
			p = processBuilder.start();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Unable to start Process thread or create StreamGobbler.");
		}
		
		cb.callback("frameComplete", "");
	}
	
	public String getOutput() throws Exception {
		try {
			// Lots of conversions
			/// Gets path to tempFile
			/// Reads all the lines into list
			/// Converts list to single string (It's only one line long...)
			if (sharedFile.exists()) {
				String ret = Arrays.toString(Files.readAllLines(Paths.get(sharedFile.toURI())).toArray());
				sharedFile.delete();
				return ret;
			} return null;
		} catch (FileNotFoundException e) {
			throw e;
		}
	}
}
