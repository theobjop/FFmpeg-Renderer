package github.theobjop.ffmpegrenderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
     
	InputStream is;
	String type;
	
	boolean cmd = false;
	
	String output = "";
	
    public StreamGobbler(InputStream is, String type) {
    	this.is = is;
    	this.type = type;
    }
    
    public StreamGobbler(InputStream is, String type, boolean cmd) {
    	this.is = is;
    	this.type = type;
    	this.cmd = cmd;
    }
    
    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
            	if (!cmd) {
            		System.out.println(type + "> " + line);
                	Renderer.getInstance().getConsole().append(line);
            	} else {
            		System.out.println(type + "> " + line);
            		output += line;
            	}
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public String getOutput() {
    	return output;
    }
}