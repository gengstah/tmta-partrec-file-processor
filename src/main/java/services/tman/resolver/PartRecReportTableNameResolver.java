package services.tman.resolver;

public class PartRecReportTableNameResolver extends TMANReportTableNameResolver {
	@Override
	String resolve(String filename) {
		return getFilenameReportTableMapping().get(filename.toUpperCase());
	}
}