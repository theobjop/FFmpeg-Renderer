/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package github.theobjop.ffmpegrenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 *
 * @author Brandon Woolworth
 */
public final class x264Settings extends Settings {

	public static final File FILE = new File(Renderer.USER_HOME + "\\.x264opt");
	public static final String TYPE = "x264";
	
	public static File getFile() {
		return FILE;
	}
	
	private SpringLayout layout;
	
    // Incase we need them...
    public JLabel pLbl;
    public JLabel vBLbl;
    
    public JLabel fpsLbl;
    public JLabel crfLbl;
    public JLabel qpLbl;
    
    public JLabel aFLbl;
	public JLabel aBLbl;

    public JTextField video_bitrate;
    public JTextField video_framerate;
    public JTextField constRateFrame;
    public JTextField qpRate;
    
    public JTextField audio_frequency;
	public JTextField audio_bitrate;	

    private final String[] defaultSelections = {"none", "ultrafast", "superfast", "veryfast", "faster", "fast", "medium", "slow", "veryslow"};
    public JComboBox<String> presetSelector;

    SettingsContainer parent;

    public x264Settings() {    	
		Renderer.log.info("Loading x264Settings");
		// Get if properties were loaded
		//  True: They were loaded, meaning the file existed
		// False: They weren't loaded, meaning file didn't exist. Load defaults after instantiation.
		boolean defaults = !PropertiesWriter.LoadProperties(PropertiesWriter.getPreset(this));
    		
    	layout = new SpringLayout();
    	
        // Presets
        pLbl = new JLabel("Preset: ");
        presetSelector = new JComboBox<>(defaultSelections);
        presetSelector.setSelectedIndex(0);
        presetSelector.setPreferredSize(presetSelector.getPreferredSize());

        // Video Labels
        vBLbl = new JLabel("Video Kb/s:");
        fpsLbl = new JLabel("FPS:");
        crfLbl = new JLabel("CRF:");
        qpLbl = new JLabel("QP:");

        aFLbl = new JLabel("Audio Hz:");
		aBLbl = new JLabel("Audio Kb/s:");
        
        video_bitrate = new JTextField("1000");
        video_framerate = new JTextField("30");
        constRateFrame = new JTextField("21");
        qpRate = new JTextField("0");
        
		audio_frequency = new JTextField("44100");
		audio_bitrate = new JTextField("192");
		
		String prest = PropertiesWriter.get("preset") == null ? "none" : PropertiesWriter.get("preset");
		
        presetSelector.setSelectedItem(prest);
        video_bitrate.setText(PropertiesWriter.get("Videobitrate"));
        video_framerate.setText(PropertiesWriter.get("fps"));
        audio_bitrate.setText(PropertiesWriter.get("Audiobitrate"));
        audio_frequency.setText(PropertiesWriter.get("Audiofrequency"));
        constRateFrame.setText(PropertiesWriter.get("crf"));
        qpRate.setText(PropertiesWriter.get("qp"));
        
        // if the properties were not loaded, we revert to defaults.
        if (defaults)
        	this.setDefaults();
    }
    
    public void setLayout() {
        ////// LEFT SIDE        
        // Position "Video Kb/s:" label 7 away from left of the parent panel
        layout.putConstraint(SpringLayout.WEST, vBLbl, 7, SpringLayout.WEST, parent);
        
        // Position videobitrate textbox 7 away from top 
        layout.putConstraint(SpringLayout.NORTH, video_bitrate, 0, SpringLayout.NORTH, parent);
        layout.putConstraint(SpringLayout.NORTH, video_framerate, 0, SpringLayout.NORTH, parent);
        
        // Position video bitrate textbox 4 away from label      
        layout.putConstraint(SpringLayout.WEST, video_bitrate, 4, SpringLayout.EAST, vBLbl);
        
        // Position Audiobitrate textbox 7 away from bottom then match it up with the videobitrate Horizontal
        layout.putConstraint(SpringLayout.SOUTH, audio_bitrate, -6, SpringLayout.SOUTH, parent);
        
        // Lineup AudioBitrate label, PresetLbl, and AudioFrequency with the videobitrate one
        layout.putConstraint(SpringLayout.EAST, aBLbl, 0, SpringLayout.EAST, vBLbl);
        layout.putConstraint(SpringLayout.EAST, aFLbl, 0, SpringLayout.EAST, vBLbl);
        layout.putConstraint(SpringLayout.EAST,  pLbl, 0, SpringLayout.EAST, vBLbl);
        
        // Lineup all the Horizontal centers of the remaining left-side components
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, audio_frequency, 0, SpringLayout.HORIZONTAL_CENTER, video_bitrate);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,   audio_bitrate, 0, SpringLayout.HORIZONTAL_CENTER, video_bitrate);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER,  presetSelector, 0, SpringLayout.HORIZONTAL_CENTER, video_bitrate);
        
        // Make them all the same width as presetSelector
        layout.putConstraint(SpringLayout.WIDTH, audio_frequency, 0, SpringLayout.WIDTH, presetSelector);
        layout.putConstraint(SpringLayout.WIDTH,   video_bitrate, 0, SpringLayout.WIDTH, presetSelector);
        layout.putConstraint(SpringLayout.WIDTH,   audio_bitrate, 0, SpringLayout.WIDTH, presetSelector);
        
        // Space the presetSelector 4 away from videobitrate
        layout.putConstraint(SpringLayout.NORTH,  presetSelector,  4, SpringLayout.SOUTH, video_bitrate);
        // Same with the audiofrequency and audiobitrate
        layout.putConstraint(SpringLayout.SOUTH, audio_frequency, -4, SpringLayout.NORTH, audio_bitrate);
        
        // Vertical labels to their corresponding textboxes
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, vBLbl, 0, SpringLayout.VERTICAL_CENTER, video_bitrate);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, aBLbl, 0, SpringLayout.VERTICAL_CENTER, audio_bitrate);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, aFLbl, 0, SpringLayout.VERTICAL_CENTER, audio_frequency);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER,  pLbl, 0, SpringLayout.VERTICAL_CENTER, presetSelector);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER,crfLbl, 0, SpringLayout.VERTICAL_CENTER, constRateFrame);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, qpLbl, 0, SpringLayout.VERTICAL_CENTER, qpRate);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER,fpsLbl, 0, SpringLayout.VERTICAL_CENTER, video_framerate);
        
        ////// RIGHT SIDE
        // Position FPS textbox 4 away from right of parent panel and match it's vertical up with bitrateTB
        layout.putConstraint(SpringLayout.EAST, video_framerate, -7, SpringLayout.EAST, parent);
        
        // Match all textboxes under fps to horiz center and width
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, constRateFrame, 0, SpringLayout.HORIZONTAL_CENTER, video_framerate);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, qpRate, 0, SpringLayout.HORIZONTAL_CENTER, video_framerate);
        layout.putConstraint(SpringLayout.WIDTH, constRateFrame, 0, SpringLayout.WIDTH, video_framerate);
        layout.putConstraint(SpringLayout.WIDTH, qpRate, 0, SpringLayout.WIDTH, video_framerate);
        
        // Space them 4 apart from each other downwards
        layout.putConstraint(SpringLayout.NORTH, constRateFrame, 4, SpringLayout.SOUTH, video_framerate);
        layout.putConstraint(SpringLayout.NORTH, qpRate, 4, SpringLayout.SOUTH, constRateFrame);
        
        // Place FPS label 4 away from it's textbox so we can match it's right side with the rest of the labels
        layout.putConstraint(SpringLayout.EAST, fpsLbl, -4, SpringLayout.WEST, video_framerate);
        layout.putConstraint(SpringLayout.EAST, crfLbl, 0, SpringLayout.EAST, fpsLbl);
        layout.putConstraint(SpringLayout.EAST, qpLbl,  0, SpringLayout.EAST, fpsLbl);
        
        
        // Set the textboxes width to the same, but also smaller than the presetSelector
        layout.putConstraint(SpringLayout.WIDTH, video_framerate, -20, SpringLayout.WIDTH, presetSelector);
        layout.putConstraint(SpringLayout.WIDTH, constRateFrame, -20, SpringLayout.WIDTH, presetSelector);
        layout.putConstraint(SpringLayout.WIDTH, qpRate, -20, SpringLayout.WIDTH, presetSelector);
        
        // Lastly, the height of everything must be the same.
        layout.putConstraint(SpringLayout.HEIGHT, video_bitrate, 0, SpringLayout.HEIGHT, presetSelector);
        layout.putConstraint(SpringLayout.HEIGHT, video_framerate, 0, SpringLayout.HEIGHT, presetSelector);
        layout.putConstraint(SpringLayout.HEIGHT, constRateFrame, 0, SpringLayout.HEIGHT, presetSelector);
        layout.putConstraint(SpringLayout.HEIGHT, qpRate, 0, SpringLayout.HEIGHT, presetSelector);
        layout.putConstraint(SpringLayout.HEIGHT, audio_bitrate, 0, SpringLayout.HEIGHT, presetSelector);
        layout.putConstraint(SpringLayout.HEIGHT, audio_frequency, 0, SpringLayout.HEIGHT, presetSelector);
        parent.setLayout(getLayout());
    }

    @Override
    public void setElementsEnabled(boolean f) {
        video_bitrate.setEnabled(f);
        video_framerate.setEditable(f);
        audio_bitrate.setEnabled(f);
        audio_frequency.setEditable(f);
        constRateFrame.setEditable(f);
        presetSelector.setEnabled(f);
    }

    public void setParent(SettingsContainer parent) {
    	this.parent = parent;
    }
    
    public void addAll() {
    	addAll(this.parent);
    }
    
    public void addAll(SettingsContainer parent) {
    	this.parent = parent;
        parent.add(pLbl);
        parent.add(vBLbl);
        parent.add(aBLbl);
        parent.add(aFLbl);
        parent.add(fpsLbl);
        parent.add(crfLbl);
        parent.add(qpLbl);
        parent.add(video_bitrate);
        parent.add(video_framerate);
        parent.add(constRateFrame);
        parent.add(presetSelector);
        parent.add(qpRate);
        parent.add(audio_bitrate);
        parent.add(audio_frequency);
    }

    public String getVideoCodec() {
        return VIDEO_CODEC;
    }
	
	public String getAudioCodec() {
		return AUDIO_CODEC;
	}
	
	public String getAudioBitrate() {
		return audio_bitrate.getText();
	}
	
	public String getAudioFrequency() {
		return audio_frequency.getText();
	}

    public String getVideoBitrate() {
        return video_bitrate.getText();
    }

    public String getFPS() {
        return video_framerate.getText();
    }

    public String getCRF() {
        return constRateFrame.getText();
    }

    public String getQP() {
        return qpRate.getText();
    }

    public JComboBox<String> getPresetSelector() {
        return presetSelector;
    }

    private String getCRForQP_OPT() {
        if (getCRF().isEmpty()) {
            return QP_OPT;
        }
        if (getQP().isEmpty()) {
            return CRF_OPT;
        }

        return null;
    }

    private String getCRForQP() {
        if (getCRF().isEmpty()) {
            return getQP();
        }
        if (getQP().isEmpty()) {
            return getCRF();
        }

        return null;
    }

    public String[] getRenderSettings() throws Exception {
        ArrayList<String> CMD_ARRAY = new ArrayList<>();

        // Get bitrate unless QP is set - QP will imply Variable Bit Rate
        if (!getVideoBitrate().isEmpty() && getCRForQP() != null) {
            CMD_ARRAY.add(V_BIT_OPT);
            CMD_ARRAY.add(getVideoBitrate());
        }

        // Get FPS, if not set then default 30
        if (!getFPS().isEmpty()) {
            CMD_ARRAY.add(FPS_OPT);
            CMD_ARRAY.add(getFPS());
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

        if (!((String) (getPresetSelector().getSelectedItem())).equalsIgnoreCase("none")) {
            CMD_ARRAY.add(PRESET_OPT);
            CMD_ARRAY.add((String) getPresetSelector().getSelectedItem());
        }
		
		if (!getAudioBitrate().isEmpty()) {
			CMD_ARRAY.add(A_BIT_OPT);
			CMD_ARRAY.add(getAudioBitrate() + "k");
		} else
			throw new Exception("Audio Bitrate not set.");
		
		if (!getAudioFrequency().isEmpty()) {
			CMD_ARRAY.add(A_FREQ_OPT);
			CMD_ARRAY.add(getAudioFrequency());
		} else
			throw new Exception("Audio Frequency (Hz) not set.");

        return CMD_ARRAY.toArray(new String[CMD_ARRAY.size()]);
    }

    public Map<String, String> getSaveSettings() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Videobitrate", getVideoBitrate());
        map.put("Audiobitrate", getAudioBitrate());
        map.put("Audiofrequency", getAudioFrequency());
        map.put("fps", getFPS());
        map.put("crf", getCRF());
        map.put("qp", getQP());
        map.put("preset", (String) getPresetSelector().getSelectedItem());
        return map;
    }

	@Override
	public SpringLayout getLayout() {
		return layout;
	}

	public void setDefaults() {
        video_bitrate.setText("1000");
        video_framerate.setText("30");
        audio_bitrate.setText("192");
        audio_frequency.setText("44100");
        constRateFrame.setText("21");
        qpRate.setText("0");
        presetSelector.setSelectedItem("none");
	}
	
	public String getType() {
		return TYPE;
	}
}
