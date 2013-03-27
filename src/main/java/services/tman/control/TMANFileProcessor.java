package services.tman.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface TMANFileProcessor {
	void initReader(File file) throws FileNotFoundException;
	void process(File file) throws IOException;
	boolean isFlatFileValid(File file);
	Map<String, String> getInputDirectoriesToProcessedFilesDirectories();
	Map<String, String> getInputDirectoriesToInvalidFilesDirectories();
	void shutdown() throws IOException;
}