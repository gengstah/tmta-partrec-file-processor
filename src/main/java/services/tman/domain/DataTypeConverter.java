package services.tman.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class is mainly utilized to convert String to 
 * the proper data type definition defined for each
 * database column that a value is set
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class DataTypeConverter {
	private static final Logger logger = Logger.getLogger(DataTypeConverter.class);
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	private Properties dataDictionary;
	private String tableName;
	private String columnHeader;
	private String fileName;
	
	public DataTypeConverter(Properties dataDictionary) { setDataDictionary(dataDictionary); }
	
	/**
	 * This method is used to convert string to the proper database
	 * column definition. It takes the actual data value and
	 * returns the converted type.
	 * 
	 * @param actualDataValue The actual value in string format
	 * @return Object that is converted to the proper data type for saving
	 * in the database
	 */
	public Object convertToProperDataType(String actualDataValue) {
		String dataType = dataDictionary.getProperty(tableName + "." + columnHeader, DataType.STRING.toString());
		
		if(dataType.equals(DataType.STRING.toString())) return actualDataValue;
		else if(dataType.equals(DataType.LONG.toString())) {
			try {
				return Long.parseLong(actualDataValue);
			} catch(NumberFormatException nfe) {
				logger.error("Invalid actual data of file (" + fileName + ") cannot be parsed to a Long value: "
						+ actualDataValue);
			}
		} else if(dataType.equals(DataType.DOUBLE.toString())) {
			try {
				return Double.parseDouble(actualDataValue);
			} catch(NumberFormatException nfe) {
				logger.error("Invalid actual data of file (" + fileName + ") cannot be parsed to a Double value: "
						+ actualDataValue);
			}
		} else if(dataType.equals(DataType.DATE.toString())) {
			try {
				return df.parse(actualDataValue);
			} catch (ParseException e) {
				logger.error("Invalid date found for actual date: "
						+ actualDataValue + " of file (" + fileName
						+ ") Expected format: YYYY-MMM-DD");
			}
		}
		
		return null;
	}
	
	public void setDataDictionary(Properties dataDictionary) {
		this.dataDictionary = dataDictionary;
	}
	
	public DataTypeConverter setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	public DataTypeConverter setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
		return this;
	}
	
	public DataTypeConverter setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
}