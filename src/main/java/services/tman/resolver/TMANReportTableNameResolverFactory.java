package services.tman.resolver;

import java.io.ObjectStreamException;
import java.util.Map;

public class TMANReportTableNameResolverFactory {
	private static final TMANReportTableNameResolverFactory INSTANCE = new TMANReportTableNameResolverFactory();
	private Map<String, TMANReportTableNameResolver> fileExtensionResolverMapping;
	
	private TMANReportTableNameResolverFactory() {}
	
	public static final TMANReportTableNameResolverFactory getInstance() {
		return INSTANCE;
	}
	
	public TMANReportTableNameResolver getTableNameResolver(String fileExtension) {
		return fileExtensionResolverMapping.get(fileExtension);
	}

	public void setFileExtensionResolverMapping(
			Map<String, TMANReportTableNameResolver> fileExtensionResolverMapping) {
		this.fileExtensionResolverMapping = fileExtensionResolverMapping;
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