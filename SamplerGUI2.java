package SamplerPackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;

import java.awt.SystemColor;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.SpringLayout;
import java.awt.Button;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.JPopupMenu;


public class SamplerGUI2 {


	public JFrame mainFrame;
	private JTextField txtClientName;
	private JTextField txtClientLocation;
	private JTextField textField;
	private JTextField filePath;
	private final JFileChooser openFileChooser;
	public File dataFile;
	public boolean loadPanelShown = false;
	public boolean dataLoadedCorrectly = false;
	public boolean drawPanelShown = false;
	private JTextField fileNameInput;
	
	

	
	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	/**
	 * Create the application UI.
	 */
	public SamplerGUI2() {
		initialize();
		
		
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("c:\\temp"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));

	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		/*  Create main frame */
		mainFrame = new JFrame();
		mainFrame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
		});
		mainFrame.setTitle("Sample Creator");
		mainFrame.setBounds(50, 50, 600, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel loadPanel = new JPanel();
		loadPanel.setBackground(Color.LIGHT_GRAY);
		mainFrame.getContentPane().add(loadPanel);
		SpringLayout sl_loadPanel = new SpringLayout();
		loadPanel.setLayout(sl_loadPanel);
		

		JPanel drawPanel = new JPanel();
		drawPanel.setBackground(Color.LIGHT_GRAY);
		//mainFrame.getContentPane().add(drawPanel);
		SpringLayout sl_drawPanel = new SpringLayout();
		drawPanel.setLayout(sl_drawPanel);
		
		JButton btnNext_1 = new JButton("Done");
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, btnNext_1, -10, SpringLayout.SOUTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.EAST, btnNext_1, -10, SpringLayout.EAST, drawPanel);
		drawPanel.add(btnNext_1);
		
		JButton btnBack = new JButton("Back");
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, btnBack, 0, SpringLayout.SOUTH, btnNext_1);
		sl_drawPanel.putConstraint(SpringLayout.EAST, btnBack, -9, SpringLayout.WEST, btnNext_1);
		drawPanel.add(btnBack);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		sl_drawPanel.putConstraint(SpringLayout.WEST, btnCancel_1, 10, SpringLayout.WEST, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, btnCancel_1, 0, SpringLayout.SOUTH, btnNext_1);
		drawPanel.add(btnCancel_1);
		
		JLabel lblSampleInfo = new JLabel("Sample Info:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblSampleInfo, 31, SpringLayout.NORTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblSampleInfo, 41, SpringLayout.WEST, drawPanel);
		drawPanel.add(lblSampleInfo);
		
		JLabel lblSampleSize = new JLabel("Sample Size: ");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblSampleSize, 26, SpringLayout.SOUTH, lblSampleInfo);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblSampleSize, 0, SpringLayout.WEST, lblSampleInfo);
		drawPanel.add(lblSampleSize);
		
		JLabel lblTotalAmmount = new JLabel("Total Ammount:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblTotalAmmount, 20, SpringLayout.SOUTH, lblSampleSize);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblTotalAmmount, 0, SpringLayout.WEST, lblSampleInfo);
		drawPanel.add(lblTotalAmmount);
		
		
		JLabel finishNotice = new JLabel("File should now appear on desktop. Press \"Done\" to close.");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, finishNotice, -53, SpringLayout.NORTH, btnBack);
		sl_drawPanel.putConstraint(SpringLayout.WEST, finishNotice, -366, SpringLayout.EAST, btnBack);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, finishNotice, -24, SpringLayout.NORTH, btnBack);
		sl_drawPanel.putConstraint(SpringLayout.EAST, finishNotice, 0, SpringLayout.EAST, btnBack);
		drawPanel.add(finishNotice);
		finishNotice.setVisible(false);
		
		JButton btnExportTocsv = new JButton("Export to .csv");
		btnExportTocsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String desFileName = fileNameInput.getText();
				if(desFileName.endsWith(".csv") == false){
					System.out.println("IMPROPER FORMAT");
					desFileName += ".csv";
				}
				while(SamplerMainClass.dataProcessed == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				CSVWriter.writeSampleFile(desFileName, SamplerMainClass.sampleClaims);
				CSVWriter.writeStatFile("stats.csv", SamplerMainClass.getMajorStrata());
				finishNotice.setVisible(true);
			}
		});
		sl_drawPanel.putConstraint(SpringLayout.NORTH, btnExportTocsv, 132, SpringLayout.NORTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.WEST, btnExportTocsv, 0, SpringLayout.WEST, btnBack);
		drawPanel.add(btnExportTocsv);
		
		JLabel lblNewLabel = new JLabel("225");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblNewLabel, 0, SpringLayout.NORTH, lblSampleSize);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblNewLabel, 57, SpringLayout.EAST, lblSampleSize);
		drawPanel.add(lblNewLabel);

		
		
		fileNameInput = new JTextField();
		sl_drawPanel.putConstraint(SpringLayout.WEST, fileNameInput, 0, SpringLayout.WEST, btnBack);
		drawPanel.add(fileNameInput);
		fileNameInput.setColumns(10);
		
		JLabel lblEnteredNameOf = new JLabel("Enter name of file:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, fileNameInput, 6, SpringLayout.SOUTH, lblEnteredNameOf);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblEnteredNameOf, 0, SpringLayout.NORTH, lblSampleSize);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblEnteredNameOf, 0, SpringLayout.WEST, btnBack);
		drawPanel.add(lblEnteredNameOf);
		
		
		JLabel lblSelectCsvFile = new JLabel("Select csv file or specify absolute path:");
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblSelectCsvFile, 76, SpringLayout.NORTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.WEST, lblSelectCsvFile, 32, SpringLayout.WEST, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, lblSelectCsvFile, 101, SpringLayout.NORTH, loadPanel);
		lblSelectCsvFile.setBackground(Color.BLACK);
		loadPanel.add(lblSelectCsvFile);
		
		
		/**Button that opens the file opener
		 * 
		 */
		JButton btnOpenFile = new JButton("Open File...");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Handle open button action.
			    if (e.getSource() == btnOpenFile) {
			        int returnVal = openFileChooser.showOpenDialog(loadPanel);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            dataFile = openFileChooser.getSelectedFile();
			            //This is where a real application would open the file.
			            filePath.setText(dataFile.getPath());
			            if(dataFile.getName().endsWith(".csv")) {
			            		dataLoadedCorrectly = true;
			            }
			        } else {
			        		
			        }
			   } 
					
				
			}
		});
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblSelectCsvFile, -6, SpringLayout.WEST, btnOpenFile);
		sl_loadPanel.putConstraint(SpringLayout.NORTH, btnOpenFile, 75, SpringLayout.NORTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.WEST, btnOpenFile, 297, SpringLayout.WEST, loadPanel);
		loadPanel.add(btnOpenFile);
		
		filePath = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.NORTH, filePath, 75, SpringLayout.NORTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.WEST, filePath, 6, SpringLayout.EAST, btnOpenFile);
		sl_loadPanel.putConstraint(SpringLayout.EAST, filePath, 178, SpringLayout.EAST, btnOpenFile);
		loadPanel.add(filePath);
		filePath.setColumns(10);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dataLoadedCorrectly == true) {
					mainFrame.getContentPane().add(drawPanel);
					mainFrame.getContentPane().remove(loadPanel);
					mainFrame.getContentPane().validate();
					mainFrame.getContentPane().repaint();
				}
			}
		});
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, btnNext, -10, SpringLayout.SOUTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.EAST, btnNext, 0, SpringLayout.EAST, filePath);
		loadPanel.add(btnNext);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		sl_loadPanel.putConstraint(SpringLayout.WEST, btnCancel, 10, SpringLayout.WEST, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, btnCancel, 0, SpringLayout.SOUTH, btnNext);
		loadPanel.add(btnCancel);
		
		JLabel lblOr = new JLabel("OR:");
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblOr, 44, SpringLayout.SOUTH, btnOpenFile);
		sl_loadPanel.putConstraint(SpringLayout.WEST, lblOr, 0, SpringLayout.WEST, btnOpenFile);
		loadPanel.add(lblOr);
		
		JLabel lblSelectFromDatabase = new JLabel("Select from DataBase:");
		sl_loadPanel.putConstraint(SpringLayout.WEST, lblSelectFromDatabase, 0, SpringLayout.WEST, lblSelectCsvFile);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, lblSelectFromDatabase, -82, SpringLayout.NORTH, btnCancel);
		loadPanel.add(lblSelectFromDatabase);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mainFrame.setJMenuBar(menuBar);
		
		JMenuItem mntmLoadFile = new JMenuItem("Load File");
		mntmLoadFile.setBackground(SystemColor.inactiveCaption);
		mntmLoadFile.setSelected(true);
		mntmLoadFile.setForeground(SystemColor.textInactiveText);
		mntmLoadFile.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mntmLoadFile);
		
		JMenuItem mntmDrawSample = new JMenuItem("Draw Sample and Export");
		mntmDrawSample.setBackground(SystemColor.inactiveCaptionBorder);
		mntmDrawSample.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mntmDrawSample);
		
		
	}
}

