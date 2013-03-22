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
			numberEditor = new CustomNumberEditor(Long.class, true);
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

	public DateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public CustomDateEditor getDateEditor() {
		return dateEditor;
	}

	public void setDateEditor(CustomDateEditor dateEditor) {
		this.dateEditor = dateEditor;
	}

	public Map<String, CustomDateEditor> getDateEditorOverrides() {
		return dateEditorOverrides;
	}

	public void setDateEditorOverrides(Map<String, CustomDateEditor> dateEditorOverrides) {
		this.dateEditorOverrides = dateEditorOverrides;
	}

	public Map<String, NumberFormat> getNumberFormatOverrides() {
		return numberFormatOverrides;
	}

	public void setNumberFormatOverrides(Map<String, NumberFormat> numberFormatOverrides) {
		this.numberFormatOverrides = numberFormatOverrides;
	}
}