package services.tman.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.tman.control.TMTAFileProcessor;

/**
 * This class is of type <code>QuartzJobBean</code> 
 * which overrides the executeInternal() method.
 * This method implements the scheduling functionality
 * 
 * @author Gerard
 * @version 1.0.0
 * @since March 2013
 */
public class FlatFileProcessorJob extends QuartzJobBean {
	private static final Logger logger = Logger.getLogger(FlatFileProcessorJob.class);
	
	private TMTAFileProcessor processor;
	private List<String> directories;
	private String processedFilesDirectory;
	private String invalidFilesDirectory;
	
	public FlatFileProcessorJob() { }

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		for(String directoryName : directories) {
			File input = new File(directoryName);
			String []flatFileNames = input.list();
			
			for(String flatFileName : flatFileNames) {
				File flatFile = new File(directoryName + "\\" + flatFileName);
				if(flatFile.isDirectory()) continue;
				if(isFileNotYetTransferredCompletely(flatFile)) continue;
				
				logger.info("Processing: " + flatFile.getName());
				
				processTMTAFlatFile(flatFile);
			}
		}
	}

	private boolean isFileNotYetTransferredCompletely(File flatFile) {
		String []fileNameSplittedArray = flatFile.getName().split("\\.");
		return fileNameSplittedArray[fileNameSplittedArray.length - 1].equals("TMP");
	}

	private void processTMTAFlatFile(File flatFile) {
		try {
			processor.initReader(flatFile);
			processor.process(flatFile);
			moveFlatFileToProcessedFilesDirectory(flatFile);
		} catch (IOException e) {
			try { moveFlatFileToInvalidFilesDirectory(flatFile);
			} catch (IOException e1) {}
			logger.error(e.getMessage());
		} catch (Exception e) {
			try { moveFlatFileToInvalidFilesDirectory(flatFile);
			} catch (IOException e1) {}
			logger.error(e.getMessage());
		} finally {
			logger.info("Processing Completed For: " + flatFile.getName());
			flatFile.delete();
		}
	}
	
	private void moveFlatFileToProcessedFilesDirectory(File flatFile) throws IOException {
	    moveFlatFileToDestination(flatFile, processedFilesDirectory);
	}
	
	private void moveFlatFileToInvalidFilesDirectory(File flatFile) throws IOException {
		moveFlatFileToDestination(flatFile, invalidFilesDirectory);
	}

	private void moveFlatFileToDestination(File flatFile,
			String destinationFolder) throws FileNotFoundException, IOException {
		InputStream inStream = new FileInputStream(flatFile);
		OutputStream outStream = new FileOutputStream(new File(destinationFolder + "\\" + flatFile.getName()));
		
		byte[] buffer = new byte[1024];
		int length;
	    while ((length = inStream.read(buffer)) > 0)
	    	outStream.write(buffer, 0, length);
	    
	    inStream.close();
	    outStream.close();
	}

	public void setProcessor(TMTAFileProcessor processor) {
		this.processor = processor;
	}

	public void setDirectories(List<String> directories) {
		this.directories = directories;
	}

	public void setProcessedFilesDirectory(String processedFilesDirectory) {
		this.processedFilesDirectory = processedFilesDirectory;
	}

	public void setInvalidFilesDirectory(String invalidFilesDirectory) {
		this.invalidFilesDirectory = invalidFilesDirectory;
	}
}