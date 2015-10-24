package playercontrol;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import vidVox.guiScreens.MainPlayerScreen;

/**
 * This class will help set up the position slider and all its functionality 
 * in the Vidivox video commentator.
 * @author jxu811
 * 
 */
public class PositionSlider {
	//Initialise my variables that I will be using.
	private JSlider positionSlider;
	private GridBagConstraints  constraint;
	MainPlayerScreen mainPlayer;
	private ChangeListener listener;
	public boolean refresh=false;
	private boolean pressedWhilePlaying = false;
	public PositionSlider(MainPlayerScreen mainPlayer, GridBagConstraints constraint){
		this.mainPlayer=mainPlayer;
		this.constraint=constraint;

	}

	/**
	 * @param topPane JPanel which will be used to add the slider
	 * This method will set up the position slider with its appropriate layout and position.
	 */
	public void setUpPositionSlider(JPanel topPane){
		//Adding the position Slider which will change as the video progresses
		setPositionSlider(new JSlider());
		getPositionSlider().setMinimum(0);
		getPositionSlider().setMaximum(1000);
		getPositionSlider().setValue(0);
		//positionSlider.setToolTipText("Position");
		constraint = new GridBagConstraints();
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.weightx = 1;
		constraint.weighty = 0;
		constraint.insets = new Insets(0,10,10,10);

		constraint.fill = GridBagConstraints.HORIZONTAL;
		topPane.add(getPositionSlider(), constraint);

	}


	/**
	 * This method will set the video based on the position of the slider.
	 */
	void setSliderBasedPosition() {
		//Check if it is playable.
		if(!mainPlayer.mediaPlayerComponent.getMediaPlayer().isSeekable()) {
			return;
		}

		float positionValue = getPositionSlider().getValue() / 1000.0f;
		// Makes sure it wont freeze.
		if(positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		//Set position of position slider.
		mainPlayer.mediaPlayerComponent.getMediaPlayer().setPosition(positionValue);
	}


	/**
	 * @param value Sets the position of the slider based on the value.
	 * Updates the position of the position slider based on value given.
	 */
	public void updatePosition(int value) {
		getPositionSlider().setValue(value);
	}

	/**
	 * This will set up the action listeners for the position slider so that it can be clicked on
	 * and events such as updating the video will occur when position slider is dragged.
	 */
	public void setUpListeners(){
		//This is a change listener used to look at the changes when the video is being played and mainly used to
		//update the the time of the video when the position slider is being dragged.
		listener = new ChangeListener()
		{
			public void stateChanged(ChangeEvent event)
			{
				if (refresh){
					setSliderBasedPosition();
					mainPlayer.updateGUI();
				}
			}
		};
		getPositionSlider().addChangeListener(listener);
		getPositionSlider().addMouseListener(new MouseAdapter() {
			//Checks for when the mouse is being pressed and updates the position and time appropriately.
			@Override
			public void mousePressed(MouseEvent e) {
				refresh=true;
				if(mainPlayer.mediaPlayerComponent.getMediaPlayer().isPlaying()) {
					pressedWhilePlaying = true;
				}
				else {
					pressedWhilePlaying = false;
				}
				//This will allow you to go to a certain point in the video by clicking on the position slider and then updates
				//the video based on where you clicked.
				//Code was referenced from http://www.java-forums.org/awt-swing/84832-move-jslider-per-click.html
				JSlider sourceSlider=(JSlider)e.getSource();
				BasicSliderUI ui = (BasicSliderUI)sourceSlider.getUI();
				int value = ui.valueForXPosition( e.getX() );
				getPositionSlider().setValue(value);
				setSliderBasedPosition();
			}
			//Checks for when the mouse has been released and gets the point where it has been released.
			@Override
			public void mouseReleased(MouseEvent e) {
				//Updates the slider and the time.
				refresh=false;
				setSliderBasedPosition();
				mainPlayer.updateGUI();
			}
		});
	}

	//Getters and setters for my position slider.
	public JSlider getPositionSlider() {
		return positionSlider;
	}

	public void setPositionSlider(JSlider positionSlider) {
		this.positionSlider = positionSlider;
	}
}
