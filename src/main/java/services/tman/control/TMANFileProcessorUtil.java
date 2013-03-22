package services.tman.control;

import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;

import services.tman.resolver.TMANReportTableNameResolver;
import services.tman.resolver.TMANReportTableNameResolverFactory;

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
public class TMANFileProcessorUtil implements Serializable {
	private static final long serialVersionUID = -7452219004290345068L;
	private static final TMANFileProcessorUtil INSTANCE = new TMANFileProcessorUtil();
	private TMANReportTableNameResolverFactory tableNameResolverFactory;
	
	private TMANFileProcessorUtil() {}
	
	public static TMANFileProcessorUtil getInstance() {
		return INSTANCE;
	}

	public void setTableNameResolverFactory(TMANReportTableNameResolverFactory tableNameResolverFactory) {
		this.tableNameResolverFactory = tableNameResolverFactory;
	}

	String[] parseToArrayByComma(String toParse) {
		return toParse.split(",");
	}
	
	int parseDataCount(String data) {
		return Integer.parseInt(data.split(":")[1].replace("¹", "").trim());
	}
	
	boolean isFirstLineDataMarkerInvalid(String firstLine) {
		return !firstLine.substring(0, 3).equals("!@#");
	}
	
	boolean isDateInvalid(String firstLine, DateFormat dateFormat) {
		try {
			dateFormat.parse(firstLine.substring(3));
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
		return getFileExtension(file.getName());
	}
	
	private String getFileExtension(String filename) {
		String []splittedFileName = filename.split("\\.");
		String fileExtension = splittedFileName[splittedFileName.length - 1];
		
		return fileExtension;
	}
	
	String getTableName(String filename) {
		TMANReportTableNameResolver resolver = 
				tableNameResolverFactory.getTableNameResolver(getFileExtension(filename));
		if(resolver == null) { return null; }
		return resolver.resolveTableName(filename);
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
	
	String eliminatePartrecQuotes(String actualDataValue) {
		if(actualDataValue.contains("¹"))
			return actualDataValue.replace("¹", "");
		
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