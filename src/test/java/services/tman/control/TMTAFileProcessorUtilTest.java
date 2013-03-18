package services.tman.control;

import static org.junit.Assert.*;

import org.junit.Test;

public class TMTAFileProcessorUtilTest {
	private static final TMTAFileProcessorUtil UTIL = TMTAFileProcessorUtil.getInstance();
	
	@Test
	public void testParseToArrayByComma() {
		assertEquals(5, UTIL.parseToArrayByComma(" ,      ,       ,    ,   ").length);
		assertEquals(5, UTIL.parseToArrayByComma(" , , ,\" \", ").length);
	}
	
	@Test
	public void testParseDataCount() {
		assertEquals(3, UTIL.parseDataCount("RECORD COUNT: 3"));
		assertEquals(5, UTIL.parseDataCount("COLUMN COUNT:5"));
	}
	
	@Test
	public void testFirstLineDataMarkerValidity() {
		assertTrue(UTIL.isFirstLineDataMarkerInvalid("1,!@#2012-05-22"));
	}
	
	@Test
	public void testDateValidity() {
		assertTrue(UTIL.isDateInvalid("!@#05-20-A1989"));
		assertTrue(UTIL.isDateInvalid("!@#1989A-05-20"));
	}
	
	@Test
	public void testTableNameIfCorrect() {
		UTIL.setCustomerTable("TMTA_CST");
		UTIL.setMachineTable("TMTA_MCH");
		UTIL.setEcDataTable("TMTA_ECD");
		UTIL.setPmDataTable("TMTA_PMD");
		assertEquals("TMTA_CST", UTIL.getTableName("CST"));
		assertEquals("TMTA_MCH", UTIL.getTableName("MCH"));
		assertEquals("TMTA_ECD", UTIL.getTableName("ECD"));
		assertEquals("TMTA_PMD", UTIL.getTableName("PMD"));
	}
	
	@Test
	public void testQuoteElimination() {
		assertEquals("Gerard", UTIL.eliminateQuotes("G\"\"er\"a\"\"r\"d"));
	}
}