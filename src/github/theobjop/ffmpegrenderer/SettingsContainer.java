package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SettingsContainer extends JPanel implements ActionListener {

    public JButton resetToDefault;

	public SettingsContainer() {
		this.setBounds(5, 62+24+6, 430, 278-24-6); // 313
        // Get Preset from PropertiesWriter
		this.setBorder(new TitledBorder("Settings"));

		resetToDefault = new JButton("Defaults");
		resetToDefault.addActionListener(this);

		//// These sizes are to be used for formatting.
		this.add(resetToDefault);
	}

	public void addSettings(Settings set) {
		// We removed everything, so re add the button..
		this.add(resetToDefault);
		set.setParent(this);
        set.addAll();
		set.setLayout();
		set.getLayout().putConstraint(SpringLayout.SOUTH, resetToDefault, -7, SpringLayout.SOUTH, this);
		set.getLayout().putConstraint(SpringLayout.EAST, resetToDefault, -7, SpringLayout.EAST, this);
		this.setLayout(set.getLayout());
		this.revalidate();
		this.repaint();
	}
	
    public static JPanel getInstance() {
        return Renderer.getSettingsContainer();
    }

    public static void setElementsEnabled(boolean f) {
    	Renderer.getSettings().setElementsEnabled(f);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetToDefault) {
			Renderer.getSettings().setDefaults();
		}
	}
}
