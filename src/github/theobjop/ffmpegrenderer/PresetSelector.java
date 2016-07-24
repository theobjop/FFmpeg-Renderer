package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class PresetSelector extends JComboBox<String> implements ActionListener {

    static String[] defaultSelections = {"x264", "Lossless", "YouTube 1080p", "YouTube 720p", "WebM"};

    // Video Settings
    private static String videoBitrate = "";
    private static String preset = "";
    private static String fps = "";
    private static String crf = "";
    private static String qp = "";

    // Audio Settings
    private static String audioBitrate = "";
    private static String audioFrequency = "";

    // Default Settings (Usually don't need changed)
    private static String bFrames = "";
    private static String vProfile = "";
    private static String bAdapt = "";
    private static String flags = "";
    private static String pix_fmt = "";
    private static String mov_flags = "";

    private static final String[] x264_Preset = {};

    private static final String[] Lossless_Preset = {
        videoBitrate = "", preset = "ultrafast", crf = "0",
        bFrames = "2", vProfile = "high", bAdapt = "2",
        flags = "+cgop", pix_fmt = "yuv420p", mov_flags = "+faststart"
    };

    private static final String[] YouTube_Preset = {
        videoBitrate = "3500", preset = "slow", crf = "14",
    };

    private static final String[] WebM_Preset = {};

	public PresetSelector() {
		super(defaultSelections);
        this.setSelectedIndex(0);
        this.addActionListener(this);
    }

    public void actionListener(ActionEvent e) {
        // We imply that this is the only source, so no need to do a check
        Renderer.LoadPreset((String) this.getSelectedItem());
    }
}
