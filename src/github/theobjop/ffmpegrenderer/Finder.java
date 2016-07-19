package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class Finder extends JPanel implements ActionListener {

	// We'll store them here, why not?
	String exeFromSaveFile, aviFromSaveFile;
	
	JTextField exeLocField;
	JButton exeLocBrowse;
	JFileChooser exeOpenFile;
	
	JTextField aviLocField;
	JButton aviLocBrowse;
	JFileChooser aviOpenFile;
	
	public Finder(String exeFromSaveFile, String aviFromSaveFile) {
		//// Create area in which the panel is..
		this.setLayout(null);
		this.setBounds(0, 0, 441, 62);
		
		// Store for later
		this.exeFromSaveFile = exeFromSaveFile == null ? "" : exeFromSaveFile;
		this.aviFromSaveFile = aviFromSaveFile == null ? "" : aviFromSaveFile;
		
		// For formatting at the same place
		Rectangle2D bounds = Renderer.font.getStringBounds("FFmpeg Exe: ", Renderer.frc);
		
		//// Executable Location Label, Field, and Browse
		JLabel exeLocLabel = new JLabel("FFmpeg Exe:");
		exeLocLabel.setBounds(7, 11, 125, 14);
		
		exeLocField = new JTextField();
		exeLocField.setBounds((int)bounds.getWidth() + 12, 7, 268, 24);

		if (!this.exeFromSaveFile.isEmpty())
			exeLocField.setText(this.exeFromSaveFile);
		
		exeLocBrowse = new JButton("Browse");
		exeLocBrowse.setBounds(357, 7, 77, 24);
		
		exeOpenFile = new JFileChooser();
		exeOpenFile.addActionListener(this);		
		exeLocBrowse.addActionListener(this);
		
		this.add(exeLocLabel);
		this.add(exeLocField);
		this.add(exeLocBrowse);
		//////////////////////////////////////////////////////////////

		
		//// AVI Location Label, Field, and Browse
		JLabel aviLocLabel = new JLabel("Locate AVI:");
		aviLocLabel.setBounds(7, 40, 125, 14);
		
		aviLocField = new JTextField();
		aviLocField.setBounds((int)bounds.getWidth() + 12, 36, 268, 24);
		
		if (!this.aviFromSaveFile.isEmpty())
			aviLocField.setText(this.aviFromSaveFile);

		aviLocBrowse = new JButton("Browse");
		aviLocBrowse.setBounds(357, 36, 77, 24);

		aviOpenFile = new JFileChooser();
		aviOpenFile.addActionListener(this);
		aviLocBrowse.addActionListener(this);
		aviOpenFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		this.add(aviLocLabel);
		this.add(aviLocField);
		this.add(aviLocBrowse);
		//////////////////////////////////////////////////////////////
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exeLocBrowse)
			exeOpenFile.showOpenDialog(Renderer.FFMPEGRENDERER);
		if (e.getSource() == aviLocBrowse)
			aviOpenFile.showOpenDialog(Renderer.FFMPEGRENDERER);
		
		if (e.getSource() instanceof JFileChooser && e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
			if (e.getSource() == exeOpenFile) {
				File file = exeOpenFile.getSelectedFile();
				exeLocField.setText(file.getPath());
			}
			
			if (e.getSource() == aviOpenFile) {
				File file = aviOpenFile.getSelectedFile();
				aviLocField.setText(file.getPath());
			}
		}
	}
	
	public String getLoc() throws Exception {
		if (exeLocField.getText().isEmpty())
			throw new Exception("FFmpeg location not set.");
		if (!FilenameUtils.getName(exeLocField.getText()).equalsIgnoreCase("ffmpeg.exe"))
			throw new Exception("Invalid FFmpeg file.");
		
		return exeLocField.getText();
	}
	
	public String getStreamFile() throws Exception {
		String loc = aviLocField.getText();
		if (loc.isEmpty())
			throw new Exception("AVI location not set.");
		
		File f = new File(loc);
		
		if (f.isDirectory()) {
			File[] list = f.listFiles();
			for (int i = 0; i < list.length; i++)
				if (FilenameUtils.getExtension(list[i].getPath()).equalsIgnoreCase("avi"))
					return list[i].getAbsolutePath();
			
			throw new Exception("There is no AVI file within directory.");
		}
		
		// Will only reach this part of the code if it's a file.
		if (!FilenameUtils.getExtension(f.getAbsolutePath()).equalsIgnoreCase("avi"))
			throw new Exception("File selected is not an AVI file.");
		
		return f.getAbsolutePath();
	}
	
	public String getAbsoluteStreamLocation() throws Exception {
		String loc = aviLocField.getText();
		if (loc.isEmpty())
			throw new Exception("AVI location not set.");
		
		return loc;
	}
}
