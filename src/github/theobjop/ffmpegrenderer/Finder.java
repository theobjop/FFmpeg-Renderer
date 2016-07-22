package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class Finder extends JPanel implements ActionListener {

	// We'll store them here, why not?
	String exeFromSaveFile, aviFromSaveFile;
	
	JTextField exeLocField;
	public JButton exeLocBrowse;
	JFileChooser exeOpenFile;
	
	JTextField aviLocField;
	JButton aviLocBrowse;
	JFileChooser aviOpenFile;
	
	public Finder(String exeFromSaveFile, String aviFromSaveFile) {
		//super(new SpringLayout());
		//// Create area in which the panel is..
		this.setBounds(7, 7, 427, 55);
		
		// Store for later
		this.exeFromSaveFile = exeFromSaveFile == null ? "" : exeFromSaveFile;
		this.aviFromSaveFile = aviFromSaveFile == null ? "" : aviFromSaveFile;
		
		//// Executable Location Label, Field, and Browse
		JLabel exeLocLabel = new JLabel("FFmpeg Exe:");
		
		exeLocField = new JTextField();
		
		if (!this.exeFromSaveFile.isEmpty())
			exeLocField.setText(this.exeFromSaveFile);
		
		exeLocBrowse = new JButton("Browse");
		
		exeOpenFile = new JFileChooser();
		exeOpenFile.addActionListener(this);		
		exeLocBrowse.addActionListener(this);
		//////////////////////////////////////////////////////////////

		
		//// AVI Location Label, Field, and Browse
		JLabel aviLocLabel = new JLabel("Locate AVI:");
		
		aviLocField = new JTextField();
		
		if (!this.aviFromSaveFile.isEmpty())
			aviLocField.setText(this.aviFromSaveFile);

		aviLocBrowse = new JButton("Browse");

		aviOpenFile = new JFileChooser();
		aviOpenFile.addActionListener(this);
		aviLocBrowse.addActionListener(this);
		aviOpenFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//////////////////////////////////////////////////////////////
		
		
		//// Finalize styling - Create Layout
		SpringLayout sl = new SpringLayout();
		
		// X Coordinate
		sl.putConstraint(SpringLayout.WEST, exeLocLabel, 0, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.HORIZONTAL_CENTER, aviLocLabel, 0, SpringLayout.HORIZONTAL_CENTER, exeLocLabel);
		sl.putConstraint(SpringLayout.VERTICAL_CENTER, aviLocLabel, 0, SpringLayout.VERTICAL_CENTER, aviLocBrowse);

		sl.putConstraint(SpringLayout.VERTICAL_CENTER, exeLocLabel, 0, SpringLayout.VERTICAL_CENTER, exeLocBrowse);
		
		sl.putConstraint(SpringLayout.VERTICAL_CENTER, exeLocField, 0, SpringLayout.VERTICAL_CENTER, exeLocBrowse);

		sl.putConstraint(SpringLayout.VERTICAL_CENTER, aviLocField, 0, SpringLayout.VERTICAL_CENTER, aviLocBrowse);
		sl.putConstraint(SpringLayout.WEST, aviLocField, 0, SpringLayout.WEST, exeLocField);
		sl.putConstraint(SpringLayout.SOUTH, aviLocField, 0, SpringLayout.SOUTH, aviLocBrowse);
		sl.putConstraint(SpringLayout.EAST, aviLocField, -4, SpringLayout.WEST, aviLocBrowse);
		
		sl.putConstraint(SpringLayout.SOUTH, exeLocField, 0, SpringLayout.SOUTH, exeLocBrowse);
		
		sl.putConstraint(SpringLayout.WEST, exeLocField, 5, SpringLayout.EAST, exeLocLabel);
		
		sl.putConstraint(SpringLayout.EAST, exeLocField, -4, SpringLayout.WEST, exeLocBrowse);
		
		sl.putConstraint(SpringLayout.EAST, exeLocBrowse, 0, SpringLayout.EAST, this);
		sl.putConstraint(SpringLayout.EAST, aviLocBrowse, 0, SpringLayout.EAST, this);		
		
		// Y Coordinate
		sl.putConstraint(SpringLayout.NORTH, exeLocLabel, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, exeLocField, 0, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, exeLocBrowse, 0, SpringLayout.NORTH, this);
		
		sl.putConstraint(SpringLayout.NORTH, aviLocField, 5, SpringLayout.SOUTH, exeLocField);
		sl.putConstraint(SpringLayout.NORTH, aviLocBrowse, 5, SpringLayout.SOUTH, exeLocBrowse);
		
		sl.putConstraint(SpringLayout.SOUTH, aviLocField, 0, SpringLayout.SOUTH, this);
		sl.putConstraint(SpringLayout.SOUTH, aviLocBrowse, 0, SpringLayout.SOUTH, this);
		
		sl.putConstraint(SpringLayout.SOUTH, exeLocField, 0, SpringLayout.SOUTH, exeLocBrowse);
		sl.putConstraint(SpringLayout.SOUTH, aviLocField, 0, SpringLayout.SOUTH, aviLocBrowse);
		
		this.setLayout(sl);
		
		this.add(exeLocLabel);
		this.add(exeLocField);
		this.add(exeLocBrowse);
		
		this.add(aviLocLabel);
		this.add(aviLocField);
		this.add(aviLocBrowse);
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
