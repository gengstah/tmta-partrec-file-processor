package services.tman.control;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import services.tman.converter.DataTypeConverter;
import services.tman.dao.JdbcTerritoryManagerDao;

public class PartRecFileProcessorTest {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy");
	private PartRecFileProcessor partrecFileProcessor;
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
		partrecFileProcessor = new PartRecFileProcessor();
		partrecFileProcessor.setDateFormat(DATE_FORMAT);
		partrecFileProcessor.setUserActions(new HashMap<String, Boolean>());
		dataDictionary = mock(Properties.class);
		
		partrecFileProcessor.setDao(mock(JdbcTerritoryManagerDao.class));
		partrecFileProcessor.setUtil(TMANFileProcessorUtil.getInstance());
		
		mockFile = mock(File.class);
		when(mockFile.getName()).thenReturn("MockFileName.CST");
		when(dataDictionary.getProperty(anyString(), anyString())).thenReturn("STRING");
		
		DataTypeConverter converter = new DataTypeConverter();
		converter.setDataDictionary(dataDictionary);
		converter.setTableName("TMTA_CST");
		partrecFileProcessor.setConverter(converter);
		partrecFileProcessor.setColumnHeaderToColumnNameMapping(new HashMap<String, String>());
		
		validTMTAFileReader = mock(BufferedReader.class);
		invalidFileDueToIncorrectDataMarkerReader = mock(BufferedReader.class);
		invalidFileDueToIncorrectDateFormatReader = mock(BufferedReader.class);
		invalidFileDueToNoReportName = mock(BufferedReader.class);
		invalidFileDueToInconsistentNumberOfColumnsAndHeaders = mock(BufferedReader.class);
		invalidFileDueToIncorrectActualDataMarker = mock(BufferedReader.class);
		invalidFileDueToInconsistentRecordCountAndActualData = mock(BufferedReader.class);
		
		when(validTMTAFileReader.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToIncorrectDataMarkerReader.readLine())
			.thenReturn("#@!12312011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToIncorrectDateFormatReader.readLine())
			.thenReturn("!@#1231a2011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToNoReportName.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("¹¹,¹¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToInconsistentNumberOfColumnsAndHeaders.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:28")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToIncorrectActualDataMarker.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹$%^¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    1¹");
		
		when(invalidFileDueToInconsistentRecordCountAndActualData.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("¹Physical Inventory¹,¹Regular VIW¹,¹PSAR=N¹,¹FC7=N¹,¹ASL=N¹,¹Comment=Y¹,¹Reconcile=Y¹")
			.thenReturn("Columns:27")
			.thenReturn("¹Data Marker¹,¹Country¹,¹Branch¹,¹Territory¹,¹Out Loc¹,¹Serial Number¹,¹Part Number¹,¹Stock Loc¹,¹Part Name¹,¹APC Code¹,¹PI StartDate¹,¹OLI Qty¹,¹PHYS Qty¹,¹Variance¹,¹Unit Price¹,¹Activity Sequence #¹,¹Qty¹,¹Order Number¹,¹Ship B/O¹,¹Ship O/L¹,¹Rec B/O¹,¹Rec O/L¹,¹Trans Date¹,¹Trans Code¹,¹Trans Type¹,¹FNF¹,¹EMCD¹                                  ")
			.thenReturn("¹#$%¹,¹649¹,¹017¹,¹533¹,¹200¹,¹      ¹,¹012R2109¹,¹      ¹,¹CONTROLLER¹,¹0    ¹,¹12/03/12¹,¹    1 ¹,¹    0 ¹,¹    1-¹,¹         4263.32¹,¹       ¹,¹         ¹,¹      ¹,¹   ¹,¹   ¹,¹   ¹,¹   ¹,¹        ¹,¹  ¹,¹    ¹,¹ ¹,¹ ¹")
			.thenReturn("¹RECORD COUNT:    12¹");
	}
	
	@Test
	public void testValidDataMarker() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarker() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToIncorrectDataMarkerReader);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDate() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToIncorrectDateFormatReader);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFileWithNoReportName() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToNoReportName);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test
	public void testFileReportName() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
		
		assertEquals("Physical Inventory", partrecFileProcessor.getReportName());
		assertEquals("Regular VIW", partrecFileProcessor.getSubReportName());
	}
	
	@Test
	public void testNumberOfColumns() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
		
		assertEquals(27, partrecFileProcessor.getColumnCount());
	}
	
	@Test
	public void testColumnHeaderAgainstNumberOfColumns() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
		
		assertEquals(partrecFileProcessor.getColumnCount(), partrecFileProcessor.getColumnHeaders().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentColumnNumberAndHeaders() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToInconsistentNumberOfColumnsAndHeaders);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDataMarkerOfActualData() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToIncorrectActualDataMarker);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test
	public void testActualDataAgainstRecordCount() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
		
		assertEquals(partrecFileProcessor.getRecordCount(), partrecFileProcessor.getActualDataRecordList().size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInconsistentRecordCountAndActualData() throws IOException {
		partrecFileProcessor.setReader(invalidFileDueToInconsistentRecordCountAndActualData);
		partrecFileProcessor.process(mockFile);
	}
	
	@Test
	public void testUserActions() throws IOException {
		partrecFileProcessor.setReader(validTMTAFileReader);
		partrecFileProcessor.process(mockFile);
		assertNotNull(partrecFileProcessor.getUserActions().get("PSAR"));
		assertNotNull(partrecFileProcessor.getUserActions().get("FC7"));
		assertNotNull(partrecFileProcessor.getUserActions().get("ASL"));
		assertNotNull(partrecFileProcessor.getUserActions().get("Comment"));
		assertNotNull(partrecFileProcessor.getUserActions().get("Reconcile"));
		assertEquals(partrecFileProcessor.getUserActions().get("PSAR"), false);
		assertEquals(partrecFileProcessor.getUserActions().get("FC7"), false);
		assertEquals(partrecFileProcessor.getUserActions().get("ASL"), false);
		assertEquals(partrecFileProcessor.getUserActions().get("Comment"), true);
		assertEquals(partrecFileProcessor.getUserActions().get("Reconcile"), true);
	}
}