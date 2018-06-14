package SamplerPackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVWriter {
	private static final String COMMA_DELIMITER = ",";
	
	private static final String NEW_LINE_SEPARATOR = "\n";
        
	private static final String FILE_HEADER = "ObservationNumber,Claim_Number,Line_Number,Amount,Stratum_Number";
	private static final String FILE_HEADER2 = "Stratum_Number,Lower,Upper,Size,Amount,Sample_Size,First_Claim_Position,Last_Claim_Position";

	public static void writeSampleFile(String fileName, ArrayList<DataItem> data) {
		FileWriter fileWriter = null;
		File desktop = new File(System.getProperty("user.home") + "/Desktop", fileName);
		try {
			fileWriter = new FileWriter(desktop);
			
			//CSV header
			fileWriter.append(FILE_HEADER.toString());
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			for(int i = 0; i < data.size(); i++) {
				fileWriter.append(String.valueOf(data.get(i).obsNum));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.get(i).claimID);
                fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.get(i).lineNum);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(data.get(i).amount));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(data.get(i).stratumNum));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				
				fileWriter.flush();
				fileWriter.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void writeStatFile(String fileName, ArrayList<Stratum> strata) {
		FileWriter fileWriter = null;
		File desktop = new File(System.getProperty("user.home") + "/Desktop", fileName);
		try {
			fileWriter = new FileWriter(desktop);
			
			//CSV header
			fileWriter.append(FILE_HEADER2.toString());
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			System.out.println(strata);
			for(int i = 1; i < 21; i++) {
				fileWriter.append(String.valueOf(i));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getLowerBound()));
                fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getUpperBound()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getStratumNumClaims()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getStratumTotalAmount()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getStratumSampleSize()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getFirstClaimPos()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(strata.get(i).getLastClaimPos()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				
				fileWriter.flush();
				fileWriter.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isWin() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			return true;
		}else {
			return false;
		}
	}

    
}
