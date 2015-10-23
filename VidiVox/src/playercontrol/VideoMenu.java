package playercontrol;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import vidVox.MoveFile;
import vidVox.OpenVideo;
import vidVox.SaveVideoAs;
import vidVox.guiScreens.MainPlayerScreen;
import vidVox.guiScreens.TextToMp3Screen;

public class VideoMenu {

	//Menu at the top which allows users to select their appropriate options.
	JMenuBar menuBar;
	JMenu video;
	JMenuItem openVideo, saveVideo, saveVideoAs;
	MainPlayerScreen mainPlayer;
	
	public VideoMenu(MainPlayerScreen mainPlayer){
		this.mainPlayer=mainPlayer;
	}
	
	public JMenuBar setUpMenuBar(){
		
		//Creates a Menu bar for the frame
		menuBar = new JMenuBar();
		GridBagConstraints c = new GridBagConstraints();

		//adds a menu to the menu bar
		video = new JMenu("Video");
		menuBar.add(video);

		//open video button
		openVideo = new JMenuItem("Open Video...");	
		video.add(openVideo);

		//save video button
		saveVideo = new JMenuItem("Save Video...");	
		video.add(saveVideo);		

		//save video as button
		saveVideoAs = new JMenuItem("Save Video as...");	
		video.add(saveVideoAs);
		return menuBar;	
	}

	
	

	
	public void setUpMenuListeners(){
	//Allows the user to save video.
	saveVideo.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (MainPlayerScreen.mediapath == null){
				JOptionPane.showMessageDialog(null, "Error please open a video before trying to save.");
			}else{
				MoveFile k = new MoveFile(mainPlayer.mediapath, TextToMp3Screen.originalVideo);
				k.execute();
			}
		}
	});
	//Allows the user to save Video as.
	saveVideoAs.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Uses the method saveVideoAs which will allow you to save the video into a location the user wants.
			mainPlayer.saveVideo.saveVideoAs();
		}
	});
	//This will allow you to choose a file and play it.
	openVideo.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Pauses the current video being played if any.
			mainPlayer.mediaPlayerComponent.getMediaPlayer().setPause(true);
			//Check if the user grabbed a file.
			boolean openfile=OpenVideo.grabFile();
			if (openfile){
				mainPlayer.mediapath=OpenVideo.mediaPath;
				mainPlayer.turnOffRewindAndFastforward();
				mainPlayer.play.setText("pause");
				mainPlayer.run();
				mainPlayer.setTitle(OpenVideo.videoName);
			}
			if (mainPlayer.play.getText().equals("pause")){
				//If user decided to cancel the operation, it will continue playing the video if it is being played.
				mainPlayer.mediaPlayerComponent.getMediaPlayer().play();
			}
		}
	});
	}
	
}
