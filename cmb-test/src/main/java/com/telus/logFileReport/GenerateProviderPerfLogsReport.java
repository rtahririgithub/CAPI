package com.telus.logFileReport;
import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class GenerateProviderPerfLogsReport {  
	
	FileWriter fileWriter = null;
	BufferedWriter bufferWriter = null;
	FileInputStream fstream = null;
	static File file;
	static String FILTER_CRITERIA;
	static String SOURCE_PATH;
	static String EXCEL_RESULTS_PATH;
	
     /* Step -3: Define logical Map to consume CSV file data into excel */
	 static TreeMap<String, Object[]> excel_data   = new TreeMap<String, Object[]>();
	 

	
	 Object []  excelFileHeader = { "Date"," StartTime "," EndTime ", "GMTOffset", "avgResponseTime(ms)" ,"ExecutionCount", " HostName ", "ApplicationId ","MethodSignature"};
	
	static{
		// reading configurations from property file
		Properties pro = new Properties();
		try {
			file = new File("C:/TFS/CMB/main_dev/cmb-test/src/main/java/com/telus/logFileReport/apiusage.properties.");
			pro.load(new FileInputStream(file));
			FILTER_CRITERIA = pro.getProperty("filtercriteria");
			SOURCE_PATH = pro.getProperty("sourcefilesPath");
			EXCEL_RESULTS_PATH = pro.getProperty("excelOutputPath");
		} catch (Exception e) {
			System.out.println("exception occured while reading the properties file.."+ e.getStackTrace());
		}
	}

	
	
	public void writeSetToExcelSheet(Map<String, Object[]> excel_data ,String currentFilter ) throws IOException{
		
               
               Set<String> keyset = excel_data.keySet();
               int rownum = 0;
               
               
               /* Step -1  : Define POI Spreadsheet objects */ 
       
               HSSFWorkbook new_workbook = new HSSFWorkbook(); //create a blank workbook object
               HSSFSheet sheet = new_workbook.createSheet(currentFilter);  
               
               
               /* Step -2  : Create Sheet Rows with inputs values */ 
               
               for (String key : keyset) { 
            	  
            	   if(rownum == 0){
            		   // Create the excel header if it is first row.
            		   int cellnum = 0;
            		   Row row = sheet.createRow(rownum++);
            		   
            		   for (Object obj : excelFileHeader) {
                           Cell cell = row.createCell(cellnum++);
                           if(obj instanceof Double)
                        	   
                                   cell.setCellValue((Double)obj);
                           else
                                   cell.setCellValue((String)obj);
                           }
            	   }
            	   
                       Row row = sheet.createRow(rownum++);
                       Object [] objArr = excel_data.get(key);
                       
                       int cellnum = 0;
                       for (Object obj : objArr) {
                               Cell cell = row.createCell(cellnum++);
                               if(obj instanceof Double)
                                       cell.setCellValue((Double)obj);
                               else
                                       cell.setCellValue((String)obj);
                               }
                       
                      
			
		
               }
               
          	   FileOutputStream output_file = new FileOutputStream(new File(EXCEL_RESULTS_PATH + currentFilter +".xls"),true); //create XLS file
               new_workbook.write(output_file);
               output_file.flush();
               output_file.close(); 
         }

	
	
        public void  filterRecords (String inputCSVFile,String fileName,String currentFilter) throws IOException {
                
                /* Step -1 : Read input CSV file in Java */
               
                FileInputStream  fstream = new FileInputStream(inputCSVFile);
    			DataInputStream in = new DataInputStream(fstream);
    			BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String nextLine0;   
                
                int lnNum = excel_data.keySet().size()+1; 
                
                /* Step -2 : Populate data into logical Map */
                
                while ((nextLine0 = br.readLine()) != null) {
                	boolean filter = isCurrentLineMathesWithFilter(nextLine0, new String [] { currentFilter});
                	if (filter == false) {
                		continue;
                	}
                		
                	String[] nextLine = nextLine0.split("\\|");
                	lnNum++;
                	excel_data.put(Integer.toString(lnNum), new Object[] { nextLine[0],nextLine[1], nextLine[2], nextLine[3], nextLine[4],nextLine[5], nextLine[6], nextLine[7], nextLine[8]});
		}
               
              
                System.out.println(excel_data.size());
               
               
        }
        
        private boolean isCurrentLineMathesWithFilter(String cuurrentLine, String[] filtercriteria) {
    		for (String filter : filtercriteria) {
    			if (cuurrentLine.contains(filter)) {
    				return true;
    			}

    		}
    		return false;
    	}
        
        public static void main(String[] args) throws IOException {
        	
        	System.out.println(" Begin API provider usage/performance report generation");
        	
        	File csvdirectory = new File(SOURCE_PATH);
        	File[] csvfileArray = csvdirectory.listFiles();

        	String[] filtercriteria = FILTER_CRITERIA.split(",");
        	GenerateProviderPerfLogsReport csv_xls= new GenerateProviderPerfLogsReport();
        
        	for (String filter : filtercriteria) {
        		
        		for (File csvFile : csvfileArray) {
    				File[] files = csvFile.listFiles();
    				for (int i = 0; i < files.length; i++) {
    				    System.out.println("processing file....... "+ files[i].getAbsolutePath() +" for filter " + filter );
    				    csv_xls.filterRecords(files[i].getAbsolutePath(),files[i].getName(),filter);
    				}
    			}
    			
        		
			int size = excel_data.keySet().size();
			
			System.out.println("     ....  : >  total no.of  records  found  for given criteria : "+  filter   +"  is  : " + size +"  .... .:<     ");

			int mapSize = excel_data.size();
			int counter = 0;
			int parsedSize = 0;
			int splitBy = 60000;
			int fileNumber = 1;

			TreeMap<String, Object[]> smallMap = new TreeMap<String, Object[]>();
			
			// split the maps into smaller ones to fit into excel sheet..
			
			for (Map.Entry<String, Object[]> entry : excel_data.entrySet()) {
				
				parsedSize++;
				counter++;
				
				smallMap.put(entry.getKey(), entry.getValue());
				
				if (parsedSize == splitBy || counter == mapSize) {
					
					csv_xls.writeSetToExcelSheet(smallMap, filter+ " file number - " + fileNumber);
					System.out.println(" @@@@@@@@ Excel sheet generation completed successfully with the"+ filter+ " file number - " + fileNumber+"  for rows "+parsedSize+" @@@@@@@");
					smallMap.clear();
					parsedSize = 0;
					fileNumber++;
					
				}
			}
			
			// clear the old set which finishes the excel sheet generation.
			excel_data.clear();
			
		}
        	System.out.println(" End API provider usage / performance report generation");
        }
        
}
