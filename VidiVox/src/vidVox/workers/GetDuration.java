package vidVox.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import vidVox.guiScreens.AddCommentaryScreen;

/**
 * @author jxu811
 * This class will allow me to calculate the duration of a commentary or mp3 file.
 */
public class GetDuration extends SwingWorker<Void,String>{
	//Fields that are used in this class
	private String file;
	public String duration;
	private String line,hour,minute,second;;
	private AddCommentaryScreen commentary;
	private String  commentaryContent;
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will calculate the duration of the video
		String cmd=	"ffprobe -i \"" + file + "\" -show_entries format=duration 2>&1 | grep \"duration=\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
		try {
			Process process = x.start();
			//Read the stdout message
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			//This will grab the duration which is outputted in the curent format duration=13:03:21000
			line = stdoutBuffered.readLine();
			String[] temp=line.split("=");
			//Grabs the second part after the equals
			line=temp[1];
			process.waitFor();
			stdoutBuffered.close();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return null;
	}
	
	/**
	 * @param file
	 * @param commentary
	 * @param hour
	 * @param minute
	 * @param second
	 * @param commentaryContent
	 * Constructor for GetDuration
	 */
	public GetDuration(String file,AddCommentaryScreen commentary,String hour, String minute,String second,String commentaryContent){
		this.file = file;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.commentaryContent= commentaryContent;
	}

	//This will add to the table after it has calculated the duration of the commentary or mp3file.
	@Override
	public void done(){
		commentary.addToTable(this.commentaryContent,this.line,this.hour,this.minute,this.second,this.file);
	}

}
