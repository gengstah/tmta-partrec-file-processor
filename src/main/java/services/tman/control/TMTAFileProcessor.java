package services.tman.control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class is the implementation class
 * of {@link services.tman.control.TMANFileProcessor}
 * for <b>TMTA file reports</b>
 * 
 * @author Gerard
 * @version 1.0.0
 */
public class TMTAFileProcessor extends TMANFileProcessorSupport {
	private static final Logger logger = Logger.getLogger(TMTAFileProcessor.class);
	
	/**
	 * Default constructor
	 */
	public TMTAFileProcessor() { }
	
	@Override
	void processFirstLine(File file) throws IOException {
		String []firstLineArray = getUtil().parseToArrayByComma(getReader().readLine());
		String firstLine = firstLineArray[firstLineArray.length - 1].trim();
		
		if(getUtil().isFirstLineDataMarkerInvalid(firstLine)) {
			getReader().close();
			throw new IllegalArgumentException(
					"Invalid data marker on line 1 of file (" + file.getName() + "): "
							+ firstLine.substring(0, 3) + " Expected: !@#");
		}
		
		if(getUtil().isDateInvalid(firstLine, getDateFormat())) {
			getReader().close();
			throw new IllegalArgumentException("Length of date is invalid on line 1 of file (" + file.getName() + "): "
					+ firstLine.substring(3) + " Expected format: YYYY-MMM-DD");
		}
	}
	
	@Override
	void processReportName(File file) throws IOException {
		String reportName = getReader().readLine();
		
		if(getUtil().isReportNameInvalid(reportName)) {
			getReader().close();
			throw new IllegalArgumentException(
					"Invalid report name on line 2 of file (" + file.getName() + "): "
							+ reportName + " Expected: Report name");
		}
		
		setReportName(getUtil().parseReportName(reportName));
	}
	
	@Override
	void processColumnHeader(File file) throws IOException {
		getColumnHeaders().clear();
		String columnHeadersLine = getReader().readLine();
		String []columnHeadersArray = getUtil().parseToArrayByComma(columnHeadersLine.replace("\"", ""));
		for(String columnHeader : columnHeadersArray)
			if(!columnHeader.equals(" ")) getColumnHeaders().add(columnHeader.trim());
		
		if(isColumnHeaderSizeIsNotEqualToColumnCount()) {
			getReader().close();
			throw new IllegalArgumentException(
					"Invalid number of column headers on line 4 of file (" + file.getName() + "): "
							+ columnHeadersLine + " Expected number of columns: " + getColumnCount());
		}
	}
	
	void processActualData(File file) throws IOException {
		getActualDataRecordList().clear();
		
		int actualDataLineCounter = 5;
		String actualDataRecord;
		
		while((actualDataRecord = getReader().readLine()) != null) {
			actualDataRecord = actualDataRecord.substring(2);
			
			if(getUtil().isRecordCountLine(actualDataRecord)) {
				processRecordCount(file, actualDataRecord);
				break;
			}
			
			if(getUtil().isActualDataValid(actualDataRecord)) {
				String []actualDataArray = actualDataRecord.split("\",\"");
				int columnHeaderIndex = 0;
				Map<String, Object> properties = new HashMap<String, Object>();
				for(String actualDataValue : actualDataArray) {
					actualDataValue = getUtil().eliminateQuotes(actualDataValue);
					String columnHeader = getColumnHeaders().get(columnHeaderIndex);
					
					getConverter().setTableName(getTableName())
						.setColumnHeader(columnHeader);
	
					properties.put(columnHeader, getConverter().convert(actualDataValue.trim()));
					columnHeaderIndex++;
				}
				
				logger.info("Saving in " + getTableName() + ": " + properties);
				getDao().addReport(properties, getTableName());
				getActualDataRecordList().add(actualDataRecord);
			} else {
				logger.error("Invalid actual data marker on line " + actualDataLineCounter + " of file (" + file.getName() + "): "
						+ actualDataRecord.substring(1, 4) + " Expected: #$%");
			}
			
			actualDataLineCounter++;
		}
	}
}