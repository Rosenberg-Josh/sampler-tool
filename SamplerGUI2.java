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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.awt.SystemColor;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ImageIcon;

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
	private JTextField filePath;
	private final JFileChooser openFileChooser;
	private final JFileChooser saveFileChooser;
	public File dataFile;
	public File statFile;
	public boolean loadPanelShown = false;
	public boolean dataLoadedCorrectly = false;
	public boolean drawPanelShown = false;
	private JTextField fileNameInput;
	public String dataFormat;
	JLabel lblMeanClaimAmnt;
	JLabel WeightedSampleMeanVal;
	JLabel AbsoluteDiffVal;
	JLabel PercentageDiffVal;
	JLabel StandardizedDiffVal;
	private JTextField sampleSizeField;
	private JTextField numberOfStrataField;
	private JTextField topClaimsField;
	private JTextField zeroDollarClaimsField;
	public boolean nextButtonPressed = false;
	public File clientDirectory;
	private JTextField Confidence_LevelField;

	
	public File getClientDirectory() {
		return clientDirectory;
	}

	public void setClientDirectory(File clientDirectory) {
		this.clientDirectory = clientDirectory;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

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
		
		saveFileChooser = new JFileChooser();
		saveFileChooser.setCurrentDirectory(new File("c:\\temp"));
		saveFileChooser.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));

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
		mainFrame.setBounds(50, 50, 600, 453);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel loadPanel = new JPanel();
		loadPanel.setBackground(Color.LIGHT_GRAY);
		//mainFrame.getContentPane().add(loadPanel);
		SpringLayout sl_loadPanel = new SpringLayout();
		loadPanel.setLayout(sl_loadPanel);
		

		JPanel drawPanel = new JPanel();
		drawPanel.setBackground(Color.LIGHT_GRAY);
		mainFrame.getContentPane().add(drawPanel);
		SpringLayout sl_drawPanel = new SpringLayout();
		drawPanel.setLayout(sl_drawPanel);
		
		JButton btnDone = new JButton("Done");
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, btnDone, -10, SpringLayout.SOUTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.EAST, btnDone, -10, SpringLayout.EAST, drawPanel);
		drawPanel.add(btnDone);
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		sl_drawPanel.putConstraint(SpringLayout.WEST, btnCancel_1, 10, SpringLayout.WEST, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, btnCancel_1, 0, SpringLayout.SOUTH, btnDone);
		drawPanel.add(btnCancel_1);
		
		JLabel lblSampleSize = new JLabel("Sample Info:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblSampleSize, 73, SpringLayout.NORTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblSampleSize, 41, SpringLayout.WEST, drawPanel);
		drawPanel.add(lblSampleSize);
		
		JLabel MeanClaimAmount = new JLabel("Population Mean Claim Amount: ");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, MeanClaimAmount, 20, SpringLayout.SOUTH, lblSampleSize);
		sl_drawPanel.putConstraint(SpringLayout.WEST, MeanClaimAmount, 41, SpringLayout.WEST, drawPanel);
		drawPanel.add(MeanClaimAmount);
		
		
		JButton btnExportTocsv = new JButton("Export to Drive");
		sl_drawPanel.putConstraint(SpringLayout.EAST, btnExportTocsv, -40, SpringLayout.EAST, drawPanel);
		btnExportTocsv.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//Handle open button action.
					String desFileName = fileNameInput.getText();
					if(desFileName.endsWith(".csv") == false){
						desFileName += ".csv";
					}
					while(SamplerMainClass.dataProcessed == false) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					
					if (e.getSource() == btnExportTocsv) {
						dataFile = CSVWriter.writeSampleFile(desFileName, SamplerMainClass.sampleClaims);
						statFile = CSVWriter.writeStatFile("TEMP", SamplerMainClass.finStrata);
						saveFileChooser.setSelectedFile(dataFile);

						int returnVal = saveFileChooser.showSaveDialog(drawPanel);
						File chosenDir = saveFileChooser.getCurrentDirectory(); //Directory which user chose to output sample
						clientDirectory = chosenDir;

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				        	dataFile.renameTo(new File(chosenDir + "\\" + "AUDIT_SAMPLE_FOR_" + dataFile.toString())); //Move file to chosen directory
				        	statFile.renameTo(new File(chosenDir + "\\" + "AUDIT_STATS_FOR_" + dataFile.toString())); //Move file to chosen directory
				        	ExcelWriter.writeToTemplate(chosenDir, SamplerMainClass.sampleClaims, SamplerMainClass.finStrata);
				            filePath.setText(dataFile.getPath());
				            if(dataFile.getName().endsWith(".csv")) {
				            		dataLoadedCorrectly = true;
				            }
				        }
				   } 
				}
			});

		sl_drawPanel.putConstraint(SpringLayout.NORTH, btnExportTocsv, 132, SpringLayout.NORTH, drawPanel);
		drawPanel.add(btnExportTocsv);

		
		
		fileNameInput = new JTextField();
		sl_drawPanel.putConstraint(SpringLayout.EAST, fileNameInput, -41, SpringLayout.EAST, drawPanel);
		drawPanel.add(fileNameInput);
		fileNameInput.setColumns(10);
		
		JLabel lblEnteredNameOf = new JLabel("Enter name of Entity:");
		sl_drawPanel.putConstraint(SpringLayout.EAST, lblEnteredNameOf, -40, SpringLayout.EAST, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, fileNameInput, 6, SpringLayout.SOUTH, lblEnteredNameOf);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblEnteredNameOf, 0, SpringLayout.NORTH, lblSampleSize);
		drawPanel.add(lblEnteredNameOf);
		
		lblMeanClaimAmnt = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblMeanClaimAmnt, 0, SpringLayout.NORTH, MeanClaimAmount);
		sl_drawPanel.putConstraint(SpringLayout.EAST, lblMeanClaimAmnt, -161, SpringLayout.WEST, fileNameInput);
		drawPanel.add(lblMeanClaimAmnt);
		lblMeanClaimAmnt.setVisible(false);
		
		WeightedSampleMeanVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.EAST, WeightedSampleMeanVal, -161, SpringLayout.WEST, btnExportTocsv);
		drawPanel.add(WeightedSampleMeanVal);
		WeightedSampleMeanVal.setVisible(false);
		
		JLabel lblWeightedSampleMean = new JLabel("Weighted Sample Mean: ");
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblWeightedSampleMean, 41, SpringLayout.WEST, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, WeightedSampleMeanVal, 0, SpringLayout.NORTH, lblWeightedSampleMean);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, lblWeightedSampleMean, 0, SpringLayout.SOUTH, btnExportTocsv);
		drawPanel.add(lblWeightedSampleMean);
		
		JLabel lblAbsoluteDifference = new JLabel("Absolute Difference:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblAbsoluteDifference, 17, SpringLayout.SOUTH, lblWeightedSampleMean);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblAbsoluteDifference, 41, SpringLayout.WEST, drawPanel);
		drawPanel.add(lblAbsoluteDifference);
		
		AbsoluteDiffVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, AbsoluteDiffVal, 0, SpringLayout.NORTH, lblAbsoluteDifference);
		sl_drawPanel.putConstraint(SpringLayout.WEST, AbsoluteDiffVal, 268, SpringLayout.WEST, drawPanel);
		drawPanel.add(AbsoluteDiffVal);
		AbsoluteDiffVal.setVisible(false);
		
		JLabel lblPercentageDifference = new JLabel("Percentage Difference:");
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblPercentageDifference, 25, SpringLayout.SOUTH, lblAbsoluteDifference);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblPercentageDifference, 41, SpringLayout.WEST, drawPanel);
		drawPanel.add(lblPercentageDifference);
		
		PercentageDiffVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, PercentageDiffVal, 0, SpringLayout.NORTH, lblPercentageDifference);
		sl_drawPanel.putConstraint(SpringLayout.WEST, PercentageDiffVal, 0, SpringLayout.EAST, lblMeanClaimAmnt);
		//sl_drawPanel.putConstraint(SpringLayout.SOUTH, PercentageDiffVal, 0, SpringLayout.NORTH, lblPercentageDifference);
		//sl_drawPanel.putConstraint(SpringLayout.EAST, PercentageDiffVal, 0, SpringLayout.EAST, lblMeanClaimAmnt);
		drawPanel.add(PercentageDiffVal);
		PercentageDiffVal.setVisible(false);
		
		StandardizedDiffVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, StandardizedDiffVal, -171, SpringLayout.SOUTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.EAST, StandardizedDiffVal, 0, SpringLayout.EAST, lblMeanClaimAmnt);
		drawPanel.add(StandardizedDiffVal);
		
		JLabel gLogo2 = new JLabel("");
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, gLogo2, -31, SpringLayout.SOUTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.EAST, gLogo2, -86, SpringLayout.WEST, btnDone);
		File image = new File("gallagher_wtag_stackedlarge-3d-1.png");
		gLogo2.setIcon(new ImageIcon("/Users/joshrosenberg/eclipse-workspace/Sampling.Tool/gallagher_wtag_stackedlarge-3d-1.png"));
		drawPanel.add(gLogo2);
		
		
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
		sl_loadPanel.putConstraint(SpringLayout.EAST, btnNext, -10, SpringLayout.EAST, loadPanel);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dataLoadedCorrectly == true) {
					nextButtonPressed = true;
					mainFrame.getContentPane().add(drawPanel);
					mainFrame.getContentPane().remove(loadPanel);
					mainFrame.getContentPane().validate();
					mainFrame.getContentPane().repaint();
				}
			}
		});
		loadPanel.add(btnNext);
		
		JButton btnCancel = new JButton("Cancel");
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.NORTH, btnNext, 0, SpringLayout.NORTH, btnCancel);
		sl_loadPanel.putConstraint(SpringLayout.WEST, btnCancel, 10, SpringLayout.WEST, loadPanel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		loadPanel.add(btnCancel);
		
		String[] formatOptions = { "Select Type", "Standard (Obs Num, Claim Id, etc..)", "From Sas (Amount and Freq" };
		
		JLabel lblSampleSizel = new JLabel("Sample Size:");
		loadPanel.add(lblSampleSizel);
		
		sampleSizeField = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblSampleSizel, 5, SpringLayout.NORTH, sampleSizeField);
		sl_loadPanel.putConstraint(SpringLayout.WEST, sampleSizeField, 0, SpringLayout.WEST, btnOpenFile);
		loadPanel.add(sampleSizeField);
		sampleSizeField.setText(String.valueOf(SamplerMainClass.nTotalSamples));
		sampleSizeField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Number of Strata: ");
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblSampleSizel, 0, SpringLayout.EAST, lblNewLabel_1);
		loadPanel.add(lblNewLabel_1);
		
		numberOfStrataField = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblNewLabel_1, 5, SpringLayout.NORTH, numberOfStrataField);
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblNewLabel_1, -23, SpringLayout.WEST, numberOfStrataField);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, sampleSizeField, -6, SpringLayout.NORTH, numberOfStrataField);
		sl_loadPanel.putConstraint(SpringLayout.WEST, numberOfStrataField, 0, SpringLayout.WEST, btnOpenFile);
		loadPanel.add(numberOfStrataField);
		numberOfStrataField.setText(String.valueOf(SamplerMainClass.nMajorStrata));
		numberOfStrataField.setColumns(10);
		
		JLabel lblNumberOfTop = new JLabel("Number of Top Claims: ");
		loadPanel.add(lblNumberOfTop);
		
		topClaimsField = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblNumberOfTop, 5, SpringLayout.NORTH, topClaimsField);
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblNumberOfTop, -23, SpringLayout.WEST, topClaimsField);
		sl_loadPanel.putConstraint(SpringLayout.NORTH, topClaimsField, 185, SpringLayout.NORTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.WEST, topClaimsField, 297, SpringLayout.WEST, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, numberOfStrataField, -6, SpringLayout.NORTH, topClaimsField);
		loadPanel.add(topClaimsField);
		topClaimsField.setText(String.valueOf(SamplerMainClass.nTopNSamples));
		topClaimsField.setColumns(10);
		
		JLabel lblZeroDollarClaims = new JLabel("Zero Dollar Claims: ");
		loadPanel.add(lblZeroDollarClaims);
		
		zeroDollarClaimsField = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblZeroDollarClaims, -23, SpringLayout.WEST, zeroDollarClaimsField);
		sl_loadPanel.putConstraint(SpringLayout.NORTH, zeroDollarClaimsField, 217, SpringLayout.NORTH, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblZeroDollarClaims, 5, SpringLayout.NORTH, zeroDollarClaimsField);
		sl_loadPanel.putConstraint(SpringLayout.WEST, zeroDollarClaimsField, 0, SpringLayout.WEST, btnOpenFile);
		loadPanel.add(zeroDollarClaimsField);
		zeroDollarClaimsField.setText(String.valueOf(SamplerMainClass.nZeroDollarSamples));
		zeroDollarClaimsField.setColumns(10);
		
		JLabel lblConfidenceLevel = new JLabel("Confidence Level: ");
		sl_loadPanel.putConstraint(SpringLayout.NORTH, lblConfidenceLevel, 21, SpringLayout.SOUTH, lblZeroDollarClaims);
		loadPanel.add(lblConfidenceLevel);
		
		Confidence_LevelField = new JTextField();
		sl_loadPanel.putConstraint(SpringLayout.EAST, lblConfidenceLevel, -23, SpringLayout.WEST, Confidence_LevelField);
		sl_loadPanel.putConstraint(SpringLayout.SOUTH, Confidence_LevelField, 0, SpringLayout.SOUTH, lblConfidenceLevel);
		sl_loadPanel.putConstraint(SpringLayout.EAST, Confidence_LevelField, 0, SpringLayout.EAST, sampleSizeField);
		loadPanel.add(Confidence_LevelField);
		Confidence_LevelField.setColumns(10);
		
		JLabel gLogoLoad = new JLabel("");
		sl_loadPanel.putConstraint(SpringLayout.NORTH, gLogoLoad, 17, SpringLayout.SOUTH, lblConfidenceLevel);
		sl_loadPanel.putConstraint(SpringLayout.EAST, gLogoLoad, 0, SpringLayout.EAST, sampleSizeField);
		File image2 = new File("gallagher_wtag_stackedlarge-3d-1.png");
		gLogoLoad.setIcon(new ImageIcon(image2.getAbsolutePath()));
		loadPanel.add(gLogoLoad);
		
		
	}

	public JTextField getSampleSizeField() {
		return sampleSizeField;
	}

	public void setSampleSizeField(JTextField sampleSizeField) {
		this.sampleSizeField = sampleSizeField;
	}

	public JTextField getNumberOfStrataField() {
		return numberOfStrataField;
	}

	public void setNumberOfStrataField(JTextField numberOfStrataField) {
		this.numberOfStrataField = numberOfStrataField;
	}

	public JTextField getTopClaimsField() {
		return topClaimsField;
	}

	public void setTopClaimsField(JTextField topClaimsField) {
		this.topClaimsField = topClaimsField;
	}

	public JTextField getZeroDollarClaimsField() {
		return zeroDollarClaimsField;
	}

	public void setZeroDollarClaimsField(JTextField zeroDollarClaimsField) {
		this.zeroDollarClaimsField = zeroDollarClaimsField;
	}
}

