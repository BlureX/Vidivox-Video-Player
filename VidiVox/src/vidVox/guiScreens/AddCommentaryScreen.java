package vidVox.guiScreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vidVox.MoveFile;
import vidVox.workers.GetDuration;
import vidVox.workers.OverlayMp3OntoVideo;
import vidVox.workers.PreviewMP3;
import vidVox.workers.TextToFile;

/**
 * @author jxu811
 * This class corresponds to the Add Commentary frame which is initiated when the user clicks the
 * Add Commentary button in the main player.
 */
public class AddCommentaryScreen extends JFrame{
	AddCommentaryScreen addCommentary= this;
	public static JFileChooser ourFileSelector= new JFileChooser();
	private JPanel pane;
	private JTextField textfield;
	public static TextToMp3Screen createCommentaryScreen;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private JSpinner secondSpinner;
	private int counter=0;
	final DefaultTableModel commentaryTable;
	final JTable table;
	List<PreviewMP3> voiceList=new ArrayList<PreviewMP3>();	


	/**
	 * @param screen
	 * Constructor for my AddCommentary class
	 */
	public AddCommentaryScreen (MainPlayerScreen screen){



		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (PreviewMP3 festival: voiceList){
					festival.destroyProcess();
					//voiceList.remove(festival);
				}
				voiceList=new ArrayList<PreviewMP3>();	
			}
		});


		//making the main initial layout for the AddCommentaryScreen
		//setBounds(450, 450, 850, 100);
		//Create gridbag constrant variable.
		GridBagConstraints c = new GridBagConstraints();
		setTitle("Add Commentary");
		createCommentaryScreen = new TextToMp3Screen(screen);
		createCommentaryScreen.setBounds(385, 475, 650, 100);
		createCommentaryScreen.setMinimumSize(new Dimension(650, 100));

		String[] audioOverlayOptions = {"Commentary", "Duration","Time to add",""};

		commentaryTable = new DefaultTableModel(audioOverlayOptions,0){
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				Class classType = String.class;
				switch (columnIndex) {
				case 0:
					classType = String.class;
					break;
				case 1:
					classType = String.class;
					break;
				case 2:
					classType = String.class;
					break;
				case 3:
					classType = String.class;
					break;
				}
				return classType;
			}
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		table = new JTable(commentaryTable);
		table.setModel(commentaryTable);
		table.setMinimumSize(new Dimension(550,900));

		//Code which I have referenced to hide the table
		//http://stackoverflow.com/questions/1492217/how-to-make-a-columns-in-jtable-invisible-for-swing-java
		table.removeColumn(table.getColumnModel().getColumn(3));



		JScrollPane scrollPane = new JScrollPane(); 
		// scrollPane.setBounds(20, 75, 400, 400);
		scrollPane.setViewportView(table);
		//  scrollPane.setMinimumSize( scrollPane.getPreferredSize() );

		//creating the content pane which will store all of the addcommentaryScreen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);

		//Creates a Jtable to display my commentary I will add to the video.
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 3;
		c.gridwidth = 6;
		c.weighty = 99;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10,10,10,10);
		pane.add(scrollPane, c);

		//creating a Jbutton which will add commentary to the start of the video
		JButton selectMp3 = new JButton("Add mp3 Commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0,10,0,0);
		c.anchor = GridBagConstraints.WEST;
		pane.add(selectMp3, c);

		//creating a JButton which allows you to delete the selected commentary.
		JButton deleteRow = new JButton("Delete Commentary");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth =1;
		c.weightx = 3;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(deleteRow, c);

		//creating a Jbutton which will add commentary to the start of the video
		JButton createCommentary = new JButton("Create Commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,10,0,0);
		c.anchor = GridBagConstraints.WEST;
		pane.add(createCommentary, c);

		
//		String[] festivalVoice = { "Default", "Female", "Robo"};
//		JComboBox voiceChanger = new JComboBox(festivalVoice);
//		c = new GridBagConstraints();
//		c.gridx = 0;
//		c.gridy = 2;
//		c.weightx = 1;
//		c.weighty = 0;
//		c.gridwidth =;
//		c.anchor = GridBagConstraints.EAST;
//		c.insets = new Insets(0,0,0,0);
//		c.fill = GridBagConstraints.HORIZONTAL;
//		pane.add(voiceChanger, c);
		
		//This is a textfield box which allow you to create commentary to add to the video.
		textfield = new JTextField();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0,0,0,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textfield, c);

		//This is labels to indicate what each spinner model corresponds to.
		JLabel label1 = new JLabel("HH:MM:SS");
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 0;
		c.insets = new Insets(0,1,0,1);
		c.anchor = GridBagConstraints.WEST;
		pane.add(label1, c);

		//This is the hour spinner model which will correspond to the hour you want to add the commentary at.
		SpinnerModel hourSpinnerModel = new SpinnerNumberModel(0,0,99,1);
		hourSpinner = new JSpinner(hourSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		//c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		//c.insets = new Insets(0,3,0,3);
		pane.add(hourSpinner, c);

		//This is the minute spinner model which will correspond to the minute you want to add the commentary at.
		SpinnerModel minuteSpinnerModel = new SpinnerNumberModel(0,0,60,1);
		minuteSpinner = new JSpinner(minuteSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		//c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0,2,0,0);
		pane.add(minuteSpinner, c);

		//This is the second spinner model which will correspond to the second you want to add the commentary at.
		SpinnerModel secondSpinnerModel = new SpinnerNumberModel(0,0,60,1);
		secondSpinner = new JSpinner(secondSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		//c.gridwidth = 1;
		//c.anchor = GridBagConstraints.WEST;
		//c.insets = new Insets(0,0,0,0);
		pane.add(secondSpinner, c);

		//Empty JLabel to organise the gridbag layout more nicely.
		JLabel one = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		pane.add(one, c);

		//Empty JLabel to organise the gridbag layout more nicely.
		JLabel two = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		pane.add(two, c);

		//Preview video button which will allow you to preview the video with all the commentary.
		JButton createVideo = new JButton("Preview Video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0,0,0,10);
		//c.insets = new Insets(3,3,3,3);
		pane.add(createVideo, c);

		//Button which will allow you to preview the commentary that is to be added.
		JButton preview = new JButton("Preview");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(3,10,3,10);
		pane.add(preview, c);

		//Button which will allow you to save your created commentary to a location.
		JButton saveButton = new JButton("Save");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(3,5,3,0);
		pane.add(saveButton, c);


		//Action listener which will allow you to choose a file for adding mp3.
		selectMp3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File ourFile;
				String mediaPath = null;
				FileFilter filter = new FileNameExtensionFilter("MP3 FILES","mp3");
				ourFileSelector.resetChoosableFileFilters();
				ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
				ourFileSelector.setFileFilter(filter);
				int status = ourFileSelector.showOpenDialog(null);
				if (status == JFileChooser.APPROVE_OPTION){
					if (!(ourFileSelector.getSelectedFile() == null)){
						ourFile=ourFileSelector.getSelectedFile();
						mediaPath=ourFile.getAbsolutePath();
						String hour=hourSpinner.getValue().toString();
						String minute=minuteSpinner.getValue().toString();
						String second=secondSpinner.getValue().toString();

						if (hourSpinner.getValue().toString().length()<=1){
							hour="0"+hourSpinner.getValue().toString();
						}
						if (minuteSpinner.getValue().toString().length()<=1){
							minute="0"+minuteSpinner.getValue().toString();
						}
						if (secondSpinner.getValue().toString().length()<=1){
							second="0"+secondSpinner.getValue().toString();
						}
						GetDuration duration = new GetDuration(mediaPath,addCommentary,hour,minute,second,ourFile.getName());
						duration.execute();

					}else {
						JOptionPane.showMessageDialog(null, "Error please select an appropriate file");
					}
					//Do nothing if the cancel option is selected.
				}else if (status == JFileChooser.CANCEL_OPTION){

				}
			}
		});
		//This will grab whatever is selected in the text box and add it to the commentary table.
		createCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Increase counter to ensure that the save location of commentary is not the same.
				counter++;
				//Appends the 0 to front of the time if it is in single digits.
				String hour=hourSpinner.getValue().toString();
				String minute=minuteSpinner.getValue().toString();
				String second=secondSpinner.getValue().toString();

				if (hourSpinner.getValue().toString().length()<=1){
					hour="0"+hourSpinner.getValue().toString();
				}
				if (minuteSpinner.getValue().toString().length()<=1){
					minute="0"+minuteSpinner.getValue().toString();
				}
				if (secondSpinner.getValue().toString().length()<=1){
					second="0"+secondSpinner.getValue().toString();
				}
				//Starts creating the mp3 file and place it in the tmp folder.
				if ((textfield.getText() != null) && (!textfield.getText().trim().equals(""))){
					TextToFile tmpFile = new TextToFile(textfield.getText(),"/tmp/festSpeech"+counter,addCommentary,hour,minute,second);
					tmpFile.execute();
				} else {
					JOptionPane.showMessageDialog(null, "Error, please enter appropriate commentary");
				}
			}
		});
		//This will delete the selected commentary rows.
		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int rowsSelected = table.getSelectedRows().length;
				for(int i=0; i<rowsSelected ; i++ ) {
					//Cancels any playing commentary from the selected rows
					for (PreviewMP3 currentCommentary : voiceList){
						if (commentaryTable.getValueAt(table.getSelectedRow(), 3).equals(currentCommentary.getFile())){
							currentCommentary.destroyProcess();
							voiceList.remove(currentCommentary);
							break;
						}
					}
					commentaryTable.removeRow(table.getSelectedRow());
				}
			}
		});
		//This will allow you to create the video with the stated commentary;
		createVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int totalRows = table.getRowCount();
				if (totalRows > 0){
					String cmd = "ffmpeg -y -i " + MainPlayerScreen.mediapath + " ";
					for (int i = 0 ; i<totalRows; i++){
						String fileName = table.getModel().getValueAt(i,3).toString();
						String time = table.getValueAt(i,2).toString();
						String[] splitArray = time.split(":");
						int totalTime = Integer.parseInt(splitArray[0])*3600 + Integer.parseInt(splitArray[1])*60 + Integer.parseInt(splitArray[2]);
						cmd = cmd+ "-itsoffset " + totalTime + " -i \"" + fileName + "\" ";
					}
					cmd = cmd + "-map 0:v:0 ";
					for (int i = 1 ; i<= totalRows; i++){
						cmd = cmd + "-map " + i +":0" + " ";
					}
					cmd = cmd + "-c:v copy -async 1 -filter_complex amix=inputs=" + (totalRows+1) + " ";
					//System.out.println(cmd);
					OverlayMp3OntoVideo mergeVideo = new OverlayMp3OntoVideo(cmd, "coolffFile",true);
					mergeVideo.execute();
					setVisible(false);
					MainPlayerScreen.saved=false;
				}
			}
		});
		//This will allow you to preview the commentary stated.
		preview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row=table.getSelectedRow();
				boolean speaking=false;
				if (row >-1 ){
					//Loop through the current playing voices.
					for (PreviewMP3 currentCommentary : voiceList){
						//If it is in the festival voiceList but stopped playing, remove it and set speaking to false.
						if (commentaryTable.getValueAt(row, 3).equals(currentCommentary.getFile()) && currentCommentary.speaking==false){
							voiceList.remove(currentCommentary);
							speaking=false;
							break;
							//If it is still speaking, set speaking to true.
						}else if (commentaryTable.getValueAt(row, 3).equals(currentCommentary.getFile()) && currentCommentary.speaking==true){
							speaking=true;
							break;
						}
					}
					//If it is not speaking or it has not been selected before, add it to the array and execute the voice.
					if (speaking == false){
						PreviewMP3 previewAudio = new PreviewMP3(commentaryTable.getValueAt(row, 3).toString());
						voiceList.add(previewAudio);
						previewAudio.execute();
					}
				}
			}
		});

		//This buton allows me to save mp3.
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Chooses file path.
				String mediaPath="~/";
				File ourFile;
				FileFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
				SaveDialogScreen.ourFileSelector.resetChoosableFileFilters();
				//opening the save file explorer, user gets to choose where to save the file   
				SaveDialogScreen.ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
				SaveDialogScreen.ourFileSelector.setFileFilter(filter);
				int status = SaveDialogScreen.ourFileSelector.showSaveDialog(null);
				//Checks if user chose to save.
				if (status == JFileChooser.APPROVE_OPTION){
					//Checks if valid path.
					if (!(SaveDialogScreen.ourFileSelector.getSelectedFile() == null)){
						ourFile=SaveDialogScreen.ourFileSelector.getSelectedFile();
						mediaPath=ourFile.getAbsolutePath();
						//Adds .mp3 to end of file if it isnt specified in the text.
						if ((!(mediaPath.endsWith(".mp3")))) {
							mediaPath = mediaPath+".mp3";
						}
						//creates the mp3 file at the location
						int row = table.getSelectedRow();
						String filename = commentaryTable.getValueAt(row, 3).toString();
						//Uses move file to place the file into the stated path.
						MoveFile k = new MoveFile(filename,mediaPath,true);
						k.execute();
					}else{
						JOptionPane.showMessageDialog(null, "Error please save to an appropriate location");
					}
				}else if (status == JFileChooser.CANCEL_OPTION){			
				}
			}
		});
	}


	/**
	 * @param content
	 * @param duration
	 * @param hour
	 * @param minute
	 * @param second
	 * @param path
	 * This will add the content, duration, time of video and path of commentary into the table.
	 */
	public void addToTable(String content, String duration, String hour, String minute, String second, String path){
		Object[] data = { content , duration, hour+":"+minute+":"+second,path };
		commentaryTable.addRow(data);
	}


}
