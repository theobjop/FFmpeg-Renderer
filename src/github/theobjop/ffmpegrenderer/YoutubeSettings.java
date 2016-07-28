package github.theobjop.ffmpegrenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class YoutubeSettings extends Settings {

	public static final File FILE = new File(Renderer.USER_HOME + "\\.ytopt");
	public static final String TYPE = "YouTube";
	
	SettingsContainer parent;
	SpringLayout layout;
	
	JLabel _fpsChooser;
	JComboBox<String> fpsChooser;
	
	JLabel _qualityChooser;
	JComboBox<String> qualityChooser;
	
	JLabel _speedChooser;
	JComboBox<String> speedChooser;
	
	JLabel _audioHzChooser;
	JComboBox<String> audioHzChooser;
	
	JLabel _audioQualityChooser;
	JComboBox<String> audioQualityChooser; // Mono, Stereo, 5.1
	
	JLabel _videoWidth;
	JTextField videoWidth;
	
	JLabel _videoHeight;
	JTextField videoHeight;
	
	JComponent[] localCollectionOfAllTheElementsInThisClassBecauseImTooLazyToIterateAllOfThem;
	
	public YoutubeSettings() {    	
		Renderer.log.info("Loading YoutubeSettings");
		// Get if properties were loaded
		//  True: They were loaded, meaning the file existed
		// False: They weren't loaded, meaning file didn't exist. Load defaults after instantiation.
		boolean defaults = !PropertiesWriter.LoadProperties(PropertiesWriter.getPreset(this));
		layout = new SpringLayout();
		
		_fpsChooser = new JLabel("FPS:");
		_qualityChooser = new JLabel("Video Quality:");
		_speedChooser = new JLabel("Render Speed:");
		_audioHzChooser = new JLabel("Audio Hz:");
		_audioQualityChooser = new JLabel("Audio Quality:");
		_videoWidth = new JLabel("Width:");
		_videoHeight = new JLabel("Height:");
		
		fpsChooser = new JComboBox<>(new String[]{"24", "29.97", "30", "50", "60"});
		qualityChooser = new JComboBox<>(new String[]{"Highest", "High", "Normal", "Low", "Lowest"});
		speedChooser = new JComboBox<>(new String[]{"Very fast", "Fast", "Normal", "Slow", "Very slow"});
		audioHzChooser = new JComboBox<>(new String[]{"44100","48000"});
		audioQualityChooser = new JComboBox<>(new String[]{"Mono", "Stereo", "5.1"});
		
		fpsChooser.setSelectedItem(PropertiesWriter.get("fps"));
		qualityChooser.setSelectedItem(PropertiesWriter.get("q"));
		speedChooser.setSelectedItem(PropertiesWriter.get("spd"));
		audioQualityChooser.setSelectedItem(PropertiesWriter.get("aq"));
		
		videoWidth = new JTextField(PropertiesWriter.get("vw"));
		videoHeight = new JTextField(PropertiesWriter.get("vh"));
		
		localCollectionOfAllTheElementsInThisClassBecauseImTooLazyToIterateAllOfThem = new JComponent[]{
				_fpsChooser, _qualityChooser, _speedChooser, _audioHzChooser, _audioQualityChooser, _videoWidth, _videoHeight,
				 fpsChooser,  qualityChooser,  speedChooser,  audioHzChooser,  audioQualityChooser,  videoWidth,  videoHeight
		};
		
		if (defaults)
			this.setDefaults();
	}

	@Override
	public void addAll() {
		addAll(this.parent);
	}

	@Override
	public void addAll(SettingsContainer parent) {
		this.parent = parent;
		for (int i = 0; i < localCollectionOfAllTheElementsInThisClassBecauseImTooLazyToIterateAllOfThem.length; i++) {
			parent.add(localCollectionOfAllTheElementsInThisClassBecauseImTooLazyToIterateAllOfThem[i]);
		}
	}
	
	// This is for creating the layout constraints.
	@Override
	public void setLayout() {
		layout.putConstraint(SpringLayout.NORTH, audioQualityChooser, 4, SpringLayout.SOUTH, qualityChooser);
		layout.putConstraint(SpringLayout.NORTH, speedChooser, 4, SpringLayout.SOUTH, audioQualityChooser);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _qualityChooser, 0, SpringLayout.VERTICAL_CENTER, _fpsChooser);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, qualityChooser, 0, SpringLayout.VERTICAL_CENTER, fpsChooser);
		layout.putConstraint(SpringLayout.WEST, qualityChooser, 4, SpringLayout.EAST, _qualityChooser);
		
		layout.putConstraint(SpringLayout.EAST, fpsChooser, -7, SpringLayout.EAST, parent);
		layout.putConstraint(SpringLayout.NORTH, fpsChooser, 0, SpringLayout.NORTH, parent);
		layout.putConstraint(SpringLayout.NORTH, videoWidth, 4, SpringLayout.SOUTH, fpsChooser);
		layout.putConstraint(SpringLayout.NORTH, videoHeight, 4, SpringLayout.SOUTH, videoWidth);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, videoWidth, 0, SpringLayout.HORIZONTAL_CENTER, fpsChooser);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, videoHeight, 0, SpringLayout.HORIZONTAL_CENTER, fpsChooser);
		layout.putConstraint(SpringLayout.HEIGHT, videoWidth, 0, SpringLayout.HEIGHT, qualityChooser);
		layout.putConstraint(SpringLayout.HEIGHT, videoHeight, 0, SpringLayout.HEIGHT, qualityChooser);
		layout.putConstraint(SpringLayout.WIDTH, videoWidth, 0, SpringLayout.WIDTH, audioHzChooser);
		layout.putConstraint(SpringLayout.WIDTH, videoHeight, 0, SpringLayout.WIDTH, audioHzChooser);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _fpsChooser, 0, SpringLayout.VERTICAL_CENTER, fpsChooser);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _videoWidth, 0, SpringLayout.VERTICAL_CENTER, videoWidth);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _videoHeight, 0, SpringLayout.VERTICAL_CENTER, videoHeight);
		layout.putConstraint(SpringLayout.EAST, _fpsChooser, -4, SpringLayout.WEST, fpsChooser);
		layout.putConstraint(SpringLayout.EAST, _videoWidth, -4, SpringLayout.WEST, videoWidth);
		layout.putConstraint(SpringLayout.EAST, _videoHeight, -4, SpringLayout.WEST, videoHeight);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, audioQualityChooser, 0, SpringLayout.HORIZONTAL_CENTER, qualityChooser);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, speedChooser, 0, SpringLayout.HORIZONTAL_CENTER, qualityChooser);
		
		layout.putConstraint(SpringLayout.WIDTH, qualityChooser, 0, SpringLayout.WIDTH, speedChooser);
		layout.putConstraint(SpringLayout.WIDTH, audioQualityChooser, 0, SpringLayout.WIDTH, speedChooser);
		
		layout.putConstraint(SpringLayout.WEST, _speedChooser, 7, SpringLayout.WEST, parent);
		layout.putConstraint(SpringLayout.EAST, _qualityChooser, 0, SpringLayout.EAST, _speedChooser);
		layout.putConstraint(SpringLayout.EAST, _audioQualityChooser, 0, SpringLayout.EAST, _speedChooser);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _qualityChooser, 0, SpringLayout.VERTICAL_CENTER, qualityChooser);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _audioQualityChooser, 0, SpringLayout.VERTICAL_CENTER, audioQualityChooser);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _speedChooser, 0, SpringLayout.VERTICAL_CENTER, speedChooser);
		
		layout.putConstraint(SpringLayout.EAST, audioHzChooser, 0, SpringLayout.EAST, videoHeight);
		layout.putConstraint(SpringLayout.NORTH, audioHzChooser, 4, SpringLayout.SOUTH, videoHeight);
		layout.putConstraint(SpringLayout.HEIGHT, audioHzChooser, 0, SpringLayout.HEIGHT, fpsChooser);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, _audioHzChooser, 0, SpringLayout.VERTICAL_CENTER, audioHzChooser);
		layout.putConstraint(SpringLayout.EAST, _audioHzChooser, -4, SpringLayout.WEST, audioHzChooser);
		layout.putConstraint(SpringLayout.WIDTH, fpsChooser, 0, SpringLayout.WIDTH, audioHzChooser);
	}
	
	@Override
	public void setElementsEnabled(boolean f) {
		fpsChooser.setEnabled(f);
		qualityChooser.setEnabled(f);
		speedChooser.setEnabled(f);
		audioHzChooser.setEnabled(f);
		audioQualityChooser.setEnabled(f);
		videoWidth.setEnabled(f);
		videoHeight.setEnabled(f);
	}

	@Override
	public SpringLayout getLayout() {
		return layout;
	}

	@Override
	public void setParent(SettingsContainer p) {
		this.parent = p;
	}

	@Override
	public String getVideoCodec() {
		return VIDEO_CODEC;
	}

	@Override
	public String getAudioCodec() {
		return AUDIO_CODEC;
	}

	@Override
	public String getVideoBitrate() {
		return null;
	}

	@Override
	public String getAudioBitrate() {
		return null;
	}

	@Override
	public String getFPS() {
		return (String)fpsChooser.getSelectedItem();
	}

	@Override
	public String getCRF() {
		// Depends on Quality
		return "21";
	}

	@Override
	public String getQP() {
		// Depends on Quality
		return "3";
	}

	@Override
	public String[] getRenderSettings() throws Exception {
		ArrayList<String> CMD_ARRAY = new ArrayList<>();
		CMD_ARRAY.add(V_CODEC_OPT);
		CMD_ARRAY.add(VIDEO_CODEC);
        
		CMD_ARRAY.add(FPS_OPT);
		CMD_ARRAY.add(getFPS());
		
		CMD_ARRAY.add(PRESET_OPT);
		switch ((String)speedChooser.getSelectedItem()) {
		default:
		case "Normal":
			CMD_ARRAY.add("medium");
			break;
		case "Fast":
			CMD_ARRAY.add("fast");
			break;
		case "Very fast":
			CMD_ARRAY.add("veryfast");
			break;
		case "Slow":
			CMD_ARRAY.add("slow");
			break;
		case "Very slow":
			CMD_ARRAY.add("veryslow");
			break;
		}
		
		CMD_ARRAY.add(CRF_OPT);
		switch ((String)qualityChooser.getSelectedItem()) {
		case "Highest":
			CMD_ARRAY.add("12");
			break;
		case "High":
			CMD_ARRAY.add("16");
			break;
		default:
		case "Normal":
			CMD_ARRAY.add("20");
			break;
		case "Low":
			CMD_ARRAY.add("24");
			break;
		case "Lowest":
			CMD_ARRAY.add("30");
			break;
		}
		
		CMD_ARRAY.add(A_BIT_OPT);
		switch ((String)audioQualityChooser.getSelectedItem()) {
		default:
		case "Stereo":
			CMD_ARRAY.add("256k");			
			break;
		case "Mono":
			CMD_ARRAY.add("128k");
			break;
		case "5.1":
			CMD_ARRAY.add("384k");
			break;
		}
		
		CMD_ARRAY.add(A_FREQ_OPT);
		CMD_ARRAY.add((String)audioHzChooser.getSelectedItem());

        return CMD_ARRAY.toArray(new String[CMD_ARRAY.size()]);
	}

	@Override
	public Map<String, String> getSaveSettings() {
		HashMap<String, String> map = new HashMap<>();
        map.put("fps", (String)fpsChooser.getSelectedItem());
        map.put("q", (String)(qualityChooser.getSelectedItem()));
        map.put("spd", (String)(speedChooser.getSelectedItem()));
        map.put("aq", (String)audioQualityChooser.getSelectedItem());
        map.put("ahz", (String)audioHzChooser.getSelectedItem());
        map.put("vw", videoWidth.getText());
        map.put("vh", videoHeight.getText());
        return map;
	}

	@Override
	public void setDefaults() {
		fpsChooser.setSelectedItem("30");
		qualityChooser.setSelectedItem("Normal");
		speedChooser.setSelectedItem("Normal");
		audioHzChooser.setSelectedItem("44100");
		audioQualityChooser.setSelectedItem("Stereo");
		videoWidth.setText("1920");
		videoHeight.setText("1080");
	}
	
	public String getType() {
		return TYPE;
	}
}
