package github.theobjop.ffmpegrenderer;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class PresetSelector extends JComboBox<String> {
	
	static String[] defaultSelections = { "none", "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "veryslow" };
	
	public PresetSelector() {
		super(defaultSelections);
		this.setSelectedIndex(0);
	}
}
