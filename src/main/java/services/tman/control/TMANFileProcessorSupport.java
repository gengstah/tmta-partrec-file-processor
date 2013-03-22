package services.tman.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import services.tman.converter.DataTypeConverter;
import services.tman.dao.TerritoryManagerDao;

public abstract class TMANFileProcessorSupport implements TMANFileProcessor {
	private DateFormat dateFormat;
	private int columnCount;
	private List<String> columnHeaders;
	private List<String> actualDataRecordList;
	private int recordCount;
	private DataTypeConverter converter;
	
	private TerritoryManagerDao dao;
	
	private BufferedReader reader;
	private String reportName;
	private TMANFileProcessorUtil util;
	private String tableName;
	
	private List<String> inputDirectories;
	private String processedFilesDirectory;
	private String invalidFilesDirectory;
	
	public TMANFileProcessorSupport() {
		columnHeaders = new ArrayList<String>();
		actualDataRecordList = new ArrayList<String>();
	}
	
	int getColumnCount() {
		return columnCount;
	}
	
	void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	String getReportName() {
		return reportName;
	}
	
	List<String> getColumnHeaders() {
		return columnHeaders;
	}
	
	void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
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
	
	BufferedReader getReader() {
		return reader;
	}
	
	/**
	 * Set the data access object
	 * 
	 * @param dao The Territory Manager Data Access Object
	 */
	public void setDao(TerritoryManagerDao dao) {
		this.dao = dao;
	}
	
	TerritoryManagerDao getDao() {
		return dao;
	}
	
	/**
	 * Set the utility class
	 * 
	 * @param util The utility that will be used by this class
	 */
	public void setUtil(TMANFileProcessorUtil util) {
		this.util = util;
	}
	
	public TMANFileProcessorUtil getUtil() {
		return util;
	}

	@Override
	public String getProcessedFilesDirectory() {
		return processedFilesDirectory;
	}

	public void setProcessedFilesDirectory(String processedFilesDirectory) {
		this.processedFilesDirectory = processedFilesDirectory;
	}

	@Override
	public String getInvalidFilesDirectory() {
		return invalidFilesDirectory;
	}

	public void setInvalidFilesDirectory(String invalidFilesDirectory) {
		this.invalidFilesDirectory = invalidFilesDirectory;
	}

	@Override
	public List<String> getInputDirectories() {
		return inputDirectories;
	}

	public void setInputDirectories(List<String> inputDirectories) {
		this.inputDirectories = inputDirectories;
	}

	String getTableName() {
		return tableName;
	}

	void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public DataTypeConverter getConverter() {
		return converter;
	}

	public void setConverter(DataTypeConverter converter) {
		this.converter = converter;
	}

	@Override
	public boolean isFlatFileValid(File file) {
		setTableName(util.getTableName(file.getName()));
		
		if(getTableName() == null) return false;
		return true;
	}

	/**
	 * Initialize the file reader with the file that will be processed
	 * 
	 * @param file The file that will be processed
	 * @throws FileNotFoundException
	 */
	@Override
	public void initReader(File file) throws FileNotFoundException {
		FileReader fileReader = new FileReader(file);
		reader = new BufferedReader(fileReader);
	}
	
	@Override
	public void process(File file) throws IOException {
		processFirstLine(file);
		processReportName(file);
		processColumnCount();
		processColumnHeader(file);
		processActualData(file);
		shutdown();
	}
	
	abstract void processFirstLine(File file) throws IOException;
	abstract void processReportName(File file) throws IOException;
	abstract void processColumnHeader(File file) throws IOException;
	
	void processColumnCount() throws IOException {
		String columnCount = reader.readLine();
		this.columnCount = util.parseDataCount(columnCount);
	}
	
	boolean isColumnHeaderSizeIsNotEqualToColumnCount() {
		return columnHeaders.size() != getColumnCount();
	}
	
	abstract void processActualData(File file) throws IOException;
	
	void processRecordCount(File file, String actualDataRecord) throws IOException {
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
	
	@Override
	public void shutdown() throws IOException {
		reader.close();
	}
}