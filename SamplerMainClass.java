package SamplerPackage;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
	protected String data;
	
	/* Constructor */
	public DataItem() {
		obsNum = 0;
		amount = 0.0;
		stratumNum = -1;
		data = "";
	}
	/* Constructor */
	public DataItem(int o, double a, String dat) {
		obsNum = o;
		amount = a;
		stratumNum = -1;
		data = dat;
	}
	/* Constructor */
	public DataItem(DataItem d) {
		this.obsNum = d.obsNum;
		this.amount = d.amount;
		this.stratumNum = d.stratumNum;
		this.data = d.data;
	}
	/* Constructor */
	public DataItem(int amount) {
		obsNum = 0;
		this.amount = amount;
		stratumNum = -1;
		data = "";
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
	
	public static ArrayList<String> heads = new ArrayList<String>();

	public static int loadClaimsData(ArrayList<DataItem> dataList, String dataFileName, SamplerGUI2 currWindow) throws IOException {
		
		if(dataFileName.contains(".csv")) {
			int nLoaded = -1;  //-1 = Error, nothing loaded; positive n = number of claims loaded
			/* Open the data file */
			File inputFile = new File(dataFileName);
			Scanner in = new Scanner(inputFile);
			
			/* Read in the data file */
			if (in.hasNext()) { 
				String headLine = "";
				headLine = in.nextLine(); //Header Line
				Scanner headToParse = new Scanner(headLine).useDelimiter(","); //split header into iterative scanner

				while(headToParse.hasNext()) { //Iterate though entire header
					String headElement = headToParse.next();
					currWindow.comboBox.addItem(headElement);
					currWindow.comboBox_1.addItem(headElement);
					heads.add(headElement);
				}
				currWindow.mainFrame.repaint();
				
				while(SamplerGUI2.columnBtnPushed == false) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
				int obsIndex = SamplerGUI2.comboBox.getSelectedIndex();
				int amntIndex = SamplerGUI2.comboBox_1.getSelectedIndex();
				
				
				int index = 0; //Starting Index
				
			nLoaded = 0;
			
			int obsNum = 0;	
			double amntPaid = 0;
			
			while (in.hasNext()) { //Iterate though each claim line
				String inputLine = in.nextLine();
				String data = inputLine;
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
					DataItem newClaim = new DataItem(obsNum, amntPaid, data);
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
				for(int j = 0; row.getCell(j).getStringCellValue() != null; j++) {
					String currElement =  row.getCell(j).getStringCellValue().toLowerCase();
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
			for(int i = dataStartRow; i <= inputSheet.getLastRowNum(); i++) {
				XSSFRow row = inputSheet.getRow(i);
				int obsNum = (int)row.getCell(obsIndex).getNumericCellValue();
				double amntPaid = row.getCell(amntIndex).getNumericCellValue();
				if(amntPaid >= 0) {
					DataItem newClaim = new DataItem(obsNum, amntPaid, "null");
					dataList.add(newClaim);
					nLoaded++;
				}
			}
			workbook.close();
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
	public static int nClaimsInDataFile;
	public static double popMean;
	public static double sampleMean;
	public static double gAbsDiff;
	public static double gPerDiff;
	public static  SamplerGUI2 currWindow;
	
	public static void main(String[] args) throws SQLException, InterruptedException, IOException {
		
		/* Global input variables */


		
		/* Global data structures */
		ArrayList<DataItem> claimsData = new ArrayList<>();
//		ResultSet rsSampleDefs;
		ArrayList<Stratum> trialStrata = new ArrayList<>();  // sortable on amount.  Stratum has upperBound (not inclusive), 
					// lowerBound (inclusive), stratumSampleSize, stratumTotalAmount, stratumNumClaims, 
		ArrayList<Stratum> majorStrata = new ArrayList<>();  // holds the final Major Strata
		sampleClaims = new ArrayList<>();  //holds the claims sample that is drawn
		currWindow = new SamplerGUI2(); //GUI window to make references to inside main


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

		nClaimsInDataFile = 0; //Number of claims in entire file (Total Population)
		
		nClaimsInDataFile = SampleData.loadClaimsData(claimsData,dataFileName, currWindow);

		while(currWindow.columnBtnPushed == false) {
			try {
				Thread.sleep(500);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
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
		
		if(nMajorStrata <= 15 && nMajorStrata > 8) {
			minSamplesPerStratum = 15;
		}else if(nMajorStrata > 15) {
			minSamplesPerStratum = 9;
		}
		
		
		
		portClaimsData = claimsData; //update global claimsData arraylist so it can be used outside this class

		int trials = 0; //Integer for keeping track of the number of trials per min stratum size
		boolean noSampleFound = false; //boolean to check if sampling was successful
		
		double perdif; //declare percentage difference variable so it may be updated and used for loop below
		
		double precision = (100 - confLevel);
		
		boolean sizeIsGood = true;
		
		do {
			
			sizeIsGood = true;
			/*
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
			*/
			minSamplesPerStratum = 9;
			
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
			System.out.println("Pop mean: " + x);
			
			double y = getWeightedSampleMean(sampleClaims, finStrata);
			System.out.println("Weighted sample mean: " + y);
			
			double abDiff = getAbsDiff(x, y);
			System.out.println("Absolute Difference: " + abDiff);
			
			perdif = getPerDiff(getAbsDiff(x, y), x);
			System.out.println("Percentage difference: " + getPerDiff(getAbsDiff(x, y), x) + "%");
			
			if(perdif < precision) {
				currWindow.lblMeanClaimAmnt.setText(String.valueOf(x));
				popMean = x;
				currWindow.WeightedSampleMeanVal.setText(String.valueOf(y));
				sampleMean = y;
				currWindow.AbsoluteDiffVal.setText(String.valueOf(abDiff));
				gAbsDiff = abDiff;
				currWindow.PercentageDiffVal.setText(String.valueOf(perdif) + "%");
				gPerDiff = perdif;
			}
			
			System.out.println("=========================================");
			if((perdif < precision) == false) { //Clear out arrayLists so Algorithm can run again
				trialStrata.clear();
				majorStrata.clear();
				sampleClaims.clear();
			}
			
			trials++;
			
			
		} while (perdif > precision); //Make sure to update if statement above if this precision is changed from .5 // && 
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
		for(int i = 0; i < finStrata.size() - 1; i++) { //Use strata 0 to 20, omit 21 because it skews the weighted mean
			double currStratMean = finStrata.get(i).stratumMean;

			double weight = finStrata.get(i).stratumNumClaims / (double)(nClaimsInDataFile);
			result += (currStratMean * weight);
			
		}
		return roundToTwo(result);
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
	
	/*
	 * Checks the data to ensure that the density of zero claims is low
	 */
	public static double checkZeroClaimDensity(ArrayList<Stratum> finStrata) {
		int zeroClaims = finStrata.get(0).stratumNumClaims;
		int totalClaims = 0;
		for(int i = 0; i < finStrata.size(); i++) {
			totalClaims += finStrata.get(i).stratumNumClaims;
		}
		double ratio = (double)zeroClaims / (double)totalClaims;
		
		return ratio;
	}
	
	

}



