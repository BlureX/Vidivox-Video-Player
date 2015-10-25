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
import vidVox.gui.MainPlayerScreen;

/**
 * @author jxu811
 * This class corresponds to the menu bar which is on top of the player.
 */
public class VideoMenu {

	//Menu at the top which allows users to select their appropriate options.
	JMenuBar menuBar;
	JMenu video,help;
	JMenuItem openVideo, saveVideo, saveVideoAs,about;
	MainPlayerScreen mainPlayer;
	OpenVideo openVideoOption=new OpenVideo();

	/**
	 * @param mainPlayer
	 * Constructor for my class
	 */
	public VideoMenu(MainPlayerScreen mainPlayer){
		this.mainPlayer=mainPlayer;
	}

	/**
	 * @return
	 * This will set up my JmenuBar and add the approrpiate items to it
	 * so that my Vidivox functions
	 */
	public JMenuBar setUpMenuBar(){

		//Creates a Menu bar for the frame
		menuBar = new JMenuBar();
		GridBagConstraints c = new GridBagConstraints();

		//adds a menu to the menu bar
		video = new JMenu("Video");
		video.setToolTipText("Allows you to open,save current video or save as current video");
		menuBar.add(video);

		//open video button
		openVideo = new JMenuItem("Open Video...");	
		openVideo.setToolTipText("Opens a video from specified path");
		video.add(openVideo);

		//save video button
		saveVideo = new JMenuItem("Save Video...");	
		saveVideo.setToolTipText("Saves current video with whatever commentary is added");
		video.add(saveVideo);		

		//save video as button
		saveVideoAs = new JMenuItem("Save Video as...");
		saveVideoAs.setToolTipText("Save current video in a specified path");
		video.add(saveVideoAs);

		//Help menu
		help = new JMenu("About");
		help.setToolTipText("Help Menu");
		menuBar.add(help);

		//About button used for helping users
		about = new JMenuItem("Help Menu");	
		about.setToolTipText("Informs user about the functionality of the program");
		help.add(about);

		return menuBar;	
	}





	/**
	 * This will set up my Action listeners for my items which are located in the menu
	 */
	public void setUpMenuListeners(){
		//Allows the user to save video.
		saveVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (MainPlayerScreen.mediapath == null){
					JOptionPane.showMessageDialog(null, "Error, please open a video before trying to save.");
				}else{
					MoveFile k = new MoveFile(mainPlayer.mediapath, mainPlayer.originalVideo,false);
					k.execute();
					MainPlayerScreen.saved=true;
				}
			}
		});
		//Allows the user to save Video as.
		saveVideoAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Pauses the current video being played if any.
				mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
				//Uses the method saveVideoAs which will allow you to save the video into a location the user wants.
				mainPlayer.saveVideo.saveVideoAs();
				if (mainPlayer.getPlay().getText().equals("pause")){
					//If user decided to cancel the operation, it will continue playing the video if it is being played.
					mainPlayer.getMediaPlayerComponent().getMediaPlayer().play();
				}
				MainPlayerScreen.saved=true;
			}
		});
		//This will allow you to choose a file and play it.
		openVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Pauses the current video being played if any.
				mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
				//Check if the user grabbed a file.
				boolean openfile=openVideoOption.grabFile();
				if (openfile){
					mainPlayer.mediapath=openVideoOption.getMediaPath();
					mainPlayer.getPlayback().turnOffRewindAndFastforward();
					mainPlayer.getPlay().setText("pause");
					mainPlayer.addCommentaryScreen.clearCommentary();
					mainPlayer.run();
					mainPlayer.setTitle(openVideoOption.getVideoName());
				}
				if (mainPlayer.getPlay().getText().equals("pause")){
					//If user decided to cancel the operation, it will continue playing the video if it is being played.
					mainPlayer.getMediaPlayerComponent().getMediaPlayer().play();
				}
			}
		});
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null, "Hover over the componenets of Vidivox to get an idea of what " +
							"the compnenets are meant to do. Also reading the user manual will help users.");

				}
			
		});
	}

}
