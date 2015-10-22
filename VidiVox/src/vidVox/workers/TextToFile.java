package vidVox.workers;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

import vidVox.guiScreens.AddCommentaryScreen;

public class TextToFile extends SwingWorker<Void, String>{
	//
	//text = text that needs to be spoken in the mp3
	//location = location of where the mp3 will be saved
	private String text;
	private String filename;
	private String location;
	private Boolean overlay;
	private AddCommentaryScreen commentary;
	private String commentaryContent;
	String hour,minute,second;
	@Override
	protected Void doInBackground() throws Exception {
		commentaryContent = text;
		text = text.trim();
		filename = text.replaceAll("\\s+","");
		location = location.replaceAll("\\s+","");

		//creating the bash process which will create a wav file from a text file
		String cmd = "echo "+"\""+text+"\""+" > \"/tmp/"+filename+"\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//text is the text which needs to be spoken
	public TextToFile (String text, String location, AddCommentaryScreen commentary,String hour, String minute,String second){
		this.text = text;
		this.location = location;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
	}
	
	//This is the done method which will turn my text to a wave file.
	protected void done(){
		TextToWav k = new TextToWav(this.location, this.filename, this.commentary,this.hour,this.minute,this.second,this.commentaryContent);
		k.execute();

	}

}
