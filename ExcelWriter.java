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

/*
 *  This class contains the methods to populate excel templates with the data obtained from SamplerMainClass and CreateSample classes. 
 *  Note that this navitages the file system on the office's specific public drive, from where it pulls the model. If this program is to be used on 
 *  a machine outside of this network, the filepath of the templates will need to be changed.
 */
public class ExcelWriter {
	
	
	/*
	 * Method to write in the sample information into the 'Audit Inventory and Results for HCA' template.
	 */
	public static void writeToTemplate(File clientDir, ArrayList<DataItem> finalSample, ArrayList<Stratum> finalStrata, String clientName){
		
		String fileName = "S:\\Models\\AuditSampling\\Audit Inventory and Results for HCA.xlsm"; //Location of Inventory template sheet, not applicable on out of network machines
		String outputName = clientDir + "\\" + "Audit Inventory and Results for " + clientName + ".xlsm"; //Location of populated template, where clientDir is specified by used in UI
		File file = new File(fileName);
		if(file.exists()) {
			try {
	
				FileInputStream xlsxFile = new FileInputStream(file);
				XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
				XSSFSheet inputSheet = workbook.getSheetAt(0); //Illegal access, possible that this can be ignored but a further probe would be useful		
				XSSFCell cell = null;
				
				for(int i = 0; i < finalSample.size(); i++) { //Loop will populate initial inventory sheet of template
					XSSFRow row = inputSheet.getRow(i + 12);
					cell = row.getCell(2);
					cell.setCellValue(finalSample.get(i).stratumNum);
	
				}
				inputSheet = workbook.getSheetAt(2);
				for(int i = 0; i < finalStrata.size(); i++) { //Loop will populate table # 2 with neccessary data, which will copied to other tables when user eneter ctrl + alt + F9
					XSSFRow row = inputSheet.getRow(i + 10);
					cell = row.getCell(5);
					cell.setCellValue(finalStrata.get(i).stratumNumClaims);
					cell = row.getCell(7);
					cell.setCellValue(finalStrata.get(i).stratumTotalAmount);
					
				}
				
				
	            FileOutputStream outputStream = new FileOutputStream(outputName); //Write new template file to destination folder
	            workbook.write(outputStream);
	            workbook.close();
	            outputStream.close();
				xlsxFile.close();
	
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}System.out.println("Template created without errors");
		}else {
			System.out.println("No excel template found"); //This will happen if machine is not on networks
		}
	}
	
	/*
	 * This method will populate the "Sample_Template.xlsx" template with the sample (obsNum and Stratum number) on sheet 1 and sample stats on sheet 2
	 */

	public static void writeSample(ArrayList<DataItem> sampleClaims, ArrayList<Stratum> finalStrata, String clientName, File clientDir) {
		
		String templateName = "S:\\Models\\AuditSampling\\Sample_Template.xlsx"; //File path of template file, must be done on in-network computer
		File templateFile = new File(templateName);
		if(templateFile.exists()) {
			try {
				//Write out sample
				if(clientName.toLowerCase().endsWith(".csv")) {
					clientName = clientName.substring(0, clientName.length() - 3);
				}
				
			
				FileInputStream xlsxFile = new FileInputStream(templateFile);
				String outputName = clientDir + "\\" + "Audit sample for " + clientName + ".xlsx"; //Build of file name for populated template
				XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile);
				XSSFSheet inputSheet = workbook.getSheetAt(0); //Illegal access		
				File origData = new File(SamplerMainClass.currWindow.getFileNameInput().getText());
				ArrayList<String> headers = new ArrayList<>();
				
				if(origData.getName().contains(".xl")) { //original Data was in xl format
					XSSFWorkbook orWorkbook = new XSSFWorkbook(origData);
					XSSFSheet orInputSheet = orWorkbook.getSheetAt(0);
					System.out.println("XL");
					XSSFWorkbook originalWB = new XSSFWorkbook(origData);
					XSSFSheet fSheet = originalWB.getSheetAt(0);
					XSSFRow headRow = fSheet.getRow(0);
					headers.add("ObsNum");
					headers.add("Stratum Num");
					for(int i = 0; i < headRow.getLastCellNum(); i++) {
						headers.add(headRow.getCell(i).getStringCellValue());
					}
					XSSFRow newHead = inputSheet.createRow(0);
					for(int j = 0; j < headers.size(); j++) { //Set header values
						XSSFCell cell = newHead.createCell(j);
						cell.setCellValue(headers.get(j));
					}
					
					for(int i = 0; i < sampleClaims.size(); i++) {
						XSSFRow elementRow = inputSheet.createRow(i + 1);
						XSSFCell cell = elementRow.createCell(0);
						cell.setCellValue(sampleClaims.get(i).obsNum);
						
						XSSFCell sCell = elementRow.createCell(0);
						sCell.setCellValue(sampleClaims.get(i).getStratumNum());
						
					}
					
				}else{ //Original data was in csv format
					XSSFRow headRow = inputSheet.createRow(0);
					int q;
					for(q = 0; q < SampleData.heads.size(); q++) { //Sets the headers in the spreadsheet
						headRow.createCell(q).setCellValue(SampleData.heads.get(q));
					}
					headRow.createCell(q + 1).setCellValue("Stratum Number");
					
					for(int i = 0; i < sampleClaims.size(); i++) { //Write the claim Data;
						XSSFRow dataRow = inputSheet.createRow(i + 1);
						Scanner dataToParse = new Scanner(sampleClaims.get(i).data).useDelimiter(",");
						int j = 0;
						while(dataToParse.hasNext()) {
							XSSFCell dataCell = dataRow.createCell(j);
							dataCell.setCellValue(dataToParse.next());
							j++;
						}
						dataRow.createCell(j).setCellValue(sampleClaims.get(i).stratumNum);
						dataToParse.close();
					}
					
					for(int i = 0; i < inputSheet.getLastRowNum(); i++) {
						inputSheet.autoSizeColumn(i);
					}
					
					/*
					XSSFRow headRow = inputSheet.createRow(0);
					headRow.createCell(0).setCellValue("Observation Num");
					headRow.createCell(1).setCellValue("Stratum Num");
					for(int i = 0; i < sampleClaims.size(); i++) {
						XSSFRow elementRow = inputSheet.createRow(i + 1);
						XSSFCell cell = elementRow.createCell(0);
						cell.setCellValue(sampleClaims.get(i).obsNum);
						
						XSSFCell sCell = elementRow.createCell(1);
						sCell.setCellValue(sampleClaims.get(i).getStratumNum());
						
					}
					*/
				}
				
				
				/*
				 * Write out stat info
				 */
				
				XSSFSheet statsheet = workbook.getSheetAt(1);
				for(int i = 0; i < finalStrata.size(); i++) { //This loop will populate the template with information on each stratum. Headers should already be present
					XSSFRow statRow = statsheet.createRow(i + 1);
					
					statRow.createCell(0).setCellValue(i);
					statRow.createCell(1).setCellValue(finalStrata.get(i).lowerBound);
					statRow.createCell(2).setCellValue(finalStrata.get(i).upperBound);
					statRow.createCell(3).setCellValue(finalStrata.get(i).stratumNumClaims);
					statRow.createCell(4).setCellValue(finalStrata.get(i).stratumTotalAmount);
					statRow.createCell(5).setCellValue(finalStrata.get(i).stratumSampleSize);
					statRow.createCell(6).setCellValue(finalStrata.get(i).firstClaimPos);
					statRow.createCell(7).setCellValue(finalStrata.get(i).lastClaimPos);
					statRow.createCell(8).setCellValue(SamplerMainClass.roundToTwo(finalStrata.get(i).stratumMean));
					statRow.createCell(9).setCellValue(SamplerMainClass.roundToTwo(finalStrata.get(i).stratumSD));
					
					

				}
				
				int validityRow = finalStrata.size() + 3;
				XSSFRow meanRow = statsheet.createRow(validityRow);
				
				meanRow.createCell(0).setCellValue("Population Mean");
				meanRow.createCell(1).setCellValue(SamplerMainClass.popMean);
				validityRow++;
				
				XSSFRow sMeanRow = statsheet.createRow(validityRow);
				sMeanRow.createCell(0).setCellValue("Weighted Sample Mean");
				sMeanRow.createCell(1).setCellValue(SamplerMainClass.sampleMean);
				validityRow++;
				
				XSSFRow absRow = statsheet.createRow(validityRow);
				absRow.createCell(0).setCellValue("Absolute Difference");
				absRow.createCell(1).setCellValue(SamplerMainClass.gAbsDiff);
				validityRow++;
				
				XSSFRow perRow = statsheet.createRow(validityRow);
				perRow.createCell(0).setCellValue("Percentage Difference");
				perRow.createCell(1).setCellValue(SamplerMainClass.gPerDiff);
				
				statsheet.autoSizeColumn(0);
				statsheet.autoSizeColumn(1);
				
	            FileOutputStream outputStream = new FileOutputStream(outputName);
	            workbook.write(outputStream);
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
