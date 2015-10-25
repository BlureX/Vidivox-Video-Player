package playercontrol;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import vidVox.gui.MainPlayerScreen;

/**
 * This class is used for setting up the volume control in my Vidivox.
 * @author jxu811
 *
 */
public class VolumeControl {
	private int currentVolume=50;
	private JSlider volume;
	private JLabel volumeLabel;
	private GridBagConstraints  constraint;
	MainPlayerScreen mainPlayer;
	private JButton mute;


	/**
	 * @param mainPlayer
	 * @param constraint
	 * This is the constructor for my VolumeControl class and will take in mainplayer and
	 * GridBagConstraint as variables.
	 */
	public VolumeControl(MainPlayerScreen mainPlayer, GridBagConstraints constraint){
		this.mainPlayer=mainPlayer;
		this.constraint=constraint;

	}

	/**
	 * @param bottomPane
	 * This will set up my button and sliders and plaec them in the correct position.
	 * They will all be placed in the JPanel which is passed in as argument.
	 */
	public void setUpVolume(JPanel bottomPane){
		//Volume label
		volumeLabel = new JLabel("Volume");
		constraint = new GridBagConstraints();
		constraint.gridx = 5;
		constraint.gridy = 0;
		constraint.weightx = 0;
		constraint.weighty = 1;
		constraint.insets = new Insets(0,20,0,10);
		constraint.anchor = GridBagConstraints.WEST;
		bottomPane.add(volumeLabel, constraint);

		//JSlider which controls the volume of the video
		volume = new JSlider();
		constraint = new GridBagConstraints();
		constraint.gridx = 6;
		constraint.gridy = 0;
		constraint.weightx = 0;
		constraint.weighty = 1;
		constraint.insets = new Insets(0,10,0,10);
		bottomPane.add(volume, constraint);

		//This will set up the mute button.
		mute = new JButton("Mute");
		constraint = new GridBagConstraints();
		constraint.gridx = 7;
		constraint.gridy = 0;
		constraint.weightx = 0;
		constraint.weighty = 1;
		constraint.insets = new Insets(0,10,0,10);
		constraint.anchor = GridBagConstraints.EAST;
		bottomPane.add(mute, constraint);



	}


	/**
	 * This will set up my volume slider and mute button action listeners so that
	 * they have some functionality.
	 */
	public void setUpListener(){
		//This will allow you to identify when the volume bar is being changed and then set the value to the position
		//of the slider to the correct volume.
		volume.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				mainPlayer.getMediaPlayerComponent().getMediaPlayer().setVolume(source.getValue());
			}
		});
		//This will allow you to click anywhere on the volume slider and this will update the volume bar.
		//This code was referenced from http://www.java-forums.org/awt-swing/84832-move-jslider-per-click.html
		volume.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e)
			{
				JSlider sourceSlider=(JSlider)e.getSource();
				BasicSliderUI ui = (BasicSliderUI)sourceSlider.getUI();
				int value = ui.valueForXPosition( e.getX() );
				volume.setValue(value);
			}
		});
		//This will allow you to mute the volume or unmute it back to the previous value.
		mute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Checks if volume is not already muted and if not, mute it.
				if (volume.getValue() != 0 ) {
					currentVolume = volume.getValue();
					volume.setValue(0);
					//If muted already, and clicked mute again, it will refer back to the previous value it was just before being muted.
				} else {
					volume.setValue(currentVolume);
				}
			}
		});

	}


}


