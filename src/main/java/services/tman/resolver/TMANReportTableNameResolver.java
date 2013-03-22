package services.tman.resolver;

import java.util.Map;


public abstract class TMANReportTableNameResolver {
	private Map<String, String> filenameReportTableMapping;
	private String fileExtension;
	
	public String resolveTableName(String filename) {
		String []filenameArray = filename.split("\\.");
		fileExtension = filenameArray[filenameArray.length - 1];
		
		return resolve(filename);
	}
	
	abstract String resolve(String filename);

	protected Map<String, String> getFilenameReportTableMapping() {
		return filenameReportTableMapping;
	}
	
	public void setFilenameReportTableMapping(Map<String, String> fileExtensionTableMapping) {
		this.filenameReportTableMapping = fileExtensionTableMapping;
	}

	public String getFileExtension() {
		return fileExtension;
	}
}