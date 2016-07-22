package github.theobjop.ffmpegrenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class VideoSettings {

	// Making a singleton and announcing it as unused because we're not going to edit this shit.
	private static VideoSettings SINGLETON;
	
	// Incase we need them...
	public static final String V_CODEC_OPT = "-c:v";
	public static final String V_BIT_OPT = "-b:v";
	public static final String FPS_OPT = "-framerate";
	public static final String CRF_OPT = "-crf";
	public static final String QP_OPT = "-qp";
	public static final String PRESET_OPT = "-preset";
	
	public static JLabel pLbl;
	public static JLabel vBLbl;
	public static JLabel fpsLbl;
	public static JLabel crfLbl;
	public static JLabel qpLbl;
	
	public static JTextField bitrate;
	public static JTextField framerate;
	public static JTextField constRateFrame;
	public static JTextField qpRate;
	
	public static PresetSelector presetSelector;
	
	private static final String CODEC = "libx264";
	
	public static void CreateSingleton() {
		if (SINGLETON == null)
			SINGLETON = new VideoSettings();
	}
	
	private VideoSettings() {		
		// Presets
		pLbl = new JLabel("Preset: ");
		presetSelector = new PresetSelector();
		
		// Video Labels
		vBLbl = new JLabel("Video Kb/s:");
		fpsLbl = new JLabel("FPS:");
		crfLbl = new JLabel("CRF:");
		qpLbl = new JLabel("QP:");
		bitrate = new JTextField("1000");
		framerate = new JTextField("30");
		constRateFrame = new JTextField("21");
		qpRate = new JTextField("0");
		
		loadSettings();
	}
	
	public static void loadSettings() {
		String prest = PropertiesWriter.get(PRESET_OPT) == null ? "none" : PropertiesWriter.get(PRESET_OPT);
		presetSelector.setSelectedItem(prest);
		bitrate.setText(PropertiesWriter.get(V_BIT_OPT));
		framerate.setText(PropertiesWriter.get(FPS_OPT));
		constRateFrame.setText(PropertiesWriter.get(CRF_OPT));
		qpRate.setText(PropertiesWriter.get(QP_OPT));
	}
	
	private static void elementsB(boolean f) {
		bitrate.setEnabled(f);
		framerate.setEditable(f);
		constRateFrame.setEditable(f);
		presetSelector.setEnabled(f);
	}
	
	public static void enableElements() {
		elementsB(true);
	}
	
	public static void disableElements() {
		elementsB(false);
	}
	
	public static void addTo(JPanel parent) {		
		parent.add(pLbl);
		parent.add(vBLbl);
		parent.add(fpsLbl);
		parent.add(crfLbl);
		parent.add(qpLbl);
		parent.add(bitrate);
		parent.add(framerate);
		parent.add(constRateFrame);
		parent.add(presetSelector);
		parent.add(qpRate);
	}
	
	public static JComponent[] getComponenets() {
		JComponent[] ret = { pLbl, vBLbl, fpsLbl, crfLbl, qpLbl, bitrate, framerate, constRateFrame, presetSelector, qpRate };
		return ret;
	}
	
	public static String getCodec() {
		return CODEC;
	}
	
	public static String getBitrate() {
		return bitrate.getText();
	}
	
	public static String getFPS() {
		return framerate.getText();
	}
	
	public static String getCRF() {
		return constRateFrame.getText();
	}
	
	public static String getQP() {
		return qpRate.getText();
	}
	
	public static PresetSelector getPresetSelector() {
		return presetSelector;
	}
	
	public static String getCRForQP_OPT() {
		if (VideoSettings.getCRF().isEmpty())
			return QP_OPT;
		if (VideoSettings.getQP().isEmpty())
			return CRF_OPT;
	
		return null;
	}
	
	public static String getCRForQP() {
		if (VideoSettings.getCRF().isEmpty())
			return getQP();
		if (VideoSettings.getQP().isEmpty())
			return getCRF();
	
		return null;
	}
	
	public static String[] getRenderSettings() throws Exception {
		ArrayList<String> CMD_ARRAY = new ArrayList<String>();
		
		if (!VideoSettings.CODEC.isEmpty()) {
			CMD_ARRAY.add(V_CODEC_OPT);
			CMD_ARRAY.add(VideoSettings.CODEC);
		}
		
		// Get bitrate unless QP is set - QP will imply Variable Bit Rate
		if (!VideoSettings.getBitrate().isEmpty() && getCRForQP() != null) {
			CMD_ARRAY.add(V_BIT_OPT);
			CMD_ARRAY.add(VideoSettings.getBitrate());
		}
		
		// Get FPS, if not set then default 30
		if (!VideoSettings.getFPS().isEmpty()) {
			CMD_ARRAY.add(FPS_OPT);
			CMD_ARRAY.add(VideoSettings.getFPS());
		} else {
			CMD_ARRAY.add(FPS_OPT);
			CMD_ARRAY.add("30");	
		}
		
		// Get CRF if it's set : Get QP if it's set
		if (getCRForQP_OPT() != null && getCRForQP() != null) {
			CMD_ARRAY.add(getCRForQP_OPT());
			CMD_ARRAY.add(getCRForQP());
		} else {
			throw new Exception("Make sure either CRF or QP is empty.");
		}
		
		if (!( (String) (VideoSettings.getPresetSelector().getSelectedItem())).equalsIgnoreCase("none")) {
			CMD_ARRAY.add(PRESET_OPT);
			CMD_ARRAY.add((String)VideoSettings.getPresetSelector().getSelectedItem());
		}
		
		return CMD_ARRAY.toArray(new String[CMD_ARRAY.size()]);
	}
	
	public static Map<String,String> getSaveSettings() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(V_BIT_OPT, VideoSettings.getBitrate());
		map.put(FPS_OPT, VideoSettings.getFPS());
		map.put(CRF_OPT, getCRF());
		map.put(QP_OPT, getQP());
		map.put(PRESET_OPT, (String)VideoSettings.getPresetSelector().getSelectedItem());
		return map;
	}

	public static Map<String, String> getDefaultSettings() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(V_BIT_OPT, "1000");
		map.put(FPS_OPT, "30");
		map.put(CRF_OPT, "21");
		map.put(QP_OPT, "0");
		map.put(PRESET_OPT, "none");		
		return map;
	}
}
