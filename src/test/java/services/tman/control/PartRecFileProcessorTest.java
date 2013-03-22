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
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToIncorrectDataMarkerReader.readLine())
			.thenReturn("#@!12312011")
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToIncorrectDateFormatReader.readLine())
			.thenReturn("!@#1231a2011")
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToNoReportName.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("��,��,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToInconsistentNumberOfColumnsAndHeaders.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:28")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToIncorrectActualDataMarker.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�$%^�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    1�");
		
		when(invalidFileDueToInconsistentRecordCountAndActualData.readLine())
			.thenReturn("!@#12312011")
			.thenReturn("�Physical Inventory�,�Regular VIW�,�PSAR=N�,�FC7=N�,�ASL=N�,�Comment=Y�,�Reconcile=Y�")
			.thenReturn("Columns:27")
			.thenReturn("�Data Marker�,�Country�,�Branch�,�Territory�,�Out Loc�,�Serial Number�,�Part Number�,�Stock Loc�,�Part Name�,�APC Code�,�PI StartDate�,�OLI Qty�,�PHYS Qty�,�Variance�,�Unit Price�,�Activity Sequence #�,�Qty�,�Order Number�,�Ship B/O�,�Ship O/L�,�Rec B/O�,�Rec O/L�,�Trans Date�,�Trans Code�,�Trans Type�,�FNF�,�EMCD�                                  ")
			.thenReturn("�#$%�,�649�,�017�,�533�,�200�,�      �,�012R2109�,�      �,�CONTROLLER�,�0    �,�12/03/12�,�    1 �,�    0 �,�    1-�,�         4263.32�,�       �,�         �,�      �,�   �,�   �,�   �,�   �,�        �,�  �,�    �,� �,� �")
			.thenReturn("�RECORD COUNT:    12�");
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