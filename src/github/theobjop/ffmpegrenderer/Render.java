package github.theobjop.ffmpegrenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
public class Render extends JPanel implements ActionListener {

	JButton renderBtn;
	JFileChooser openFile;
	JProgressBar progBar;
	int maxFrame;

	public Render() {
		this.setBounds(7, 456, 427, 35);

		renderBtn = new JButton("Render");
		renderBtn.setBounds(357, 4, 77, 24);
		renderBtn.addActionListener(this);

		progBar = new JProgressBar();
		progBar.setStringPainted(true);
		progBar.setBounds(0, 4, 346, 24);

		openFile = new JFileChooser("Save");
		openFile.setDialogType(JFileChooser.SAVE_DIALOG);
		openFile.setFileFilter(new FileNameExtensionFilter("MP4 Files","mp4"));

		SpringLayout sl = new SpringLayout();
		sl.putConstraint(SpringLayout.NORTH, renderBtn, 4, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.NORTH, progBar, 4, SpringLayout.NORTH, this);
		sl.putConstraint(SpringLayout.SOUTH, progBar, 0, SpringLayout.SOUTH, renderBtn);

		sl.putConstraint(SpringLayout.WEST, progBar, 0, SpringLayout.WEST, this);
		sl.putConstraint(SpringLayout.EAST, progBar, -4, SpringLayout.WEST, renderBtn);
		sl.putConstraint(SpringLayout.EAST, renderBtn, 0, SpringLayout.EAST, this);

		this.setLayout(sl);

		this.add(progBar);
		this.add(renderBtn);
	}

	public void execute(String fileName) {
		if (!FilenameUtils.getExtension(fileName).equalsIgnoreCase("mp4")) {
			fileName += ".mp4";  // append .xml if "foo.jpg.mp4" is OK
		}

		// Attempt to create process with all the settings, if error then don't continue.
		try {
			//// Get all locations
			String exeloc = Renderer.getLocation();
			String inloc = AvsWriter.CreateAVS(Renderer.getStreamFile()).getAbsolutePath();
			String outloc = fileName;
			///////////////////////////////////////////////////////////////

			//// Threads for Processes, one for frameCount, one for the Encoder
			RenderProcess proc = new RenderProcess(exeloc, inloc, outloc);
			FrameRetriever fr = new FrameRetriever(proc, progBar, VideoSettings.getFPS(), exeloc, inloc);
			///////////////////////////////////////////////////////////////

			fr.execute();

			progBar.setMinimum(0);
			renderBtn.setEnabled(false);
			SettingsContainer.disableElements();
		} catch (Exception err) {
			JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, err.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == renderBtn) {
			try {
				File f = new File(Renderer.getAbsoluteStreamLocation());
				String fileName = Renderer.getStreamFile();
				int returnVal = 0;
				if (f.isFile()) {
					returnVal = openFile.showSaveDialog(Renderer.FFMPEGRENDERER);

					if (returnVal == JFileChooser.APPROVE_OPTION)
						execute(openFile.getSelectedFile().getAbsolutePath());
				} else {
					execute(fileName);
				}

				Render.reset();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(Renderer.FFMPEGRENDERER, e1.getMessage());
			}
		}
	}

	public static void reset() {
		Renderer.getRender().renderBtn.setEnabled(true);
		Renderer.getRender().progBar.setValue(0);
		Renderer.getRender().progBar.setMinimum(0);
		Renderer.getRender().progBar.setMaximum(0);
		Renderer.getRender().progBar.setString("0%");
	}

	public static void progress(int frame, int fps) {
		if (frame >= Renderer.getRender().progBar.getMaximum()) {
			Renderer.getRender().progBar.setString("DONE!");
			Renderer.FFMPEGRENDERER.setTitle("FFmpeg Renderer");
			Renderer.getRender().renderBtn.setEnabled(true);
			SettingsContainer.enableElements();
			return;
		}
		Renderer.getRender().progBar.setString(String.format("%.2f", Renderer.getRender().progBar.getPercentComplete()*100) + "%");
		Renderer.getRender().progBar.setValue(frame);
		Renderer.FFMPEGRENDERER.setTitle("FFmpeg Renderer - Estimated Time: " + timeConversion(fps));
	}

	private static String timeConversion(int fps) {
		if (fps == 0)
			return "";
		int totalSeconds = (Renderer.getRender().progBar.getMaximum() - Renderer.getRender().progBar.getValue()) / fps;
	    int seconds = totalSeconds % 60;
	    int totalMinutes = totalSeconds / 60;
	    int minutes = totalMinutes % 60;
	    int hours = totalMinutes / 60;
	    return (hours > 0 ? hours + " hours " : "") + (minutes > 0 || totalSeconds > 60 ? minutes + " minutes, " : "") + seconds + " seconds";
	}
}
