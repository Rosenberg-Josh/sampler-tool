package SamplerPackage;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.Toolkit;


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
	private JTextField sampleSizeField;
	private JTextField numberOfStrataField;
	private JTextField topClaimsField;
	private JTextField zeroDollarClaimsField;
	public boolean nextButtonPressed = false;
	public File clientDirectory;
	private JTextField Confidence_LevelField;
	public static ArrayList<String> headers;
	public static JComboBox comboBox;
	public static JComboBox comboBox_1;

	
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
		openFileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Excel file", "xls", "xlsx"));
		
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
		mainFrame.setResizable(false);
		mainFrame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
		});
		mainFrame.setTitle("Sample Creator");
		mainFrame.setBounds(50, 50, 600, 453);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		/*
		 * Process Image for use in background
		 */
		Image img = null;
		File imgRel = new File("GallagherBackgroung.PNG");
		try{
			img = ImageIO.read(new File(imgRel.getAbsolutePath()));
		} catch (IOException e){
			e.printStackTrace();
		}

		
		BackgroundPanel bpanel = new BackgroundPanel(img);
		mainFrame.getContentPane().add(bpanel);
		

		BackgroundPanel drawPanel = new BackgroundPanel(img);
		drawPanel.setBackground(Color.LIGHT_GRAY);
		//mainFrame.getContentPane().add(drawPanel);
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
		drawPanel.add(lblSampleSize);
		
		JLabel MeanClaimAmount = new JLabel("Population Mean Claim Amount: ");
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblSampleSize, 0, SpringLayout.WEST, MeanClaimAmount);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, lblSampleSize, -27, SpringLayout.NORTH, MeanClaimAmount);
		drawPanel.add(MeanClaimAmount);
		
		
		JButton btnExportTocsv = new JButton("Export to Drive");
		sl_drawPanel.putConstraint(SpringLayout.EAST, btnExportTocsv, -41, SpringLayout.EAST, drawPanel);
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
				        	ExcelWriter.writeSample(SamplerMainClass.sampleClaims, SamplerMainClass.finStrata, desFileName, chosenDir);
				            filePath.setText(dataFile.getPath());
				           if(dataFile.getName().endsWith(".csv")) {
				            		dataLoadedCorrectly = true;
				            }
				            
				        }
				   } 
				}
			});
		drawPanel.add(btnExportTocsv);

		
		
		fileNameInput = new JTextField();
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, fileNameInput, -231, SpringLayout.SOUTH, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, btnExportTocsv, 6, SpringLayout.SOUTH, fileNameInput);
		sl_drawPanel.putConstraint(SpringLayout.WEST, fileNameInput, 0, SpringLayout.WEST, btnExportTocsv);
		drawPanel.add(fileNameInput);
		fileNameInput.setColumns(10);
		
		JLabel lblEnteredNameOf = new JLabel("Enter name of Entity:");
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblEnteredNameOf, 0, SpringLayout.WEST, btnExportTocsv);
		sl_drawPanel.putConstraint(SpringLayout.SOUTH, lblEnteredNameOf, -13, SpringLayout.NORTH, fileNameInput);
		drawPanel.add(lblEnteredNameOf);
		
		lblMeanClaimAmnt = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, MeanClaimAmount, 0, SpringLayout.NORTH, lblMeanClaimAmnt);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblMeanClaimAmnt, 109, SpringLayout.NORTH, drawPanel);
		lblMeanClaimAmnt.setText("Sampling in Progress\n");
		drawPanel.add(lblMeanClaimAmnt);
		//lblMeanClaimAmnt.setVisible(false);
		
		WeightedSampleMeanVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblMeanClaimAmnt, 0, SpringLayout.WEST, WeightedSampleMeanVal);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, WeightedSampleMeanVal, 0, SpringLayout.NORTH, lblEnteredNameOf);
		WeightedSampleMeanVal.setVerticalAlignment(SwingConstants.TOP);
		WeightedSampleMeanVal.setText("N/A\n");
		drawPanel.add(WeightedSampleMeanVal);
		//WeightedSampleMeanVal.setVisible(false);
		
		JLabel lblWeightedSampleMean = new JLabel("Weighted Sample Mean: ");
		sl_drawPanel.putConstraint(SpringLayout.WEST, MeanClaimAmount, 0, SpringLayout.WEST, lblWeightedSampleMean);
		sl_drawPanel.putConstraint(SpringLayout.WEST, WeightedSampleMeanVal, 75, SpringLayout.EAST, lblWeightedSampleMean);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblWeightedSampleMean, 0, SpringLayout.NORTH, lblEnteredNameOf);
		drawPanel.add(lblWeightedSampleMean);
		
		JLabel lblAbsoluteDifference = new JLabel("Absolute Difference:");
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblWeightedSampleMean, 0, SpringLayout.WEST, lblAbsoluteDifference);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblAbsoluteDifference, 5, SpringLayout.NORTH, fileNameInput);
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblAbsoluteDifference, 37, SpringLayout.WEST, drawPanel);
		drawPanel.add(lblAbsoluteDifference);
		
		AbsoluteDiffVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, AbsoluteDiffVal, 5, SpringLayout.NORTH, fileNameInput);
		sl_drawPanel.putConstraint(SpringLayout.WEST, AbsoluteDiffVal, 0, SpringLayout.WEST, lblMeanClaimAmnt);
		AbsoluteDiffVal.setVerticalAlignment(SwingConstants.TOP);
		AbsoluteDiffVal.setText("N/A");
		drawPanel.add(AbsoluteDiffVal);
		
		PercentageDiffVal = new JLabel();
		sl_drawPanel.putConstraint(SpringLayout.NORTH, PercentageDiffVal, 11, SpringLayout.SOUTH, AbsoluteDiffVal);
		PercentageDiffVal.setVerticalAlignment(SwingConstants.TOP);
		PercentageDiffVal.setText("N/A");
		//sl_drawPanel.putConstraint(SpringLayout.SOUTH, PercentageDiffVal, 0, SpringLayout.NORTH, lblPercentageDifference);
		//sl_drawPanel.putConstraint(SpringLayout.EAST, PercentageDiffVal, 0, SpringLayout.EAST, lblMeanClaimAmnt);
		drawPanel.add(PercentageDiffVal);
		
		JLabel lblPercentageDifference = new JLabel("Percentage Difference: ");
		sl_drawPanel.putConstraint(SpringLayout.WEST, lblPercentageDifference, 37, SpringLayout.WEST, drawPanel);
		sl_drawPanel.putConstraint(SpringLayout.WEST, PercentageDiffVal, 82, SpringLayout.EAST, lblPercentageDifference);
		sl_drawPanel.putConstraint(SpringLayout.NORTH, lblPercentageDifference, 5, SpringLayout.NORTH, btnExportTocsv);
		drawPanel.add(lblPercentageDifference);
		bpanel.setLayout(null);
		
		
		JLabel lblSelectCsvFile = new JLabel("Select data file: ");
		lblSelectCsvFile.setBounds(82, 106, 189, 25);
		lblSelectCsvFile.setBackground(Color.BLACK);
		bpanel.add(lblSelectCsvFile);
		
		
		/**Button that opens the file opener
		 * 
		 */
		JButton btnOpenFile = new JButton("Open File...");
		btnOpenFile.setBounds(277, 105, 121, 29);
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Handle open button action.
			    if (e.getSource() == btnOpenFile) {
			        int returnVal = openFileChooser.showOpenDialog(bpanel);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            dataFile = openFileChooser.getSelectedFile();
			            filePath.setText(dataFile.getPath());
			            if(dataFile.getName().endsWith(".csv") || dataFile.getName().contains(".xl")) {
			            		dataLoadedCorrectly = true;
			            }
			        } else {
			        		
			        }
			   } 
					
				
			}
		});
		bpanel.add(btnOpenFile);
		
		filePath = new JTextField();
		filePath.setBounds(404, 105, 172, 26);
		bpanel.add(filePath);
		filePath.setColumns(10);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(505, 378, 75, 29);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dataLoadedCorrectly == true) {
					nextButtonPressed = true;
					mainFrame.getContentPane().add(drawPanel);
					mainFrame.getContentPane().remove(bpanel);
					mainFrame.getContentPane().validate();
					mainFrame.getContentPane().repaint();
				}
			}
		});
		bpanel.add(btnNext);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(17, 378, 86, 29);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		bpanel.add(btnCancel);
		
		sampleSizeField = new JTextField();
		sampleSizeField.setBounds(354, 220, 130, 26);
		bpanel.add(sampleSizeField);
		sampleSizeField.setText(String.valueOf(SamplerMainClass.nTotalSamples));
		sampleSizeField.setColumns(10);
		
		numberOfStrataField = new JTextField();
		numberOfStrataField.setBounds(354, 252, 130, 26);
		bpanel.add(numberOfStrataField);
		numberOfStrataField.setText(String.valueOf(SamplerMainClass.nMajorStrata));
		numberOfStrataField.setColumns(10);
		
		topClaimsField = new JTextField();
		topClaimsField.setBounds(354, 284, 130, 26);
		bpanel.add(topClaimsField);
		topClaimsField.setText(String.valueOf(SamplerMainClass.nTopNSamples));
		topClaimsField.setColumns(10);
		
		JLabel lblZeroDollarClaims = new JLabel("Zero Dollar Claims: ");
		lblZeroDollarClaims.setBounds(-144, 288, 125, 16);
		bpanel.add(lblZeroDollarClaims);
		
		zeroDollarClaimsField = new JTextField();
		zeroDollarClaimsField.setBounds(354, 311, 130, 26);
		bpanel.add(zeroDollarClaimsField);
		zeroDollarClaimsField.setText(String.valueOf(SamplerMainClass.nZeroDollarSamples));
		zeroDollarClaimsField.setColumns(10);
		
		JLabel lblConfidenceLevel = new JLabel("Confidence Level: ");
		lblConfidenceLevel.setBounds(-134, 320, 115, 16);
		bpanel.add(lblConfidenceLevel);
		
		Confidence_LevelField = new JTextField();
		Confidence_LevelField.setBounds(354, 343, 130, 26);
		bpanel.add(Confidence_LevelField);
		Confidence_LevelField.setText(String.valueOf(SamplerMainClass.confLevel));
		Confidence_LevelField.setColumns(10);
		
		JLabel lblSamplesize = new JLabel("Sample Size: ");
		lblSamplesize.setBounds(205, 225, 82, 16);
		bpanel.add(lblSamplesize);
		
		JLabel lblNumberOfMain = new JLabel("Number of Main Strata: ");
		lblNumberOfMain.setBounds(138, 257, 149, 16);
		bpanel.add(lblNumberOfMain);
		
		JLabel lblNumberOfTop_1 = new JLabel("Number of Top Claims: ");
		lblNumberOfTop_1.setBounds(137, 289, 150, 16);
		bpanel.add(lblNumberOfTop_1);
		
		JLabel lblNumberOfZero = new JLabel("Number of Zero Dollar Claims: ");
		lblNumberOfZero.setBounds(91, 316, 196, 16);
		bpanel.add(lblNumberOfZero);
		
		JLabel lblConfidenceLevel_1 = new JLabel("Confidence Level: ");
		lblConfidenceLevel_1.setBounds(172, 348, 115, 16);
		bpanel.add(lblConfidenceLevel_1);
		
		JLabel lblSelectObservation = new JLabel("Select Observation Number Column:");
		lblSelectObservation.setBounds(55, 151, 228, 16);
		bpanel.add(lblSelectObservation);
		
		
		comboBox = new JComboBox();
		comboBox.setBounds(299, 147, 149, 27);
		bpanel.add(comboBox);
		
		JLabel lblSelectionPaid = new JLabel("Selection Paid Amoung Column:");
		lblSelectionPaid.setBounds(86, 187, 201, 16);
		bpanel.add(lblSelectionPaid);
		
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(299, 181, 149, 27);
		bpanel.add(comboBox_1);
		
		JButton btnNewButton = new JButton("Use Columns");
		btnNewButton.setBounds(461, 143, 115, 65);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		bpanel.add(btnNewButton);
		
		/*
		JLabel gLogoLoad = new JLabel("");
		sl_loadPanel.putConstraint(SpringLayout.NORTH, gLogoLoad, 123, SpringLayout.SOUTH, filePath);
		sl_loadPanel.putConstraint(SpringLayout.EAST, gLogoLoad, -173, SpringLayout.EAST, loadPanel);
		sl_loadPanel.putConstraint(SpringLayout.EAST, Confidence_LevelField, -6, SpringLayout.WEST, gLogoLoad);
		sl_loadPanel.putConstraint(SpringLayout.EAST, topClaimsField, -6, SpringLayout.WEST, gLogoLoad);
		sl_loadPanel.putConstraint(SpringLayout.EAST, numberOfStrataField, -6, SpringLayout.WEST, gLogoLoad);
		sl_loadPanel.putConstraint(SpringLayout.EAST, sampleSizeField, -6, SpringLayout.WEST, gLogoLoad);
		sl_loadPanel.putConstraint(SpringLayout.EAST, zeroDollarClaimsField, -6, SpringLayout.WEST, gLogoLoad);
		File image2 = new File("gallagher_wtag_stackedlarge-3d-1.png");
		System.out.println(image2.exists());
		gLogoLoad.setIcon(new ImageIcon(image2.getAbsolutePath()));
		loadPanel.add(gLogoLoad);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("/Users/joshrosenberg/eclipse-workspace/Sampling.Tool/GallagherBackgroung.PNG"));

		loadPanel.add(lblNewLabel);
		*/
		
	}

	public JTextField getFileNameInput() {
		return fileNameInput;
	}

	public void setFileNameInput(JTextField fileNameInput) {
		this.fileNameInput = fileNameInput;
	}

	public JTextField getConfidence_LevelField() {
		return Confidence_LevelField;
	}

	public void setConfidence_LevelField(JTextField confidence_LevelField) {
		Confidence_LevelField = confidence_LevelField;
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