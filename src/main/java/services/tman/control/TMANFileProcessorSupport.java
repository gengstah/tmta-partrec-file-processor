package services.tman.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import services.tman.converter.DataTypeConverter;
import services.tman.dao.TerritoryManagerDao;

/**
 * Provides most methods needed by implementations, this support class
 * also implements most methods in 
 * {@link services.tman.control.TMANFileProcessor} 
 * interface.
 * 
 * @author Gerard
 * @version 1.0.0
 * @see services.tman.control.TMANFileProcessor
 * @see #initReader(File)
 * @see #process(File)
 * @see #isFlatFileValid(File)
 * @see #getInputDirectoriesToProcessedFilesDirectories()
 * @see #getInputDirectoriesToInvalidFilesDirectories()
 * @see #shutdown()
 */
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
	
	private Map<String, String> inputDirectoriesToProcessedFilesDirectories;
	private Map<String, String> inputDirectoriesToInvalidFilesDirectories;
	
	/**
	 * Default constructor
	 */
	public TMANFileProcessorSupport() {
		columnHeaders = new ArrayList<String>();
		actualDataRecordList = new ArrayList<String>();
	}
	
	/**
	 * Retrives the column count
	 * 
	 * @return The column count
	 */
	protected int getColumnCount() {
		return columnCount;
	}
	
	/**
	 * Set the report name
	 * 
	 * @param reportName The report name
	 */
	protected void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	/**
	 * Retrieves the report name
	 * 
	 * @return The report name
	 */
	protected String getReportName() {
		return reportName;
	}
	
	/**
	 * Retrieves the list of column headers
	 * 
	 * @return The list of column headers
	 */
	protected List<String> getColumnHeaders() {
		return columnHeaders;
	}
	
	/**
	 * Set the record count
	 * 
	 * @param recordCount The record count
	 */
	protected void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	/**
	 * Retrieves the record count
	 * 
	 * @return The record count
	 */
	protected int getRecordCount() {
		return recordCount;
	}
	
	/**
	 * Retrieves the actual data record list
	 * 
	 * @return The actual data record list
	 */
	protected List<String> getActualDataRecordList() {
		return actualDataRecordList;
	}
	
	/**
	 * Set the {@link java.io.BufferedReader}
	 * 
	 * @param reader The {@link java.io.BufferedReader}
	 */
	protected void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	
	/**
	 * Retrieves the {@link java.io.BufferedReader}
	 * 
	 * @return The {@link java.io.BufferedReader}
	 */
	protected BufferedReader getReader() {
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
	
	/**
	 * Retrieves the {@link services.tman.dao.TerritoryManagerDao}
	 * 
	 * @return The {@link services.tman.dao.TerritoryManagerDao}
	 */
	protected TerritoryManagerDao getDao() {
		return dao;
	}
	
	/**
	 * Set the {@link services.tman.control.TMANFileProcessorUtil}
	 * 
	 * @param util The {@link services.tman.control.TMANFileProcessorUtil}
	 */
	public void setUtil(TMANFileProcessorUtil util) {
		this.util = util;
	}
	
	/**
	 * Retrieves the {@link services.tman.control.TMANFileProcessorUtil}
	 * 
	 * @return The {@link services.tman.control.TMANFileProcessorUtil}
	 */
	public TMANFileProcessorUtil getUtil() {
		return util;
	}

	/**
	 * Retrieves the table name
	 * 
	 * @return The table name
	 */
	protected String getTableName() {
		return tableName;
	}

	/**
	 * Set the table name
	 * 
	 * @param tableName The table name
	 */
	void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Set the {@link java.text.DateFormat}
	 * 
	 * @param dateFormat The {@link java.text.DateFormat}
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Retrives the {@link java.text.DateFormat}
	 * 
	 * @return The {@link java.text.DateFormat}
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * Retrieves the {@link services.tman.converter.DataTypeConverter}
	 * 
	 * @return The {@link services.tman.converter.DataTypeConverter}
	 */
	public DataTypeConverter getConverter() {
		return converter;
	}

	/**
	 * Set the {@link services.tman.converter.DataTypeConverter}
	 * 
	 * @param converter The {@link services.tman.converter.DataTypeConverter}
	 */
	public void setConverter(DataTypeConverter converter) {
		this.converter = converter;
	}

	@Override
	public boolean isFlatFileValid(File file) {
		setTableName(util.getTableName(file.getName()));
		
		if(getTableName() == null) return false;
		return true;
	}

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
	abstract void processActualData(File file) throws IOException;
	
	/**
	 * Process the column count variable
	 * in the {@link java.io.File}
	 * 
	 * @throws IOException When application encounters an io error on
	 * reading the file reports
	 */
	protected void processColumnCount() throws IOException {
		String columnCount = reader.readLine();
		this.columnCount = util.parseDataCount(columnCount);
	}
	
	/**
	 * Determines whether column header count
	 * is not equal to column count
	 * 
	 * @return true if column header count is not equal
	 * to column count, otherwise, false
	 */
	protected boolean isColumnHeaderSizeIsNotEqualToColumnCount() {
		return columnHeaders.size() != getColumnCount();
	}
	
	/**
	 * Process the record count of actual data
	 * variable in the {@link java.io.File}
	 * 
	 * @param file The {@link java.io.File} that will be processed
	 * @param actualDataRecord The actual data record
	 * @throws IOException When application encounters an io error on
	 * reading the file reports
	 */
	protected void processRecordCount(File file, String actualDataRecord) throws IOException {
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

	@Override
	public Map<String, String> getInputDirectoriesToProcessedFilesDirectories() {
		return inputDirectoriesToProcessedFilesDirectories;
	}

	/**
	 * Set the map for input directories to processed files
	 * directories
	 * 
	 * @param inputDirectoriesToProcessedFilesDirectories
	 * The map for input directories to processed files
	 * directories
	 */
	public void setInputDirectoriesToProcessedFilesDirectories(
			Map<String, String> inputDirectoriesToProcessedFilesDirectories) {
		this.inputDirectoriesToProcessedFilesDirectories = inputDirectoriesToProcessedFilesDirectories;
	}

	@Override
	public Map<String, String> getInputDirectoriesToInvalidFilesDirectories() {
		return inputDirectoriesToInvalidFilesDirectories;
	}

	/**
	 * Set the map for input directories to invalid files
	 * directories
	 * 
	 * @param inputDirectoriesToInvalidFilesDirectories
	 * The map for input directories to invalid files
	 * directories
	 */
	public void setInputDirectoriesToInvalidFilesDirectories(
			Map<String, String> inputDirectoriesToInvalidFilesDirectories) {
		this.inputDirectoriesToInvalidFilesDirectories = inputDirectoriesToInvalidFilesDirectories;
	}
}