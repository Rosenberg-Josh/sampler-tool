package SamplerPackage;
import java.util.ArrayList;
import java.util.Collections;

class CreateSample {
	/** This class contains the methods to define the strata and create the sample */
	
	public static void defineStrata(ArrayList<DataItem> claimsData, ArrayList<Stratum> trialStrata, ArrayList<Stratum> majorStrata, 
			int nTrialStrata, int nZeroDollarSamples, int nTopNSamples, int nTotalSamples, int nMajorStrata, int minSamplesPerStratum) {
		
		/* Set variables */
		int claimsDataSize = claimsData.size();
		int MArrayPos = claimsDataSize-nTopNSamples-1;  //position of M in claimsData
		double M = claimsData.get(MArrayPos).getAmount();  // M is the largest non-zero claim in the random sample...
							//(top N claims are all audited, so random sample range ends with N+1th largest)
		int mArrayPos = 0;  // position of m (the smallest non-zero claim) in the claimsData array
		for (int i=0; claimsData.get(i).getAmount()==0; i++){ // iterates through claims data and finds smallest non-zero claim J>R
			mArrayPos++;
		}
		int numZeroClaims = mArrayPos;
		int numNonZeroClaims = claimsDataSize - mArrayPos;
		double m = claimsData.get(mArrayPos).getAmount();  //m is the amount of the smallest non-zero claim
		double d = M - m;
		double stratumWidth = d/nTrialStrata;
		int stratumMaxClaims = (int)((numNonZeroClaims-nTopNSamples)*0.03);  //max claims in a random stratum -- use creating detail strata
		
		/* Populate the first stratum boundaries */
		Stratum newStratum = new Stratum();
		newStratum.setLowerBound(0);
		newStratum.setUpperBound(0);
		trialStrata.add(newStratum);

		/* Populate second stratum boundy */
		newStratum = new Stratum();
		newStratum.setLowerBound(0.001);
		newStratum.setUpperBound(newStratum.getLowerBound()+stratumWidth);
		trialStrata.add(newStratum);
		 
		/* Populate the remaining trialStrata boundaries */
		for (int i=2; i<nTrialStrata+2; i++) {
			newStratum = new Stratum();
//			System.out.println("Stratum "+i+" upper bound = "+trialStrata.get(i-1).getUpperBound());
			newStratum.setLowerBound(trialStrata.get(i-1).getUpperBound());
			newStratum.setUpperBound(newStratum.getLowerBound()+stratumWidth);
			trialStrata.add(newStratum);
		}
		trialStrata.get(nTrialStrata).setUpperBound(M+.001);  // adjusts last random sample stratum to hold M (upper bound being exclusive)
		trialStrata.get(nTrialStrata+1).setLowerBound(trialStrata.get(nTrialStrata).getUpperBound());  // adjust boundaries of the large claims stratum
		trialStrata.get(nTrialStrata+1).setUpperBound(claimsData.get(claimsData.size()-1).getAmount()+.001);  // adjust boundaries of the large claims stratum
		
		/*  Put the claims into the strata */
		int curStratum = 0;
		for (int i=0; i<claimsDataSize; i++){  //walk through each claim
			while (trialStrata.get(curStratum).getUpperBound()<claimsData.get(i).getAmount()) { //if the claim doesn't fit in the curStratum, advance
				curStratum++;
			}
			claimsData.get(i).setStratumNum(curStratum);
		}

		/* Adjust Top 25 stratum if needed */
		for (int i=1; i<=nTopNSamples; i++) {  //Go through the Top N claims
			if (claimsData.get(MArrayPos+i).getStratumNum() != trialStrata.size()-1) { // If it's not in the TopN Stratum, move it...
				claimsData.get(MArrayPos+i).setStratumNum(trialStrata.size()-1);  //Change the claim Stratum number
				trialStrata.get(trialStrata.size()-1).setStratumNumClaims(trialStrata.get(trialStrata.size()-1).getStratumNumClaims()+1);  //Increment the stratum num claims stat
				trialStrata.get(trialStrata.size()-1).setStratumTotalAmount(trialStrata.get(trialStrata.size()-1).getStratumTotalAmount()+claimsData.get(MArrayPos+i).getAmount());  // Add the claim amount to the Stratum total
			}
		}
		
		/* Calculate the trialStrata Stats */
		double totalAmount  = calcStrataStats(claimsData, trialStrata, nTrialStrata, claimsDataSize);

		/* Print out strata */
		for (int i=0; i<nTrialStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(trialStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "+Math.round(trialStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(trialStrata.get(i).getStratumNumClaims()*1000)/1000.0+"  Amount: "+Math.round(trialStrata.get(i).getStratumTotalAmount()*1000)/1000.0);
		}
		
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		
		/* Create the detail strata */
		int curClaimPos = mArrayPos;  //start with the first non-zero claim
		int curOldStratumNum = claimsData.get(curClaimPos).getStratumNum();
		int prevOldStratumNum = curOldStratumNum;
		int curNewStratumNum = curOldStratumNum;
		int curNewStratumCount = 0;
		while (curClaimPos < MArrayPos+1) { //update all the non-zero, but not topN, claims
			prevOldStratumNum = curOldStratumNum;
			curOldStratumNum = claimsData.get(curClaimPos).getStratumNum();
			if (curOldStratumNum != prevOldStratumNum) { //if we have moved on to a new old stratum, reset counters
				curNewStratumCount = 0;
				curNewStratumNum++;
			}
			if (curNewStratumCount < stratumMaxClaims) {  //if the current new stratum isn't full, add this claim to it
				claimsData.get(curClaimPos).setStratumNum(curNewStratumNum);
				curNewStratumCount++;
			}
			else {  //if the current new stratum IS full, add a new stratum
				curNewStratumNum++;
				claimsData.get(curClaimPos).setStratumNum(curNewStratumNum);
				curNewStratumCount=1;
			}
			curClaimPos++; 
		}
		curNewStratumNum++;
		while (curClaimPos < claimsDataSize) {  //renumber the large claims
			claimsData.get(curClaimPos).setStratumNum(curNewStratumNum);
			curClaimPos++;
		}
		/* Recreate the trialStrata arraylist */
		while (trialStrata.size()>0) { //empty the old trialStrata list
			trialStrata.remove(0);
		}
		curNewStratumNum = 0;
		curClaimPos = 0;
		double curNewStratumLowerBound = claimsData.get(curClaimPos).getAmount();
		while (curClaimPos < claimsDataSize) { //loop through all the claims
			if (claimsData.get(curClaimPos).getStratumNum() != curNewStratumNum) {  //if we are at a new stratum, add the previous to array
				newStratum = new Stratum();
				newStratum.setLowerBound(curNewStratumLowerBound);
				newStratum.setUpperBound(claimsData.get(curClaimPos).getAmount());
				trialStrata.add(newStratum);
				curNewStratumNum = claimsData.get(curClaimPos).getStratumNum();
				curNewStratumLowerBound = claimsData.get(curClaimPos).getAmount();
			}
			curClaimPos++;
		}
		/* Add the last Stratum */
		newStratum = new Stratum();
		newStratum.setLowerBound(curNewStratumLowerBound);
		newStratum.setUpperBound(claimsData.get(curClaimPos-1).getAmount());
		trialStrata.add(newStratum);

		/* Check the Top 25 again, just to be sure */
		for (int i=1; i<=nTopNSamples; i++) {  //Go through the Top N claims
			if (claimsData.get(MArrayPos+i).getStratumNum() != trialStrata.size()-1) { // If it's not in the TopN Stratum, move it...
				claimsData.get(MArrayPos+i).setStratumNum(trialStrata.size()-1);  //Change the claim Stratum number
				trialStrata.get(trialStrata.size()-1).setStratumNumClaims(trialStrata.get(trialStrata.size()-1).getStratumNumClaims()+1);  //Increment the stratum num claims stat
				trialStrata.get(trialStrata.size()-1).setStratumTotalAmount(trialStrata.get(trialStrata.size()-1).getStratumTotalAmount()+claimsData.get(MArrayPos+i).getAmount());  // Add the claim amount to the Stratum total
			}
		}
		
		/* Calculate the new trialStrata Stats */
		nTrialStrata = trialStrata.size()-2;
		totalAmount  = calcStrataStats(claimsData, trialStrata, nTrialStrata, claimsDataSize);

		/* Calculate and set the t, SRF and CSRF values */
		calcTCSRF(trialStrata, nTrialStrata, stratumMaxClaims);
				
		/* Print out detail strata */
		for (int i=0; i<nTrialStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(trialStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "+Math.round(trialStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(trialStrata.get(i).getStratumNumClaims()*1000)/1000.0+"  Amount: "+Math.round(trialStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  CSRF: "+(int)trialStrata.get(i).getCSRF());
		}	
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		double tmptotal = 0.0;
		for (int i=0; i<trialStrata.size(); i++) {
			tmptotal = tmptotal + trialStrata.get(i).getStratumTotalAmount();
		}
		System.out.println("Sum of all the strata totals = "+tmptotal);
		
		/* Create the Major Strata */
		createMajorStrata(trialStrata, majorStrata, claimsData, nMajorStrata, nTrialStrata, mArrayPos, MArrayPos, nZeroDollarSamples, nTopNSamples);
		
		/*  Put the claims into the Major strata */
		curStratum = 0;
		for (int i=0; i<claimsDataSize; i++){  //walk through each claim
			while (majorStrata.get(curStratum).getUpperBound()<claimsData.get(i).getAmount()) { //if the claim doesn't fit in the curStratum, advance
				curStratum++;
			}
			claimsData.get(i).setStratumNum(curStratum);
		}
		/* Check the Top 25 again and move as necessary */
		for (int i=1; i<=nTopNSamples; i++) {  //Go through the Top N claims
			if (claimsData.get(MArrayPos+i).getStratumNum() != majorStrata.size()-1) { // If it's not in the TopN Stratum, move it...
				claimsData.get(MArrayPos+i).setStratumNum(majorStrata.size()-1);  //Change the claim Stratum number
				majorStrata.get(majorStrata.size()-1).setStratumNumClaims(majorStrata.get(majorStrata.size()-1).getStratumNumClaims()+1);  //Increment the stratum num claims stat
				majorStrata.get(majorStrata.size()-1).setStratumTotalAmount(majorStrata.get(majorStrata.size()-1).getStratumTotalAmount()+claimsData.get(MArrayPos+i).getAmount());  // Add the claim amount to the Stratum total
			}
		}	
		
		/* Calculate the Major Strata stats */
		totalAmount  = calcStrataStats(claimsData, majorStrata, nMajorStrata, claimsDataSize);

		/* Print out major strata */
		for (int i=0; i<nMajorStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(majorStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "+Math.round(majorStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(majorStrata.get(i).getStratumNumClaims()*1000)/1000.0+"  Amount: "+Math.round(majorStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  CSRF: "+(int)majorStrata.get(i).getCSRF());
		}	
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		tmptotal = 0.0;
		for (int i=0; i<majorStrata.size(); i++) {
			tmptotal = tmptotal + majorStrata.get(i).getStratumTotalAmount();
		}
		System.out.println("Sum of all the strata totals = "+tmptotal);
		
		/* Calculate the sample size for each major stratum */
		double sigmaNkSxk = 0.0;  //calculate sigmaNkSxk
		for (int i=1; i<=nMajorStrata; i++) {  
			sigmaNkSxk += majorStrata.get(i).getStratumNumClaims() * majorStrata.get(i).getStratumSD();
		}
		int nSamplesCreated = 0;  //calculate the sample n for each stratum (nsubi)
		for (int i=1; i<=nMajorStrata; i++) {
			int nsubi = 0;
			nsubi = Math.min(majorStrata.get(i).getStratumNumClaims(), (int) Math.round((nTotalSamples-nZeroDollarSamples-nTopNSamples) * 
					(majorStrata.get(i).getStratumNumClaims()*majorStrata.get(i).getStratumSD()) / sigmaNkSxk));
			majorStrata.get(i).setStratumSampleSize(nsubi);
			nSamplesCreated += nsubi;
		}

		/* Print out major strata with sample sizes */
		System.out.println("\n\nHere are the major strata with theoretical sample sizes:");
		for (int i=0; i<nMajorStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(majorStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "+Math.round(majorStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(majorStrata.get(i).getStratumNumClaims()*1000)/1000.0+"  Amount: "+Math.round(majorStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  Sample Size: "+majorStrata.get(i).getStratumSampleSize());
		}	
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		tmptotal = 0.0;
		for (int i=0; i<majorStrata.size(); i++) {
			tmptotal = tmptotal + majorStrata.get(i).getStratumSampleSize();
		}
		System.out.println("Sum of all the strata sample sizes = "+tmptotal);

		
		/* Ensure a minimum number of samples per stratum (if possible) */
		int nSamplesAvailable = 0;  //number of samples above the minimum 
		int nSamplesNeeded = 0;  //number of samples needed to meet the minimum
		for (int i=1; i<=nMajorStrata; i++) {  //loop through the random sample strata (not zero and large claims)
			if (majorStrata.get(i).getStratumSampleSize() < minSamplesPerStratum) {
				nSamplesNeeded += minSamplesPerStratum - majorStrata.get(i).getStratumSampleSize();
			}
			else {
				if (majorStrata.get(i).getStratumSampleSize() > minSamplesPerStratum) {
					nSamplesAvailable += majorStrata.get(i).getStratumSampleSize() - minSamplesPerStratum;
				}
			}
		}
		double takePercent = nSamplesNeeded / (nSamplesAvailable*1.0);
		for (int i=1; i<=nMajorStrata; i++) {  //loop through and make the changes
			if (majorStrata.get(i).getStratumSampleSize() < minSamplesPerStratum) { //bring low samples up to minimum
				majorStrata.get(i).setStratumSampleSize(minSamplesPerStratum);
			}
			else {
				if (majorStrata.get(i).getStratumSampleSize() > minSamplesPerStratum) {  //take back from samples above the minimum
					int extraSamples = majorStrata.get(i).getStratumSampleSize() - minSamplesPerStratum;
					majorStrata.get(i).setStratumSampleSize(majorStrata.get(i).getStratumSampleSize() - (int)(extraSamples * takePercent));
				}
			}
		}
		
		/* Print out major strata with sample sizes */
		System.out.println("\n\nHere are the major strata adjusting for min sample size:");
		for (int i=0; i<nMajorStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(majorStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "+Math.round(majorStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(majorStrata.get(i).getStratumNumClaims()*1000)/1000.0+"  Amount: "+Math.round(majorStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  Sample Size: "+majorStrata.get(i).getStratumSampleSize());
		}	
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		tmptotal = 0.0;
		for (int i=0; i<majorStrata.size(); i++) {
			tmptotal = tmptotal + majorStrata.get(i).getStratumSampleSize();
		}
		System.out.println("Sum of all the strata sample sizes = "+tmptotal);
		
		/* Adjust sample sizes for rounding */
		nSamplesCreated = 0;
		for (int i=1; i<=nMajorStrata; i++) {
			nSamplesCreated += majorStrata.get(i).getStratumSampleSize();
		}
		int nSamplesToAdjust = nTotalSamples - nZeroDollarSamples - nTopNSamples - nSamplesCreated;
		if (nSamplesToAdjust > 0) {  // need to add some samples back in -- add to highest strata
			curStratum = nMajorStrata;
			while (nSamplesToAdjust > 0) { //add to the highest strata
				int nAvailable = majorStrata.get(curStratum).getStratumNumClaims() - majorStrata.get(curStratum).getStratumSampleSize();
				int nToAdd = Math.min(nSamplesToAdjust,  nAvailable);
				majorStrata.get(curStratum).setStratumSampleSize(majorStrata.get(curStratum).getStratumSampleSize() + nToAdd);
				nSamplesToAdjust -= nToAdd;
				curStratum--;
			}
		}
		else {
			if (nSamplesToAdjust < 0) { //need to remove some samples -- remove them from the largest strata 
				int largestSample = 0;
				for (int i=1; i<nMajorStrata; i++) {  //find the largest stratum
					if (majorStrata.get(i).getStratumSampleSize()>=largestSample) {
						largestSample = majorStrata.get(i).getStratumSampleSize();
						curStratum = i;
					}
				}
				while (nSamplesToAdjust < 0) {
					int nToRemove = Math.min(-1*nSamplesToAdjust,  majorStrata.get(curStratum).getStratumSampleSize()/4);  //don't take away more than 1/4 of the samples in a stratum
					majorStrata.get(curStratum).setStratumSampleSize(majorStrata.get(curStratum).getStratumSampleSize() - nToRemove);
					nSamplesToAdjust += nToRemove;
					curStratum--;					
				}
			}	
		}
		
		/* Set the first/last claim pos stats */
		setStrataClaimPos(majorStrata);
		
		/* Print out major strata with sample sizes */
		System.out.println("\n\nHere are the final major strata, adjusting for rounding:");
		for (int i=0; i<nMajorStrata+2; i++) {
			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(majorStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "
						+Math.round(majorStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(majorStrata.get(i).getStratumNumClaims()*1000)/1000.0
						+"  Amount: "+Math.round(majorStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  Sample Size: "
						+ majorStrata.get(i).getStratumSampleSize()+"  First Claim Pos: "+majorStrata.get(i).getFirstClaimPos()
						+ "  Last Claim Pos:  " + majorStrata.get(i).getLastClaimPos());
		}	
		System.out.format("Total amount = %f",totalAmount);
		System.out.println("");
		tmptotal = 0.0;
		for (int i=0; i<majorStrata.size(); i++) {
			tmptotal = tmptotal + majorStrata.get(i).getStratumSampleSize();
		}
		System.out.println("Sum of all the strata sample sizes = "+tmptotal);
		
	}
	
	public static double calcStrataStats(ArrayList<DataItem> claimsData, ArrayList<Stratum> trialStrata, int nTrialStrata, int claimsDataSize) {
		/** Calculates the trialStrata stats */
		double alttotal = 0;
		int curStratum = 0;
		int curNStratumClaims = 0;
		double curStratumAmount = 0.0;
		double totalAmount = 0.0;
		for (int i=0; i<claimsDataSize; i++){
			alttotal = alttotal + claimsData.get(i).getAmount();
			/* If the current claim is in a new stratum, save the stratum totals and reset */
			if (claimsData.get(i).getStratumNum() != curStratum) {
				trialStrata.get(curStratum).setStratumNumClaims(curNStratumClaims);
				trialStrata.get(curStratum).setStratumTotalAmount(curStratumAmount);
				trialStrata.get(curStratum).setStratumMean(curStratumAmount/curNStratumClaims);
				while (curStratum != claimsData.get(i).getStratumNum()){ //Advance to the new curStratNum
					curStratum++;
				}
				/* Reset Stratum totals */
				curNStratumClaims = 0;
				curStratumAmount = 0.0;
			}  
			
			/* Add to the current stratum stats */
			curNStratumClaims++;
			curStratumAmount = curStratumAmount + claimsData.get(i).getAmount();
			totalAmount = totalAmount + claimsData.get(i).getAmount();
		}
		/* Record the last Stratum stats */
		trialStrata.get(curStratum).setStratumNumClaims(curNStratumClaims);
		trialStrata.get(curStratum).setStratumTotalAmount(curStratumAmount);
		trialStrata.get(curStratum).setStratumMean(curStratumAmount/curNStratumClaims);
		System.out.format("Alt Total: %f", alttotal);
		System.out.println("");
		
		/* Calculate the stratum standard deviation */
		curStratum = 0;
		double variance = 0.0;
		for (int i=0; i<claimsDataSize; i++) {
			/* Write out the variance if moving on to a new stratum */
			if (claimsData.get(i).getStratumNum() > curStratum) {
				trialStrata.get(curStratum).setStratumSD(Math.pow(variance, 0.5));
				variance = 0;
				curStratum = claimsData.get(i).getStratumNum();
			}
			variance = variance + Math.pow((claimsData.get(i).getAmount()-trialStrata.get(curStratum).getStratumMean()),2);			
		}
		trialStrata.get(curStratum).setStratumSD(Math.pow(variance, 0.5));
		
		return totalAmount;
	}
	
	public static void calcTCSRF(ArrayList<Stratum> trialStrata, int nTrialStrata, int stratumMaxClaims) {
		/** Calculates the t, SRF and CSRF statistics for the trialStrata arraylist, and sets those values for each
		 * Stratum object in the array.
		 */
		
		/* Set the t statistic */
		for (int i=1; i<=nTrialStrata; i++) {
			int newT = 1;
			int oldI = i;
			if ((i <= nTrialStrata) && (trialStrata.get(i).getStratumNumClaims() == stratumMaxClaims)) {
				while ((i <= nTrialStrata) && (trialStrata.get(i).getStratumNumClaims() == stratumMaxClaims)) {
					newT++;
					i++;
				}
			}
			for (int j=oldI; j<=i; j++) {
				trialStrata.get(j).setT(newT);
			}
		}
		
		/* Set the SRF and CSRF statistics */
		double CSRF = 0.0;
		for (int i=1; i<=nTrialStrata; i++) {
			trialStrata.get(i).setSRF(Math.sqrt(trialStrata.get(i).getStratumNumClaims()/(trialStrata.get(i).getT()*1.0)));
			CSRF = CSRF + trialStrata.get(i).getSRF();
			trialStrata.get(i).setCSRF(CSRF);
		}
	}
	
	public static void createMajorStrata(ArrayList<Stratum> trialStrata, ArrayList<Stratum> majorStrata, 
			ArrayList<DataItem> claimsData, int nMajorStrata, int nTrialStrata, int mArrayPos, int MArrayPos, int nZeroDollarSamples, int nTopNSamples){
		/** Creates the Major Strata */
		
		/* Set F */
		double F = trialStrata.get(nTrialStrata).getCSRF();
		
		/* Create the zero-dollar claim stratum */
		Stratum newStratum = new Stratum(trialStrata.get(0));
		newStratum.setUpperBound(0); //Not sure if this will still yield valid sample
		newStratum.setStratumSampleSize(nZeroDollarSamples);
		majorStrata.add(newStratum);
		
		/* Create the non-zero/non-TopN strata */
		int trialStrataPos = 1; //keeps track of which trialStrata we are on
		for (int i=1; i<=nMajorStrata; i++){ //create each major stratified sample stratum
			newStratum = new Stratum();
			if(i == 1) {
				newStratum.setLowerBound(0.01);
			}else {
				newStratum.setLowerBound(majorStrata.get(i-1).getUpperBound());
			}
			do {
				newStratum.setUpperBound(trialStrata.get(trialStrataPos).getUpperBound());
				trialStrataPos++;
			} while (trialStrata.get(trialStrataPos-1).getCSRF()<((i*F)/nMajorStrata));
			majorStrata.add(newStratum);
		}
		
		/* Create the TopN stratum */
		newStratum = new Stratum();
		newStratum.setLowerBound(majorStrata.get(nMajorStrata).getUpperBound());
		newStratum.setUpperBound(trialStrata.get(trialStrataPos).getUpperBound());
		newStratum.setStratumSampleSize(nTopNSamples);
		majorStrata.add(newStratum);
	}

	public static void setStrataClaimPos(ArrayList<Stratum> strata) {
				
		/* Set the first/last claimPos variable for each stratum */
		int curClaimPos = 0; //position in the claimsData array
		for (int i=0; i<strata.size(); i++) {
			strata.get(i).setFirstClaimPos(curClaimPos);
			curClaimPos = curClaimPos + strata.get(i).getStratumNumClaims()-1;
			strata.get(i).setLastClaimPos(curClaimPos);
			curClaimPos++;
	}
}

	public static void drawSample(ArrayList<DataItem> claimsData, ArrayList<DataItem> sampleClaims, ArrayList<Stratum> majorStrata, 
			int nZeroDollarSamples, int nTopNSamples, int nMajorSamples, int nTotalSamples, int nMajorStrata) {
		
		/* Empty out sampleClaims if necessary */
		while (sampleClaims.size()>0) {
			sampleClaims.remove(1);
		}
		
		/* Draw the stratum samples */
		for (int i=0; i<=nMajorStrata; i++) {  //for each stratum (except the TopN)...
			int nToDraw = majorStrata.get(i).getStratumSampleSize();
			int nStratumClaims = majorStrata.get(i).getStratumNumClaims();
			
			/* Create and load stratumClaims arraylist */
			int curClaimsDataPos = majorStrata.get(i).getFirstClaimPos();
			ArrayList<Integer> stratumClaims = new ArrayList<>();  //holds the claimsData array position numbers for the claims in this stratum
			for (int j = 0; j<nStratumClaims; j++) {
				stratumClaims.add(curClaimsDataPos);
				curClaimsDataPos++;
			}
			
			/* Draw the stratum sample */
			while (nToDraw > 0) {
				int samplePos = (int) (Math.random()*nStratumClaims);  //random select the claim in the stratum
				DataItem newSampleClaim = new DataItem(claimsData.get(stratumClaims.get(samplePos)));  //clone the sample DataItem
				sampleClaims.add(newSampleClaim);  //add it to the sample ArrayList
				stratumClaims.remove(samplePos);  //remove it from the sample pool
				nStratumClaims--;
				nToDraw--;
			}
		}

		/* Add the TopN stratum */
		int curClaimsPos = claimsData.size() - nTopNSamples;
		while (curClaimsPos < claimsData.size()) {
			DataItem newSampleClaim = new DataItem(claimsData.get(curClaimsPos));  //clone the sample DataItem
			sampleClaims.add(newSampleClaim);  //add it to the sample ArrayList
			curClaimsPos++;
		}
		
		/*  ADD PRINT SAMPLE CODE HERE */
		System.out.println("\n\nHere is the claims sample, broken up by major strata:");
		int curSamplePos = 0;
		int sampleCount = 0;
		double totalAmount = 0.0;
		for (int i=0; i<nMajorStrata+2; i++) {
			System.out.printf("Strata Number:%4d,  Lower: %,10.2f,  Upper: %,10.2f,  Size:%,6d,  Amount:%,12.2f, Sample Size:%4d,  First Claim Pos:%,8d,  Last Claim Pos:%,8d%n", 
					+ i, majorStrata.get(i).getLowerBound(), majorStrata.get(i).getUpperBound(), majorStrata.get(i).getStratumNumClaims(), 
					+ majorStrata.get(i).getStratumTotalAmount(), majorStrata.get(i).getStratumSampleSize(), majorStrata.get(i).getFirstClaimPos(), 
					+ majorStrata.get(i).getLastClaimPos());
//			System.out.println("Strata Number: "+i+"  Lower: "+Math.round(majorStrata.get(i).getLowerBound()*1000)/1000.0+"  Upper: "
//						+Math.round(majorStrata.get(i).getUpperBound()*1000)/1000.0+"  Size: "+Math.round(majorStrata.get(i).getStratumNumClaims()*1000)/1000.0
//						+"  Amount: "+Math.round(majorStrata.get(i).getStratumTotalAmount()*1000)/1000.0+"  Sample Size: "
//						+ majorStrata.get(i).getStratumSampleSize()+"  First Claim Pos: "+majorStrata.get(i).getFirstClaimPos()
//						+ "  Last Claim Pos:  " + majorStrata.get(i).getLastClaimPos());
			while ((curSamplePos < sampleClaims.size()) && (sampleClaims.get(curSamplePos).getStratumNum() == i)) {
				totalAmount += sampleClaims.get(curSamplePos).getAmount();
				sampleCount++;
//				System.out.printf("Stratum:  %4d,  ObsNum:  %6d,  Amount:  %,8.2f%n",sampleClaims.get(curSamplePos).getStratumNum(),
//						sampleClaims.get(curSamplePos).getObsNum(),sampleClaims.get(curSamplePos).getAmount());
				curSamplePos++;
			}
		}	
		System.out.format("Total audit sample paid claims amount = %,12.2f,  Total number of samples:  %d",totalAmount, sampleCount);
		System.out.println("");
		double tmptotal = 0.0;
		for (int i=0; i<majorStrata.size(); i++) {
			tmptotal = tmptotal + majorStrata.get(i).getStratumSampleSize();
		}
		System.out.println("Sum of all the strata sample sizes = "+tmptotal);		
	}

		
}

