package SamplerPackage;
import java.util.Scanner;
import SamplerPackage.SamplerGUI2;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.lang.Math;
import java.sql.*;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;


class Stratum implements Comparable<Stratum> {
	/** Implements a single Stratum.  The stratum number is represented by its location in an ArrayList
	 *  Fields are:
	 *  	upperBound (not inclusive)
	 *  	lowerBound (inclusive)
	 *  	stratumSampleSize
	 *  	stratumTotalAmount
	 *  	stratumNumClaims  
	 *		t   //the number of pieces the original trial stratum was split into -- equals 1 if not split
	 *		SRF  //the "square root of frequency" for this stratum
	 *		CSRF  //the cumulative square root of frequency for this stratum, i.e., the sum of the SRFs for this and all the lower strata
	 *		firstClaimPos  //the position in the claimsData arraylist of the first claim in this stratum
	 *		lastClaimPos  //the position in the claimsData arraylist of the last claim in this stratum
	 */
	
	protected double upperBound;
	protected double lowerBound;
	protected int stratumSampleSize;
	protected double stratumTotalAmount;
	protected int stratumNumClaims;
	protected int t;
	protected double SRF;
	protected double CSRF;
	protected double stratumMean;
	protected double stratumSD;
	protected int firstClaimPos;
	protected int lastClaimPos;
	
	/* Constructor */
	public Stratum() {
		upperBound = 0;
		lowerBound = 0;
		stratumSampleSize = 0;
		stratumTotalAmount = 0;
		stratumNumClaims = 0;
		t = 1;
		SRF = 0.0;
		CSRF = 0.0;
		stratumMean = 0.0;
		stratumSD = 0.0;
		firstClaimPos = 0;
		lastClaimPos = 0;
	}
	
	/* Constructor */
	public Stratum(double u, double l, int s, double a, int n) {
		upperBound = u;
		lowerBound = l;
		stratumSampleSize = s;
		stratumTotalAmount = a;
		stratumNumClaims = n;
		t = 1;
		SRF = 0.0;
		CSRF = 0.0;
		stratumMean = 0.0;
		stratumSD = 0.0;
		firstClaimPos = 0;
		lastClaimPos = 0;
	}

	/* Constructor */
	public Stratum (Stratum otherStratum){
		this.upperBound = otherStratum.upperBound;
		this.lowerBound = otherStratum.lowerBound;
		this.stratumSampleSize = otherStratum.stratumSampleSize;
		this.stratumTotalAmount = otherStratum.stratumTotalAmount;
		this.stratumNumClaims = otherStratum.stratumNumClaims;
		this.t = otherStratum.t;
		this.SRF = otherStratum.SRF;
		this.CSRF = otherStratum.CSRF;
		this.stratumMean = otherStratum.stratumMean;
		this.stratumSD = otherStratum.stratumSD;
		this.firstClaimPos = otherStratum.firstClaimPos;
		this.lastClaimPos = otherStratum.lastClaimPos;
		}

	
	/* Getters and Setters */
	public void setUpperBound(double u) {
		upperBound = u;
	}
	public double getUpperBound() {
		return upperBound;
	}
	public void setLowerBound(double l) {
		lowerBound = l;
	}
	public double getLowerBound() {
		return lowerBound;
	}
	public void setStratumSampleSize(int s) {
		stratumSampleSize = s;
	}
	public int getStratumSampleSize() {
		return stratumSampleSize;
	}
	public void setStratumTotalAmount(double a) {
		stratumTotalAmount = a;
	}
	public double getStratumTotalAmount() {
		return stratumTotalAmount;
	}
	public void setStratumNumClaims(int n) {
		stratumNumClaims = n;
	}
	public int getStratumNumClaims() {
		return stratumNumClaims;
	}
	public void setT(int tValue) {
		t = tValue;
	}
	public int getT() {
		return t;
	}
	public void setSRF(double s) {
		SRF = s;
	}
	public double getSRF() {
		return SRF;
	}
	public void setCSRF(double c) {
		CSRF = c;
	}
	public double getCSRF() {
		return CSRF;
	}
	public void setStratumMean(double mean) {
		stratumMean = mean;
	}
	public double getStratumMean() {
		return stratumMean;
	}
	public void setStratumSD(double sd) {
		stratumSD = sd;
	}
	public double getStratumSD() {
		return stratumSD;
	}
	public void setFirstClaimPos(int p1) {
		firstClaimPos = p1;
	}
	public int getFirstClaimPos() {
		return firstClaimPos;
	}
	public void setLastClaimPos(int p2) {
		lastClaimPos = p2;
	}
	public int getLastClaimPos() {
		return lastClaimPos;
	}
	
	/* Comparison */
	public int compareTo(Stratum otherOne) {  //THIS DOESN'T DO ANYTHING RIGHT NOW
		int intVal = 0;
		/*
		double actVal = this.amount - otherOne.getAmount();
		if (actVal <0) {
			intVal = -1;
		}
		else {
			if (actVal > 0) {
				intVal = 1;
			}
			else {
				intVal = 0;
			}
		} */
		return intVal;
	}	
}

class DataItem implements Comparable<DataItem> {
	/** This class implements a row of data that is used by the Sampler program.
	 *  This is a subset of the full data record in the claims file, but contains all the 
	 *  fields needed by the Sampler program.  These are:
	 *  	obsNum: The observation number, or line number, of the claim in the master data file
	 *  	claimID:  The unique identifier for a claim
	 *  	lineNum:  The line number of the original claim this data represents.
	 *  			claimID & lineNum together uniquely identify a "claim" in the master file and in our subset
	 *  	amount:  The amount of the claimID&lineNum.
	 *  It also includes fields assigned during the sampling process:
	 *  	stratumNum:  The number stratum the claim is assigned to
	 */
	
	protected int obsNum;
	protected String claimID;
	protected String lineNum;
	protected double amount;
	protected int stratumNum;
	
	/* Constructor */
	public DataItem() {
		obsNum = 0;
		claimID = null;
		lineNum = null;
		amount = 0.0;
		stratumNum = -1;
	}
	/* Constructor */
	public DataItem(int o, String c, String l, double a) {
		obsNum = o;
		claimID = c;
		lineNum = l;
		amount = a;
		stratumNum = -1;
	}
	/* Constructor */
	public DataItem(DataItem d) {
		this.obsNum = d.obsNum;
		this.claimID = d.claimID;
		this.lineNum = d.lineNum;
		this.amount = d.amount;
		this.stratumNum = d.stratumNum;
	}
	/* Function to set obsNum */
	public void setObsNum(int o) {
		obsNum = o;
	}
	/* Function to get obsNum */
	public int getObsNum() {
		return obsNum;
	}
	/* Function to set claimID */
	public void setClaimID(String c) {
		claimID = c;
	}
	/* Function to get claimID */
	public String getClaimID() {
		return claimID;
	}
	/* Function to set lineNum */
	public void setLineNum(String l) {
		lineNum = l;
	}
	/* Function to get lineNum */
	public String getLineNum() {
		return lineNum;
	}
	/* Function to set amount */
	public void setAmount(double a) {
		amount = a;
	}
	/* Function to get amount */
	public double getAmount() {
		return amount;
	}
	/* Function to set stratumNum */
	public void setStratumNum(int a) {
		stratumNum = a;
	}
	/* Function to get stratumNum */
	public int getStratumNum() {
		return stratumNum;
	}
	/* Comparison -- based on the amount field -- allows the arraylist to be sorted by amount */
	public int compareTo(DataItem otherOne) {
		int intVal;
		double actVal = this.amount - otherOne.getAmount();
		if (actVal <0) {
			intVal = -1;
		}
		else {
			if (actVal > 0) {
				intVal = 1;
			}
			else {
				intVal = 0;
			}
		}
		return intVal;
	}
}

class SampleData {

	public static Connection setUpDBConnection(String dbName) throws SQLException {
		/** Sets up the database connection for the Sampler internal
		 *  database.
		 *  Returns the connection to the DB.
		 */
		
		// define the driver to use 
		 String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		// define the Derby connection URL to use 
		 String connectionURL = "jdbc:derby:" + dbName + ";create=true";
		// the Sampler database connection -- return value
		Connection conn = null;
		 
		//  Beginning of Primary DB access section
		//   ## BOOT DATABASE SECTION ##
        // Create (if needed) and connect to the database.
        // The driver is loaded automatically.
		conn = DriverManager.getConnection(connectionURL);		 
		System.out.println("Connected to database " + dbName);
		 
		 return conn;
	}

	public static int makeNewDB(Connection conn) throws SQLException {
		/** Creates the table structure for the Sampler DB.
		 *  It assumes that the DB has been checked for existing tables and 
		 *  that the UI is being managed elsewhere.  This just creates the tables.
		 */
		/* NOTE - CHANGE NEEDED:  The current definition of sampleID as an int means there can only be 65K sampleDefs.  
		 * Probably want to change this.
		 */
		
		String sqlCommand = "";
		Statement s;
		int status = 0;  // status flag: 1 = ok;
		
		s = conn.createStatement();
		ResultSet rs;
		
		s.execute("Drop table tblDrawnSample");
		s.execute("Drop table tblStrataDefs");
		s.execute("Drop table tblSampleDefs");
		s.execute("Drop table tblClients");
		s.execute("Drop TABLE tblUsers");
		sqlCommand = "CREATE TABLE tblUsers ("
				+ "userId varchar(50) not null,"
				+ "user_email varchar(100),"
				+ "user_name varchar(100),"
				+ "PRIMARY KEY (userId))";
		s.execute(sqlCommand);
		System.out.println("tblUsers created");
		sqlCommand = "INSERT INTO tblUsers VALUES ('1001', 'noemail', 'User Name')";
		s.execute(sqlCommand);
		System.out.println("added new user");
		sqlCommand = "CREATE TABLE tblClients ("
				+ "BenefitPointID varchar(50) not null, "
				+ "client_name varchar(100), "
				+ "client_location varchar(100), "
				+ "PRIMARY KEY(BenefitPointID))";
		s.execute(sqlCommand);
		System.out.println("tblClients created");	
		sqlCommand = "INSERT INTO tblClients VALUES ('AB1001', 'COMPANY1', 'PRINCETON, NJ')";
		s.execute(sqlCommand);
		sqlCommand = "INSERT INTO tblClients VALUES ('CD1002', 'COMPANY2', 'BOSTON, MA')";
		s.execute(sqlCommand);
		System.out.println("added 2 new clients");
		sqlCommand = "CREATE TABLE tblSampleDefs ("
				+ "sampleDefID int not null,"
				+ "sampleDescripton varchar(100),"
				+ "clientBPID varchar(50)," 
				+ "claimFileLoc varchar(200),"
				+ "clientName varchar(100),"
				+ "claimType varchar(50),"
				+ "clientLocation varchar(100),"
				+ "startDate date,"
				+ "endDate date,"
				+ "sampleDefCreateDate timestamp,"
				+ "sampleDefModDate timestamp,"
				+ "sampleSize int, "
				+ "PRIMARY KEY (sampleDefID),"
				+ "FOREIGN KEY (clientBPID) REFERENCES tblClients(BenefitPointID))";
		s.execute(sqlCommand);
		System.out.println("tblSampleDefs created");	
		sqlCommand = "INSERT INTO tblSampleDefs VALUES ("
				+ "1, 'SampleDef 1', 'AB1001',"
				+ "'c:\\claimfile.csv',"
				+ "'Client 1',"
				+ "'MEDICAL',"
				+ "'PRINCETON, NJ',"
				+ "'2016-01-01',"
				+ "'2016-12-31',"
				+ "'2017-10-29 03:23:34.234',"
				+ "'2017-10-29 03:23:34.234',"
				+ "225)";
		s.execute(sqlCommand);
		System.out.println("added 1 new sampleDef");
//		sqlCommand = "SELECT * FROM tblSampleDefs";
//		rs = s.executeQuery(sqlCommand);
//		while (rs.next()) {
//			System.out.println(rs.getLong(1));
//			System.out.println(rs.getString(2));
//			System.out.println(rs.getString(3));
//			System.out.println(rs.getString(4));
//			System.out.println(rs.getString(5));
//			System.out.println(rs.getString(6));
//			System.out.println(rs.getString(7));
//			System.out.println(rs.getDate(8));
//			System.out.println(rs.getDate(9));
//			System.out.println(rs.getTimestamp(10));
//			System.out.println(rs.getTimestamp(11));
//			System.out.println(rs.getInt(12));
//		}
		sqlCommand = "CREATE TABLE tblStrataDefs ("
				+ "strataDefID int not null,"
				+ "sampleDefID int,"
				+ "stratumNum int," 
				+ "lowerBound double,"
				+ "upperBound double,"
				+ "stratumNSamples int,"
				+ "PRIMARY KEY (strataDefID),"
				+ "FOREIGN KEY (sampleDefID) REFERENCES tblSampleDefs(sampleDefID))";
		s.execute(sqlCommand);
		System.out.println("tblStrataDefs created");	
		sqlCommand = "INSERT INTO tblStrataDefs VALUES (1,1,0,-1.0,1.0,-1)";
		s.execute(sqlCommand);
		sqlCommand = "INSERT INTO tblStrataDefs VALUES (2,1,1,1.0,50.0,-1)";
		s.execute(sqlCommand);
		sqlCommand = "INSERT INTO tblStrataDefs VALUES (3,1,2,50.0,150.0,-1)";
		s.execute(sqlCommand);
		System.out.println("added 3 new strataDef records");
		sqlCommand = "CREATE TABLE tblDrawnSample ("
				+ "drawnSampleID int not null,"
				+ "sampleDefID int,"
				+ "strataDefID int,"
				+ "drawDate timestamp," 
				+ "sampleObsNum int,"
				+ "claimfileObsNum bigint,"
				+ "claimAmount double,"
				+ "PRIMARY KEY (drawnSampleID),"
				+ "FOREIGN KEY (sampleDefID) REFERENCES tblSampleDefs(sampleDefID),"
				+ "FOREIGN KEY (strataDefID) REFERENCES tblStrataDefs(strataDefID))";
		s.execute(sqlCommand);
		System.out.println("tblDrawnSample created");	
		sqlCommand = "INSERT INTO tblDrawnSample VALUES (1,1,1,'2017-10-29 03:23:34.234',1,15,0.15)";
		s.execute(sqlCommand);
		sqlCommand = "INSERT INTO tblDrawnSample VALUES (2,1,1,'2017-10-29 03:23:34.234',2,126,-.25)";
		s.execute(sqlCommand);
		sqlCommand = "INSERT INTO tblDrawnSample VALUES (3,1,2,'2017-10-29 03:23:34.234',3,1054,-.25)";
		s.execute(sqlCommand);
		System.out.println("added 3 new strataDef records");
			
		status = 1; // Set flag to OK
		return status;	
	}
	
	
	public static int loadClaimsData(ArrayList<DataItem> dataList, String dataFileName) throws FileNotFoundException {
		
		int nLoaded = -1;  //-1 = Error, nothing loaded; positive n = number of claims loaded
		
		/* Open the data file */
		File inputFile = new File(dataFileName);
		Scanner in = new Scanner(inputFile);
		System.out.println(in);
		
		/* Read in the data file */
		//First the header line -- throw this away
		if (in.hasNext()) { 
			String inputLine = "";
			inputLine = in.nextLine();
		}
		//Then the rest of the file -- load into the data array
		nLoaded = 0;
		while (in.hasNext()) {
			String inputLine = "";
			String junk = "";
			inputLine = in.nextLine();
			Scanner lineToParse = new Scanner(inputLine).useDelimiter(",");
			int obsNum = lineToParse.nextInt();
			//int obsNum = nLoaded + 1;
			String claimID = lineToParse.next();
			String lineNum = lineToParse.next();

			junk = lineToParse.next();  //We don't use the date field
//			junk = lineToParse.next();  //We don't use the time field
			double amount = lineToParse.nextDouble();
			if (amount >= 0) {  //Don't load negative claims --> THIS NEEDS TO BE BETTER ADDRESSED
				DataItem newClaim = new DataItem(obsNum, claimID, lineNum, amount);
				dataList.add(newClaim);
				nLoaded++;				
			}
			lineToParse.close();
		}
		
		in.close();		
		return nLoaded;	
	}
}

public class SamplerMainClass {

/**
 * Tool to generate and manage statistically valid, optimized stratified random samples of health care claims.
 * Based on the approach described in the paper "_____"
 *  
 * @param args
 * @throws FileNotFoundException
 * @throws SQLException
 * @throws InterruptedException 
 */
	
	public static ArrayList<DataItem> sampleClaims;
	
	
	public static void main(String[] args) throws FileNotFoundException, SQLException, InterruptedException {
		
		/* Global input variables */
		int nTotalSamples = 225;  //the total number of samples
		int minSamplesPerStratum = 6;  //min samples per stratum (if possible)
		int nZeroDollarSamples = 10;  //total number of zero dollar claim samples
		int nTopNSamples = 25;  //the number of Top N samples, which will be 100% audited
		int nStratSamples = nTotalSamples - nZeroDollarSamples - nTopNSamples; //the number of samples in the stratified sample group
		int nMajorStrata = 20; //Number of major strata
		int nTrialStrata = 100;  //Number of trial strata to start with

		
		/* Global data structures */
		ArrayList<DataItem> claimsData = new ArrayList<>();
//		ResultSet rsSampleDefs;
		ArrayList<Stratum> trialStrata = new ArrayList<>();  // sortable on amount.  Stratum has upperBound (not inclusive), 
					// lowerBound (inclusive), stratumSampleSize, stratumTotalAmount, stratumNumClaims, 
		ArrayList<Stratum> majorStrata = new ArrayList<>();  // holds the final Major Strata
		sampleClaims = new ArrayList<>();  //holds the claims sample that is drawn
		SamplerGUI2 currWindow = new SamplerGUI2();
//		/* Database connection */
//		Connection conn = null;
//				
//		/* Open Sampler database connection */
//		conn = SampleData.setUpDBConnection(dbName);
//		
//		int junk = SampleData.makeNewDB(conn);
//		System.out.println("Done");

		/* Invoke the UI home page */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					currWindow.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		
		
		while(currWindow.getDataFile() == null) {
			Thread.sleep(1500);
			
		}
		String dataFileName = currWindow.getDataFile().getAbsolutePath();
		
		String dbName = "SamplerDB";  // name of the internal Sampler DB
		int nClaimsInDataFile = 0;
				
		/* Read in summary claims data */
		nClaimsInDataFile = SampleData.loadClaimsData(claimsData,dataFileName);
		System.out.println("Read in "+nClaimsInDataFile+" claims.  Sorting...");
		double tmptotal = 0;
		for (int i=0; i<nClaimsInDataFile; i++){
			tmptotal = tmptotal+claimsData.get(i).getAmount();
		}
		System.out.format("Total claims amount: %f",tmptotal);
		System.out.println("");
		Collections.sort(claimsData);
		System.out.println("Sort done.  Here are the first 10 records...");
		System.out.format("Total claims amount: %f",tmptotal);
		System.out.println("");
//		for (int i=0; i<10; i++) {
//			System.out.println(claimsData.get(i).getObsNum()+", "+claimsData.get(i).getClaimID()+", "+claimsData.get(i).getLineNum()+", "+claimsData.get(i).getAmount());			
//		}
		
//		/* Test the claimsData ArrayList */
//		DataItem data3 = new DataItem(3,"300","003",5.00);
//		data3.setStratumNum(2);
//		claimsData.add(data3);
//		DataItem data1 = new DataItem(1,"100","001",15.00);
//		data1.setStratumNum(3);
//		claimsData.add(data1);
//		DataItem data2 = new DataItem(2,"200","002",10.00);
//		data2.setStratumNum(4);
//		claimsData.add(data2);
//		for (int i=0; i<claimsData.size(); i++) {
//			System.out.println(claimsData.get(i).getObsNum()+", "+claimsData.get(i).getClaimID()+", "+claimsData.get(i).getLineNum()+", "+claimsData.get(i).getAmount()+", "+claimsData.get(i).getStratumNum());			
//		}
//		System.out.println("");
//		Collections.sort(claimsData);
//		for (int i=0; i<claimsData.size(); i++) {
//			System.out.println(claimsData.get(i).getObsNum()+", "+claimsData.get(i).getClaimID()+", "+claimsData.get(i).getLineNum()+", "+claimsData.get(i).getAmount()+", "+claimsData.get(i).getStratumNum());			
//		}
		
		/* Determine the strata */
		CreateSample.defineStrata(claimsData, trialStrata, majorStrata, nTrialStrata, nZeroDollarSamples, nTopNSamples, nTotalSamples, nMajorStrata, minSamplesPerStratum);
		
		/* Draw and test the sample */
		CreateSample.drawSample(claimsData, sampleClaims, majorStrata, nZeroDollarSamples, nTopNSamples, nStratSamples, nTotalSamples, nMajorStrata);
		
		
		/*  Clean up  */
//		conn.close();

		
	}

}
