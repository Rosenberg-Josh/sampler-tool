package SamplerPackage;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelWriter {
	
	public static void writeToTemplate(File clientDir, ArrayList<DataItem> finalSample, ArrayList<Stratum> finalStrata){
		
		String fileName = "S:\\Models\\AuditSampling\\Audit Inventory and Results for HCA.xlsm";
		String outputName = clientDir + "\\" + "Audit Inventory and Results for HCA.xlsm";
		File file = new File(fileName);
		if(file.exists()) {
			try {
	
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
				
				
	            FileOutputStream outputStream = new FileOutputStream(outputName);
	            workbook.write(outputStream);
	            workbook.close();
	            outputStream.close();
				xlsxFile.close();
	
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}System.out.println("done");
		}else {
			System.out.println("No excel template found");
		}
	}
	
	public static void writeSample(ArrayList<DataItem> sampleClaims, ArrayList<Stratum> finalStrata, String clientName, File clientDir) {
		
		String templateName = "S:\\Models\\AuditSampling\\Sample_Template.xlsx";
		File templateFile = new File(templateName);
		//File origData = new File(origDataFileName);
		//if(templateFile.exists() && origData.exists()) {
			if(templateFile.exists()) {
			try {
				//Write out sample
				FileInputStream xlsxFile = new FileInputStream(templateFile);
				String outputName = clientDir + "\\" + "Audit results.xlsx";
				XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
				XSSFSheet inputSheet = workbook.getSheetAt(0); //Illegal access		
				XSSFCell cell = null;
				File origData = new File(SamplerMainClass.currWindow.getFileNameInput().getText());
				
				ArrayList<String> headers = new ArrayList<>();
				
				if(origData.getName().contains(".xl")) {
					XSSFWorkbook originalWB = new XSSFWorkbook(origData);
					XSSFSheet fSheet = originalWB.getSheetAt(0);
					XSSFRow headRow = fSheet.getRow(0);
					for(int i = 0; i < headRow.getLastCellNum(); i++) {
						headers.add(headRow.getCell(i).getStringCellValue());
					}
					
				}else if (origData.getName().contains(".csv")) {
					Scanner cScan = new Scanner(origData);
					cScan.
				}
				
				
				/*
				 * Write out stat info
				 */
				
				XSSFSheet statsheet = workbook.getSheetAt(1);
				for(int i = 0; i < finalStrata.size(); i++) {
					XSSFRow statRow = statsheet.createRow(i + 1);
					
					statRow.createCell(0).setCellValue(i);
					statRow.createCell(1).setCellValue(finalStrata.get(i).lowerBound);
					statRow.createCell(2).setCellValue(finalStrata.get(i).upperBound);
					statRow.createCell(3).setCellValue(finalStrata.get(i).stratumNumClaims);
					statRow.createCell(4).setCellValue(finalStrata.get(i).stratumTotalAmount);
					statRow.createCell(5).setCellValue(finalStrata.get(i).stratumSampleSize);
					statRow.createCell(6).setCellValue(finalStrata.get(i).firstClaimPos);
					statRow.createCell(7).setCellValue(finalStrata.get(i).lastClaimPos);

				}
				
				int validityRow = finalStrata.size() + 3;
				XSSFRow meanRow = statsheet.createRow(validityRow);
				meanRow.createCell(0).setCellValue("Population Mean");
				meanRow.createCell(1).setCellValue(SamplerMainClass.popMean);
				
				XSSFRow sMeanRow = statsheet.createRow(validityRow + 1);
				meanRow.createCell(0).setCellValue("Weighted Sample Mean");
				meanRow.createCell(1).setCellValue(SamplerMainClass.sampleMean);
				
				XSSFRow absRow = statsheet.createRow(validityRow + 2);
				meanRow.createCell(0).setCellValue("Absolute Difference");
				meanRow.createCell(1).setCellValue(SamplerMainClass.gAbsDiff);
				
				XSSFRow perRow = statsheet.createRow(validityRow + 3);
				meanRow.createCell(0).setCellValue("Percentage Difference");
				meanRow.createCell(1).setCellValue(SamplerMainClass.gPerDiff);
				
	            FileOutputStream outputStream = new FileOutputStream(outputName);
	            workbook.write(outputStream);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
