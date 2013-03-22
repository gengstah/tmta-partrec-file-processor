package services.tman.converter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import services.tman.converter.DataTypeConverter;

/**
 * Data type converter test
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class DataTypeConverterTest {
	private DataTypeConverter converter;
	private Properties dataDictionary;
	private DateFormat dateFormat;
	private CustomDateEditor dateEditor;
	
	@Before
	public void setUp() {
		dataDictionary = mock(Properties.class);
		when(dataDictionary.getProperty(anyString(), anyString()))
				.thenReturn("STRING").thenReturn("LONG")
				.thenReturn("DOUBLE").thenReturn("DATE")
				.thenReturn("DOUBLE").thenReturn("DOUBLE");
		converter = new DataTypeConverter();
		converter.setDataDictionary(dataDictionary);
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateEditor = new CustomDateEditor(dateFormat, true);
		converter.setDateFormat(dateFormat);
		converter.setDateEditor(dateEditor);
		
		Map<String, CustomDateEditor> customEditorOverrides = new HashMap<String, CustomDateEditor>();
		customEditorOverrides.put("MONTHLY_DISB_AND_RETURNS_IMR.TRANSACTION_DATE", new CustomDateEditor(new SimpleDateFormat("MMddyy"), true));
		converter.setDateEditorOverrides(customEditorOverrides);
		Map<String, NumberFormat> numberFormatOverrides = new HashMap<String, NumberFormat>();
		numberFormatOverrides.put("MONTHLY_DISB_AND_RETURNS_IMR.PRICE", new DecimalFormat("#,###.###"));
		numberFormatOverrides.put("BRANCH_MANAGER_SUMMARY_ASL.TOTAL_INV_$", NumberFormat.getCurrencyInstance(Locale.US));
		converter.setNumberFormatOverrides(numberFormatOverrides);
	}
	
	@Test
	public void testConversion() {
		assertTrue(converter.convert("String Value")
				instanceof String);
		assertTrue(converter.convert("123456789")
				instanceof Long);
		assertTrue(converter.convert("3.1416")
				instanceof Double);
		converter.setTableName("MONTHLY_DISB_AND_RETURNS_IMR").setColumnHeader("TRANSACTION_DATE");
		assertTrue(converter.convert("2013-05-20")
				instanceof Date);
		converter.setTableName("BRANCH_MANAGER_SUMMARY_ASL").setColumnHeader("TOTAL_INV_$");
		assertTrue(converter.convert("$47,126")
				instanceof Double);
		converter.setTableName("MONTHLY_DISB_AND_RETURNS_IMR").setColumnHeader("PRICE");
		assertTrue(converter.convert("47,126.06")
				instanceof Double);
	}
}