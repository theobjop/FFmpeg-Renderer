package github.theobjop.ffmpegrenderer;

import java.util.Map;
import javax.swing.JComponent;

public class VideoSettings {

    public static final String V_CODEC_OPT = "-c:v";
    public static final String V_BIT_OPT = "-b:v";
    public static final String FPS_OPT = "-framerate";
    public static final String CRF_OPT = "-crf";
    public static final String QP_OPT = "-qp";
    public static final String PRESET_OPT = "-preset";

    public VideoSettings() {

    }

    public void loadSettings() {

    }

    public void setElementsEnabled(boolean f) {
    }

    public static String getCodec() {
        return null;
    }

    public static String getBitrate() {
        return null;
    }

    public static String getFPS() {
        return null;
    }

    public static String getCRF() {
        return null;
    }

    public static String getQP() {
        return null;
    }

    public void addTo(JComponent component) {
    }

    public void removeAll(JComponent component) {

    }

    public String[] getRenderSettings() throws Exception {
        throw new Exception("Video Settings not initialized.");
    }

    public Map<String, String> getSaveSettings() {
        return null;
    }

    public static Map<String, String> getDefaultSettings() {
        return null;
    }
}
