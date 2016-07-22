package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SettingsContainer extends JPanel implements ActionListener {
	
	
	public static SpringLayout layout;
	JButton resetToDefault;
	
	public SettingsContainer() {
		layout = new SpringLayout();
		
		this.setBounds(5, 62, 430, 278); // 313
		this.setBorder(new TitledBorder("Settings"));
		
		resetToDefault = new JButton("Defaults");
		layout.putConstraint(SpringLayout.SOUTH, resetToDefault, -7, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, resetToDefault, -7, SpringLayout.EAST, this);
		resetToDefault.addActionListener(this);
		
		layout.putConstraint(SpringLayout.NORTH, VideoSettings.bitrate, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, VideoSettings.presetSelector, 5, SpringLayout.SOUTH, VideoSettings.bitrate);
		layout.putConstraint(SpringLayout.WEST, VideoSettings.presetSelector, 0, SpringLayout.WEST, VideoSettings.bitrate);
		
		layout.putConstraint(SpringLayout.WEST, VideoSettings.vBLbl, 7, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, VideoSettings.bitrate, 5, SpringLayout.EAST, VideoSettings.vBLbl);
		layout.putConstraint(SpringLayout.EAST, VideoSettings.bitrate, 0, SpringLayout.EAST, VideoSettings.presetSelector);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.vBLbl, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.bitrate);
		
		layout.putConstraint(SpringLayout.EAST, VideoSettings.pLbl, 0, SpringLayout.EAST, VideoSettings.vBLbl);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.pLbl, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.presetSelector);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, VideoSettings.framerate, 0, SpringLayout.HORIZONTAL_CENTER, resetToDefault);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.framerate, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.bitrate);
		layout.putConstraint(SpringLayout.HEIGHT, VideoSettings.framerate, 0, SpringLayout.HEIGHT, VideoSettings.bitrate);
		layout.putConstraint(SpringLayout.EAST, VideoSettings.framerate, 0, SpringLayout.EAST, resetToDefault);
		layout.putConstraint(SpringLayout.WEST, VideoSettings.framerate, 5, SpringLayout.EAST, VideoSettings.fpsLbl);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.fpsLbl, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.WEST, VideoSettings.fpsLbl, 0, SpringLayout.WEST, resetToDefault);
		
		layout.putConstraint(SpringLayout.HEIGHT, VideoSettings.constRateFrame, 0, SpringLayout.HEIGHT, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.WIDTH, VideoSettings.constRateFrame, 0, SpringLayout.WIDTH, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, VideoSettings.constRateFrame, 0, SpringLayout.HORIZONTAL_CENTER, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.NORTH, VideoSettings.constRateFrame, 5, SpringLayout.SOUTH, VideoSettings.framerate);
		
		layout.putConstraint(SpringLayout.HEIGHT, VideoSettings.qpRate, 0, SpringLayout.HEIGHT, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.WIDTH, VideoSettings.qpRate, 0, SpringLayout.WIDTH, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, VideoSettings.qpRate, 0, SpringLayout.HORIZONTAL_CENTER, VideoSettings.framerate);
		layout.putConstraint(SpringLayout.NORTH, VideoSettings.qpRate, 5, SpringLayout.SOUTH, VideoSettings.constRateFrame);

		layout.putConstraint(SpringLayout.EAST, VideoSettings.crfLbl, 0, SpringLayout.EAST, VideoSettings.fpsLbl);
		layout.putConstraint(SpringLayout.EAST, VideoSettings.qpLbl, 0, SpringLayout.EAST, VideoSettings.fpsLbl);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.crfLbl, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.constRateFrame);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, VideoSettings.qpLbl, 0, SpringLayout.VERTICAL_CENTER, VideoSettings.qpRate);
		
		layout.putConstraint(SpringLayout.NORTH, AudioSettings.bitrate, 0, SpringLayout.NORTH, resetToDefault);
		layout.putConstraint(SpringLayout.SOUTH, AudioSettings.bitrate, 0, SpringLayout.SOUTH, resetToDefault);
		
		layout.putConstraint(SpringLayout.EAST, AudioSettings.bitrate, 0, SpringLayout.EAST, VideoSettings.presetSelector);
		layout.putConstraint(SpringLayout.WEST, AudioSettings.bitrate, 0, SpringLayout.WEST, VideoSettings.presetSelector);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, AudioSettings.aBLbl, 0, SpringLayout.VERTICAL_CENTER, AudioSettings.bitrate);
		layout.putConstraint(SpringLayout.EAST, AudioSettings.aBLbl, 0, SpringLayout.EAST, VideoSettings.pLbl);
		
		layout.putConstraint(SpringLayout.WEST, AudioSettings.frequency, 0, SpringLayout.WEST, AudioSettings.bitrate);
		layout.putConstraint(SpringLayout.EAST, AudioSettings.frequency, 0, SpringLayout.EAST, AudioSettings.bitrate);
		layout.putConstraint(SpringLayout.HEIGHT, AudioSettings.frequency, 0, SpringLayout.HEIGHT, AudioSettings.bitrate);
		
		layout.putConstraint(SpringLayout.HEIGHT, VideoSettings.bitrate, 0, SpringLayout.HEIGHT, AudioSettings.bitrate);
		layout.putConstraint(SpringLayout.SOUTH, AudioSettings.frequency, -5, SpringLayout.NORTH, AudioSettings.bitrate);
		
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, AudioSettings.aFLbl, 0, SpringLayout.VERTICAL_CENTER, AudioSettings.frequency);
		layout.putConstraint(SpringLayout.EAST, AudioSettings.aFLbl, 0, SpringLayout.EAST, AudioSettings.aBLbl);
		
		//// These sizes are to be used for formatting.
		VideoSettings.addTo(this);
		AudioSettings.addTo(this);
		this.add(resetToDefault);
		
		this.setLayout(layout);
	}
	
	public static void enableElements() {
		VideoSettings.enableElements();
		AudioSettings.enableElements();
	}
	
	public static void disableElements() {
		VideoSettings.disableElements();
		AudioSettings.disableElements();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetToDefault) {
			PropertiesWriter.createDefault();
			VideoSettings.loadSettings();
			AudioSettings.loadSettings();
		}
	}
}
