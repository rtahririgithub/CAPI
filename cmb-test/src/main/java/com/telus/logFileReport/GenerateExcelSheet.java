package com.telus.logFileReport;

import java.io.*;
import java.util.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

public class GenerateExcelSheet {

	FileWriter fileWriter = null;
	BufferedWriter bufferWriter = null;
	static FileInputStream fstream = null;
	File file;
	static String SOURCE_PATH;
	String RESULTS_PATH;
	int cellStart = 0;
	String envName= null;
	static Map<String,String> methodAndSPNames = new HashMap<String,String>();
	boolean resetRowNumber =  true;
	private void readConfiguration() {
		// reading configurations from property file
		Properties pro = new Properties();
		try {
			//file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/accountejb_monitoringlogfiles.properties");
			file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/subscriberejb_monitoringlogfiles.properties");
			//file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/referenceejb_monitoringlogfiles.properties");

			System.out.println("PROPERTY FILE PATH" + file.getCanonicalPath());
			pro.load(new FileInputStream(file));
			SOURCE_PATH = pro.getProperty("SourcePath");
			RESULTS_PATH = pro.getProperty("DestinationPathforExcel");
			if (SOURCE_PATH.contains("ST101A_INLINE")) {
				cellStart = 0;
				envName = "ST101A_INLINE";
			} else if (SOURCE_PATH.contains("PRA_LOGS_9I")) {
				cellStart = 8;
				envName = "PR-A-Oracle9i";
			} else if (SOURCE_PATH.contains("ST")) {
				cellStart = 16;
				envName = "PROD-A";	
			}

		} catch (Exception e) {
			System.out.println("ERROR OCCRED WHILE READING THE LOGS PROPERTIES FILE"+ e.getMessage());
		}
	}
	
	private void readStoredProcedureAndMethodNames() throws IOException{
		//fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/accountejb_methods.properties");
		//fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/subscriberejb_methods.properties");
		fstream = new FileInputStream("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/special_perf_methods.properties");

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			String[] values = strLine.trim().split(",");
			if(values.length!=2){
				 System.out.println("readStoredProcedureAndMethodNames error , incorrect method and sp name provided");
					return ;
			}
			methodAndSPNames.put(values[0].trim(), values[1].trim());
		}
	}
	
	public static void main(String[] args) throws Exception {
		try {
			
			int avgOracle12Cell = 13;
			int avgOracle9iCell = 10;
			int greatest = avgOracle12Cell > avgOracle9iCell ? avgOracle12Cell : avgOracle9iCell;
			int perDiffInt = ((avgOracle9iCell - avgOracle12Cell)*100)/greatest;
			System.out.println("perDiffInt"+perDiffInt);
			
			GenerateExcelSheet sheetGeneration = new GenerateExcelSheet();
			sheetGeneration.readConfiguration();
			sheetGeneration.readStoredProcedureAndMethodNames();
			File folder = new File(SOURCE_PATH);
			File[] listOfSubFolders = folder.listFiles();
			for (int i = 0; i < listOfSubFolders.length; i++) {
				 File[] listOfFiles = listOfSubFolders[i].listFiles();
				for (int j = 0; j < listOfFiles.length; j++) {
					File file = listOfFiles[j];
					// only the read the text files from folder and exclude"perfstats *.text" files
					if (file.isFile() && file.getName().endsWith(".txt") && !file.getName().contains("perfstats") && !file.getName().contains("retrieveSubscriberListByPhoneNumbers")) {
						System.out.println("Processing the file "+ file.getName());
						sheetGeneration.updateToExcelSheet(file.getPath(),file.getName()); 
					}
				}
			}

		} catch (Exception e) {
			System.out.println("************ERROR OCCRED WHILE PROCESSING THE FILES************ ");
			e.printStackTrace();
		}
	}
   
	
   @SuppressWarnings("deprecation")
   public void  updateToExcelSheet(String path,String fileName) throws IOException {
		try {
			String[] pathValues = path.split(":?\\\\");
			//Read the existing excel file and try to update it if it already exists.
			String results_path = RESULTS_PATH + "/" + pathValues[pathValues.length-3]+"PerReport.xls";
			fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			FileInputStream fin = null;
			HSSFWorkbook workbook = null;
			try {				
				// read existing workbook if it is exists
				fin = new FileInputStream(results_path);
				workbook = new HSSFWorkbook(fin);
			} catch (FileNotFoundException fne) {
				// create a new workbook if it is NOT exists
				workbook = new HSSFWorkbook();
			}
			int rownum = 0;
			HSSFSheet spreadsheet = null;
			// search in workbook if the sheet already exists with the file name.
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet.getSheetName().equals(fileName)) {
					spreadsheet = workbook.getSheetAt(i);
					break;
				}
			}
			
			// create a new sheet if the sheet NOT exists with the file name.
			if (spreadsheet == null) {
				spreadsheet = workbook.createSheet(fileName);
			} else{
				rownum = spreadsheet.getLastRowNum() +1;
				for (int j = 1; j <= spreadsheet.getLastRowNum(); j++) {
					HSSFRow row = spreadsheet.getRow(j);
					if(envName.equalsIgnoreCase("ST101A_INLINE")){
						if(row.getCell(0)==null){
							rownum = j-1;
							break;
						}
					}else if(envName.equalsIgnoreCase("PR-A-Oracle9i")){
						if(row.getCell(8)== null || row.getCell(8).getCellType()== HSSFCell.CELL_TYPE_BLANK){
							rownum = j-1;
							break;
						}
					}else if(envName.equalsIgnoreCase("ST101B")){
						if(row.getCell(16)== null || row.getCell(16).getCellType()== HSSFCell.CELL_TYPE_BLANK){
							rownum = j-1;
							break;
						}
					}

				}
			}
			
			
			spreadsheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 6)); //(fromRow, toRow, fromColumn,toColumn)
			spreadsheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 14)); //(fromRow, toRow, fromColumn,toColumn)
			spreadsheet.addMergedRegion(new CellRangeAddress(0, 1, 16, 22)); //(fromRow, toRow, fromColumn,toColumn)

			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				String afterReplcae = strLine.replace("|", "}");
				String[] values = afterReplcae.split("}");
//				if (cellStart == 0) {
//					if (!(values[1].contains("08.2018"))) {
//						System.out.println("excluding the record.." + strLine);
//						continue;
//					}
//				} else if (cellStart == 8) {
//					if (!values[1].contains("05.2018")) {
//						System.out.println("excluding the record.." + strLine);
//						continue;
//					}
//				}

				if(rownum>30000){
					break;
				}
				if (rownum > 2 && values[4] != null && Integer.parseInt(values[5]) < 10) {
					// Don't take < 10 invocations  
				} else {
					fileName = fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
					createOrUpdateSheet(spreadsheet, values,pathValues[pathValues.length - 2],methodAndSPNames.get(fileName.trim()), workbook,rownum);
					rownum++;
				}
			}
			// write it to excel sheet and close it.
			FileOutputStream fileout = new FileOutputStream(new File(results_path));
			workbook.write(fileout);
			fileout.flush();
			fileout.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private HSSFCellStyle getStyleColor(HSSFWorkbook workbook,int perDiff) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		if(perDiff>0){
			cellStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
			cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		}else{
			cellStyle.setFillBackgroundColor(HSSFColor.RED.index);
			cellStyle.setFillForegroundColor(HSSFColor.RED.index);
		}
		return cellStyle;
	}
	
	public void createOrUpdateSheet(HSSFSheet spreadsheet, String[] values,String hostAndNodeName,String storedProcedureName,HSSFWorkbook workbook, int rownum) throws IOException {
			HSSFRow row;
		// This data needs to be written (Object[])
		Map<String, Object[]> rowObject = new TreeMap<String, Object[]>();
		// Add the header if it first row.
		Object obj = null;
		if(rownum < 2){
			String headerValue = "\t\t"+envName +"    |   " +storedProcedureName;
			rowObject.put("0", new Object[]{headerValue});
		}
		else if (rownum == 2) {
			 if(cellStart == 8){
				 rowObject.put("1", new Object[]{"hostAndNodeName","startTime", "endTime","MinimumInvocationTime", "MaximumInvocationTime","AverageInvocationTime", "TotalNumberOfInvocations","percentage improvement"});	 
			 }else {
				 rowObject.put("1", new Object[]{"hostAndNodeName","startTime", "endTime","MinimumInvocationTime", "MaximumInvocationTime","AverageInvocationTime", "TotalNumberOfInvocations"});
			 }
		}  
		else if (rownum > 2) { 
			rowObject.put("2", new Object[]{hostAndNodeName,values[1].trim(), values[2].trim(),values[11].trim(), values[12].trim(), values[4].trim(),values[5].trim(),obj});
		}

		// Iterate over data and write to sheet
		Set<String> keyid = rowObject.keySet();
		for (String key : keyid) {
			// Update the existing row if exists.
			row = spreadsheet.getRow(rownum);
			if (row == null) {
				// Create a new row if it exists.
				row = spreadsheet.createRow(rownum);
			}
			Object[] objectArr = rowObject.get(key);
			int perDiffInt = 0;
			// Calculate the Percentage Improvement 9i vs 12c.
			if (cellStart == 8 && rownum > 2 && row.getCell(5) != null && values[4] != null) {
				int avgOracle12Cell = Integer.parseInt(row.getCell(5).getStringCellValue());
				int avgOracle9iCell = Integer.parseInt(values[4]);
				int greatest = avgOracle12Cell > avgOracle9iCell ? avgOracle12Cell : avgOracle9iCell;
				perDiffInt = ((avgOracle9iCell - avgOracle12Cell)*100)/greatest;
				obj = (Object) Integer.toString(perDiffInt);
				objectArr[7] = obj;
			}
			
			
			int cellid = cellStart;
			for (int i = 0; i < objectArr.length; i++) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String) objectArr[i]);
				if (i == 7 && perDiffInt != 0) {
					cell.setCellStyle(getStyleColor(workbook, perDiffInt));
				}
			}
		}
	}
}