package vidVox.guiScreens;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import vidVox.workers.OverlayMp3OntoVideo;

public class AddCommentaryScreen extends JFrame{

	public static JFileChooser ourFileSelector= new JFileChooser();
	private JPanel pane;
	private JTextField textfield;
	public static TextToMp3Screen createCommentaryScreen;
	public AddCommentaryScreen (MainPlayerScreen screen){
		
		//making the main initial layout for the AddCommentaryScreen
		//setBounds(450, 450, 850, 100);
		GridBagConstraints c = new GridBagConstraints();
		setTitle("Choose a mp3 file to overlay onto the current video");
		createCommentaryScreen = new TextToMp3Screen(screen);
		createCommentaryScreen.setBounds(385, 475, 650, 100);
		createCommentaryScreen.setMinimumSize(new Dimension(650, 100));
        final DefaultTableModel audioOverlayTable;
        String[] audioOverlayOptions = {"Commentary", "Duration","Time to add"};
        audioOverlayTable = new DefaultTableModel(audioOverlayOptions,0){
        @Override
        public Class<?> getColumnClass(int columnIndex) {
        	 Class classType = String.class;
        	      switch (columnIndex) {
        	        case 0:
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
       final JTable table = new JTable(audioOverlayTable);
       table.setModel(audioOverlayTable);
       table.setPreferredSize(new Dimension(550,400));
       JScrollPane scrollPane = new JScrollPane(); 
      // scrollPane.setBounds(20, 75, 400, 400);
       scrollPane.setViewportView(table);
     //  scrollPane.setMinimumSize( scrollPane.getPreferredSize() );
       
       for (int i=0; i<10;i++){
       Object[] data = { i , i, i };
       audioOverlayTable.addRow(data);
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
		JSpinner hourSpinner = new JSpinner(hourSpinnerModel);
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
		JSpinner minuteSpinner = new JSpinner(minuteSpinnerModel);
		c = new GridBagConstraints();
		c.gridx = 4;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.insets = new Insets(0,3,0,3);
		pane.add(minuteSpinner, c);
		

		SpinnerModel secondSpinnerModel = new SpinnerNumberModel(0,0,60,1);
		JSpinner secondSpinner = new JSpinner(secondSpinnerModel);
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

				ourFileSelector.showOpenDialog(null);
				if (!(ourFileSelector.getSelectedFile() == null)){
					ourFile=ourFileSelector.getSelectedFile();
					mediaPath=ourFile.getAbsolutePath();
					textfield.setText(mediaPath);	
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
				 Object[] data = { textfield.getText() , "gg", "time" };
				audioOverlayTable.addRow(data);
			 }
		 });
		 
			
		 deleteRow.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {

				 int rowsSelected = table.getSelectedRows().length;
				 for(int i=0; i<rowsSelected ; i++ ) {

				     audioOverlayTable.removeRow(table.getSelectedRow());
				 }
				 	
			        
			 }
		 });


	}


}
