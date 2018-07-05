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

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


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

	@Override
	public int compareTo(Stratum arg0) {
		// TODO Auto-generated method stub
		return 0;
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
	protected double amount;
	protected int stratumNum;
	
	/* Constructor */
	public DataItem() {
		obsNum = 0;
		amount = 0.0;
		stratumNum = -1;
	}
	/* Constructor */
	public DataItem(int o, double a) {
		obsNum = o;
		amount = a;
		stratumNum = -1;
	}
	/* Constructor */
	public DataItem(DataItem d) {
		this.obsNum = d.obsNum;
		this.amount = d.amount;
		this.stratumNum = d.stratumNum;
	}
	/* Constructor */
	public DataItem(int amount) {
		obsNum = 0;
		this.amount = amount;
		stratumNum = -1;
	}
	/* Function to set obsNum */
	public void setObsNum(int o) {
		obsNum = o;
	}
	/* Function to get obsNum */
	public int getObsNum() {
		return obsNum;
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
		 String connectionURL = "jdbc:sqlserver:" + dbName + ";ReadOnly=true";
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

	
	
	public static ArrayList<DataItem> readData(Connection conn, String client_id, String client_name) throws SQLException{

		String sqlCommand = "";
		Statement s;
		int status = 0;  // status flag: 1 = ok;

		s = conn.createStatement();
		ResultSet rs;
		sqlCommand = "Select client_id" + 
				"                ,client_name" + 
				"from odwrs_test.dbo.tbl_clients" + 
				"order by left(client_name,charindex('(',client_name))" + 
				"                ,client_id";

		s.execute(sqlCommand);
		sqlCommand = "Select top 999 Claim_Number" + 
				"                                ,Line_Number" + 
				"                                ,paid_amount" + 
				"                                ,rec_id" + 
				"                from odwrs_test.dbo.tbl_claims_66114";
		System.out.println("");
		
		
		
		
		
		ArrayList<DataItem> result = new ArrayList<DataItem>();
		return result;
		
	}
	
	public static int loadClaimsData(ArrayList<DataItem> dataList, String dataFileName) throws IOException {
		
		if(dataFileName.contains(".csv")) {
			int nLoaded = -1;  //-1 = Error, nothing loaded; positive n = number of claims loaded
			/* Open the data file */
			File inputFile = new File(dataFileName);
			Scanner in = new Scanner(inputFile);
			
			/* Read in the data file */
			if (in.hasNext()) { 
				int obsIndex = 999; //Used to refrece observation number from data
				int amntIndex = 999; //Used to reference amnt paid from data
				int obsNum = -1;
				double amntPaid = -1;
				String headLine = "";
				headLine = in.nextLine(); //Header Line
				Scanner headToParse = new Scanner(headLine).useDelimiter(","); //split header into iterative scanner
				int index = 0; //Starting Index
				while(headToParse.hasNext()) { //Iterate though entire header
					String currElement = headToParse.next().toLowerCase();
					
					if(currElement.equals("obs") || currElement.equals("obsnum") || currElement.equals("oberservationnum") || 
							currElement.equals("obsnumber") || currElement.contains("obs")) { //Find observation number, this should be updated with more criteria
						obsIndex = index;
					
					}else if(currElement.equals("amtpaid") || currElement.equals("amntpaid") || (currElement.contains("am") 
							&& currElement.contains("paid")) || (currElement.contains("am") && currElement.contains("pd"))  || (currElement.contains("am") && currElement.contains("pmt"))) { //Find paid amount, this REALLY should be updated with more criteria
						amntIndex = index;
					}
					
					if(obsIndex != 999 && amntIndex != 999) { //Break if both indexes have been set
						break;
					}
					index++;
			}
			if(obsIndex == 999 || amntIndex == 999) {
				//ERROR, possible to make user specify where field, but will adress in later version
			}
			
			//Then the rest of the file -- load into the data array
			nLoaded = 0;
			
			while (in.hasNext()) { //Iterate though each claim line
				String inputLine = in.nextLine();
				Scanner lineToParse = new Scanner(inputLine).useDelimiter(",");
				int currIndex = 0;
				boolean obsLoaded = false;
				boolean amntLoaded = false;
				while(lineToParse.hasNext()) {
					if(currIndex == obsIndex) {
						obsNum = lineToParse.nextInt();
						obsLoaded = true;
					}else if (currIndex == amntIndex) {
						amntPaid = lineToParse.nextDouble();
						amntLoaded = true;
					}else {
						lineToParse.next();
					}
					currIndex++;
					if(amntLoaded && obsLoaded) {
						break;
					}
				}
				if(amntLoaded == false || amntLoaded == false) {
					//error
				}
	
	
				if (amntPaid >= 0) {  //Don't load negative claims --> THIS NEEDS TO BE BETTER ADDRESSED
					DataItem newClaim = new DataItem(obsNum, amntPaid);
					dataList.add(newClaim);
					nLoaded++;				
				}
				lineToParse.close();
			}
			}
			in.close();		
			return nLoaded;	
			
		}else if(dataFileName.contains(".xlsm") || dataFileName.contains(".xls") || dataFileName.contains(".xlsx") || dataFileName.contains(".xlst")) {
			int nLoaded = -1;  //-1 = Error, nothing loaded; positive n = number of claims loaded
			File file = new File(dataFileName);
			FileInputStream xlsxFile = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
			XSSFSheet inputSheet = workbook.getSheetAt(0);
			boolean headersLoaded = false;
			int obsIndex = -1;
			int amntIndex = -1;
			int dataStartRow = -1;
			for(int i = 0; headersLoaded == false; i++) {
				XSSFRow row = inputSheet.getRow(i);
				System.out.println(row.getCell(0).getStringCellValue());
				for(int j = 0; row.getCell(j).getStringCellValue() != null; j++) {
					String currElement =  row.getCell(j).getStringCellValue().toLowerCase();
					System.out.println(currElement);
					if(currElement.equals("obs") || currElement.equals("obsnum") || currElement.equals("oberservationnum") || 
							currElement.equals("obsnumber") || currElement.contains("obs")) { //Find observation number, this should be updated with more criteria
						obsIndex = j;
					
					}else if(currElement.equals("amtpaid") || currElement.equals("amntpaid") || (currElement.contains("am") 
							&& currElement.contains("paid")) || (currElement.contains("am") && currElement.contains("pd"))  || (currElement.contains("am") && currElement.contains("pmt"))) { //Find paid amount, this REALLY should be updated with more criteria
						amntIndex = j;
					}
					if(obsIndex != -1 && amntIndex != -1) {
						headersLoaded = true;
						dataStartRow = i + 1;
						break;
					}
				}
			}
			nLoaded = 0;
			System.out.println(obsIndex);
			System.out.println(amntIndex);
			System.out.println(inputSheet.getLastRowNum());
			for(int i = dataStartRow; i <= inputSheet.getLastRowNum(); i++) {
				XSSFRow row = inputSheet.getRow(i);
				int obsNum = (int)row.getCell(obsIndex).getNumericCellValue();
				double amntPaid = row.getCell(amntIndex).getNumericCellValue();
				if(amntPaid >= 0) {
					DataItem newClaim = new DataItem(obsNum, amntPaid);
					dataList.add(newClaim);
					nLoaded++;
				}
			}
			System.out.println(nLoaded);
			return nLoaded;
		}else {
			System.out.println("File error");
			return -1;
		}
	}
	
	public static int loadClaimsDataFromSas(ArrayList<DataItem> dataList, String dataFileName) throws FileNotFoundException {
		
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
			inputLine = in.nextLine();
			Scanner lineToParse = new Scanner(inputLine).useDelimiter(",");
			int amount = lineToParse.nextInt();
			int freq = lineToParse.nextInt();
			if (amount >= 0) {  //Don't load negative claims --> THIS NEEDS TO BE BETTER ADDRESSED
				while(freq > 0) {
					DataItem newClaim = new DataItem(amount);
					dataList.add(newClaim);
					nLoaded++;
					freq--;
				}
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
 * Based on the approach described in the paper "ASSESSMENT OF AUDIT SAMPLING AND EXTRAPOLATION PROCESS" 
 *  
 * @param args
 * @throws FileNotFoundException
 * @throws SQLException
 * @throws InterruptedException 
 */
	
	/* Claims to be accessed outside of class*/
	public static ArrayList<DataItem> sampleClaims;
	
	public static ArrayList<Stratum> finStrata;
	
	public static ArrayList<DataItem> portClaimsData;
	
	/*Boolean to check weather sampling is complete*/
	public static boolean dataProcessed = false;
	
	public static int nTotalSamples = 225; //the total number of samples
	
	public static int minSamplesPerStratum = 9;  //min samples per stratum (if possible)
	public static int nZeroDollarSamples = 5;  //total number of zero dollar claim samples
	public static int nTopNSamples = 25;  //the number of Top N samples, which will be 100% audited
	public static int nStratSamples = nTotalSamples - nZeroDollarSamples - nTopNSamples; //the number of samples in the stratified sample group
	public static int nMajorStrata = 20; //Number of major strata
	public static int nTrialStrata = 100;  //Number of trial strata to start with
	public static double confLevel = 99.0;
	
	public static void main(String[] args) throws SQLException, InterruptedException, IOException {
		
		/* Global input variables */


		
		/* Global data structures */
		ArrayList<DataItem> claimsData = new ArrayList<>();
//		ResultSet rsSampleDefs;
		ArrayList<Stratum> trialStrata = new ArrayList<>();  // sortable on amount.  Stratum has upperBound (not inclusive), 
					// lowerBound (inclusive), stratumSampleSize, stratumTotalAmount, stratumNumClaims, 
		ArrayList<Stratum> majorStrata = new ArrayList<>();  // holds the final Major Strata
		sampleClaims = new ArrayList<>();  //holds the claims sample that is drawn
		SamplerGUI2 currWindow = new SamplerGUI2(); //GUI window to make references to inside main


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

		
		while(currWindow.getDataFile() == null) { //Stops thread until a valid data file is processed
			try {
				Thread.sleep(1500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		String dataFileName = currWindow.getDataFile().getAbsolutePath();

		int nClaimsInDataFile = 0; //Number of claims in entire file (Total Population)
				

		while(currWindow.nextButtonPressed == false) {
			try {
				Thread.sleep(500);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		nTotalSamples = Integer.parseInt(currWindow.getSampleSizeField().getText());
		nTopNSamples = Integer.parseInt(currWindow.getTopClaimsField().getText());
		nZeroDollarSamples = Integer.parseInt(currWindow.getZeroDollarClaimsField().getText());
		nMajorStrata = Integer.parseInt(currWindow.getNumberOfStrataField().getText());
		confLevel = Double.parseDouble(currWindow.getConfidence_LevelField().getText());
		
		if(nMajorStrata <= 15 && nMajorStrata > 8) {
			minSamplesPerStratum = 15;
		}else if(nMajorStrata > 15) {
			minSamplesPerStratum = 9;
		}
		
		nClaimsInDataFile = SampleData.loadClaimsData(claimsData,dataFileName);
		
		portClaimsData = claimsData; //update global claimsData arraylist so it can be used outside this class

		int trials = 0; //Integer for keeping track of the number of trials per min stratum size
		boolean noSampleFound = false; //boolean to check if sampling was successful
		
		double perdif; //declare percentage difference variable so it may be updated and used for loop below
		
		double precision = (100 - confLevel);
		
		do {
			
			
			if(nMajorStrata > 15) {
				if(trials > 100 && minSamplesPerStratum > 6) { //Updates min strata size if no valid samples are found within 200 samples
					minSamplesPerStratum--;
					trials = 0;
				}
				if(trials > 100 && minSamplesPerStratum <= 10) {
					minSamplesPerStratum++;
					trials = 0;
				}
				
				if(trials > 150) {
					noSampleFound = true;
					break;
				}
			}else if(nMajorStrata <= 15 && nMajorStrata > 8) {
				if(trials > 100 && minSamplesPerStratum > 8) { //Updates min strata size if no valid samples are found within 200 samples
					minSamplesPerStratum--;
					trials = 0;
				}
				if(trials > 100 && minSamplesPerStratum <= 17) {
					minSamplesPerStratum++;
					trials = 0;
				}
				
				if(trials > 150) {
					noSampleFound = true;
					break;
				}
			}
			
			double tmptotal = 0;
			for (int i = 0; i < nClaimsInDataFile; i++) {  //get total amount of payments from entire population of claims
				tmptotal = tmptotal + claimsData.get(i).getAmount();
			}
			System.out.format("Total claims amount: %f", tmptotal);
			System.out.println("");
			Collections.sort(claimsData);
			System.out.format("Total claims amount: %f", tmptotal);
			System.out.println("");
			//		for (int i=0; i<10; i++) {
			//			System.out.println(claimsData.get(i).getObsNum()+", "+claimsData.get(i).getClaimID()+", "+claimsData.get(i).getLineNum()+", "+claimsData.get(i).getAmount());			
			//		}
			
			/* Determine the strata */
			CreateSample.defineStrata(claimsData, trialStrata, majorStrata, nTrialStrata, nZeroDollarSamples,
					nTopNSamples, nTotalSamples, nMajorStrata, minSamplesPerStratum);
			
			/* Draw and test the sample */
			CreateSample.drawSample(claimsData, sampleClaims, majorStrata, nZeroDollarSamples, nTopNSamples,
					nStratSamples, nTotalSamples, nMajorStrata);
			
			
			finStrata = majorStrata; //update final strata list to use outside class
			
			/*
			 * Code below generates statistics on current sample and prints both to console and GUI draw panel
			 */
			System.out.println("=========================================");
			double x = getPopMean(claimsData);
			currWindow.lblMeanClaimAmnt.setText(String.valueOf(x));
			System.out.println("Pop mean: " + x);
			
			double y;
			if(nClaimsInDataFile > 20000) {
				y = getWeightedSampleMean(sampleClaims, finStrata);
			}else {
				y = getWeightedSampleMeanSmall(sampleClaims, finStrata);
			}
			currWindow.WeightedSampleMeanVal.setText(String.valueOf(y));
			System.out.println("Weighted sample mean: " + y);
			
			double abDiff = getAbsDiff(x, y);
			currWindow.AbsoluteDiffVal.setText(String.valueOf(abDiff));
			System.out.println("Absolute Difference: " + abDiff);
			
			perdif = getPerDiff(getAbsDiff(x, y), x);
			currWindow.PercentageDiffVal.setText(String.valueOf(perdif) + "%");
			System.out.println("Percentage difference: " + getPerDiff(getAbsDiff(x, y), x) + "%");
			
			System.out.println("=========================================");
			if((perdif < precision) == false) { //Clear out arrayLists so Algorithm can run again
				trialStrata.clear();
				majorStrata.clear();
				sampleClaims.clear();
			}
			
			trials++;
		} while (perdif > precision); //Make sure to update if statement above if this precision is changed from .5
		System.out.println(trials + " Trials");
		
		if(noSampleFound) {
			//display error message
			System.out.println("fail");
		}
		
		dataProcessed = true; //Update boolean to show sampling algorithm had finished processing
		
		/*
		 * Show values of stats in GUI since algorithm is finished
		 */
		currWindow.lblMeanClaimAmnt.setVisible(true);
		currWindow.WeightedSampleMeanVal.setVisible(true);
		currWindow.AbsoluteDiffVal.setVisible(true);
		currWindow.PercentageDiffVal.setVisible(true);
		
	}
	
	
	public static ArrayList<Stratum> getMajorStrata() {
		return finStrata;
	}
	
	public static double getPopMean(ArrayList<DataItem> claimData) { //Returns mean of entire population from the claims data
		
		double total = 0;
		double size = 0; 
		for (int i = 0; claimData.get(i).stratumNum < finStrata.size() - 1; i++) {
			total += claimData.get(i).amount; //update numerator
			size++; //update denominator
		}
		
		return roundToTwo(total / size);
	}
	
	/* Calculates the weight sample mean by summing the product of each strata mean with its respective weight, where each weight is the proportion 
	 * of the sample to the entire sample size, ommiting the top 21, usually using 200
	 */
	
	public static double getWeightedSampleMean(ArrayList<DataItem> sampleCLaims, ArrayList<Stratum> finStrata) {
		double result = 0;
		double divisor = 0;
		for(int i = 0; i < finStrata.size() - 1; i++) { //Use strata 0 to 20, omit 21 because it skews the weighted mean
			int currStratSize = 0;
			double currStratAmnt = 0;
			for(int j = 0; j < sampleCLaims.size(); j++) {
				if (sampleClaims.get(j).stratumNum == i) {
					currStratSize++;
					currStratAmnt += sampleClaims.get(j).amount;
					
				}
			}
			double currMean = currStratAmnt / currStratSize;
			double weight = currStratSize / (float)(nTotalSamples);
			result += (currMean * weight);
			divisor++;
		}
		return roundToTwo(result / divisor);
	}
	/*
	 * Calculates weighted sample mean for small samples
	 */
	public static double getWeightedSampleMeanSmall(ArrayList<DataItem> sampleCLaims, ArrayList<Stratum> finStrata) {
		double result = 0;
		double divisor = sampleClaims.size();
		for(int i = 0; i < finStrata.size() - 1; i++) { //Use strata 0 to 20, omit 21 because it skews the weighted mean
			int currStratSize = 0;
			double currStratAmnt = 0;
			for(int j = 0; j < sampleCLaims.size(); j++) {
				if (sampleClaims.get(j).stratumNum == i) {
					currStratSize++;
					currStratAmnt += sampleClaims.get(j).amount;
					
				}
			}
			double currMean = currStratAmnt / currStratSize;

			result += (currMean);
		}
		return roundToTwo(result / divisor);
	}
	
	/*
	 * Returns absolute difference of pop mean and weighted sample mean
	 */
	public static double getAbsDiff(double x, double y) {
		return roundToTwo(Math.abs(x-y));
	}
	
	/*
	 * Returns percentage difference of pop mean and weighted sample mean  
	 */
	public static double getPerDiff(double diff, double popMean) {
		return roundToTwo((Math.abs(diff / popMean) * 100));
	}
	
	/*
	 * Rounds double to 2 digits for presentation purposes
	 */
	public static double roundToTwo(double num) {
		double x = num * 1000;
		return (Math.round(x) / 1000.0);
	}
	
	
	}

