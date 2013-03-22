package services.tman.resolver;

public class TMTAReportTableNameResolver extends TMANReportTableNameResolver {
	@Override
	String resolve(String filename) {
		return getFilenameReportTableMapping().get(getFileExtension());
	}
}