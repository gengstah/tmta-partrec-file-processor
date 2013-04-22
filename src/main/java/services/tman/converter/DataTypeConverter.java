package services.tman.converter;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

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
	private Map<String, NumberFormat> numberFormatOverrides;
	private DateFormat dateFormat;
	private CustomDateEditor dateEditor;
	private Map<String, CustomDateEditor> dateEditorOverrides;
	private CustomNumberEditor numberEditor;
	
	private Properties dataDictionary;
	private String tableName;
	private String columnHeader;
	
	/**
	 * Default constructor
	 */
	public DataTypeConverter() { }
	
	/**
	 * This method is used to convert string to the proper database
	 * column definition. It takes the actual data value and
	 * returns the converted type.
	 * 
	 * @param actualDataValue The actual value in string format
	 * @return Object that is converted to the proper data type for saving
	 * in the database
	 */
	public Object convert(String actualDataValue) {
		String key = tableName + "." + columnHeader;
		String dataType = dataDictionary.getProperty(key, "String");
		
		if(dataType.toUpperCase().equals("LONG")) {
			if(numberFormatOverrides.containsKey(key)) {
				numberEditor = new CustomNumberEditor(Long.class, numberFormatOverrides.get(key), true);
			} else {
				numberEditor = new CustomNumberEditor(Long.class, true);
			}
			
			numberEditor.setAsText(actualDataValue);
			return numberEditor.getValue();
		} else if(dataType.toUpperCase().equals("DOUBLE")) {
			if(numberFormatOverrides.containsKey(key)) {
				numberEditor = new CustomNumberEditor(Double.class, numberFormatOverrides.get(key), true);
			} else {
				numberEditor = new CustomNumberEditor(Double.class, true);
			}
			
			numberEditor.setAsText(actualDataValue);
			return numberEditor.getValue();
		} else if(dataType.toUpperCase().equals("DATE")) {
			if(dateEditorOverrides.containsKey(key)) {
				CustomDateEditor dateEditorOverride = dateEditorOverrides.get(key);
				dateEditorOverride.setAsText(actualDataValue);
				return dateEditorOverride.getValue();
			}
			
			dateEditor.setAsText(actualDataValue);
			return dateEditor.getValue();
		}
		
		return actualDataValue;
	}
	
	/**
	 * Set the data dictionary {@link java.util.Properties}
	 * 
	 * @param dataDictionary The data dictionary {@link java.util.Properties}
	 */
	public void setDataDictionary(Properties dataDictionary) {
		this.dataDictionary = dataDictionary;
	}
	
	/**
	 * Set the table name
	 * 
	 * @param tableName The table name
	 * @return a {@link DataTypeConverter} instance; returns itself
	 */
	public DataTypeConverter setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}
	
	/**
	 * Set the column header
	 *  
	 * @param columnHeader The column header
	 * @return The {@link DataTypeConverter} instance; returns itself
	 */
	public DataTypeConverter setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
		return this;
	}

	/**
	 * Retrieves the {@link java.text.DateFormat}
	 * 
	 * @return The {@link java.text.DateFormat}
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
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
	 * Retrieves the {@link org.springframework.beans.propertyeditors.CustomDateEditor}
	 * 
	 * @return The {@link org.springframework.beans.propertyeditors.CustomDateEditor}
	 */
	public CustomDateEditor getDateEditor() {
		return dateEditor;
	}

	/**
	 * Set the {@link org.springframework.beans.propertyeditors.CustomDateEditor}
	 * 
	 * @param dateEditor The {@link org.springframework.beans.propertyeditors.CustomDateEditor}
	 */
	public void setDateEditor(CustomDateEditor dateEditor) {
		this.dateEditor = dateEditor;
	}

	/**
	 * Retrieves the date editor overrides
	 * 
	 * @return The date editor overrides
	 */
	public Map<String, CustomDateEditor> getDateEditorOverrides() {
		return dateEditorOverrides;
	}

	/**
	 * Set the date editor overrides
	 * 
	 * @param dateEditorOverrides The date editor overrides
	 */
	public void setDateEditorOverrides(Map<String, CustomDateEditor> dateEditorOverrides) {
		this.dateEditorOverrides = dateEditorOverrides;
	}

	/**
	 * Retrieves the number format overrides
	 * 
	 * @return The number format overrides
	 */
	public Map<String, NumberFormat> getNumberFormatOverrides() {
		return numberFormatOverrides;
	}

	/**
	 * Set the number format overrides
	 * 
	 * @param numberFormatOverrides The number format overrides
	 */
	public void setNumberFormatOverrides(Map<String, NumberFormat> numberFormatOverrides) {
		this.numberFormatOverrides = numberFormatOverrides;
	}
}