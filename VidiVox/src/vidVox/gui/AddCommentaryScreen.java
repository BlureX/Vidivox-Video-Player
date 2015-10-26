package vidVox.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vidVox.MoveFile;
import vidVox.workers.GetDuration;
import vidVox.workers.OverlayMp3OntoVideo;
import vidVox.workers.PreviewMP3;
import fileconverter.FestivalSpeechWorker;
import fileconverter.TextToFile;

/**
 * @author jxu811
 * This class corresponds to the Add Commentary frame which is initiated when the user clicks the
 * Add Commentary button in the main player.
 */
public class AddCommentaryScreen extends JFrame{
	private AddCommentaryScreen addCommentary= this;
	public static JFileChooser ourFileSelector= new JFileChooser();
	private JPanel pane;
	private JTextField textfield;
	private int counter=0;
	private final DefaultTableModel commentaryTable;
	private final JTable table;
	private List<PreviewMP3> voiceList=new ArrayList<PreviewMP3>();	
	private MainPlayerScreen mainPlayer;
	private boolean displayText=true;
	private JComboBox voiceChanger;
	private TimeSpinner spinningModel = new TimeSpinner();
	/**
	 * @param screen
	 * Constructor for my AddCommentary class
	 */
	public AddCommentaryScreen (final MainPlayerScreen  mainPlayer){
		this.mainPlayer=mainPlayer;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				for (PreviewMP3 festival: voiceList){
					festival.destroyProcess();
					//voiceList.remove(festival);
				}
				voiceList=new ArrayList<PreviewMP3>();	
			}
		});

		//Create gridbag constrant variable.
		GridBagConstraints c = new GridBagConstraints();
		setTitle("Add Commentary");
		String[] tableHeadings = {"Commentary", "Duration","Time to add","","Voice Type"};

		//Sets up table.
		commentaryTable = new DefaultTableModel(tableHeadings,0){
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
				case 4:
					classType = String.class;
					break;
				}
				return classType;
			}
			//Makes cell not editable
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
		scrollPane.setViewportView(table);
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
		selectMp3.setToolTipText("Adds a mp3 file to commentary table and based on current time selected");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0,10,0,10);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(selectMp3, c);

		//creating a JButton which allows you to delete the selected commentary.
		JButton deleteRow = new JButton("Delete Commentary");
		deleteRow.setToolTipText("Deletes selected commentary");
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
		createCommentary.setToolTipText("Creates commentary based on text and time specified");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,10,0,10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(createCommentary, c);

		//This is a textfield box which allow you to create commentary to add to the video.
		textfield = new JTextField();
		textfield.setToolTipText("Place to add commentary");
		textfield.setText("Add Commentary Here......[100 Character Limit]");
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
		JLabel label2 = new JLabel("Change Voice");
		label2.setToolTipText("Choose a accent for the commentary");
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 0;
		c.insets = new Insets(0,1,0,10);
		c.anchor = GridBagConstraints.EAST;
		pane.add(label2, c);

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
		createVideo.setToolTipText("Creates the video from the commentaries in the table");
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 3;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0,0,0,10);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(createVideo, c);

		//Button which will allow you to preview the commentary that is to be added.
		JButton preview = new JButton("Listen");
		preview.setToolTipText("Previews a selected commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3,10,3,10);
		pane.add(preview, c);

		//Button which will allow you to save your created commentary to a location.
		JButton saveButton = new JButton("Save");
		saveButton.setToolTipText("Saves Selected Commentary");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(saveButton, c);

		//This will create the combo box for selecting the different voices
		String[] festivalVoices = { "Robotic", "Kiwi", "British"};
		voiceChanger = new JComboBox(festivalVoices);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth =1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0,0,0,10);
		pane.add(voiceChanger, c);
		
		//Set up spinners
		spinningModel.setUpSpinner(pane);

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
						if (ourFile.exists()){
							if ((ourFile.getName().endsWith(".mp3"))){
								mediaPath=ourFile.getAbsolutePath();
								String hour=checkZero(spinningModel.getHourSpinner().getValue().toString());
								String minute=checkZero(spinningModel.getMinuteSpinner().getValue().toString());
								String second=checkZero(spinningModel.getSecondSpinner().getValue().toString());
								GetDuration duration = new GetDuration(mediaPath,addCommentary,hour,minute,second,ourFile.getName(),"mp3 file");
								duration.execute();
							}else {
								JOptionPane.showMessageDialog(null, "Please select a mp3 file");
							}
						}else {
							JOptionPane.showMessageDialog(null, "File doesnt exists, please select an mp3 file");
						}

					}else {
						JOptionPane.showMessageDialog(null, "Error please select an appropriate mp3 file");
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
				String hour=checkZero(spinningModel.getHourSpinner().getValue().toString());
				String minute=checkZero(spinningModel.getMinuteSpinner().getValue().toString());
				String second=checkZero(spinningModel.getSecondSpinner().getValue().toString());

				//Starts creating the mp3 file and place it in the tmp folder.
				if ((textfield.getText() != null) && (!textfield.getText().trim().equals(""))){
					//Checks that character limit is correct
					if (textfield.getText().length()<=100 && textfield.getText().length()>0 && displayText==false) {
						//Checks which voicetype is selected
						if (voiceChanger.getSelectedIndex()==0){
							TextToFile tmpFile = new TextToFile(textfield.getText(),"/tmp/festSpeech"+counter,addCommentary,hour,minute,second,"Robotic");
							tmpFile.execute();
						}else if (voiceChanger.getSelectedIndex()==1){
							FestivalSpeechWorker tmpFile = new FestivalSpeechWorker(textfield.getText(),"/tmp/festSpeech"+counter,addCommentary,hour,minute,second,1,counter,"Kiwi");
							tmpFile.execute();
						}else if (voiceChanger.getSelectedIndex()==2){
							FestivalSpeechWorker tmpFile = new FestivalSpeechWorker(textfield.getText(),"/tmp/festSpeech"+counter,addCommentary,hour,minute,second,2,counter,"British");
							tmpFile.execute();
						}else{
							JOptionPane.showMessageDialog(null, "Error in creating commentary, please try again");
						}
					}else{
						JOptionPane.showMessageDialog(null, "Please enter between 1-100 characters");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Error, please enter appropriate commentary (No Blank Commentary)");
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
				//Checks if there is commentary to add
				if (totalRows > 0){
					String cmd = "ffmpeg -y -i " + MainPlayerScreen.originalVideo + " ";
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
					OverlayMp3OntoVideo mergeVideo = new OverlayMp3OntoVideo(cmd, "coolffFile",true,mainPlayer);
					mergeVideo.execute();
					setVisible(false);
					MainPlayerScreen.saved=false;
				}else {
					JOptionPane.showMessageDialog(null, "Please create commentary before previewing");
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
				if (table.getSelectedRow()!= -1){
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
				}else{
					JOptionPane.showMessageDialog(null, "Please select a commentary to save");
				}
			}
		});

		textfield.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				//Clears the text when user first presses the box
				if (displayText){
					textfield.setText("");
					displayText=false;
				}		
			}
		});
	}
	public void addToTable(String content, String duration, String hour, String minute, String second, String path,String voiceType){
		if (checkTime(duration,hour,minute,second)){
			Object[] data = { content , duration, hour+":"+minute+":"+second,path,voiceType };
			commentaryTable.addRow(data);
		}else{
			JOptionPane.showMessageDialog(null, "Error, commentary will exceed the video length");
		}
	}
	//Appends zero to the front of the video if it is single digits.
	public String checkZero(String number){
		if (number.length()<=1){
			number="0"+number;
		}
		return number;
	}
	//Checks the time of the commentary does not exceed the length of the video.
	public boolean checkTime(String duration, String hour, String minute, String second){
		float totalTime= (Long.parseLong(hour)*3600) + (Long.parseLong(minute)*60) + (Long.parseLong(second))+Float.parseFloat(duration);
		float videoTime =mainPlayer.getOriginalVideoLength();
		if (videoTime < totalTime){
			return false;
		}
		return true;
	}
	//This will clear all the commentary and reset it back to intial state.
	public void clearCommentary(){
		commentaryTable.setRowCount(0);
		spinningModel.getHourSpinner().setValue(new Integer("0"));
		spinningModel.getMinuteSpinner().setValue(new Integer("0"));
		spinningModel.getSecondSpinner().setValue(new Integer("0"));
		textfield.setText("Add Commentary Here......[100 Character Limit]");
		displayText=true;
	}
}
