package vidVox.workers;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

/**
 * @author jxu811
 * This class is used to allow you to preview the text or mp3 audio you have added to the table
 */
public class PreviewMP3 extends SwingWorker<Void, String>{

	private String file;
	public boolean speaking=true;
	Process process;

	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival

		String cmd="ffplay -nodisp -autoexit -af volume=1 \"" + file+ "\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
		Thread.sleep(100);

		try {
			process = x.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * @param file
	 * Constructor
	 */
	public PreviewMP3(String file){
		this.file = file;
	}


	public void destroyProcess(){
		process.destroy();
	}
	/**
	 * @return
	 * Get File name
	 */
	public String getFile(){
		return file;
	}
	@Override
	public void done(){
		this.speaking=false;
	}
}
