package playercontrol;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import vidVox.gui.MainPlayerScreen;
import vidVox.workers.Skip;

public class PlaybackControl {
	private boolean rw,ff;
	private Skip ffswing, rwswing;
	private GridBagConstraints c;
	private  JButton fastforward, rewind;
	private MainPlayerScreen mainPlayer;

	public PlaybackControl(MainPlayerScreen mainPlayer){
		this.mainPlayer=mainPlayer;
	}
	public void setUpPlaybackButtons(JPanel bottomPane){
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

	}


	public void setUpPlaybackListeners(){

		//Added action listener for the rewind(<<) button when it is clicked.
		rewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Press rewind button while fastforwarding and not rewinding will result in fast
				//forward being canceled and rewind being used.
				if ((ff==true)&&(rw==false)){
					rwswing= new Skip(mainPlayer.getMediaPlayerComponent(),-1000,mainPlayer);
					ffswing.cancel(true);
					rwswing.skip=true;
					rwswing.execute();
					rw=true;
					ff=false;
					//Press rewind button while not rewinding or fast forwarding will result in rewind 
					//just being executed.
				}else if ((rw==false)&&(ff==false)){
					rwswing= new Skip(mainPlayer.getMediaPlayerComponent(),-1000,mainPlayer);
					rwswing.skip=true;
					rwswing.execute();
					rw=true;
					//Press rewind button while rewinding and not fast forwarding will cause rewind 
					//to be canceled.
				}else if ((rw==true) && (ff==false)){
					rwswing.cancel(true);
					rw=false;
					//Will pause the component if rewind is canceled and it was paused when rewinding.
					if (mainPlayer.getPlay().getText().equals("play")){
						mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
					}
					//Last case where you press the rewind button whilst its rewinding and fastforwarding
					//which cant occur but act as a backup code in base of bugs.
				}else{
					rwswing.cancel(true);
					rw=false;
					ffswing.cancel(true);
					ff=false;
					//Sets the play button to an appropriate button.
					if (mainPlayer.getPlay().getText().equals("play")){
						mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
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
					ffswing = new Skip(mainPlayer.getMediaPlayerComponent(),1000,mainPlayer);
					rwswing.cancel(true);
					ffswing.skip=true;
					ffswing.execute();
					ff=true;
					rw=false;
					//Checks whether fastforward button is clicked when fastforward is off and rewind
					//is off. It will cause fastforward to execute.
				}else if ((ff==false)&&(rw==false)){
					ffswing = new Skip(mainPlayer.getMediaPlayerComponent(),1000,mainPlayer);
					ffswing.skip=true;
					ffswing.execute();
					ff=true;
					//Checks whether fastforward button is clicked when fastforward is on and rewind
					//is off. It will cause fastforward to stop.
				}else if ((ff==true)&&(rw==false)){
					//ffswing.skip=false;
					ffswing.cancel(true);
					ff=false;
					if (mainPlayer.getPlay().getText().equals("play")){
						mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
					}
					//Last case where you press the fastforward button whilst its rewinding and fastforwarding
					//which cant occur but act as a backup code in base of bugs.
				}else{
					rwswing.cancel(true);
					rw=false;
					ffswing.cancel(true);
					ff=false;
					if (mainPlayer.getPlay().getText().equals("play")){
						mainPlayer.getMediaPlayerComponent().getMediaPlayer().setPause(true);
					}
				}
			}
		});
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
}
