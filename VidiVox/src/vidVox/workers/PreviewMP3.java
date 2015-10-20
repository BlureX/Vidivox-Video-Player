package vidVox.workers;

import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;
//
//uses festival and bash to preview the text entered in the text box when the button is pressed
public class PreviewMP3 extends SwingWorker<Void, String>{

	private String file;
	public boolean speaking=true;
	
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival
		//String cmd = "echo "+"\""+text+"\""+" | festival --tts";
		String cmd="mpg123 " + file + " >& /dev/null";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
		Thread.sleep(100);
		
		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return null;
	}
	//Constructor
	public PreviewMP3(String file){
		this.file = file;
	}
	
	public String getFile(){
		return file;
	}
	@Override
	public void done(){
		this.speaking=false;
	}
}
