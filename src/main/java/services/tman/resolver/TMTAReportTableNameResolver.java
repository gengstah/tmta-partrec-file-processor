package services.tman.resolver;

/**
 * This class is a subclass of
 * {@link services.tman.resolver.TMANReportTableNameResolver}
 * made specifically <b>TMTA file reports</b>
 * 
 * @author Gerard
 * @version 1.0.0
 */
public class TMTAReportTableNameResolver extends TMANReportTableNameResolver {
	/**
	 * Default constructor
	 */
	public TMTAReportTableNameResolver() { }
	
	@Override
	String resolve(String filename) {
		return getFilenameReportTableMapping().get(getFileExtension());
	}
}