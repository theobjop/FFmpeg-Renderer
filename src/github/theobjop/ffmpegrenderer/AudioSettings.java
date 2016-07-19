package github.theobjop.ffmpegrenderer;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AudioSettings {
	
	// Making a singleton and announcing it as unused because we're not going to edit this shit.
	private static AudioSettings SINGLETON;
	
	private static final String A_CODEC_OPT = "-c:a";
	private static final String A_BIT_OPT = "-b:a";
	private static final String A_FREQ_OPT = "-ar";	
	
	private static JLabel aFLbl;
	private static JLabel aBLbl;
	
	private static JTextField frequency;
	private static JTextField bitrate;	
	
	private static final String CODEC = "aac";
	
	public static void CreateSingleton() {
		if (SINGLETON == null)
			SINGLETON = new AudioSettings();
	}
	
	private AudioSettings() {
		// For formatting
		Rectangle2D comSetSize = Renderer.font.getStringBounds("Audio Kb/s:", Renderer.frc);
		
		//// Audio Labels
		aFLbl = new JLabel("Audio Hz:");
		aBLbl = new JLabel("Audio Kb/s:");
		frequency = new JTextField("44100");
		bitrate = new JTextField("192");

		/// Audio TextFields
		aFLbl.setBounds(20, 232-6-4, (int)comSetSize.getWidth()+2, (int)comSetSize.getHeight());
		aBLbl.setBounds(9, 254-4, (int)comSetSize.getWidth()+2, (int)comSetSize.getHeight());
		frequency.setBounds((int)comSetSize.getWidth()+15, 218, 75, 24);
		bitrate.setBounds((int)comSetSize.getWidth()+15, 246, 75, 24);
	}
	
	public static void loadSettings() {
		frequency.setText(PropertiesWriter.get(A_FREQ_OPT));
		bitrate.setText(PropertiesWriter.get(A_BIT_OPT));
	}
	
	private static void elementsB(boolean f) {
		bitrate.setEnabled(f);
		frequency.setEditable(f);
	}
	
	public static void enableElements() {
		elementsB(true);
	}
	
	public static void disableElements() {
		elementsB(false);
	}
	
	public static void addTo(JPanel parent) {
		parent.add(aFLbl);
		parent.add(aBLbl);
		parent.add(frequency);
		parent.add(bitrate);
	}
	
	public static String getCodec() {
		return CODEC;
	}
	
	public static String getBitrate() {
		return bitrate.getText();
	}
	
	public static String getFrequency() {
		return frequency.getText();
	}
	
	public static String[] getRenderSettings() throws Exception {
		ArrayList<String> CMD_ARRAY = new ArrayList<String>();
		
		if (!AudioSettings.CODEC.isEmpty()) {
			CMD_ARRAY.add(A_CODEC_OPT);
			CMD_ARRAY.add(AudioSettings.CODEC);
		}
		
		if (!AudioSettings.getBitrate().isEmpty()) {
			CMD_ARRAY.add(A_BIT_OPT);
			CMD_ARRAY.add(AudioSettings.getBitrate() + "k");
		} else
			throw new Exception("Audio Bitrate not set.");
		
		if (!AudioSettings.getFrequency().isEmpty()) {
			CMD_ARRAY.add(A_FREQ_OPT);
			CMD_ARRAY.add(AudioSettings.getFrequency());
		} else
			throw new Exception("Audio Frequency (Hz) not set.");
		
		return CMD_ARRAY.toArray(new String[CMD_ARRAY.size()]);
	}
	
	public static Map<String, String> getSaveSettings() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(A_BIT_OPT, AudioSettings.getBitrate());
		map.put(A_FREQ_OPT, AudioSettings.getFrequency());
		return map;
	}
	
	public static Map<String, String> getDefaultSettings() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(A_BIT_OPT, "192");
		map.put(A_FREQ_OPT, "44100");
		return map;
	}
}
