package vidVox.guiScreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import playercontrol.PositionSlider;
import playercontrol.VideoMenu;
import playercontrol.VideoTime;
import playercontrol.VolumeControl;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import vidVox.MoveFile;
import vidVox.OpenVideo;
import vidVox.SaveVideoAs;
import vidVox.workers.Skip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainPlayerScreen extends JFrame {
	//Fields which are used within this class and package.
	MainPlayerScreen mainplayer = this;
	private JPanel topPane, bottomPane;
	public EmbeddedMediaPlayerComponent mediaPlayerComponent;
	Skip ffswing, rwswing;
	public static String mediapath;
	public static LoadingScreen loadingScreen = new LoadingScreen();
	public static AddCommentaryScreen addCommentaryScreen;
	private boolean pressedWhilePlaying = false;
	private boolean ff=false, rw=false;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	VolumeControl volumeFunctionality;
	public PositionSlider progressSlider;
	VideoTime videoTime;
	//Set up the GridBag Layout for my screen.
	GridBagLayout gbl_topPane;
	GridBagLayout gbl_bottomPane;
	GridBagConstraints c;
	
	//Buttons, sliders and labels which are used in my GUI for users to click.
	JButton fastforward, rewind;
	public JButton play;
	JButton addCommentaryButton;

	public SaveVideoAs saveVideo = new SaveVideoAs();

	//Menu at the top which allows users to select their appropriate options.
	VideoMenu videoMenu;

//	/**
//	 * Main Method used to start my application.
//	 * Also used code from https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java for
//	 * additional features such as progress bar.
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		//Initialising all the screens which will be used in the video player.
//		MainPlayerScreen frame = new MainPlayerScreen();
//		frame.setBounds(300, 200, 820, 650);
//		frame.setMinimumSize(new Dimension(820, 1));
//		frame.setVisible(true);
//
//		loadingScreen.setBounds(510, 495, 400, 60);
//		loadingScreen.setMinimumSize(new Dimension(400, 60));
//		addCommentaryScreen = new AddCommentaryScreen(frame);
//		addCommentaryScreen.setBounds(285, 475, 850, 500);
//		addCommentaryScreen.setMinimumSize(new Dimension(650, 500));
//	}

	/**
	 * Constructor for my class.
	 */
	public MainPlayerScreen() {
		loadingScreen.setBounds(510, 495, 400, 60);
		loadingScreen.setMinimumSize(new Dimension(400, 60));
		addCommentaryScreen = new AddCommentaryScreen(mainplayer);
		addCommentaryScreen.setBounds(285, 475, 850, 500);
		addCommentaryScreen.setMinimumSize(new Dimension(650, 500));
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!(mediapath == TextToMp3Screen.originalVideo)){
					Object[] options = { "Save", "Save as.." , "Exit without saving" };
					int choice = JOptionPane.showOptionDialog(null, 
							"Save changes to your video before closing?", 
							"Warning video has changes which are not saved", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, 
							options, 
							options[0]);
					if (choice == 0) {
						if (MainPlayerScreen.mediapath == null){
							JOptionPane.showMessageDialog(null, "Error please open a video before trying to save");
						} else { 
							MoveFile k = new MoveFile(mediapath, TextToMp3Screen.originalVideo);
							k.execute();
						}
					} else if (choice == 1) {
						//save video as
						saveVideo.saveVideoAs();
					} else {
						//closing without saving 
					}
				}
			}
		});

		//making the main initial layout
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 490);
		c = new GridBagConstraints();		
		//Runs the methods in this class which establish the buttons and their
		//appropriate action.
		setUpLayout();
		setUpListeners();
	}

	
	/**
	 * This will run the video from the media path provided.
	 */
	public void run() {
		//This will run the video.
		mediaPlayerComponent.getMediaPlayer().playMedia(mediapath);
		//This will get the current time of the video.
		final long time = mediaPlayerComponent.getMediaPlayer().getTime();
		//This will get the position of the video.
		final int position = (int)(mediaPlayerComponent.getMediaPlayer().getPosition() * 1000.0f);
		//This will get the total length of the video.
		final long duration= mediaPlayerComponent.getMediaPlayer().getLength();

		//This will be used to update the GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(mediaPlayerComponent.getMediaPlayer().isPlaying()) {
					videoTime.updateTime(time);
					progressSlider.updatePosition(position);
					videoTime.updateDuration(duration);
				}
			}
		});
		//This will execute the code at a fixed rate.
		executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayerComponent), 0, 100, TimeUnit.MILLISECONDS);
	}



	
	/**
	 * Update the GUI as it plays.
	 */
	public void updateGUI() {
		if(!mediaPlayerComponent.getMediaPlayer().isPlaying()) {
			if(!pressedWhilePlaying) {
				try {
					Thread.sleep(100);
				}
				catch(InterruptedException e) {
					// Don't care if unblocked early
				}
				//Pauses the video to make sure it is paused if dragged while paused during position slider.
				mediaPlayerComponent.getMediaPlayer().setPause(true);
			}
		}
		//Set the title of the video.
		
		//Gets the current time and position and updates them in the GUI.
		long time = mediaPlayerComponent.getMediaPlayer().getTime();
		int position = (int)(mediaPlayerComponent.getMediaPlayer().getPosition() * 1000.0f);
		videoTime.updateTime(time);
		progressSlider.updatePosition(position);
	}



	
	/**
	 * @author jxu811
	 * Class which is used to update the GUI.
	 */
	private final class UpdateRunnable implements Runnable {

		private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

		private UpdateRunnable(EmbeddedMediaPlayerComponent mediaPlayer) {
			this.mediaPlayerComponent = mediaPlayer;
		}
		//This is a method which will take in the current time, position and length of the video and updates them.
		@Override
		public void run() {
			final long time = mediaPlayerComponent.getMediaPlayer().getTime();
			final int position = (int)(mediaPlayerComponent.getMediaPlayer().getPosition() * 1000.0f);
			final long duration= mediaPlayerComponent.getMediaPlayer().getLength();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(mediaPlayerComponent.getMediaPlayer().isPlaying()) {
						//Updates time position and duration.
						videoTime.updateTime(time);
						progressSlider.updatePosition(position);
						videoTime.updateDuration(duration);
					}
				}
			});
		}
	}

	/**
	 * This will set out the layout of my panes and vidVox player with their
	 * buttons associated with it.
	 */
	private void setUpLayout(){
		//creating the content pane which will store all of the video components
		setTitle("");
		gbl_topPane = new GridBagLayout();
		topPane = new JPanel(gbl_topPane);
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(topPane);

		//creating the content pane which will store all of the control components
		gbl_bottomPane = new GridBagLayout();
		bottomPane = new JPanel(gbl_bottomPane);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.SOUTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0,10,10,10);
		topPane.add(bottomPane, c);

		//Creates a Menu bar for the frame
		videoMenu= new VideoMenu(mainplayer);
		JMenuBar menuBar = videoMenu.setUpMenuBar();
		setJMenuBar(menuBar);

		//Adding in the video area where a mp4 can be played
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setPreferredSize(new Dimension(600,480));
		ff=false;
		rw=false;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1.5;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10,10,10,10);
		topPane.add(mediaPlayerComponent, c);	

		//JButton which fast forwards through the video
		fastforward = new JButton(">>");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,5,0,10);
		//	c.anchor = GridBagConstraints.EAST;
		bottomPane.add(fastforward, c);

		//JButton which fast forwards through the video
		rewind = new JButton("<<");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,10,0,5);
		c.anchor = GridBagConstraints.WEST;
		bottomPane.add(rewind, c);

		//JButton which Plays the video
		play = new JButton("pause");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,5,0,5);
		bottomPane.add(play, c);

		//This will set up the volume controls such as mute and volume slider.
		volumeFunctionality = new VolumeControl(mainplayer,c);
		volumeFunctionality.setUpVolume(bottomPane);

		//This will set up the progress bar for my video controller.
		progressSlider=new PositionSlider(mainplayer,c);
		progressSlider.setUpPositionSlider(topPane);

		videoTime = new VideoTime(mainplayer);
		videoTime.setUpTime(topPane);
		//JSlider which controls the volume of the video
		addCommentaryButton = new JButton("Add Commentary");
		c = new GridBagConstraints();
		c.gridx = 8;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,10,0,10);
		c.anchor = GridBagConstraints.EAST;
		bottomPane.add(addCommentaryButton, c);	 
		
		
	}
	/**
	 * This will check if rewind and fastforward are currently on and turn them off.
	 */
	public void turnOffRewindAndFastforward(){
		if (ff==true){
			ffswing.cancel(true);
		}
		if (rw==true){
			rwswing.cancel(true);
		}
		ff=false;
		rw=false;
	}
	
	
	
	/**
	 * Set up listeners for my buttons in my GUI
	 */
	public void setUpListeners(){
		//Sets up action listener for volume slider and mute button.
		volumeFunctionality.setUpListener();
		//Sets up action listener for progress slider.
		progressSlider.setUpListeners();
		
		//This will set up the video menu bar
		videoMenu.setUpMenuListeners();
		
		//Adds an action listener when the play button is clicked.
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check if fast forwarding or rewind is on when the play/pause button is clicked
				//and will cancel it.
				turnOffRewindAndFastforward();
				//Pauses or plays the video depending if it is playing or paused respectively and also
				//changes the text of the button to add a more user friendly experience.
				if (play.getText().equals("play")){
					play.setText("pause");
					mediaPlayerComponent.getMediaPlayer().play();

				}else{
					play.setText("play");
					mediaPlayerComponent.getMediaPlayer().setPause(true);
				}
			}
		});
		//Added action listener for the rewind(<<) button when it is clicked.
		rewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Press rewind button while fastforwarding and not rewinding will result in fast
				//forward being canceled and rewind being used.
				if ((ff==true)&&(rw==false)){
					rwswing= new Skip(mediaPlayerComponent,-1000,mainplayer);
					ffswing.cancel(true);
					rwswing.skip=true;
					rwswing.execute();
					rw=true;
					ff=false;
					//Press rewind button while not rewinding or fast forwarding will result in rewind 
					//just being executed.
				}else if ((rw==false)&&(ff==false)){
					rwswing= new Skip(mediaPlayerComponent,-1000,mainplayer);
					rwswing.skip=true;
					rwswing.execute();
					rw=true;
					//Press rewind button while rewinding and not fast forwarding will cause rewind 
					//to be canceled.
				}else if ((rw==true) && (ff==false)){
					rwswing.cancel(true);
					rw=false;
					//Will pause the component if rewind is canceled and it was paused when rewinding.
					if (play.getText().equals("play")){
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
					//Last case where you press the rewind button whilst its rewinding and fastforwarding
					//which cant occur but act as a backup code in base of bugs.
				}else{
					rwswing.cancel(true);
					rw=false;
					ffswing.cancel(true);
					ff=false;
					//Sets the play button to an appropriate button.
					if (play.getText().equals("play")){
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
				}
			}
		});
		//Added action listener for the fastforward (>>) button when it is clicked.
		fastforward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Checks for when fastforward button is clicked when fastforward is off and rewind
				//is on. It will cause the rewind function to cancel and execute the fastforward.
				if((ff==false)&&(rw==true)){
					ffswing = new Skip(mediaPlayerComponent,1000,mainplayer);
					rwswing.cancel(true);
					ffswing.skip=true;
					ffswing.execute();
					ff=true;
					rw=false;
					//Checks whether fastforward button is clicked when fastforward is off and rewind
					//is off. It will cause fastforward to execute.
				}else if ((ff==false)&&(rw==false)){
					ffswing = new Skip(mediaPlayerComponent,1000,mainplayer);
					ffswing.skip=true;
					ffswing.execute();
					ff=true;
					//Checks whether fastforward button is clicked when fastforward is on and rewind
					//is off. It will cause fastforward to stop.
				}else if ((ff==true)&&(rw==false)){
					//ffswing.skip=false;
					ffswing.cancel(true);
					ff=false;
					if (play.getText().equals("play")){
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
					//Last case where you press the fastforward button whilst its rewinding and fastforwarding
					//which cant occur but act as a backup code in base of bugs.
				}else{
					rwswing.cancel(true);
					rw=false;
					ffswing.cancel(true);
					ff=false;
					if (play.getText().equals("play")){
						mediaPlayerComponent.getMediaPlayer().setPause(true);
					}
				}
			}
		});
		//Allows the user to add commentary.
		addCommentaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ff==true){
					ffswing.cancel(true);
				}
				if (rw==true){
					rwswing.cancel(true);
				}
				ff=false;
				rw=false;
				play.setText("play");
				mediaPlayerComponent.getMediaPlayer().setPause(true);

				if (MainPlayerScreen.mediapath == null){
					JOptionPane.showMessageDialog(null, "Error please open a video before trying to add commentary");
				}else{
					addCommentaryScreen.setVisible(true);
				}
			}
		});
		//Checks for when the video is finished and if it is, it will stop the video and user will need to play video again.
		mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				mediaPlayerComponent.getMediaPlayer().stop();
				play.setText("play");
			}
		});
	}
}