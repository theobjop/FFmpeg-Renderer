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

    private static JButton resetToDefault;

	public SettingsContainer() {
		layout = new SpringLayout();

        this.setBounds(5, 62, 430, 278); // 313
        // Get Preset from PropertiesWriter
		this.setBorder(new TitledBorder("Settings"));

		resetToDefault = new JButton("Defaults");
		layout.putConstraint(SpringLayout.SOUTH, resetToDefault, -7, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, resetToDefault, -7, SpringLayout.EAST, this);
		resetToDefault.addActionListener(this);

		//// These sizes are to be used for formatting.
		AudioSettings.addTo(this);
		this.add(resetToDefault);

		this.setLayout(layout);
	}

    public static void enableElements() {
		AudioSettings.enableElements();
	}

	public static void disableElements() {
		AudioSettings.disableElements();
	}

    public static JPanel getInstance() {
        return Renderer.getSettingsContainer();
    }

    public static JButton getResetButton() {
        return resetToDefault;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetToDefault) {
			PropertiesWriter.createDefault();
			AudioSettings.loadSettings();
		}
	}
}
