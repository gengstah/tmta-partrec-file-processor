package services.tman.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TMANFileProcessor {
	void initReader(File file) throws FileNotFoundException;
	void process(File file) throws IOException;
	boolean isFlatFileValid(File file);
	String getProcessedFilesDirectory();
	String getInvalidFilesDirectory();
	List<String> getInputDirectories();
	void shutdown() throws IOException;
}