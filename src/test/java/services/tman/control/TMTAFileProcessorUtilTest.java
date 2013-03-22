package services.tman.control;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class TMTAFileProcessorUtilTest {
	private static final TMANFileProcessorUtil UTIL = TMANFileProcessorUtil.getInstance();
	
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
		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		assertTrue(UTIL.isDateInvalid("!@#05-20-A1989", dateFormat));
		assertTrue(UTIL.isDateInvalid("!@#1989A-05-20", dateFormat));
	}
	
	@Test
	public void testQuoteElimination() {
		assertEquals("Gerard", UTIL.eliminateQuotes("G\"\"er\"a\"\"r\"d"));
	}
}