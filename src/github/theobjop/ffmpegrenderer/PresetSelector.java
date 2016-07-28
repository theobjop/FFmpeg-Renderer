package github.theobjop.ffmpegrenderer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PresetSelector extends JComboBox<String> implements ActionListener {
	
	JPanel container;
	static String[] defaultSelections = { "x264", "YouTube" };
	
	public PresetSelector() {
		super(defaultSelections);
		container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setBounds(7, 66, 427, 24);

		String t = PropertiesWriter.getLastPreset().type;
		this.setSelectedItem(t);
		this.addActionListener(this);
		container.add(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Renderer.LoadPreset((String)this.getSelectedItem());
		} catch (Exception ed) {
			// TODO Auto-generated catch block
			for (StackTraceElement ste : ed.getStackTrace()) {
				System.out.println(ste);
			}
		}
	}
}
