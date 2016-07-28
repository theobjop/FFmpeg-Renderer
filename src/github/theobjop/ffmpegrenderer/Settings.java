package github.theobjop.ffmpegrenderer;

import java.io.File;
import java.util.Map;
import javax.swing.SpringLayout;

public abstract class Settings {

    public static final String V_CODEC_OPT = "-c:v";
    public static final String V_BIT_OPT = "-b:v";
    public static final String FPS_OPT = "-framerate";
    public static final String CRF_OPT = "-crf";
    public static final String QP_OPT = "-qp";
    public static final String PRESET_OPT = "-preset";
    
    public static final String A_CODEC_OPT = "-c:a";
    public static final String A_BIT_OPT = "-b:a";
    public static final String A_FREQ_OPT = "-ar";
    
    public static final String VIDEO_CODEC = "libx264";
    public static final String AUDIO_CODEC = "aac";
    
    public abstract void setElementsEnabled(boolean f);
    public abstract SpringLayout getLayout();
    public abstract void setLayout();
    
    public abstract void setParent(SettingsContainer p);
    
    public abstract String getVideoCodec();
    public abstract String getAudioCodec();
    public abstract String getVideoBitrate();
    public abstract String getAudioBitrate();
    public abstract String getFPS();
    public abstract String getCRF();
    public abstract String getQP();

    public abstract void addAll(SettingsContainer parent);
    public abstract void addAll();
    
    public abstract String[] getRenderSettings() throws Exception;

    public abstract Map<String, String> getSaveSettings();
    
    public abstract void setDefaults();
    public abstract String getType();
}
