package vidVox.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

import vidVox.guiScreens.AddCommentaryScreen;

public class GetDuration extends SwingWorker<Void,String>{
	private String file;
	public String duration;
	private String line,hour,minute,second;;
	private AddCommentaryScreen commentary;
	private String  commentaryContent;
	@Override
	protected Void doInBackground() throws Exception {

		//creating the bash process which will speak the user high score in festival
		//String cmd = "echo "+"\""+text+"\""+" | festival --tts";
		System.out.println(file);
		String cmd=	"ffprobe -i \"" + file + "\" -show_entries format=duration 2>&1 | grep \"duration=\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		
		
		try {
			Process process = x.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			line = stdoutBuffered.readLine();
			System.out.println(line);
			String[] temp=line.split("=");
			line=temp[1];
			System.out.println(line);
			process.waitFor();
			stdoutBuffered.close();
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return null;
	}
	//Constructor
	public GetDuration(String file,AddCommentaryScreen commentary,String hour, String minute,String second,String commentaryContent){
		this.file = file;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.commentaryContent= commentaryContent;
	}

	@Override
	public void done(){
		commentary.addToTable(this.commentaryContent,this.line,this.hour,this.minute,this.second,this.file);
	}

}
