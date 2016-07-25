package github.theobjop.ffmpegrenderer;

public interface StreamCallback {	
	public default void callback(String uid, Object o) { }
}
