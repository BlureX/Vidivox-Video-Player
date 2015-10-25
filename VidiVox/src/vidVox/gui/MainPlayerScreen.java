package vidVox.gui;

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

import playercontrol.PlaybackControl;
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



/**
 * @author jxu811
 * This class is used for setting up the main player screen and will set out the layout and functionality
 * of the buttons.
 * Also used code from 
 * https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java for
 * additional features such as progress bar, updateRunnable and ScheduledExecutorService referenced from this code.
 */
/**
 * @author jxu811
 *
 */
public class MainPlayerScreen extends JFrame {
	//Fields which are used within this class and package.
	MainPlayerScreen mainplayer = this;
	private JPanel topPane, bottomPane;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public static String mediapath;
	public static LoadingScreen loadingScreen = new LoadingScreen();
	public static AddCommentaryScreen addCommentaryScreen;
	public static boolean saved=true;
	public static int videoNumber =0;
	public static String originalVideo;
	private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	private VolumeControl volumeFunctionality;
	private PositionSlider progressSlider;
	private VideoTime videoTime;
	private long originalVideoLength;
	

	//Set up the GridBag Layout for my screen.
	private GridBagLayout gbl_topPane;
	private  GridBagLayout gbl_bottomPane;
	private  GridBagConstraints c;

	//Buttons, sliders and labels which are used in my GUI for users to click.
	private JButton play;
	private JButton addCommentaryButton;
	public SaveVideoAs saveVideo = new SaveVideoAs();

	//Menu at the top which allows users to select their appropriate options.
	private VideoMenu videoMenu;

	private PlaybackControl playback;

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
				//if (!(mediapath.equals(TextToMp3Screen.originalVideo))){
				if (saved==false){
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
							MoveFile k = new MoveFile(mediapath, originalVideo,false);
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
		getMediaPlayerComponent().getMediaPlayer().playMedia(mediapath);
		//This will get the current time of the video.
		final long time = getMediaPlayerComponent().getMediaPlayer().getTime();
		//This will get the position of the video.
		final int position = (int)(getMediaPlayerComponent().getMediaPlayer().getPosition() * 1000.0f);
		//This will get the total length of the video.
		final long duration= getMediaPlayerComponent().getMediaPlayer().getLength();
		setOriginalVideoLength(duration/1000);
		//This will be used to update the GUI.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(getMediaPlayerComponent().getMediaPlayer().isPlaying()) {
					videoTime.updateTime(time);
					getProgressSlider().updatePosition(position);
					videoTime.updateDuration(duration);
				}
			}
		});
		//This will execute the code at a fixed rate.
		executorService.scheduleAtFixedRate(new UpdateRunnable(getMediaPlayerComponent()), 0, 100, TimeUnit.MILLISECONDS);
	}
	/**
	 * Update the GUI as it plays.
	 */
	public void updateGUI() {
		if(!getMediaPlayerComponent().getMediaPlayer().isPlaying()) {
			if(!progressSlider.isPressedWhilePlaying()) {
				try {
					Thread.sleep(100);
				}
				catch(InterruptedException e) {
					// Don't care if unblocked early
				}
				//Pauses the video to make sure it is paused if dragged while paused during position slider.
				getMediaPlayerComponent().getMediaPlayer().setPause(true);
			}
		}
		//Set the title of the video.

		//Gets the current time and position and updates them in the GUI.
		long time = getMediaPlayerComponent().getMediaPlayer().getTime();
		int position = (int)(getMediaPlayerComponent().getMediaPlayer().getPosition() * 1000.0f);
		videoTime.updateTime(time);
		getProgressSlider().updatePosition(position);
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
			setOriginalVideoLength(duration/1000);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(mediaPlayerComponent.getMediaPlayer().isPlaying()) {
						//Updates time position and duration.
						videoTime.updateTime(time);
						getProgressSlider().updatePosition(position);
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
		setMediaPlayerComponent(new EmbeddedMediaPlayerComponent());
		getMediaPlayerComponent().setPreferredSize(new Dimension(600,480));
		//		ff=false;
		//		rw=false;
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1.5;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10,10,10,10);
		topPane.add(getMediaPlayerComponent(), c);	

		//JButton which Plays the video
		setPlay(new JButton("Pause"));
		getPlay().setToolTipText("Play/Pause button");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,5,0,5);
		bottomPane.add(getPlay(), c);

		//This will set up the volume controls such as mute and volume slider.
		volumeFunctionality = new VolumeControl(mainplayer,c);
		volumeFunctionality.setUpVolume(bottomPane);

		//This will set up the progress bar for my video controller.
		setProgressSlider(new PositionSlider(mainplayer,c));
		getProgressSlider().setUpPositionSlider(topPane);

		videoTime = new VideoTime(mainplayer);
		videoTime.setUpTime(topPane);
		//JSlider which controls the volume of the video
		addCommentaryButton = new JButton("Add Commentary");
		addCommentaryButton.setToolTipText("Allows you to add commentary");
		c = new GridBagConstraints();
		c.gridx = 8;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 1;
		c.insets = new Insets(0,10,0,10);
		c.anchor = GridBagConstraints.EAST;
		bottomPane.add(addCommentaryButton, c);	 

		setPlayback(new PlaybackControl(mainplayer));
		getPlayback().setUpPlaybackButtons(bottomPane);


	}

	/**
	 * Set up listeners for my buttons in my GUI
	 */
	public void setUpListeners(){
		//Sets up action listener for volume slider and mute button.
		volumeFunctionality.setUpListener();
		//Sets up action listener for progress slider.
		getProgressSlider().setUpListeners();

		getPlayback().setUpPlaybackListeners();


		//This will set up the video menu bar
		videoMenu.setUpMenuListeners();

		//Adds an action listener when the play button is clicked.
		getPlay().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Check if fast forwarding or rewind is on when the play/pause button is clicked
				//and will cancel it.
				getPlayback().turnOffRewindAndFastforward();
				//Pauses or plays the video depending if it is playing or paused respectively and also
				//changes the text of the button to add a more user friendly experience.
				if (getPlay().getText().equals("Play")){
					getPlay().setText("Pause");
					getMediaPlayerComponent().getMediaPlayer().play();

				}else{
					getPlay().setText("Play");
					getMediaPlayerComponent().getMediaPlayer().setPause(true);
				}
			}
		});

		//Allows the user to add commentary.
		addCommentaryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPlayback().turnOffRewindAndFastforward();
				getPlay().setText("play");
				getMediaPlayerComponent().getMediaPlayer().setPause(true);

				if (MainPlayerScreen.mediapath == null){
					JOptionPane.showMessageDialog(null, "Error please open a video before trying to add commentary");
				}else{
					addCommentaryScreen.setVisible(true);
				}
			}
		});
		//Checks for when the video is finished and if it is, it will stop the video and user will need to play video again.
		getMediaPlayerComponent().getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				getMediaPlayerComponent().getMediaPlayer().stop();
				getPlay().setText("Play");
			}
		});
	}

	//Getters and setters
	public JButton getPlay() {
		return play;
	}
	public void setPlay(JButton play) {
		this.play = play;
	}
	public PositionSlider getProgressSlider() {
		return progressSlider;
	}
	public void setProgressSlider(PositionSlider progressSlider) {
		this.progressSlider = progressSlider;
	}
	public PlaybackControl getPlayback() {
		return playback;
	}
	public void setPlayback(PlaybackControl playback) {
		this.playback = playback;
	}
	public EmbeddedMediaPlayerComponent getMediaPlayerComponent() {
		return mediaPlayerComponent;
	}
	public void setMediaPlayerComponent(EmbeddedMediaPlayerComponent mediaPlayerComponent) {
		this.mediaPlayerComponent = mediaPlayerComponent;
	}
	public long getOriginalVideoLength() {
		return originalVideoLength;
	}
	public void setOriginalVideoLength(long originalVideoLength) {
		this.originalVideoLength = originalVideoLength;
	}


}