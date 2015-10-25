package fileconverter;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

import vidVox.gui.AddCommentaryScreen;

/**
 * @author jxu811
 * This class converts the text file into a wav file
 */
public class TextToWav extends SwingWorker<Void, String>{

	//This is the fields which will be used in the class
	private String location;
	private String filename;
	private AddCommentaryScreen commentary;
	private String commentaryContent;
	private String hour,minute,second;
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will create a wav file from a text file
		//the wav file is named after the text which is spoken in it, and it is stored in /tmp/
		// e.g hello is being spoken, /tmp/hello.wav exists
		String cmd = "text2wave \"/tmp/"+filename+"\""+" -o \"/tmp/"+filename+".wav"+"\"";

		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * @param location
	 * @param filename
	 * @param commentary
	 * @param hour
	 * @param minute
	 * @param second
	 * @param commentaryContent
	 * Constructor for my class
	 */
	public TextToWav (String location, String filename, AddCommentaryScreen commentary,String hour, String minute,String second,String  commentaryContent){
		this.location = location;
		this.filename = filename;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.commentaryContent=commentaryContent;
	}


	protected void done(){
		WavToMp3 k = new WavToMp3(this.location, this.filename,this.commentary,this.hour,this.minute,this.second,this.commentaryContent);
		k.execute();
	}
}
