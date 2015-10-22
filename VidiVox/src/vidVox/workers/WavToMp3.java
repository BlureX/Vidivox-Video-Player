package vidVox.workers;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.DateFormat.Field;
import javax.swing.SwingWorker;

import vidVox.guiScreens.AddCommentaryScreen;

public class WavToMp3 extends SwingWorker<Void, String>{
	//
	//location = location of where the mp3 will be saved
	//filename = what the file is called
	private String location;
	private String filename;
	private AddCommentaryScreen commentary;
	String hour,minute,second;
	private String  commentaryContent;
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will change a wav file into a mp3 file and store it in
		//the home directory

		if (!(location.endsWith(".mp3"))){
			location = location+".mp3";
		}

		String cmd = "ffmpeg -y -i \"/tmp/"+filename+".wav\" -codec:a libmp3lame -qscale:a 2 "+"\""+location+"\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public WavToMp3 (String location, String filename,AddCommentaryScreen commentary,String hour, String minute,String second,String commentaryContent){
		this.location = location;
		this.filename = filename;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.commentaryContent= commentaryContent;
	}

	protected void done(){
		GetDuration duration = new GetDuration(this.location,this.commentary,this.hour,this.minute,this.second,this.commentaryContent);
		duration.execute();
		
	}

}
