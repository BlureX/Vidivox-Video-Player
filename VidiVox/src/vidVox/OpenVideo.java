package vidVox;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidVox.guiScreens.MainPlayerScreen;
import vidVox.guiScreens.TextToMp3Screen;

/**
 * @author jxu811
 * This class is used to open a video file.
 */
public class OpenVideo {
	//This will choose my file and also a variable for my media path.
	public JFileChooser ourFileSelector= new JFileChooser();
	private String mediaPath="";
	private String videoName="";

	/**
	 * @return
	 * This will return a boolean to show that the file the user wanted to grab is successful.
	 */
	public boolean grabFile() {

		//File we want to choose.
		File ourFile;

		FileFilter filter = new FileNameExtensionFilter("MP4 & AVI","mp4","avi");
		ourFileSelector.resetChoosableFileFilters();
		ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ourFileSelector.setFileFilter(filter);
		//This will open up a dialog which lets us choose our file and also check that the user has chosen the open option.
		int checkFile = ourFileSelector.showOpenDialog(null);
		if (checkFile==0){
			//Get selected file.
			ourFile=ourFileSelector.getSelectedFile();
			if (ourFile.exists()){
				setMediaPath(ourFile.getAbsolutePath());
				setVideoName(ourFile.getName());
				TextToMp3Screen.originalVideo = getMediaPath();
				MainPlayerScreen.saved=true;
				return true;
			}else {
				JOptionPane.showMessageDialog(null, "Invalid File");
				return false;
			}

		} else {
			//Returns false if they chose to cancel.
			return false;
		}

	}

	/**
	 * @return
	 * Returns the media path
	 */
	public String getMediaPath() {
		return mediaPath;
	}

	/**
	 * @param mediaPath
	 * Sets the media path
	 */
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	/**
	 * @return
	 * Returns video name
	 */
	public String getVideoName() {
		return videoName;
	}

	/**
	 * @param videoName
	 * Sets video name
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
}
