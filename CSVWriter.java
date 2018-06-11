package SamplerPackage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVWriter {
	private static final String COMMA_DELIMITER = ",";
	
	private static final String NEW_LINE_SEPARATOR = "\n";
        
	private static final String FILE_HEADER = "ObservationNumber,Claim_Number,Line_Number,Amount";

	public static void writeCsvFile(String fileName, ArrayList<DataItem> data) {
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
