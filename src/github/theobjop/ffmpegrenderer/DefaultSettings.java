package github.theobjop.ffmpegrenderer;

import java.text.NumberFormat;
import java.text.ParseException;

public class DefaultSettings {
	
	private static final String PIXFMT_OPT = "-pix_fmt";
	private static final String B_FRAMES_OPT = "-bf";
	private static final String B_ADAPT_OPT = "-b_strategy";
	private static final String V_PROFILE_OPT = "-profile:v";
	private static final String KEY_INT_OPT = "-g";
	private static final String FLAGS_OPT = "-flags";
	private static final String MOV_FLAG_OPT = "-movflags";
	
	private static String bFrames = "2";
	private static String vProfile = "high";
	private static String bAdapt = "2";
	private static String flags = "+cgop";
	private static String pix_fmt = "yuv420p";
	private static String mov_flags = "+faststart";
	
	public static final String getKeyint(String fps) throws ParseException {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		Number n = nf.parse(fps);
		return String.valueOf(n.intValue()/2);
	}
	
	public static final String getVProfile() {
		return vProfile;
	}
	
	public static final String getBAdapt() {
		return bAdapt;
	}
	
	public static final String getBFrames() {
		return bFrames;
	}
	
	public static final String getFlags() {
		return flags;
	}
	
	public static final String getPixelFormat() {
		return pix_fmt;
	}
	
	public static final String getMovFlags() {
		return mov_flags;
	}
	
	public static String[] getSettings(String fps) throws Exception {
		String[] CMD_ARRAY = {
		KEY_INT_OPT, getKeyint(fps),
		V_PROFILE_OPT, getVProfile(),
		B_FRAMES_OPT, getBFrames(),
		B_ADAPT_OPT, getBAdapt(),
		FLAGS_OPT, getFlags(),
		PIXFMT_OPT, getPixelFormat(),
		MOV_FLAG_OPT, getMovFlags(),
		};		
		return CMD_ARRAY;
	}
}
