package github.theobjop.ffmpegrenderer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class AvsWriter {
	
	public static File location;
	
	public static File CreateAVS(String out) throws Exception {
		location = new File("frameserver.avs");
		PrintWriter writer;
		try {
			writer = new PrintWriter(location);
			writer.println("AviSource(\"" + out + "\")");
			writer.close();
		} catch (FileNotFoundException e) {
			throw new Exception("Was not able to create frameserver.avs\nPlease run as administrator.");
		}
		
		return location;
	}
	
	public static void deleteFile() {
		if (location == null)
			return;
		if (location.exists())
			location.delete();
	}
}
