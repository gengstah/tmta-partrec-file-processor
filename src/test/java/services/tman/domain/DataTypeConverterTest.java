package services.tman.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

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
	
	@Before
	public void setUp() {
		dataDictionary = mock(Properties.class);
		when(dataDictionary.getProperty(anyString(), anyString()))
				.thenReturn("STRING").thenReturn("LONG")
				.thenReturn("DOUBLE").thenReturn("DATE");
		converter = new DataTypeConverter(dataDictionary);
	}
	
	@Test
	public void testConversion() {
		assertTrue(converter.convertToProperDataType("String Value")
				instanceof String);
		assertTrue(converter.convertToProperDataType("123456789")
				instanceof Long);
		assertTrue(converter.convertToProperDataType("3.1416")
				instanceof Double);
		assertTrue(converter.convertToProperDataType("2013-05-20")
				instanceof Date);
	}
}