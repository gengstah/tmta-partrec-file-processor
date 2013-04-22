package services.tman.resolver;

/**
 * This class is a subclass of
 * {@link services.tman.resolver.TMANReportTableNameResolver}
 * made specifically <b>PartRec file reports</b>
 * 
 * @author Gerard
 * @version 1.0.0
 */
public class PartRecReportTableNameResolver extends TMANReportTableNameResolver {
	/**
	 * Default constructor
	 */
	public PartRecReportTableNameResolver() { }
	
	@Override
	String resolve(String filename) {
		return getFilenameReportTableMapping().get(filename.toUpperCase());
	}
}