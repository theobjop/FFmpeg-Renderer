package github.theobjop.ffmpegrenderer;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import bsh.util.JConsole;

@SuppressWarnings("serial")
public class Console extends JPanel {
	
	JConsole _console;
	
	public Console() {
		this.setBounds(7, 340, 427, 116);
		
		_console = new JConsole();
		
		SpringLayout sl = new SpringLayout();
		sl.putConstraint(SpringLayout.NORTH, _console, 4, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.SOUTH, _console, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.EAST, _console, 0, SpringLayout.EAST, this);
		sl.putConstraint(SpringLayout.WEST, _console, 0, SpringLayout.WEST, this);
		
		this.setLayout(sl);
		this.add(_console);
	}
	
	public static void write(String str) {
		Renderer.getConsole()._console.println(str);
	}
}