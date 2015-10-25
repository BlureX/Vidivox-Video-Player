package vidVox;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import vidVox.gui.MainPlayerScreen;
import vidVox.gui.SaveDialogScreen;

/**
 * @author jxu811
 * This class will be used to save the current video as a new video.
 */
public class SaveVideoAs {

	/**
	 * Method which allows you to save video as.
	 */
	public void saveVideoAs(){

		//Declare my variables
		String mediaPath1;
		File ourFile1;

		//Opens the file chooser and if there is no video playing, return an error.
		if (MainPlayerScreen.mediapath == null){
			JOptionPane.showMessageDialog(null, "Error please open a video before trying to save");
		}else{
			//Applys a file filter so that you can only see mp4 and avi files
			FileFilter filter = new FileNameExtensionFilter("MP4 & AVI","mp4","avi");
			SaveDialogScreen.ourFileSelector.resetChoosableFileFilters();
			SaveDialogScreen.ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
			SaveDialogScreen.ourFileSelector.setSelectedFile(new File(""));	
			SaveDialogScreen.ourFileSelector.setFileFilter(filter);
			int status = SaveDialogScreen.ourFileSelector.showSaveDialog(null); 
			//Checks if user selected to save
			if (status == JFileChooser.APPROVE_OPTION){
				if (!(SaveDialogScreen.ourFileSelector.getSelectedFile() == null)){

					ourFile1=SaveDialogScreen.ourFileSelector.getSelectedFile();
					mediaPath1=ourFile1.getAbsolutePath();
					if ((!(mediaPath1.endsWith(".mp4"))) && (!(mediaPath1.endsWith(".avi")))) {
						mediaPath1 = mediaPath1+".avi";
					}
					
					MoveFile k = new MoveFile(MainPlayerScreen.mediapath, mediaPath1,false);
					k.execute();
					
					
				}
			}
		}
	}

}
