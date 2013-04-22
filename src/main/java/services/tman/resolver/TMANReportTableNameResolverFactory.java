package services.tman.resolver;

import java.io.ObjectStreamException;
import java.util.Map;

/**
 * The factory class used to create instances
 * of {@link services.tman.resolver.TMANReportTableNameResolver}
 * 
 * 
 * @author Gerard
 * @version 1.0.0
 * @see services.tman.resolver.TMANReportTableNameResolver
 * @see #getInstance()
 * @see #getTableNameResolver(String)
 * @see #setFileExtensionResolverMapping(Map)
 */
public class TMANReportTableNameResolverFactory {
	private static final TMANReportTableNameResolverFactory INSTANCE = new TMANReportTableNameResolverFactory();
	private Map<String, TMANReportTableNameResolver> fileExtensionResolverMapping;
	
	/**
	 * Default constructor
	 */
	private TMANReportTableNameResolverFactory() {}
	
	/**
	 * Retrieves the one and only instance of
	 * {@link services.tman.resolver.TMANReportTableNameResolverFactory}
	 * 
	 * @return The {@link services.tman.resolver.TMANReportTableNameResolverFactory}
	 * instance
	 */
	public static final TMANReportTableNameResolverFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Retrieves the {@link services.tman.resolver.TMANReportTableNameResolver}
	 * instance for a given file extension
	 * 
	 * @param fileExtension The file extension of the TMAN file report
	 * @return The {@link services.tman.resolver.TMANReportTableNameResolver}
	 */
	public TMANReportTableNameResolver getTableNameResolver(String fileExtension) {
		return fileExtensionResolverMapping.get(fileExtension);
	}

	/**
	 * Set the map for the file extension to 
	 * {@link services.tman.resolver.TMANReportTableNameResolver}
	 * mapping
	 * 
	 * @param fileExtensionResolverMapping The map for the file extension to 
	 * {@link services.tman.resolver.TMANReportTableNameResolver}
	 */
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