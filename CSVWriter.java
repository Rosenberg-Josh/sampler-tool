package SamplerPackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class CSVWriter {
	private static final String COMMA_DELIMITER = ",";
	
	private static final String NEW_LINE_SEPARATOR = "\n";
        
	private static final String SAMPLE_HEADER = "ObservationNumber,Stratum_Number"; //Header for sample output with standard input
	private static final String STAT_HEADER = "Stratum_Number,Lower_Bound,Upper_Bound,Size,Amount,Sample_Size,First_Claim_Position,Last_Claim_Position"; //Header for strata info and stats

	public static File writeSampleFile(String fileName, ArrayList<DataItem> data) { //Writes sample data to file and returns file
		FileWriter fileWriter = null;
		File desktop = new File(fileName);
		try {
			fileWriter = new FileWriter(desktop);
			
			//CSV header
			fileWriter.append(SAMPLE_HEADER.toString());
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			for(int i = 0; i < data.size(); i++) {
				fileWriter.append(String.valueOf(data.get(i).obsNum));
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
		return desktop;
	}
	
	public static File writeStatFile(String fileName, ArrayList<Stratum> strata) {
		FileWriter fileWriter = null;
		File desktop = new File(fileName);
		try {
			fileWriter = new FileWriter(desktop);
			
			//CSV header
			fileWriter.append(STAT_HEADER.toString());
			
			fileWriter.append(NEW_LINE_SEPARATOR);
			System.out.println(strata);
			for(int i = 0; i < 22; i++) {
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
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("Population Mean");
			fileWriter.append(COMMA_DELIMITER);
			double x = SamplerMainClass.getPopMean(SamplerMainClass.portClaimsData);
			fileWriter.append(String.valueOf(x));
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("Weighted Sample Mean");
			fileWriter.append(COMMA_DELIMITER);
			double y = SamplerMainClass.getWeightedSampleMean(SamplerMainClass.sampleClaims, SamplerMainClass.finStrata);
			fileWriter.append(String.valueOf(y));
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("Absolute Difference");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(SamplerMainClass.getAbsDiff(x, y)));
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("Percentage Difference");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(String.valueOf(SamplerMainClass.getPerDiff(SamplerMainClass.getAbsDiff(x, y), x)));
			fileWriter.append(NEW_LINE_SEPARATOR);
				
			
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
		return desktop;
	}
    
}
