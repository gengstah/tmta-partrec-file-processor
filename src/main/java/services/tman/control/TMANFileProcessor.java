package services.tman.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * The core interface that provides methods to
 * process the TMAN file reports.
 * 
 * @author Gerard
 * @version 1.0.0
 * @see #initReader(File)
 * @see #process(File)
 * @see #isFlatFileValid(File)
 * @see #getInputDirectoriesToProcessedFilesDirectories()
 * @see #getInputDirectoriesToInvalidFilesDirectories()
 * @see #shutdown()
 */
public interface TMANFileProcessor {
	/**
	 * Initialize the file reader with the file that will be processed
	 * 
	 * @param file The {@link java.io.File} that will be processed
	 * @throws FileNotFoundException When the application failed
	 * to find the {@link java.io.File} specified
	 */	
	void initReader(File file) throws FileNotFoundException;
	
	/**
	 * Processes the file report instance.<br><br>
	 * This method will process the given
	 * {@link java.io.File} report instance, report any
	 * errors found on the {@link java.io.File} report 
	 * instance, and load the data to the staging area.
	 * 
	 * @param file The {@link java.io.File} that will be processed
	 * @throws IOException When application encounters an io error on
	 * reading the file reports
	 */
	void process(File file) throws IOException;
	
	/**
	 * Determines whether the {@link java.io.File}
	 * passed is a valid TMAN Report file
	 * 
	 * This method uses the {@link services.tman.control.TMANFileProcessorUtil}
	 * which in turn uses {@link services.tman.resolver.TMANReportTableNameResolver}
	 * to determine if there is a mapping found in the
	 * configuration for the database table name of that
	 * particular {@link java.io.File}.
	 * 
	 * @param file The {@link java.io.File} that will be processed
	 */
	boolean isFlatFileValid(File file);
	
	/**
	 * Retrieves the map for input directories to processed files
	 * directories
	 */
	Map<String, String> getInputDirectoriesToProcessedFilesDirectories();
	
	/**
	 * Retrieves the map for input directories to invalid files
	 * directories
	 */
	Map<String, String> getInputDirectoriesToInvalidFilesDirectories();
	
	/**
	 * Method used to close all resources relating to IO
	 * 
	 * @throws IOException When application encounters an io error on
	 * reading the file reports
	 */
	void shutdown() throws IOException;
}