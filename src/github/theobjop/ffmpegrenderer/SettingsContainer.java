package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SettingsContainer extends JPanel implements ActionListener {
	
	JButton resetToDefault;
	
	public SettingsContainer() {
		this.setLayout(null);
		this.setBounds(0, 56, 441, 284); // 313
		
		JPanel borderPanel = new JPanel();
		borderPanel.setLayout(null);
		borderPanel.setBounds(7, 7, 430, 279); // 308
		borderPanel.setBorder(new TitledBorder("Settings"));
		
		resetToDefault = new JButton("Defaults");
		resetToDefault.setBounds(341, 246, 80, 24);
		resetToDefault.addActionListener(this);
		
		//// These sizes are to be used for formatting.
		VideoSettings.addTo(borderPanel);
		AudioSettings.addTo(borderPanel);
		
		borderPanel.add(resetToDefault);
		this.add(borderPanel);
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
