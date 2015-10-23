package vidVox;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class MoveFile extends SwingWorker<Void, String>{
	//Fields used in my class.
	private String file;
	private String location;
	
	//Background process which just copies and pastes a file into a certain location specified by the constructor.
	@Override
	protected Void doInBackground() throws Exception {
		//
		String cmd = "cp "+"\""+file+"\""+" "+"\""+location+"\"";
		ProcessBuilder x = new ProcessBuilder("/bin/bash", "-c", cmd );

		try {
			Process process = x.start();
			process.waitFor();
		} catch (IOException e) {
			
		}
		return null;

	}

	public MoveFile (String video, String location){
		this.file = video;
		this.location = location;
	}
	@Override
	public void done(){
		JOptionPane.showMessageDialog(null, "Done");
	}

}
