package services.tman.resolver;

import java.util.Map;

/**
 * Abstract class that handles the mapping of file name and/or
 * file extension of the TMAN file reports and resolves
 * these names into their corresponding database table names
 * 
 * @author Gerard
 * @version 1.0.0
 */
public abstract class TMANReportTableNameResolver {
	private Map<String, String> filenameReportTableMapping;
	private String fileExtension;
	
	/**
	 * Resolves and retrieves the database table name
	 * given the file name of the TMAN file report
	 * 
	 * @param filename The file name of the TMAN file report
	 * @return The table name, otherwise, null if
	 * mapping entry for the file name to database table
	 * cannot be found
	 */
	public String resolveTableName(String filename) {
		String []filenameArray = filename.split("\\.");
		fileExtension = filenameArray[filenameArray.length - 1];
		
		return resolve(filename);
	}
	
	abstract String resolve(String filename);

	/**
	 * Retrieves the map for the file name to database table mapping
	 * 
	 * @return The map for the file name to database table mapping
	 */
	protected Map<String, String> getFilenameReportTableMapping() {
		return filenameReportTableMapping;
	}
	
	/**
	 * Set the map for the file name to database table mapping
	 * 
	 * @param fileExtensionTableMapping The map for the file name to database table mapping
	 */
	public void setFilenameReportTableMapping(Map<String, String> fileExtensionTableMapping) {
		this.filenameReportTableMapping = fileExtensionTableMapping;
	}

	/**
	 * Retrieves the file extension
	 * 
	 * @return The file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}
}