package services.tman.control;

import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import services.tman.domain.ReportFileTypeEnum;

/**
 * This class is a utility class that is utilized by
 * <code>TMTAFileProcessor</code> to help in the
 * readability of its code. This class is designed to
 * be a singleton class, hence, this class will have
 * one and only representation.
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class TMTAFileProcessorUtil implements Serializable {
	private static final long serialVersionUID = -7452219004290345068L;
	private static final TMTAFileProcessorUtil INSTANCE = new TMTAFileProcessorUtil();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	private String customerTable;
	private String machineTable;
	private String ecDataTable;
	private String pmDataTable;
	
	private TMTAFileProcessorUtil() {}
	
	public static TMTAFileProcessorUtil getInstance() {
		return INSTANCE;
	}
	
	String[] parseToArrayByComma(String toParse) {
		return toParse.split(",");
	}
	
	int parseDataCount(String data) {
		return Integer.parseInt(data.split(":")[1].trim());
	}
	
	public void setPmDataTable(String pmDataTable) {
		this.pmDataTable = pmDataTable;
	}

	public void setEcDataTable(String ecDataTable) {
		this.ecDataTable = ecDataTable;
	}

	public void setMachineTable(String machineTable) {
		this.machineTable = machineTable;
	}

	public void setCustomerTable(String customerTable) {
		this.customerTable = customerTable;
	}
	
	boolean isFirstLineDataMarkerInvalid(String firstLine) {
		return !firstLine.substring(0, 3).equals("!@#");
	}
	
	boolean isDateInvalid(String firstLine) {
		try {
			DATE_FORMAT.parse(firstLine.substring(3));
			return false;
		} catch (ParseException e) {
			return true;
		}
	}
	
	boolean isReportNameInvalid(String reportName) {
		String []parsedReportName = parseToArrayByComma(reportName);
		for(String repName : parsedReportName)
			if(!repName.trim().equals("")&& !repName.equals("\" \"")) return false;
		return true;
	}
	
	String parseReportName(String reportName) {
		String []parsedReportName = parseToArrayByComma(reportName);
		return parseToArrayByComma(reportName.replace(parsedReportName[0] + ",", ""))[0];
	}
	
	String getFileExtension(File file) {
		String []splittedFileName = file.getName().split("\\.");
		String fileExtension = splittedFileName[splittedFileName.length - 1];
		
		return fileExtension;
	}
	
	String getTableName(String fileExtension) {
		String tableName = "";
		if(getFileType(fileExtension) == ReportFileTypeEnum.CST)
			tableName = customerTable;
		else if(getFileType(fileExtension) == ReportFileTypeEnum.ECD)
			tableName = ecDataTable;
		else if(getFileType(fileExtension) == ReportFileTypeEnum.MCH)
			tableName = machineTable;
		else if(getFileType(fileExtension) == ReportFileTypeEnum.PMD)
			tableName = pmDataTable;
		
		return tableName;
	}
	
	ReportFileTypeEnum getFileType(String fileExtension) {
		if(isCustomerReportFileType(fileExtension)) return ReportFileTypeEnum.CST;
		else if(isECDataReportFileType(fileExtension)) return ReportFileTypeEnum.ECD;
		else if(isMachineReportFileType(fileExtension)) return ReportFileTypeEnum.MCH;
		else if(isPMDataReportFileType(fileExtension)) return ReportFileTypeEnum.PMD;
		else return ReportFileTypeEnum.INVALID_FILE_EXTENSION;
	}
	
	private boolean isCustomerReportFileType(String fileExtension) {
		return Enum.valueOf(ReportFileTypeEnum.class, fileExtension).equals(
				ReportFileTypeEnum.CST);
	}
	
	private boolean isECDataReportFileType(String fileExtension) {
		return Enum.valueOf(ReportFileTypeEnum.class, fileExtension).equals(
				ReportFileTypeEnum.ECD);
	}
	
	private boolean isMachineReportFileType(String fileExtension) {
		return Enum.valueOf(ReportFileTypeEnum.class, fileExtension).equals(
				ReportFileTypeEnum.MCH);
	}
	
	private boolean isPMDataReportFileType(String fileExtension) {
		return Enum.valueOf(ReportFileTypeEnum.class, fileExtension).equals(
				ReportFileTypeEnum.PMD);
	}
	
	boolean isRecordCountLine(String actualDataRecord) {
		return actualDataRecord.contains("RECORD COUNT");
	}
	
	boolean isActualDataValid(String actualDataRecord) {
		return actualDataRecord.substring(1, 4).equals("#$%");
	}
	
	String eliminateQuotes(String actualDataValue) {
		if(actualDataValue.contains("\""))
			return actualDataValue.replace("\"", "");
		
		return actualDataValue;
	}
	
	/**
	 * Returns the one and only instance of the class in case of
	 * deserialization.
	 * 
	 * @return The one and only instance of the class
	 * @throws ObjectStreamException When this method encounters an
	 * error in serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		return INSTANCE;
	}
}