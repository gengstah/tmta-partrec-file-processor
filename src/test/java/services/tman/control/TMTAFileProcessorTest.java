package services.tman.control;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import services.tman.control.TMTAFileProcessor;
import services.tman.dao.JdbcTerritoryManagerDao;

/**
 * TMTA File Processor test
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class TMTAFileProcessorTest {
	private TMTAFileProcessor fileProcessor;
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
		fileProcessor = new TMTAFileProcessor();
		dataDictionary = mock(Properties.class);
		
		fileProcessor.setDao(mock(JdbcTerritoryManagerDao.class));
		fileProcessor.setDataDictionary(dataDictionary);
		fileProcessor.setUtil(TMTAFileProcessorUtil.getInstance());
		
		mockFile = mock(File.class);
		when(mockFile.getName()).thenReturn("MockFileName.CST");
		when(dataDictionary.getProperty(anyString(), anyString())).thenReturn("STRING");
		
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
		fileProcessor.setReader(validTMTAFileReader);
		fileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarker() throws IOException {
		fileProcessor.setReader(invalidFileDueToIncorrectDataMarkerReader);
		fileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDate() throws IOException {
		fileProcessor.setReader(invalidFileDueToIncorrectDateFormatReader);
		fileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFileWithNoReportName() throws IOException {
		fileProcessor.setReader(invalidFileDueToNoReportName);
		fileProcessor.process(mockFile);
	}
	
	@Test
	public void testFileReportName() throws IOException {
		fileProcessor.setReader(validTMTAFileReader);
		fileProcessor.process(mockFile);
		
		assertEquals("\"CUSTOMERS\"", fileProcessor.getReportName());
	}
	
	@Test
	public void testNumberOfColumns() throws IOException {
		fileProcessor.setReader(validTMTAFileReader);
		fileProcessor.process(mockFile);
		
		assertEquals(11, fileProcessor.getColumnCount());
	}
	
	@Test
	public void testColumnHeaderAgainstNumberOfColumns() throws IOException {
		fileProcessor.setReader(validTMTAFileReader);
		fileProcessor.process(mockFile);
		
		assertEquals(fileProcessor.getColumnCount(), fileProcessor.getColumnHeaders().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentColumnNumberAndHeaders() throws IOException {
		fileProcessor.setReader(invalidFileDueToInconsistentNumberOfColumnsAndHeaders);
		fileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarkerOfActualData() throws IOException {
		fileProcessor.setReader(invalidFileDueToIncorrectActualDataMarker);
		fileProcessor.process(mockFile);
	}
	
	@Test
	public void testActualDataAgainstRecordCount() throws IOException {
		fileProcessor.setReader(validTMTAFileReader);
		fileProcessor.process(mockFile);
		
		assertEquals(fileProcessor.getRecordCount(), fileProcessor.getActualDataRecordList().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentRecordCountAndActualData() throws IOException {
		fileProcessor.setReader(invalidFileDueToInconsistentRecordCountAndActualData);
		fileProcessor.process(mockFile);
	}
}