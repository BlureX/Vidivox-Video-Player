package vidVox.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

import fileconverter.WavToMp3;

import vidVox.gui.AddCommentaryScreen;

/**
 * @author jxu811
 * This is a class which will work to add different type of voices.
 */
public class FestivalSpeechWorker extends SwingWorker<Void,Void>{
	//Fields which are used in this class
	private Process process;
	private int voice,counter;
	private AddCommentaryScreen commentary;
	private String commentaryContent,hour,minute,second,location,filename,voiceType; 

	@Override
	protected Void doInBackground() throws Exception {
		String cmd;
		filename = "festSpeech" + counter;
		//This option indicates kiwi voice type will be used
		if (voice==1){
			cmd="echo \"" + commentaryContent + "\" | text2wave -o \"" +location+".wav\"" + " -eval \"(voice_akl_nz_jdt_diphone)\"";
		//This option indicates British voice type will be used
		}else{
			cmd="echo \"" + commentaryContent + "\" | text2wave -o \"" +location+".wav\"" + " -eval \"(voice_rab_diphone)\"";
		}
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );
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
	public FestivalSpeechWorker(String commentaryContent,
								String location, 
								AddCommentaryScreen commentary,
								String hour, 
								String minute,
								String second,
								int voice,
								int counter,
								String voiceType){
		this.commentaryContent=commentaryContent;
		this.location=location;
		this.commentary=commentary;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.voice=voice;
		this.counter=counter;
		this.voiceType=voiceType;
	}

	//Converts the wav to mp3
	@Override
	public void done(){
		WavToMp3 k = new WavToMp3(this.location, filename,this.commentary,this.hour,this.minute,this.second,this.commentaryContent,this.voiceType);
		k.execute();
	}

}
