package vidVox;

import java.awt.Dimension;

import vidVox.guiScreens.AddCommentaryScreen;
import vidVox.guiScreens.LoadingScreen;
import vidVox.guiScreens.MainPlayerScreen;

public class StartVidivox {

	public static LoadingScreen loadingScreen = new LoadingScreen();
	public static AddCommentaryScreen addCommentaryScreen;
	/**
	 * Main Method used to start my application.
	 * Also used code from https://github.com/caprica/vlcj/blob/master/src/test/java/uk/co/caprica/vlcj/test/basic/PlayerControlsPanel.java for
	 * additional features such as progress bar.
	 * @param args
	 */
	public static void main(String[] args) {
		//Initialising all the screens which will be used in the video player.
		MainPlayerScreen frame = new MainPlayerScreen();
		frame.setBounds(300, 200, 820, 650);
		frame.setMinimumSize(new Dimension(820, 1));
		frame.setVisible(true);


	}
}
