package github.theobjop.ffmpegrenderer;

import bsh.util.JConsole;

@SuppressWarnings("serial")
public class Console extends JConsole {
	public Console() {
		this.setBounds(9, 344, 427, 110); // 110
	}
	
	public void append(String str) {
		this.print(str + "\n");
	}
	
	public static void write(String str) {
		Renderer.getInstance().getConsole().println(str);
	}
}