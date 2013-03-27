package services.tman.control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class PartRecFileProcessor extends TMANFileProcessorSupport {
	private static final Logger logger = Logger.getLogger(PartRecFileProcessor.class);
	private Map<String, Boolean> userActions;
	private Map<String, String> columnHeaderToColumnNameMapping;
	private String subReportName;
	
	public PartRecFileProcessor() { }
	
	public String getSubReportName() {
		return subReportName;
	}

	public void setSubReportName(String subReportName) {
		this.subReportName = subReportName;
	}
	
	Map<String, Boolean> getUserActions() {
		return userActions;
	}
	
	public void setUserActions(Map<String, Boolean> userActions) {
		this.userActions = userActions;
	}

	void addUserActions(String userAction, Boolean value) {
		userActions.put(userAction, value);
	}
	
	public Map<String, String> getColumnHeaderToColumnNameMapping() {
		return columnHeaderToColumnNameMapping;
	}

	public void setColumnHeaderToColumnNameMapping(
			Map<String, String> columnHeaderToColumnNameMapping) {
		this.columnHeaderToColumnNameMapping = columnHeaderToColumnNameMapping;
	}

	@Override
	void processFirstLine(File file) throws IOException {
		String firstLine = getReader().readLine();
		
		if(getUtil().isFirstLineDataMarkerInvalid(firstLine)) {
			getReader().close();
			throw new IllegalArgumentException(
					"Invalid data marker on line 1 of file (" + file.getName() + "): "
							+ firstLine.substring(0, 3) + " Expected: !@#");
		}
		
		if(getUtil().isDateInvalid(firstLine, getDateFormat())) {
			getReader().close();
			throw new IllegalArgumentException("Length of date is invalid on line 1 of file (" + file.getName() + "): "
					+ firstLine.substring(3) + " Expected format: MMddyyyy");
		}
	}
	
	@Override
	void processReportName(File file) throws IOException {
		String reportNameLine = getReader().readLine();
		String []parsedReportNameLine = reportNameLine.replace("¹", "").split(",");
		
		int index = 0;
		setReportName(parsedReportNameLine[index++]);
		setSubReportName(parsedReportNameLine[index++]);
		
		if(getReportName() == null || getReportName().trim().equals("")) {
			getReader().close();
			throw new IllegalArgumentException(
					"Invalid report name/sub report name on line 2 of file (" + file.getName() + "): "
							+ getReportName() + " Expected: Report name");
		}
		
		for(int indexLoop = index;indexLoop < parsedReportNameLine.length;indexLoop++) {
			Boolean userActionValue = parsedReportNameLine[indexLoop].split("=")[1].trim().equals("Y") ? Boolean.TRUE : Boolean.FALSE;
			addUserActions(parsedReportNameLine[indexLoop].split("=")[0].trim(), userActionValue);
		}
	}
	
	void processColumnHeader(File file) throws IOException {
		getColumnHeaders().clear();
		String columnHeadersLine = getReader().readLine();
		String []columnHeadersArray = getUtil().parseToArrayByComma(columnHeadersLine.replace("¹", ""));
		for(String columnHeader : columnHeadersArray)
			getColumnHeaders().add(columnHeader.trim());
		
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
			if(getUtil().isRecordCountLine(actualDataRecord)) {
				processRecordCount(file, actualDataRecord);
				break;
			}
			
			if(getUtil().isActualDataValid(actualDataRecord)) {
				String []actualDataArray = actualDataRecord.split("¹,¹");
				int columnHeaderIndex = 0;
				Map<String, Object> properties = new HashMap<String, Object>();
				String []convertedColumnHeaderNames = new String[getColumnHeaders().size()];
				for(String actualDataValue : actualDataArray) {
					actualDataValue = getUtil().eliminatePartrecQuotes(actualDataValue);
					String columnHeader = getColumnHeaders().get(columnHeaderIndex);
					columnHeader = columnHeaderToColumnNameMapping.get(getTableName() + "." + columnHeader);
					
					getConverter().setTableName(getTableName())
						.setColumnHeader(columnHeader);
					
					if(properties.containsKey(columnHeader)) columnHeader = columnHeader + "1";
					properties.put(columnHeader, getConverter().convert(actualDataValue.trim()));
					convertedColumnHeaderNames[columnHeaderIndex] = columnHeader;
					columnHeaderIndex++;
				}
				
				logger.info("Saving in " + getTableName() + ": " + properties);
				getDao().addReport(properties, getTableName(), convertedColumnHeaderNames);
				getActualDataRecordList().add(actualDataRecord);
			} else {
				logger.error("Invalid actual data marker on line " + actualDataLineCounter + " of file (" + file.getName() + "): "
						+ actualDataRecord.substring(1, 4) + " Expected: #$%");
			}
			
			actualDataLineCounter++;
		}
	}
}