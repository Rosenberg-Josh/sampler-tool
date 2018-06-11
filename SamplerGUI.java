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

import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.SystemColor;
import javax.swing.AbstractAction;
import javax.swing.Action;


public class SamplerGUI {

	public JFrame frmSampleCreator;
	private JTextField txtClientName;
	private JTextField txtClientLocation;
	private JTextField textField;
	private final Action action = new SwingAction();
	private final JFileChooser openFileChooser;


//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SamplerGUI window = new SamplerGUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application UI.
	 */
	public SamplerGUI() {
		initialize();
		
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("c:\\temp"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("CSV file","csv"));
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		/*  Create main frame */
		frmSampleCreator = new JFrame();
		frmSampleCreator.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			}
		});
		frmSampleCreator.setTitle("Sample Creator");
		frmSampleCreator.setBounds(100, 100, 900, 600);
		frmSampleCreator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSampleCreator.getContentPane().setLayout(null);
		
		/* Create master panel with card layout */
		CardLayout cardLayout = new CardLayout();
		JPanel pnlMaster = new JPanel();
		pnlMaster.setBounds(0, 45, 882, 508);
		frmSampleCreator.getContentPane().add(pnlMaster);
		pnlMaster.setLayout(cardLayout);
		
		/* Create individual menu item screens */
		/* Load Screen Panel */
		JPanel pnlLoadScreen = new JPanel();
		pnlLoadScreen.setBackground(SystemColor.menu);
		pnlLoadScreen.setBounds(0, 45, 882, 508);
		pnlMaster.add(pnlLoadScreen, "LoadScreen");
		pnlLoadScreen.setLayout(null);
		setupPnlLoadScreen(pnlLoadScreen);
		
		JButton btnOpenFile = new JButton("Open File...");
		
		btnOpenFile.setAction(action);
		btnOpenFile.setBounds(273, 51, 117, 29);
		pnlLoadScreen.add(btnOpenFile);
		
		textField = new JTextField();
		textField.setBounds(402, 51, 280, 26);
		pnlLoadScreen.add(textField);
		textField.setColumns(10);
		
		/* Client Info Screen Panel */
		JPanel pnlClientInfoScreen = new JPanel();
		pnlClientInfoScreen.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		pnlClientInfoScreen.setBounds(0, 45, 882, 508);
		pnlMaster.add(pnlClientInfoScreen, "ClientInfoScreen");
		pnlClientInfoScreen.setLayout(null);
		setupPnlClientInfoScreen(pnlClientInfoScreen);
		
		/* Create main menu bar and load */
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 882, 46);
		frmSampleCreator.getContentPane().add(menuBar);
		setupMenuBar(menuBar, pnlMaster, cardLayout);
	}

	/**
	 * Helper method to set up the main menu items
	 */
	private void setupMenuBar(JMenuBar menuBar, JPanel pnlMaster, CardLayout cardLayout) {

		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.setHorizontalAlignment(SwingConstants.CENTER);
		mntmLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				cardLayout.show(pnlMaster, "LoadScreen");
			}
		});
		menuBar.add(mntmLoad);
		
		JMenuItem mntmClientInfo = new JMenuItem("Client Info");
		mntmClientInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				cardLayout.show(pnlMaster, "ClientInfoScreen");
			}
		});
		menuBar.add(mntmClientInfo);
		
		JMenuItem mntmSampleSpec = new JMenuItem("Sample Spec");
		menuBar.add(mntmSampleSpec);
		
		JMenuItem mntmDrawSample = new JMenuItem("Draw Sample");
		menuBar.add(mntmDrawSample);
		
		JMenuItem mntmOutput = new JMenuItem("Output");
		menuBar.add(mntmOutput);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
	//			if pnlLoadScreen.get
			}
		});
		menuBar.add(mntmExit);
	}
	
	/** 
	 * Helper method to set up the Load Screen
	 */
	private void setupPnlLoadScreen(JPanel pnlLoadScreen) {
		
		JLabel lblSelectExistingSample = new JLabel("Select an existing sample to load from drop-down menu, or click â€œNew Sampleâ€? button to create a new sample:");
		lblSelectExistingSample.setVerticalAlignment(SwingConstants.TOP);
		lblSelectExistingSample.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectExistingSample.setBounds(108, 56, 153, 25);
		pnlLoadScreen.add(lblSelectExistingSample);
		
		
		JLabel lblOr = new JLabel("OR");
		lblOr.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOr.setHorizontalAlignment(SwingConstants.CENTER);
		lblOr.setBounds(402, 179, 27, 22);
		pnlLoadScreen.add(lblOr);
		
		JButton btnNewButton = new JButton("Create New Sample");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Define action here...
			}
		});
		btnNewButton.setBounds(304, 237, 246, 25);
		pnlLoadScreen.add(btnNewButton);
	}
	
	/** 
	 * Helper method to set up the Client Info Screen
	 */
	private void setupPnlClientInfoScreen(JPanel pnlClientInfoScreen) {
		JLabel lblClientName = new JLabel("Client Name");
		lblClientName.setForeground(UIManager.getColor("Label.foreground"));
		lblClientName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblClientName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientName.setBounds(12, 89, 109, 16);
		pnlClientInfoScreen.add(lblClientName);
		
		txtClientName = new JTextField();
		txtClientName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtClientName.setBounds(149, 89, 337, 22);
		pnlClientInfoScreen.add(txtClientName);
		txtClientName.setColumns(10);
		
		JLabel lblBenefitPointId = new JLabel("BenefitPoint ID");
		lblBenefitPointId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBenefitPointId.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblBenefitPointId.setBounds(12, 118, 109, 16);
		pnlClientInfoScreen.add(lblBenefitPointId);
		
		JLabel lblClientLocation = new JLabel("Client Location");
		lblClientLocation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientLocation.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblClientLocation.setBounds(12, 147, 109, 16);
		pnlClientInfoScreen.add(lblClientLocation);
		
		txtClientLocation = new JTextField();
		txtClientLocation.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		txtClientLocation.setColumns(10);
		txtClientLocation.setBounds(149, 149, 337, 22);
		pnlClientInfoScreen.add(txtClientLocation);
		
		JLabel lblClaimType = new JLabel("Claim Type");
		lblClaimType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClaimType.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		lblClaimType.setBounds(12, 192, 109, 21);
		pnlClientInfoScreen.add(lblClaimType);
		
		JComboBox cbxClaimType = new JComboBox();
		cbxClaimType.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		cbxClaimType.setEditable(true);
		cbxClaimType.setBounds(149, 194, 337, 22);
		pnlClientInfoScreen.add(cbxClaimType);
		
		JButton btnClientInfoNext = new JButton("Next");
		btnClientInfoNext.setBounds(389, 289, 97, 25);
		pnlClientInfoScreen.add(btnClientInfoNext);
		
		JButton btnClientInfoBack = new JButton("Back");
		btnClientInfoBack.setBounds(260, 289, 97, 25);
		pnlClientInfoScreen.add(btnClientInfoBack);		
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}


