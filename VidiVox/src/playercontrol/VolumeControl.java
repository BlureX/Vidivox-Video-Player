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
import vidVox.guiScreens.MainPlayerScreen;

public class VolumeControl {
	private int currentVolume;
	private JSlider volume;
	private JLabel volumeLabel;
	private GridBagConstraints  constraint;
	MainPlayerScreen mainPlayer;
	private JButton mute;
	
	
	public VolumeControl(MainPlayerScreen mainPlayer, GridBagConstraints constraint){
		this.mainPlayer=mainPlayer;
		this.constraint=constraint;
				
	}
	
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
	
	
	 public void setUpListener(){
	 //This will allow you to identify when the volume bar is being changed and then set the value to the position
	 //of the slider to the correct volume.
	 volume.addChangeListener(new ChangeListener() {
		 @Override
		 public void stateChanged(ChangeEvent e) {
			 JSlider source = (JSlider)e.getSource();
			 mainPlayer.mediaPlayerComponent.getMediaPlayer().setVolume(source.getValue());
		 }
	 });
	 //This will allow you to click anywhere on the volume slider and this will update the volume bar 
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
				 //mediaPlayerComponent.getMediaPlayer().mute();
				 //If muted already, and clicked mute again, it will refer back to the previous value it was just before being muted.
			 } else {
				 volume.setValue(currentVolume);
			 }
		 }
	 });
	 
	 }
	 

}


