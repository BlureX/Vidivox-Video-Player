package vidVox.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

import vidVox.gui.MainPlayerScreen;

/**
 * @author jxu811
 * This allows me to place audio over a video.
 */
public class OverlayMp3OntoVideo extends SwingWorker<Void, String>{
	//Fields used for my class.
	private String command;
	private String filename;
	private Boolean overlay;
	private MainPlayerScreen mainPlayer;


	@Override
	protected Void doInBackground() throws Exception {

		//This will overlay MP3 onto video
		mainPlayer.loadingScreen.setVisible(true);
		//String cmd = "ffmpeg -y -i "+"\""+originalVideo+"\""+" -i "+"\""+audio+"\""+" -filter_complex amix -strict -2 \"/tmp/V"+filename+TextToMp3Screen.videoNumber+".mp4"+"\"";
		command = command + "\"/tmp/V"+filename+mainPlayer.videoNumber+".mp4"+"\"";

		String cmd = command;
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//Constructor used for my class.
	public OverlayMp3OntoVideo (String command, String filename, Boolean overlay,MainPlayerScreen mainPlayer) {
		this.command = command;
		this.filename = filename;
		this.overlay = overlay;
		this.mainPlayer=mainPlayer;
	}
	//This is code executed when my video has been overlayed. It will run the TexttoMP3 screen.
	protected void done(){
		MainPlayerScreen.mediapath = "/tmp/V"+filename+MainPlayerScreen.videoNumber+".mp4";
		mainPlayer.getPlay().setText("pause");
		mainPlayer.run();
		mainPlayer.loadingScreen.setVisible(false);
		mainPlayer.videoNumber++;
	}

}




