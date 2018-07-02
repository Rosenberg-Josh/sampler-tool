package SamplerPackage;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	
	public static void writeToTemplate(File clientDir, ArrayList<DataItem> finalSample, ArrayList<Stratum> finalStrata){
		
		String fileName = clientDir + "\\" + "Audit Inventory and Results for HCA.xlsm";
		try {
			File file = new File(fileName);
			FileInputStream xlsxFile = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
			XSSFSheet inputSheet = workbook.getSheetAt(0); //Illegal access		
			XSSFCell cell = null;
			
			for(int i = 0; i < finalSample.size(); i++) {
				XSSFRow row = inputSheet.getRow(i + 12);
				cell = row.getCell(2);
				cell.setCellValue(finalSample.get(i).stratumNum);

			}
			inputSheet = workbook.getSheetAt(2);
			for(int i = 0; i < finalStrata.size(); i++) {
				XSSFRow row = inputSheet.getRow(i + 10);
				cell = row.getCell(5);
				cell.setCellValue(finalStrata.get(i).stratumNumClaims);
				cell = row.getCell(7);
				cell.setCellValue(finalStrata.get(i).stratumTotalAmount);
				
			}
			
			
            FileOutputStream outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
			xlsxFile.close();

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}System.out.println("done");
	}
}
