package playercontrol;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;

import vidVox.gui.MainPlayerScreen;

/**
 * @author jxu811
 * This class corresponds to the Video time and labels which are associated with
 * the Vidivox player.
 */
public class VideoTime {

	JLabel timeLabel,endLabel;
	GridBagConstraints constraints;
	public long totaltime;
	MainPlayerScreen mainPlayer;

	/**
	 * @param mainPlayer
	 * Constructor for my Vidivox player
	 */
	public VideoTime(MainPlayerScreen mainPlayer){
		this.mainPlayer=mainPlayer;
	}

	/**
	 * @param topPane
	 * This will set up the labels at the start and end of the position slider and
	 * takes in a JPanel which will add these labels.
	 */
	public void setUpTime(JPanel topPane){

		//Adding a Jlabel which will be the starting time of the video
		timeLabel = new JLabel("00:00:00");
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0,10,10,10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(timeLabel, constraints);

		//Adding a Jlabel which will be the ending time of the video
		endLabel = new JLabel("00:00:00");
		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(0,10,10,10);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(endLabel, constraints);

	}


	/**
	 * @param millis
	 * This method takes in the time and converts it to the appropriate time format.
	 * The time label is also set based on the time inputted.
	 */
	public void updateTime(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		timeLabel.setText(s);
	}


	/**
	 * @param millis
	 * This method will take in the total length of the video and then set the label
	 * to its appropriate time.
	 */
	public void updateDuration(long millis) {
		String s = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		endLabel.setText(s);
	}
}
