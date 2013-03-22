package services.tman.resolver;

import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TMANReportTableNameResolverTest {
	private TMANReportTableNameResolver tmtaResolver;
	private TMANReportTableNameResolver partrecResolver;
	private Map<String, String> tmtaFilenameReportTableMapping;
	private Map<String, String> partrecFilenameReportTableMapping;
	
	@Before
	public void setUp() {
		tmtaResolver = new TMTAReportTableNameResolver();
		partrecResolver = new PartRecReportTableNameResolver();
		
		tmtaFilenameReportTableMapping = new HashMap<String, String>();
		tmtaFilenameReportTableMapping.put("CST", "TMTA_CST");
		tmtaFilenameReportTableMapping.put("PMD", "TMTA_PMD");
		tmtaFilenameReportTableMapping.put("ECD", "TMTA_ECD");
		tmtaFilenameReportTableMapping.put("MCH", "TMTA_MCH");
		tmtaResolver.setFilenameReportTableMapping(tmtaFilenameReportTableMapping);
		
		partrecFilenameReportTableMapping = new HashMap<String, String>();
		partrecFilenameReportTableMapping.put("MONTHLY ACCOUNT CODE CHANGE.IMR", "MONTHLY_ACCOUNT_CODE_CHANGE_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY DISB AND RETURNS.IMR", "MONTHLY_DISB_AND_RETURNS_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY MACHINE TYPE REPORT.IMR", "MONTHLY_MACHINE_TYPE_REPORT_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY PRICE CHANGE.IMR", "MONTHLY_PRICE_CHANGE_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY QUANTITY ADJUSTMENT.IMR", "MONTHLY_QUANTITY_ADJUSTMENT_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY RECEIPT ACTIVITY.IMR", "MONTHLY_RECEIPT_ACTIVITY_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY RECCOMENDED ADDS.IMR", "MONTHLY_RECCOMENDED_ADDS_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY RECCOMENDED RETURNS.IMR", "MONTHLY_RECCOMENDED_RETURNS_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY STOCK STATUS.IMR", "MONTHLY_STOCK_STATUS_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY SUBSTITUTION.IMR", "MONTHLY_SUBSTITUTION_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY PARTS DISPOSITION.IMR", "MONTHLY_PARTS_DISPOSITION_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY MINUS ON HAND.IMR", "MONTHLY_MINUS_ON_HAND_IMR");
		partrecFilenameReportTableMapping.put("WEEKLY MINUS ON HAND.IMR", "WEEKLY_MINUS_ON_HAND_IMR");
		partrecFilenameReportTableMapping.put("WEEKLY RECOMMENDED ADDS.IMR", "WEEKLY_RECCOMENDED_ADD_IMR");
		partrecFilenameReportTableMapping.put("WEEKLY RECOMMENDED RETURNS.IMR", "WEEKLY_RECOMMENDED_RETURNS_IMR");
		partrecFilenameReportTableMapping.put("WEEKLY DISBURSEMENTS AND RETURNS.IMR", "WEEKLY_DISBURSEMENTS_AND_RETURNS_IMR");
		partrecFilenameReportTableMapping.put("WEEKLY RECEIPT ACTIVITY.IMR", "WEEKLY_RECEIPT_ACTIVITY_IMR");
		partrecFilenameReportTableMapping.put("MONTHLY AGED RETURNS.UPR", "MONTHLY_AGED_RETURNS_UPR");
		partrecFilenameReportTableMapping.put("MONTHLY ANTICIPATED RETURNS.UPR", "MONTHLY_ANTICIPATED_RETURNS_UPR");
		partrecFilenameReportTableMapping.put("MONTHLY POTENTIAL RETURNS.UPR", "MONTHLY_POTENTIAL_RETURNS_UPR");
		partrecFilenameReportTableMapping.put("WEEKLY POTENTIAL RETURNS UPR.UPR", "WEEKLY_POTENTIAL_RETURNS_UPR");
		partrecFilenameReportTableMapping.put("BRANCH MANAGER SUMMARY.ASL", "BRANCH_MANAGER_SUMMARY_ASL");
		partrecFilenameReportTableMapping.put("BRANCH MANAGER TOTALS.ASL", "BRANCH_MANAGER_TOTALS_ASL");
		partrecFilenameReportTableMapping.put("FIELD MANAGER SUMMARY.ASL", "FIELD_MANAGER_SUMMARY_ASL");
		partrecFilenameReportTableMapping.put("TERRITORY DETAIL.ASL", "TERRITORY_DETAIL_ASL");
		partrecFilenameReportTableMapping.put("REGULAR  COUNTS. PHY", "REGULAR_COUNTS_PHY");
		partrecFilenameReportTableMapping.put("REGULAR  RECOUNTS. PHY", "REGULAR_RECOUNTS_PHY");
		partrecFilenameReportTableMapping.put("REGULAR  VIW.VIW", "REGULAR_VIW_VIW");
		partrecFilenameReportTableMapping.put("NBO  VIW.VIW", "NBO_VIW_VIW");
		partrecFilenameReportTableMapping.put("REGULAR ADJUSTMENT REPORT SUMMARY. RIR", "REGULAR_ADJUSTMENT_REPORT_SUMMARY_RIR");
		partrecFilenameReportTableMapping.put("REGULAR  ADJUSTMENT  REPORT  DETAIL.RIR", "REGULAR_ADJUSTMENT_REPORT_DETAIL_RIR");
		partrecFilenameReportTableMapping.put("NBO  COUNTS.NBO", "NBO_COUNTS_NBO");
		partrecFilenameReportTableMapping.put("NBO  RECOUNTS.NBO", "NBO_RECOUNTS_NBO");
		partrecFilenameReportTableMapping.put("REGULAR  ADJUSTMENT  REPORT  SUMMARY.NIR", "REGULAR_ADJUSTMENT_REPORT_SUMMARY_NIR");
		partrecFilenameReportTableMapping.put("REGULAR ADJUSTMENT REPORT DETAIL.NIR", "REGULAR_ADJUSTMENT_REPORT_DETAIL_NIR");
		partrecResolver.setFilenameReportTableMapping(partrecFilenameReportTableMapping);
	}
	
	@Test
	public void testTMTAReportTableNameResolver() {
		assertEquals("TMTA_PMD", tmtaResolver.resolveTableName("TMA000C4R.PMD"));
		assertEquals("TMTA_CST", tmtaResolver.resolveTableName("TMA000C4R.CST"));
		assertEquals("TMTA_MCH", tmtaResolver.resolveTableName("TMA000C4R.MCH"));
		assertEquals("TMTA_ECD", tmtaResolver.resolveTableName("TMA000C4R.ECD"));		
	}
	
	@Test
	public void testPartRecReportTableNameResolver() {
		assertEquals("MONTHLY_ACCOUNT_CODE_CHANGE_IMR", partrecResolver.resolveTableName("MONTHLY ACCOUNT CODE CHANGE.IMR"));
		assertEquals("MONTHLY_DISB_AND_RETURNS_IMR", partrecResolver.resolveTableName("MONTHLY DISB AND RETURNS.IMR"));
		assertEquals("MONTHLY_MACHINE_TYPE_REPORT_IMR", partrecResolver.resolveTableName("MONTHLY MACHINE TYPE REPORT.IMR"));
		assertEquals("MONTHLY_PRICE_CHANGE_IMR", partrecResolver.resolveTableName("MONTHLY PRICE CHANGE.IMR"));
		assertEquals("MONTHLY_QUANTITY_ADJUSTMENT_IMR", partrecResolver.resolveTableName("MONTHLY QUANTITY ADJUSTMENT.IMR"));
		assertEquals("MONTHLY_RECEIPT_ACTIVITY_IMR", partrecResolver.resolveTableName("MONTHLY RECEIPT ACTIVITY.IMR"));
		assertEquals("MONTHLY_RECCOMENDED_ADDS_IMR", partrecResolver.resolveTableName("MONTHLY RECCOMENDED ADDS.IMR"));
		assertEquals("MONTHLY_RECCOMENDED_RETURNS_IMR", partrecResolver.resolveTableName("MONTHLY RECCOMENDED RETURNS.IMR"));
		assertEquals("MONTHLY_STOCK_STATUS_IMR", partrecResolver.resolveTableName("MONTHLY STOCK STATUS.IMR"));
		assertEquals("MONTHLY_SUBSTITUTION_IMR", partrecResolver.resolveTableName("MONTHLY SUBSTITUTION.IMR"));
		assertEquals("MONTHLY_PARTS_DISPOSITION_IMR", partrecResolver.resolveTableName("MONTHLY PARTS DISPOSITION.IMR"));
		assertEquals("MONTHLY_MINUS_ON_HAND_IMR", partrecResolver.resolveTableName("MONTHLY MINUS ON HAND.IMR"));
		assertEquals("WEEKLY_MINUS_ON_HAND_IMR", partrecResolver.resolveTableName("WEEKLY MINUS ON HAND.IMR"));
		assertEquals("WEEKLY_RECCOMENDED_ADD_IMR", partrecResolver.resolveTableName("WEEKLY RECOMMENDED ADDS.IMR"));
		assertEquals("WEEKLY_RECOMMENDED_RETURNS_IMR", partrecResolver.resolveTableName("WEEKLY RECOMMENDED RETURNS.IMR"));
		assertEquals("WEEKLY_DISBURSEMENTS_AND_RETURNS_IMR", partrecResolver.resolveTableName("WEEKLY DISBURSEMENTS AND RETURNS.IMR"));
		assertEquals("WEEKLY_RECEIPT_ACTIVITY_IMR", partrecResolver.resolveTableName("WEEKLY RECEIPT ACTIVITY.IMR"));
		assertEquals("MONTHLY_AGED_RETURNS_UPR", partrecResolver.resolveTableName("MONTHLY AGED RETURNS.UPR"));
		assertEquals("MONTHLY_ANTICIPATED_RETURNS_UPR", partrecResolver.resolveTableName("MONTHLY ANTICIPATED RETURNS.UPR"));
		assertEquals("MONTHLY_POTENTIAL_RETURNS_UPR", partrecResolver.resolveTableName("MONTHLY POTENTIAL RETURNS.UPR"));
		assertEquals("WEEKLY_POTENTIAL_RETURNS_UPR", partrecResolver.resolveTableName("WEEKLY POTENTIAL RETURNS UPR.UPR"));
		assertEquals("BRANCH_MANAGER_SUMMARY_ASL", partrecResolver.resolveTableName("BRANCH MANAGER SUMMARY.ASL"));
		assertEquals("BRANCH_MANAGER_TOTALS_ASL", partrecResolver.resolveTableName("BRANCH MANAGER TOTALS.ASL"));
		assertEquals("FIELD_MANAGER_SUMMARY_ASL", partrecResolver.resolveTableName("FIELD MANAGER SUMMARY.ASL"));
		assertEquals("TERRITORY_DETAIL_ASL", partrecResolver.resolveTableName("TERRITORY DETAIL.ASL"));
		assertEquals("REGULAR_COUNTS_PHY", partrecResolver.resolveTableName("REGULAR  COUNTS. PHY"));
		assertEquals("REGULAR_RECOUNTS_PHY", partrecResolver.resolveTableName("REGULAR  RECOUNTS. PHY"));
		assertEquals("REGULAR_VIW_VIW", partrecResolver.resolveTableName("REGULAR  VIW.VIW"));
		assertEquals("NBO_VIW_VIW", partrecResolver.resolveTableName("NBO  VIW.VIW"));
		assertEquals("REGULAR_ADJUSTMENT_REPORT_SUMMARY_RIR", partrecResolver.resolveTableName("REGULAR ADJUSTMENT REPORT SUMMARY. RIR"));
		assertEquals("REGULAR_ADJUSTMENT_REPORT_DETAIL_RIR", partrecResolver.resolveTableName("REGULAR  ADJUSTMENT  REPORT  DETAIL.RIR"));
		assertEquals("NBO_COUNTS_NBO", partrecResolver.resolveTableName("NBO  COUNTS.NBO"));
		assertEquals("NBO_RECOUNTS_NBO", partrecResolver.resolveTableName("NBO  RECOUNTS.NBO"));
		assertEquals("REGULAR_ADJUSTMENT_REPORT_SUMMARY_NIR", partrecResolver.resolveTableName("REGULAR  ADJUSTMENT  REPORT  SUMMARY.NIR"));
		assertEquals("REGULAR_ADJUSTMENT_REPORT_DETAIL_NIR", partrecResolver.resolveTableName("REGULAR ADJUSTMENT REPORT DETAIL.NIR"));		
	}
}