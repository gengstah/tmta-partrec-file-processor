package services.tman.control;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import services.tman.control.TMTAFileProcessor;
import services.tman.converter.DataTypeConverter;
import services.tman.dao.JdbcTerritoryManagerDao;

/**
 * TMTA File Processor test
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class TMTAFileProcessorTest {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private TMTAFileProcessor tmtaFileProcessor;
	private File mockFile;
	private BufferedReader validTMTAFileReader;
	private BufferedReader invalidFileDueToIncorrectDataMarkerReader;
	private BufferedReader invalidFileDueToIncorrectDateFormatReader;
	private BufferedReader invalidFileDueToNoReportName;
	private BufferedReader invalidFileDueToInconsistentNumberOfColumnsAndHeaders;
	private BufferedReader invalidFileDueToIncorrectActualDataMarker;
	private BufferedReader invalidFileDueToInconsistentRecordCountAndActualData;
	private Properties dataDictionary;
	
	@Before
	public void setUp() throws IOException {
		tmtaFileProcessor = new TMTAFileProcessor();
		tmtaFileProcessor.setDateFormat(DATE_FORMAT);
		dataDictionary = mock(Properties.class);
		
		tmtaFileProcessor.setDao(mock(JdbcTerritoryManagerDao.class));
		tmtaFileProcessor.setUtil(TMANFileProcessorUtil.getInstance());
		
		mockFile = mock(File.class);
		when(mockFile.getName()).thenReturn("MockFileName.CST");
		when(dataDictionary.getProperty(anyString(), anyString())).thenReturn("STRING");
		
		DataTypeConverter converter = new DataTypeConverter();
		converter.setDataDictionary(dataDictionary);
		converter.setTableName("TMTA_CST");
		tmtaFileProcessor.setConverter(converter);
		
		validTMTAFileReader = mock(BufferedReader.class);
		invalidFileDueToIncorrectDataMarkerReader = mock(BufferedReader.class);
		invalidFileDueToIncorrectDateFormatReader = mock(BufferedReader.class);
		invalidFileDueToNoReportName = mock(BufferedReader.class);
		invalidFileDueToInconsistentNumberOfColumnsAndHeaders = mock(BufferedReader.class);
		invalidFileDueToIncorrectActualDataMarker = mock(BufferedReader.class);
		invalidFileDueToInconsistentRecordCountAndActualData = mock(BufferedReader.class);
		
		when(validTMTAFileReader.readLine())
			.thenReturn("1,!@#2012-05-22")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\" \",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToIncorrectDataMarkerReader.readLine())
			.thenReturn("1,#@!2012-05-22")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToIncorrectDateFormatReader.readLine())
			.thenReturn("1,!@#2012-0531")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToNoReportName.readLine())
			.thenReturn("1,!@#2012-05-22")
			.thenReturn(" ,\" \",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToInconsistentNumberOfColumnsAndHeaders.readLine())
			.thenReturn("1,!@#2012-05-22")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:13")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToIncorrectActualDataMarker.readLine())
			.thenReturn("1,!@#2012-05-22")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"%$#\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:1");
		
		when(invalidFileDueToInconsistentRecordCountAndActualData.readLine())
			.thenReturn("1,!@#2012-05-22")
			.thenReturn(" ,\"CUSTOMERS\",,,,,,")
			.thenReturn(" ,COLUMNS:11")
			.thenReturn(" ,\"DATA_MARKER\",\"COUNTRY_CODE\",\"BO\",\"TERR\",\"CUST\",\"NAME\",\"ADDRESS\",\"CITY\",\"STATE\",\"ZIP\",\"PERIOD_DATE\"")
			.thenReturn(" ,\"#$%\",\"000\",\"111\",\"000\",\"9242374\",\"USDA FOREST\",\"13700 US HIGHWAY 33\",\"NELSONVILLE\",\"OH\",\"45764\",\"2012-05-22\"")
			.thenReturn(" ,RECORD COUNT:2");
	}
	
	@Test
	public void testValidDataMarker() throws IOException {
		tmtaFileProcessor.setReader(validTMTAFileReader);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarker() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToIncorrectDataMarkerReader);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDate() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToIncorrectDateFormatReader);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFileWithNoReportName() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToNoReportName);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test
	public void testFileReportName() throws IOException {
		tmtaFileProcessor.setReader(validTMTAFileReader);
		tmtaFileProcessor.process(mockFile);
		
		assertEquals("\"CUSTOMERS\"", tmtaFileProcessor.getReportName());
	}
	
	@Test
	public void testNumberOfColumns() throws IOException {
		tmtaFileProcessor.setReader(validTMTAFileReader);
		tmtaFileProcessor.process(mockFile);
		
		assertEquals(11, tmtaFileProcessor.getColumnCount());
	}
	
	@Test
	public void testColumnHeaderAgainstNumberOfColumns() throws IOException {
		tmtaFileProcessor.setReader(validTMTAFileReader);
		tmtaFileProcessor.process(mockFile);
		
		assertEquals(tmtaFileProcessor.getColumnCount(), tmtaFileProcessor.getColumnHeaders().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentColumnNumberAndHeaders() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToInconsistentNumberOfColumnsAndHeaders);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarkerOfActualData() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToIncorrectActualDataMarker);
		tmtaFileProcessor.process(mockFile);
	}
	
	@Test
	public void testActualDataAgainstRecordCount() throws IOException {
		tmtaFileProcessor.setReader(validTMTAFileReader);
		tmtaFileProcessor.process(mockFile);
		
		assertEquals(tmtaFileProcessor.getRecordCount(), tmtaFileProcessor.getActualDataRecordList().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentRecordCountAndActualData() throws IOException {
		tmtaFileProcessor.setReader(invalidFileDueToInconsistentRecordCountAndActualData);
		tmtaFileProcessor.process(mockFile);
	}
}