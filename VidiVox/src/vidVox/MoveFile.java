package vidVox;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import vidVox.gui.TextToMp3Screen;

/**
 * @author jxu811
 * This class will allow me to use the bash command cp to copy a file to a stated location
 *
 */
public class MoveFile extends SwingWorker<Void, String>{
	//Fields used in my class.
	private String file;
	private String location;
	private boolean mp3=false;

	//Background process which just copies and pastes a file into a certain location specified by the constructor.
	@Override
	protected Void doInBackground() throws Exception {
		if (mp3==false){
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(true);
		}
		String cmd = "cp "+"\""+file+"\""+" "+"\""+location+"\"";

		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {

		}
		return null;

	}

	/**
	 * @param video
	 * @param location
	 * Constructor which takes in the file location and the location the file is suppose to be copied to.
	 */
	public MoveFile (String video, String location,Boolean isMP3){
		this.file = video;
		this.location = location;
		mp3=isMP3;
	}

	@Override
	public void done(){
		//Once it is done, it will display a message.
		if (mp3==false){
		TextToMp3Screen.mainPlayerScreen.loadingScreen.setVisible(false);
		}
		JOptionPane.showMessageDialog(null, "Done");
	}

}
