
package services.tman.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import services.tman.dao.TerritoryManagerDao;
import services.tman.domain.DataTypeConverter;

/**
 * 
 * Territory Maintenance Territory Analysis (TMTA)
 * 
 * This class is the processor of a TMTA File
 * 
 * @author Gerard Delas Armas
 * @version 1.0.0
 * @since March 2013
 */
public class TMTAFileProcessor {
	private static final Logger logger = Logger.getLogger(TMTAFileProcessor.class);
	private BufferedReader reader;
	private int columnCount;
	private String reportName;
	private List<String> columnHeaders;
	private List<String> actualDataRecordList;
	private int recordCount;
	
	private TerritoryManagerDao dao;
	private Properties dataDictionary;
	private TMTAFileProcessorUtil util;
	
	public TMTAFileProcessor() { 
		columnHeaders = new ArrayList<String>();
		actualDataRecordList = new ArrayList<String>();
	}
	
	int getColumnCount() {
		return columnCount;
	}
	
	String getReportName() {
		return reportName;
	}
	
	List<String> getColumnHeaders() {
		return columnHeaders;
	}
	
	int getRecordCount() {
		return recordCount;
	}
	
	List<String> getActualDataRecordList() {
		return actualDataRecordList;
	}
	
	void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	/**
	 * Set the data access object
	 * 
	 * @param dao The Territory Manager Data Access Object
	 */
	public void setDao(TerritoryManagerDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Set the data dictionary
	 * 
	 * @param dataDictionary The data dictionary for the column definition of
	 * the table to be used
	 */
	public void setDataDictionary(Properties dataDictionary) {
		this.dataDictionary = dataDictionary;
	}
	
	/**
	 * Set the utility class
	 * 
	 * @param util The utility that will be used by this class
	 */
	public void setUtil(TMTAFileProcessorUtil util) {
		this.util = util;
	}

	/**
	 * Initialize the file reader with the file that will be processed
	 * 
	 * @param file The file that will be processed
	 * @throws FileNotFoundException
	 */
	public void initReader(File file) throws FileNotFoundException {
		FileReader fileReader = new FileReader(file);
		reader = new BufferedReader(fileReader);
	}
	
	/**
	 * 
	 * This method is the main method that processes the
	 * <code>TMTA flat file</code>
	 * 
	 * @param file The TMTA File object to be processed
	 * @throws IOException When this method encountered a problem in IO
	 */
	public void process(File file) throws IOException {
		processFirstLine(file);
		processReportName(file);
		processColumnCount();
		processColumnHeader(file);
		processActualData(file);
		reader.close();
	}

	private void processFirstLine(File file) throws IOException {
		String []firstLineArray = util.parseToArrayByComma(reader.readLine());
		String firstLine = firstLineArray[firstLineArray.length - 1].trim();
		
		if(util.isFirstLineDataMarkerInvalid(firstLine)) {
			reader.close();
			throw new IllegalArgumentException(
					"Invalid data marker on line 1 of file (" + file.getName() + "): "
							+ firstLine.substring(0, 3) + " Expected: !@#");
		}
		
		if(util.isDateInvalid(firstLine)) {
			reader.close();
			throw new IllegalArgumentException("Length of date is invalid on line 1 of file (" + file.getName() + "): "
					+ firstLine.substring(3) + " Expected format: YYYY-MMM-DD");
		}
	}
	
	private void processReportName(File file) throws IOException {
		String reportName = reader.readLine();
		
		if(util.isReportNameInvalid(reportName)) {
			reader.close();
			throw new IllegalArgumentException(
					"Invalid report name on line 2 of file (" + file.getName() + "): "
							+ reportName + " Expected: Report name");
		}
		
		this.reportName = util.parseReportName(reportName);
	}
	
	private void processColumnCount() throws IOException {
		String columnCount = reader.readLine();
		this.columnCount = util.parseDataCount(columnCount);
	}
	
	private void processColumnHeader(File file) throws IOException {
		columnHeaders.clear();
		String columnHeadersLine = reader.readLine();
		String []columnHeadersArray = util.parseToArrayByComma(columnHeadersLine);
		for(String columnHeader : columnHeadersArray)
			if(!columnHeader.equals(" ")) columnHeaders.add(columnHeader.trim().replace("\"", ""));
		
		if(isColumnHeaderSizeIsNotEqualToColumnCount()) {
			reader.close();
			throw new IllegalArgumentException(
					"Invalid number of column headers on line 4 of file (" + file.getName() + "): "
							+ columnHeadersLine + " Expected number of columns: " + getColumnCount());
		}
	}
	
	private boolean isColumnHeaderSizeIsNotEqualToColumnCount() {
		return columnHeaders.size() != getColumnCount();
	}
	
	private void processActualData(File file) throws IOException {
		actualDataRecordList.clear();
		String fileExtension = util.getFileExtension(file);
		String tableName = util.getTableName(fileExtension);
		
		int actualDataLineCounter = 5;
		String actualDataRecord;
		
		while((actualDataRecord = reader.readLine()) != null) {
			actualDataRecord = actualDataRecord.substring(2);
			
			if(util.isRecordCountLine(actualDataRecord)) {
				processRecordCount(file, actualDataRecord);
				break;
			}
			
			if(util.isActualDataValid(actualDataRecord)) {
				String []actualDataArray = actualDataRecord.split("\",\"");
				int columnHeaderIndex = 0;
				Map<String, Object> properties = new HashMap<String, Object>();
				for(String actualDataValue : actualDataArray) {
					actualDataValue = util.eliminateQuotes(actualDataValue);
					String columnHeader = columnHeaders.get(columnHeaderIndex);
					
					DataTypeConverter converter = new DataTypeConverter(dataDictionary)
														.setTableName(tableName).setColumnHeader(columnHeader)
														.setFileName(file.getName());
					
					properties.put(columnHeader, converter.convertToProperDataType(actualDataValue));
					columnHeaderIndex++;
				}
				
				logger.info("Saving in " + tableName + ": " + properties);
				dao.addReport(properties, tableName);
				actualDataRecordList.add(actualDataRecord);
			} else {
				logger.error("Invalid actual data marker on line " + actualDataLineCounter + " of file (" + file.getName() + "): "
						+ actualDataRecord.substring(1, 4) + " Expected: #$%");
			}
			
			actualDataLineCounter++;
		}
	}
	
	private void processRecordCount(File file, String actualDataRecord) throws IOException {
		recordCount = util.parseDataCount(actualDataRecord);
		if(isActualDataCountEqualToRecordCount()) {
			reader.close();
			throw new IllegalArgumentException(
					"Invalid number of actual data of file (" + file.getName() + "): " + actualDataRecordList.size()
							+ " Expected number of actual data: " + getRecordCount());
		}
	}
	
	private boolean isActualDataCountEqualToRecordCount() {
		return actualDataRecordList.size() != getRecordCount();
	}
}