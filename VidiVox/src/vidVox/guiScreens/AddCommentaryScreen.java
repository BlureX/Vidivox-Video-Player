package vidVox.guiScreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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
import vidVox.workers.OverlayMp3OntoVideo;
import vidVox.workers.PreviewMP3;
import vidVox.workers.TextToFile;

public class AddCommentaryScreen extends JFrame{

	public static JFileChooser ourFileSelector= new JFileChooser();
	private JPanel pane;
	private JTextField textfield;
	public static TextToMp3Screen createCommentaryScreen;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private JSpinner secondSpinner;
	private int counter=0;
	List<PreviewMP3> voiceList=new ArrayList<PreviewMP3>();	


	public AddCommentaryScreen (MainPlayerScreen screen){

		//making the main initial layout for the AddCommentaryScreen
		//setBounds(450, 450, 850, 100);
		GridBagConstraints c = new GridBagConstraints();
		setTitle("Choose a mp3 file to overlay onto the current video");
		createCommentaryScreen = new TextToMp3Screen(screen);
		createCommentaryScreen.setBounds(385, 475, 650, 100);
		createCommentaryScreen.setMinimumSize(new Dimension(650, 100));
		final DefaultTableModel commentaryTable;
		String[] audioOverlayOptions = {"Commentary", "Duration","Time to add",""};
		//Code which I have referenced to hide the table
		//http://stackoverflow.com/questions/1492217/how-to-make-a-columns-in-jtable-invisible-for-swing-java
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
		final JTable table = new JTable(commentaryTable);
		table.setModel(commentaryTable);
		table.setPreferredSize(new Dimension(550,400));
		//table.removeColumn(table.getColumnModel().getColumn(3));
		JScrollPane scrollPane = new JScrollPane(); 
		// scrollPane.setBounds(20, 75, 400, 400);
		scrollPane.setViewportView(table);
		//  scrollPane.setMinimumSize( scrollPane.getPreferredSize() );

		for (int i=0; i<10;i++){
			Object[] data = { i , i, i };
			commentaryTable.addRow(data);
		}
		//creating the content pane which will store all of the addcommentaryScreen components
		GridBagLayout gbl_Pane = new GridBagLayout();
		pane = new JPanel(gbl_Pane);
		setContentPane(pane);

		//Creates a Jtable to display my commentary I will add to the video.
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 3;
		c.gridwidth = 5;
		c.weighty = 99;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10,10,10,10);
		pane.add(scrollPane, c);

		//creating a Jbutton which will add commentary to the start of the video
		JButton selectMp3 = new JButton("Add Mp3 Commentary..");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;

		pane.add(selectMp3, c);

		//creating a label which will show the currently opened file;
		/*textfield = new JTextField("N/A");
		textfield.setEditable(false);
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 3;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(textfield, c);*/

		//creating a JButton which allows you to delete the selected commentary.
		JButton deleteRow = new JButton("Delete Commentary");
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weightx = 3;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		pane.add(deleteRow, c);

		//creating a label which will show the currently opened file;
		/*JLabel label1 = new JLabel("Current File: ");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;

		pane.add(label1, c);
		 */
		//creating a Jbutton which will add commentary to the start of the video
		/*JButton start = new JButton("Add at start of video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		c.anchor = GridBagConstraints.WEST;
		pane.add(start, c);*/

		//creating a Jbutton which will add commentary to the start of the video
		JButton createCommentary = new JButton("Create Commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(0,5,0,10);
		c.anchor = GridBagConstraints.WEST;
		pane.add(createCommentary, c);

		textfield = new JTextField();
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0,3,0,3);
		c.fill = GridBagConstraints.HORIZONTAL;
		pane.add(textfield, c);

		JLabel label1 = new JLabel("Hours/Minutes/Seconds");
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 0;
		c.insets = new Insets(0,1,0,1);
		c.anchor = GridBagConstraints.WEST;
		pane.add(label1, c);

		SpinnerModel hourSpinnerModel = new SpinnerNumberModel(0,0,113,1);
		hourSpinner = new JSpinner(hourSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0,3,0,3);
		pane.add(hourSpinner, c);

		SpinnerModel minuteSpinnerModel = new SpinnerNumberModel(0,0,60,1);
		minuteSpinner = new JSpinner(minuteSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,3,0,3);
		pane.add(minuteSpinner, c);


		SpinnerModel secondSpinnerModel = new SpinnerNumberModel(0,0,60,1);
		secondSpinner = new JSpinner(secondSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0,3,0,3);
		pane.add(secondSpinner, c);

		JLabel one = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 3;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		pane.add(one, c);

		JLabel two = new JLabel();
		c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 1;
		pane.add(two, c);

		JButton createVideo = new JButton("Create Video");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.EAST;
		//c.insets = new Insets(3,3,3,3);
		pane.add(createVideo, c);

		JButton preview = new JButton("Preview");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(3,3,3,3);
		pane.add(preview, c);
		
		JButton saveButton = new JButton("Save");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(3,3,3,3);
		pane.add(saveButton, c);

		/*
		JButton editCommentary = new JButton("Edit Commentary");
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		//c.insets = new Insets(3,3,3,3);
		pane.add(editCommentary, c);

		JButton editTime = new JButton("Edit Time");
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		//c.insets = new Insets(3,3,3,3);
		pane.add(editTime, c);
		 */


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
						Object[] data = { ourFile.getName() , "gg", hour+":"+minute+":"+second, mediaPath};
						commentaryTable.addRow(data);	
					}else {
						JOptionPane.showMessageDialog(null, "Error please select an appropriate file");
					}
				}else if (status == JFileChooser.CANCEL_OPTION){

				}
			}
		});
		//Action listener for adding video at the start.
		/*start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Checks if commentary file is empty.
				if (!(textfield.getText().equals("N/A"))){
					OverlayMp3OntoVideo k = new OverlayMp3OntoVideo(textfield.getText(), "kkona", true);
					k.execute();
				}else{
					JOptionPane.showMessageDialog(null, "Error please select a mp3 before adding commentary please");
				}
			}
		});*/

		createCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				TextToFile tmpFile = new TextToFile(textfield.getText(),"/tmp/festSpeech"+counter,false);
				tmpFile.execute();
				Object[] data = { textfield.getText() , "gg", hour+":"+minute+":"+second,"/tmp/festSpeech"+counter+".mp3" };
				counter++;
				commentaryTable.addRow(data);
			}
		});


		deleteRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int rowsSelected = table.getSelectedRows().length;
				for(int i=0; i<rowsSelected ; i++ ) {

					commentaryTable.removeRow(table.getSelectedRow());
				}


			}
		});

		createVideo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int totalRows = table.getRowCount();
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
			}
		});

		preview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row=table.getSelectedRow();
				boolean speaking=false;
				if (row >-1 ){
					//Loop through the current playing voices.
					for (PreviewMP3 currentCommentary : voiceList){
						//If it is in the festival voice but stopped playing, remove it and set speaking to false.
						if (commentaryTable.getValueAt(row, 3).equals(currentCommentary.getFile()) && currentCommentary.speaking==false){
							voiceList.remove(currentCommentary);
							System.out.println("GG");
							speaking=false;
							break;
							//If it is still speaking, set speaking to true.
						}else if (commentaryTable.getValueAt(row, 3).equals(currentCommentary.getFile()) && currentCommentary.speaking==true){
							speaking=true;
							System.out.println("SPEAKING NOW SOn");
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

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String mediaPath="~/";
				File ourFile;
				FileFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
				SaveDialogScreen.ourFileSelector.resetChoosableFileFilters();
				//opening the save file explorer, user gets to choose where to save the file   
				SaveDialogScreen.ourFileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
				SaveDialogScreen.ourFileSelector.setFileFilter(filter);
				int status = SaveDialogScreen.ourFileSelector.showSaveDialog(null);
				if (status == JFileChooser.APPROVE_OPTION){
					if (!(SaveDialogScreen.ourFileSelector.getSelectedFile() == null)){
						ourFile=SaveDialogScreen.ourFileSelector.getSelectedFile();
						mediaPath=ourFile.getAbsolutePath();
						if ((!(mediaPath.endsWith(".mp3")))) {
							mediaPath = mediaPath+".mp3";
						}
						//creates the mp3 file at the location
						int row = table.getSelectedRow();
						String filename = commentaryTable.getValueAt(row, 3).toString();
						MoveFile k = new MoveFile(filename,mediaPath);
						k.execute();
					}else{
						JOptionPane.showMessageDialog(null, "Error please save to an appropriate location");
					}
				}else if (status == JFileChooser.CANCEL_OPTION){

				}

			}
		});
		/*
		editCommentary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowsSelected = table.getSelectedRows();
				for(int i=0; i<rowsSelected.length ; i++ ) {
					audioOverlayTable.setValueAt(textfield.getText(), rowsSelected[i], 0);
				}
				TextToFile tmpFile = new TextToFile(textfield.getText(),"/tmp/festSpeech"+counter,false);
				tmpFile.execute();
				counter++;
			}
		});

		editTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rowsSelected = table.getSelectedRows();

				for(int i=0; i<rowsSelected.length ; i++ ) {
					audioOverlayTable.setValueAt(textfield.getText(), rowsSelected[i], 0);
				}

			}
		});
		 */
	}


}
