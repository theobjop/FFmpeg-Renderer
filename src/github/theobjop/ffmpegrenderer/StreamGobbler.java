package github.theobjop.ffmpegrenderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.logging.Level;

public class StreamGobbler extends Thread {
    
	StreamCallback cb;
	InputStream[] is;
	String uniqueID;
	
	boolean cmd = false;
	
	public StreamGobbler(InputStream... is) {
    	this.is = is;
    	this.uniqueID = UUID.randomUUID().toString();
    }
	
    public StreamGobbler(String uniqueId, StreamCallback cb, InputStream... is) {
    	this.uniqueID = uniqueId;
    	this.cb = cb;
    	this.is = is;
    }
    
    public StreamGobbler(StreamCallback cb, InputStream... is) {
    	this.uniqueID = UUID.randomUUID().toString();
    	this.cb = cb;
    	this.is = is;
    }
    
    @Override
    public void run() {
    	// Create a new InputStream for all of the provided input streams.
    	// It usually only contains Error and Output streams, moreso singally one or the other.
    	for (InputStream in : is) {
    		new Thread(new Runnable() {
    			@Override
    			public void run() {
    				try {
	    				BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    				String line = null;
	    	            while ((line = br.readLine()) != null) {
	    	            	Renderer.log.info(line);
	    	            	Console.write(line);
	    	        		cb.callback(uniqueID, line);
	    	            }
    				} catch (IOException e) {
    					Renderer.log.log(Level.SEVERE, "Thread in StreamGobbler", e);
    				}
    			}
    		}).start();
    	}
    }
}